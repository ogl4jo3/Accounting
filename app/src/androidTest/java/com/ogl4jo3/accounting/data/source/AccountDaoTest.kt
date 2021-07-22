package com.ogl4jo3.accounting.data.source

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.AccountCategory
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber


@RunWith(AndroidJUnit4::class)
class AccountDaoTest {

    private val context: Context by lazy { ApplicationProvider.getApplicationContext() }
    private lateinit var database: AppDatabase
    private lateinit var accountDao: AccountDao

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries().build()
        accountDao = database.accountDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun `Test-InsertAccount_And_GetAllAccount`() = runBlocking {
        Timber.d("Test-InsertAccount_And_GetAllAccount")
        accountDao.insertAccount(
            Account(
                name = "test1",
                initialAmount = 100,
                category = AccountCategory.Cash,
                isDefaultAccount = true,
            )
        )
        accountDao.insertAccount(
            Account(
                name = "test2",
                initialAmount = 300,
                category = AccountCategory.Cash,
                isDefaultAccount = true,
            )
        )
        Assert.assertEquals(2, accountDao.getAllAccounts().size)
    }

    @Test
    fun `Test-InsertAccount_And_GetAccountById`() = runBlocking {
        Timber.d("Test-InsertAccount_And_GetAccountById")
        val account = Account(
            name = "test1",
            initialAmount = 100,
            category = AccountCategory.Cash,
            isDefaultAccount = true,
        )
        accountDao.insertAccount(account)
        Timber.d("account: $account")

        accountDao.getAccountById(account.id)?.let { accountLoaded ->
            Timber.d("accountLoaded: $accountLoaded")
            Assert.assertEquals(accountLoaded.name, (account.name))
            Assert.assertEquals(accountLoaded.initialAmount, (account.initialAmount))
            Assert.assertEquals(accountLoaded.category, (account.category))
            Assert.assertEquals(accountLoaded.isDefaultAccount, (account.isDefaultAccount))
        } ?: Assert.fail()
    }

    @Test
    fun `Test-GetNullAccount`() = runBlocking {
        Timber.d("Test-GetNullAccount")
        val account = accountDao.getAccountById("00")
        Timber.d("account: $account")
        Assert.assertNull(account)
    }

    @Test
    fun `Test-UpdateAccount_And_GetAccountById`() = runBlocking {
        Timber.d("Test-UpdateAccount_And_GetAccountById")
        val account = Account(
            name = "test1",
            initialAmount = 100,
            category = AccountCategory.Cash,
            isDefaultAccount = true,
        )
        accountDao.insertAccount(account)
        Timber.d("account: $account")

        account.name = "test2"
        account.initialAmount = 50000
        account.category = AccountCategory.Bank
        account.isDefaultAccount = false
        accountDao.updateAccount(account)
        Timber.d("updated account: $account")

        accountDao.getAccountById(account.id)?.let { accountLoaded ->
            Timber.d("accountLoaded: $accountLoaded")
            Assert.assertEquals(accountLoaded.name, (account.name))
            Assert.assertEquals(accountLoaded.initialAmount, (account.initialAmount))
            Assert.assertEquals(accountLoaded.category, (account.category))
            Assert.assertEquals(accountLoaded.isDefaultAccount, (account.isDefaultAccount))
        } ?: Assert.fail()
    }

    @Test
    fun `Test-DeleteAccount`() = runBlocking {
        Timber.d("Test-DeleteAccount")
        val account = Account(
            name = "test1",
            initialAmount = 100,
            category = AccountCategory.Cash,
            isDefaultAccount = true,
        )
        accountDao.insertAccount(account)
        Assert.assertNotNull(accountDao.getAccountById(account.id))

        accountDao.deleteAccount(account)
        Assert.assertNull(accountDao.getAccountById(account.id))
    }

    @Test
    fun `Test-InsertAccount_And_GetDefaultAccount-1`() = runBlocking {
        Timber.d("Test-InsertAccount_And_GetDefaultAccount-1")
        val defaultAccount = Account(
            name = "test1",
            initialAmount = 100,
            category = AccountCategory.Cash,
            isDefaultAccount = true,
        )
        accountDao.insertAccount(defaultAccount)
        accountDao.insertAccount(
            Account(
                name = "test2",
                initialAmount = 300,
                category = AccountCategory.Cash,
                isDefaultAccount = false,
            )
        )
        Timber.d("defaultAccount: $defaultAccount")

        accountDao.getDefaultAccount()?.let { accountLoaded ->
            Timber.d("accountLoaded: $accountLoaded")
            Assert.assertEquals(accountLoaded.name, (defaultAccount.name))
            Assert.assertEquals(accountLoaded.initialAmount, (defaultAccount.initialAmount))
            Assert.assertEquals(accountLoaded.category, (defaultAccount.category))
            Assert.assertEquals(accountLoaded.isDefaultAccount, (defaultAccount.isDefaultAccount))
        } ?: Assert.fail()
    }

    @Test
    fun `Test-InsertAccount_And_GetDefaultAccount-2`() = runBlocking {
        Timber.d("Test-InsertAccount_And_GetDefaultAccount-2")
        val account = Account(
            name = "test1",
            initialAmount = 100,
            category = AccountCategory.Cash,
            isDefaultAccount = false,
        )
        accountDao.insertAccount(account)
        Timber.d("account: $account")
        Assert.assertNull(accountDao.getDefaultAccount())
    }

    @Test
    fun `Test-InsertAccount_And_GetNumberOfAccounts`() = runBlocking {
        Timber.d("Test-InsertAccount_And_GetNumberOfAccounts")
        accountDao.insertAccount(
            Account(
                name = "test1",
                initialAmount = 100,
                category = AccountCategory.Cash,
                isDefaultAccount = true,
            )
        )
        accountDao.insertAccount(
            Account(
                name = "test2",
                initialAmount = 300,
                category = AccountCategory.Cash,
                isDefaultAccount = false,
            )
        )
        Assert.assertEquals(2, accountDao.getNumberOfAccounts())
    }

    @Test
    fun `Test-InsertAccount_And_GetNumberOfAccountsByName`() = runBlocking {
        Timber.d("Test-InsertAccount_And_GetNumberOfAccountsByName")
        accountDao.insertAccount(
            Account(
                name = "test1",
                initialAmount = 100,
                category = AccountCategory.Cash,
                isDefaultAccount = true,
            )
        )
        accountDao.insertAccount(
            Account(
                name = "test2",
                initialAmount = 300,
                category = AccountCategory.Cash,
                isDefaultAccount = false,
            )
        )
        Assert.assertEquals(1, accountDao.getNumberOfAccountsByName("test1"))
    }
}