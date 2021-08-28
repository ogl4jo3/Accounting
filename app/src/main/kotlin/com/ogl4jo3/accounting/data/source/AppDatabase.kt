package com.ogl4jo3.accounting.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.AccountingNotification
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.Converters
import com.ogl4jo3.accounting.data.ExpenseRecord
import com.ogl4jo3.accounting.data.IncomeRecord

@Database(
    entities = [Category::class, Account::class, ExpenseRecord::class, IncomeRecord::class, AccountingNotification::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun accountDao(): AccountDao
    abstract fun expenseRecordDao(): ExpenseRecordDao
    abstract fun incomeRecordDao(): IncomeRecordDao
    abstract fun accountingNotificationDao(): AccountingNotificationDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `notification` (`id` TEXT NOT NULL, `time` TEXT NOT NULL, `isOn` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_notification_time` ON `notification` (`time`)")
    }
}