package com.ogl4jo3.accounting.ui.categoryMgmt

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import com.google.android.material.snackbar.Snackbar
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.data.Category
import com.ogl4jo3.accounting.databinding.ItemCategoryBinding

class CategoryAdapter(val viewModel: CategoryMgmtViewModel) :
    ListAdapter<Category, CategoryViewHolder>(CategoryDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ).apply {
            binding.apply {
                swipeLayoutCategory.showMode = SwipeLayout.ShowMode.PullOut
                swipeLayoutCategory.addDrag(SwipeLayout.DragEdge.Right, tvDelete)
            }
        }
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)
        holder.binding.apply {
            this.category = category
            ivCategoryIcon.setImageResource(
                category.iconResName.drawableId(
                    this.root.context as Activity?,
                    this.root.resources
                )
            )
            swipeLayoutCategory.surfaceView.setOnClickListener {
                viewModel.navigateToItem(category)
            }
            tvDelete.setOnClickListener {
                viewModel.deleteCategory(category, {
                    Snackbar.make(
                        root,
                        root.context.getString(R.string.msg_category_deleted, category.name),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }, {})
            }
            executePendingBindings()
        }
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        val fromItem = getItem(fromPosition)
        val toItem = getItem(toPosition)
        viewModel.swapCategoryOrderNumber(fromItem, toItem)
        //FIXME: workaround, check how to fix order number bug
        notifyItemMoved(fromPosition, toPosition)
    }
}

class CategoryViewHolder(val binding: ItemCategoryBinding) :
    RecyclerView.ViewHolder(binding.root)

class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(
        oldItem: Category, newItem: Category,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: Category, newItem: Category,
    ): Boolean = oldItem.id == newItem.id
}