package com.example.ui;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.example.adapter.DetailPagerAdapter;
import com.example.custom.CustomTextView;
import com.example.news.R;
import com.example.service.NewsManager;



public class NewsDetailsActivity extends SherlockFragmentActivity implements OnClickListener  
{  
    private ArrayList<HashMap<String, Object>> mNewsData;  
    private int mPosition ;  
    private int mNid;  
    private String newslist_item_title;  //新闻标题  
    private Button mNewsdetailsTitlebarComm;// 新闻回复数  
    private ImageButton mNewsReplyImgBtn;// 发表新闻回复图片  
    private LinearLayout mNewsReplyImgLayout;// 发表新闻回复图片Layout  
    private RelativeLayout mNewsReplyEditLayout;// 发表新闻回复回复Layout  
    private TextView mNewsReplyContent;// 新闻回复内容  
    private String region="广东网友";//定位所在地方，默认是广东  
    private String categoryName;
    private ImageButton share;//分享按钮  
    private NewsManager mNewsManager;  
    private final int POST_COMMENT_ERROR=2;  
    private final int POST_COMMENT_SUCCESS=3;  
  
    private ViewPager detailViewPager;  
    private DetailPagerAdapter detailPagerAdapter;  
    private ImageButton tabBack;
    
    private Handler mHandler = new Handler(){  
        @Override  
        public void handleMessage(Message msg)  
        {  
              
            switch (msg.arg1)  
            {  
                case POST_COMMENT_ERROR:  
                    Toast.makeText(NewsDetailsActivity.this, "发表失败", Toast.LENGTH_SHORT).show();  
                    break;  
                case POST_COMMENT_SUCCESS:  
                    Toast.makeText(NewsDetailsActivity.this, "发表成功", Toast.LENGTH_SHORT).show();  
                    break;  
                default:  
                    break;  
                  
            }  
        }  
    };  
  
    @Override  
    public void onCreate(Bundle savedInstanceState)  
    {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.newsdetails);  
        mNewsManager=new NewsManager();  
        // 获取传递的数据  
        Intent intent = getIntent();  
        Bundle bundle = intent.getExtras();  
        // 设置标题栏名称  
        categoryName = bundle.getString("categoryName");  
        // 获取新闻集合  
        Serializable s = bundle.getSerializable("newsData");  
        mNewsData = (ArrayList<HashMap<String, Object>>) s;  
        // 获取点击位置  
        mPosition = bundle.getInt("position");  
        mNid=(int) mNewsData.get(mPosition).get("nid");
        
