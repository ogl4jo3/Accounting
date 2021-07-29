package com.ogl4jo3.accounting.ui.statistics

import android.content.Context
import com.ogl4jo3.accounting.R
import java.util.Calendar
import java.util.Date


fun Date.yearText(context: Context): String {
    return context.getString(R.string.tab_year_text, Calendar.getInstance().let {
        it.time = this
        it[Calendar.YEAR]
    })
}

fun Date.yearNMonthText(context: Context): String {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return context.getString(
        R.string.tab_year_n_month_text,
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH] + 1
    )
}
