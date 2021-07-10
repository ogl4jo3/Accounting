package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.data.Category

interface CategoryDataSource {
    suspend fun insertCategory(category: Category): Long

    suspend fun hasDuplicatedName(name: String): Boolean
}