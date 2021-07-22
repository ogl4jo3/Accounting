package com.ogl4jo3.accounting.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ogl4jo3.accounting.data.ExpenseRecord

@Dao
interface ExpenseRecordDao {
    @Insert
    suspend fun insertExpenseRecord(expenseRecord: ExpenseRecord)

    @Query("SELECT * FROM expenseRecord")
    suspend fun getAllRecords(): List<ExpenseRecord>

}