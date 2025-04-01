package com.jackappsdev.password_manager.presentation.screens.manage_categories.event

import androidx.compose.foundation.lazy.LazyListState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ManageCategoriesEffectHandler(
    private val scope: CoroutineScope
) {
    fun onScrollToTop(state: LazyListState) {
        scope.launch {
            state.animateScrollToItem(0)
        }
    }
}
