package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.CategoryType

class DefaultCategoryDataSource(
    val categoryDao: CategoryDao
) : CategoryDataSource {

    override suspend fun insertCategory(category: Category): Long {
        category.orderNumber =
            categoryDao.getMaxOrderNumber(category.categoryType)?.let { it + 1 } ?: 0
        return categoryDao.insertCategory(category)
    }

    override suspend fun getCategoriesByType(categoryType: CategoryType): List<Category> {
        return categoryDao.getCategoriesByType(categoryType)
    }

    override suspend fun updateCategory(category: Category) {
        categoryDao.updateCategory(category)
    }

    override suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }

    override suspend fun getNumberOfCategories(categoryType: CategoryType): Int {
        return categoryDao.getNumberOfCategories(categoryType)
    }

    override suspend fun hasDuplicatedName(name: String): Boolean {
        return categoryDao.getNumberOfCategoriesByName(name) > 0
    }

    override suspend fun hasDuplicatedName(name: String, excludeId: String): Boolean {
        return categoryDao.getNumberOfCategoriesByName(name, excludeId) > 0
    }

    override suspend fun swapCategoryOrderNumber(fromCategory: Category, toCategory: Category) {
        val fromOrderNumber = fromCategory.orderNumber
        val toOrderNumber = toCategory.orderNumber
        fromCategory.orderNumber = toOrderNumber
        toCategory.orderNumber = fromOrderNumber
        categoryDao.updateCategory(fromCategory)
        categoryDao.updateCategory(toCategory)
    }

}