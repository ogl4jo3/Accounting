package com.ogl4jo3.accounting.ui.accountMgmt

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.AccountCategory
import com.ogl4jo3.accounting.data.source.AccountDataSource
import com.ogl4jo3.accounting.utils.safeLet
import kotlinx.coroutines.launch
import timber.log.Timber

class AccountAddViewModel(
    private val accountDataSource: AccountDataSource
) : ViewModel() {

    val accountName = MutableLiveData("")
    val isDefaultAccount = MutableLiveData(true)
    val accountCategory = MutableLiveData(AccountCategory.Cash)
    val initialAmount = MutableLiveData(0)

    var accountNameEmptyError: () -> Unit = { }
    var accountNameExistError: () -> Unit = { }
    var navToAccountListFragment: () -> Unit = { }

    fun addAccount() {
        safeLet(
            accountName.value, initialAmount.value, accountCategory.value, isDefaultAccount.value
        ) { accountName, initialAmount, accountCategory, isDefaultAccount ->
            Account(
                name = accountName,
                initialAmount = initialAmount,
                category = accountCategory,
                isDefaultAccount = isDefaultAccount,
            )
        }?.let { account ->
            viewModelScope.launch {
                addAccount(account)
            }
        } ?: return
    }

    suspend fun addAccount(account: Account) {
        if (account.name.isBlank()) {
            accountNameEmptyError()
        } else if (accountDataSource.hasDuplicatedName(account.name)) {
            accountNameExistError()
        } else {
            val id = accountDataSource.insertAccount(account)
            if (id < 0) {
                Timber.e("insertAccount failed, account: $account")
            } else {
                if (account.isDefaultAccount) {
                    accountDataSource.resetDefaultAccountExceptId(account.id)
                }
                navToAccountListFragment()
            }
        }
    }

}