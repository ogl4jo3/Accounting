package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.AccountingApplication
import com.ogl4jo3.accounting.data.Account

class DefaultAccountDataSource(
    val accountDao: AccountDao = AccountingApplication.database.accountDao()
) : AccountDataSource {
    override suspend fun insertAccount(account: Account): Long {
        return accountDao.insertAccount(account)
    }

    override suspend fun updateDefaultAccount(defaultAccountId: String) {
        accountDao.updateDefaultAccount(defaultAccountId)
    }
}