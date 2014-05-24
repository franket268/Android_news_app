package com.example.news;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.custom.Category;
import com.example.custom.CustomSimpleAdapter;
import com.example.custom.ListHeaderView;
import com.example.service.SyncHttp;
import com.example.service.UpdateManager;
import com.example.util.DensityUtil;
import com.example.util.StringUtil;



import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class MainActivity extends Activity {
	
		private final int COLUMNWIDTHPX = 55;
		private final int FLINGVELOCITYPX = 800; // ��������
		private final int NEWSCOUNT = 5; //����������Ŀ
		private final int SUCCESS = 0;//���سɹ�
		private final int NONEWS = 1;//����Ŀ��û������
		private final int NOMORENEWS = 2;//����Ŀ��û�и�������
		private final int LOADERROR = 3;//����ʧ��
		
		private int mColumnWidthDip;
		private int mFlingVelocityDip;
		private int mCid;
		private String mCatName;
		private ArrayList<HashMap<String, Object>> mNewsData;
		private ListHeaderView headerView;
		private ImageView imageview;
		private Drawable drawable;  //����������ȡ�ĵĵ�һ�����ŵ�ͼƬ
		private ListView mNewsList;
		private SimpleAdapter mNewsListAdapter;
		private LayoutInflater mInflater;
		private Button mTitlebarRefresh;
		private ProgressBar mLoadnewsProgress;
		private Button mLoadMoreBtn;
		private Button titlebar_search;//������ť
		private Resources resource;
	
		
		private LoadNewsAsyncTask loadNewsAsyncTask;

		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			mInflater = getLayoutInflater();
			mNewsData = new ArrayList<HashMap<String,Object>>();
			
			mNewsList = (ListView)findViewById(R.id.newslist);
			mTitlebarRefresh = (Button)findViewById(R.id.titlebar_refresh);
			mLoadnewsProgress = (ProgressBar)findViewById(R.id.loadnews_progress);
			mTitlebarRefresh.setOnClickListener(loadMoreListener);
			titlebar_search=(Button)findViewById(R.id.titlebar_search);
			titlebar_search.setOnClickListener(loadMoreListener);
			
			
			
			//��pxת����dip
			mColumnWidthDip = DensityUtil.px2dip(this, COLUMNWIDTHPX);
			mFlingVelocityDip = DensityUtil.px2dip(this, FLINGVELOCITYPX);
			
			//��ȡ���ŷ���
			String[] categoryArray = getResources().getStringArray(R.array.categories);
			//�����ŷ��ౣ�浽List��
			final List<HashMap<String, Category>> categories = new ArrayList<HashMap<String, Category>>();
			//�ָ����������ַ���
			for(int i=0;i<categoryArray.length;i++)
			{
				String[] temp = categoryArray[i].split("[|]");
				if (temp.length==2)
				{
					int cid = StringUtil.String2Int(temp[0]);
					String title = temp[1];
					Category type = new Category(cid, title);
					HashMap<String, Category> hashMap = new HashMap<String, Category>();
					hashMap.put("category_title", type);
					categories.add(hashMap);
				}
			}
			//Ĭ��ѡ�е����ŷ���
			mCid = 1;
			mCatName ="����";
			//����Adapter��ָ��ӳ���ֶ�
			CustomSimpleAdapter categoryAdapter = new CustomSimpleAdapter(this, categories, R.layout.category_title, new String[]{"category_title"}, new int[]{R.id.category_title});
			
			GridView category = new GridView(this);
			category.setColumnWidth(57);//ÿ����Ԫ����
			category.setNumColumns(GridView.AUTO_FIT);//��Ԫ����Ŀ
			category.setGravity(Gravity.CENTER);//���ö��䷽ʽ
			//���õ�Ԫ��ѡ���Ǳ���ɫΪ͸��������ѡ��ʱ�Ͳ���ʵ��ɫ����
			category.setSelector(new ColorDrawable(Color.TRANSPARENT));
			//���ݵ�Ԫ���Ⱥ���Ŀ�����ܿ��
			//int width = mColumnWidthDip * categories.size();
			LayoutParams params = new LayoutParams(590, LayoutParams.FILL_PARENT);
			//����category��Ⱥ͸߶ȣ�����category��һ����ʾ
			category.setLayoutParams(params);
			//����������
			category.setAdapter(categoryAdapter);
			//��category���뵽������
			LinearLayout categoryList = (LinearLayout) findViewById(R.id.category_layout);
			categoryList.addView(category);
			//��ӵ�Ԫ�����¼�
			category.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id)
				{
					TextView categoryTitle;
					//�ָ�ÿ����Ԫ�񱳾�ɫ
					for (int i = 0; i < parent.getChildCount(); i++)
					{
						categoryTitle = (TextView) (parent.getChildAt(i));
						categoryTitle.setBackgroundDrawable(null);
						categoryTitle.setTextColor(0XFFADB2AD);
					}
					//����ѡ��Ԫ��ı���ɫ
					categoryTitle = (TextView) (parent.getChildAt(position));
					categoryTitle.setBackgroundResource(R.drawable.categorybar_item_background);
					categoryTitle.setTextColor(0XFFFFFFFF);
					//��ȡѡ�е����ŷ���id
					mCid = categories.get(position).get("category_title").getCid();
					mCatName = categories.get(position).get("category_title").getTitle();
					//��ȡ����Ŀ������
					//getSpeCateNews(mCid,mNewsData,0,true);
					//֪ͨListView���и���
					//mNewsListAdapter.notifyDataSetChanged();
					
					
					loadNewsAsyncTask = new LoadNewsAsyncTask();
					loadNewsAsyncTask.execute(mCid,0,true);
				}
			});
			
			// ��ͷ
			final HorizontalScrollView categoryScrollview = (HorizontalScrollView) findViewById(R.id.category_scrollview);
			Button categoryArrowRight = (Button) findViewById(R.id.category_arrow_right);
			categoryArrowRight.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					categoryScrollview.fling(DensityUtil.px2dip(MainActivity.this, mFlingVelocityDip));
				}
			});
			
			
			
			
			//��ȡָ����Ŀ�������б�
			//getSpeCateNews(mCid,mNewsData,0,true);
			mNewsListAdapter = new SimpleAdapter(this, mNewsData, R.layout.newslist_item, 
											new String[]{"newslist_item_title","newslist_item_digest","newslist_item_source","newslist_item_ptime"}, 
											new int[]{R.id.newslist_item_title,R.id.newslist_item_digest,R.id.newslist_item_source,R.id.newslist_item_ptime});
			View loadMoreLayout = mInflater.inflate(R.layout.loadmore, null);
		//	View firstImage = mInflater.inflate(R.layout.first_image, null);
		//	imageview=(ImageView) firstImage.findViewById(R.id.firstImage);
		//	imageview.setImageDrawable(drawable);
		    headerView=new ListHeaderView(this);
		//	headView.setImage();
	    	mNewsList.addHeaderView(headerView);
			mNewsList.addFooterView(loadMoreLayout);
			mNewsList.setAdapter(mNewsListAdapter); 
			mNewsList.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id)
				{
					Intent intent = new Intent(MainActivity.this, NewsDetailsActivity.class);
					//����Ҫ����Ϣ�ŵ�Intent��
					intent.putExtra("newsData", mNewsData);//���������������ͷ����ȥ
					intent.putExtra("position", position);
					intent.putExtra("categoryName", mCatName);
					startActivity(intent);
				}
			});
			
			
				
			
			mLoadMoreBtn = (Button)findViewById(R.id.loadmore_btn);
			mLoadMoreBtn.setOnClickListener(loadMoreListener);
			
			//��һ��Ĭ�ϵļ�������
			loadNewsAsyncTask = new LoadNewsAsyncTask();
			loadNewsAsyncTask.execute(mCid,0,true);
			
		}
		
		
		/**
		 * ���Ӳ˵�
		 */
		@Override
		public boolean onCreateOptionsMenu(Menu menu)
		{
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.menu_item, menu);
			return true;
		}
		
		/**
		 * ����˵��¼�
		 */
		@Override
		public boolean onOptionsItemSelected(MenuItem item)
		{
			if (item.getItemId()==R.id.menu_item_update)
			{
				UpdateManager manager = new UpdateManager(MainActivity.this);
				// ����������
				manager.checkUpdate();
			}
			return true;
		}
		
		
		
		
		/**
		 * ��ȡָ�����͵������б�
		 * @param cid ����ID
		 * @param newsList ����������Ϣ�ļ���
		 * @param startnid ��ҳ
		 * @param firstTimes	�Ƿ��һ�μ���
		 * @param drawble ������ҳͼƬ
		 */
		private int getSpeCateNews(int cid,List<HashMap<String, Object>> newsList,int startnid,Boolean firstTimes,ListHeaderView headerView)
		{
			
			int flat=0;
			if (firstTimes)
			{
				//����ǵ�һ�Σ�����ռ���������
				newsList.clear();
				flat=1;
			}
			//����URL���ַ���
			String url = "http://54.186.248.222:8080/web/getSpecifyCategoryNews";
			String params = "startnid="+startnid+"&count="+NEWSCOUNT+"&cid="+cid;
			SyncHttp syncHttp = new SyncHttp();
			resource = getBaseContext().getResources();
			
			try
			{
				//��Get��ʽ���󣬲���÷��ؽ��
				String retStr = syncHttp.httpGet(url, params);
				JSONObject jsonObject = new JSONObject(retStr);
				//��ȡ�����룬0��ʾ�ɹ�
				int retCode = jsonObject.getInt("ret");
				Log.i("t1","0");
				drawable = resource.getDrawable(R.drawable.no_pic);
				headerView.setDrawable(drawable);
				if (0==retCode)
				{  
					Log.i("t1","1");
					JSONObject dataObject = jsonObject.getJSONObject("data");
					//��ȡ������Ŀ
					int totalnum = dataObject.getInt("totalnum");
					if (totalnum>0)
					{
						Log.i("t1","2");
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
							if(flat==1){
								String imgsrc=newsObject.getString("imgsrc");
								Log.i("t1",imgsrc);
								if(!imgsrc.equals("null")){
									Log.i("t1","3");
									drawable = Drawable.createFromStream(new URL(imgsrc).openStream(), "image");
									headerView.setDrawable(drawable);
								}
								else{
									Log.i("t1","4");
//									String img_url="http://54.186.248.222:8080/web/img/noPic.jpg";
//									drawable = Drawable.createFromStream(new URL(img_url).openStream(), "image");
									
								}
								headerView.setDrawable(drawable);
								flat=0;
							}
							
						}
						
						return SUCCESS;
					}
					else
					{
						Log.i("t1","5");
		//				drawable = resource.getDrawable(R.drawable.no_pic);
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
					Log.i("t1","6");
		//			drawable = resource.getDrawable(R.drawable.no_pic);
					return LOADERROR;
				}
			} catch (Exception e)
			{
				e.printStackTrace();
				return NOMORENEWS;
			}
		
		}
		
		private OnClickListener loadMoreListener = new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				loadNewsAsyncTask = new LoadNewsAsyncTask();
				switch (v.getId())
				{
				case R.id.loadmore_btn:
					//��ȡ����Ŀ������
					//getSpeCateNews(mCid,mNewsData,mNewsData.size(),false);
					//֪ͨListView���и���
					//mNewsListAdapter.notifyDataSetChanged();
					loadNewsAsyncTask.execute(mCid,mNewsData.size(),false);
					break;
				case R.id.titlebar_refresh:
					loadNewsAsyncTask.execute(mCid,0,true);
					break;
				case R.id.titlebar_search:
					Intent intent =new Intent(MainActivity.this,SearchActivity.class);
					startActivity(intent);
					
				}
				
			}
		};
		
		private class LoadNewsAsyncTask extends AsyncTask<Object, Integer, Integer>
		{
			
			@Override
			protected void onPreExecute()
			{
				//����ˢ�°�ť
				mTitlebarRefresh.setVisibility(View.GONE);
				//��ʾ������
				mLoadnewsProgress.setVisibility(View.VISIBLE); 
				//���ڼ���ʱ��ʾ��Ϣ
				mLoadMoreBtn.setText("���ڼ��أ����Ժ󡤡���");
			}

			@Override
			//Object... params����˵������������Object���͵Ĳ�����Ҳ����˵���Դ���0�����Object�������Ķ������������
			protected Integer doInBackground(Object... params)
			{
				return getSpeCateNews((Integer)params[0],mNewsData,(Integer)params[1],(Boolean)params[2],headerView);
			}

			@Override
			
			//result�Ƿ���doInBackground(Object... params)���ص�ֵ
			protected void onPostExecute(Integer result)
			{
				//���ݷ���ֵ��ʾ��ص�Toast
				switch (result)
				{
				case NONEWS:
					Toast.makeText(MainActivity.this,"û������", Toast.LENGTH_LONG).show();
				break;
				case NOMORENEWS:
					Toast.makeText(MainActivity.this, "û�и�������", Toast.LENGTH_LONG).show();
					break;
				case LOADERROR:
					Toast.makeText(MainActivity.this, "���ż���ʧ��", Toast.LENGTH_LONG).show();
					break;
				}

				//֪ͨListView���и���
				
				mNewsListAdapter.notifyDataSetChanged(); 
				headerView.setImage(headerView.getDrawable());
				
				
				//��ʾˢ�°�ť
				mTitlebarRefresh.setVisibility(View.VISIBLE);
				//���ؽ�����
				mLoadnewsProgress.setVisibility(View.GONE); 
				//����LoadMore Button ��ʾ�ı�
				mLoadMoreBtn.setText("���ظ���");
			}
		}
	}
