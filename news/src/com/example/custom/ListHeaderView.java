package com.example.custom;

import com.example.news.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ListHeaderView extends LinearLayout {
	private Context context;
	private ImageView imageview;
	private Drawable drawable;

	public Drawable getDrawable() {
		return drawable;
	}

	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}

	public ListHeaderView(Context context) {
		super(context);
		this.context=context;
		View view = LayoutInflater.from(this.context).inflate(R.layout.first_image, null); 
		addView(view);
		imageview=(ImageView) view.findViewById(R.id.firstImage);
		
	}
	
	public void setImage(Drawable drawable){
	//	imageview.setImageResource(R.drawable.p11);
		imageview.setImageDrawable(drawable);
	}

}
