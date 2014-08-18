package com.example.service;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class NewsManager {
	private final String Tag=NewsManager.class.getSimpleName();
	private final int COLUMNWIDTHPX = 55;
	private final int FLINGVELOCITYPX = 800; // 滚动距离
	private final int NEWSCOUNT = 5; //返回新闻数目
	private final int SUCCESS = 0;//加载成功
	private final int NONEWS = 1;//该栏目下没有新闻
	private final int NOMORENEWS = 2;//该栏目下没有更多新闻
	private final int LOADERROR = 3;//加载失败
	
	
	
	/**
	 * 获取指定类型的新闻列表
	 * @param newsList 保存新闻信息的集合
	 * @param startnid 分页
	 * @param firstTimes	是否第一次加载
	 */
	public int getSpeCateNews(List<HashMap<String, Object>> newsList,int startnid,Boolean firstTimes)
	{
		if (firstTimes)
		{
			//如果是第一次，则清空集合里数据
			newsList.clear();
		}
		//请求URL和字符串
		String url = "http://54.187.183.108:8080/web/getSpecifyCategoryNews";
		String params = "startnid="+startnid+"&count="+NEWSCOUNT;
		SyncHttp syncHttp = new SyncHttp();
		try
		{
			//以Get方式请求，并获得返回结果
			String retStr = syncHttp.httpGet(url, params);
			Log.d(Tag,"httpGet response= "+retStr);
			JSONObject jsonObject = new JSONObject(retStr);
			//获取返回码，0表示成功
			int retCode = jsonObject.getInt("ret");
			Log.d(Tag,"retCode= "+String.valueOf(retCode));
			if (0==retCode)
			{
				JSONObject dataObject = jsonObject.getJSONObject("data");
				//获取返回数目
				int totalnum = dataObject.getInt("totalnum");
				Log.d(Tag,"returnNewsTotalnum= "+String.valueOf(totalnum));
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
					if (firstTimes)
					{
						return NONEWS;
					}
					else
					{
						return NOMORENEWS;
					}
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
}
