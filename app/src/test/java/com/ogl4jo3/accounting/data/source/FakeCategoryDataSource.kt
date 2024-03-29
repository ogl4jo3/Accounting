package com.ogl4jo3.accounting.data.source

import androidx.annotation.VisibleForTesting
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.CategoryType

class FakeCategoryDataSource(
    var categories: MutableList<Category> = mutableListOf()
) : CategoryDataSource {

    override suspend fun insertCategory(category: Category): Long {
        category.orderNumber = categories.filter { it.categoryType == category.categoryType }
            .maxOfOrNull { it.orderNumber }?.let { it + 1 } ?: 0
        return if (categories.find { it.name == category.name } != null) {
            -1
        } else {
            categories.add(category)
            0
        }
    }

    override suspend fun getCategoryById(categoryId: String): Category? {
        return categories.find { it.id == categoryId }
    }

    override suspend fun getCategoriesByType(categoryType: CategoryType): List<Category> {
        return categories.filter { it.categoryType == categoryType }.sortedBy { it.orderNumber }
    }

    override suspend fun updateCategory(category: Category) {
        categories.replaceAll { if (it.id == category.id) category else it }
    }

    override suspend fun deleteCategory(category: Category) {
        categories.remove(category)
    }

    override suspend fun getNumberOfCategories(categoryType: CategoryType): Int {
        return categories.filter { it.categoryType == categoryType }.size
    }

    override suspend fun hasDuplicatedName(name: String): Boolean {
        return categories.any { it.name == name }
    }

    override suspend fun hasDuplicatedName(name: String, excludeId: String): Boolean {
        return categories.any { it.name == name && it.id != excludeId }
    }

    override suspend fun swapCategoryOrderNumber(fromCategory: Category, toCategory: Category) {
        categories.find { it.id == fromCategory.id }?.orderNumber = toCategory.orderNumber
        categories.find { it.id == toCategory.id }?.orderNumber = fromCategory.orderNumber
    }

    @VisibleForTesting
    fun getCategory(index: Int): Category {
        return categories[index].copy()
    }
}