package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.data.AccountingNotification

interface AccountingNotificationDataSource {
    suspend fun insertNotification(notification: AccountingNotification): Long
    suspend fun updateNotification(notification: AccountingNotification): Boolean
    suspend fun getDefaultNotification(): AccountingNotification?
    suspend fun getAllNotifications(): List<AccountingNotification>
    suspend fun getNumberOfNotifications(): Int

    companion object {
        const val MAX_SIZE = 1
    }
}