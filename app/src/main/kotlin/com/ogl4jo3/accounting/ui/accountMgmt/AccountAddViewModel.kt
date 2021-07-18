package com.ogl4jo3.accounting.ui.accountMgmt

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.AccountCategory
import com.ogl4jo3.accounting.data.source.AccountDataSource
import com.ogl4jo3.accounting.data.source.DefaultAccountDataSource
import com.ogl4jo3.accounting.utils.safeLet
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class AccountAddViewModel(
    private val accountDataSource: AccountDataSource = DefaultAccountDataSource()
) : ViewModel() {

    val accountName = MutableLiveData("")
    val isDefaultAccount = MutableLiveData(true)
    val accountCategory = MutableLiveData(AccountCategory.Cash)
    val initialAmount = MutableLiveData(0)

    var accountNameEmptyError: () -> Unit = { }
    var accountNameExistError: () -> Unit = { }
    var navPopBackStack: () -> Unit = { }

    fun addAccount() {
        safeLet(
            accountName.value, initialAmount.value, accountCategory.value, isDefaultAccount.value
        ) { accountName, initialAmount, accountCategory, isDefaultAccount ->
            Account(
                name = accountName,
                initialAmount = initialAmount,
                category = accountCategory,
                isDefaultAccount = isDefaultAccount,
                budgetPrice = 0,
                budgetNotice = 0.0f,
                balance = initialAmount
            )
        }?.let { account ->
            runBlocking { addAccount(account) }
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
                navPopBackStack()
            }
        }
    }

}