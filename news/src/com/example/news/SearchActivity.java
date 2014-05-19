package com.example.news;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;



import com.example.service.SyncHttp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchActivity extends Activity {
	private ListView mNewsList;
	private SimpleAdapter mNewsListAdapter;
	private ArrayList<HashMap<String, Object>> mNewsData;
	private LayoutInflater mInflater;
	private final int SUCCESS = 0;//加载成功
	private final int NONEWS = 1;//该栏目下没有新闻
	private final int NOMORENEWS = 2;//该栏目下没有更多新闻
	private final int LOADERROR = 3;//加载失败
	private EditText keywordText;
	private String keyword=null;
	private ImageButton search_button;
	private LoadNewsAsyncTask loadNewsAsyncTask;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		
		keywordText=(EditText)findViewById(R.id.search_Keywords);
		mNewsList=(ListView)findViewById(R.id.searchlist);
		
		Button goback_button=(Button)findViewById(R.id.search_goback);
		goback_button.setOnClickListener(ButtonListener);
		
		search_button=(ImageButton)findViewById(R.id.search_button);
		search_button.setOnClickListener(ButtonListener);
		
		mNewsData = new ArrayList<HashMap<String,Object>>();
		
		mNewsListAdapter = new SimpleAdapter(this, mNewsData, R.layout.newslist_item, 
				new String[]{"newslist_item_title","newslist_item_digest","newslist_item_source","newslist_item_ptime"}, 
				new int[]{R.id.newslist_item_title,R.id.newslist_item_digest,R.id.newslist_item_source,R.id.newslist_item_ptime});
  //      View loadMoreLayout = mInflater.inflate(R.layout.loadmore, null);
 //       mNewsList.addFooterView(loadMoreLayout);
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
        
        /**
		 * 获取搜索新闻列表
		
		 * @param newsList 保存新闻信息的集合
		 * @param startnid 分页
		 * @param firstTimes	是否第一次加载
		 */
        
	private int getSearchNews(List<HashMap<String, Object>> newsList,String key)
		{
            //清屏，清楚之前搜的的结果
	     	newsList.clear();
			//请求URL和字符串
			String url = "http://10.0.2.2:8080/web/getSearch";
			String params = "keyword="+key;
			SyncHttp syncHttp = new SyncHttp();
			try
			{
				//以Get方式请求，并获得返回结果
				
				String retStr = syncHttp.httpGet(url, params);
				JSONObject jsonObject = new JSONObject(retStr);
				//获取返回码，0表示成功
				int retCode = jsonObject.getInt("ret");
				
				if (0==retCode)
				{
					JSONObject dataObject = jsonObject.getJSONObject("data");
					//获取返回数目
					int totalnum = dataObject.getInt("totalnum");
					System.out.println(totalnum);
					if (totalnum>0)
					{
						//获取返回新闻集合
						JSONArray newslist = dataObject.getJSONArray("newslist");
						for(int i=0;i<newslist.length();i++)
						{
							JSONObject newsObject = (JSONObject)newslist.opt(i); 
							HashMap<String, Object> hashMap = new HashMap<String, Object>();
							hashMap.put("nid", newsObject.getInt("nid"));
							hashMap.put("newslist_item_title", newsObject.getString("title"));
							hashMap.put("newslist_item_digest", newsObject.getString("digest"));
							hashMap.put("newslist_item_source", newsObject.getString("source"));
							hashMap.put("newslist_item_ptime", newsObject.getString("ptime"));
							hashMap.put("newslist_item_comments", newsObject.getString("commentcount"));
							newsList.add(hashMap);
						}
						return SUCCESS;
					}
					else
					{
						System.out.println(key);
						
				     	return NOMORENEWS;
						
					}
				}
				else
				{
					return LOADERROR;
				}
			} catch (Exception e)
			{
				e.printStackTrace();
				return LOADERROR;
			}
		}
		
	
	private OnClickListener ButtonListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			loadNewsAsyncTask = new LoadNewsAsyncTask();
			switch (v.getId())
			{


			case R.id.search_button:
				keyword=keywordText.getText().toString();
				loadNewsAsyncTask = new LoadNewsAsyncTask();
				loadNewsAsyncTask.execute(keyword);
				break;
				
			case R.id.search_goback:
				finish();
				break;
			}
			
		}
	};
	
	
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
			return getSearchNews(mNewsData,(String)params[0]);
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

