package com.ogl4jo3.accounting.ui.common

import androidx.databinding.BindingAdapter

@BindingAdapter("pieChartDataList")
fun setPieChartData(pieChart: PieChart, pieChartDataList: List<PieChartData>) {
    pieChart.setData(pieChartDataList)
}