package com.ogl4jo3.accounting.ui.expense

import org.junit.Assert
import org.junit.Test
import java.util.Date

class ExpenseAddViewModelTest {

    private val viewModel = ExpenseAddViewModel(Date())

    @Test
    fun checkFormat() {
        Assert.assertEquals(
            true,
            viewModel.checkFormat(
                price = 100,
                accountName = "AccountName-1",
                categoryId = 1
            )
        )
        Assert.assertEquals(
            false,
            viewModel.checkFormat(
                price = 0,
                accountName = "AccountName-1",
                categoryId = 1
            )
        )
        Assert.assertEquals(
            false,
            viewModel.checkFormat(
                price = 100,
                accountName = "",
                categoryId = 1
            )
        )
        Assert.assertEquals(
            false,
            viewModel.checkFormat(
                price = 100,
                accountName = "AccountName-1",
                categoryId = -1
            )
        )
    }
}