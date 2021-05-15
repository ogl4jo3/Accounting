package com.ogl4jo3.accounting.ui.categoryMgmt

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.setting.categorymanagement.Category

/**
 * 類別itemAdapter
 *
 *
 * Created by ogl4jo3 on 2017/7/14.
 */
abstract class CategoryAdapter(
    private val mContext: Context, private val fragmentManager: FragmentManager,
    private val categoryList: List<Category>
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //return null;
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = categoryList[position].name
        holder.ivIcon.setImageResource(categoryList[position].icon)
        holder.swipeLayoutCategory.surfaceView.setOnClickListener { onItemClick(holder) }
        holder.tvDelete.setOnClickListener { onItemDismiss(holder.adapterPosition) }
    }

    /*public void onItemMove(int fromPosition, int toPosition) {
		Collections.swap(categoryList, fromPosition, toPosition);
		notifyItemMoved(fromPosition, toPosition);
	}

	public void onItemDismiss(int position) {
		categoryList.remove(position);
		notifyItemRemoved(position);
	}*/
    abstract fun onItemClick(holder: ViewHolder)
    abstract fun onItemMove(fromPosition: Int, toPosition: Int)
    abstract fun onItemDismiss(position: Int)
    override fun getItemCount(): Int {
        return categoryList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var swipeLayoutCategory: SwipeLayout
        var tvName: TextView
        var ivIcon: ImageView
        var tvDelete: TextView

        init {
            swipeLayoutCategory =
                itemView.findViewById<View>(R.id.swipeLayout_category) as SwipeLayout
            tvName = itemView.findViewById<View>(R.id.tv_name) as TextView
            ivIcon = itemView.findViewById<View>(R.id.iv_icon) as ImageView
            tvDelete = itemView.findViewById<View>(R.id.tv_delete) as TextView
            swipeLayoutCategory.showMode = SwipeLayout.ShowMode.PullOut
            swipeLayoutCategory.addDrag(
                SwipeLayout.DragEdge.Right,
                swipeLayoutCategory.findViewById(R.id.tv_delete)
            )
        }
    }
}