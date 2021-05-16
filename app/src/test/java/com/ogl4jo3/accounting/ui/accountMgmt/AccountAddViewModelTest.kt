package com.ogl4jo3.accounting.ui.accountMgmt

import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.AccountCategory
import com.ogl4jo3.accounting.data.source.FakeAccountDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AccountAddViewModelTest {

    private lateinit var accountAddViewModel: AccountAddViewModel
    private lateinit var fakeAccountDataSource: FakeAccountDataSource

    @Before
    fun setupViewModel() {
        fakeAccountDataSource = FakeAccountDataSource()
        accountAddViewModel = AccountAddViewModel(fakeAccountDataSource)
    }

    @Test
    fun `test save account`() = runBlocking {
        val accountsSize = fakeAccountDataSource.accounts.size
        accountAddViewModel.saveAccount(
            Account(
                name = "test1",
                initialAmount = 100,
                category = AccountCategory.Cash,
                isDefaultAccount = true,
                budgetPrice = 50,
                budgetNotice = 0.3f,
                balance = 10
            )
        )
        Assert.assertEquals(accountsSize + 1, fakeAccountDataSource.accounts.size)
        Assert.assertEquals(
            1, fakeAccountDataSource.accounts.filter { it.isDefaultAccount }.size
        )
    }

    @Test
    fun `test save and update default account`() = runBlocking {
        val accountsSize = fakeAccountDataSource.accounts.size
        accountAddViewModel.saveAccount(
            Account(
                name = "test1",
                initialAmount = 100,
                category = AccountCategory.Cash,
                isDefaultAccount = true,
                budgetPrice = 50,
                budgetNotice = 0.3f,
                balance = 10
            )
        )
        accountAddViewModel.saveAccount(
            Account(
                name = "test1234",
                initialAmount = 100,
                category = AccountCategory.Cash,
                isDefaultAccount = true,
                budgetPrice = 50,
                budgetNotice = 0.3f,
                balance = 10
            )
        )
        Assert.assertEquals(accountsSize + 2, fakeAccountDataSource.accounts.size)
        Assert.assertEquals(
            1, fakeAccountDataSource.accounts.filter { it.isDefaultAccount }.size
        )
    }

    @Test
    fun `test save duplicate name account`() = runBlocking {
        val accountsSize = fakeAccountDataSource.accounts.size
        accountAddViewModel.saveAccount(
            Account(
                name = "test1",
                initialAmount = 100,
                category = AccountCategory.Cash,
                isDefaultAccount = true,
                budgetPrice = 50,
                budgetNotice = 0.3f,
                balance = 10
            )
        )
        accountAddViewModel.saveAccount(
            Account(
                name = "test1",
                initialAmount = 100,
                category = AccountCategory.Cash,
                isDefaultAccount = true,
                budgetPrice = 50,
                budgetNotice = 0.3f,
                balance = 10
            )
        )
        Assert.assertEquals(accountsSize + 1, fakeAccountDataSource.accounts.size)
    }

}