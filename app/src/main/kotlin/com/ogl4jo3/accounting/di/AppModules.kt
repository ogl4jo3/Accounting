package com.ogl4jo3.accounting.di

import android.content.Context
import androidx.preference.PreferenceManager
import com.ogl4jo3.accounting.data.local.DefaultSharedPreferencesHelper
import com.ogl4jo3.accounting.data.local.SharedPreferencesHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModules = module {
    single { provideSharedPreferencesHelper(androidContext()) }
}

private fun provideSharedPreferencesHelper(applicationContext: Context): SharedPreferencesHelper {
    return DefaultSharedPreferencesHelper(
        PreferenceManager.getDefaultSharedPreferences(applicationContext)
    )
}