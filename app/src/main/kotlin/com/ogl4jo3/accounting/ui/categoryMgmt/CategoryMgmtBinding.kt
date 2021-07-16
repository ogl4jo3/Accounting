package com.ogl4jo3.accounting.ui.categoryMgmt

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("categoryIconList")
fun bindCategoryIconList(recyclerView: RecyclerView, categoryIconList: List<CategoryIcon>) {
    (recyclerView.adapter as CategoryIconAdapter).submitList(categoryIconList)
}