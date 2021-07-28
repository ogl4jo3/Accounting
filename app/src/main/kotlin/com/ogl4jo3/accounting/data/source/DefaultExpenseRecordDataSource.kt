package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.common.beginOfDay
import com.ogl4jo3.accounting.common.beginOfMonth
import com.ogl4jo3.accounting.common.beginOfYear
import com.ogl4jo3.accounting.common.endOfDay
import com.ogl4jo3.accounting.common.endOfMonth
import com.ogl4jo3.accounting.common.endOfYear
import com.ogl4jo3.accounting.data.ExpenseRecord
import java.util.Date

class DefaultExpenseRecordDataSource(
    private val expenseRecordDao: ExpenseRecordDao
) : ExpenseRecordDataSource {
    override suspend fun insertExpenseRecord(expenseRecord: ExpenseRecord) {
        expenseRecordDao.insertExpenseRecord(expenseRecord)
    }

    override suspend fun getExpenseRecordsByDate(date: Date): List<ExpenseRecord> {
        return expenseRecordDao.getExpenseRecordsByDate(date.beginOfDay.time, date.endOfDay.time)
    }

    override suspend fun getExpenseRecordsByTime(
        startTime: Long, endTime: Long
    ): List<ExpenseRecord> {
        return expenseRecordDao.getExpenseRecordsByDate(startTime, endTime)
    }

    override suspend fun getExpenseRecordsByMonth(date: Date): List<ExpenseRecord> {
        return expenseRecordDao.getExpenseRecordsByDate(
            date.beginOfMonth.time, date.endOfMonth.time
        )
    }

    override suspend fun getExpenseRecordsByYear(date: Date): List<ExpenseRecord> {
        return expenseRecordDao.getExpenseRecordsByDate(
            date.beginOfYear.time, date.endOfYear.time
        )
    }

    override suspend fun updateExpenseRecord(expenseRecord: ExpenseRecord) {
        expenseRecordDao.updateExpenseRecord(expenseRecord)
    }

    override suspend fun deleteExpenseRecord(expenseRecord: ExpenseRecord) {
        expenseRecordDao.deleteExpenseRecord(expenseRecord)
    }
}