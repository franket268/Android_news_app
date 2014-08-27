package com.example.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
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

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.example.news.R;
import com.example.service.NewsManager;

public class CommentsActivity extends SherlockFragmentActivity implements OnClickListener   {  
    private List<HashMap<String,Object>> mCommsData;  
    private ImageButton mNewsReplyImgBtn;// 发表新闻回复图片  
    private LinearLayout mNewsReplyImgLayout;// 发表新闻回复图片Layout  
    private RelativeLayout mNewsReplyEditLayout;// 发表新闻回复回复Layout  
    private TextView mNewsReplyContent;// 新闻回复内容  
    private ImageButton news_share_btn;  
    private int mNid;  
    private String mRegion="广东网友";  
    private NewsManager mNewsManager;  
    private ListView commentsList;  
    private ImageButton tabBack;
    private SimpleAdapter commentsAdapter;    
    private final int GET_COMMENTS_ERROR=0;  
    private final int GET_COMMENTS_SUCCESS=1;  
    private final int POST_COMMENT_ERROR=2;  
    private final int POST_COMMENT_SUCCESS=3;  
      
      
    private Handler mHandler = new Handler(){  
        @Override  
        public void handleMessage(Message msg)  
        {  
              
            switch (msg.arg1)  
            {  
                case GET_COMMENTS_ERROR:  
                    Toast.makeText(CommentsActivity.this, "评论获取失败", Toast.LENGTH_SHORT).show();  
                    break;  
                case GET_COMMENTS_SUCCESS:  
                    commentsAdapter.notifyDataSetChanged();  
                    break;  
                case POST_COMMENT_ERROR:  
                    Toast.makeText(CommentsActivity.this, "发表失败", Toast.LENGTH_SHORT).show();  
                    break;  
                case POST_COMMENT_SUCCESS:  
                    Toast.makeText(CommentsActivity.this, "发表成功", Toast.LENGTH_SHORT).show();  
                    new GetCommentThread().start();  
                    break;  
                default:  
                    break;  
                  
            }  
        }  
    };  
      
    @Override  
    public void onCreate(Bundle savedInstanceState){  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.comments);  
        mNewsManager=new NewsManager();  
        initView();   
    }  
  
    public void initView(){  
        initBar();  
          
        // 查找新闻回复图片Layout  
        mNewsReplyImgLayout = (LinearLayout) findViewById(R.id.news_reply_img_layout);  
        // 查找新闻回复回复Layout  
       mNewsReplyEditLayout = (RelativeLayout) findViewById(R.id.news_reply_edit_layout);  
          
        Intent intent=getIntent();  
        mNid=intent.getIntExtra("nid", 0);//获取不到时默认值为0  
        mCommsData=new ArrayList<HashMap<String,Object>>();  
  
        new GetCommentThread().start();  
          
         commentsAdapter = new SimpleAdapter(this, mCommsData, R.layout.comments_list_item, new String[]  
        { "commentator_from", "comment_ptime", "comment_content" }, new int[]  
        { R.id.commentator_from, R.id.comment_ptime, R.id.comment_content });  
  
        commentsList = (ListView) findViewById(R.id.comments_list);  
        commentsList.setAdapter(commentsAdapter);  
          
        //切换回回复默认
        tabBack=(ImageButton) findViewById(R.id.tap_back);
        tabBack.setOnClickListener(this);  
        
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
        
        mNewsReplyImgBtn = (ImageButton) findViewById(R.id.news_reply_img_btn);  
        mNewsReplyImgBtn.setOnClickListener(this);  
          
          
        // 发表回复  
        Button newsReplyPost = (Button) findViewById(R.id.news_reply_post);  
        newsReplyPost.setOnClickListener(this);  
//        //取消发发表  
//        canclebtn=(Button)findViewById(R.id.news_reply_delete);  
//        canclebtn.setOnClickListener(commsOnClickListener);  
          
    }  
      
    public void initBar(){  
        View headView = LayoutInflater.from(this).inflate(R.layout.comment_actionbar, null);  
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
            new PostCommentThread().start();  
            mNewsReplyImgLayout.setVisibility(View.VISIBLE);  
            mNewsReplyEditLayout.setVisibility(View.GONE);  
            m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
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
            int retCode=mNewsManager.postComment(mNid, mRegion, mNewsReplyContent.getText().toString());  
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
  
      
    private class GetCommentThread extends Thread{  
        @Override  
        public void run()  
        {  
              
            // 从网络上获取评论  
            int retCode=mNewsManager.getComments(mNid, mCommsData);  
            Message msg = mHandler.obtainMessage();  
            if(retCode==0){               
                msg.arg1 = GET_COMMENTS_SUCCESS;  
                msg.obj = mCommsData;  
                  
            }  
            else{  
                msg.arg1=GET_COMMENTS_ERROR;  
            }  
            mHandler.sendMessage(msg);  
  
        }  
    }  
      
      
  
}  