package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.AccountingApplication
import com.ogl4jo3.accounting.data.Account
import kotlinx.coroutines.runBlocking

class DefaultAccountDataSource(
    private val defaultAccounts: List<Account> = AccountingApplication.defaultAccounts,
    val accountDao: AccountDao = AccountingApplication.database.accountDao()
) : AccountDataSource {
    init {
        runBlocking {
            if (getNumberOfAccounts() <= 0) {
                defaultAccounts.forEach { insertAccount(it) }
            }
        }
    }

    override suspend fun getNumberOfAccounts(): Int {
        return accountDao.getNumberOfAccounts()
    }

    override suspend fun getAllAccounts(): List<Account> {
        return accountDao.getAllAccounts()
    }

    override suspend fun insertAccount(account: Account): Long {
        return accountDao.insertAccount(account)
    }

    override suspend fun updateDefaultAccount(defaultAccountId: String) {
        accountDao.updateDefaultAccount(defaultAccountId)
    }
}