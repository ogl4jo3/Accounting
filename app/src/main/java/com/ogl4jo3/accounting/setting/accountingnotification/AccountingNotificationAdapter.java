package com.ogl4jo3.accounting.setting.accountingnotification;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import com.ogl4jo3.accounting.R;
import com.ogl4jo3.accounting.utility.database.MyDBHelper;

/**
 * 記帳通知Adapter
 * Created by ogl4jo3 on 2017/12/17.
 */

public class AccountingNotificationAdapter
		extends RecyclerView.Adapter<AccountingNotificationAdapter.ViewHolder> {

	private Context mContext;
	private FragmentManager fragmentManager;
	private List<AccountingNotification> accountingNotificationList;

	//建立意圖
	private Intent intent;
	//建立待處理意圖
	private PendingIntent pending;
	//取得AlarmManager
	private AlarmManager alarm;

	public AccountingNotificationAdapter(Context context, FragmentManager fragmentManager,
	                                     List<AccountingNotification> accountingNotificationList) {
		this.mContext = context;
		this.fragmentManager = fragmentManager;
		this.accountingNotificationList = accountingNotificationList;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		//return null;
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_accounting_notification, parent, false);

		return new AccountingNotificationAdapter.ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		final AccountingNotification accountingNotificationThis =
				accountingNotificationList.get(position);
		holder.ivCategoryIcon.setImageResource(accountingNotificationThis.getCategoryIcon());
		holder.tvCategoryName.setText(accountingNotificationThis.getCategoryName());
		holder.tvNotificationName.setText(accountingNotificationThis.getNotificationName());
		//若小於10，補0
		holder.tvNotificationHour.setText(accountingNotificationThis.getNotificationHour() < 10 ?
				"0" + String.valueOf(accountingNotificationThis.getNotificationHour()) :
				String.valueOf(accountingNotificationThis.getNotificationHour()));
		holder.tvNotificationMinute.setText(
				accountingNotificationThis.getNotificationMinute() < 10 ?
						"0" + String.valueOf(accountingNotificationThis.getNotificationMinute()) :
						String.valueOf(accountingNotificationThis.getNotificationMinute()));
		holder.swAlarm.setChecked(accountingNotificationThis.isOn());

		holder.swAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
				SQLiteDatabase db = MyDBHelper.getDatabase(mContext);
				new AccountingNotificationDAO(db).saveOnOff(accountingNotificationThis, isChecked);

				//建立意圖
				intent = new Intent();
				intent.setClass(mContext, AccountingAlarmReceiver.class);
				intent.putExtra(AccountingNotificationDAO.KEY_ID,
						accountingNotificationThis.getId());
				//建立待處理意圖
				pending = PendingIntent
						.getBroadcast(mContext, accountingNotificationThis.getId(), intent,
								PendingIntent.FLAG_UPDATE_CURRENT);
				//取得AlarmManager
				alarm = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

				if (isChecked) {
					//使用Calendar指定時間
					Calendar calendar = Calendar.getInstance();
					calendar.set(Calendar.HOUR_OF_DAY,
							accountingNotificationThis.getNotificationHour());
					calendar.set(Calendar.MINUTE,
							accountingNotificationThis.getNotificationMinute());
					calendar.set(Calendar.SECOND, 0);

					alarm.setRepeating(AlarmManager.RTC_WAKEUP,
							calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pending);
				} else {
					alarm.cancel(pending);
				}

			}
		});

		holder.itemView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AccountingNotificationNewEditFragment accountingNotificationNewEditFragment =
						AccountingNotificationNewEditFragment
								.newInstance(String.valueOf(accountingNotificationThis.getId()),
										"");
				fragmentManager.beginTransaction()
						.replace(R.id.layout_main_content, accountingNotificationNewEditFragment,
								null).addToBackStack(null).commit();
			}
		});
	}

	@Override
	public int getItemCount() {
		return accountingNotificationList.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {

		ImageView ivCategoryIcon;
		TextView tvNotificationName;
		TextView tvCategoryName;
		TextView tvNotificationHour;
		TextView tvNotificationMinute;
		Switch swAlarm;

		public ViewHolder(View itemView) {
			super(itemView);
			ivCategoryIcon = (ImageView) itemView.findViewById(R.id.iv_category_icon);
			tvNotificationName = (TextView) itemView.findViewById(R.id.tv_notification_name);
			tvCategoryName = (TextView) itemView.findViewById(R.id.tv_category_name);
			tvNotificationHour = (TextView) itemView.findViewById(R.id.tv_notification_hour);
			tvNotificationMinute = (TextView) itemView.findViewById(R.id.tv_notification_minute);
			swAlarm = (Switch) itemView.findViewById(R.id.sw_alarm);
		}
	}
}