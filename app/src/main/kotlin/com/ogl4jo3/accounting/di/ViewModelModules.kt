package com.ogl4jo3.accounting.di

import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.CategoryType
import com.ogl4jo3.accounting.data.ExpenseRecord
import com.ogl4jo3.accounting.data.IncomeRecord
import com.ogl4jo3.accounting.ui.accountMgmt.AccountAddViewModel
import com.ogl4jo3.accounting.ui.accountMgmt.AccountEditViewModel
import com.ogl4jo3.accounting.ui.accountMgmt.AccountListViewModel
import com.ogl4jo3.accounting.ui.categoryMgmt.CategoryAddViewModel
import com.ogl4jo3.accounting.ui.categoryMgmt.CategoryEditViewModel
import com.ogl4jo3.accounting.ui.categoryMgmt.CategoryIcon
import com.ogl4jo3.accounting.ui.categoryMgmt.CategoryMgmtViewModel
import com.ogl4jo3.accounting.ui.expense.ExpenseAddViewModel
import com.ogl4jo3.accounting.ui.expense.ExpenseEditViewModel
import com.ogl4jo3.accounting.ui.expense.ExpenseViewModel
import com.ogl4jo3.accounting.ui.income.IncomeAddViewModel
import com.ogl4jo3.accounting.ui.income.IncomeEditViewModel
import com.ogl4jo3.accounting.ui.income.IncomeViewModel
import com.ogl4jo3.accounting.ui.statistics.expense.ExpenseStatisticsViewModel
import com.ogl4jo3.accounting.ui.statistics.income.IncomeStatisticsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.Date

val viewModelModules = module {
    viewModel { (date: Date?) ->
        ExpenseViewModel(get(), get(), get(), date)
    }
    viewModel { (date: Date) ->
        ExpenseAddViewModel(get(), get(), get(), date)
    }
    viewModel { (expenseRecord: ExpenseRecord) ->
        ExpenseEditViewModel(get(), get(), get(), expenseRecord)
    }
    viewModel { (date: Date?) ->
        IncomeViewModel(get(), get(), get(), date)
    }
    viewModel { (date: Date) ->
        IncomeAddViewModel(get(), get(), get(), date)
    }
    viewModel { (incomeRecord: IncomeRecord) ->
        IncomeEditViewModel(get(), get(), get(), incomeRecord)
    }
    viewModel {
        ExpenseStatisticsViewModel(get(), get())
    }
    viewModel {
        IncomeStatisticsViewModel(get(), get())
    }
    viewModel {
        AccountListViewModel(get())
    }
    viewModel {
        AccountAddViewModel(get())
    }
    viewModel { (account: Account) ->
        AccountEditViewModel(get(), account)
    }
    viewModel { (categoryType: CategoryType) ->
        CategoryMgmtViewModel(get(), categoryType)
    }
    viewModel { (categoryIcon: CategoryIcon, category: Category) ->
        CategoryEditViewModel(get(), categoryIcon, category)
    }
    viewModel { (categoryType: CategoryType, defaultCategoryIcon: CategoryIcon) ->
        CategoryAddViewModel(get(), categoryType, defaultCategoryIcon)
    }
}