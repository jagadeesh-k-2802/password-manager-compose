package com.jackappsdev.password_manager.core

import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataMap
import com.google.android.gms.wearable.DataMapItem

/**
 * Extension function to get a [DataMap] from [DataEvent].
 */
fun DataEvent.getDataMap(): DataMap {
    return DataMapItem.fromDataItem(dataItem).dataMap
}
