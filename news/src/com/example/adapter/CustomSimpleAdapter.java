package com.example.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class CustomSimpleAdapter extends SimpleAdapter{

	public CustomSimpleAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
	}
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	View view= super.getView(position, convertView, parent);
    	//���µ�һ������
    	if(position==0){
    		TextView categoryTitle=(TextView)view;
			categoryTitle.setTextColor(0XFFFFFFFF);
			categoryTitle.setBackgroundColor(0xff6600);
    	}
    	return view;
    }
}
