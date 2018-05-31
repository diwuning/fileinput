package com.h.fileinput.FragmentPager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by chyulei on 16/7/9.
 * 食材viewpager布局适配器
 */
public class TabFragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
	private ArrayList<Fragment> fragments;
	private FragmentManager fm;

	public TabFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		this.fm = fm;
	}

	public TabFragmentPagerAdapter(FragmentManager fm,
                                   ArrayList<Fragment> fragments) {
		super(fm);
		this.fm = fm;
		this.fragments = fragments;
	}

	@Override
	public int getCount() {
		return fragments == null ? 0 : fragments.size();
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		Object obj = super.instantiateItem(container, position);
		return obj;
	}

}
