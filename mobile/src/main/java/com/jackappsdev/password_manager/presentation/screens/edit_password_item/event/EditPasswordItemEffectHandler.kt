package com.jackappsdev.password_manager.presentation.screens.edit_password_item.event

import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.ui.platform.SoftwareKeyboardController
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.domain.mappers.toPasswordItemDto
import com.jackappsdev.password_manager.domain.model.PasswordWithCategoryModel
import com.jackappsdev.password_manager.shared.constants.KEY_PASSWORD
import com.jackappsdev.password_manager.shared.constants.UPSERT_PASSWORD
import com.jackappsdev.password_manager.shared.core.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class EditPasswordItemEffectHandler(
    context: Context,
    private val keyboardController: SoftwareKeyboardController?,
    private val navigateToAddCategory: () -> Unit,
    private val navigateUp: () -> Unit,
    private val scope: CoroutineScope
) {

    private val dataClient = Wearable.getDataClient(context)

    fun onEditComplete(newPasswordItemModel: PasswordWithCategoryModel?) {
        keyboardController?.hide()

        if (newPasswordItemModel?.isAddedToWatch == false) {
            navigateUp()
            return
        }

        val putDataRequest = PutDataMapRequest.create(UPSERT_PASSWORD).run {
            dataMap.putString(KEY_PASSWORD, Json.encodeToString(newPasswordItemModel?.toPasswordItemDto()))
            setUrgent()
            asPutDataRequest()
        }

        dataClient.putDataItem(putDataRequest).addOnCompleteListener {
            navigateUp()
        }
    }

    fun onNavigateToAddCategory() {
        navigateToAddCategory()
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
        effect: EditPasswordItemUiEffect.OpenExportAttachmentIntent
    ) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = effect.mimeType
            putExtra(Intent.EXTRA_TITLE, effect.fileName)
        }
        launcher.launch(intent)
    }
}
