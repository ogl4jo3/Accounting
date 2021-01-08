package com.ogl4jo3.accounting.common.statistics.expenses_income.income;

import android.app.FragmentManager;
import android.content.Context;

import com.ogl4jo3.accounting.common.statistics.expenses_income.StatisticsItem;
import com.ogl4jo3.accounting.common.statistics.expenses_income.StatisticsItemAdapter;
import com.ogl4jo3.accounting.utils.dialog.StatisticsDialogFragment;
import com.google.gson.Gson;

import java.util.List;

/**
 * 收入統計Adapter
 * Created by ogl4jo3 on 2017/8/9.
 */

public class IncomeStatisticsAdapter extends StatisticsItemAdapter {

	private Context mContext;
	private FragmentManager fragmentManager;
	private List<StatisticsItem> statisticsItems;

	public IncomeStatisticsAdapter(Context context, FragmentManager fragmentManager,
	                                 List<StatisticsItem> statisticsItems) {
		super(context, fragmentManager, statisticsItems);
		this.mContext = context;
		this.fragmentManager = fragmentManager;
		this.statisticsItems = statisticsItems;
	}

	@Override
	public void onItemClick(int position) {
		String statisticsJson = new Gson().toJson(statisticsItems.get(position));
		StatisticsDialogFragment statisticsDialogFragment = StatisticsDialogFragment
				.newInstance(StatisticsDialogFragment.INCOME, statisticsJson);
		statisticsDialogFragment.show(fragmentManager, null);
	}
}
