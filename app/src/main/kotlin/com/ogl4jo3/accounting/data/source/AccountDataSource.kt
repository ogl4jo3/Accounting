package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.data.Account

interface AccountDataSource {
    suspend fun insertAccount(account: Account): Long
    suspend fun updateAccount(account: Account)
    suspend fun resetDefaultAccountExceptId(defaultAccountId: String)
    suspend fun deleteAccount(account: Account)
    suspend fun getAccountById(accountId: String): Account?
    suspend fun getAllAccounts(): List<Account>
    suspend fun getNumberOfAccounts(): Int
    suspend fun hasDuplicatedName(name: String): Boolean
    suspend fun hasDuplicatedName(name: String, excludeId: String): Boolean
    suspend fun hasDefaultAccount(excludeId: String): Boolean
    suspend fun getDefaultAccount(): Account?
    suspend fun setDefaultAccount(id: String)
}