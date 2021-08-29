package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.data.AccountingNotification

interface AccountingNotificationDataSource {
    suspend fun insertNotification(notification: AccountingNotification): Long
    suspend fun updateNotification(notification: AccountingNotification)
    suspend fun deleteNotification(notification: AccountingNotification)
    suspend fun getNotificationById(notificationId: String): AccountingNotification?
    suspend fun getAllNotifications(): List<AccountingNotification>
    suspend fun getNumberOfNotifications(): Int
}