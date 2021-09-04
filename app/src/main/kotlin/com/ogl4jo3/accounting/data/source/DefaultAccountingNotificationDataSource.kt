package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.data.AccountingNotification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class DefaultAccountingNotificationDataSource(
    private val notificationDao: AccountingNotificationDao
) : AccountingNotificationDataSource {

    override fun insertNotification(notification: AccountingNotification): Flow<Long> {
        return flow {
            if (notification.is24HFormat()) {
                emit(notificationDao.insertNotification(notification))
            } else {
                Timber.e("insertNotification failed, notification: $notification")
                emit(-1)
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

    override fun getAllNotifications(): Flow<List<AccountingNotification>> {
        return notificationDao.getAllNotifications()
    }

    override fun getNumberOfNotifications(): Flow<Int> {
        return notificationDao.getNumberOfNotifications()
    }

}