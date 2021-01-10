package com.ogl4jo3.accounting.common.statistics;

import android.app.Fragment;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.ogl4jo3.accounting.R;
import com.ogl4jo3.accounting.common.statistics.account.AccountStatisticsFragment;
import com.ogl4jo3.accounting.common.statistics.budget.BudgetStatisticsFragment;
import com.ogl4jo3.accounting.common.statistics.expenses_income.expenses.ExpensesStatisticsFragment;
import com.ogl4jo3.accounting.common.statistics.expenses_income.income.IncomeStatisticsFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {

	public static final String STATISTICS_FRAGMENT_TAG = "STATISTICS_FRAGMENT_TAG";

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	public StatisticsFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment StatisticsFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static StatisticsFragment newInstance(String param1, String param2) {
		StatisticsFragment fragment = new StatisticsFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		getActivity().setTitle(R.string.title_statistics);
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_statistics, container, false);

		List<String> pageTitles = new ArrayList<>();
		List<Fragment> fragmentList = new ArrayList<>();
		fragmentList.add(ExpensesStatisticsFragment.newInstance("", ""));
		pageTitles.add(getString(R.string.title_expense_statistics));
		fragmentList.add(IncomeStatisticsFragment.newInstance("", ""));
		pageTitles.add(getString(R.string.title_income_statistics));
		fragmentList.add(AccountStatisticsFragment.newInstance("", ""));
		pageTitles.add(getString(R.string.title_account_statistics));
		fragmentList.add(BudgetStatisticsFragment.newInstance("", ""));
		pageTitles.add(getString(R.string.title_budget_statistics));

		ViewPager vpStatistics = (ViewPager) view.findViewById(R.id.vp_statistics);
		StatisticsPagerAdapter statisticsPagerAdapter =
				new StatisticsPagerAdapter(getFragmentManager(), pageTitles, fragmentList);
		vpStatistics.setAdapter(statisticsPagerAdapter);

		return view;
	}

}
