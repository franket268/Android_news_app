package com.example.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.example.news.R;
import com.example.service.SyncHttp;

public class SearchActivity extends Activity {
	private ListView mNewsList;
	private SimpleAdapter mNewsListAdapter;
	private ArrayList<HashMap<String, Object>> mNewsData;
	private LayoutInflater mInflater;
	private final int SUCCESS = 0;//���سɹ�
	private final int NONEWS = 1;//����Ŀ��û������
	private final int NOMORENEWS = 2;//����Ŀ��û�и������
	private final int LOADERROR = 3;//����ʧ��
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
          //����Ҫ����Ϣ�ŵ�Intent��
          intent.putExtra("newsData", mNewsData);//��������������ͷ����ȥ
          intent.putExtra("position", position);
          intent.putExtra("categoryName", "����");
          startActivity(intent);
         }
        });
        
	}   
        
        /**
		 * ��ȡ���������б�
		
		 * @param newsList ����������Ϣ�ļ���
		 * @param startnid ��ҳ
		 * @param firstTimes	�Ƿ��һ�μ���
		 */
        
	private int getSearchNews(List<HashMap<String, Object>> newsList,String key)
		{
            //���������֮ǰ�ѵĵĽ��
	     	newsList.clear();
			//����URL���ַ�
			String url = "http://54.186.248.222:8080/web/getSearch";
			String params = "keyword="+key;
			SyncHttp syncHttp = new SyncHttp();
			try
			{
				//��Get��ʽ���󣬲���÷��ؽ��
				
				String retStr = syncHttp.httpGet(url, params);
				JSONObject jsonObject = new JSONObject(retStr);
				//��ȡ�����룬0��ʾ�ɹ�
				int retCode = jsonObject.getInt("ret");
				
				if (0==retCode)
				{
					JSONObject dataObject = jsonObject.getJSONObject("data");
					//��ȡ������Ŀ
					int totalnum = dataObject.getInt("totalnum");
					System.out.println(totalnum);
					if (totalnum>0)
					{
						//��ȡ�������ż���
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
		//Object... params����˵������������Object���͵Ĳ���Ҳ����˵���Դ���0�����Object�������Ķ������������
		protected Integer doInBackground(Object... params)
		{
			return getSearchNews(mNewsData,(String)params[0]);
		}

		@Override
		
		//result�Ƿ���doInBackground(Object... params)���ص�ֵ
		protected void onPostExecute(Integer result)
		{
			switch (result)
			{

			case NOMORENEWS:
				Toast.makeText(SearchActivity.this, "û���ѵ��������", Toast.LENGTH_LONG).show();
				break;
			case LOADERROR:
				Toast.makeText(SearchActivity.this, "���ż���ʧ��", Toast.LENGTH_LONG).show();
				break;
			}
			
			//���Ѹ�������
			mNewsListAdapter.notifyDataSetChanged();
		}
	}
		
		
		
		
		
}

