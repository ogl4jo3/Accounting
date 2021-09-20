package com.ogl4jo3.accounting.ui.accountingnotification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogl4jo3.accounting.data.AccountingNotification
import com.ogl4jo3.accounting.data.source.AccountingNotificationDataSource
import kotlinx.coroutines.launch
import timber.log.Timber

class AccountingNotificationViewModel(
    private val notificationDataSource: AccountingNotificationDataSource,
    private val alarmSetter: AlarmSetter,
) : ViewModel() {

    var updateFailed: () -> Unit = {}
    var showTimePickerDialog: (notification: AccountingNotification) -> Unit = { }

    private val _defaultNotification = MutableLiveData<AccountingNotification>()
    val defaultNotification: LiveData<AccountingNotification> = _defaultNotification

    init {
        viewModelScope.launch {
            _defaultNotification.value = notificationDataSource.getDefaultNotification()
        }
    }

    fun updateNotificationTime(hour: Int, minute: Int, notification: AccountingNotification) {
        notification.hour = hour
        notification.minute = minute
        updateNotification(notification)
    }

    fun switchNotification(isChecked: Boolean, notification: AccountingNotification) {
        notification.isOn = isChecked
        updateNotification(notification)
    }

    fun updateNotification(notification: AccountingNotification) {
        viewModelScope.launch {
            val isSuccessful = notificationDataSource.updateNotification(notification)
            if (isSuccessful) {
                updateAlarm(notification)
                _defaultNotification.value = notification
            } else {
                Timber.e("updateNotification failed")
                updateFailed()
            }
        }
    }

    private fun updateAlarm(notification: AccountingNotification) {
        when (notification.isOn) {
            true -> {
                alarmSetter.setInexactRepeating(notification.hour, notification.minute)
            }
            false -> {
                alarmSetter.cancel()
            }
        }
    }

}