        initView();  
    }  
      
    public void initView(){  
        initActionBar();  
    

        mNewsReplyImgLayout = (LinearLayout) findViewById(R.id.news_reply_img_layout);    
        mNewsReplyEditLayout = (RelativeLayout) findViewById(R.id.news_reply_edit_layout);  
        // 新闻回复内容  
        mNewsReplyContent = (TextView) findViewById(R.id.news_reply_edittext);  
      
          
        // 发表新闻回复图片Button  
        mNewsReplyImgBtn = (ImageButton) findViewById(R.id.news_reply_img_btn);  
        mNewsReplyImgBtn.setOnClickListener(this);  
        // 发表回复  
        Button newsReplyPost = (Button) findViewById(R.id.news_reply_post);  
        newsReplyPost.setOnClickListener(this);  

        //分享新闻  
        share=(ImageButton)findViewById(R.id.news_share_btn);  
        share.setOnClickListener(this);  

        TextView titleBarTitle = (TextView) findViewById(R.id.newsdetails_titlebar_title);  
        titleBarTitle.setText(categoryName);  


         
        tabBack=(ImageButton) findViewById(R.id.tap_back);
        tabBack.setOnClickListener(this);
        detailViewPager=(ViewPager) findViewById(R.id.news_body_pager);  
        detailPagerAdapter=new DetailPagerAdapter(getSupportFragmentManager(),this, mNewsData);  
        detailViewPager.setAdapter(detailPagerAdapter);  
        detailViewPager.setCurrentItem(mPosition);  
        detailViewPager.setOnPageChangeListener(new OnPageChangeListener() {  
              
            @Override  
            public void onPageSelected(int position) {  
                mPosition=position;  
                mNid=(int) mNewsData.get(mPosition).get("nid");
                mNewsdetailsTitlebarComm.setText(mNewsData.get(mPosition).get("newslist_item_comments") + "跟帖");
            }  
              
            @Override  
            public void onPageScrolled(int arg0, float arg1, int arg2) {  
                // TODO Auto-generated method stub  
                  
            }  
              
            @Override  
            public void onPageScrollStateChanged(int arg0) {  
                // TODO Auto-generated method stub  
                  
            }  
        });  
    }  
      
    public void  initActionBar(){  
        View headView = LayoutInflater.from(this).inflate(R.layout.detail_news_actionbar, null);  
        // 上一篇新闻  
        Button newsDetailsTitlebarPref = (Button)headView.findViewById(R.id.newsdetails_titlebar_previous);  
        newsDetailsTitlebarPref.setOnClickListener(this);  
        // 下一篇新闻  
        Button newsDetailsTitlebarNext = (Button)headView.findViewById(R.id.newsdetails_titlebar_next);  
        newsDetailsTitlebarNext.setOnClickListener(this);  
        // 新闻回复条数Button  
        mNewsdetailsTitlebarComm = (Button) headView.findViewById(R.id.newsdetails_titlebar_comments);  
        mNewsdetailsTitlebarComm.setOnClickListener(this);  
        mNewsdetailsTitlebarComm.setText(mNewsData.get(mPosition).get("newslist_item_comments") + "跟帖");
        ActionBar actionBar = getSupportActionBar();  
        actionBar.setCustomView(headView);  
        actionBar.setDisplayShowCustomEnabled(true);  
        actionBar.setDisplayShowHomeEnabled(false);  
        actionBar.setDisplayHomeAsUpEnabled(true);  
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);  
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bg));  
    }  
      
  
  
    /** 
     * 处理NewsDetailsTitleBar点击事件 
     */  
  
    @Override  
    public void onClick(View v)  
    {  
        InputMethodManager m = (InputMethodManager) mNewsReplyContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
        switch (v.getId())  
        {  
        // 上一条新闻  
        case R.id.newsdetails_titlebar_previous:  
            if (mPosition > 0)  
            {  
                detailViewPager.setCurrentItem(mPosition-1);  
            } else  
            {  
                Toast.makeText(this,"没有上条新闻", Toast.LENGTH_SHORT).show();  
            }  
            break;  
        // 下一条新闻  
        case R.id.newsdetails_titlebar_next:  
            if (mPosition < mNewsData.size() - 1)  
            {  
                detailViewPager.setCurrentItem(mPosition+1);  
            } else  
            {  
                Toast.makeText(this,"没有下条新闻", Toast.LENGTH_SHORT).show();  
            }  
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
            break;  
        // 发表新闻回复  
        case R.id.news_reply_post:  
            new PostCommentThread().start();  
            mNewsReplyImgLayout.setVisibility(View.VISIBLE);  
            mNewsReplyEditLayout.setVisibility(View.GONE);  
            m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
            break;  

        case R.id.news_share_btn:  
            //设置分享新闻  
           Intent intent2=new Intent(Intent.ACTION_SEND);    
           intent2.setType("image/*");    
           intent2.putExtra(Intent.EXTRA_TEXT,"来自展鸿新闻客户端："+ newslist_item_title);    
           intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
           startActivity(Intent.createChooser(intent2, getTitle()));  
           break;
        case R.id.tap_back:
        	mNewsReplyImgLayout.setVisibility(View.VISIBLE);
        	mNewsReplyEditLayout.setVisibility(View.GONE);
        	break;
        }  
    }  
      
  
  
    private class PostCommentThread extends Thread  
    {  
          
        @Override  
        public void run()  
        {   
            int retCode=mNewsManager.postComment(mNid, region,  mNewsReplyContent.getText().toString());  
            Message msg = mHandler.obtainMessage();  
            if(retCode==0){               
                msg.arg1 = POST_COMMENT_SUCCESS;  
                  
            }  
            else{  
                msg.arg1=POST_COMMENT_ERROR;  
            }  
            mHandler.sendMessage(msg);  
        }  
    }  
  
  
  
}  