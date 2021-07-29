package com.ogl4jo3.accounting.ui.statistics

data class StatisticsItem(
    val categoryId: String,
    val amount: Int,
    var percent: Float,
    var orderNumber: Int
)
