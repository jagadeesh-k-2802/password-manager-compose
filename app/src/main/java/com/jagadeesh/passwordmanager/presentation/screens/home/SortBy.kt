package com.jagadeesh.passwordmanager.presentation.screens.home

enum class SortBy {
    ALPHABET_ASCENDING,
    ALPHABET_DESCENDING,
    NEWEST,
    OLDEST
}

fun SortBy.orderBy(): String {
    return when (this) {
        SortBy.ALPHABET_ASCENDING -> "name ASC"
        SortBy.ALPHABET_DESCENDING -> "name DESC"
        SortBy.NEWEST -> "timestamp ASC"
        SortBy.OLDEST -> "timestamp DESC"
    }
}
