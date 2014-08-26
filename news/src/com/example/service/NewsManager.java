package com.example.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.example.model.Parameter;

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
	 * @param cid 类型ID
	 * @param newsList 保存新闻信息的集合
	 * @param startnid 分页
	 * @param firstTimes	是否第一次加载
	 */
	public int getSpeCateNews(int cid,List<HashMap<String, Object>> newsList,int startnid,Boolean firstTimes)
	{
		if (firstTimes)
		{
			//如果是第一次，则清空集合里数据
			newsList.clear();
		}
		//请求URL和字符串
		String url = "http://54.187.183.108:8080/web/getSpecifyCategoryNews";
		String params = "startnid="+startnid+"&count="+NEWSCOUNT+"&cid="+cid;
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
	
	/**
	 * 获取新闻详细信息
	 * 
	 * @return
	 */
	public ArrayList<HashMap<String,Object>> getNewsBody(int nid)
	{
		SyncHttp syncHttp = new SyncHttp();
		ArrayList<HashMap<String,Object>> bodyList=new ArrayList<HashMap<String,Object>>();
		String url = "http://54.187.183.108:8080/web/getNews";
		String params = "nid=" + nid;
		try
		{
			String retString = syncHttp.httpGet(url, params);
			JSONObject jsonObject = new JSONObject(retString);
			// 获取返回码，0表示成功
			int retCode = jsonObject.getInt("ret");
			if (0 == retCode)
			{
				JSONObject dataObject = jsonObject.getJSONObject("data");
				JSONObject newsObject = dataObject.getJSONObject("news");
				//retStr = newsObject.getString("body");
				//String s=newsObject.getString("imgsrc");
				//System.out.println(s);
				JSONArray bodyArray=newsObject.getJSONArray("body");
				for(int i=0;i<bodyArray.length();i++)
				{
					JSONObject object=(JSONObject)bodyArray.opt(i);
					HashMap<String,Object> hashMap=new HashMap<String,Object>();
					hashMap.put("index", object.get("index"));
					hashMap.put("type", object.get("type"));
					hashMap.put("value", object.get("value"));
				
					bodyList.add(hashMap);
				}
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return bodyList;
	}
	
	/**
	 * 发表评论

	 */
	
	public int postComment(int nid,String region,String replyContent){
		SyncHttp syncHttp = new SyncHttp();
		String url = "http://54.187.183.108:8080/web/postComment";
		List<Parameter> params = new ArrayList<Parameter>();
		int retCode=-1;
		params.add(new Parameter("nid","11"));
		params.add(new Parameter("region","lala"));
		params.add(new Parameter("content", "haha"));
		try
		{
			String retStr = syncHttp.httpPost(url, params);
			JSONObject jsonObject = new JSONObject(retStr);
			retCode = jsonObject.getInt("ret");
				

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		Log.d("tag1",String.valueOf(retCode));
       return retCode;
	}
	
	
	/**
	 * 获取评论

	 */
	
	
	public int getComments(int nid,List<HashMap<String,Object>> mCommsData)
	{
	//请求URL和字符串
	String url = "http://54.187.183.108:8080/web/getComments";
	String params ="nid="+nid+"&startnid=0&count=10";
	SyncHttp syncHttp = new SyncHttp();
	int retCode =-1;
	try
	{
		//以Get方式请求，并获得返回结果
		String retStr = syncHttp.httpGet(url, params);
		JSONObject jsonObject = new JSONObject(retStr);
		//获取返回码，0表示成功
	    retCode = jsonObject.getInt("ret");
		if (0==retCode)
		{
			JSONObject dataObject = jsonObject.getJSONObject("data");
			//获取返回数目
			int totalnum = dataObject.getInt("totalnum");
			if (totalnum>0)
			{
				//获取返回新闻集合
				JSONArray newslist = dataObject.getJSONArray("commentslist");
				for(int i=0;i<newslist.length();i++)
				{
					JSONObject newsObject = (JSONObject)newslist.opt(i); 
					HashMap<String, Object> hashMap = new HashMap<String, Object>();
					hashMap.put("comid", newsObject.getInt("comid"));
					hashMap.put("commentator_from", newsObject.getString("region"));
					hashMap.put("comment_ptime", newsObject.getString("ptime"));
					hashMap.put("comment_content", newsObject.getString("content"));
					mCommsData.add(hashMap);
				}
			}

		}

	} catch (Exception e)
	{
		e.printStackTrace();
		
	}
	return retCode;
}
	
}
