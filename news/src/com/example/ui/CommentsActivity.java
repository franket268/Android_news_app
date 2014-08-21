package com.example.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.Parameter;
import com.example.news.R;
import com.example.service.SyncHttp;

public class CommentsActivity extends Activity
{
	private List<HashMap<String,Object>> mCommsData;
	private ImageButton mNewsReplyImgBtn;// �������Żظ�ͼƬ
	private LinearLayout mNewsReplyImgLayout;// �������Żظ�ͼƬLayout
	private RelativeLayout mNewsReplyEditLayout;// �������Żظ��ظ�Layout
	private TextView mNewsReplyContent;// ���Żظ�����
	private Button canclebtn;//����ȡ���
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
		// �������Żظ�ͼƬLayout
		mNewsReplyImgLayout = (LinearLayout) findViewById(R.id.news_reply_img_layout);
	    // �������Żظ��ظ�Layout
	   mNewsReplyEditLayout = (RelativeLayout) findViewById(R.id.news_reply_edit_layout);
		
		Intent intent=getIntent();
		nid=intent.getIntExtra("nid", 0);//��ȡ����ʱĬ��ֵΪ0
		mCommsData=new ArrayList<HashMap<String,Object>>();

		getComments(nid);
		SimpleAdapter commentsAdapter = new SimpleAdapter(this, mCommsData, R.layout.comments_list_item, new String[]
		{ "commentator_from", "comment_ptime", "comment_content" }, new int[]
		{ R.id.commentator_from, R.id.comment_ptime, R.id.comment_content });

		ListView commentsList = (ListView) findViewById(R.id.comments_list);
		commentsList.setAdapter(commentsAdapter);
		
		
		//�ѷ��?ť����
		news_share_btn=(ImageButton)findViewById(R.id.news_share_btn);
		news_share_btn.setVisibility(View.GONE);
		
		//��ȡ��������
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
		
		
		// ����ظ�
		Button newsReplyPost = (Button) findViewById(R.id.news_reply_post);
		newsReplyPost.setOnClickListener(commsOnClickListener);
		//ȡ����
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
			// ���Żظ�ͼƬ
			case R.id.news_reply_img_btn:
				mNewsReplyImgLayout.setVisibility(View.GONE);
				mNewsReplyEditLayout.setVisibility(View.VISIBLE);
				mNewsReplyContent.requestFocus();  //ʹ�����۽�
				m.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
				break;
			// �������Żظ�
			case R.id.news_reply_post:
				mNewsReplyEditLayout.post(new PostCommentThread());
				mNewsReplyImgLayout.setVisibility(View.VISIBLE);
				mNewsReplyEditLayout.setVisibility(View.GONE);
				m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				break;
		  //ȡ��
			case R.id.news_reply_delete:
				// �������Żظ�Layout�Ƿ�ɼ�
				mNewsReplyImgLayout.setVisibility(View.VISIBLE);
				mNewsReplyEditLayout.setVisibility(View.GONE);
				InputMethodManager im = (InputMethodManager) mNewsReplyContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//�����������
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
			String url = "http://54.186.248.222:8080/web/postComment";
			List<Parameter> params = new ArrayList<Parameter>();
			params.add(new Parameter("nid", nid + ""));
			params.add(new Parameter("region", "֣����"));
			params.add(new Parameter("content", mNewsReplyContent.getText().toString()));
			try
			{
				String retStr = syncHttp.httpPost(url, params);
				JSONObject jsonObject = new JSONObject(retStr);
				int retCode = jsonObject.getInt("ret");
				if (0 == retCode)
				{
					Toast.makeText(CommentsActivity.this, "���۳ɹ�", Toast.LENGTH_SHORT).show();
					mNewsReplyImgLayout.setVisibility(View.VISIBLE);
					mNewsReplyEditLayout.setVisibility(View.GONE);
					return;
				}

			} catch (Exception e)
			{
				e.printStackTrace();
			}
			Toast.makeText(CommentsActivity.this,"����ʧ��", Toast.LENGTH_SHORT).show();
		}
	}

	
	private void getComments(int nid)
	{
	//����URL���ַ�
	String url = "http://54.186.248.222:8080/web/getComments";
	String params ="nid="+nid+"&startnid=0&count=10";
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
			if (totalnum>0)
			{
				//��ȡ�������ż���
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
				Toast.makeText(CommentsActivity.this, "���޻ظ�", Toast.LENGTH_LONG).show();
			}
		}
		else
		{
			Toast.makeText(CommentsActivity.this, "��ȡ����ʧ��", Toast.LENGTH_LONG).show();
		}
	} catch (Exception e)
	{
		e.printStackTrace();
		
	}
}
}
