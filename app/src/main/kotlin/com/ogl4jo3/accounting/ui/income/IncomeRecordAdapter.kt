package com.ogl4jo3.accounting.ui.income

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ogl4jo3.accounting.data.IncomeRecordItem
import com.ogl4jo3.accounting.databinding.ItemIncomeRecordBinding

class IncomeRecordAdapter(val onItemClick: (item: IncomeRecordItem) -> Unit) :
    ListAdapter<IncomeRecordItem, IncomeRecordViewHolder>(IncomeRecordDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeRecordViewHolder {
        return IncomeRecordViewHolder(
            ItemIncomeRecordBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: IncomeRecordViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
        holder.binding.apply {
            this.incomeRecord = item
            executePendingBindings()
        }
    }
}

class IncomeRecordViewHolder(val binding: ItemIncomeRecordBinding) :
    RecyclerView.ViewHolder(binding.root)

class IncomeRecordDiffCallback : DiffUtil.ItemCallback<IncomeRecordItem>() {
    override fun areItemsTheSame(
        oldItem: IncomeRecordItem, newItem: IncomeRecordItem,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: IncomeRecordItem, newItem: IncomeRecordItem,
    ): Boolean = oldItem.incomeRecordId == newItem.incomeRecordId
}