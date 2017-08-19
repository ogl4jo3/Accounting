package ogl4jo3.shaowei.ogl4jo3.accounting;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ogl4jo3.shaowei.ogl4jo3.utility.sharedpreferences.SharedPreferencesHelper;
import ogl4jo3.shaowei.ogl4jo3.utility.sharedpreferences.SharedPreferencesTag;

public class UseTeachingActivity extends AppCompatActivity {

	private List<View> pageList;
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_use_teaching);

		setPageList();
		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) findViewById(R.id.viewPager_use_teaching);
		mPagerAdapter = new SamplePagerAdapter(pageList);
		mPager.setAdapter(mPagerAdapter);
		//Pager Indicator
		TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
		tabLayout.setupWithViewPager(mPager, true);

	}

	/**
	 * 設置使用教學頁面
	 */
	private void setPageList() {
		//取得所有使用教學畫面
		Resources res = getResources();
		TypedArray icons = res.obtainTypedArray(R.array.use_teaching);
		int viewAmount = icons.length();
		int[] viewArray = new int[viewAmount];
		for (int i = 0; i < viewAmount; i++) {
			viewArray[i] = icons.getResourceId(i, -1);
			//Log.d(TAG, "iconArray[i] " + iconArray[i]);
		}
		// recycle the array
		icons.recycle();

		pageList = new ArrayList<>();
		for (int drawableRes : viewArray) {
			View pageView = new ImageView(this);
			pageView.setBackgroundResource(drawableRes);
			pageList.add(pageView);
		}

		//新增最後一頁開始使用按鈕畫面
		LinearLayout layoutStart = new LinearLayout(this);
		layoutStart.setGravity(Gravity.CENTER);
		//開始使用按鈕
		Button btnStart = new Button(this);
		btnStart.setText("開始使用");
		btnStart.setTextSize(24);
		btnStart.setLayoutParams(
				new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));
		btnStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				startUse();
			}
		});

		layoutStart.addView(btnStart);
		pageList.add(layoutStart);
	}

	/**
	 * 開始使用
	 */
	private void startUse() {
		//將第一次使用改為否
		new SharedPreferencesHelper(UseTeachingActivity.this, SharedPreferencesTag.prefsData)
				.setBoolean(SharedPreferencesTag.prefsFirstUse, false);
		//開始使用
		Intent intent = new Intent(UseTeachingActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	private class SamplePagerAdapter extends PagerAdapter {

		private List<View> pageList;

		public SamplePagerAdapter(List<View> pageList) {
			this.pageList = pageList;
		}

		@Override
		public int getCount() {
			return pageList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object o) {
			return o == view;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(pageList.get(position));
			return pageList.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}
}
