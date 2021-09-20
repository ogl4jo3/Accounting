package com.ogl4jo3.accounting.ui.expense

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ogl4jo3.accounting.data.ExpenseRecordItem

@BindingAdapter("expenseRecords")
fun bindExpenseRecords(recyclerView: RecyclerView, expenseRecords: List<ExpenseRecordItem>) {
    (recyclerView.adapter as ExpenseRecordAdapter).submitList(expenseRecords)
}