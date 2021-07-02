package com.ogl4jo3.accounting.ui.accountMgmt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.databinding.ItemAccountBinding

/**
 * 帳戶Adapter
 */
class AccountAdapter(val viewModel: AccountListViewModel) :
    ListAdapter<Account, AccountViewHolder>(AccountDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        return AccountViewHolder(
            ItemAccountBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.binding.apply {
            this.account = getItem(position)
            this.viewModel = this@AccountAdapter.viewModel
            executePendingBindings()
        }
    }
}

class AccountViewHolder(val binding: ItemAccountBinding) :
    RecyclerView.ViewHolder(binding.root)

class AccountDiffCallback : DiffUtil.ItemCallback<Account>() {
    override fun areItemsTheSame(
        oldItem: Account, newItem: Account,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: Account, newItem: Account,
    ): Boolean = oldItem.id == newItem.id
}