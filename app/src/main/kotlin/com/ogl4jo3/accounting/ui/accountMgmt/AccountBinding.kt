package com.ogl4jo3.accounting.ui.accountMgmt

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.AccountCategory
import java.text.NumberFormat
import java.util.Locale

@BindingAdapter("accounts")
fun bindAccounts(recyclerView: RecyclerView, accounts: List<Account>) {
    (recyclerView.adapter as AccountAdapter).submitList(accounts)
}

@BindingAdapter("money")
fun setMoneyText(textView: TextView, money: Int) {
    textView.text = String.format(
        "$%s", NumberFormat.getNumberInstance(Locale.getDefault()).format(money.toLong())
    )
}

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