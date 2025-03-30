package com.jackappsdev.password_manager.core

import android.content.Context
import android.widget.Toast
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataMap
import com.google.android.gms.wearable.DataMapItem

/**
 * Extension function to get a [DataMap] from [DataEvent].
 */
fun DataEvent.getDataMap(): DataMap {
    return DataMapItem.fromDataItem(dataItem).dataMap
}

/**
 * Extension function to show a short toast
 */
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
