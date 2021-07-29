package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.data.ExpenseRecord
import java.util.Date

interface ExpenseRecordDataSource {
    suspend fun insertExpenseRecord(expenseRecord: ExpenseRecord)
    suspend fun getExpenseRecordsByDate(date: Date): List<ExpenseRecord>
    suspend fun getExpenseRecordsByTime(startTime: Long, endTime: Long): List<ExpenseRecord>
    suspend fun getExpenseRecordsByMonth(date: Date): List<ExpenseRecord>
    suspend fun getExpenseRecordsByYear(date: Date): List<ExpenseRecord>
    suspend fun updateExpenseRecord(expenseRecord: ExpenseRecord)
    suspend fun deleteExpenseRecord(expenseRecord: ExpenseRecord)

}