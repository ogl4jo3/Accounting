package com.ogl4jo3.accounting.ui.accountingnotification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ogl4jo3.accounting.data.AccountingNotification
import com.ogl4jo3.accounting.data.source.AccountingNotificationDataSource
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class AccountingNotificationViewModel(
    private val notificationDataSource: AccountingNotificationDataSource
) : ViewModel() {
    companion object {
        const val NOTIFICATION_MAX = 3
    }

    var notificationMaximumError: () -> Unit = { }
    var notificationExistError: () -> Unit = { }
    var showDelConfirmDialog: (notification: AccountingNotification) -> Unit = { }
    var showTimePickerDialog: (
        notification: AccountingNotification,
        onSuccess: (notification: AccountingNotification) -> Unit
    ) -> Unit = { _, _ -> }

    private val _allNotifications: MutableLiveData<List<AccountingNotification>> =
        MutableLiveData(emptyList())
    val allNotifications: LiveData<List<AccountingNotification>> = _allNotifications

    fun updateNotificationList() {
        _allNotifications.value = runBlocking { notificationDataSource.getAllNotifications() }
    }

    fun addNotification(hour: Int, minute: Int, isOn: Boolean) {
        addNotification(AccountingNotification(hour = hour, minute = minute, isOn = isOn))
    }

    fun addNotification(notification: AccountingNotification) {
        runBlocking {
            if (notificationDataSource.getNumberOfNotifications() >= NOTIFICATION_MAX) {
                notificationMaximumError()
            } else if (notificationDataSource.hasDuplicatedNotification(
                    notification.hour, notification.minute, notification.id
                )
            ) {
                notificationExistError()
            } else {
                val id = notificationDataSource.insertNotification(notification)
                if (id < 0) {
                    Timber.e("insertNotification failed, notification: $notification")
                } else {
                    updateNotificationList()
                }
            }
        }
    }

    fun deleteNotification(
        notification: AccountingNotification,
        onSuccess: () -> Unit = {},
        onFail: () -> Unit = {}
    ) {
        runBlocking {
            if (notificationDataSource.getNumberOfNotifications() <= 1) {
                onFail()
            } else {
                notificationDataSource.deleteNotification(notification)
                //TODO: cancel all notification and alarm
                updateNotificationList()
                onSuccess()
            }
        }
    }

    fun updateNotification(
        id: String, hour: Int, minute: Int, isOn: Boolean,
        onSuccess: (notification: AccountingNotification) -> Unit = {}
    ) {
        updateNotification(AccountingNotification(id, hour, minute, isOn), onSuccess)
    }

    fun updateNotification(
        notification: AccountingNotification,
        onSuccess: (notification: AccountingNotification) -> Unit = {}
    ) {
        runBlocking {
            if (notificationDataSource.hasDuplicatedNotification(
                    notification.hour, notification.minute, notification.id
                )
            ) {
                notificationExistError()
            } else {
                notificationDataSource.updateNotification(notification)
                onSuccess(notification)
            }
        }
    }

}