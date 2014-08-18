package com.example.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.example.news.R;
import com.example.service.NewsManager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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
	