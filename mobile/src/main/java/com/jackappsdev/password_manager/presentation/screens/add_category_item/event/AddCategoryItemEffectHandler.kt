package com.jackappsdev.password_manager.presentation.screens.add_category_item.event

import androidx.navigation.NavController
import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.presentation.screens.add_category_item.constants.CREATED_CATEGORY
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AddCategoryItemEffectHandler(
    private val navController: NavController
) {

    fun onNavigateUp(model: CategoryModel?) {
        navController.navigateUp()

        model?.let {
            val savedState = navController.currentBackStackEntry?.savedStateHandle
            savedState?.set(CREATED_CATEGORY, Json.encodeToString(model))
        }
    }
}
