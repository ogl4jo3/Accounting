package com.ogl4jo3.accounting

import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.ui.accountMgmt.AccountEditViewModel
import com.ogl4jo3.accounting.ui.categoryMgmt.CategoryIcon
import com.ogl4jo3.accounting.ui.categoryMgmt.ExpenseCategoryAddViewModel
import com.ogl4jo3.accounting.ui.categoryMgmt.ExpenseCategoryEditViewModel
import com.ogl4jo3.accounting.ui.expense.ExpenseAddViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.Date


val appModule = module {
    viewModel { (date: Date) ->
        ExpenseAddViewModel(date)
    }
    viewModel { (accountId: String) ->
        AccountEditViewModel(accountId = accountId)
    }
    viewModel { (defaultCategoryIcon: CategoryIcon) ->
        ExpenseCategoryAddViewModel(
            defaultCategoryIcon = defaultCategoryIcon
        )
    }
    viewModel { (categoryIcon: CategoryIcon, category: Category) ->
        ExpenseCategoryEditViewModel(
            categoryIcon = categoryIcon,
            category = category
        )
    }
}