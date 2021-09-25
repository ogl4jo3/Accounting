package com.ogl4jo3.accounting.ui.statistics

import com.ogl4jo3.accounting.data.Category
import kotlinx.coroutines.CoroutineScope

interface IGetCategory {
    val getCategoryScope: CoroutineScope
    suspend fun getCategoryById(categoryId: String): Category?
}