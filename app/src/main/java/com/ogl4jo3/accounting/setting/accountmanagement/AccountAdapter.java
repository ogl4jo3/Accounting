package com.ogl4jo3.accounting.setting.accountmanagement;

import android.app.FragmentManager;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ogl4jo3.accounting.R;
import com.ogl4jo3.accounting.utility.string.StringUtil;
import com.google.gson.Gson;

import java.util.List;

/**
 * 帳戶Adapter
 * Created by ogl4jo3 on 2017/8/10.
 */

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

	private Context mContext;
	private FragmentManager fragmentManager;
	private List<Account> accountList;

	public AccountAdapter(Context context, FragmentManager fragmentManager,
	                      List<Account> accountList) {
		this.mContext = context;
		this.fragmentManager = fragmentManager;
		this.accountList = accountList;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		//return null;
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_account, parent, false);

		return new AccountAdapter.ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		holder.ivCategoryIcon.setImageResource(accountList.get(position).getCategoryIcon());
		holder.tvCategoryName.setText(accountList.get(position).getCategoryName());
		holder.tvAccountName.setText(accountList.get(position).getName());
		if (accountList.get(position).isDefaultAccount()) {
			holder.tvDefault.setVisibility(View.VISIBLE);
		}
		holder.tvBalance.setText(StringUtil.toMoneyStr(accountList.get(position).getBalance()));

		holder.itemView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AccountNewEditFragment accountNewEditFragment = AccountNewEditFragment.newInstance(
						new Gson().toJson(accountList.get(holder.getAdapterPosition())), "");
				fragmentManager.beginTransaction()
						.replace(R.id.layout_main_content, accountNewEditFragment, null)
						.addToBackStack(null).commit();
			}
		});
	}

	@Override
	public int getItemCount() {
		return accountList.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {

		ImageView ivCategoryIcon;
		TextView tvAccountName;
		TextView tvDefault;
		TextView tvCategoryName;
		TextView tvBalance;

		public ViewHolder(View itemView) {
			super(itemView);
			ivCategoryIcon = (ImageView) itemView.findViewById(R.id.iv_account_category_icon);
			tvAccountName = (TextView) itemView.findViewById(R.id.tv_account_name);
			tvDefault = (TextView) itemView.findViewById(R.id.tv_default_account);
			tvCategoryName = (TextView) itemView.findViewById(R.id.tv_account_category_name);
			tvBalance = (TextView) itemView.findViewById(R.id.tv_account_balance);
		}
	}
}
