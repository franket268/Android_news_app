package com.example.news;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.model.Parameter;

import com.example.service.SyncHttp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CommentsActivity extends Activity
{
	private List<HashMap<String,Object>> mCommsData;
	private ImageButton mNewsReplyImgBtn;// 发表新闻回复图片
	private LinearLayout mNewsReplyImgLayout;// 发表新闻回复图片Layout
	private RelativeLayout mNewsReplyEditLayout;// 发表新闻回复回复Layout
	private TextView mNewsReplyContent;// 新闻回复内容
	private Button canclebtn;//发表取消键
	private ImageButton news_share_btn;
	private int nid;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comments);
		
		StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		//
		// 查找新闻回复图片Layout
		mNewsReplyImgLayout = (LinearLayout) findViewById(R.id.news_reply_img_layout);
	    // 查找新闻回复回复Layout
	   mNewsReplyEditLayout = (RelativeLayout) findViewById(R.id.news_reply_edit_layout);
		
		Intent intent=getIntent();
		nid=intent.getIntExtra("nid", 0);//获取不到时默认值为0
		mCommsData=new ArrayList<HashMap<String,Object>>();

		getComments(nid);
		SimpleAdapter commentsAdapter = new SimpleAdapter(this, mCommsData, R.layout.comments_list_item, new String[]
		{ "commentator_from", "comment_ptime", "comment_content" }, new int[]
		{ R.id.commentator_from, R.id.comment_ptime, R.id.comment_content });

		ListView commentsList = (ListView) findViewById(R.id.comments_list);
		commentsList.setAdapter(commentsAdapter);
		
		
		//把分享按钮屏蔽
		news_share_btn=(ImageButton)findViewById(R.id.news_share_btn);
		news_share_btn.setVisibility(View.GONE);
		
		//获取新闻内容
		mNewsReplyContent = (TextView) findViewById(R.id.news_reply_edittext);

		
		Button commsTitlebarNews=(Button)findViewById(R.id.comments_titlebar_news);
		commsTitlebarNews.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		CommentsOnClickListener commsOnClickListener = new CommentsOnClickListener();
		mNewsReplyImgBtn = (ImageButton) findViewById(R.id.news_reply_img_btn);
		mNewsReplyImgBtn.setOnClickListener(commsOnClickListener);
		
		
		// 发表回复
		Button newsReplyPost = (Button) findViewById(R.id.news_reply_post);
		newsReplyPost.setOnClickListener(commsOnClickListener);
		//取消发发表
		canclebtn=(Button)findViewById(R.id.news_reply_delete);
		canclebtn.setOnClickListener(commsOnClickListener);
		
	}
	
	class CommentsOnClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			InputMethodManager m = (InputMethodManager) mNewsReplyContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			switch (v.getId())
			{
			// 新闻回复图片
			case R.id.news_reply_img_btn:
				mNewsReplyImgLayout.setVisibility(View.GONE);
				mNewsReplyEditLayout.setVisibility(View.VISIBLE);
				mNewsReplyContent.requestFocus();  //使输入框聚焦
				m.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
				break;
			// 发表新闻回复
			case R.id.news_reply_post:
				mNewsReplyEditLayout.post(new PostCommentThread());
				mNewsReplyImgLayout.setVisibility(View.VISIBLE);
				mNewsReplyEditLayout.setVisibility(View.GONE);
				m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				break;
		  //取消发布
			case R.id.news_reply_delete:
				// 设置新闻回复Layout是否可见
				mNewsReplyImgLayout.setVisibility(View.VISIBLE);
				mNewsReplyEditLayout.setVisibility(View.GONE);
				InputMethodManager im = (InputMethodManager) mNewsReplyContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//隐藏输入键盘
				break;
			}
		}
	}
	


	

	private class PostCommentThread extends Thread
	{
		@Override
		public void run()
		{
			SyncHttp syncHttp = new SyncHttp();
			String url = "http://10.0.2.2:8080/web/postComment";
			List<Parameter> params = new ArrayList<Parameter>();
			params.add(new Parameter("nid", nid + ""));
			params.add(new Parameter("region", "郑州市"));
			params.add(new Parameter("content", mNewsReplyContent.getText().toString()));
			try
			{
				String retStr = syncHttp.httpPost(url, params);
				JSONObject jsonObject = new JSONObject(retStr);
				int retCode = jsonObject.getInt("ret");
				if (0 == retCode)
				{
					Toast.makeText(CommentsActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
					mNewsReplyImgLayout.setVisibility(View.VISIBLE);
					mNewsReplyEditLayout.setVisibility(View.GONE);
					return;
				}

			} catch (Exception e)
			{
				e.printStackTrace();
			}
			Toast.makeText(CommentsActivity.this,"评论失败", Toast.LENGTH_SHORT).show();
		}
	}

	
	private void getComments(int nid)
	{
	//请求URL和字符串
	String url = "http://10.0.2.2:8080/web/getComments";
	String params ="nid="+nid+"&startnid=0&count=10";
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
			else
			{
				Toast.makeText(CommentsActivity.this, "暂无回复", Toast.LENGTH_LONG).show();
			}
		}
		else
		{
			Toast.makeText(CommentsActivity.this, "获取评论失败", Toast.LENGTH_LONG).show();
		}
	} catch (Exception e)
	{
		e.printStackTrace();
		
	}
}
}
