package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.common.beginOfDay
import com.ogl4jo3.accounting.common.endOfDay
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
}