package com.ogl4jo3.accounting.data.source

import androidx.room.*
import com.ogl4jo3.accounting.data.Account

@Dao
interface AccountDao {
    //新增
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAccount(vararg account: Account)

    //儲存
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAccount(account: Account)

    //刪除
    @Delete
    suspend fun deleteAccount(account: Account)

    //取得帳戶數量,TODO:

    //取得所有帳戶預算總合,TODO:

    //取得預設帳戶
    @Query("SELECT * FROM account WHERE isDefaultAccount=${true}")
    suspend fun getDefaultAccount(): Account?

    //透過ID取得帳戶
    @Query("SELECT * FROM account WHERE id = :id")
    suspend fun getAccountById(id: String): Account?

    //取得所有帳戶
    @Query("SELECT * FROM account")
    suspend fun getAllAccounts(): List<Account>

    //檢查帳戶名稱是否已存在,TODO:


}