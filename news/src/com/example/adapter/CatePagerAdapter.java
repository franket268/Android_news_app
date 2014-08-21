package com.example.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class CatePagerAdapter extends FragmentPagerAdapter {
	private final String Tag=CatePagerAdapter.class.getSimpleName();
	private List<Fragment> mFragments;
	
	public CatePagerAdapter(FragmentManager fm,List<Fragment> fragments) {
		super(fm);
		mFragments=fragments;		
	}


	@Override
	public Fragment getItem(int position) {
		return null == mFragments ? null : mFragments.get(position);
	}

	@Override
	public int getCount() {
		return null == mFragments ? 0 : mFragments.size();
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}
	
}
	