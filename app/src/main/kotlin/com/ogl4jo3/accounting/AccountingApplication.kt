package com.ogl4jo3.accounting

import android.app.Application
import androidx.room.Room
import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.AccountCategory
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.CategoryType
import com.ogl4jo3.accounting.data.source.AppDatabase
import com.ogl4jo3.accounting.ui.categoryMgmt.drawableName
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

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger()
            androidContext(this@AccountingApplication)
            modules(appModule)
        }

        initDBDefaultData()

    }

    private fun initDBDefaultData() {
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
        var incomeCategoryOrderNum = 0
        defaultCategories = listOf(
            // Expense Categories
            Category(
                name = getString(R.string.category_lunch),
                iconResName = R.drawable.ic_category_lunch.drawableName(resources),
                categoryType = CategoryType.Expense,
            ),
            Category(
                name = getString(R.string.category_dinner),
                iconResName = R.drawable.ic_category_dinner.drawableName(resources),
                categoryType = CategoryType.Expense,
            ),
            Category(
                name = getString(R.string.category_afternoon_tea),
                iconResName = R.drawable.ic_category_afternoon_tea.drawableName(resources),
                categoryType = CategoryType.Expense,
            ),
            Category(
                name = getString(R.string.category_dessert),
                iconResName = R.drawable.ic_category_dessert.drawableName(resources),
                categoryType = CategoryType.Expense,
            ),
            //TODO: add more category

            // Income Categories
            Category(
                name = getString(R.string.category_salary),
                iconResName = R.drawable.ic_category_salary.drawableName(resources),
                categoryType = CategoryType.Income,
                orderNumber = incomeCategoryOrderNum++
            ),
            Category(
                name = getString(R.string.category_bonus),
                iconResName = R.drawable.ic_category_bonus.drawableName(resources),
                categoryType = CategoryType.Income,
                orderNumber = incomeCategoryOrderNum++
            ),
            Category(
                name = getString(R.string.category_investment),
                iconResName = R.drawable.ic_category_investment.drawableName(resources),
                categoryType = CategoryType.Income,
                orderNumber = incomeCategoryOrderNum++
            ),
            Category(
                name = getString(R.string.category_subsidy),
                iconResName = R.drawable.ic_category_subsidy.drawableName(resources),
                categoryType = CategoryType.Income,
                orderNumber = incomeCategoryOrderNum++
            ),
            Category(
                name = getString(R.string.category_other),
                iconResName = R.drawable.ic_category_other.drawableName(resources),
                categoryType = CategoryType.Income,
                orderNumber = incomeCategoryOrderNum++
            )
        )
    }

    companion object {
        lateinit var database: AppDatabase
        lateinit var defaultAccounts: List<Account>
        lateinit var defaultCategories: List<Category>
    }
}