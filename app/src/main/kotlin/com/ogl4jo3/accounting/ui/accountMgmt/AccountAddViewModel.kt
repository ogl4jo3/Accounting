package com.ogl4jo3.accounting.ui.accountMgmt

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.AccountCategory
import com.ogl4jo3.accounting.data.source.AccountDataSource
import com.ogl4jo3.accounting.data.source.DefaultAccountDataSource
import com.ogl4jo3.accounting.utils.safeLet
import kotlinx.coroutines.runBlocking

class AccountAddViewModel(
    private val accountDataSource: AccountDataSource = DefaultAccountDataSource()
) : ViewModel() {

    val accountName = MutableLiveData<String>()
    val isDefaultAccount = MutableLiveData(true)
    val accountCategory = MutableLiveData(AccountCategory.Cash)
    val initialAmount = MutableLiveData(0)

    var accountNameEmptyError: () -> Unit = { }
    var accountNameExistError: () -> Unit = { }
    var navPopBackStack: () -> Unit = { }

    fun addAccount() {
        if (!accountName.value.isNullOrEmpty()) {
            safeLet(
                accountName.value,
                initialAmount.value,
                accountCategory.value,
                isDefaultAccount.value
            ) { accountName, initialAmount, accountCategory, isDefaultAccount ->
                runBlocking {
                    val account = Account(
                        name = accountName,
                        initialAmount = initialAmount,
                        category = accountCategory,
                        isDefaultAccount = isDefaultAccount,
                        budgetPrice = 0,
                        budgetNotice = 0.0f,
                        balance = 0
                    )
                    saveAccount(account)
                }
            }
        } else {
            accountNameEmptyError()
        }
    }

    suspend fun saveAccount(account: Account) {
        val id = accountDataSource.insertAccount(account)
        if (id < 0) { // insert failed.
            accountNameExistError()
        } else {
            if (account.isDefaultAccount) {
                accountDataSource.updateDefaultAccount(account.id)
            }
            navPopBackStack()
        }
    }

}