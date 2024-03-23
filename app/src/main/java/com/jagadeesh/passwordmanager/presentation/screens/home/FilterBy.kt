package com.jagadeesh.passwordmanager.presentation.screens.home

sealed interface FilterBy {
    data object All : FilterBy
    data class Category(val categoryId: Int) : FilterBy
    data object NoCategoryItems : FilterBy
}

fun FilterBy.where(): String {
    return when(this) {
        FilterBy.All -> ""
        is FilterBy.Category -> "category_id = ${this.categoryId}"
        FilterBy.NoCategoryItems -> "category_id IS NULL"
    }
}
