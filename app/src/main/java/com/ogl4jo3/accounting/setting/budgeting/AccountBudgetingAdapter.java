package com.ogl4jo3.accounting.setting.budgeting;

import android.app.FragmentManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ogl4jo3.accounting.common.expenses.ExpensesDAO;
import com.ogl4jo3.accounting.setting.accountmanagement.Account;
import com.ogl4jo3.accounting.utility.database.MyDBHelper;
import com.ogl4jo3.accounting.utility.date.DateUtil;
import com.ogl4jo3.accounting.utility.sharedpreferences.SharedPreferencesHelper;
import com.ogl4jo3.accounting.utility.sharedpreferences.SharedPreferencesTag;
import com.ogl4jo3.accounting.utility.string.StringUtil;
import com.google.gson.Gson;

import java.util.Date;
import java.util.List;

/**
 * 帳戶預算 Adapter
 * Created by ogl4jo3 on 2017/8/13.
 */

public class AccountBudgetingAdapter
		extends RecyclerView.Adapter<AccountBudgetingAdapter.ViewHolder> {

	private Context mContext;
	private FragmentManager fragmentManager;
	private List<Account> accountList;

	public AccountBudgetingAdapter(Context context, FragmentManager fragmentManager,
	                               List<Account> accountList) {
		this.mContext = context;
		this.fragmentManager = fragmentManager;
		this.accountList = accountList;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		//return null;
		View view = LayoutInflater.from(parent.getContext())
				.inflate(com.ogl4jo3.accounting.R.layout.item_account_budgeting, parent, false);

		return new AccountBudgetingAdapter.ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		holder.ivAccountCategoryIcon.setImageResource(accountList.get(position).getCategoryIcon());
		holder.tvAccountName.setText(accountList.get(position).getName());
		holder.tvRemainingBudget.setText(
				StringUtil.toMoneyStr(calculateRemainingBudget(accountList.get(position))));
		holder.tvBudget.setText(String.valueOf(accountList.get(position).getBudgetPrice()));

		holder.itemView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				BudgetEditFragment budgetEditFragment = BudgetEditFragment.newInstance(
						new Gson().toJson(accountList.get(holder.getAdapterPosition())), "");
				fragmentManager.beginTransaction()
						.replace(com.ogl4jo3.accounting.R.id.layout_main_content, budgetEditFragment, null)
						.addToBackStack(null).commit();
			}
		});
	}

	/**
	 * 計算這個月剩餘預算
	 * 剩餘預算=總預算-支出
	 */
	private int calculateRemainingBudget(Account account) {
		int remainingBudget;
		int totalBudget = account.getBudgetPrice();
		Date date = new Date();
		//取得原先預算起始日
		String budgetStartDay =
				new SharedPreferencesHelper(mContext, SharedPreferencesTag.prefsData)
						.getString(SharedPreferencesTag.prefsBudgetStartDay, "01");
		SQLiteDatabase database = MyDBHelper.getDatabase(mContext);
		int expensesPrice = new ExpensesDAO(database)
				.getSumByDateAccount(DateUtil.fromDateStrByMonth(date, budgetStartDay),
						DateUtil.toDateStrByMonth(date, budgetStartDay), account.getName());

		remainingBudget = totalBudget - expensesPrice;
		return remainingBudget;

	}

	@Override
	public int getItemCount() {
		return accountList.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {

		ImageView ivAccountCategoryIcon;
		TextView tvAccountName;
		TextView tvRemainingBudget;
		TextView tvBudget;

		public ViewHolder(View itemView) {
			super(itemView);
			ivAccountCategoryIcon =
					(ImageView) itemView.findViewById(com.ogl4jo3.accounting.R.id.iv_account_category_icon);
			tvAccountName = (TextView) itemView.findViewById(com.ogl4jo3.accounting.R.id.tv_account_name);
			tvRemainingBudget = (TextView) itemView.findViewById(
					com.ogl4jo3.accounting.R.id.tv_remaining_budget);
			tvBudget = (TextView) itemView.findViewById(com.ogl4jo3.accounting.R.id.tv_budget);
		}
	}
}
