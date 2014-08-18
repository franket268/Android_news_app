package com.example.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.news.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ListView;
import android.widget.Toast;

public class FocusFragment extends Fragment{
	private ListView mFocusList;
	private ArrayList<HashMap<String, Object>> mNewsData= new ArrayList<HashMap<String,Object>>();;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		return inflater.inflate(R.layout.focus_fragment, null);
	}
     

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		buildView();
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	
	public void buildView(){
		View contentView = getView();
		if (null == contentView) {
			return;
		}
		mFocusList=(ListView) contentView.findViewById(R.id.focus_list);
		
		
	}
	
	private class LoadNewsAsyncTask extends AsyncTask<Object, Integer, Integer>
	{
		
		@Override
		protected void onPreExecute()
		{
//			//隐藏刷新按钮
//			mTitlebarRefresh.setVisibility(View.GONE);
//			//显示进度条
//			mLoadnewsProgress.setVisibility(View.VISIBLE); 
//			//正在加载时显示信息
//			mLoadMoreBtn.setText("正在加载，请稍后···");
		}

		@Override
		//Object... params就是说可以有任意多个Object类型的参数，也就是说可以传递0到多个Object类或子类的对象到这个方法。
		protected Integer doInBackground(Object... params)
		{
			return getSpeCateNews((Integer)params[0],mNewsData,(Integer)params[1],(Boolean)params[2]);
		}

		@Override
		
		//result是方法doInBackground(Object... params)返回的值
		protected void onPostExecute(Integer result)
		{
			//根据返回值显示相关的Toast
			switch (result)
			{
			case NONEWS:
				Toast.makeText(MainActivity.this,"没有新闻", Toast.LENGTH_LONG).show();
			break;
			case NOMORENEWS:
				Toast.makeText(MainActivity.this, "没有更多新闻", Toast.LENGTH_LONG).show();
				break;
			case LOADERROR:
				Toast.makeText(MainActivity.this, "新闻加载失败", Toast.LENGTH_LONG).show();
				break;
			}
			//通知ListView进行更新
			mNewsListAdapter.notifyDataSetChanged();
			//显示刷新按钮
			mTitlebarRefresh.setVisibility(View.VISIBLE);
			//隐藏进度条
			mLoadnewsProgress.setVisibility(View.GONE); 
			//设置LoadMore Button 显示文本
			mLoadMoreBtn.setText("加载更多");
		}
	}
	
}
