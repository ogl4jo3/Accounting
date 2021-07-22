package com.ogl4jo3.accounting.data

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

class ConvertersTest {

    private val date = Date()

    @Test
    fun calendarToDatestamp() {
        assertEquals(date.time, Converters().dateToLong(date))
    }

    @Test
    fun datestampToCalendar() {
        assertEquals(Converters().longToDate(date.time), date)
    }
}
