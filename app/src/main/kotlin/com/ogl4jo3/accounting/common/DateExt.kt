package com.ogl4jo3.accounting.common

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

val simpleDateFormat get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

val Date.simpleDateString: String get() = simpleDateFormat.format(this)

val Date.beginOfDay: Date
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.time
    }

val Date.endOfDay: Date
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar[Calendar.HOUR_OF_DAY] = 23
        calendar[Calendar.MINUTE] = 59
        calendar[Calendar.SECOND] = 59
        calendar[Calendar.MILLISECOND] = 999
        return calendar.time
    }

val Date.previousMonth: Date
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar.add(Calendar.MONTH, -1)
        return calendar.time
    }

val Date.nextMonth: Date
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar.add(Calendar.MONTH, 1)
        return calendar.time
    }

val Date.previousYear: Date
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar.add(Calendar.YEAR, -1)
        return calendar.time
    }

val Date.nextYear: Date
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar.add(Calendar.YEAR, 1)
        return calendar.time
    }

val Date.beginOfMonth: Date
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar[Calendar.DAY_OF_MONTH] = 1
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.time
    }

val Date.endOfMonth: Date
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar[Calendar.DAY_OF_MONTH] = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.time
    }

val Date.beginOfYear: Date
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar[Calendar.DAY_OF_YEAR] = 1
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.time
    }

val Date.endOfYear: Date
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar[Calendar.DAY_OF_YEAR] = calendar.getActualMaximum(Calendar.DAY_OF_YEAR)
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.time
    }