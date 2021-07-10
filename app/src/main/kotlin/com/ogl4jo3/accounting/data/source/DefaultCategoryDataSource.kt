package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.AccountingApplication
import com.ogl4jo3.accounting.data.Category

class DefaultCategoryDataSource(
//    private val defaultAccounts: List<Category> = AccountingApplication.defaultAccounts,//TODO: add default category
    val categoryDao: CategoryDao = AccountingApplication.database.categoryDao()
) : CategoryDataSource {
//    init {
//        runBlocking {
//            if (getNumberOfAccounts() <= 0) {
//                defaultAccounts.forEach { insertAccount(it) }
//            }
//        }
//    }

    override suspend fun insertCategory(category: Category): Long {
        category.orderNumber =
            categoryDao.getMaxOrderNumber(category.categoryType)?.let { it + 1 } ?: 0
        return categoryDao.insertCategory(category)
    }

    //    override suspend fun updateAccount(account: Account) {
//        categoryDao.updateAccount(account)
//    }
//
//    override suspend fun resetDefaultAccountExceptId(defaultAccountId: String) {
//        categoryDao.resetDefaultAccountExceptId(defaultAccountId)
//    }
//
//    override suspend fun deleteAccount(account: Account) {
//        categoryDao.deleteAccount(account)
//    }
//
//    override suspend fun getAccountById(accountId: String): Account? {
//        return categoryDao.getAccountById(accountId)
//    }
//
//    override suspend fun getAllAccounts(): List<Account> {
//        return categoryDao.getAllAccounts()
//    }
//
//    override suspend fun getNumberOfAccounts(): Int {
//        return categoryDao.getNumberOfAccounts()
//    }
//
    override suspend fun hasDuplicatedName(name: String): Boolean {
        return categoryDao.getNumberOfCategoriesByName(name) > 0
    }
//
//    override suspend fun hasDuplicatedName(name: String, excludeId: String): Boolean {
//        return categoryDao.getNumberOfAccountsByName(name, excludeId) > 0
//    }
//
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