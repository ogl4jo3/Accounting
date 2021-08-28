package com.ogl4jo3.accounting.ui.accountingnotification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ogl4jo3.accounting.data.AccountingNotification
import com.ogl4jo3.accounting.databinding.ItemNotificationBinding

class AccountingNotificationAdapter() :
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
        holder.binding.apply {
            this.item = getItem(position)
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
    ): Boolean = oldItem.time == newItem.time && oldItem.isOn == newItem.isOn
}