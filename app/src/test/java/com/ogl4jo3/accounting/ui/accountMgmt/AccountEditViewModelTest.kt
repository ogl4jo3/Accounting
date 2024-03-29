package com.ogl4jo3.accounting.ui.accountMgmt

import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.AccountCategory
import com.ogl4jo3.accounting.data.source.FakeAccountDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AccountEditViewModelTest {

    private lateinit var accountEditViewModel: AccountEditViewModel
    private lateinit var fakeAccountDataSource: FakeAccountDataSource

    @Before
    fun setupViewModel() {
        val defaultAccounts = mutableListOf(
            Account(
                name = "現金帳戶",
                initialAmount = 0,
                category = AccountCategory.Cash,
                isDefaultAccount = true,
            ),
            Account(
                name = "銀行帳戶",
                initialAmount = 0,
                category = AccountCategory.Bank,
                isDefaultAccount = false,
            ),
            Account(
                name = "信用卡",
                initialAmount = 0,
                category = AccountCategory.Card,
                isDefaultAccount = false,
            )
        )
        fakeAccountDataSource = FakeAccountDataSource(defaultAccounts)
        accountEditViewModel = AccountEditViewModel(fakeAccountDataSource, defaultAccounts[0])
    }

    @Test
    fun `test save account`() = runBlocking {
        val accounts = fakeAccountDataSource.getAllAccounts()
        val account = accounts[0]
        account.name = "test123"
        account.initialAmount = 1234
        account.category = AccountCategory.Card
        accountEditViewModel.saveAccount(account)
        Assert.assertEquals("test123", fakeAccountDataSource.getAllAccounts()[0].name)
        Assert.assertEquals(1234, fakeAccountDataSource.getAllAccounts()[0].initialAmount)
        Assert.assertEquals(
            AccountCategory.Card, fakeAccountDataSource.getAllAccounts()[0].category
        )
    }

    @Test
    fun `test save failed because have one default account at least`() = runBlocking {
        val defaultAccount = fakeAccountDataSource.getDefaultAccount() ?: return@runBlocking
        val originalAccountName = defaultAccount.name
        defaultAccount.isDefaultAccount = false
        defaultAccount.name = "test12345"
        accountEditViewModel.saveAccount(defaultAccount)
        Assert.assertEquals(originalAccountName, fakeAccountDataSource.getDefaultAccount()?.name)
    }

    @Test
    fun `test save duplicate name account`() = runBlocking {
        val accounts = fakeAccountDataSource.getAllAccounts()
        val account = accounts[0]
        account.name = accounts[1].name
        accountEditViewModel.saveAccount(account)
        Assert.assertEquals(accounts[0].name, fakeAccountDataSource.getAllAccounts()[0].name)
    }

    @Test
    fun `test delete account`() = runBlocking {
        accountEditViewModel.deleteAccount()
        Assert.assertEquals(2, fakeAccountDataSource.getNumberOfAccounts())
    }

    @Test
    fun `test delete account failed because have one account at least`() = runBlocking {
        val defaultAccounts = mutableListOf(
            Account(
                name = "現金帳戶",
                initialAmount = 0,
                category = AccountCategory.Cash,
                isDefaultAccount = true,
            )
        )
        fakeAccountDataSource = FakeAccountDataSource(defaultAccounts)
        accountEditViewModel.deleteAccount()
        Assert.assertEquals(1, fakeAccountDataSource.getNumberOfAccounts())
    }

    @Test
    fun `test delete default account`() = runBlocking {
        accountEditViewModel.deleteAccount()
        Assert.assertEquals(2, fakeAccountDataSource.getNumberOfAccounts())
        Assert.assertEquals(true, fakeAccountDataSource.hasDefaultAccount(""))
    }
}