package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.data.Account

class DefaultAccountDataSource(
    val accountDao: AccountDao
) : AccountDataSource {

    override suspend fun insertAccount(account: Account): Long {
        return accountDao.insertAccount(account)
    }

    override suspend fun updateAccount(account: Account) {
        accountDao.updateAccount(account)
    }

    override suspend fun resetDefaultAccountExceptId(defaultAccountId: String) {
        accountDao.resetDefaultAccountExceptId(defaultAccountId)
    }

    override suspend fun deleteAccount(account: Account) {
        accountDao.deleteAccount(account)
    }

    override suspend fun getAccountById(accountId: String): Account? {
        return accountDao.getAccountById(accountId)
    }

    override suspend fun getAllAccounts(): List<Account> {
        return accountDao.getAllAccounts()
    }

    override suspend fun getNumberOfAccounts(): Int {
        return accountDao.getNumberOfAccounts()
    }

    override suspend fun hasDuplicatedName(name: String): Boolean {
        return accountDao.getNumberOfAccountsByName(name) > 0
    }

    override suspend fun hasDuplicatedName(name: String, excludeId: String): Boolean {
        return accountDao.getNumberOfAccountsByName(name, excludeId) > 0
    }

    override suspend fun hasDefaultAccount(excludeId: String): Boolean {
        return accountDao.hasDefaultAccount(excludeId) > 0
    }

    override suspend fun getDefaultAccount(): Account? {
        return accountDao.getDefaultAccount()
    }

    override suspend fun setDefaultAccount(id: String) {
        return accountDao.setDefaultAccount(id)
    }

}