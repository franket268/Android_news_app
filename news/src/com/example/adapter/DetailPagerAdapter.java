package com.example.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.fragment.DetailNewsFragment;
import com.example.service.NewsManager;

public class DetailPagerAdapter extends FragmentPagerAdapter {  
    private Context mContext;  
    private ArrayList<HashMap<String, Object>> mNewsData = new ArrayList<HashMap<String, Object>>();  
    private ArrayList<HashMap<String,Object>> bodyList= new ArrayList<HashMap<String, Object>>();  
    private NewsManager newsManager;  
    private int mPosition;  
    private int flag=0;  
    private HashMap<String,Fragment> fragmentMap =new HashMap<String,Fragment>();  
      
      
    public DetailPagerAdapter(FragmentManager fm,Context context, ArrayList<HashMap<String, Object>> newsData) {  
        super(fm);  
        mNewsData = newsData;  
        mContext = context;   
    }  
  
  
    @Override  
    public Fragment getItem(int position) {  
        DetailNewsFragment detailNewsFragment= new DetailNewsFragment();  
        Bundle args = new Bundle();  
        args.putInt("position", position);  
        args.putSerializable("list", mNewsData);  
        detailNewsFragment.setArguments(args);  
        fragmentMap.put(String.valueOf(position), detailNewsFragment);  
        return detailNewsFragment;  
    }  
  
    @Override  
    public int getCount() {  
        return mNewsData.size();  
    }  
      
    @Override  
    public void destroyItem(ViewGroup container, int position, Object object) {  
        super.destroyItem(container, position, object);  
    }  
  
}  