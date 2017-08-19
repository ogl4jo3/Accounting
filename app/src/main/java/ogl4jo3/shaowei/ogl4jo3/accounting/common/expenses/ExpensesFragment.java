package ogl4jo3.shaowei.ogl4jo3.accounting.common.expenses;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ogl4jo3.shaowei.ogl4jo3.accounting.R;
import ogl4jo3.shaowei.ogl4jo3.accounting.setting.accountmanagement.Account;
import ogl4jo3.shaowei.ogl4jo3.accounting.setting.accountmanagement.AccountDAO;
import ogl4jo3.shaowei.ogl4jo3.utility.database.MyDBHelper;
import ogl4jo3.shaowei.ogl4jo3.utility.date.DateUtil;
import ogl4jo3.shaowei.ogl4jo3.utility.sharedpreferences.SharedPreferencesHelper;
import ogl4jo3.shaowei.ogl4jo3.utility.sharedpreferences.SharedPreferencesTag;
import ogl4jo3.shaowei.ogl4jo3.utility.string.StringUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpensesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpensesFragment extends Fragment {

	public static final String EXPENSES_FRAGMENT_TAG = "EXPENSES_FRAGMENT_TAG";

	// UI 元件
	private TextView tvDate;    //日期
	private TextView tvThisDayExpenses; //本日支出
	private TextView tvRemainingBudget; //剩餘預算
	private TextView tvTotalBudget;     //預算總額
	private TextView tvBudgetPercent;   //預算百分比
	private TextView tvNoData;  //沒資料時顯示
	private RecyclerView rvExpensesItem;
	private RecyclerView.LayoutManager mLayoutManager;
	private ExpensesAdapter mAdapter;

	private String dateStr;
	private List<Expenses> expensesList;
	private int totalRemainingBudget;//剩餘預算總額，所有帳戶的
	private int totalBudget;    //所有帳戶預算相加
	private int budgetPercent;  //當剩餘預算總額變動時，會跟著變動

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	public ExpensesFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment ExpensesFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ExpensesFragment newInstance(String param1, String param2) {
		ExpensesFragment fragment = new ExpensesFragment();
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
		setHasOptionsMenu(true);
		dateStr = DateUtil.getCurrentDate();
		SQLiteDatabase database = MyDBHelper.getDatabase(getActivity());
		totalBudget = new AccountDAO(database).getBudgetSumOfAccounts();

		//new SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault()).format(new Date());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		getActivity().setTitle(R.string.title_expenses);
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_expenses, container, false);
		initUI(view);
		initExpensesList(); //需放在onCreateView，從新增或編輯頁跳回時才會重載資料
		setViewData();

		setRecyclerView();
		setOnClickListener();

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.menu_expenses_income, menu);
		//super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.menu_date) {
			Date date = null;
			try {
				date = DateUtil.strToDate(tvDate.getText().toString());
				//new SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault()).parse(tvDate.getText().toString());

			} catch (ParseException e) {
				e.printStackTrace();
			}
			final Calendar mCalendar = Calendar.getInstance();
			mCalendar.setTime(date);

			new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int month, int day) {
					mCalendar.set(Calendar.YEAR, year);
					mCalendar.set(Calendar.MONTH, month);
					mCalendar.set(Calendar.DAY_OF_MONTH, day);
					dateStr = DateUtil.dateToStr(mCalendar.getTime());
					//new SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault()).format(mCalendar.getTime());
					tvDate.setText(dateStr);

					//依據日期更新 expensesList資料，並刷新Adapter
					updateExpensesList(dateStr);
				}

			}, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
					mCalendar.get(Calendar.DAY_OF_MONTH)).show();

		} else if (id == R.id.menu_new) {

			//TODO:待刪除，測試用，將第一次使用改為是
			new SharedPreferencesHelper(getActivity(), SharedPreferencesTag.prefsData)
					.setBoolean(SharedPreferencesTag.prefsFirstUse, true);

			FragmentManager fragmentManager = getFragmentManager();
			ExpensesNewEditFragment expensesNewEditFragment =
					ExpensesNewEditFragment.newInstance(tvDate.getText().toString(), "");
			fragmentManager.beginTransaction()
					.replace(R.id.layout_main_content, expensesNewEditFragment, null)
					.addToBackStack(null).commit();

		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 依據日期
	 * 初始化資料
	 */
	private void initExpensesList() {
		//新增測試用資料
		//SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
		//new ExpensesDAO(db).newTestExpensesData();

		SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
		expensesList = new ExpensesDAO(db).getByDate(dateStr);
		/*for (int i = 0; i < expensesList.size(); i++) {
			Log.d(TAG, "after listExpenses(" + i + "): " + expensesList.get(i).toString());
		}*/
		if (expensesList.size() <= 0) {//若沒資料時，顯示無資料
			rvExpensesItem.setVisibility(View.GONE);
			tvNoData.setVisibility(View.VISIBLE);
		} else {
			rvExpensesItem.setVisibility(View.VISIBLE);
			tvNoData.setVisibility(View.GONE);
		}
	}

	/**
	 * 初始化元件
	 *
	 * @param view View
	 */
	private void initUI(View view) {
		tvNoData = (TextView) view.findViewById(R.id.tv_no_data);
		rvExpensesItem = (RecyclerView) view.findViewById(R.id.rv_expenses_item);
		tvDate = (TextView) view.findViewById(R.id.tv_date);
		tvThisDayExpenses = (TextView) view.findViewById(R.id.tv_this_day_expenses);
		tvRemainingBudget = (TextView) view.findViewById(R.id.tv_remaining_budget);
		tvTotalBudget = (TextView) view.findViewById(R.id.tv_total_budget);
		tvBudgetPercent = (TextView) view.findViewById(R.id.tv_budget_percent);
	}

	/**
	 * 設置元件資料
	 */
	private void setViewData() {
		tvDate.setText(dateStr);
		tvThisDayExpenses.setText(StringUtil.toMoneyStr(calculateThisDayExpenses()));
		totalRemainingBudget = calculateRemainingBudget();//計算所有帳戶預算餘額
		tvRemainingBudget.setText(StringUtil.toMoneyStr(totalRemainingBudget));
		tvTotalBudget.setText(StringUtil.toMoneyStr(totalBudget));
		tvBudgetPercent.setText(String.valueOf(budgetPercent));
	}

	/**
	 * 設置RecyclerView
	 */
	private void setRecyclerView() {
		rvExpensesItem.setHasFixedSize(true);
		// Layout管理器
		mLayoutManager = new LinearLayoutManager(this.getActivity());
		rvExpensesItem.setLayoutManager(mLayoutManager);
		// Adapter
		mAdapter = new ExpensesAdapter(getActivity(), getFragmentManager(), expensesList);
		rvExpensesItem.setAdapter(mAdapter);
	}

	/**
	 * 設置OnClickListener
	 */
	private void setOnClickListener() {
		tvDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				dateStr = DateUtil.getCurrentDate();
				//new SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault()).format(new Date());
				tvDate.setText(dateStr);
				//依據日期更新 expensesList資料，並刷新Adapter
				updateExpensesList(dateStr);
			}
		});
	}

	/**
	 * 依據日期更新 expensesList資料，並刷新Adapter
	 */
	private void updateExpensesList(String dateStr) {
		SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
		expensesList.clear();
		expensesList.addAll(new ExpensesDAO(db).getByDate(dateStr));
		mAdapter.notifyDataSetChanged();
		//更新本日支出欄位
		tvThisDayExpenses.setText(StringUtil.toMoneyStr(calculateThisDayExpenses()));
		totalRemainingBudget = calculateRemainingBudget();//計算所有帳戶預算餘額
		//更新預算餘額、百分比
		tvRemainingBudget.setText(StringUtil.toMoneyStr(totalRemainingBudget));
		tvBudgetPercent.setText(String.valueOf(budgetPercent));

		if (expensesList.size() <= 0) {//若沒資料時，顯示無資料
			rvExpensesItem.setVisibility(View.GONE);
			tvNoData.setVisibility(View.VISIBLE);
		} else {
			rvExpensesItem.setVisibility(View.VISIBLE);
			tvNoData.setVisibility(View.GONE);
		}
	}

	/**
	 * 計算本日支出
	 */
	private int calculateThisDayExpenses() {
		int thisDayExpenses = 0;
		for (Expenses expenses : expensesList) {
			thisDayExpenses += expenses.getPrice();
		}
		return thisDayExpenses;
	}

	/**
	 * 所有帳戶剩餘預算相加
	 * 計算這個月剩餘預算
	 * 剩餘預算=總預算-支出
	 */
	private int calculateRemainingBudget() {
		int totalRemainingBudget = 0;
		//取得預算起始日
		String budgetStartDay =
				new SharedPreferencesHelper(getActivity(), SharedPreferencesTag.prefsData)
						.getString(SharedPreferencesTag.prefsBudgetStartDay, "01");
		Date date = new Date();
		try {
			date = DateUtil.strToDate(tvDate.getText().toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SQLiteDatabase database = MyDBHelper.getDatabase(getActivity());
		List<Account> accountList = new AccountDAO(database).getAll();
		for (Account account : accountList) {
			int remainingBudget;
			int totalBudget = account.getBudgetPrice();
			int expensesPrice = new ExpensesDAO(database)
					.getSumByDateAccount(DateUtil.fromDateStrByMonth(date, budgetStartDay),
							DateUtil.toDateStrByMonth(date, budgetStartDay), account.getName());

			remainingBudget = totalBudget - expensesPrice;
			totalRemainingBudget += remainingBudget;
		}
		//剩餘預算百分比
		if (totalBudget == 0 || totalRemainingBudget <= 0) {//若預算總額為0或總預算餘額小於等於零時
			budgetPercent = 0;
		} else {
			budgetPercent = totalRemainingBudget * 100 / totalBudget;
		}
		return totalRemainingBudget;

	}
}
