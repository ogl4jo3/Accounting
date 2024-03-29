package com.ogl4jo3.accounting.utils

import com.ogl4jo3.accounting.data.Account
import com.ogl4jo3.accounting.data.AccountCategory
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.data.CategoryType
import com.ogl4jo3.accounting.data.ExpenseRecord
import java.util.Calendar
import java.util.Date

val testAccounts: List<Account> = listOf(
    Account("0", "AccountName-Cash", 1000, AccountCategory.Cash, true),
    Account("1", "AccountName-Bank", 500, AccountCategory.Bank, false),
    Account("2", "AccountName-Card", 300, AccountCategory.Card, false)
)

val testExpenseCategories: List<Category> = listOf(
    Category("0", "Lunch", 0, "R.drawable.lunch", CategoryType.Expense),
    Category("1", "Dinner", 1, "R.drawable.dinner", CategoryType.Expense),
    Category("2", "AfternoonTea", 2, "R.drawable.afternoon_tea", CategoryType.Expense),
    Category("3", "Dessert", 3, "R.drawable.dessert", CategoryType.Expense),
    Category("4", "Fitness", 4, "R.drawable.fitness", CategoryType.Expense)
)

val testIncomeCategories: List<Category> = listOf(
    Category("0", "Salary", 0, "R.drawable.salary", CategoryType.Expense),
    Category("1", "Bonus", 1, "R.drawable.bonus", CategoryType.Expense),
    Category("2", "Investment", 2, "R.drawable.investment", CategoryType.Expense),
)

val testDateArray: Array<Date> = arrayOf(
    Calendar.getInstance().let {
        it[Calendar.YEAR] = 2021
        it[Calendar.MONTH] = 6
        it[Calendar.DAY_OF_MONTH] = 11
        it.time
    }, Calendar.getInstance().let {
        it[Calendar.YEAR] = 2021
        it[Calendar.MONTH] = 6
        it[Calendar.DAY_OF_MONTH] = 12
        it.time
    }, Calendar.getInstance().let {
        it[Calendar.YEAR] = 2021
        it[Calendar.MONTH] = 6
        it[Calendar.DAY_OF_MONTH] = 13
        it.time
    }, Calendar.getInstance().let {
        it[Calendar.YEAR] = 2021
        it[Calendar.MONTH] = 5
        it[Calendar.DAY_OF_MONTH] = 13
        it.time
    }, Calendar.getInstance().let {
        it[Calendar.YEAR] = 2021
        it[Calendar.MONTH] = 5
        it[Calendar.DAY_OF_MONTH] = 1
        it.time
    }, Calendar.getInstance().let {
        it[Calendar.YEAR] = 2021
        it[Calendar.MONTH] = 4
        it[Calendar.DAY_OF_MONTH] = 13
        it.time
    }, Calendar.getInstance().let {
        it[Calendar.YEAR] = 2020
        it[Calendar.MONTH] = 4
        it[Calendar.DAY_OF_MONTH] = 13
        it.time
    }, Calendar.getInstance().let {
        it[Calendar.YEAR] = 2020
        it[Calendar.MONTH] = 3
        it[Calendar.DAY_OF_MONTH] = 13
        it.time
    }
)

val testExpenseRecords: List<ExpenseRecord> = listOf(
    ExpenseRecord(
        "0",
        50,
        testAccounts[0].id,
        testExpenseCategories[0].id,
        "測試用支出",
        testDateArray[0]
    ),
    ExpenseRecord(
        "1",
        150,
        testAccounts[0].id,
        testExpenseCategories[1].id,
        "測試用支出",
        testDateArray[1]
    ),
    ExpenseRecord(
        "2",
        2150,
        testAccounts[1].id,
        testExpenseCategories[2].id,
        "測試用支出",
        testDateArray[2]
    ),
    ExpenseRecord(
        "3",
        2150,
        testAccounts[1].id,
        testExpenseCategories[2].id,
        "測試用支出",
        testDateArray[3]
    ),
    ExpenseRecord(
        "4",
        2150,
        testAccounts[1].id,
        testExpenseCategories[2].id,
        "測試用支出",
        testDateArray[4]
    ),
    ExpenseRecord(
        "5",
        2150,
        testAccounts[1].id,
        testExpenseCategories[2].id,
        "測試用支出",
        testDateArray[5]
    ),
    ExpenseRecord(
        "6",
        2150,
        testAccounts[1].id,
        testExpenseCategories[2].id,
        "測試用支出",
        testDateArray[6]
    ),
    ExpenseRecord(
        "7",
        2150,
        testAccounts[1].id,
        testExpenseCategories[2].id,
        "測試用支出",
        testDateArray[7]
    ),
    ExpenseRecord(
        "8",
        2150,
        testAccounts[1].id,
        testExpenseCategories[2].id,
        "測試用支出",
        testDateArray[7]
    )
)