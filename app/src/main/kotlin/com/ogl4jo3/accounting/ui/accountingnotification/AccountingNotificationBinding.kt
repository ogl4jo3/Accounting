package com.ogl4jo3.accounting.ui.accountingnotification

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.data.AccountingNotification

@BindingAdapter("notifications")
fun bindNotifications(recyclerView: RecyclerView, notifications: List<AccountingNotification>) {
    (recyclerView.adapter as AccountingNotificationAdapter).submitList(notifications)
}

@BindingAdapter("ampm")
fun setAMPMText(textView: TextView, notification: AccountingNotification) {
    textView.apply {
        text = if (notification.hour < 12) {
            context.getString(R.string.tv_am)
        } else {
            context.getString(R.string.tv_pm)
        }
    }
}

@BindingAdapter("time12HFormat")
fun setTimeTextWith12HFormat(textView: TextView, notification: AccountingNotification) {
    textView.apply {
        text = if (notification.hour < 12) {
            context.getString(R.string.tv_notification_time, notification.hour, notification.minute)
        } else {
            context.getString(
                R.string.tv_notification_time,
                notification.hour - 12,
                notification.minute
            )
        }
    }
}