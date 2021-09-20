package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.common.beginOfDay
import com.ogl4jo3.accounting.common.beginOfMonth
import com.ogl4jo3.accounting.common.beginOfYear
import com.ogl4jo3.accounting.common.endOfDay
import com.ogl4jo3.accounting.common.endOfMonth
import com.ogl4jo3.accounting.common.endOfYear
import com.ogl4jo3.accounting.data.ExpenseRecord
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

class DefaultExpenseRecordDataSource(
    private val expenseRecordDao: ExpenseRecordDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ExpenseRecordDataSource {
    override suspend fun insertExpenseRecord(expenseRecord: ExpenseRecord) =
        withContext(ioDispatcher) {
            expenseRecordDao.insertExpenseRecord(expenseRecord)
        }

    override suspend fun getExpenseRecordsByDate(date: Date): List<ExpenseRecord> =
        withContext(ioDispatcher) {
            return@withContext expenseRecordDao.getExpenseRecordsByDate(
                date.beginOfDay.time, date.endOfDay.time
            )
        }

    override suspend fun getExpenseRecordsByMonth(date: Date): List<ExpenseRecord> =
        withContext(ioDispatcher) {
            return@withContext expenseRecordDao.getExpenseRecordsByDate(
                date.beginOfMonth.time, date.endOfMonth.time
            )
        }

    override suspend fun getExpenseRecordsByYear(date: Date): List<ExpenseRecord> =
        withContext(ioDispatcher) {
            return@withContext expenseRecordDao.getExpenseRecordsByDate(
                date.beginOfYear.time, date.endOfYear.time
            )
        }

    override suspend fun updateExpenseRecord(expenseRecord: ExpenseRecord) =
        withContext(ioDispatcher) {
            expenseRecordDao.updateExpenseRecord(expenseRecord)
        }

    override suspend fun deleteExpenseRecord(expenseRecord: ExpenseRecord) =
        withContext(ioDispatcher) {
            expenseRecordDao.deleteExpenseRecord(expenseRecord)
        }
}