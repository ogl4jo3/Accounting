package com.ogl4jo3.accounting.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ogl4jo3.accounting.R
import java.util.UUID

//TODO: add parcelize for navigation
@Entity(tableName = "account", indices = [Index(value = ["name"], unique = true)])
data class Account(
    @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "initialAmount") var initialAmount: Int, //起始金額
    @ColumnInfo(name = "category") var category: AccountCategory, //帳戶類別，0:現金、1:卡片、2:銀行
    @ColumnInfo(name = "isDefaultAccount") var isDefaultAccount: Boolean, //是否為預設帳戶
    @ColumnInfo(name = "budgetPrice") var budgetPrice: Int, //預算金額//TODO:
    @ColumnInfo(name = "budgetNotice") var budgetNotice: Float, //預算低於多少百分比提醒//TODO:
    @ColumnInfo(name = "balance") var balance: Int, //帳戶餘額//TODO: check is necessary??, use (initialAmount - expense + income) instead
)

enum class AccountCategory(val nameRes: Int, val iconRes: Int) {
    Cash(R.string.tv_account_category_cash, R.drawable.ic_account_category_cash),
    Card(R.string.tv_account_category_card, R.drawable.ic_account_category_card),
    Bank(R.string.tv_account_category_bank, R.drawable.ic_account_category_bank),
}
