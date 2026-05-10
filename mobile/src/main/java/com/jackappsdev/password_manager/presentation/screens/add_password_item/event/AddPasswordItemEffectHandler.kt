package com.jackappsdev.password_manager.presentation.screens.add_password_item.event

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.ui.platform.SoftwareKeyboardController
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.navigation.Navigator
import com.jackappsdev.password_manager.presentation.navigation.Routes
import com.jackappsdev.password_manager.shared.core.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddPasswordItemEffectHandler(
    val navigator: Navigator,
    val keyboardController: SoftwareKeyboardController?,
    val scope: CoroutineScope
) {

    fun onNavigateToAddCategory() {
        navigator.navigate(Routes.AddCategoryItem)
    }

    fun onNavigateUp() {
        keyboardController?.hide()
        navigator.navigateUp()
    }

    fun onExportAttachmentToUri(context: Context, uri: Uri, bytes: ByteArray) {
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
        effect: AddPasswordItemUiEffect.OpenExportAttachmentIntent
    ) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = effect.mimeType
            putExtra(Intent.EXTRA_TITLE, effect.fileName)
        }
        launcher.launch(intent)
    }
}
