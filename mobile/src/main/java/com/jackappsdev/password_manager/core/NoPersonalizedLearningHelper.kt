package com.jackappsdev.password_manager.core

import android.view.inputmethod.EditorInfo
import androidx.annotation.DoNotInline

/**
 * Helper to add the IME_FLAG_NO_PERSONALIZED_LEARNING flag to EditorInfo
 * This helps to turn on incognito mode for text fields, preventing the keyboard from learning
 */
internal object NoPersonalizedLearningHelper {
    @DoNotInline
    fun addNoPersonalizedLearning(info: EditorInfo) {
        info.imeOptions = info.imeOptions or EditorInfo.IME_FLAG_NO_PERSONALIZED_LEARNING
    }
}
