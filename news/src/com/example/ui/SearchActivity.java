package com.example.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.example.news.R;
import com.example.service.NewsManager;

public class SearchActivity extends SherlockFragmentActivity implements OnClickListener {
	private ListView mNewsList;
	private SimpleAdapter mNewsListAdapter;
	private ArrayList<HashMap<String, Object>> mNewsData;
	private LayoutInflater mInflater;
	private final int SUCCESS = 0;//加载成功
	private final int NOMORENEWS = 2;//
	private final int LOADERROR = 3;//加载失败
	private EditText keywordText;
	private String keyword=null;
	private ImageButton search_button;
	private Button goback_button;
	private LoadNewsAsyncTask loadNewsAsyncTask;
	private NewsManager mNewsManager;  
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		mNewsManager=new NewsManager();
		initView();
	}
	
	public void initView(){	
		initActionBar();
		keywordText=(EditText)findViewById(R.id.search_Keywords);
		mNewsList=(ListView)findViewById(R.id.searchlist);
		

		
		search_button=(ImageButton)findViewById(R.id.search_button);
		Log.d("tag2","haha");
		search_button.setOnClickListener(this);
		
		mNewsData = new ArrayList<HashMap<String,Object>>();
		
		mNewsListAdapter = new SimpleAdapter(this, mNewsData, R.layout.newslist_item, 
				new String[]{"newslist_item_title","newslist_item_digest","newslist_item_source","newslist_item_ptime"}, 
				new int[]{R.id.newslist_item_title,R.id.newslist_item_digest,R.id.newslist_item_source,R.id.newslist_item_ptime});

        mNewsList.setAdapter(mNewsListAdapter);
        mNewsList.setOnItemClickListener(new OnItemClickListener()
        {
          @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id)
         {
          Intent intent = new Intent(SearchActivity.this, NewsDetailsActivity.class);
          //把需要的信息放到Intent中
          intent.putExtra("newsData", mNewsData);//给分类的所有新闻头发过去
          intent.putExtra("position", position);
          intent.putExtra("categoryName", "搜索");
          startActivity(intent);
         }
        });
        
	}   
	
	public void initActionBar(){
		View headView = LayoutInflater.from(this).inflate(R.layout.search_actionbar, null);
		 goback_button=(Button)headView.findViewById(R.id.search_goback);
		 goback_button.setOnClickListener(this);
		 ActionBar actionBar = getSupportActionBar();
		actionBar.setCustomView(headView);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bg));
	}
        
     

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{

		case R.id.search_button:
			keyword=keywordText.getText().toString();
			Log.d("tag2","keyword ="+keyword);
			loadNewsAsyncTask = new LoadNewsAsyncTask();
			loadNewsAsyncTask.execute(keyword);
			break;
			
		case R.id.search_goback:
			finish();
			break;
		}
		
	}

	
	
	private class LoadNewsAsyncTask extends AsyncTask<Object, Integer, Integer>
	{
		
		@Override
		protected void onPreExecute()
		{
		}

		@Override
		//Object... params就是说可以有任意多个Object类型的参数，也就是说可以传递0到多个Object类或子类的对象到这个方法。
		protected Integer doInBackground(Object... params)
		{
			return mNewsManager.getSearchNews(mNewsData,(String)params[0]);
		}

		@Override
		
		//result是方法doInBackground(Object... params)返回的值
		protected void onPostExecute(Integer result)
		{
			switch (result)
			{

			case NOMORENEWS:
				Toast.makeText(SearchActivity.this, "没有搜到相关新闻", Toast.LENGTH_LONG).show();
				break;
			case LOADERROR:
				Toast.makeText(SearchActivity.this, "新闻加载失败", Toast.LENGTH_LONG).show();
				break;
			}
			
			//提醒更新新闻
			mNewsListAdapter.notifyDataSetChanged();
		}
	}
		
	
}

