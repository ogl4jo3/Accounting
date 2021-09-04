package com.ogl4jo3.accounting.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ogl4jo3.accounting.data.AccountingNotification

@Dao
interface AccountingNotificationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNotification(notification: AccountingNotification): Long

    @Query("SELECT * FROM notification WHERE id = :id")
    suspend fun getNotificationById(id: String): AccountingNotification?

    @Query("SELECT * FROM notification")
    suspend fun getAllNotifications(): List<AccountingNotification>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNotification(notification: AccountingNotification)

    @Delete
    suspend fun deleteNotification(notification: AccountingNotification)

    @Query("SELECT COUNT(id) FROM notification")
    suspend fun getNumberOfNotifications(): Int

}