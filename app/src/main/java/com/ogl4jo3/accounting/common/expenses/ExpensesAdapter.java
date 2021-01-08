package com.ogl4jo3.accounting.common.expenses;

import android.app.FragmentManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ogl4jo3.accounting.setting.categorymanagement.Category;
import com.ogl4jo3.accounting.setting.categorymanagement.CategoryDAO;
import com.ogl4jo3.accounting.utility.database.MyDBHelper;
import com.ogl4jo3.accounting.utility.string.StringUtil;
import com.google.gson.Gson;

import java.util.List;

/**
 * 支出item Adapter
 * Created by ogl4jo3 on 2017/7/27.
 */

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ViewHolder> {

	private Context mContext;
	private FragmentManager fragmentManager;
	private List<Expenses> expensesList;

	public ExpensesAdapter(Context context, FragmentManager fragmentManager,
	                       List<Expenses> expensesList) {
		this.mContext = context;
		this.fragmentManager = fragmentManager;
		this.expensesList = expensesList;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		//return null;

		View view = LayoutInflater.from(parent.getContext())
				.inflate(com.ogl4jo3.accounting.R.layout.item_expenses_income, parent, false);

		return new ExpensesAdapter.ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		int categoryID = expensesList.get(position).getCategoryId();
		SQLiteDatabase db = MyDBHelper.getDatabase(mContext);
		Category categoryTmp = new CategoryDAO(db).getExpensesData(categoryID);
		holder.tvCategoryName.setText(categoryTmp.getName());
		holder.ivCategoryIcon.setImageResource(categoryTmp.getIcon());
		holder.tvMoney.setText(StringUtil.toMoneyStr(expensesList.get(position).getPrice()));

		holder.itemView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				//Toast.makeText(mContext, "item click\n" + expensesList.get(position).toString(),
				//		Toast.LENGTH_SHORT).show();
				Expenses expenses = expensesList.get(holder.getAdapterPosition());
				ExpensesNewEditFragment expensesNewEditFragment = ExpensesNewEditFragment
						.newInstance(expenses.getRecordTime(), new Gson().toJson(expenses));
				fragmentManager.beginTransaction()
						.replace(com.ogl4jo3.accounting.R.id.layout_main_content, expensesNewEditFragment, null)
						.addToBackStack(null).commit();
			}
		});
	}

	@Override
	public int getItemCount() {
		return expensesList.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {

		ImageView ivCategoryIcon;
		TextView tvCategoryName;
		TextView tvMoney;

		public ViewHolder(View itemView) {
			super(itemView);
			ivCategoryIcon = (ImageView) itemView.findViewById(com.ogl4jo3.accounting.R.id.iv_category_icon);
			tvCategoryName = (TextView) itemView.findViewById(com.ogl4jo3.accounting.R.id.tv_category_name);
			tvMoney = (TextView) itemView.findViewById(com.ogl4jo3.accounting.R.id.tv_money);
		}
	}
}