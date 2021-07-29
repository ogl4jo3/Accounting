package com.ogl4jo3.accounting.ui.statistics

import com.ogl4jo3.accounting.data.Category

interface IGetCategory {
    suspend fun getCategoryById(categoryId: String): Category?
}