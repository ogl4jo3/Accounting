package com.ogl4jo3.accounting.utility.sharedpreferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesHelper {

	private SharedPreferences appSharedPrefs;
	private Editor prefsEditor;

	@SuppressLint("CommitPrefEdits")
	public SharedPreferencesHelper(Context context, String prefsName) {
		this.appSharedPrefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
		this.prefsEditor = appSharedPrefs.edit();
	}

	public int getInt(String key, int defValue) {
		return appSharedPrefs.getInt(key, defValue);
	}

	public void setInt(String key, int value) {
		prefsEditor.putInt(key, value);
		prefsEditor.commit();
	}

	public long getLong(String key, long defValue) {
		return appSharedPrefs.getLong(key, defValue);
	}

	public void setLong(String key, long value) {
		prefsEditor.putLong(key, value);
		prefsEditor.commit();
	}

	public float getFloat(String key, float defValue) {
		return appSharedPrefs.getFloat(key, defValue);
	}

	public void setFloat(String key, float value) {
		prefsEditor.putFloat(key, value);
		prefsEditor.commit();
	}

	public String getString(String key, String defValue) {
		return appSharedPrefs.getString(key, defValue);
	}

	public void setString(String key, String data) {
		prefsEditor.putString(key, data);
		prefsEditor.commit();
	}

	public boolean getBoolean(String key, boolean defValue) {
		return appSharedPrefs.getBoolean(key, defValue);
	}

	public void setBoolean(String key, boolean data) {
		prefsEditor.putBoolean(key, data);
		prefsEditor.commit();
	}

}