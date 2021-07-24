package com.ogl4jo3.accounting.ui.income

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ogl4jo3.accounting.data.IncomeRecord

@BindingAdapter("incomeRecords")
fun bindIncomeRecords(recyclerView: RecyclerView, incomeRecords: List<IncomeRecord>) {
    (recyclerView.adapter as IncomeRecordAdapter).submitList(incomeRecords)
}