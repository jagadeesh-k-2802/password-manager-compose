package com.jackappsdev.password_manager.autofill

import android.app.PendingIntent
import android.app.assist.AssistStructure
import android.content.Intent
import android.os.Build
import android.os.CancellationSignal
import android.service.autofill.AutofillService
import android.service.autofill.Dataset
import android.service.autofill.FillCallback
import android.service.autofill.FillRequest
import android.service.autofill.FillResponse
import android.service.autofill.SaveCallback
import android.service.autofill.SaveRequest
import android.view.View
import android.view.autofill.AutofillId
import android.view.autofill.AutofillValue
import android.widget.RemoteViews
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import com.jackappsdev.password_manager.shared.constants.EMPTY_STRING
import com.jackappsdev.password_manager.shared.core.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * AutofillService implementation for Password Manager.
 *
 * This service does not directly expose passwords. Instead, it provides an authenticated
 * flow via [AutofillActivity] where the user unlocks the vault and selects which
 * credentials to use for autofill.
 */
@AndroidEntryPoint
class AutofillService : AutofillService() {

    @Inject
    lateinit var passwordItemRepository: PasswordItemRepository

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onFillRequest(
        request: FillRequest,
        cancellationSignal: CancellationSignal,
        callback: FillCallback
    ) {
        val fillContext = request.fillContexts.lastOrNull()

        // If there's no fill context, we can't proceed.
        if (fillContext == null) {
            callback.onSuccess(null)
            return
        }

        val structure = fillContext.structure
        val (usernameId, passwordId) = extractAutofillIds(structure)

        // If we couldn't find any suitable fields, bail out.
        if (usernameId == null || passwordId == null) {
            callback.onSuccess(null)
            return
        }

        val intent = Intent(this, AutofillActivity::class.java).apply {
            putExtra(AutofillActivity.EXTRA_USERNAME_ID, usernameId)
            putExtra(AutofillActivity.EXTRA_PASSWORD_ID, passwordId)
            putExtra(AutofillActivity.EXTRA_CLIENT_STATE, request.clientState)
        }

        val flags = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_MUTABLE
            }

