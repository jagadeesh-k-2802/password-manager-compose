package com.jackappsdev.password_manager.presentation.screens.edit_password_item.event

import android.content.Context
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.navigation.NavController
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.jackappsdev.password_manager.domain.mappers.toPasswordItemDto
import com.jackappsdev.password_manager.domain.model.PasswordWithCategoryModel
import com.jackappsdev.password_manager.shared.constants.KEY_PASSWORD
import com.jackappsdev.password_manager.shared.constants.UPSERT_PASSWORD
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class EditPasswordItemEffectHandler(
    context: Context,
    private val navController: NavController,
    private val keyboardController: SoftwareKeyboardController?
) {

    private val dataClient = Wearable.getDataClient(context)

    fun onEditComplete(newPasswordItemModel: PasswordWithCategoryModel?) {
        keyboardController?.hide()

        if (newPasswordItemModel?.isAddedToWatch == false) {
            navController.navigateUp()
            return
        }

        val putDataRequest = PutDataMapRequest.create(UPSERT_PASSWORD).run {
            dataMap.putString(KEY_PASSWORD, Json.encodeToString(newPasswordItemModel?.toPasswordItemDto()))
            setUrgent()
            asPutDataRequest()
        }

        dataClient.putDataItem(putDataRequest).addOnCompleteListener {
            navController.navigateUp()
        }
    }
}
