package com.ogl4jo3.accounting.ui.statistics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ogl4jo3.accounting.databinding.ItemStatisticsBinding
import kotlinx.coroutines.runBlocking

class StatisticsItemAdapter(
    private val getCategoryImpl: IGetCategory
) :
    ListAdapter<StatisticsItem, StatisticsItemViewHolder>(StatisticsItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsItemViewHolder {
        return StatisticsItemViewHolder(
            ItemStatisticsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: StatisticsItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            //TODO: feature - show record detail list
        }
        holder.binding.apply {
            this.item = item
            this.category = runBlocking { getCategoryImpl.getCategoryById(item.categoryId) }
            executePendingBindings()
        }
    }
}

class StatisticsItemViewHolder(val binding: ItemStatisticsBinding) :
    RecyclerView.ViewHolder(binding.root)

class StatisticsItemDiffCallback : DiffUtil.ItemCallback<StatisticsItem>() {
    override fun areItemsTheSame(
        oldItem: StatisticsItem, newItem: StatisticsItem,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: StatisticsItem, newItem: StatisticsItem,
    ): Boolean = oldItem.amount == newItem.amount
}