            else -> {
                PendingIntent.FLAG_CANCEL_CURRENT
            }
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, flags)

        val remoteView = RemoteViews(packageName, R.layout.autofill_auth_presentation).apply {
            setTextViewText(
                R.id.autofill_auth_title,
                getString(R.string.text_autofill_open_password_manager)
            )
        }

        val idsToAuthenticate = arrayOf(usernameId, passwordId)

        @Suppress("DEPRECATION")
        val response = FillResponse.Builder()
            .setAuthentication(idsToAuthenticate, pendingIntent.intentSender, remoteView)
            .build()

        callback.onSuccess(response)
    }

    override fun onSaveRequest(request: SaveRequest, callback: SaveCallback) {
        val fillContext = request.fillContexts.lastOrNull()

        // If there's no context, there's nothing to save.
        if (fillContext == null) {
            callback.onSuccess()
            return
        }

        val structure = fillContext.structure
        val saveData = extractSaveData(structure)

        // Basic validation for username & password.
        if (saveData == null ||
            saveData.username.isBlank() ||
            saveData.password.isBlank()
        ) {
            callback.onFailure(applicationContext.getString(R.string.text_autofill_save_fail))
            return
        }

        serviceScope.launch {
            try {
                val item = PasswordItemModel(
                    name = saveData.appName,
                    username = saveData.username,
                    password = saveData.password,
                    notes = EMPTY_STRING,
                    website = EMPTY_STRING,
                    isAddedToWatch = false
                )

                passwordItemRepository.upsertPasswordItem(item)

                withContext(Dispatchers.Main) {
                    showToast(applicationContext.getString(R.string.toast_password_saved, saveData.appName))
                }

                callback.onSuccess()
            } catch (_: Exception) {
                callback.onFailure(applicationContext.getString(R.string.text_autofill_save_fail_generic))
            }
        }
    }

    private fun extractAutofillIds(structure: AssistStructure): AutofillFieldIds {
        var usernameId: AutofillId? = null
        var passwordId: AutofillId? = null
        val windowCount = structure.windowNodeCount

        for (i in 0 until windowCount) {
            val windowNode = structure.getWindowNodeAt(i)
            val rootViewNode = windowNode.rootViewNode
            traverseViewNode(rootViewNode) { node ->
                val hints = node.autofillHints
                if (hints != null) {
                    for (hint in hints) {
                        when (hint) {
                            View.AUTOFILL_HINT_USERNAME,
                            View.AUTOFILL_HINT_EMAIL_ADDRESS, -> if (usernameId == null) {
                                usernameId = node.autofillId
                            }

                            View.AUTOFILL_HINT_PASSWORD -> if (passwordId == null) {
                                passwordId = node.autofillId
                            }
                        }
                    }
                }

                if (usernameId != null && passwordId != null) return@traverseViewNode
            }
        }

        return AutofillFieldIds(usernameId, passwordId)
    }

    private fun traverseViewNode(
        node: AssistStructure.ViewNode,
        onNode: (AssistStructure.ViewNode) -> Unit
    ) {
        onNode(node)
        val childrenSize = node.childCount
        for (i in 0 until childrenSize) {
            traverseViewNode(node.getChildAt(i), onNode)
        }
    }

    private fun extractSaveData(structure: AssistStructure): AutofillSaveData? {
        var username: String? = null
        var password: String? = null

        val windowCount = structure.windowNodeCount
        for (i in 0 until windowCount) {
            val windowNode = structure.getWindowNodeAt(i)
            val rootViewNode = windowNode.rootViewNode
            traverseViewNode(rootViewNode) { node ->
                val hints = node.autofillHints ?: return@traverseViewNode
                val value = node.autofillValue ?: return@traverseViewNode

                if (!value.isText) return@traverseViewNode
                val text = value.textValue.toString()
                if (text.isEmpty()) return@traverseViewNode

                for (hint in hints) {
                    when (hint) {
                        View.AUTOFILL_HINT_USERNAME,
                        View.AUTOFILL_HINT_EMAIL_ADDRESS -> if (username.isNullOrEmpty()) {
                            username = text
                        }

                        View.AUTOFILL_HINT_PASSWORD -> if (password.isNullOrEmpty()) {
                            password = text
                        }
                    }
                }

                if (!username.isNullOrEmpty() && !password.isNullOrEmpty()) {
                    return@traverseViewNode
                }
            }
        }

        if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
            return null
        }

        val component = structure.activityComponent
        val appName = if (component != null) {
            val pm = applicationContext.packageManager
            try {
                val appInfo = pm.getApplicationInfo(component.packageName, 0)
                pm.getApplicationLabel(appInfo).toString()
            } catch (_: Exception) {
                component.packageName
            }
        } else {
            return null
        }

        return AutofillSaveData(
            appName = appName,
            username = username,
            password = password
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    private data class AutofillFieldIds(
        val usernameId: AutofillId?,
        val passwordId: AutofillId?
    )

    private data class AutofillSaveData(
        val appName: String,
        val username: String,
        val password: String
    )

    companion object {
        /**
         * Utility to build a [FillResponse] with a single dataset populated with the given
         * credentials and target ids. This is used by [AutofillActivity] when the user
         * selects an item after unlocking the vault.
         */
        fun buildFillResponseForSelection(
            packageName: String,
            label: String,
            usernameId: AutofillId?,
            passwordId: AutofillId?,
            username: String,
            password: String
        ): FillResponse? {
            if (usernameId == null && passwordId == null) return null

            val remoteView = RemoteViews(packageName, R.layout.autofill_dataset_presentation).apply {
                setTextViewText(R.id.autofill_dataset_label, label)
            }

            @Suppress("DEPRECATION")
            val datasetBuilder = Dataset.Builder(remoteView)

            @Suppress("DEPRECATION")
            usernameId?.let {
                datasetBuilder.setValue(it, AutofillValue.forText(username), null)
            }

            @Suppress("DEPRECATION")
            passwordId?.let {
                datasetBuilder.setValue(it, AutofillValue.forText(password), null)
            }

            val dataset = datasetBuilder.build()

            return FillResponse.Builder()
                .addDataset(dataset)
                .build()
        }
    }
}
