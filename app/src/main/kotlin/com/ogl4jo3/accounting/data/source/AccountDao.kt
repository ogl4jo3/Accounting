package com.ogl4jo3.accounting.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ogl4jo3.accounting.data.Account

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAccount(account: Account): Long

    @Query("SELECT * FROM account WHERE id = :id")
    suspend fun getAccountById(id: String): Account?

    @Query("SELECT * FROM account")
    suspend fun getAllAccounts(): List<Account>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAccount(account: Account)

    @Delete
    suspend fun deleteAccount(account: Account)

    @Query("SELECT COUNT(id) FROM account")
    suspend fun getNumberOfAccounts(): Int

    @Query("SELECT COUNT(id) FROM account WHERE name = :name")
    suspend fun getNumberOfAccountsByName(name: String): Int

    @Query("SELECT COUNT(id) FROM account WHERE name = :name AND id != :excludeId")
    suspend fun getNumberOfAccountsByName(name: String, excludeId: String): Int

    @Query("SELECT * FROM account WHERE isDefaultAccount = 1")
    suspend fun getDefaultAccount(): Account?

    @Query("SELECT COUNT(id) FROM account WHERE isDefaultAccount = 1 AND id != :excludeId")
    suspend fun hasDefaultAccount(excludeId: String): Int

    @Query("UPDATE account SET isDefaultAccount = 0 WHERE id != :excludeId")
    suspend fun resetDefaultAccountExceptId(excludeId: String)

    @Query("UPDATE account SET isDefaultAccount = 1 WHERE id == :id")
    suspend fun setDefaultAccount(id: String)

    //取得所有帳戶預算總合,TODO:

}