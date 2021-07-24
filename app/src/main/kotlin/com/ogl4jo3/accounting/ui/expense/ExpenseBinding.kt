package com.ogl4jo3.accounting.ui.expense

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ogl4jo3.accounting.data.ExpenseRecord

@BindingAdapter("expenseRecords")
fun bindExpenseRecords(recyclerView: RecyclerView, expenseRecords: List<ExpenseRecord>) {
    (recyclerView.adapter as ExpenseRecordAdapter).submitList(expenseRecords)
}