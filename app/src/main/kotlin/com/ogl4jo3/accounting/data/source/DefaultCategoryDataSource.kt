package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.AccountingApplication
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.CategoryType
import kotlinx.coroutines.runBlocking

class DefaultCategoryDataSource(
    private val defaultCategories: List<Category> = AccountingApplication.defaultCategories,
    val categoryDao: CategoryDao = AccountingApplication.database.categoryDao()
) : CategoryDataSource {
    init {
        runBlocking {
            if (getNumberOfCategories(CategoryType.Expense) <= 0) {
                defaultCategories.filter { it.categoryType == CategoryType.Expense }
                    .forEach { insertCategory(it) }
            }
            if (getNumberOfCategories(CategoryType.Income) <= 0) {
                defaultCategories.filter { it.categoryType == CategoryType.Income }
                    .forEach { insertCategory(it) }
            }
        }
    }

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

    //    override suspend fun resetDefaultAccountExceptId(defaultAccountId: String) {
//        categoryDao.resetDefaultAccountExceptId(defaultAccountId)
//    }

    override suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }

    //    override suspend fun getAccountById(accountId: String): Account? {
//        return categoryDao.getAccountById(accountId)
//    }
//
//    override suspend fun getAllAccounts(): List<Account> {
//        return categoryDao.getAllAccounts()
//    }
//
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

    //    override suspend fun hasDefaultAccount(excludeId: String): Boolean {
//        return categoryDao.hasDefaultAccount(excludeId) > 0
//    }
//
//    override suspend fun getDefaultAccount(): Account? {
//        return categoryDao.getDefaultAccount()
//    }
//
//    override suspend fun setDefaultAccount(id: String) {
//        return categoryDao.setDefaultAccount(id)
//    }

}