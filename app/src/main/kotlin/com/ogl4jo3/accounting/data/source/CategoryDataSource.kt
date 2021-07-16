package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.CategoryType

interface CategoryDataSource {
    suspend fun insertCategory(category: Category): Long
    suspend fun getCategoriesByType(categoryType: CategoryType): List<Category>
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)

    suspend fun getNumberOfCategories(categoryType: CategoryType): Int
    suspend fun hasDuplicatedName(name: String): Boolean
    suspend fun hasDuplicatedName(name: String, excludeId: String): Boolean

}