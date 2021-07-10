package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.data.Category

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

    //    override suspend fun updateAccount(account: Account) {
//        categories.replaceAll { if (it.id == account.id) account else it }
//    }
//
//    override suspend fun resetDefaultAccountExceptId(defaultAccountId: String) {
//        categories.filter { it.id != defaultAccountId }.forEach { it.isDefaultAccount = false }
//    }
//
//    override suspend fun deleteAccount(account: Account) {
//        categories.remove(account)
//    }
//
//    override suspend fun getAccountById(accountId: String): Account? {
//        return categories.find { it.id == accountId }
//    }
//
//    override suspend fun getAllAccounts(): List<Account> {
//        return categories
//    }
//
//    override suspend fun getNumberOfAccounts(): Int {
//        return categories.size
//    }
//
    override suspend fun hasDuplicatedName(name: String): Boolean {
        return categories.any { it.name == name }
    }
//
//    override suspend fun hasDuplicatedName(name: String, excludeId: String): Boolean {
//        return categories.any { it.name == name && it.id != excludeId }
//    }
//
//    override suspend fun hasDefaultAccount(excludeId: String): Boolean {
//        return categories.any { it.isDefaultAccount && it.id != excludeId }
//    }
//
//    override suspend fun getDefaultAccount(): Account? {
//        return categories.find { it.isDefaultAccount }?.copy()
//    }
//
//    override suspend fun setDefaultAccount(id: String) {
//        categories.find { it.id == id }?.isDefaultAccount = true
//    }
}