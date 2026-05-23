package com.jackappsdev.password_manager.shared.core

import android.content.Context
import com.jackappsdev.password_manager.shared.constants.ZERO

/**
 * Singleton object to track the version of the app on the other device (Mobile or Wear OS).
 * This is used to determine if certain features are supported based on the version of the app
 */
object VersionTracker {
    @Volatile
    var otherDeviceVersion: String? = null

    fun isVersionAtLeast(version: String?, minVersion: String): Boolean {
        if (version == null) return false
        val parts = version.split(".")
        val minParts = minVersion.split(".")
        for (i in 0 until maxOf(parts.size, minParts.size)) {
            val part = parts.getOrNull(i)?.toIntOrNull() ?: ZERO
            val minPart = minParts.getOrNull(i)?.toIntOrNull() ?: ZERO
            if (part > minPart) return true
            if (part < minPart) return false
        }
        return true
    }

    fun getAppVersionName(context: Context): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, ZERO)
            packageInfo.versionName ?: "1.0.0"
        } catch (_: Exception) {
            "1.0.0"
        }
    }
}
