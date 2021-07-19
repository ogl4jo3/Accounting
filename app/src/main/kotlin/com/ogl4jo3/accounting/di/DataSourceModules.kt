package com.ogl4jo3.accounting.di

import com.ogl4jo3.accounting.data.source.AccountDataSource
import com.ogl4jo3.accounting.data.source.CategoryDataSource
import com.ogl4jo3.accounting.data.source.DefaultAccountDataSource
import com.ogl4jo3.accounting.data.source.DefaultCategoryDataSource
import org.koin.dsl.module

val dataSourceModules = module {
    factory<AccountDataSource> { DefaultAccountDataSource(get()) }
    factory<CategoryDataSource> { DefaultCategoryDataSource(get()) }
}