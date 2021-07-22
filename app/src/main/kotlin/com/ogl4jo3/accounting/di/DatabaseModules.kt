package com.ogl4jo3.accounting.di

import android.content.Context
import androidx.room.Room
import com.ogl4jo3.accounting.data.source.AccountDao
import com.ogl4jo3.accounting.data.source.AppDatabase
import com.ogl4jo3.accounting.data.source.CategoryDao
import com.ogl4jo3.accounting.data.source.ExpenseRecordDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModules = module {
    single { provideDatabase(androidContext()) }
    single { provideAccountDao(get()) }
    single { provideCategoryDao(get()) }
    single { provideExpenseRecordDao(get()) }
}

private fun provideDatabase(applicationContext: Context): AppDatabase {
    return Room.databaseBuilder(
        applicationContext, AppDatabase::class.java, "accounting"
    ).build()
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