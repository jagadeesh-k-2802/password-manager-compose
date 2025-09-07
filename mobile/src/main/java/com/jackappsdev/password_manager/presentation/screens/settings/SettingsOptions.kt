package com.jackappsdev.password_manager.presentation.screens.settings

import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.presentation.components.OptionItem

object SettingsOptions {
    val values = listOf(
        OptionItem(R.string.label_auto_lock_30_seconds, 30_000L),
        OptionItem(R.string.label_auto_lock_1_minute, 60_000L),
        OptionItem(R.string.label_auto_lock_2_minutes, 120_000L),
        OptionItem(R.string.label_auto_lock_5_minutes, 300_000L)
    )
}
