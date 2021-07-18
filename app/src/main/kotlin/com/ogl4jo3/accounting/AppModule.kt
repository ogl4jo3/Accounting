package com.ogl4jo3.accounting

import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.CategoryType
import com.ogl4jo3.accounting.ui.accountMgmt.AccountEditViewModel
import com.ogl4jo3.accounting.ui.categoryMgmt.CategoryEditViewModel
import com.ogl4jo3.accounting.ui.categoryMgmt.CategoryIcon
import com.ogl4jo3.accounting.ui.categoryMgmt.CategoryMgmtViewModel
import com.ogl4jo3.accounting.ui.categoryMgmt.ExpenseCategoryAddViewModel
import com.ogl4jo3.accounting.ui.categoryMgmt.IncomeCategoryAddViewModel
import com.ogl4jo3.accounting.ui.expense.ExpenseAddViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.Date


val appModule = module {
    viewModel { (date: Date) ->
        ExpenseAddViewModel(date)
    }
    viewModel { (account: Account) ->
        AccountEditViewModel(account = account)
    }
    viewModel { (categoryType: CategoryType) ->
        CategoryMgmtViewModel(
            categoryType = categoryType
        )
    }
    viewModel { (categoryIcon: CategoryIcon, category: Category) ->
        CategoryEditViewModel(
            categoryIcon = categoryIcon,
            category = category
        )
    }
    viewModel { (defaultCategoryIcon: CategoryIcon) ->
        ExpenseCategoryAddViewModel(
            defaultCategoryIcon = defaultCategoryIcon
        )
    }
    viewModel { (defaultCategoryIcon: CategoryIcon) ->
        IncomeCategoryAddViewModel(
            defaultCategoryIcon = defaultCategoryIcon
        )
    }
}