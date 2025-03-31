package com.jackappsdev.password_manager.domain.model

import com.jackappsdev.password_manager.data.local.entity.PasswordItemEntity

enum class SortBy {
    ALPHABET_ASCENDING,
    ALPHABET_DESCENDING,
    NEWEST,
    OLDEST
}

/**
 * The string mapping SHOULD match the column names of [PasswordItemEntity]
 * else will result in Runtime Error
 */
fun SortBy.orderBy(): String {
    return when (this) {
        SortBy.ALPHABET_ASCENDING -> "name ASC"
        SortBy.ALPHABET_DESCENDING -> "name DESC"
        SortBy.NEWEST -> "created_at DESC"
        SortBy.OLDEST -> "created_at ASC"
    }
}
