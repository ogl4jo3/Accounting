package ogl4jo3.shaowei.ogl4jo3.accounting.common.income;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import ogl4jo3.shaowei.ogl4jo3.accounting.setting.accountmanagement.AccountDAO;
import ogl4jo3.shaowei.ogl4jo3.utility.database.MyDBHelper;
import ogl4jo3.shaowei.ogl4jo3.utility.date.DateUtil;
import ogl4jo3.shaowei.ogl4jo3.utility.sharedpreferences.SharedPreferencesHelper;
import ogl4jo3.shaowei.ogl4jo3.utility.sharedpreferences.SharedPreferencesTag;
import ogl4jo3.shaowei.ogl4jo3.utility.string.StringUtil;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IncomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IncomeFragment extends Fragment {

	public static final String INCOME_FRAGMENT_TAG = "INCOME_FRAGMENT_TAG";

	// UI 元件
	private TextView tvDate;    //日期
	private TextView tvThisDayIncome;   //本日收入
	private TextView tvThisMonthIncome; //本月收入
	private TextView tvIncomePercent;   //收入百分比

	private RecyclerView rvIncomeItem;
	private RecyclerView.LayoutManager mLayoutManager;
	private IncomeAdapter mAdapter;

	private String dateStr;
	private List<Income> incomeList;
	private int thisDayIncome;//本日收入金額
	private int thisMonthIncome;//本月收入金額
	private int incomePercent;//收入百分比

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	public IncomeFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment IncomeFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static IncomeFragment newInstance(String param1, String param2) {
		IncomeFragment fragment = new IncomeFragment();
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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		getActivity().setTitle(R.string.title_income);
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_income, container, false);
		initUI(view);
		initIncomeList(); //需放在onCreateView，從新增或編輯頁跳回時才會重載資料
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

					//依據日期更新 incomeList資料，並刷新Adapter
					updateIncomeList(dateStr);
				}

			}, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
					mCalendar.get(Calendar.DAY_OF_MONTH)).show();

		} else if (id == R.id.menu_new) {

			FragmentManager fragmentManager = getFragmentManager();
			IncomeNewEditFragment incomeNewEditFragment =
					IncomeNewEditFragment.newInstance(tvDate.getText().toString(), "");
			fragmentManager.beginTransaction()
					.replace(R.id.layout_main_content, incomeNewEditFragment, null)
					.addToBackStack(null).commit();

		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 依據日期
	 * 初始化資料
	 */
	private void initIncomeList() {
		SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
		incomeList = new IncomeDAO(db).getByDate(dateStr);
		for (int i = 0; i < incomeList.size(); i++) {
			Log.d(TAG, "after incomeList(" + i + "): " + incomeList.get(i).toString());
		}
	}

	/**
	 * 初始化元件
	 *
	 * @param view View
	 */
	private void initUI(View view) {
		rvIncomeItem = (RecyclerView) view.findViewById(R.id.rv_income_item);
		tvDate = (TextView) view.findViewById(R.id.tv_date);
		tvThisDayIncome = (TextView) view.findViewById(R.id.tv_this_day_income);
		tvThisMonthIncome = (TextView) view.findViewById(R.id.tv_this_month_income);
		tvIncomePercent = (TextView) view.findViewById(R.id.tv_income_percent);
	}

	/**
	 * 設置元件資料
	 */
	private void setViewData() {
		tvDate.setText(dateStr);
		thisDayIncome = calculateThisDayIncome();
		thisMonthIncome = calculateThisMonthIncome();
		if (thisMonthIncome == 0) {
			incomePercent = 0;
		} else {
			incomePercent = thisDayIncome * 100 / thisMonthIncome;
		}
		tvThisDayIncome.setText(StringUtil.toMoneyStr(thisDayIncome));
		tvThisMonthIncome.setText(StringUtil.toMoneyStr(thisMonthIncome));
		tvIncomePercent.setText(String.valueOf(incomePercent));
	}

	/**
	 * 設置RecyclerView
	 */
	private void setRecyclerView() {
		rvIncomeItem.setHasFixedSize(true);
		// Layout管理器
		mLayoutManager = new LinearLayoutManager(this.getActivity());
		rvIncomeItem.setLayoutManager(mLayoutManager);
		// Adapter
		mAdapter = new IncomeAdapter(getActivity(), getFragmentManager(), incomeList);
		rvIncomeItem.setAdapter(mAdapter);
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
				//依據日期更新 incomeList資料，並刷新Adapter
				updateIncomeList(dateStr);
			}
		});
	}

	/**
	 * 依據日期更新 incomeList資料，並刷新Adapter
	 */
	private void updateIncomeList(String dateStr) {
		SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
		incomeList.clear();
		incomeList.addAll(new IncomeDAO(db).getByDate(dateStr));
		mAdapter.notifyDataSetChanged();

		thisDayIncome = calculateThisDayIncome();
		thisMonthIncome = calculateThisMonthIncome();
		if (thisMonthIncome == 0) {
			incomePercent = 0;
		} else {
			incomePercent = thisDayIncome * 100 / thisMonthIncome;
		}
		//更新本日收入欄位
		tvThisDayIncome.setText(StringUtil.toMoneyStr(thisDayIncome));
		//更新本月收入欄位
		tvThisMonthIncome.setText(StringUtil.toMoneyStr(thisMonthIncome));
		//更新收入百分比欄位
		tvIncomePercent.setText(String.valueOf(incomePercent));
	}

	/**
	 * 計算本日收入
	 */
	private int calculateThisDayIncome() {
		int thisDayIncome = 0;
		for (Income income : incomeList) {
			thisDayIncome += income.getPrice();
		}
		return thisDayIncome;
	}

	/**
	 * 計算本月總收入
	 */
	private int calculateThisMonthIncome() {

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
		SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
		return new IncomeDAO(db)
				.getSumByDateAccount(DateUtil.fromDateStrByMonth(date, budgetStartDay),
						DateUtil.toDateStrByMonth(date, budgetStartDay), AccountDAO.ALL_ACOUNT);

	}
}
