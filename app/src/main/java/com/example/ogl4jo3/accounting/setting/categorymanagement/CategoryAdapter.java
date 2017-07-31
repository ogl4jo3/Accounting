package com.example.ogl4jo3.accounting.setting.categorymanagement;

import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.example.ogl4jo3.accounting.R;

import java.util.List;

/**
 * 類別itemAdapter
 * <p>
 * Created by ogl4jo3 on 2017/7/14.
 */

public abstract class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

	private Context mContext;
	private FragmentManager fragmentManager;
	private List<Category> categoryList;

	public CategoryAdapter(Context context, FragmentManager fragmentManager,
	                       List<Category> categoryList) {
		this.mContext = context;
		this.fragmentManager = fragmentManager;
		this.categoryList = categoryList;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		//return null;

		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_category, parent, false);

		return new CategoryAdapter.ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		holder.tvName.setText(categoryList.get(position).getName());
		holder.ivIcon.setImageResource(categoryList.get(position).getIcon());

		holder.swipeLayoutCategory.getSurfaceView().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onItemClick(holder.getAdapterPosition());
			}
		});
		holder.tvDelete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onItemDismiss(holder.getAdapterPosition());
			}
		});
	}

	/*public void onItemMove(int fromPosition, int toPosition) {
		Collections.swap(categoryList, fromPosition, toPosition);
		notifyItemMoved(fromPosition, toPosition);
	}

	public void onItemDismiss(int position) {
		categoryList.remove(position);
		notifyItemRemoved(position);
	}*/
	public abstract void onItemClick(int position);

	public abstract void onItemMove(int fromPosition, int toPosition);

	public abstract void onItemDismiss(int position);

	@Override
	public int getItemCount() {
		return categoryList.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {

		SwipeLayout swipeLayoutCategory;
		TextView tvName;
		ImageView ivIcon;
		TextView tvDelete;

		public ViewHolder(View itemView) {
			super(itemView);
			swipeLayoutCategory = (SwipeLayout) itemView.findViewById(R.id.swipeLayout_category);
			tvName = (TextView) itemView.findViewById(R.id.tv_name);
			ivIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
			tvDelete = (TextView) itemView.findViewById(R.id.tv_delete);
			swipeLayoutCategory.setShowMode(SwipeLayout.ShowMode.PullOut);
			swipeLayoutCategory.addDrag(SwipeLayout.DragEdge.Right,
					swipeLayoutCategory.findViewById(R.id.tv_delete));
		}
	}
}
