package com.ogl4jo3.accounting

import com.ogl4jo3.accounting.ui.expense.ExpenseAddViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.*


val appModule = module {
    viewModel { (date: Date) -> ExpenseAddViewModel(date) }
}