package com.ogl4jo3.accounting.setting.accountingnotification;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.List;

import com.ogl4jo3.accounting.R;
import com.ogl4jo3.accounting.utils.database.MyDBHelper;
import com.ogl4jo3.accounting.utils.sharedpreferences.SharedPreferencesHelper;
import com.ogl4jo3.accounting.utils.sharedpreferences.SharedPreferencesTag;
import com.ogl4jo3.accounting.utils.string.StringUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountingNotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountingNotificationFragment extends Fragment {

	//UI元件
	private LinearLayout llDailyAlarm;  //每日記帳提醒，點擊選擇時間
	private TextView tvDailyAlarmHour;  //每日記帳提醒，時
	private TextView tvDailyAlarmMinute;  //每日記帳提醒，分
	private Switch swDailyAlarm;  //每日記帳提醒ON/OFF
	private Button btnNew;  //新增按鈕
	private RecyclerView mRecyclerView;    // RecyclerView
	private RecyclerView.LayoutManager mLayoutManager;
	private AccountingNotificationAdapter mAdapter;

	private List<AccountingNotification> accountingNotificationList;

	public static final String ACCOUNTING_NOTIFICATION_FRAGMENT_TAG =
			"ACCOUNTING_NOTIFICATION_FRAGMENT_TAG";

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	public AccountingNotificationFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment AccountingNotificationFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static AccountingNotificationFragment newInstance(String param1, String param2) {
		AccountingNotificationFragment fragment = new AccountingNotificationFragment();
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
		getActivity().setTitle(R.string.title_accounting_notification);
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_accounting_notification, container, false);
		initAccountingNotificationList();
		initUI(view);
		setViewData();
		setDailyAlarm();
		setRecyclerView();
		setOnClickListener();

		return view;
	}

	/**
	 * 初始化帳戶資料
	 */
	private void initAccountingNotificationList() {
		SQLiteDatabase db = MyDBHelper.getDatabase(getActivity());
		accountingNotificationList = new AccountingNotificationDAO(db).getAll();
		/*accountingNotificationList = new ArrayList<>();
		AccountingNotification accountingNotification = new AccountingNotification();
		accountingNotification.setId(0);
		accountingNotification.setNotificationName("早餐提醒測試字串過長是否...");

		Category category= new CategoryDAO(db).getIncomeData(1);
		accountingNotification.setCategoryId(category.getId());
		accountingNotification.setCategoryIcon(category.getIcon());
		accountingNotification.setCategoryName(category.getName());

		accountingNotification.setNotificationHour(8);
		accountingNotification.setNotificationMinute(50);
		accountingNotification.setOn(true);
		accountingNotificationList.add(accountingNotification);*/
	}

	/**
	 * 初始化UI元件
	 */
	private void initUI(View view) {
		llDailyAlarm = (LinearLayout) view.findViewById(R.id.ll_daily_alarm);
		tvDailyAlarmHour = (TextView) view.findViewById(R.id.tv_daily_alarm_hour);
		tvDailyAlarmMinute = (TextView) view.findViewById(R.id.tv_daily_alarm_minute);
		swDailyAlarm = (Switch) view.findViewById(R.id.sw_daily_alarm);
		btnNew = (Button) view.findViewById(R.id.btn_new);
		mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_accounting_notification);

	}

	/**
	 * 設置元件資料
	 */
	private void setViewData() {
		String dailyAlarmHour = StringUtil.lowerTenPlusZero(
				new SharedPreferencesHelper(this.getActivity(), SharedPreferencesTag.prefsData)
						.getInt(SharedPreferencesTag.prefsDailyAlarmHour,
								AccountingDailyAlarmReceiver.DEFAULT_DAILY_ALARM_HOUR));
		String dailyAlarmMinute = StringUtil.lowerTenPlusZero(
				new SharedPreferencesHelper(this.getActivity(), SharedPreferencesTag.prefsData)
						.getInt(SharedPreferencesTag.prefsDailyAlarmMinute,
								AccountingDailyAlarmReceiver.DEFAULT_DAILY_ALARM_MINUTE));
		boolean isDailyAlarmOn =
				new SharedPreferencesHelper(this.getActivity(), SharedPreferencesTag.prefsData)
						.getBoolean(SharedPreferencesTag.prefsDailyAlarmOnOff, true);
		tvDailyAlarmHour.setText(dailyAlarmHour);
		tvDailyAlarmMinute.setText(dailyAlarmMinute);
		swDailyAlarm.setChecked(isDailyAlarmOn);
	}

	/**
	 * 設置每日記帳提醒資料
	 */
	private void setDailyAlarm() {
		llDailyAlarm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// Use the current time as the default values for the picker
				int hour = Integer.valueOf(tvDailyAlarmHour.getText().toString());
				int minute = Integer.valueOf(tvDailyAlarmMinute.getText().toString());
				// Create a new instance of TimePickerDialog and return it
				new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

						tvDailyAlarmHour.setText(StringUtil.lowerTenPlusZero(hourOfDay));
						tvDailyAlarmMinute.setText(StringUtil.lowerTenPlusZero(minute));

						new SharedPreferencesHelper(getActivity(), SharedPreferencesTag.prefsData)
								.setInt(SharedPreferencesTag.prefsDailyAlarmHour, hourOfDay);
						new SharedPreferencesHelper(getActivity(), SharedPreferencesTag.prefsData)
								.setInt(SharedPreferencesTag.prefsDailyAlarmMinute, minute);
						swDailyAlarm.setChecked(false);
					}
				}, hour, minute, true).show();

			}
		});
		swDailyAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
				new SharedPreferencesHelper(getActivity(), SharedPreferencesTag.prefsData)
						.setBoolean(SharedPreferencesTag.prefsDailyAlarmOnOff, isChecked);
				Intent intent = new Intent();
				intent.setClass(getActivity(), AccountingDailyAlarmReceiver.class);
				PendingIntent pending = PendingIntent
						.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				//取得AlarmManager
				AlarmManager alarm =
						(AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
				//使用Calendar指定時間
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.HOUR_OF_DAY,
						new SharedPreferencesHelper(getActivity(), SharedPreferencesTag.prefsData)
								.getInt(SharedPreferencesTag.prefsDailyAlarmHour,
										AccountingDailyAlarmReceiver.DEFAULT_DAILY_ALARM_HOUR));
				calendar.set(Calendar.MINUTE,
						new SharedPreferencesHelper(getActivity(), SharedPreferencesTag.prefsData)
								.getInt(SharedPreferencesTag.prefsDailyAlarmMinute,
										AccountingDailyAlarmReceiver.DEFAULT_DAILY_ALARM_MINUTE));
				calendar.set(Calendar.SECOND, 0);
				if (isChecked) {
					alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
							AlarmManager.INTERVAL_DAY, pending);
				} else {
					alarm.cancel(pending);
				}
			}
		});
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
		mAdapter = new AccountingNotificationAdapter(getActivity(), getFragmentManager(),
				accountingNotificationList);
		mRecyclerView.setAdapter(mAdapter);
	}

	/**
	 * 設置OnClickListener
	 */
	private void setOnClickListener() {
		btnNew.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				FragmentManager fragmentManager = getFragmentManager();
				AccountingNotificationNewEditFragment accountingNotificationNewEditFragment =
						AccountingNotificationNewEditFragment.newInstance("", "");
				fragmentManager.beginTransaction()
						.replace(R.id.layout_main_content, accountingNotificationNewEditFragment,
								null).addToBackStack(null).commit();
			}
		});

	}

}
