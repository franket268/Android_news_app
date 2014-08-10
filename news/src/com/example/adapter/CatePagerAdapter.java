package com.example.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class CatePagerAdapter extends PagerAdapter {
	private int pagerCount;

	public CatePagerAdapter(int count){
		this.pagerCount=count;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pagerCount;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==arg1;
	}


	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		return super.instantiateItem(container, position);
	}

	
    @Override    
    public void destroyItem(ViewGroup container, int position, Object object) {    
        super.destroyItem(container, position, object);
    } 
}
