package com.example.ui;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.custom.CustomTextView;
import com.example.model.Parameter;
import com.example.news.R;
import com.example.service.SyncHttp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;



public class NewsDetailsActivity extends Activity
{
	private final int FINISH = 0;

	private ViewFlipper mNewsBodyFlipper;
	private LayoutInflater mNewsBodyInflater;
	private float mStartX;
	private ArrayList<HashMap<String, Object>> mNewsData;
	private int mPosition = 0;
	private int mCursor;
	private int mNid;
	private String newslist_item_title;  //新闻标题
	private String newslist_item_digest;//新闻摘要
	private CustomTextView mNewsDetails;
	private Button mNewsdetailsTitlebarComm;// 新闻回复数
	private ImageButton mNewsReplyImgBtn;// 发表新闻回复图片
	private LinearLayout mNewsReplyImgLayout;// 发表新闻回复图片Layout
	private RelativeLayout mNewsReplyEditLayout;// 发表新闻回复回复Layout
	private TextView mNewsReplyContent;// 新闻回复内容
	private Button canclebtn;//发表取消键
	private String region="郑州";//定位所在地方，默认是郑州
	private Boolean keyboardShow=false;
	private ImageButton share;//分享按钮

	

	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.arg1)
			{
			case FINISH:
				// 把获取到的新闻显示到界面上
				//mNewsDetails.setText(Html.fromHtml(msg.obj.toString()));
				ArrayList<HashMap<String,Object>> bodyList=(ArrayList<HashMap<String,Object>>)msg.obj;
				mNewsDetails.setText(bodyList);
				break;
			}
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newsdetails);
		
		
//		//加了以后才能时更新UI线程
//		StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
//		StrictMode.setThreadPolicy(policy);
  
		// 查找新闻回复图片Layout
		mNewsReplyImgLayout = (LinearLayout) findViewById(R.id.news_reply_img_layout);
		// 查找新闻回复回复Layout
		mNewsReplyEditLayout = (RelativeLayout) findViewById(R.id.news_reply_edit_layout);
		// 新闻回复内容
		mNewsReplyContent = (TextView) findViewById(R.id.news_reply_edittext);
	

		NewsDetailsOnClickListener newsDetailsOnClickListener = new NewsDetailsOnClickListener();
		
		// 上一篇新闻
		Button newsDetailsTitlebarPref = (Button) findViewById(R.id.newsdetails_titlebar_previous);
		newsDetailsTitlebarPref.setOnClickListener(newsDetailsOnClickListener);
		// 下一篇新闻
		Button newsDetailsTitlebarNext = (Button) findViewById(R.id.newsdetails_titlebar_next);
		newsDetailsTitlebarNext.setOnClickListener(newsDetailsOnClickListener);
		// 新闻回复条数Button
		mNewsdetailsTitlebarComm = (Button) findViewById(R.id.newsdetails_titlebar_comments);
		mNewsdetailsTitlebarComm.setOnClickListener(newsDetailsOnClickListener);
		// 发表新闻回复图片Button
		mNewsReplyImgBtn = (ImageButton) findViewById(R.id.news_reply_img_btn);
		mNewsReplyImgBtn.setOnClickListener(newsDetailsOnClickListener);
		// 发表回复
		Button newsReplyPost = (Button) findViewById(R.id.news_reply_post);
		newsReplyPost.setOnClickListener(newsDetailsOnClickListener);
		//发表取消
		canclebtn=(Button)findViewById(R.id.news_reply_delete);
		canclebtn.setOnClickListener(newsDetailsOnClickListener);
		
		//分享新闻
		share=(ImageButton)findViewById(R.id.news_share_btn);
		share.setOnClickListener(newsDetailsOnClickListener);

		
		
		// 获取传递的数据
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		// 设置标题栏名称
		String categoryName = bundle.getString("categoryName");
		TextView titleBarTitle = (TextView) findViewById(R.id.newsdetails_titlebar_title);
		titleBarTitle.setText(categoryName);
		// 获取新闻集合
		Serializable s = bundle.getSerializable("newsData");
		mNewsData = (ArrayList<HashMap<String, Object>>) s;
		// 获取点击位置
		mCursor = mPosition = bundle.getInt("position");

		// 动态创建新闻视图并赋值
		mNewsBodyInflater = getLayoutInflater();
		inflateView(0);
		
		
		
	}
	


	/**
	 * 处理NewsDetailsTitleBar点击事件
	 */
	class NewsDetailsOnClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			InputMethodManager m = (InputMethodManager) mNewsReplyContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			switch (v.getId())
			{
			// 上一条新闻
			case R.id.newsdetails_titlebar_previous:
				showPrevious();
				break;
			// 下一条新闻
			case R.id.newsdetails_titlebar_next:
				showNext();
				break;
			// 显示评论
			case R.id.newsdetails_titlebar_comments:
				Intent intent = new Intent(NewsDetailsActivity.this, CommentsActivity.class);
				//传递新闻ID
				intent.putExtra("nid", mNid);
				startActivity(intent);
				break;
			// 新闻回复图片
			case R.id.news_reply_img_btn:
				mNewsReplyImgLayout.setVisibility(View.GONE);
				mNewsReplyEditLayout.setVisibility(View.VISIBLE);
				mNewsReplyContent.requestFocus();  //使输入框聚焦
				m.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
				keyboardShow=true;
                break;
			// 发表新闻回复
			case R.id.news_reply_post:
				mNewsReplyEditLayout.post(new PostCommentThread());
				mNewsReplyImgLayout.setVisibility(View.VISIBLE);
				mNewsReplyEditLayout.setVisibility(View.GONE);
				m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				break;
			case R.id.news_reply_delete:
				// 设置新闻回复Layout是否可见
				mNewsReplyImgLayout.setVisibility(View.VISIBLE);
				mNewsReplyEditLayout.setVisibility(View.GONE);
				InputMethodManager im = (InputMethodManager) mNewsReplyContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//隐藏输入键盘
				keyboardShow=false;
				break;
			case R.id.news_share_btn:
				//设置分享新闻
				   Intent intent2=new Intent(Intent.ACTION_SEND);  
	               intent2.setType("image/*");  
	               intent2.putExtra(Intent.EXTRA_TEXT,"来自展鸿新闻客户端："+ newslist_item_title);  
	               intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	               startActivity(Intent.createChooser(intent2, getTitle()));  
			}
		}
	}

	/**
	 * 处理新闻NewsBody触摸事件
	 */
	class NewsBodyOnTouchListener implements OnTouchListener
	{
		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			switch (event.getAction())
			{
			// 手指按下
			case MotionEvent.ACTION_DOWN:
                if(keyboardShow)
                {
                	mNewsReplyImgLayout.setVisibility(View.VISIBLE);
    				mNewsReplyEditLayout.setVisibility(View.GONE);
    				InputMethodManager m = (InputMethodManager) mNewsReplyContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    				m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    				keyboardShow=false;
                }
				
				
				
				// 记录起始坐标
				mStartX = event.getX();
				break;
			// 手指抬起
			case MotionEvent.ACTION_UP:
				// 往左滑动
				if (event.getX() < mStartX)
				{
					showPrevious();
				}
				// 往右滑动
				else if (event.getX() > mStartX)
				{
					showNext();
				}
				break;
			}
			return true;
		}
	}

	/**
	 * 显示下一条新闻
	 */
	private void showNext()
	{
		// 判断是否是最后一篇新闻
		if (mPosition < mNewsData.size() - 1)
		{
			// 设置下一屏动画
			mNewsBodyFlipper.setInAnimation(this, R.anim.push_left_in);
			mNewsBodyFlipper.setOutAnimation(this, R.anim.push_left_out);
			mPosition++;
			HashMap<String, Object> hashMap = mNewsData.get(mPosition);
			mNid = (Integer) hashMap.get("nid");
			// 判断下一屏是否已经创建
			if (mPosition >= mNewsBodyFlipper.getChildCount())
			{
				inflateView(mNewsBodyFlipper.getChildCount());
			}
			// 显示下一屏
			mNewsBodyFlipper.showNext();
		} else
		{
			Toast.makeText(this, "没有下条新闻", Toast.LENGTH_SHORT).show();
		}
		System.out.println(mCursor + ";" + mPosition);
	}

	private void showPrevious()
	{
		if (mPosition > 0)
		{
			mPosition--;
			// 记录当前新闻编号
			HashMap<String, Object> hashMap = mNewsData.get(mPosition);
			mNid = (Integer) hashMap.get("nid");
			if (mCursor > mPosition)
			{
				mCursor = mPosition;
				inflateView(0);
		//		System.out.println(mNewsBodyFlipper.getChildCount());
				mNewsBodyFlipper.showNext();// 显示下一页
			}
			mNewsBodyFlipper.setInAnimation(this, R.anim.push_right_in);// 定义下一页进来时的动画
			mNewsBodyFlipper.setOutAnimation(this, R.anim.push_right_out);// 定义当前页出去的动画
			mNewsBodyFlipper.showPrevious();// 显示上一页
		} else
		{
			Toast.makeText(this,"没有上条新闻", Toast.LENGTH_SHORT).show();
		}
	}

	private void inflateView(int index)
	{
		// 动态创建新闻视图，并赋值
		View newsBodyLayout = mNewsBodyInflater.inflate(R.layout.news_body, null);
		// 获取点击新闻基本信息
		HashMap<String, Object> hashMap = mNewsData.get(mPosition);
		// 新闻标题
		TextView newsTitle = (TextView) newsBodyLayout.findViewById(R.id.newsdetails_body_title);
		newslist_item_title=(String)hashMap.get("newslist_item_title");
		newsTitle.setText(newslist_item_title);
		
		//新闻摘要
		newslist_item_digest=(String)hashMap.get("newslist_item_digest");
		
		// 发布时间和出处
		TextView newsPtimeAndSource = (TextView) newsBodyLayout.findViewById(R.id.news_body_ptime_source);
		newsPtimeAndSource.setText(hashMap.get("newslist_item_ptime").toString() + "    " + hashMap.get("newslist_item_source").toString());
		// 新闻编号
		mNid = (Integer) hashMap.get("nid");
		// 新闻回复数
		mNewsdetailsTitlebarComm.setText(hashMap.get("newslist_item_comments") + "跟帖");

		// 把新闻视图添加到Flipper中
		mNewsBodyFlipper = (ViewFlipper) findViewById(R.id.news_body_flipper);
		mNewsBodyFlipper.addView(newsBodyLayout, index);

		// 给新闻Body添加触摸事件
		mNewsDetails = (CustomTextView) newsBodyLayout.findViewById(R.id.news_body_details);
		mNewsDetails.setOnTouchListener(new NewsBodyOnTouchListener());

		// 启动线程
		new UpdateNewsThread().start();
	}

	/**
	 * 获取新闻详细信息
	 * 
	 * @return
	 */
	private ArrayList<HashMap<String,Object>> getNewsBody()
	{
		String retStr = "网络连接失败，请稍后再试";
		SyncHttp syncHttp = new SyncHttp();
		ArrayList<HashMap<String,Object>> bodyList=new ArrayList<HashMap<String,Object>>();
		String url = "http://54.186.248.222:8080/web/getNews";
		String params = "nid=" + mNid;
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
	 * 更新新闻内容

	 */
	private class UpdateNewsThread extends Thread
	{
		@Override
		public void run()
		{
			
			// 从网络上获取新闻
			ArrayList<HashMap<String,Object>> bodyList = getNewsBody();
			Message msg = mHandler.obtainMessage();
			msg.arg1 = FINISH;
			msg.obj = bodyList;
			mHandler.sendMessage(msg);
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
			params.add(new Parameter("nid", mNid + ""));
			params.add(new Parameter("region",region));
			params.add(new Parameter("content", mNewsReplyContent.getText().toString()));
			try
			{
				String retStr = syncHttp.httpPost(url, params);
				JSONObject jsonObject = new JSONObject(retStr);
				int retCode = jsonObject.getInt("ret");
				if (0 == retCode)
				{
					Toast.makeText(NewsDetailsActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
					mNewsReplyImgLayout.setVisibility(View.VISIBLE);
					mNewsReplyEditLayout.setVisibility(View.GONE);
					return;
				}

			} catch (Exception e)
			{
				e.printStackTrace();
			}
			Toast.makeText(NewsDetailsActivity.this,"评论失败", Toast.LENGTH_SHORT).show();
		}
	}
	

}