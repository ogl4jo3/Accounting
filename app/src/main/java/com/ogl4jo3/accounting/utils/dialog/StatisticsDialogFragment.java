package com.ogl4jo3.accounting.utils.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ogl4jo3.accounting.common.expenses.Expenses;
import com.ogl4jo3.accounting.common.expenses.ExpensesDAO;
import com.ogl4jo3.accounting.common.income.Income;
import com.ogl4jo3.accounting.common.income.IncomeDAO;
import com.ogl4jo3.accounting.common.statistics.expenses_income.StatisticsItem;
import com.ogl4jo3.accounting.setting.categorymanagement.Category;
import com.ogl4jo3.accounting.setting.categorymanagement.CategoryDAO;
import com.ogl4jo3.accounting.utils.database.MyDBHelper;
import com.ogl4jo3.accounting.utils.string.StringUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 統計的類別個別紀錄對話框
 * Created by ogl4jo3 on 2017/7/23.
 */
public class StatisticsDialogFragment extends DialogFragment {

	//UI元件
	private ImageView ivCategoryIcon;
	private TextView tvCategoryName;
	private ListView lvStatistics;

	public static final String EXPENSES = "expenses";
	public static final String INCOME = "income";
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String EXPENSES_INCOME = "expenses_income";
	private static final String STATISTICS_ITEM_JSON = "statistics_item_json";

	private String expenses_income;
	private String statisticsItemJson;
	private StatisticsItem statisticsItem;
	private String fromDateStr;
	private String toDateStr;
	private int categoryID;
	private String accountName;
	private Category category;

	private SimpleAdapter statisticsAdapter;
	private List<Map<String, Object>> mapData;  //SimpleAdapter 資料
	private final String mapDateKey = "date";   //adapter 資料對應KEY
	private final String mapPriceKey = "price";   //adapter 資料對應KEY
	private List<Expenses> expensesList;
	private List<Income> incomeList;

	public StatisticsDialogFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param expenses_income Parameter 1.
	 * @param statistics_json Parameter 2.
	 * @return A new instance of fragment StatisticsDialogFragment.
	 */
	public static StatisticsDialogFragment newInstance(String expenses_income,
	                                                   String statistics_json) {
		StatisticsDialogFragment fragment = new StatisticsDialogFragment();
		Bundle args = new Bundle();
		args.putString(EXPENSES_INCOME, expenses_income);
		args.putString(STATISTICS_ITEM_JSON, statistics_json);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			expenses_income = getArguments().getString(EXPENSES_INCOME);
			statisticsItemJson = getArguments().getString(STATISTICS_ITEM_JSON);
		}
		statisticsItem = new Gson().fromJson(statisticsItemJson, StatisticsItem.class);
		fromDateStr = statisticsItem.getFromDateStr();
		toDateStr = statisticsItem.getToDateStr();
		categoryID = statisticsItem.getCategoryId();
		accountName = statisticsItem.getAccountName();

		mapData = new ArrayList<>();
		SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
		//取得支出或收入的類別
		if (expenses_income.equalsIgnoreCase(EXPENSES)) {
			expensesList = new ExpensesDAO(db)
					.getByDateCategoryAccount(fromDateStr, toDateStr, categoryID, accountName);
			category = new CategoryDAO(db).getExpensesData(categoryID);
			int expensesLen = expensesList.size();
			for (int i = 0; i < expensesLen; i++) {
				Map<String, Object> data = new HashMap<>();
				data.put(mapDateKey, expensesList.get(i).getRecordTime());
				data.put(mapPriceKey, StringUtil.toMoneyStr(expensesList.get(i).getPrice()));
				mapData.add(data);
			}
		} else if (expenses_income.equalsIgnoreCase(INCOME)) {
			incomeList = new IncomeDAO(db)
					.getByDateCategoryAccount(fromDateStr, toDateStr, categoryID, accountName);
			category = new CategoryDAO(db).getIncomeData(categoryID);
			int incomeLen = incomeList.size();
			for (int i = 0; i < incomeLen; i++) {
				Map<String, Object> data = new HashMap<>();
				data.put(mapDateKey, incomeList.get(i).getRecordTime());
				data.put(mapPriceKey, "$" + incomeList.get(i).getPrice());
				mapData.add(data);
			}
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(com.ogl4jo3.accounting.R.layout.dialog_statistics, container, false);

		ivCategoryIcon = (ImageView) view.findViewById(com.ogl4jo3.accounting.R.id.iv_category_icon);
		tvCategoryName = (TextView) view.findViewById(com.ogl4jo3.accounting.R.id.tv_category_name);
		ivCategoryIcon.setImageResource(category.getIcon());
		tvCategoryName.setText(category.getName());

		lvStatistics = (ListView) view.findViewById(com.ogl4jo3.accounting.R.id.lv_statistics);
		statisticsAdapter =
				new SimpleAdapter(getActivity(), mapData, com.ogl4jo3.accounting.R.layout.item_statistics_record,
						new String[]{mapDateKey, mapPriceKey},
						new int[]{
								com.ogl4jo3.accounting.R.id.tv_date, com.ogl4jo3.accounting.R.id.tv_price});
		lvStatistics.setAdapter(statisticsAdapter);

		return view;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);

		// 移除對話框標題
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		return dialog;
	}
}
