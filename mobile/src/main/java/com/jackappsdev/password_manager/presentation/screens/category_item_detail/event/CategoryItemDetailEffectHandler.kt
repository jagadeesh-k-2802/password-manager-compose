package com.jackappsdev.password_manager.presentation.screens.category_item_detail.event

class CategoryItemDetailEffectHandler(
    private val navigateUp: () -> Unit
) {

    fun onNavigateUp() {
        navigateUp()
    }
}
