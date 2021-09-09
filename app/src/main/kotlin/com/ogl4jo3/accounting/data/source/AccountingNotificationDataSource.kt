package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.data.AccountingNotification
import kotlinx.coroutines.flow.Flow

interface AccountingNotificationDataSource {
    fun insertNotification(notification: AccountingNotification): Flow<Long>
    fun updateNotification(notification: AccountingNotification): Flow<Boolean>
    fun getDefaultNotification(): Flow<AccountingNotification?>
    fun getAllNotifications(): Flow<List<AccountingNotification>>
    fun getNumberOfNotifications(): Flow<Int>

    companion object {
        const val MAX_SIZE = 1
    }
}