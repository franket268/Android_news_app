package com.example.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.example.adapter.CatePagerAdapter;
import com.example.adapter.CustomSimpleAdapter;
import com.example.custom.Category;
import com.example.fragment.FocusFragment;
import com.example.news.R;
import com.example.util.StringUtil;


public class MainActivity extends SherlockFragmentActivity {
	private ViewPager viewPager;
	private CatePagerAdapter pagerAdapter;
	private final List<HashMap<String, Category>> categories = new ArrayList<HashMap<String, Category>>();
	private int mCid;
	private String mCatName;
	private Button mTitlebarRefresh;
	private ProgressBar mLoadnewsProgress;
	private Class<?>[] mClazz = {FocusFragment.class};
	private List<Fragment> fragments=new ArrayList<Fragment>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		initView();
	}
	public void initView(){
		initActionBar();
		viewPager=(ViewPager) findViewById(R.id.main_viewPager); 
		mTitlebarRefresh = (Button)findViewById(R.id.titlebar_refresh);
		mLoadnewsProgress = (ProgressBar)findViewById(R.id.loadnews_progress);
	
	
		
		
		//获取新闻分类
		String[] categoryArray = getResources().getStringArray(R.array.categories);
		
		//分割新闻类型字符串
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
		
		//默认选中的新闻分类
		mCid = 1;
		mCatName ="国内";
		//创建Adapter，指明映射字段
		CustomSimpleAdapter categoryAdapter = new CustomSimpleAdapter(this, categories, R.layout.category_title, new String[]{"category_title"}, new int[]{R.id.category_title});
		
		GridView category = new GridView(this);
		category.setColumnWidth(90);//每个单元格宽度
		category.setNumColumns(GridView.AUTO_FIT);//单元格数目
		category.setGravity(Gravity.CENTER);//设置对其方式
		//设置单元格选择是背景色为透明，这样选择时就不现实黄色背景
		category.setSelector(new ColorDrawable(Color.TRANSPARENT));
		//根据单元格宽度和数目计算总宽度
		//int width = mColumnWidthDip * categories.size();
		LayoutParams params = new LayoutParams(590, LayoutParams.FILL_PARENT);
		//更新category宽度和高度，这样category在一行显示
		category.setLayoutParams(params);
		//设置适配器
		category.setAdapter(categoryAdapter);
		//把category加入到容器中
		LinearLayout categoryList = (LinearLayout) findViewById(R.id.category_layout);
		categoryList.addView(category);
		//添加单元格点击事件
		category.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				TextView categoryTitle;
				//恢复每个单元格背景色
				for (int i = 0; i < parent.getChildCount(); i++)
				{
					categoryTitle = (TextView) (parent.getChildAt(i));
					categoryTitle.setBackgroundDrawable(null);
					categoryTitle.setTextColor(0XFFADB2AD);
				}
				//设置选择单元格的背景色
				categoryTitle = (TextView) (parent.getChildAt(position));
				categoryTitle.setBackgroundResource(R.drawable.categorybar_item_background);
				categoryTitle.setTextColor(0XFFFFFFFF);
				//获取选中的新闻分类id
				mCid = categories.get(position).get("category_title").getCid();
				mCatName = categories.get(position).get("category_title").getTitle();
							
				viewPager.setCurrentItem(position);
				
				//获取该栏目下新闻
				//getSpeCateNews(mCid,mNewsData,0,true);
				//通知ListView进行更新
				//mNewsListAdapter.notifyDataSetChanged();
				

			}
		});
		
		
		for(int i=0;i<mClazz.length;i++){
			try {
				Fragment fragment = (Fragment) mClazz[i].newInstance();
				fragments.add(fragment);
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
			}
            
		}
		
		pagerAdapter=new CatePagerAdapter(getSupportFragmentManager(),fragments);
		viewPager.setAdapter(pagerAdapter);
		
		
		
	}
	
	private void initActionBar() {
		View headView = LayoutInflater.from(this).inflate(R.layout.main_action_bar, null);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setCustomView(headView);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bg));
	}
	
	
}