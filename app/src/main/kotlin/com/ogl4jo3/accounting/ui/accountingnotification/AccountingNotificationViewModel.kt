package com.ogl4jo3.accounting.ui.accountingnotification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ogl4jo3.accounting.data.AccountingNotification
import com.ogl4jo3.accounting.data.source.AccountingNotificationDataSource
import kotlinx.coroutines.runBlocking
import java.util.Calendar

class AccountingNotificationViewModel(
    private val notificationDataSource: AccountingNotificationDataSource,
    private val alarmSetter: AlarmSetter,
) : ViewModel() {

    var showTimePickerDialog: (
        notification: AccountingNotification,
        notifyItemChanged: (notification: AccountingNotification) -> Unit
    ) -> Unit = { _, _ -> }

    private val _allNotifications: MutableLiveData<List<AccountingNotification>> =
        MutableLiveData(emptyList())
    val allNotifications: LiveData<List<AccountingNotification>> = _allNotifications

    fun updateNotificationList() {
        _allNotifications.value = runBlocking { notificationDataSource.getAllNotifications() }
    }

    fun updateNotification(
        id: String, hour: Int, minute: Int, isOn: Boolean,
        notifyItemChanged: (notification: AccountingNotification) -> Unit = {}
    ) {
        updateNotification(AccountingNotification(id, hour, minute, isOn), notifyItemChanged)
    }

    fun updateNotification(
        notification: AccountingNotification,
        notifyItemChanged: (notification: AccountingNotification) -> Unit = {}
    ) {
        //TODO: fix runBlocking
        runBlocking {
            notificationDataSource.updateNotification(notification)
            notifyItemChanged(notification)
            updateAlarm(notification)
        }
    }

    fun switchNotification(notification: AccountingNotification) {
        runBlocking {
            notificationDataSource.updateNotification(notification)
            updateAlarm(notification)
        }
    }

    private fun updateAlarm(notification: AccountingNotification) {
        when (notification.isOn) {
            true -> {
                val calendar: Calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, notification.hour)
                    set(Calendar.MINUTE, notification.minute)
                }
                alarmSetter.setInexactRepeating(calendar)
            }
            false -> {
                alarmSetter.cancel()
            }
        }
    }

}