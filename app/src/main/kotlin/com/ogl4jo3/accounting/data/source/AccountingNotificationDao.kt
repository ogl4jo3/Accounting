package com.ogl4jo3.accounting.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ogl4jo3.accounting.data.AccountingNotification
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountingNotificationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNotification(notification: AccountingNotification): Long

    @Query("SELECT * FROM notification WHERE id = :id")
    fun getNotificationById(id: String): Flow<AccountingNotification?>

    @Query("SELECT * FROM notification")
    fun getAllNotifications(): Flow<List<AccountingNotification>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNotification(notification: AccountingNotification)

    @Query("SELECT COUNT(id) FROM notification")
    fun getNumberOfNotifications(): Flow<Int>

}