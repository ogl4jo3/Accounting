package com.ogl4jo3.accounting.ui.accountingnotification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ogl4jo3.accounting.data.AccountingNotification
import com.ogl4jo3.accounting.databinding.ItemNotificationBinding

class AccountingNotificationAdapter(val viewModel: AccountingNotificationViewModel) :
    ListAdapter<AccountingNotification, AccountingNotificationViewHolder>(
        AccountingNotificationDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : AccountingNotificationViewHolder {
        return AccountingNotificationViewHolder(
            ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AccountingNotificationViewHolder, position: Int) {
        val notification = getItem(position)
        holder.binding.apply {
            clTime.setOnClickListener {
                viewModel.showTimePickerDialog(notification) { updatedNotification ->
                    notification.hour = updatedNotification.hour
                    notification.minute = updatedNotification.minute
                    notifyItemChanged(position)
                }
            }
            swNotification.setOnCheckedChangeListener { _, isChecked ->
                notification.isOn = isChecked
                viewModel.switchNotification(notification)
            }
            this.item = notification
            executePendingBindings()
        }
    }
}

class AccountingNotificationViewHolder(val binding: ItemNotificationBinding) :
    RecyclerView.ViewHolder(binding.root)

class AccountingNotificationDiffCallback : DiffUtil.ItemCallback<AccountingNotification>() {
    override fun areItemsTheSame(
        oldItem: AccountingNotification,
        newItem: AccountingNotification
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: AccountingNotification,
        newItem: AccountingNotification
    ): Boolean =
        oldItem.hour == newItem.hour && oldItem.minute == newItem.minute && oldItem.isOn == newItem.isOn
}