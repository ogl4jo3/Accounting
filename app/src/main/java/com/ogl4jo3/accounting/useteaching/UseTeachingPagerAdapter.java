package com.ogl4jo3.accounting.useteaching;

import android.app.Fragment;
import android.app.FragmentManager;
import androidx.legacy.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * 使用教學頁面
 * FragmentPagerAdapter
 * Created by ogl4jo3 on 2017/7/31.
 */
@Deprecated
public class UseTeachingPagerAdapter extends FragmentStatePagerAdapter {

	private List<Fragment> fragmentList;

	public UseTeachingPagerAdapter(FragmentManager fm,
	                               List<Fragment> fragmentList) {
		super(fm);
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

}
