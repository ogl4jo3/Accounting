package com.ogl4jo3.accounting.ui.accountingnotification

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ogl4jo3.accounting.data.AccountingNotification

@BindingAdapter("notifications")
fun bindNotifications(recyclerView: RecyclerView, notifications: List<AccountingNotification>) {
    (recyclerView.adapter as AccountingNotificationAdapter).submitList(notifications)
}

@BindingAdapter("ampm")
fun setAMPMText(textView: TextView, time: String) {
    textView.text = "早上" //TODO: workaround, for debug
}

@BindingAdapter("time12HFormat")
fun setTimeTextWith12HFormat(textView: TextView, time: String) {
    textView.text = time //TODO: workaround, for debug
}