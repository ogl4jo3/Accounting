package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.data.Account

interface AccountDataSource {
    suspend fun getNumberOfAccounts(): Int
    suspend fun getAllAccounts(): List<Account>
    suspend fun insertAccount(account: Account): Long
    suspend fun updateDefaultAccount(defaultAccountId: String)
}