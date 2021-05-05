package com.ogl4jo3.accounting

import android.app.Application
import androidx.room.Room
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

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger()
            androidContext(this@AccountingApplication)
            modules(appModule)
        }

    }

    companion object {
        lateinit var database: AppDatabase
    }
}