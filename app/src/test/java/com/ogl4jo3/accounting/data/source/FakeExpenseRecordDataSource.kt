package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.common.beginOfDay
import com.ogl4jo3.accounting.common.beginOfMonth
import com.ogl4jo3.accounting.common.beginOfYear
import com.ogl4jo3.accounting.common.endOfDay
import com.ogl4jo3.accounting.common.endOfMonth
import com.ogl4jo3.accounting.common.endOfYear
import com.ogl4jo3.accounting.data.ExpenseRecord
import java.util.Date

class FakeExpenseRecordDataSource(
    var expenseRecords: MutableList<ExpenseRecord> = mutableListOf()
) : ExpenseRecordDataSource {

    override suspend fun insertExpenseRecord(expenseRecord: ExpenseRecord) {
        expenseRecords.add(expenseRecord)
    }

    override suspend fun getExpenseRecordsByDate(date: Date): List<ExpenseRecord> {
        return expenseRecords.filter {
            it.recordTime.after(date.beginOfDay) && it.recordTime.before(date.endOfDay)
        }
    }

    override suspend fun getExpenseRecordsByMonth(date: Date): List<ExpenseRecord> {
        return expenseRecords.filter {
            it.recordTime.after(date.beginOfMonth) && it.recordTime.before(date.endOfMonth)
        }
    }

    override suspend fun getExpenseRecordsByYear(date: Date): List<ExpenseRecord> {
        return expenseRecords.filter {
            it.recordTime.after(date.beginOfYear) && it.recordTime.before(date.endOfYear)
        }
    }

    override suspend fun updateExpenseRecord(expenseRecord: ExpenseRecord) {
        expenseRecords.replaceAll { if (it.expenseRecordId == expenseRecord.expenseRecordId) expenseRecord else it }
    }

    override suspend fun deleteExpenseRecord(expenseRecord: ExpenseRecord) {
        expenseRecords.remove(expenseRecord)
    }

    fun getRecord(index: Int): ExpenseRecord {
        return expenseRecords[index].copy()
    }

}