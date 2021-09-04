package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.data.AccountingNotification
import kotlinx.coroutines.flow.Flow

interface AccountingNotificationDataSource {
    fun insertNotification(notification: AccountingNotification): Flow<Long>
    fun updateNotification(notification: AccountingNotification): Flow<Boolean>
    fun getAllNotifications(): Flow<List<AccountingNotification>>
    fun getNumberOfNotifications(): Flow<Int>
}