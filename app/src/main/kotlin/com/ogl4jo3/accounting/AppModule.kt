package com.ogl4jo3.accounting

import com.ogl4jo3.accounting.ui.expense.ExpenseNewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.*


val appModule = module {
    viewModel { (date: Date) -> ExpenseNewViewModel(date) }
}