package com.ogl4jo3.accounting.utility.keyboard;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 鍵盤相關工具
 * Created by ogl4jo3 on 2017/6/14.
 */

public class KeyboardUtil {

	/**
	 * 關閉鍵盤
	 *
	 * @param activity Activity
	 * @return 成功關閉
	 */
	public static boolean closeKeyboard(Activity activity) {
		View view = activity.getCurrentFocus();
		if (view != null) {
			((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
			.hideSoftInputFromWindow(view.getWindowToken(), 0);
			return true;
		}
		return false;
	}

	/**
	 * 點按不是EditText的View時，隱藏鍵盤
	 *
	 * @param activity Activity
	 * @param view 需要關閉鍵盤範圍的畫面
	 */
	public static void setupUI(final Activity activity, final View view) {
		// Set up touch listener for non-text box views to hide keyboard.
		if (!(view instanceof EditText)) {
			view.setOnTouchListener(new View.OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					KeyboardUtil.closeKeyboard(activity);
					return false;
				}
			});
		}

		//If a layout container, iterate over children and seed recursion.
		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				View innerView = ((ViewGroup) view).getChildAt(i);
				setupUI(activity, innerView);
			}
		}
	}
}
