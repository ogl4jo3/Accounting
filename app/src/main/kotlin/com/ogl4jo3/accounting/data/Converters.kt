package com.ogl4jo3.accounting.data

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun dateToLong(date: Date): Long = date.time

    @TypeConverter
    fun longToDate(value: Long): Date = Date(value)
}