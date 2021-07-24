package com.ogl4jo3.accounting.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ogl4jo3.accounting.data.IncomeRecord

@Dao
interface IncomeRecordDao {
    @Insert
    suspend fun insertIncomeRecord(incomeRecord: IncomeRecord)

    @Query("SELECT * FROM incomeRecord")
    suspend fun getAllRecords(): List<IncomeRecord>

    @Query("SELECT * FROM incomeRecord WHERE recordTime BETWEEN :startTime AND :endTime")
    suspend fun getIncomeRecordsByDate(startTime: Long, endTime: Long): List<IncomeRecord>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateIncomeRecord(incomeRecord: IncomeRecord)

    @Delete
    suspend fun deleteIncomeRecord(incomeRecord: IncomeRecord)
}