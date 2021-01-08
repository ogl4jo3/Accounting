package com.ogl4jo3.accounting.useteaching;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import com.ogl4jo3.accounting.R;

public class UseTeachingActivity extends AppCompatActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_use_teaching);

		List<Fragment> fragmentList = new ArrayList<>();
		fragmentList.add(new UseTeachingNewExpensesFragment());
		fragmentList.add(new UseTeachingChooseDateFragment());
		fragmentList.add(new UseTeachingFunctionMenuFragment());
		fragmentList.add(new UseTeachingCustomDateFragment());
		fragmentList.add(new UseTeachingStartFragment());

		ViewPager mPager = (ViewPager) findViewById(R.id.viewPager_use_teaching);
		UseTeachingPagerAdapter useTeachingPagerAdapter =
				new UseTeachingPagerAdapter(getFragmentManager(), fragmentList);
		mPager.setAdapter(useTeachingPagerAdapter);

		//Pager Indicator
		TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
		tabLayout.setupWithViewPager(mPager, true);

	}

}
