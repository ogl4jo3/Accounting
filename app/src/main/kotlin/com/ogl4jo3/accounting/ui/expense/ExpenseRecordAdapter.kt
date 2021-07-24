package com.ogl4jo3.accounting.ui.expense

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ogl4jo3.accounting.data.ExpenseRecord
import com.ogl4jo3.accounting.databinding.ItemExpenseRecordBinding
import kotlinx.coroutines.runBlocking

class ExpenseRecordAdapter(val viewModel: ExpenseViewModel) :
    ListAdapter<ExpenseRecord, ExpenseRecordViewHolder>(ExpenseRecordDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseRecordViewHolder {
        return ExpenseRecordViewHolder(
            ItemExpenseRecordBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ExpenseRecordViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            Navigation.findNavController(it).navigate(
                ExpenseFragmentDirections.actionExpenseFragmentToExpenseEditFragment(item)
            )
        }
        holder.binding.apply {
            this.expenseRecord = item
            this.category = runBlocking { viewModel.getCategoryById(item.categoryId) }
            this.account = runBlocking { viewModel.getAccountById(item.accountId) }
            executePendingBindings()
        }
    }
}

class ExpenseRecordViewHolder(val binding: ItemExpenseRecordBinding) :
    RecyclerView.ViewHolder(binding.root)

class ExpenseRecordDiffCallback : DiffUtil.ItemCallback<ExpenseRecord>() {
    override fun areItemsTheSame(
        oldItem: ExpenseRecord, newItem: ExpenseRecord,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: ExpenseRecord, newItem: ExpenseRecord,
    ): Boolean = oldItem.expenseRecordId == newItem.expenseRecordId
}