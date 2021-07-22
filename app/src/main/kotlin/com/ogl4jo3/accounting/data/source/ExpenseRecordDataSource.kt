package com.ogl4jo3.accounting.data.source

import com.ogl4jo3.accounting.data.ExpenseRecord

interface ExpenseRecordDataSource {
    suspend fun insertExpenseRecord(expenseRecord: ExpenseRecord)

}