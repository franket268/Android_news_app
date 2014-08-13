package com.example.adapter;

import java.util.ArrayList;
import java.util.HashMap;


import com.example.news.R;
import com.example.service.NewsManager;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class CatePagerAdapter extends PagerAdapter {
	private int pagerCount;
	private ListView newsList;
	private Context mContext;
	private NewsManager newsManager;
	private int startNid=0;
    private boolean firstTimes=true;
	private Button mTitlebarRefresh;
	private ProgressBar mLoadNewsProgress;
	private Button mLoadMoreBtn;
	private ArrayList<HashMap<String, Object>> mNewsData= new ArrayList<HashMap<String,Object>>();
	private SimpleAdapter mNewsListAdapter;
	private final int NEWSCOUNT = 5; //返回新闻数目
	private final int SUCCESS = 0;//加载成功
	private final int NONEWS = 1;//该栏目下没有新闻
	private final int NOMORENEWS = 2;//该栏目下没有更多新闻
	private final int LOADERROR = 3;//加载失败
	
	public CatePagerAdapter(Context context,int count,Button titlebarRefresh,ProgressBar loadnewsProgress,Button loadMoreBtn){
		this.pagerCount=count;
		this.mContext=context;
		this.mTitlebarRefresh=titlebarRefresh;
		this.mLoadNewsProgress=loadnewsProgress;
		this.mLoadMoreBtn=loadMoreBtn;
		newsManager=new NewsManager();
	}
	
	
	@Override
	public int getCount() {
		return pagerCount;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}


	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		
		LoadNewsAsyncTask loadTask=new LoadNewsAsyncTask();
		loadTask.execute(position+1,0,true);
		View view=View.inflate(mContext, R.layout.news_list_fragment, null);
		newsList=(ListView) view.findViewById(R.id.newsList);
		mNewsListAdapter = new SimpleAdapter(mContext, mNewsData, R.layout.newslist_item, 
				new String[]{"newslist_item_title","newslist_item_digest","newslist_item_source","newslist_item_ptime"}, 
				new int[]{R.id.newslist_item_title,R.id.newslist_item_digest,R.id.newslist_item_source,R.id.newslist_item_ptime});
		newsList.setAdapter(mNewsListAdapter);
		return view ;
	}

	
    @Override    
    public void destroyItem(ViewGroup container, int position, Object object) {    
        super.destroyItem(container, position, object);
    } 
    
	private class LoadNewsAsyncTask extends AsyncTask<Object, Integer, Integer>
	{
		
		@Override
		protected void onPreExecute()
		{
			//隐藏刷新按钮
			mTitlebarRefresh.setVisibility(View.GONE);
			//显示进度条
			mLoadNewsProgress.setVisibility(View.VISIBLE); 
			//正在加载时显示信息
			mLoadMoreBtn.setText("正在加载，请稍后···");
		}

		@Override
		//Object... params就是说可以有任意多个Object类型的参数，也就是说可以传递0到多个Object类或子类的对象到这个方法。
		protected Integer doInBackground(Object... params)
		{
			return newsManager.getSpeCateNews((Integer)params[0],mNewsData,(Integer)params[1],(Boolean)params[2]);
		}

		@Override
		
		//result是方法doInBackground(Object... params)返回的值
		protected void onPostExecute(Integer result)
		{
			//根据返回值显示相关的Toast
			switch (result)
			{
			case NONEWS:
				Toast.makeText(mContext,"没有新闻", Toast.LENGTH_LONG).show();
			break;
			case NOMORENEWS:
				Toast.makeText(mContext, "没有更多新闻", Toast.LENGTH_LONG).show();
				break;
			case LOADERROR:
				Toast.makeText(mContext, "新闻加载失败", Toast.LENGTH_LONG).show();
				break;
			}
			//通知ListView进行更新
			mNewsListAdapter.notifyDataSetChanged();
			//显示刷新按钮
			mTitlebarRefresh.setVisibility(View.VISIBLE);
			//隐藏进度条
			mLoadNewsProgress.setVisibility(View.GONE); 
			//设置LoadMore Button 显示文本
			mLoadMoreBtn.setText("加载更多");
		}
	}
}

    
    
    

