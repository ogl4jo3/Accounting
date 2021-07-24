package com.ogl4jo3.accounting.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ogl4jo3.accounting.data.ExpenseRecord

@Dao
interface ExpenseRecordDao {
    @Insert
    suspend fun insertExpenseRecord(expenseRecord: ExpenseRecord)

    @Query("SELECT * FROM expenseRecord")
    suspend fun getAllRecords(): List<ExpenseRecord>

    @Query("SELECT * FROM expenseRecord WHERE recordTime BETWEEN :startTime AND :endTime")
    suspend fun getExpenseRecordsByDate(startTime: Long, endTime: Long): List<ExpenseRecord>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateExpenseRecord(expenseRecord: ExpenseRecord)

    @Delete
    suspend fun deleteExpenseRecord(expenseRecord: ExpenseRecord)
}