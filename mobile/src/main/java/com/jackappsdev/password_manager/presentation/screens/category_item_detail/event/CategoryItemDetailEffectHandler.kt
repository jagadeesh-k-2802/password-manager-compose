package com.jackappsdev.password_manager.presentation.screens.category_item_detail.event

import com.jackappsdev.password_manager.presentation.navigation.Navigator

class CategoryItemDetailEffectHandler(
    private val navigator: Navigator
) {

    fun onNavigateUp() {
        navigator.navigateUp()
    }
}
