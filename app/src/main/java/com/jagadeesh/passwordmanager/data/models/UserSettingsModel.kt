package com.jagadeesh.passwordmanager.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserSettings(
    val password: String? = null,
    val useScreenLockToUnlock: Boolean = true
)
