package com.ogl4jo3.accounting.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.setting.accountingnotification.AccountingDailyAlarmReceiver
import com.ogl4jo3.accounting.ui.instruction.InstructionActivity
import com.ogl4jo3.accounting.utils.sharedpreferences.SharedPreferencesHelper
import com.ogl4jo3.accounting.utils.sharedpreferences.SharedPreferencesTag
import java.util.*

class InitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)

        //取得是否第一次使用
        val isFirstUse = SharedPreferencesHelper(this, SharedPreferencesTag.prefsData)
                .getBoolean(SharedPreferencesTag.prefsFirstUse, true)
        setDailyAlarm(isFirstUse) //設定每日記帳提醒

        //切頁，需要與UI Thread分開
        Thread {
            val mainIntent = Intent(this@InitActivity,
                    if (isFirstUse) InstructionActivity::class.java else MainActivity::class.java)

            //等候N秒鐘
            try {
                Thread.sleep(WAIT_SECOND.toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            startActivity(mainIntent)
            finish()
        }.start()
    }

    /**
     * 設置每日記帳提醒
     */
    private fun setDailyAlarm(isFirstUse: Boolean) {
        if (isFirstUse) {
            //設定每日記帳提醒，預設為22:30，ON
            SharedPreferencesHelper(this, SharedPreferencesTag.prefsData)
                    .setInt(SharedPreferencesTag.prefsDailyAlarmHour,
                            AccountingDailyAlarmReceiver.DEFAULT_DAILY_ALARM_HOUR)
            SharedPreferencesHelper(this, SharedPreferencesTag.prefsData)
                    .setInt(SharedPreferencesTag.prefsDailyAlarmMinute,
                            AccountingDailyAlarmReceiver.DEFAULT_DAILY_ALARM_MINUTE)
            SharedPreferencesHelper(this, SharedPreferencesTag.prefsData)
                    .setBoolean(SharedPreferencesTag.prefsDailyAlarmOnOff, true)
        }
        val isDailyAlarmOn = SharedPreferencesHelper(this, SharedPreferencesTag.prefsData)
                .getBoolean(SharedPreferencesTag.prefsDailyAlarmOnOff, true)
        if (isDailyAlarmOn) {

            //建立意圖
            val intent = Intent()
            intent.setClass(this, AccountingDailyAlarmReceiver::class.java)
            //建立待處理意圖
            val pending = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            //取得AlarmManager
            val alarm = this.getSystemService(ALARM_SERVICE) as AlarmManager
            //使用Calendar指定時間
            val calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = SharedPreferencesHelper(this, SharedPreferencesTag.prefsData)
                    .getInt(SharedPreferencesTag.prefsDailyAlarmHour,
                            AccountingDailyAlarmReceiver.DEFAULT_DAILY_ALARM_HOUR)
            calendar[Calendar.MINUTE] = SharedPreferencesHelper(this, SharedPreferencesTag.prefsData)
                    .getInt(SharedPreferencesTag.prefsDailyAlarmMinute,
                            AccountingDailyAlarmReceiver.DEFAULT_DAILY_ALARM_MINUTE)
            calendar[Calendar.SECOND] = 0
            //TODO:DEBUG 第一次安裝完就會跳出通知
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY, pending)
        }
    }

    companion object {
        private const val WAIT_SECOND = 200 // 等候秒數
    }
}