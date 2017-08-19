package ogl4jo3.shaowei.ogl4jo3.accounting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ogl4jo3.shaowei.ogl4jo3.utility.sharedpreferences.SharedPreferencesHelper;
import ogl4jo3.shaowei.ogl4jo3.utility.sharedpreferences.SharedPreferencesTag;

public class InitActivity extends AppCompatActivity {

	private static final int WAIT_SECOND = 1000;// 等候秒數

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);

		//取得是否第一次使用
		final boolean isFirstUse = new SharedPreferencesHelper(this, SharedPreferencesTag.prefsData)
				.getBoolean(SharedPreferencesTag.prefsFirstUse, true);

		//切頁，需要與UI Thread分開
		new Thread(new Runnable() {

			@Override
			public void run() {
				Intent mainIntent = new Intent(InitActivity.this,
						isFirstUse ? UseTeachingActivity.class : MainActivity.class);

				//等候N秒鐘
				try {
					Thread.sleep(WAIT_SECOND);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				startActivity(mainIntent);
				finish();
			}
		}).start();
	}
}
