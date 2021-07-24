package com.ogl4jo3.accounting.utils

import com.ogl4jo3.accounting.common.beginOfDay
import com.ogl4jo3.accounting.common.endOfDay
import com.ogl4jo3.accounting.common.simpleDate
import com.ogl4jo3.accounting.common.simpleDateString
import org.junit.Assert
import org.junit.Test
import java.util.Calendar
import java.util.Date

class DateExtTest {

    @Test
    fun `test simpleDateString`() {
        val dateCalendar = Calendar.getInstance()
        dateCalendar[Calendar.YEAR] = 2020
        dateCalendar[Calendar.MONTH] = 7
        dateCalendar[Calendar.DAY_OF_MONTH] = 20
        val date = dateCalendar.time
        val dateStr = date.simpleDateString
        println("date: $date")
        println("dateStr: $dateStr")
        Assert.assertEquals("2020-08-20", dateStr)
    }

    @Test
    fun `test simpleDate convert`() {
        val date = Date()
        val dateStr = date.simpleDateString
        val convertedDate = dateStr.simpleDate ?: return Assert.fail()
        println("date: $date")
        println("dateStr: $dateStr")
        println("convertedDate: $convertedDate")

        val dateCalendar = Calendar.getInstance()
        dateCalendar.time = date

        val convertedDateCalendar = Calendar.getInstance()
        convertedDateCalendar.time = convertedDate

        Assert.assertEquals(
            dateCalendar.get(Calendar.YEAR), convertedDateCalendar.get(Calendar.YEAR)
        )
        Assert.assertEquals(
            dateCalendar.get(Calendar.MONTH), convertedDateCalendar.get(Calendar.MONTH)
        )
        Assert.assertEquals(
            dateCalendar.get(Calendar.DAY_OF_MONTH),
            convertedDateCalendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    @Test
    fun `test date get begin of day`() {
        val date = Date()
        val beginOfDay = date.beginOfDay
        println("date: $date")
        println("beginOfDay: $beginOfDay")

        val dateCalendar = Calendar.getInstance()
        dateCalendar.time = date

        val beginOfDayCalendar = Calendar.getInstance()
        beginOfDayCalendar.time = beginOfDay

        Assert.assertEquals(
            dateCalendar.get(Calendar.DAY_OF_WEEK), beginOfDayCalendar.get(Calendar.DAY_OF_WEEK)
        )
        Assert.assertEquals(
            dateCalendar.get(Calendar.MONTH), beginOfDayCalendar.get(Calendar.MONTH)
        )
        Assert.assertEquals(
            dateCalendar.get(Calendar.DAY_OF_MONTH), beginOfDayCalendar.get(Calendar.DAY_OF_MONTH)
        )
        Assert.assertEquals(0, beginOfDayCalendar.get(Calendar.HOUR_OF_DAY))
        Assert.assertEquals(0, beginOfDayCalendar.get(Calendar.MINUTE))
        Assert.assertEquals(0, beginOfDayCalendar.get(Calendar.SECOND))
        Assert.assertEquals(
            dateCalendar.get(Calendar.YEAR), beginOfDayCalendar.get(Calendar.YEAR)
        )
    }

    @Test
    fun `test date get end of day`() {
        val date = Date()
        val endOfDay = date.endOfDay
        println("date: $date")
        println("endOfDay: $endOfDay")

        val dateCalendar = Calendar.getInstance()
        dateCalendar.time = date

        val endOfDayCalendar = Calendar.getInstance()
        endOfDayCalendar.time = endOfDay

        Assert.assertEquals(
            dateCalendar.get(Calendar.DAY_OF_WEEK), endOfDayCalendar.get(Calendar.DAY_OF_WEEK)
        )
        Assert.assertEquals(
            dateCalendar.get(Calendar.MONTH), endOfDayCalendar.get(Calendar.MONTH)
        )
        Assert.assertEquals(
            dateCalendar.get(Calendar.DAY_OF_MONTH), endOfDayCalendar.get(Calendar.DAY_OF_MONTH)
        )
        Assert.assertEquals(23, endOfDayCalendar.get(Calendar.HOUR_OF_DAY))
        Assert.assertEquals(59, endOfDayCalendar.get(Calendar.MINUTE))
        Assert.assertEquals(59, endOfDayCalendar.get(Calendar.SECOND))
        Assert.assertEquals(
            dateCalendar.get(Calendar.YEAR), endOfDayCalendar.get(Calendar.YEAR)
        )
    }

}