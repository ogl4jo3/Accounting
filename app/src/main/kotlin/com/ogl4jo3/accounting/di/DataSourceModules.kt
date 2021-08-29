package com.ogl4jo3.accounting.di

import com.ogl4jo3.accounting.data.source.AccountDataSource
import com.ogl4jo3.accounting.data.source.AccountingNotificationDataSource
import com.ogl4jo3.accounting.data.source.CategoryDataSource
import com.ogl4jo3.accounting.data.source.DefaultAccountDataSource
import com.ogl4jo3.accounting.data.source.DefaultAccountingNotificationDataSource
import com.ogl4jo3.accounting.data.source.DefaultCategoryDataSource
import com.ogl4jo3.accounting.data.source.DefaultExpenseRecordDataSource
import com.ogl4jo3.accounting.data.source.DefaultIncomeRecordDataSource
import com.ogl4jo3.accounting.data.source.ExpenseRecordDataSource
import com.ogl4jo3.accounting.data.source.IncomeRecordDataSource
import org.koin.dsl.module

val dataSourceModules = module {
    factory<AccountDataSource> { DefaultAccountDataSource(get()) }
    factory<CategoryDataSource> { DefaultCategoryDataSource(get()) }
    factory<ExpenseRecordDataSource> { DefaultExpenseRecordDataSource(get()) }
    factory<IncomeRecordDataSource> { DefaultIncomeRecordDataSource(get()) }
    factory<AccountingNotificationDataSource> { DefaultAccountingNotificationDataSource(get()) }
}