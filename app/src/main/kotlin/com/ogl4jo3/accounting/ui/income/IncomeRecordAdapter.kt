package com.ogl4jo3.accounting.ui.income

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ogl4jo3.accounting.data.IncomeRecord
import com.ogl4jo3.accounting.databinding.ItemIncomeRecordBinding
import kotlinx.coroutines.runBlocking

class IncomeRecordAdapter(val viewModel: IncomeViewModel) :
    ListAdapter<IncomeRecord, IncomeRecordViewHolder>(IncomeRecordDiffCallback()) {
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
            Navigation.findNavController(it).navigate(
                IncomeFragmentDirections.actionIncomeFragmentToIncomeEditFragment(item)
            )
        }
        holder.binding.apply {
            this.incomeRecord = item
            this.category = runBlocking { viewModel.getCategoryById(item.categoryId) }
            this.account = runBlocking { viewModel.getAccountById(item.accountId) }
            executePendingBindings()
        }
    }
}

class IncomeRecordViewHolder(val binding: ItemIncomeRecordBinding) :
    RecyclerView.ViewHolder(binding.root)

class IncomeRecordDiffCallback : DiffUtil.ItemCallback<IncomeRecord>() {
    override fun areItemsTheSame(
        oldItem: IncomeRecord, newItem: IncomeRecord,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: IncomeRecord, newItem: IncomeRecord,
    ): Boolean = oldItem.incomeRecordId == newItem.incomeRecordId
}