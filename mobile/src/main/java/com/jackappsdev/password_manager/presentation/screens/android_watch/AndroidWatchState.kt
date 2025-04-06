package com.jackappsdev.password_manager.presentation.screens.android_watch

import com.jackappsdev.password_manager.shared.constants.EMPTY_STRING

data class AndroidWatchState(
    val useAndroidWatch: Boolean? = null,
    val hasAndroidWatchPinSet: Boolean? = null,
    val pin: String = EMPTY_STRING,
    val showPin: Boolean = false,
    val showDisableAndroidWatchDialog: Boolean = false
)
