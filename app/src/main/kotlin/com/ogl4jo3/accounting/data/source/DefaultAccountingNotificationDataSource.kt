package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.data.AccountingNotification
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class DefaultAccountingNotificationDataSource(
    private val notificationDao: AccountingNotificationDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AccountingNotificationDataSource {

    override suspend fun insertNotification(notification: AccountingNotification): Long =
        withContext(ioDispatcher) {
            return@withContext if (!notification.is24HFormat()
                || notificationDao.getNumberOfNotifications() >= AccountingNotificationDataSource.MAX_SIZE
            ) {
                Timber.e("insertNotification failed, notification: $notification")
                -1
            } else {
                notificationDao.insertNotification(notification)
            }
        }

    override suspend fun updateNotification(notification: AccountingNotification): Boolean =
        withContext(ioDispatcher) {
            return@withContext if (notification.is24HFormat()) {
                notificationDao.updateNotification(notification)
                true
            } else {
                Timber.e("updateNotification failed, notification: $notification")
                false
            }
        }

    override suspend fun getDefaultNotification(): AccountingNotification? =
        withContext(ioDispatcher) {
            val notificationList = notificationDao.getAllNotifications()
            return@withContext if (notificationList.size != 1) {
                null
            } else {
                notificationList[0]
            }
        }

    override suspend fun getAllNotifications(): List<AccountingNotification> =
        withContext(ioDispatcher) {
            return@withContext notificationDao.getAllNotifications()
        }

    override suspend fun getNumberOfNotifications(): Int =
        withContext(ioDispatcher) {
            return@withContext notificationDao.getNumberOfNotifications()
        }

}