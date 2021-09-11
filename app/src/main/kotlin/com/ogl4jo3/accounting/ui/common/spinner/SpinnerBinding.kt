package com.ogl4jo3.accounting.ui.common.spinner

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.Category

@BindingAdapter("spinnerAccounts")
fun bindSpinnerAccounts(spinner: AccountSpinner, accounts: List<Account>) {
    spinner.setAdapter(accounts)
}

@BindingAdapter("spinnerItemAccount")
fun setSpinnerItemAccount(accountSpinner: AccountSpinner, account: Account?) {
    account?.let {
        if (accountSpinner.getSelectedItem() != it) {
            accountSpinner.selectItem(it)
        }
    }
}

@InverseBindingAdapter(attribute = "spinnerItemAccount")
fun getSpinnerItemAccount(accountSpinner: AccountSpinner): Account? {
    return accountSpinner.getSelectedItem()
}

@BindingAdapter("spinnerItemAccountAttrChanged")
fun setListeners(accountSpinner: AccountSpinner, attrChange: InverseBindingListener) {
    accountSpinner.setOnItemClickListener { attrChange.onChange() }
}

@BindingAdapter("spinnerCategories")
fun bindSpinnerCategories(spinner: CategorySpinner, categories: List<Category>) {
    spinner.setAdapter(categories)
}

@BindingAdapter("spinnerItemCategory")
fun setSpinnerItemCategory(categorySpinner: CategorySpinner, category: Category?) {
    category?.let {
        if (categorySpinner.getSelectedItem() != it) {
            categorySpinner.selectItem(it)
        }
    }
}

@InverseBindingAdapter(attribute = "spinnerItemCategory")
fun getSpinnerItemCategory(categorySpinner: CategorySpinner): Category? {
    return categorySpinner.getSelectedItem()
}

@BindingAdapter("spinnerItemCategoryAttrChanged")
fun setListeners(categorySpinner: CategorySpinner, attrChange: InverseBindingListener) {
    categorySpinner.setOnItemClickListener { attrChange.onChange() }
}
