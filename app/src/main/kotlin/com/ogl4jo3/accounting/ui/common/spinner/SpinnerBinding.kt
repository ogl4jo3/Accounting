package com.ogl4jo3.accounting.ui.common.spinner

import androidx.databinding.BindingAdapter
import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.Category

@BindingAdapter("spinnerAccounts")
fun bindSpinnerAccounts(spinner: AccountSpinner, accounts: List<Account>) {
    spinner.setAdapter(accounts)
}

@BindingAdapter("spinnerCategories")
fun bindSpinnerCategories(spinner: CategorySpinner, categories: List<Category>) {
    spinner.setAdapter(categories)
}
