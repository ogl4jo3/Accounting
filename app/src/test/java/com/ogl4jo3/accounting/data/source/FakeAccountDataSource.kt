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

    override suspend fun updateAccount(account: Account) {
        accounts.replaceAll { if (it.id == account.id) account else it }
    }

    override suspend fun resetDefaultAccountExceptId(defaultAccountId: String) {
        accounts.filter { it.id != defaultAccountId }.forEach { it.isDefaultAccount = false }
    }

    override suspend fun deleteAccount(account: Account) {
        accounts.remove(account)
    }

    override suspend fun getAccountById(accountId: String): Account? {
        return accounts.find { it.id == accountId }
    }

    override suspend fun getAllAccounts(): List<Account> {
        return accounts
    }

    override suspend fun getNumberOfAccounts(): Int {
        return accounts.size
    }

    override suspend fun hasDuplicatedName(name: String): Boolean {
        return accounts.any { it.name == name }
    }

    override suspend fun hasDuplicatedName(name: String, excludeId: String): Boolean {
        return accounts.any { it.name == name && it.id != excludeId }
    }

    override suspend fun hasDefaultAccount(excludeId: String): Boolean {
        return accounts.any { it.isDefaultAccount && it.id != excludeId }
    }

    override suspend fun getDefaultAccount(): Account? {
        return accounts.find { it.isDefaultAccount }?.copy()
    }

    override suspend fun setDefaultAccount(id: String) {
        accounts.find { it.id == id }?.isDefaultAccount = true
    }
}