package com.ogl4jo3.accounting.common.statistics;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * 各個統計(支出、收入、帳戶、預算)
 * FragmentPagerAdapter
 * Created by ogl4jo3 on 2017/7/31.
 */

public class StatisticsPagerAdapter extends FragmentStatePagerAdapter {

	private List<String> pageTitles;
	private List<Fragment> fragmentList;

	public StatisticsPagerAdapter(FragmentManager fm, List<String> pageTitles,
	                              List<Fragment> fragmentList) {
		super(fm);
		this.pageTitles = pageTitles;
		this.fragmentList = fragmentList;
	}

	@Override
	public Fragment getItem(int position) {
		return fragmentList.get(position);
	}

	@Override
	public int getCount() {
		return fragmentList.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return pageTitles.get(position);
	}
}
