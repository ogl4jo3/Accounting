package com.ogl4jo3.accounting.common.income;

import androidx.fragment.app.FragmentManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ogl4jo3.accounting.R;
import com.ogl4jo3.accounting.setting.categorymanagement.Category;
import com.ogl4jo3.accounting.setting.categorymanagement.CategoryDAO;
import com.ogl4jo3.accounting.utils.database.MyDBHelper;
import com.ogl4jo3.accounting.utils.string.StringUtil;
import com.google.gson.Gson;

import java.util.List;

/**
 * 收入item Adapter
 * Created by ogl4jo3 on 2017/7/27.
 */

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.ViewHolder> {

	private Context mContext;
	private FragmentManager fragmentManager;
	private List<Income> incomeList;

	public IncomeAdapter(Context context, FragmentManager fragmentManager,
	                     List<Income> incomeList) {
		this.mContext = context;
		this.fragmentManager = fragmentManager;
		this.incomeList = incomeList;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		//return null;

		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_expenses_income, parent, false);

		return new IncomeAdapter.ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		int categoryID = incomeList.get(position).getCategoryId();
		SQLiteDatabase db = MyDBHelper.getDatabase(mContext);
		Category categoryTmp = new CategoryDAO(db).getIncomeData(categoryID);
		holder.tvCategoryName.setText(categoryTmp.getName());
		holder.ivCategoryIcon.setImageResource(categoryTmp.getIcon());
		holder.tvMoney.setText(StringUtil.toMoneyStr(incomeList.get(position).getPrice()));

		holder.itemView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				//Toast.makeText(mContext, "item click\n" + incomeList.get(position).toString(),
				//		Toast.LENGTH_SHORT).show();
				Income income = incomeList.get(holder.getAdapterPosition());
				IncomeNewEditFragment incomeNewEditFragment = IncomeNewEditFragment
						.newInstance(income.getRecordTime(), new Gson().toJson(income));
				fragmentManager.beginTransaction()
						.replace(R.id.layout_main_content, incomeNewEditFragment, null)
						.addToBackStack(null).commit();
			}
		});
	}

	@Override
	public int getItemCount() {
		return incomeList.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {

		ImageView ivCategoryIcon;
		TextView tvCategoryName;
		TextView tvMoney;

		public ViewHolder(View itemView) {
			super(itemView);
			ivCategoryIcon = (ImageView) itemView.findViewById(R.id.iv_category_icon);
			tvCategoryName = (TextView) itemView.findViewById(R.id.tv_category_name);
			tvMoney = (TextView) itemView.findViewById(R.id.tv_money);
		}
	}
}