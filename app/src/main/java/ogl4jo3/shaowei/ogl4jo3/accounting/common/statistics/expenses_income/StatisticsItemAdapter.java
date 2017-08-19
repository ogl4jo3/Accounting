package ogl4jo3.shaowei.ogl4jo3.accounting.common.statistics.expenses_income;

import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ogl4jo3.shaowei.ogl4jo3.accounting.R;
import ogl4jo3.shaowei.ogl4jo3.utility.string.StringUtil;

/**
 * 支出、收入共用 統計Item的Adapter
 * Created by ogl4jo3 on 2017/8/8.
 */

public abstract class StatisticsItemAdapter
		extends RecyclerView.Adapter<StatisticsItemAdapter.ViewHolder> {

	private Context mContext;
	private FragmentManager fragmentManager;
	private List<StatisticsItem> statisticsItems;

	public StatisticsItemAdapter(Context context, FragmentManager fragmentManager,
	                             List<StatisticsItem> statisticsItems) {
		this.mContext = context;
		this.fragmentManager = fragmentManager;
		this.statisticsItems = statisticsItems;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		//return null;

		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_statistics_expenses_income, parent, false);

		return new StatisticsItemAdapter.ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		holder.tvNumber.setText(String.valueOf(position + 1));
		holder.ivIcon.setImageResource(statisticsItems.get(position).getIcon());
		holder.tvName.setText(statisticsItems.get(position).getName());
		holder.tvPercentage.setText(String.valueOf(statisticsItems.get(position).getPercentage()));
		holder.tvPrice.setText(StringUtil.toMoneyStr(statisticsItems.get(position).getPrice()));

		holder.itemView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onItemClick(holder.getAdapterPosition());
			}
		});
	}

	public abstract void onItemClick(int position);

	@Override
	public int getItemCount() {
		return statisticsItems.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {

		TextView tvNumber;
		ImageView ivIcon;
		TextView tvName;
		TextView tvPercentage;
		TextView tvPrice;

		public ViewHolder(View itemView) {
			super(itemView);
			tvNumber = (TextView) itemView.findViewById(R.id.tv_number);
			ivIcon = (ImageView) itemView.findViewById(R.id.iv_category_icon);
			tvName = (TextView) itemView.findViewById(R.id.tv_category_name);
			tvPercentage = (TextView) itemView.findViewById(R.id.tv_percentage);
			tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
		}
	}
}
