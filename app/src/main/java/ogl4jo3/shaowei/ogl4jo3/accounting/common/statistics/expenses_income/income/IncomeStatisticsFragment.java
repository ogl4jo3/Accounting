package ogl4jo3.shaowei.ogl4jo3.accounting.common.statistics.expenses_income.income;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ogl4jo3.shaowei.ogl4jo3.accounting.common.income.IncomeDAO;
import ogl4jo3.shaowei.ogl4jo3.accounting.common.statistics.expenses_income.StatisticsItem;
import ogl4jo3.shaowei.ogl4jo3.accounting.setting.accountmanagement.Account;
import ogl4jo3.shaowei.ogl4jo3.accounting.setting.accountmanagement.AccountDAO;
import ogl4jo3.shaowei.ogl4jo3.utility.database.MyDBHelper;
import ogl4jo3.shaowei.ogl4jo3.utility.date.DateUtil;
import ogl4jo3.shaowei.ogl4jo3.utility.sharedpreferences.SharedPreferencesHelper;
import ogl4jo3.shaowei.ogl4jo3.utility.sharedpreferences.SharedPreferencesTag;
import ogl4jo3.shaowei.ogl4jo3.utility.string.StringUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IncomeStatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IncomeStatisticsFragment extends Fragment {

	//UI元件
	private Spinner spAccount;  //帳戶Spinner
	private TextView tvCustom;  //自訂
	private TextView tvLastMonth;//上月
	private TextView tvThisMonth;//本月
	private TextView tvHalfYear;//半年
	private TextView tvOneYear;//一年
	private TextView tvFromDate;
	private TextView tvToDate;
	private TextView tvTotalIncome;  //總收入
	private RecyclerView mRecyclerView;    // RecyclerView
	private RecyclerView.LayoutManager mLayoutManager;
	private IncomeStatisticsAdapter mAdapter;

	private List<Account> accountList;  //所有帳戶
	private List<String> accountsName;  //所有帳戶名稱
	private String budgetStartDay;//預算起始日
	private Date date;//現在時間
	private String fromDateStr;//開始日期
	private String toDateStr;//結束日期
	private int totalIncome;//總收入金額
	private List<StatisticsItem> statisticsItems;

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	public IncomeStatisticsFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment IncomeStatisticsFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static IncomeStatisticsFragment newInstance(String param1, String param2) {
		IncomeStatisticsFragment fragment = new IncomeStatisticsFragment();
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
		initData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(ogl4jo3.shaowei.ogl4jo3.accounting.R.layout.fragment_statistics_income, container, false);
		initUI(view);
		setViewData();
		setRecyclerView();
		setAccountSpinner();
		setOnClickListener();

		return view;
	}

	/**
	 * 初始化資料
	 */
	private void initData() {

		date = new Date();
		/*try {//自訂時間，方便測試
			date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2016-02-01");
		} catch (ParseException e) {
			e.printStackTrace();
		}*/

		budgetStartDay = new SharedPreferencesHelper(getActivity(), SharedPreferencesTag.prefsData)
				.getString(SharedPreferencesTag.prefsBudgetStartDay, "01");

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(budgetStartDay));

		fromDateStr = DateUtil.fromDateStrByMonth(date, budgetStartDay);//開始日期
		toDateStr = DateUtil.toDateStrByMonth(date, budgetStartDay);//結束日期

		SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
		// 初始化統計資料
		statisticsItems =
				new IncomeDAO(db).getStatistics(fromDateStr, toDateStr, AccountDAO.ALL_ACOUNT);
		accountList = new AccountDAO(db).getAll();//取得所有帳戶
		accountsName = new ArrayList<>();
		accountsName.add(AccountDAO.ALL_ACOUNT);//將第一個欄位設為所有帳戶
		for (Account account : accountList) {//取得所有帳戶名稱
			accountsName.add(account.getName());
		}
		totalIncome = new IncomeDAO(db)
				.getSumByDateAccount(fromDateStr, toDateStr, AccountDAO.ALL_ACOUNT);

	}

	/**
	 * 初始化UI元件
	 */
	private void initUI(View view) {
		spAccount = (Spinner) view.findViewById(ogl4jo3.shaowei.ogl4jo3.accounting.R.id.sp_account);
		tvCustom = (TextView) view.findViewById(ogl4jo3.shaowei.ogl4jo3.accounting.R.id.tv_custom);
		tvLastMonth = (TextView) view.findViewById(ogl4jo3.shaowei.ogl4jo3.accounting.R.id.tv_last_month);
		tvThisMonth = (TextView) view.findViewById(ogl4jo3.shaowei.ogl4jo3.accounting.R.id.tv_this_month);
		tvHalfYear = (TextView) view.findViewById(ogl4jo3.shaowei.ogl4jo3.accounting.R.id.tv_half_year);
		tvOneYear = (TextView) view.findViewById(ogl4jo3.shaowei.ogl4jo3.accounting.R.id.tv_one_year);
		tvFromDate = (TextView) view.findViewById(ogl4jo3.shaowei.ogl4jo3.accounting.R.id.tv_from_date);
		tvToDate = (TextView) view.findViewById(ogl4jo3.shaowei.ogl4jo3.accounting.R.id.tv_to_date);
		tvTotalIncome = (TextView) view.findViewById(ogl4jo3.shaowei.ogl4jo3.accounting.R.id.tv_total_income);
		mRecyclerView = (RecyclerView) view.findViewById(ogl4jo3.shaowei.ogl4jo3.accounting.R.id.rv_statistics);
	}

	/**
	 * 設定UI元件內容
	 */
	private void setViewData() {
		tvFromDate.setText(fromDateStr);
		tvToDate.setText(toDateStr);
		tvTotalIncome.setText(StringUtil.toMoneyStr(totalIncome));
	}

	/**
	 * 設置RecyclerView
	 */
	private void setRecyclerView() {
		mRecyclerView.setHasFixedSize(true);
		// Layout管理器
		mLayoutManager = new LinearLayoutManager(this.getActivity());
		mRecyclerView.setLayoutManager(mLayoutManager);
		// Adapter
		mAdapter =
				new IncomeStatisticsAdapter(getActivity(), getFragmentManager(), statisticsItems);
		mRecyclerView.setAdapter(mAdapter);
	}

	/**
	 * 設置 AccountSpinner
	 */
	private void setAccountSpinner() {
		ArrayAdapter<String> accountArrayAdapter =
				new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,
						accountsName.toArray(new String[0]));
		spAccount.setAdapter(accountArrayAdapter);
		spAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position,
			                           long l) {
				updateStatisticsData(tvFromDate.getText().toString(), tvToDate.getText().toString(),
						accountsName.get(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});
	}

	/**
	 * 設置OnClickListener
	 */
	private void setOnClickListener() {
		tvFromDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Date date = null;
				try {
					date = DateUtil.strToDate(tvFromDate.getText().toString());
					//new SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault()).parse(tvDate.getText().toString());

				} catch (ParseException e) {
					e.printStackTrace();
				}
				final Calendar mCalendar = Calendar.getInstance();
				mCalendar.setTime(date);

				DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year, int month, int day) {
								mCalendar.set(Calendar.YEAR, year);
								mCalendar.set(Calendar.MONTH, month);
								mCalendar.set(Calendar.DAY_OF_MONTH, day);
								String dateStr = DateUtil.dateToStr(mCalendar.getTime());
								tvFromDate.setText(dateStr);
								setTvClicked(tvCustom);
								//刷新統計資料
								updateStatisticsData(dateStr, tvToDate.getText().toString(),
										spAccount.getSelectedItem().toString());

							}

						}, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
						mCalendar.get(Calendar.DAY_OF_MONTH));
				try {
					datePickerDialog.getDatePicker().setMaxDate(
							DateUtil.strToDate(tvToDate.getText().toString()).getTime() +
									(1000 * 60 * 60 * 12));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				datePickerDialog.show();
			}
		});
		tvToDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Date date = null;
				try {
					date = DateUtil.strToDate(tvToDate.getText().toString());
					//new SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault()).parse(tvDate.getText().toString());

				} catch (ParseException e) {
					e.printStackTrace();
				}
				final Calendar mCalendar = Calendar.getInstance();
				mCalendar.setTime(date);

				DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year, int month, int day) {
								mCalendar.set(Calendar.YEAR, year);
								mCalendar.set(Calendar.MONTH, month);
								mCalendar.set(Calendar.DAY_OF_MONTH, day);
								String dateStr = DateUtil.dateToStr(mCalendar.getTime());
								tvToDate.setText(dateStr);
								setTvClicked(tvCustom);
								//刷新統計資料
								updateStatisticsData(tvFromDate.getText().toString(), dateStr,
										spAccount.getSelectedItem().toString());

							}

						}, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
						mCalendar.get(Calendar.DAY_OF_MONTH));
				try {
					datePickerDialog.getDatePicker().setMinDate(
							DateUtil.strToDate(tvFromDate.getText().toString()).getTime());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				datePickerDialog.show();
			}
		});
		tvCustom.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

			}
		});
		tvLastMonth.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				//TODO:尚無實際功能
				Toast.makeText(getActivity(), ogl4jo3.shaowei.ogl4jo3.accounting.R.string.msg_todo, Toast.LENGTH_SHORT).show();
				setTvClicked(tvLastMonth);

			}
		});
		tvThisMonth.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				String fromDateStr = DateUtil.fromDateStrByMonth(new Date(), budgetStartDay);
				String toDateStr = DateUtil.toDateStrByMonth(new Date(), budgetStartDay);
				tvFromDate.setText(fromDateStr);
				tvToDate.setText(toDateStr);
				//刷新統計資料
				updateStatisticsData(fromDateStr, toDateStr,
						spAccount.getSelectedItem().toString());
				setTvClicked(tvThisMonth);
			}
		});
		tvHalfYear.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				//TODO:尚無實際功能
				Toast.makeText(getActivity(), ogl4jo3.shaowei.ogl4jo3.accounting.R.string.msg_todo, Toast.LENGTH_SHORT).show();
				setTvClicked(tvHalfYear);
			}
		});
		tvOneYear.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				//TODO:尚無實際功能
				Toast.makeText(getActivity(), ogl4jo3.shaowei.ogl4jo3.accounting.R.string.msg_todo, Toast.LENGTH_SHORT).show();
				setTvClicked(tvOneYear);
			}
		});

	}

	/**
	 * 刷新統計資料
	 */
	private void updateStatisticsData(String fromDateStr, String toDateStr, String accountName) {
		SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
		statisticsItems.clear();
		statisticsItems
				.addAll(new IncomeDAO(db).getStatistics(fromDateStr, toDateStr, accountName));
		mAdapter.notifyDataSetChanged();
		totalIncome = new IncomeDAO(db).getSumByDateAccount(fromDateStr, toDateStr, accountName);
		tvTotalIncome.setText(StringUtil.toMoneyStr(totalIncome));
	}

	/**
	 * 全部取消反白，再將此TextView背景反白
	 */
	private void setTvClicked(TextView tvClicked) {
		tvCustom.setBackgroundColor(ContextCompat.getColor(getActivity(), ogl4jo3.shaowei.ogl4jo3.accounting.R.color.default_color));
		tvLastMonth
				.setBackgroundColor(ContextCompat.getColor(getActivity(), ogl4jo3.shaowei.ogl4jo3.accounting.R.color.default_color));
		tvThisMonth
				.setBackgroundColor(ContextCompat.getColor(getActivity(), ogl4jo3.shaowei.ogl4jo3.accounting.R.color.default_color));
		tvHalfYear.setBackgroundColor(ContextCompat.getColor(getActivity(), ogl4jo3.shaowei.ogl4jo3.accounting.R.color.default_color));
		tvOneYear.setBackgroundColor(ContextCompat.getColor(getActivity(), ogl4jo3.shaowei.ogl4jo3.accounting.R.color.default_color));

		tvClicked.setBackgroundColor(ContextCompat.getColor(getActivity(), ogl4jo3.shaowei.ogl4jo3.accounting.R.color.gray_press));
	}

}
