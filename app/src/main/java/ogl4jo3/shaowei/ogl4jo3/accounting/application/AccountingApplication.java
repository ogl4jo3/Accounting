package ogl4jo3.shaowei.ogl4jo3.accounting.application;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by ogl4jo3 on 2018/2/25.
 */

public class AccountingApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		FacebookSdk.sdkInitialize(getApplicationContext());
		AppEventsLogger.activateApp(this);
	}
}
