package com.ogl4jo3.accounting

import android.app.Application
import androidx.room.Room
import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.AccountCategory
import com.ogl4jo3.accounting.data.source.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class AccountingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext, AppDatabase::class.java, "accounting"
        ).build()
        defaultAccounts = listOf(
            Account(
                name = getString(R.string.default_cash_account_name),
                initialAmount = 0,
                category = AccountCategory.Cash,
                isDefaultAccount = true,
                budgetPrice = 10000,
                budgetNotice = 0.5f,
                balance = 0
            ),
            Account(
                name = getString(R.string.default_bank_account_name),
                initialAmount = 0,
                category = AccountCategory.Bank,
                isDefaultAccount = false,
                budgetPrice = 10000,
                budgetNotice = 0.5f,
                balance = 0
            ),
            Account(
                name = getString(R.string.default_card_account_name),
                initialAmount = 0,
                category = AccountCategory.Card,
                isDefaultAccount = false,
                budgetPrice = 10000,
                budgetNotice = 0.5f,
                balance = 0
            )
        )

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger()
            androidContext(this@AccountingApplication)
            modules(appModule)
        }

    }

    companion object {
        lateinit var database: AppDatabase
        lateinit var defaultAccounts: List<Account>
    }
}