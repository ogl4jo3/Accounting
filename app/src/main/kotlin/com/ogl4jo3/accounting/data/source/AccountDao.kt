package com.ogl4jo3.accounting.data.source

import androidx.room.*
import com.ogl4jo3.accounting.data.Account

@Dao
interface AccountDao {
    //新增
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAccount(account: Account): Long

    //儲存
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAccount(account: Account)

    //刪除
    @Delete
    suspend fun deleteAccount(account: Account)

    //取得帳戶數量
    @Query("SELECT COUNT(id) FROM account")
    suspend fun getNumberOfAccounts(): Int

    //取得所有帳戶預算總合,TODO:

    //取得預設帳戶
    @Query("SELECT * FROM account WHERE isDefaultAccount = 1")
    suspend fun getDefaultAccount(): Account?

    //透過ID取得帳戶
    @Query("SELECT * FROM account WHERE id = :id")
    suspend fun getAccountById(id: String): Account?

    //取得所有帳戶
    @Query("SELECT * FROM account")
    suspend fun getAllAccounts(): List<Account>

    //檢查帳戶名稱是否已存在,TODO:


    //更新預設帳戶
    @Query("UPDATE account SET isDefaultAccount = 0 WHERE id != :id")
    suspend fun updateDefaultAccount(id: String)

}