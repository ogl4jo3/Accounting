package com.ogl4jo3.accounting

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.AccountCategory
import com.ogl4jo3.accounting.data.AccountingNotification
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.CategoryType
import com.ogl4jo3.accounting.data.source.AccountDataSource
import com.ogl4jo3.accounting.data.source.AccountingNotificationDataSource
import com.ogl4jo3.accounting.data.source.CategoryDataSource
import com.ogl4jo3.accounting.di.appModules
import com.ogl4jo3.accounting.di.dataSourceModules
import com.ogl4jo3.accounting.di.databaseModules
import com.ogl4jo3.accounting.di.viewModelModules
import com.ogl4jo3.accounting.ui.categoryMgmt.drawableName
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class AccountingApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@AccountingApplication)
            modules(
                appModules,
                databaseModules,
                dataSourceModules,
                viewModelModules
            )
        }

        createChannel(
            getString(R.string.accounting_notification_channel_id),
            getString(R.string.accounting_notification_channel_name)
        )

        initDefaultAccounts()//TODO: workaround, maybe can add this in Koin modules, RoomDB.addCallBack??
        initDefaultCategories()//TODO: workaround, maybe can add this in Koin modules, RoomDB.addCallBack??
        initDefaultNotifications()

    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(false)
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                description = getString(R.string.accounting_notification_channel_description)
            }

            getSystemService(NotificationManager::class.java)
                ?.createNotificationChannel(notificationChannel)
        }
    }

    private fun initDefaultAccounts() {
        GlobalScope.launch {
            val accountDataSource: AccountDataSource by inject()
            if (accountDataSource.getNumberOfAccounts() <= 0) {
                getDefaultAccounts().forEach { accountDataSource.insertAccount(it) }
            }
        }
    }

    private fun initDefaultCategories() {
        GlobalScope.launch {
            val categoryDataSource: CategoryDataSource by inject()
            if (categoryDataSource.getNumberOfCategories(CategoryType.Expense) <= 0) {
                getDefaultCategories().filter { it.categoryType == CategoryType.Expense }
                    .forEach { categoryDataSource.insertCategory(it) }
            }
            if (categoryDataSource.getNumberOfCategories(CategoryType.Income) <= 0) {
                getDefaultCategories().filter { it.categoryType == CategoryType.Income }
                    .forEach { categoryDataSource.insertCategory(it) }
            }
        }
    }

    private fun initDefaultNotifications() {
        GlobalScope.launch {
            val notificationDataSource: AccountingNotificationDataSource by inject()
            if (notificationDataSource.getNumberOfNotifications() <= 0) {
                getDefaultNotifications().forEach {
                    notificationDataSource.insertNotification(it)
                }
            }
        }
    }

    private fun getDefaultAccounts(): List<Account> = listOf(
        Account(
            name = getString(R.string.default_cash_account_name),
            initialAmount = 0,
            category = AccountCategory.Cash,
            isDefaultAccount = true,
        ),
        Account(
            name = getString(R.string.default_bank_account_name),
            initialAmount = 0,
            category = AccountCategory.Bank,
            isDefaultAccount = false,
        ),
        Account(
            name = getString(R.string.default_card_account_name),
            initialAmount = 0,
            category = AccountCategory.Card,
            isDefaultAccount = false,
        )
    )

    private fun getDefaultCategories(): List<Category> = listOf(
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
        Category(
            name = getString(R.string.category_fitness),
            iconResName = R.drawable.ic_category_fitness.drawableName(resources),
            categoryType = CategoryType.Expense,
        ),
        Category(
            name = getString(R.string.category_house),
            iconResName = R.drawable.ic_category_house.drawableName(resources),
            categoryType = CategoryType.Expense,
        ),
        Category(
            name = getString(R.string.category_house_1),
            iconResName = R.drawable.ic_category_house_1.drawableName(resources),
            categoryType = CategoryType.Expense,
        ),
        Category(
            name = getString(R.string.category_gift),
            iconResName = R.drawable.ic_category_gift.drawableName(resources),
            categoryType = CategoryType.Expense,
        ),
        Category(
            name = getString(R.string.category_medical),
            iconResName = R.drawable.ic_category_medical.drawableName(resources),
            categoryType = CategoryType.Expense,
        ),
        Category(
            name = getString(R.string.category_parenting),
            iconResName = R.drawable.ic_category_parenting.drawableName(resources),
            categoryType = CategoryType.Expense,
        ),
        Category(
            name = getString(R.string.category_pet),
            iconResName = R.drawable.ic_category_pet.drawableName(resources),
            categoryType = CategoryType.Expense,
        ),
        Category(
            name = getString(R.string.category_phone),
            iconResName = R.drawable.ic_category_phone.drawableName(resources),
            categoryType = CategoryType.Expense,
        ),
        Category(
            name = getString(R.string.category_refueling),
            iconResName = R.drawable.ic_category_refueling.drawableName(resources),
            categoryType = CategoryType.Expense,
        ),
        Category(
            name = getString(R.string.category_swim),
            iconResName = R.drawable.ic_category_swim.drawableName(resources),
            categoryType = CategoryType.Expense,
        ),
        Category(
            name = getString(R.string.category_traffic),
            iconResName = R.drawable.ic_category_traffic.drawableName(resources),
            categoryType = CategoryType.Expense,
        ),
        Category(
            name = getString(R.string.category_video_game),
            iconResName = R.drawable.ic_category_video_game.drawableName(resources),
            categoryType = CategoryType.Expense,
        ),
        Category(
            name = getString(R.string.category_other),
            iconResName = R.drawable.ic_category_other.drawableName(resources),
            categoryType = CategoryType.Expense,
        ),
        // Income Categories
        Category(
            name = getString(R.string.category_salary),
            iconResName = R.drawable.ic_category_salary.drawableName(resources),
            categoryType = CategoryType.Income,
        ),
        Category(
            name = getString(R.string.category_bonus),
            iconResName = R.drawable.ic_category_bonus.drawableName(resources),
            categoryType = CategoryType.Income,
        ),
        Category(
            name = getString(R.string.category_investment),
            iconResName = R.drawable.ic_category_investment.drawableName(resources),
            categoryType = CategoryType.Income,
        ),
        Category(
            name = getString(R.string.category_subsidy),
            iconResName = R.drawable.ic_category_subsidy.drawableName(resources),
            categoryType = CategoryType.Income,
        ),
        Category(
            name = getString(R.string.category_other),
            iconResName = R.drawable.ic_category_other.drawableName(resources),
            categoryType = CategoryType.Income,
        )
    )

    private fun getDefaultNotifications(): List<AccountingNotification> = listOf(
        AccountingNotification(hour = 21, minute = 30, isOn = false),
    )
}