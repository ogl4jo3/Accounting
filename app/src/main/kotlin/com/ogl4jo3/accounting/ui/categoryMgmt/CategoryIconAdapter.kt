package com.ogl4jo3.accounting.ui.categoryMgmt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ogl4jo3.accounting.databinding.ItemCategoryIconBinding
import com.ogl4jo3.accounting.ui.common.extensions.hideKeyboard


class CategoryIconAdapter(
    private val onItemClick: (categoryIcon: CategoryIcon) -> Unit = { },
) :
    ListAdapter<CategoryIcon, CategoryIconViewHolder>(CategoryIconDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryIconViewHolder {
        return CategoryIconViewHolder(
            ItemCategoryIconBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryIconViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            it.hideKeyboard()
            onItemClick(item)
        }
        holder.binding.apply {
            this.ivCategoryIcon.setImageResource(item.iconRes)
            executePendingBindings()
        }
    }

}

class CategoryIconViewHolder(val binding: ItemCategoryIconBinding) :
    RecyclerView.ViewHolder(binding.root)

class CategoryIconDiffCallback : DiffUtil.ItemCallback<CategoryIcon>() {
    override fun areItemsTheSame(
        oldItem: CategoryIcon, newItem: CategoryIcon,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: CategoryIcon, newItem: CategoryIcon,
    ): Boolean = oldItem.iconRes == newItem.iconRes
}