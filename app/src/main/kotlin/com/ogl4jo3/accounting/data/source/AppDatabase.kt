package com.ogl4jo3.accounting.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ogl4jo3.accounting.data.Category

@Database(entities = [Category::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
}
