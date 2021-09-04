package com.ogl4jo3.accounting.di

import android.app.AlarmManager
import android.content.Context
import androidx.preference.PreferenceManager
import com.ogl4jo3.accounting.data.local.DefaultSharedPreferencesHelper
import com.ogl4jo3.accounting.data.local.SharedPreferencesHelper
import com.ogl4jo3.accounting.ui.accountingnotification.AccountingAlarmSetter
import com.ogl4jo3.accounting.ui.accountingnotification.AlarmSetter
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModules = module {
    single { provideSharedPreferencesHelper(androidContext()) }
    factory { androidContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager }
    single<AlarmSetter> { AccountingAlarmSetter(androidApplication(), get()) }
}

private fun provideSharedPreferencesHelper(applicationContext: Context): SharedPreferencesHelper {
    return DefaultSharedPreferencesHelper(
        PreferenceManager.getDefaultSharedPreferences(applicationContext)
    )
}