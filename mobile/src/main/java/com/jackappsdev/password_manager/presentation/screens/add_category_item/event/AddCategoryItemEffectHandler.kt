package com.jackappsdev.password_manager.presentation.screens.add_category_item.event

import com.jackappsdev.password_manager.domain.model.CategoryModel
import com.jackappsdev.password_manager.presentation.navigation.Navigator
import com.jackappsdev.password_manager.presentation.navigation.ResultEventBus

class AddCategoryItemEffectHandler(
    private val navigator: Navigator,
    private val resultEventBus: ResultEventBus
) {

    fun onNavigateUp(model: CategoryModel?) {
        navigator.navigateUp()

        model?.let {
            resultEventBus.sendResult<CategoryModel>(result = model)
        }
    }
}
