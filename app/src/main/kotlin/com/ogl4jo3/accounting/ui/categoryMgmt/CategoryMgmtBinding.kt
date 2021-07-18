package com.ogl4jo3.accounting.ui.categoryMgmt

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ogl4jo3.accounting.data.Category

@BindingAdapter("categoryIconList")
fun bindCategoryIconList(recyclerView: RecyclerView, categoryIconList: List<CategoryIcon>) {
    (recyclerView.adapter as CategoryIconAdapter).submitList(categoryIconList)
}

@BindingAdapter("categories")
fun bindCategories(recyclerView: RecyclerView, categories: List<Category>) {
    (recyclerView.adapter as CategoryAdapter).submitList(categories)
}