package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.data.AccountingNotification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class DefaultAccountingNotificationDataSource(
    private val notificationDao: AccountingNotificationDao
) : AccountingNotificationDataSource {

    override fun insertNotification(notification: AccountingNotification): Flow<Long> {
        return flow {
            if (!notification.is24HFormat()
                || notificationDao.getNumberOfNotifications()
                    .first() >= AccountingNotificationDataSource.MAX_SIZE
            ) {
                Timber.e("insertNotification failed, notification: $notification")
                emit(-1)
            } else {
                emit(notificationDao.insertNotification(notification))
            }
        }
    }

    override fun updateNotification(notification: AccountingNotification): Flow<Boolean> {
        return flow {
            if (notification.is24HFormat()) {
                notificationDao.updateNotification(notification)
                emit(true)
            } else {
                Timber.e("updateNotification failed, notification: $notification")
                emit(false)
            }
        }
    }

    override fun getDefaultNotification(): Flow<AccountingNotification?> {
        return flow {
            val notificationList = notificationDao.getAllNotifications().first()
            if (notificationList.size != 1) {
                emit(null)
            } else {
                emit(notificationList[0])
            }
        }
    }

    override fun getAllNotifications(): Flow<List<AccountingNotification>> {
        return notificationDao.getAllNotifications()
    }

    override fun getNumberOfNotifications(): Flow<Int> {
        return notificationDao.getNumberOfNotifications()
    }

}