package com.ogl4jo3.accounting.ui.accountMgmt

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.source.AccountDataSource
import com.ogl4jo3.accounting.utils.safeLet
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class AccountEditViewModel(
    private val accountDataSource: AccountDataSource,
    val account: Account
) : ViewModel() {

    val accountName = MutableLiveData(account.name)
    val isDefaultAccount = MutableLiveData(account.isDefaultAccount)
    val accountCategory = MutableLiveData(account.category)
    val initialAmount = MutableLiveData(account.initialAmount)

    var accountNameEmptyError: () -> Unit = { }
    var accountNameExistError: () -> Unit = { }
    var atLeastOneDefaultAccount: () -> Unit = { }
    var navToAccountListFragment: () -> Unit = { }

    fun saveAccount() {
        safeLet(
            accountName.value, initialAmount.value, accountCategory.value, isDefaultAccount.value
        ) { accountName, initialAmount, accountCategory, isDefaultAccount ->
            account.name = accountName
            account.initialAmount = initialAmount
            account.category = accountCategory
            account.isDefaultAccount = isDefaultAccount
            account.budgetPrice = 0
            account.budgetNotice = 0.0f
            account.balance = initialAmount
            account
        }?.let { account ->
            runBlocking { saveAccount(account) }
        } ?: run {
            Timber.e("Something error")
            navToAccountListFragment()
        }
    }

    suspend fun saveAccount(account: Account) {
        if (account.name.isBlank()) {
            accountNameEmptyError()
        } else if (accountDataSource.hasDuplicatedName(account.name, account.id)) {
            accountNameExistError()
        } else if (!account.isDefaultAccount && !accountDataSource.hasDefaultAccount(account.id)) {
            atLeastOneDefaultAccount()
        } else {
            accountDataSource.updateAccount(account)
            if (account.isDefaultAccount) {
                accountDataSource.resetDefaultAccountExceptId(account.id)
            }
            navToAccountListFragment()
        }
    }

    fun deleteAccount(onSuccess: () -> Unit = {}, onFail: () -> Unit = {}) {
        runBlocking {
            if (accountDataSource.getNumberOfAccounts() <= 1) {
                onFail()
            } else {
                accountDataSource.deleteAccount(account)
                if (account.isDefaultAccount) {
                    accountDataSource.setDefaultAccount(accountDataSource.getAllAccounts()[0].id)
                }
                onSuccess()
            }
        }
    }

}