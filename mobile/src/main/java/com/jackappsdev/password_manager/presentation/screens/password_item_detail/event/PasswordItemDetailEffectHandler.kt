package com.jackappsdev.password_manager.presentation.screens.password_item_detail.event

import android.content.Context
import androidx.navigation.NavController
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.core.copyToClipboard
import com.jackappsdev.password_manager.core.launchUrl
import com.jackappsdev.password_manager.domain.mappers.toPasswordItemDto
import com.jackappsdev.password_manager.domain.model.PasswordWithCategoryModel
import com.jackappsdev.password_manager.shared.constants.DELETE_PASSWORD
import com.jackappsdev.password_manager.shared.constants.KEY_PASSWORD
import com.jackappsdev.password_manager.shared.constants.UPSERT_PASSWORD
import com.jackappsdev.password_manager.shared.core.showToast
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PasswordItemDetailEffectHandler(
    private val context: Context,
    private val navController: NavController,
    private val onEvent: (PasswordItemDetailUiEvent) -> Unit
) {

    private val dataClient = Wearable.getDataClient(context)

    fun onCopy(text: String?) {
        copyToClipboard(context, text)
    }

    fun onLaunchUrl(url: String) {
        launchUrl(context, url)
    }

    fun onDeleteItem(passwordItem: PasswordWithCategoryModel?) {
        passwordItem?.let { passwordCategoryModel ->
            onEvent(PasswordItemDetailUiEvent.ToggleDeleteDialogVisibility)

            if (passwordItem.isAddedToWatch == false) {
                navController.navigateUp()
                return
            }

            val putDataRequest = PutDataMapRequest.create(DELETE_PASSWORD).run {
                dataMap.putString(KEY_PASSWORD, Json.encodeToString(passwordCategoryModel.toPasswordItemDto()))
                setUrgent()
                asPutDataRequest()
            }

            dataClient.putDataItem(putDataRequest).addOnCompleteListener {
                navController.navigateUp()
            }
        }
    }

    fun onToggleIsAddedToWatch(passwordItem: PasswordWithCategoryModel?) {
        val path = if (passwordItem?.isAddedToWatch != true) {
            UPSERT_PASSWORD
        } else {
            DELETE_PASSWORD
        }

        val putDataRequest = PutDataMapRequest.create(path).run {
            dataMap.putString(KEY_PASSWORD, Json.encodeToString(passwordItem?.toPasswordItemDto()))
            setUrgent()
            asPutDataRequest()
        }

        dataClient.putDataItem(putDataRequest).addOnSuccessListener {
            context.showToast(
                if (passwordItem?.isAddedToWatch != true) {
                    context.getString(R.string.toast_added_to_watch)
                } else {
                    context.getString(R.string.toast_removed_from_watch)
                }
            )
        }
    }
}
