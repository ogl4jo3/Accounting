package com.ogl4jo3.accounting.utils

import com.ogl4jo3.accounting.common.beginOfDay
import com.ogl4jo3.accounting.common.beginOfMonth
import com.ogl4jo3.accounting.common.beginOfYear
import com.ogl4jo3.accounting.common.endOfDay
import com.ogl4jo3.accounting.common.endOfMonth
import com.ogl4jo3.accounting.common.endOfYear
import com.ogl4jo3.accounting.common.nextMonth
import com.ogl4jo3.accounting.common.nextYear
import com.ogl4jo3.accounting.common.previousMonth
import com.ogl4jo3.accounting.common.previousYear
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

    @Test
    fun `test previous month - 1`() {
        val date = Calendar.getInstance().let {
            it[Calendar.YEAR] = 2021
            it[Calendar.MONTH] = 6
            it[Calendar.DAY_OF_MONTH] = 11
            it.time
        }
        val previousMonthCalendar = Calendar.getInstance().apply { time = date.previousMonth }
        println("date: $date")
        println("previousMonthCalendar: $previousMonthCalendar")
        Assert.assertEquals(2021, previousMonthCalendar[Calendar.YEAR])
        Assert.assertEquals(5, previousMonthCalendar[Calendar.MONTH])
        Assert.assertEquals(11, previousMonthCalendar[Calendar.DAY_OF_MONTH])
    }

    @Test
    fun `test previous month - 2`() {
        val date = Calendar.getInstance().let {
            it[Calendar.YEAR] = 2021
            it[Calendar.MONTH] = 0
            it[Calendar.DAY_OF_MONTH] = 11
            it.time
        }
        val previousMonthCalendar = Calendar.getInstance().apply { time = date.previousMonth }
        println("date: $date")
        println("previousMonthCalendar: $previousMonthCalendar")
        Assert.assertEquals(2020, previousMonthCalendar[Calendar.YEAR])
        Assert.assertEquals(11, previousMonthCalendar[Calendar.MONTH])
        Assert.assertEquals(11, previousMonthCalendar[Calendar.DAY_OF_MONTH])
    }

    @Test
    fun `test next month - 1`() {
        val date = Calendar.getInstance().let {
            it[Calendar.YEAR] = 2021
            it[Calendar.MONTH] = 6
            it[Calendar.DAY_OF_MONTH] = 11
            it.time
        }
        val nextMonthCalendar = Calendar.getInstance().apply { time = date.nextMonth }
        println("date: $date")
        println("nextMonthCalendar: $nextMonthCalendar")
        Assert.assertEquals(2021, nextMonthCalendar[Calendar.YEAR])
        Assert.assertEquals(7, nextMonthCalendar[Calendar.MONTH])
        Assert.assertEquals(11, nextMonthCalendar[Calendar.DAY_OF_MONTH])
    }

    @Test
    fun `test next month - 2`() {
        val date = Calendar.getInstance().let {
            it[Calendar.YEAR] = 2021
            it[Calendar.MONTH] = 11
            it[Calendar.DAY_OF_MONTH] = 11
            it.time
        }
        val nextMonthCalendar = Calendar.getInstance().apply { time = date.nextMonth }
        println("date: $date")
        println("nextMonthCalendar: $nextMonthCalendar")
        Assert.assertEquals(2022, nextMonthCalendar[Calendar.YEAR])
        Assert.assertEquals(0, nextMonthCalendar[Calendar.MONTH])
        Assert.assertEquals(11, nextMonthCalendar[Calendar.DAY_OF_MONTH])
    }

    @Test
    fun `test previous year`() {
        val date = Calendar.getInstance().let {
            it[Calendar.YEAR] = 2021
            it[Calendar.MONTH] = 6
            it[Calendar.DAY_OF_MONTH] = 11
            it.time
        }
        val previousYearCalendar = Calendar.getInstance().apply { time = date.previousYear }
        println("date: $date")
        println("previousYearCalendar: $previousYearCalendar")
        Assert.assertEquals(2020, previousYearCalendar[Calendar.YEAR])
        Assert.assertEquals(6, previousYearCalendar[Calendar.MONTH])
        Assert.assertEquals(11, previousYearCalendar[Calendar.DAY_OF_MONTH])
    }

    @Test
    fun `test next year`() {
        val date = Calendar.getInstance().let {
            it[Calendar.YEAR] = 2021
            it[Calendar.MONTH] = 6
            it[Calendar.DAY_OF_MONTH] = 11
            it.time
        }
        val nextYearCalendar = Calendar.getInstance().apply { time = date.nextYear }
        println("date: $date")
        println("nextYearCalendar: $nextYearCalendar")
        Assert.assertEquals(2022, nextYearCalendar[Calendar.YEAR])
        Assert.assertEquals(6, nextYearCalendar[Calendar.MONTH])
        Assert.assertEquals(11, nextYearCalendar[Calendar.DAY_OF_MONTH])
    }


    @Test
    fun `test begin of month`() {
        val date = Calendar.getInstance().let {
            it[Calendar.YEAR] = 2021
            it[Calendar.MONTH] = 6
            it[Calendar.DAY_OF_MONTH] = 11
            it.time
        }
        val beginOfMonthCalendar = Calendar.getInstance().apply { time = date.beginOfMonth }
        println("date: $date")
        println("beginOfMonthCalendar: $beginOfMonthCalendar")
        Assert.assertEquals(2021, beginOfMonthCalendar[Calendar.YEAR])
        Assert.assertEquals(6, beginOfMonthCalendar[Calendar.MONTH])
        Assert.assertEquals(1, beginOfMonthCalendar[Calendar.DAY_OF_MONTH])
    }

    @Test
    fun `test end of month - 1`() {
        val date = Calendar.getInstance().let {
            it[Calendar.YEAR] = 2021
            it[Calendar.MONTH] = 6
            it[Calendar.DAY_OF_MONTH] = 11
            it.time
        }
        val endOfMonthCalendar = Calendar.getInstance().apply { time = date.endOfMonth }
        println("date: $date")
        println("endOfMonthCalendar: $endOfMonthCalendar")
        Assert.assertEquals(2021, endOfMonthCalendar[Calendar.YEAR])
        Assert.assertEquals(6, endOfMonthCalendar[Calendar.MONTH])
        Assert.assertEquals(31, endOfMonthCalendar[Calendar.DAY_OF_MONTH])
    }

    @Test
    fun `test end of month - 2`() {
        val date = Calendar.getInstance().let {
            it[Calendar.YEAR] = 2021
            it[Calendar.MONTH] = 1
            it[Calendar.DAY_OF_MONTH] = 11
            it.time
        }
        val endOfMonthCalendar = Calendar.getInstance().apply { time = date.endOfMonth }
        println("date: $date")
        println("endOfMonthCalendar: $endOfMonthCalendar")
        Assert.assertEquals(2021, endOfMonthCalendar[Calendar.YEAR])
        Assert.assertEquals(1, endOfMonthCalendar[Calendar.MONTH])
        Assert.assertEquals(28, endOfMonthCalendar[Calendar.DAY_OF_MONTH])
    }

    @Test
    fun `test begin of year`() {
        val date = Calendar.getInstance().let {
            it[Calendar.YEAR] = 2021
            it[Calendar.MONTH] = 6
            it[Calendar.DAY_OF_MONTH] = 11
            it.time
        }
        val beginOfYearCalendar = Calendar.getInstance().apply { time = date.beginOfYear }
        println("date: $date")
        println("beginOfYearCalendar: $beginOfYearCalendar")
        Assert.assertEquals(2021, beginOfYearCalendar[Calendar.YEAR])
        Assert.assertEquals(0, beginOfYearCalendar[Calendar.MONTH])
        Assert.assertEquals(1, beginOfYearCalendar[Calendar.DAY_OF_MONTH])
        Assert.assertEquals(1, beginOfYearCalendar[Calendar.DAY_OF_YEAR])
    }

    @Test
    fun `test end of year - 1`() {
        val date = Calendar.getInstance().let {
            it[Calendar.YEAR] = 2021
            it[Calendar.MONTH] = 6
            it[Calendar.DAY_OF_MONTH] = 11
            it.time
        }
        val endOfYearCalendar = Calendar.getInstance().apply { time = date.endOfYear }
        println("date: $date")
        println("endOfYearCalendar: $endOfYearCalendar")
        Assert.assertEquals(2021, endOfYearCalendar[Calendar.YEAR])
        Assert.assertEquals(11, endOfYearCalendar[Calendar.MONTH])
        Assert.assertEquals(31, endOfYearCalendar[Calendar.DAY_OF_MONTH])
        Assert.assertEquals(365, endOfYearCalendar[Calendar.DAY_OF_YEAR])
    }

    @Test
    fun `test end of year - 2`() {
        val date = Calendar.getInstance().let {
            it[Calendar.YEAR] = 2020
            it[Calendar.MONTH] = 6
            it[Calendar.DAY_OF_MONTH] = 11
            it.time
        }
        val endOfYearCalendar = Calendar.getInstance().apply { time = date.endOfYear }
        println("date: $date")
        println("endOfYearCalendar: $endOfYearCalendar")
        Assert.assertEquals(2020, endOfYearCalendar[Calendar.YEAR])
        Assert.assertEquals(11, endOfYearCalendar[Calendar.MONTH])
        Assert.assertEquals(31, endOfYearCalendar[Calendar.DAY_OF_MONTH])
        Assert.assertEquals(366, endOfYearCalendar[Calendar.DAY_OF_YEAR])
    }

}