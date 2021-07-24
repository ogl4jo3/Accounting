package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.common.beginOfDay
import com.ogl4jo3.accounting.common.endOfDay
import com.ogl4jo3.accounting.data.ExpenseRecord
import java.util.Date

class DefaultExpenseRecordDataSource(
    val expenseRecordDao: ExpenseRecordDao
) : ExpenseRecordDataSource {
    override suspend fun insertExpenseRecord(expenseRecord: ExpenseRecord) {
        expenseRecordDao.insertExpenseRecord(expenseRecord)
    }

    override suspend fun getExpenseRecordsByDate(date: Date): List<ExpenseRecord> {
        return expenseRecordDao.getExpenseRecordsByDate(date.beginOfDay.time, date.endOfDay.time)
    }
}