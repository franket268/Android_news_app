package com.example.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.example.adapter.CatePagerAdapter;
import com.example.adapter.CustomSimpleAdapter;
import com.example.custom.Category;
import com.example.fragment.DomesticFragment;
import com.example.fragment.FocusFragment;
import com.example.fragment.InternationFragment;
import com.example.fragment.LeftBottomFragment;
import com.example.fragment.MilitaryFragment;
import com.example.fragment.SportFragment;
import com.example.news.R;
import com.example.util.StringUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

public class MainActivity extends SlidingActivity {
	private ViewPager viewPager;
	private CatePagerAdapter pagerAdapter;
	private final List<HashMap<String, Category>> categories = new ArrayList<HashMap<String, Category>>();
	private Class<?>[] mClazz = {FocusFragment.class,DomesticFragment.class,InternationFragment.class,MilitaryFragment.class,SportFragment.class};
	private List<Fragment> fragments=new ArrayList<Fragment>();
	private ViewHolder holder;
	private GridView category;

		
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initView();
		//初始化滑动菜单
		initSlidingMenu(savedInstanceState);
	}
	
	
	public void initView(){
		initActionBar();
		viewPager=(ViewPager) findViewById(R.id.main_viewPager); 
	
		
		
		
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
		
		
		
		//创建Adapter，指明映射字段
		CustomSimpleAdapter categoryAdapter = new CustomSimpleAdapter(this, categories, R.layout.category_title, new String[]{"category_title"}, new int[]{R.id.category_title});
		
		category = new GridView(this);
		category.setColumnWidth(100);//每个单元格宽度
		category.setNumColumns(GridView.AUTO_FIT);//单元格数目
		category.setGravity(Gravity.CENTER);//设置对其方式
		//设置单元格选择是背景色为透明，这样选择时就不现实黄色背景
		category.setSelector(new ColorDrawable(Color.TRANSPARENT));
		//根据单元格宽度和数目计算总宽度
		//int width = mColumnWidthDip * categories.size();
		LayoutParams params = new LayoutParams(590, LayoutParams.WRAP_CONTENT);
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
					categoryTitle.setTextColor(0XFFADB2AD);
					categoryTitle.setBackgroundDrawable(null);
				}
				//设置选择单元格的背景色
				categoryTitle = (TextView) (parent.getChildAt(position));
				categoryTitle.setBackgroundResource(R.drawable.categorybar_item_background);
				categoryTitle.setTextColor(0XFFFFFFFF);
		
		
		
				viewPager.setCurrentItem(position);	
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
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				TextView categoryTitle;
				for (int i = 0; i < category.getChildCount(); i++)
				{
				categoryTitle = (TextView) (category.getChildAt(i));
				categoryTitle.setTextColor(0XFFADB2AD);
				categoryTitle.setBackgroundDrawable(null);
				}
				//设置选择单元格的背景色
				categoryTitle = (TextView) (category.getChildAt(position));
				categoryTitle.setBackgroundResource(R.drawable.categorybar_item_background);
				categoryTitle.setTextColor(0XFFFFFFFF);
				
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

		private void initActionBar() {
			View headView = LayoutInflater.from(this).inflate(R.layout.main_action_bar, null);
			holder=new ViewHolder();
			holder.titlebarRefresh = (Button)headView.findViewById(R.id.titlebar_refresh);
			holder.loadnewsProgress = (ProgressBar)headView.findViewById(R.id.loadnews_progress);
//			holder.searchButton=(Button) headView.findViewById(R.id.titlebar_search);
//			holder.searchButton.setOnClickListener(new OnClickListener() {			
//				@Override
//				public void onClick(View v) {
//					Intent intent=new Intent(MainActivity.this,SearchActivity.class);
//					startActivity(intent);
//					
//				}
//			});
			holder.menuButton=(ImageButton) headView.findViewById(R.id.menu_button);
			holder.menuButton.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View v) {
					toggle();//尽心SlidingMenu的打开与关闭		
				}
			});
			ActionBar actionBar = getSupportActionBar();
			actionBar.setCustomView(headView);
			actionBar.setDisplayShowCustomEnabled(true);
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bg));
		}
		
		private void initSlidingMenu(Bundle savedInstanceState) {
			// 设置滑动菜单的视图
			setBehindContentView(R.layout.menu_frame);
			getFragmentManager().beginTransaction().replace(R.id.menu_frame, new LeftBottomFragment()).commit();		
			// 实例化滑动菜单对象
			SlidingMenu sm = getSlidingMenu();
			// 设置滑动阴影的宽度
			sm.setShadowWidthRes(R.dimen.shadow_width);
			// 设置滑动阴影的图像资源
			sm.setShadowDrawable(R.drawable.shadow);
			// 设置滑动菜单视图的宽度
			sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
			// 设置渐入渐出效果的值
			sm.setFadeDegree(0.35f);
			// 设置触摸屏幕的模式
			sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);			
		}
		
		
		public class ViewHolder{
			public  Button titlebarRefresh;
			public ProgressBar loadnewsProgress;
			private ImageButton menuButton;
			private Button searchButton;
		}
		
		public ViewHolder getViewHolder(){
			return holder;
		}


}
