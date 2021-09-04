package com.ogl4jo3.accounting.di

import android.content.Context
import androidx.room.Room
import com.ogl4jo3.accounting.data.source.AccountDao
import com.ogl4jo3.accounting.data.source.AccountingNotificationDao
import com.ogl4jo3.accounting.data.source.AppDatabase
import com.ogl4jo3.accounting.data.source.CategoryDao
import com.ogl4jo3.accounting.data.source.ExpenseRecordDao
import com.ogl4jo3.accounting.data.source.IncomeRecordDao
import com.ogl4jo3.accounting.data.source.MIGRATION_1_2
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModules = module {
    single { provideDatabase(androidContext()) }
    single { provideAccountDao(get()) }
    single { provideCategoryDao(get()) }
    single { provideExpenseRecordDao(get()) }
    single { provideIncomeRecordDao(get()) }
    single { provideAccountingNotificationDao(get()) }
}

private fun provideDatabase(applicationContext: Context): AppDatabase {
    return Room.databaseBuilder(
        applicationContext, AppDatabase::class.java, "accounting"
    ).addMigrations(MIGRATION_1_2).build()
}

private fun provideAccountDao(database: AppDatabase): AccountDao {
    return database.accountDao()
}

private fun provideCategoryDao(database: AppDatabase): CategoryDao {
    return database.categoryDao()
}

private fun provideExpenseRecordDao(database: AppDatabase): ExpenseRecordDao {
    return database.expenseRecordDao()
}

private fun provideIncomeRecordDao(database: AppDatabase): IncomeRecordDao {
    return database.incomeRecordDao()
}

private fun provideAccountingNotificationDao(database: AppDatabase): AccountingNotificationDao {
    return database.accountingNotificationDao()
}