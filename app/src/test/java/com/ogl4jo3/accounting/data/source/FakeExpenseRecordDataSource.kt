package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.data.ExpenseRecord

class FakeExpenseRecordDataSource(
    var expenseRecords: MutableList<ExpenseRecord> = mutableListOf()
) : ExpenseRecordDataSource {

    override suspend fun insertExpenseRecord(expenseRecord: ExpenseRecord) {
        expenseRecords.add(expenseRecord)
    }

}