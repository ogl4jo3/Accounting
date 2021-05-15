package com.ogl4jo3.accounting.ui.accountMgmt

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.ogl4jo3.accounting.data.AccountCategory

@BindingAdapter("accountCategory")
fun setAccountCategory(
    accountCategorySpinner: AccountCategorySpinner,
    accountCategory: AccountCategory
) {
    if (accountCategorySpinner.getSelectedItem() != accountCategory) {
        accountCategorySpinner.selectItem(accountCategory)
    }
}

@InverseBindingAdapter(attribute = "accountCategory")
fun getAccountCategory(accountCategorySpinner: AccountCategorySpinner): AccountCategory? {
    return accountCategorySpinner.getSelectedItem()
}

@BindingAdapter("accountCategoryAttrChanged")
fun setListeners(
    accountCategorySpinner: AccountCategorySpinner,
    attrChange: InverseBindingListener
) {
    accountCategorySpinner.setOnItemClickListener { attrChange.onChange() }
}