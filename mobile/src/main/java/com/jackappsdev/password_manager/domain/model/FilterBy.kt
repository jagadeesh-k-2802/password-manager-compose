package com.jackappsdev.password_manager.domain.model

import com.jackappsdev.password_manager.data.local.entity.PasswordItemEntity

sealed interface FilterBy {
    data object All : FilterBy
    data class Category(val categoryId: Int) : FilterBy
    data object NoCategoryItems : FilterBy
}

/**
 * The string mapping SHOULD match the column names of [PasswordItemEntity]
 * else will result in Runtime Error
 */
fun FilterBy.where(): String {
    return when(this) {
        FilterBy.All -> ""
        is FilterBy.Category -> "category_id = ${this.categoryId}"
        FilterBy.NoCategoryItems -> "category_id IS NULL"
    }
}
