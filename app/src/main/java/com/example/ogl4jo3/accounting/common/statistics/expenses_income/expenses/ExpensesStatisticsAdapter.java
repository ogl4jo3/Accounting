package com.example.ogl4jo3.accounting.common.statistics.expenses_income.expenses;

import android.app.FragmentManager;
import android.content.Context;

import com.example.ogl4jo3.accounting.common.statistics.expenses_income.StatisticsItem;
import com.example.ogl4jo3.accounting.common.statistics.expenses_income.StatisticsItemAdapter;
import com.example.ogl4jo3.utility.dialog.StatisticsDialogFragment;
import com.google.gson.Gson;

import java.util.List;

/**
 * 支出統計Adapter
 * Created by ogl4jo3 on 2017/8/8.
 */

public class ExpensesStatisticsAdapter extends StatisticsItemAdapter {

	private Context mContext;
	private FragmentManager fragmentManager;
	private List<StatisticsItem> statisticsItems;

	public ExpensesStatisticsAdapter(Context context, FragmentManager fragmentManager,
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
				.newInstance(StatisticsDialogFragment.EXPENSES, statisticsJson);
		statisticsDialogFragment.show(fragmentManager, null);

	}
}
