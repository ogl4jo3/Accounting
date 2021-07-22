package com.ogl4jo3.accounting.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.Converters
import com.ogl4jo3.accounting.data.ExpenseRecord

@Database(
    entities = [Category::class, Account::class, ExpenseRecord::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun accountDao(): AccountDao
    abstract fun expenseRecordDao(): ExpenseRecordDao
}
