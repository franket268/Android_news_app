package com.example.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.news.R;
import com.example.service.NewsManager;
import com.example.ui.MainActivity;
import com.example.ui.MainActivity.ViewHolder;
import com.example.ui.NewsDetailsActivity;

public class SportFragment extends Fragment implements OnClickListener{
	private ListView mFocusList;
	private NewsManager mNewsManager;
	private ArrayList<HashMap<String, Object>> mNewsData= new ArrayList<HashMap<String,Object>>();
	private LoadNewsAsyncTask asyncTask=new LoadNewsAsyncTask();
	private final int CATEGORY_TYPE = 5;
	private SimpleAdapter mNewsListAdapter;
	private Button mLoadMoreBtn;
	private ViewHolder mHolder;
	
	private final int NEWSCOUNT = 5; //返回新闻数目
	private final int SUCCESS = 0;//加载成功
	private final int NONEWS = 1;//该栏目下没有新闻
	private final int NOMORENEWS = 2;//该栏目下没有更多新闻
	private final int LOADERROR = 3;//加载失败
	private View loadMoreLayout;
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHolder=((MainActivity)getActivity()).getViewHolder();
		mNewsManager=new NewsManager();
	    loadMoreLayout = getActivity().getLayoutInflater().inflate(R.layout.loadmore, null);
		mLoadMoreBtn=(Button) loadMoreLayout.findViewById(R.id.loadmore_btn);
		asyncTask.execute(CATEGORY_TYPE,0,true);
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		return inflater.inflate(R.layout.news_list_fragment, null);
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
		mFocusList=(ListView) contentView.findViewById(R.id.newsList);
		
		mLoadMoreBtn.setOnClickListener(this);
		mHolder.titlebarRefresh.setOnClickListener(this);
		mFocusList.addFooterView(loadMoreLayout);
		mNewsListAdapter = new SimpleAdapter(getActivity(), mNewsData, R.layout.newslist_item, 
				new String[]{"newslist_item_title","newslist_item_digest","newslist_item_source","newslist_item_ptime"}, 
				new int[]{R.id.newslist_item_title,R.id.newslist_item_digest,R.id.newslist_item_source,R.id.newslist_item_ptime});
		mFocusList.setAdapter(mNewsListAdapter);
		mFocusList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(), NewsDetailsActivity.class);
				//把需要的信息放到Intent中
				intent.putExtra("newsData", mNewsData);//给分类的所有新闻头发过去
				intent.putExtra("position", position);
				intent.putExtra("categoryName", getActivity().getResources().getString(R.string.sport));
				startActivity(intent);
				
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		asyncTask=new LoadNewsAsyncTask();
		switch (v.getId()){
		case R.id.loadmore_btn:
			asyncTask.execute(CATEGORY_TYPE,mNewsData.size(),false);
			break;
		case R.id.titlebar_refresh:
			asyncTask.execute(CATEGORY_TYPE,0,true);
			break;
		   
		}
	}
	
	
	private class LoadNewsAsyncTask extends AsyncTask<Object, Integer, Integer>
	{
		
		@Override
		protected void onPreExecute()
		{
			//隐藏刷新按钮
			mHolder.titlebarRefresh.setVisibility(View.GONE);
			//显示进度条
			mHolder.loadnewsProgress.setVisibility(View.VISIBLE); 
			//正在加载时显示信息
			mLoadMoreBtn.setText("正在加载，请稍后···");
		}

		@Override
		//Object... params就是说可以有任意多个Object类型的参数，也就是说可以传递0到多个Object类或子类的对象到这个方法。
		protected Integer doInBackground(Object... params)
		{
			return mNewsManager.getSpeCateNews((Integer)params[0],mNewsData,(Integer)params[1],(Boolean)params[2]);
		}

		@Override
		
		//result是方法doInBackground(Object... params)返回的值
		protected void onPostExecute(Integer result)
		{
			//根据返回值显示相关的Toast
			switch (result)
			{
			case NONEWS:
				Toast.makeText(getActivity(),"没有新闻", Toast.LENGTH_LONG).show();
			break;
			case NOMORENEWS:
				Toast.makeText(getActivity(), "没有更多新闻", Toast.LENGTH_LONG).show();
				break;
			case LOADERROR:
				Toast.makeText(getActivity(), "新闻加载失败", Toast.LENGTH_LONG).show();
				break;
			}
			//通知ListView进行更新
			mNewsListAdapter.notifyDataSetChanged();
			//显示刷新按钮
			mHolder.titlebarRefresh.setVisibility(View.VISIBLE);
			//隐藏进度条
			mHolder.loadnewsProgress.setVisibility(View.GONE); 
			//设置LoadMore Button 显示文本
			mLoadMoreBtn.setText("加载更多");
		}
	}


	
}

