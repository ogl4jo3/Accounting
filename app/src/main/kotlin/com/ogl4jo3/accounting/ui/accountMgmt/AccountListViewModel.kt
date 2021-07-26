package com.ogl4jo3.accounting.ui.accountMgmt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.source.AccountDataSource
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class AccountListViewModel(
    private val accountDataSource: AccountDataSource
) : ViewModel() {
    var navigateToAccountEditFragment: (account: Account) -> Unit = { }

    private val _accounts: MutableLiveData<List<Account>> = MutableLiveData(emptyList())
    val accounts: LiveData<List<Account>> = _accounts

    fun updateAccountList() {
        _accounts.value = runBlocking { accountDataSource.getAllAccounts() }
    }

    fun getAccountBalance(account: Account): Int {
        //TODO: calculate account balance , use (Account.initialAmount - expense + income) .
        Timber.d("todo account balance, account: $account")
        return 0
    }
}