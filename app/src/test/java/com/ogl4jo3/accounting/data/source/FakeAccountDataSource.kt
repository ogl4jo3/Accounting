package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.data.Account

class FakeAccountDataSource(
    var accounts: MutableList<Account> = mutableListOf()
) : AccountDataSource {
    override suspend fun insertAccount(account: Account): Long {
        return if (accounts.find { it.name == account.name } != null) {
            -1
        } else {
            accounts.add(account)
            0
        }
    }

    override suspend fun updateDefaultAccount(defaultAccountId: String) {
        accounts.filter { it.id != defaultAccountId }.forEach { it.isDefaultAccount = false }
    }
}