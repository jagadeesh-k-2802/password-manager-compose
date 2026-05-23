package com.jackappsdev.password_manager.presentation.screens.password_item_detail.event

import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.core.copyToClipboard
import com.jackappsdev.password_manager.core.launchUrl
import com.jackappsdev.password_manager.domain.model.PasswordWithCategoryModel
import com.jackappsdev.password_manager.presentation.utils.WearPasswordManager
import com.jackappsdev.password_manager.shared.constants.DELETE_PASSWORD
import com.jackappsdev.password_manager.shared.constants.UPSERT_PASSWORD
import com.jackappsdev.password_manager.shared.core.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PasswordItemDetailEffectHandler(
    private val context: Context,
    private val onEvent: (PasswordItemDetailUiEvent) -> Unit,
    private val navigateToEditPassword: (Int) -> Unit,
    private val navigateUp: () -> Unit,
    private val scope: kotlinx.coroutines.CoroutineScope
) {

    private val wearPasswordManager = WearPasswordManager(context)

    fun onToggleAddedToWatch(passwordItem: PasswordWithCategoryModel?) {
        val path = if (passwordItem?.isAddedToWatch != true) {
            UPSERT_PASSWORD
        } else {
            DELETE_PASSWORD
        }

        wearPasswordManager.sendPasswordToWatch(path, passwordItem) { success ->
            if (success) {
                context.showToast(
                    if (passwordItem?.isAddedToWatch != true) {
                        context.getString(R.string.toast_added_to_watch)
                    } else {
                        context.getString(R.string.toast_removed_from_watch)
                    }
                )
                onEvent(PasswordItemDetailUiEvent.ToggleAddToWatch)
            }
        }
    }

    fun onDeleteItem(passwordItem: PasswordWithCategoryModel?) {
        onEvent(PasswordItemDetailUiEvent.ToggleDeleteDialogVisibility)

        if (passwordItem?.isAddedToWatch != true) {
            onEvent(PasswordItemDetailUiEvent.DeleteItem)
            navigateUp()
            return
        }

        wearPasswordManager.sendPasswordToWatch(DELETE_PASSWORD, passwordItem) {
            onEvent(PasswordItemDetailUiEvent.DeleteItem)
            navigateUp()
        }
    }

    fun onCopy(text: String?) {
        copyToClipboard(context, text)
    }

    fun onLaunchUrl(url: String) {
        launchUrl(context, url)
    }

    fun onNavigateToEditPassword(id: Int) {
        navigateToEditPassword(id)
    }

    fun onNavigateUp() {
        navigateUp()
    }

    fun onExportAttachmentToUri(context: Context, uri: android.net.Uri, bytes: ByteArray) {
        scope.launch(Dispatchers.IO) {
            runCatching {
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    outputStream.write(bytes)
                }
            }.onSuccess {
                withContext(Dispatchers.Main) {
                    context.showToast(context.getString(R.string.toast_attachment_exported))
                }
            }
        }
    }

    fun onOpenExportAttachmentIntent(
        launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
        effect: PasswordItemDetailUiEffect.OpenExportAttachmentIntent
    ) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = effect.mimeType
            putExtra(Intent.EXTRA_TITLE, effect.fileName)
        }
        launcher.launch(intent)
    }
}
