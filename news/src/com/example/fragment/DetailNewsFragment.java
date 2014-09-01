package com.example.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.news.R;
import com.example.service.NewsManager;
import com.example.util.ImageUtil;

public class DetailNewsFragment extends Fragment{  
    private NewsManager mNewsManager;  
    private int mPosition;  
    private ArrayList<HashMap<String, Object>> mNewsdata;  
    private ImageView image;  
    private TextView newsText,newsTitle,newsPtimeAndSource;  
    private ProgressBar progressBar;  
  
    private Handler mHandler = new Handler(){  
        @Override  
        public void handleMessage(Message msg)  
        {  
              
            switch (msg.arg1)  
            {  
                case 1:  
                    ArrayList<HashMap<String,Object>> bodyList=(ArrayList<HashMap<String,Object>>)msg.obj;  
                    for (HashMap<String, Object> map : bodyList){  
                        if (map.get("type").equals("image")){  
                            if(!map.get("value").toString().equals("null"))  
                       
                                ImageUtil.LoadImage(map.get("value").toString(), image, getActivity());  
                                image.setVisibility(View.VISIBLE);  
                        }  
                        else {  
                            newsText.setText((map.get("value").toString()));  
                            newsText.setTextColor(Color.BLACK);  
                            newsText.setVisibility(View.VISIBLE);  
                        }  
                    }  
                    progressBar.setVisibility(View.GONE);  
                    break;  
            }  
        }  
    };  
  
      
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        mNewsManager=new NewsManager();  
  
    }  
      
      
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
        super.onCreateView(inflater, container, savedInstanceState);  
  
        return inflater.inflate(R.layout.news_body, null);  
    }  
      
      
    @Override  
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
        Bundle args = getArguments();  
        mPosition=(int)args.getInt("position");  
        mNewsdata=(ArrayList<HashMap<String, Object>>)args.getSerializable("list");  
          
        buildView();  
        new GetContentThread().start();  
    }  
      
    public void buildView(){  
        View contentView = getView();  
        if (null == contentView) {  
            return;  
        }  
        // 获取点击新闻基本信息  
        HashMap<String, Object> hashMap = mNewsdata.get(mPosition);  
        // 新闻标题  
          
        newsTitle = (TextView) contentView.findViewById(R.id.newsdetails_body_title);  
        newsTitle.setText((String) hashMap.get("newslist_item_title"));  
        // 发布时间和出处  
        newsPtimeAndSource = (TextView) contentView.findViewById(R.id.news_body_ptime_source);  
        newsPtimeAndSource.setText(hashMap.get("newslist_item_ptime").toString() + "    " + hashMap.get("newslist_item_source").toString());  
  
          
        progressBar=(ProgressBar) contentView.findViewById(R.id.progressBar);  
        image=(ImageView) contentView.findViewById(R.id.detail_news_image);  
        newsText=(TextView) contentView.findViewById(R.id.detail_news_text);  
          
          
          
          
    }  
      
    private class GetContentThread extends Thread  
    {  
        @Override  
        public void run()  
        {  
              
            // 从网络上获取新闻  
            ArrayList<HashMap<String,Object>> bodyList = mNewsManager.getNewsBody((int)mNewsdata.get(mPosition).get("nid"));  
            Message msg = mHandler.obtainMessage();  
            msg.arg1 = 1;  
            msg.obj = bodyList;  
            mHandler.sendMessage(msg);  
        }  
    }  
      
      
}  
