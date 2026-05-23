package com.jackappsdev.password_manager.presentation.utils

import android.content.Context
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.jackappsdev.password_manager.domain.mappers.toPasswordItemDto
import com.jackappsdev.password_manager.domain.model.PasswordWithCategoryModel
import com.jackappsdev.password_manager.shared.constants.KEY_PASSWORD
import com.jackappsdev.password_manager.shared.core.VersionTracker
import kotlinx.serialization.json.Json

class WearPasswordManager(context: Context) {
    private val dataClient = Wearable.getDataClient(context)

    fun sendPasswordToWatch(
        path: String,
        passwordItem: PasswordWithCategoryModel?,
        onComplete: (Boolean) -> Unit
    ) {
        if (passwordItem == null) {
            onComplete(false)
            return
        }

        val putDataRequest = PutDataMapRequest.create(path).run {
            val wearOsVersion = VersionTracker.otherDeviceVersion
            val timestamp = if (VersionTracker.isVersionAtLeast(wearOsVersion, "1.7.0")) {
                System.currentTimeMillis().toString()
            } else {
                null
            }
            dataMap.putString(KEY_PASSWORD, Json.encodeToString(passwordItem.toPasswordItemDto(timestamp)))
            setUrgent()
            asPutDataRequest()
        }

        dataClient.putDataItem(putDataRequest)
            .addOnCompleteListener { task -> onComplete(task.isSuccessful) }
    }
}
