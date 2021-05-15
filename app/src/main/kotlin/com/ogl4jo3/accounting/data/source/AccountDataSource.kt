package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.data.Account

interface AccountDataSource {
    suspend fun insertAccount(account: Account): Long
    suspend fun updateDefaultAccount(id: String)
}