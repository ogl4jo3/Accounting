package com.ogl4jo3.accounting.ui.expense

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ogl4jo3.accounting.data.ExpenseRecordItem
import com.ogl4jo3.accounting.databinding.ItemExpenseRecordBinding
import com.ogl4jo3.accounting.ui.common.extensions.setOnSingleClickListener

class ExpenseRecordAdapter(val onItemClick: (item: ExpenseRecordItem) -> Unit) :
    ListAdapter<ExpenseRecordItem, ExpenseRecordViewHolder>(ExpenseRecordDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseRecordViewHolder {
        return ExpenseRecordViewHolder(
            ItemExpenseRecordBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ExpenseRecordViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnSingleClickListener {
            onItemClick(item)
        }
        holder.binding.apply {
            this.expenseRecord = item
            executePendingBindings()
        }
    }
}

class ExpenseRecordViewHolder(val binding: ItemExpenseRecordBinding) :
    RecyclerView.ViewHolder(binding.root)

class ExpenseRecordDiffCallback : DiffUtil.ItemCallback<ExpenseRecordItem>() {
    override fun areItemsTheSame(
        oldItem: ExpenseRecordItem, newItem: ExpenseRecordItem,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: ExpenseRecordItem, newItem: ExpenseRecordItem,
    ): Boolean = oldItem.expenseRecordId == newItem.expenseRecordId
}