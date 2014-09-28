package com.cqvip.zlfassist.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.adapter.NewsFragmentPagerAdapter;
import com.cqvip.zlfassist.bean.ChannelItem;
import com.cqvip.zlfassist.bean.ItemFollows;
import com.cqvip.zlfassist.constant.C;
import com.cqvip.zlfassist.db.DatabaseHelper;
import com.cqvip.zlfassist.fragment.ZKFollowListFragment;
import com.cqvip.zlfassist.fragment.ZKTopicListFragment;
import com.cqvip.zlfassist.scan.CaptureActivity;
import com.cqvip.zlfassist.tools.BaseTools;
import com.cqvip.zlfassist.view.ColumnHorizontalScrollView;
import com.cqvip.zlfassist.view.DrawerView;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public class MainActivity extends FragmentActivity {
	/** 自定义HorizontalScrollView */
	private ColumnHorizontalScrollView mColumnHorizontalScrollView;
	LinearLayout mRadioGroup_content;
	LinearLayout ll_more_columns;
	RelativeLayout rl_column;
	private ViewPager mViewPager;
	private ImageView button_more_columns;
	private static final String[] WORDS={C.MEDIA,C.SUBJECT,C.WRITER,C.ORGAN,C.FUND,C.DOMAIN,C.AREA};
	/** 用户选择的新闻分类列表 */
	private ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();
	/** 当前选中的栏目 */
	private int columnSelectIndex = 0;
	/** 左阴影部分 */
	public ImageView shade_left;
	/** 右阴影部分 */
	public ImageView shade_right;
	/** 屏幕宽度 */
	private int mScreenWidth = 0;
	/** Item宽度 */
	private int mItemWidth = 0;
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

	protected SlidingMenu side_drawer;

	/** head 头部 的中间的loading */
	private ProgressBar top_progress;
	/** head 头部 中间的刷新按钮 */
	private ImageView top_refresh;
	/** head 头部 的左侧菜单 按钮 */
	private ImageView top_head;
	/** head 头部 的右侧菜单 按钮 */
	private ImageView top_right;
	/** 请求CODE */
	// public final static int CHANNELREQUEST = 1;
	/** 调整返回的RESULTCODE */
	// public final static int CHANNELRESULT = 10;

	private DatabaseHelper databaseHelper = null;
	private String[] alltype = { C.MEDIA, C.SUBJECT, C.WRITER, C.ORGAN, C.FUND,
			C.DOMAIN, C.AREA };
	private Map<String, ArrayList<ItemFollows>> allFollowMap_TopItem = new HashMap<>();// 上面菜单显示的
	private Map<String, String> keyvalue = new HashMap<>();// 菜单键值对

	// private ArrayList<String> topItemType_List;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mScreenWidth = BaseTools.getWindowsWidth(this);
		mItemWidth = mScreenWidth / 7;// 一个Item宽度为屏幕的1/7
		getdatafromdb();
		initkeyvalue();
		initView();
		initSlidingMenu();
	}

	private void initkeyvalue() {
		keyvalue.put(C.MEDIA, "期刊");
		keyvalue.put(C.SUBJECT, "主题");
		keyvalue.put(C.WRITER, "作者");
		keyvalue.put(C.ORGAN, "机构");
		keyvalue.put(C.FUND, "基金");
		keyvalue.put(C.DOMAIN, "学科");
		keyvalue.put(C.AREA, "地区");
	}

	/** 初始化layout控件 */
	private void initView() {
		mColumnHorizontalScrollView = (ColumnHorizontalScrollView) findViewById(R.id.mColumnHorizontalScrollView);
		mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
		ll_more_columns = (LinearLayout) findViewById(R.id.ll_more_columns);
		rl_column = (RelativeLayout) findViewById(R.id.rl_column);
		button_more_columns = (ImageView) findViewById(R.id.button_more_columns);
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
		shade_left = (ImageView) findViewById(R.id.shade_left);
		shade_right = (ImageView) findViewById(R.id.shade_right);
		top_head = (ImageView) findViewById(R.id.top_head);
		top_right = (ImageView) findViewById(R.id.top_right);
		top_refresh = (ImageView) findViewById(R.id.top_refresh);
		top_progress = (ProgressBar) findViewById(R.id.top_progress);
		button_more_columns.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Intent intent_channel = new Intent(getApplicationContext(),
				// ChannelActivity.class);
				// startActivityForResult(intent_channel, CHANNELREQUEST);
				// overridePendingTransition(R.anim.slide_in_right,
				// R.anim.slide_out_left);
			}
		});
		top_head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (side_drawer.isMenuShowing()) {
					side_drawer.showContent();
				} else {
					side_drawer.showMenu();
				}
			}
		});

		top_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						CaptureActivity.class);
				startActivity(intent);
			}
		});
		
		setChangelView();
	}

	/**
	 * 当栏目项发生变化时候调用
	 * */
	private void setChangelView() {
		initColumnData();
		initTabColumn();
		initFragment();
	}

	/** 获取Column栏目 数据 */
	private void initColumnData() {
		userChannelList = getUserItems();
	}

	private ArrayList<ChannelItem> getUserItems() {
		ArrayList<ChannelItem> lists = new ArrayList<ChannelItem>();
		//lists.add(new ChannelItem("", "文章", 1, 1));
		for (String item : allFollowMap_TopItem.keySet()) {
			lists.add(new ChannelItem(item, keyvalue.get(item), 1, 1));
		}

//		lists.add(new ChannelItem(2, "期刊", 2, 1));
//		lists.add(new ChannelItem(3, "主题", 3, 1));
//		lists.add(new ChannelItem(4, "作者", 4, 1));
//		lists.add(new ChannelItem(5, "机构", 5, 1));
//		lists.add(new ChannelItem(6, "基金", 6, 1));
//		lists.add(new ChannelItem(7, "学科", 7, 1));
//		lists.add(new ChannelItem(8, "地区", 1, 0));
		return lists;
	}

	/**
	 * 初始化Column栏目项
	 * */
	private void initTabColumn() {
		mRadioGroup_content.removeAllViews();
		int count = userChannelList.size()+1;
		mColumnHorizontalScrollView.setParam(this, mScreenWidth,
				mRadioGroup_content, shade_left, shade_right, ll_more_columns,
				rl_column);
		for (int i =0; i < count; i++) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					mItemWidth, LayoutParams.WRAP_CONTENT);
			params.leftMargin = 5;
			params.rightMargin = 5;
			// TextView localTextView = (TextView)
			// mInflater.inflate(R.layout.column_radio_item, null);
			TextView columnTextView = new TextView(this);
			columnTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);
//			localTextView.setBackground(getResources().getDrawable(R.drawable.top_category_scroll_text_view_bg));
			columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
			columnTextView.setGravity(Gravity.CENTER);
			columnTextView.setPadding(5, 5, 5, 5);
			if(i==0){
			columnTextView.setId(i);
			columnTextView.setText("文章");
			}else{
				columnTextView.setId(i);
				columnTextView.setText(userChannelList.get(i-1).getName());
			}
			columnTextView.setTextColor(getResources().getColorStateList(
					R.color.top_category_scroll_text_color_day));
			if (columnSelectIndex == i) {
				columnTextView.setSelected(true);
			}
			columnTextView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
						View localView = mRadioGroup_content.getChildAt(i);
						if (localView != v)
							localView.setSelected(false);
						else {
							localView.setSelected(true);
							mViewPager.setCurrentItem(i);
						}
					}
//					Toast.makeText(getApplicationContext(),
//							userChannelList.get(v.getId()).getName(),
//							Toast.LENGTH_SHORT).show();
				}
			});
			mRadioGroup_content.addView(columnTextView, i, params);
		}
	}
	/** 
	 *  选择的Column里面的Tab
	 * */
	private void selectTab(int tab_postion) {
		columnSelectIndex = tab_postion;
		for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
			View checkView = mRadioGroup_content.getChildAt(tab_postion);
			int k = checkView.getMeasuredWidth();
			int l = checkView.getLeft();
			int i2 = l + k / 2 - mScreenWidth / 2;
			// rg_nav_content.getParent()).smoothScrollTo(i2, 0);
			mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
			// mColumnHorizontalScrollView.smoothScrollTo((position - 2) *
			// mItemWidth , 0);
		}
		// 判断是否选中
		for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
			View checkView = mRadioGroup_content.getChildAt(j);
			boolean ischeck;
			if (j == tab_postion) {
				ischeck = true;
			} else {
				ischeck = false;
			}
			checkView.setSelected(ischeck);
		}
	}

	/**
	 * 初始化Fragment
	 * */
	private void initFragment() {
		fragments.clear();//清空
		Bundle txtdata = new Bundle();
//		txtdata.putString("text", "文章");
//		txtdata.putInt("id", 1);
		ZKTopicListFragment zktopicfragment = new ZKTopicListFragment();
	//	zktopicfragment.setArguments(txtdata);
		fragments.add(zktopicfragment);
		
		int count = userChannelList.size();
		for (int i = 0; i < count; i++) {
			Bundle data = new Bundle();
    		data.putSerializable("itemfollows_list", allFollowMap_TopItem.get(userChannelList.get(i).getType()));
    		ZKFollowListFragment followsFragment = new ZKFollowListFragment();
    		//PeriodicalListFragment newfragment = new PeriodicalListFragment();
		    followsFragment.setArguments(data);
			fragments.add(followsFragment);
		}
		NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
		//mViewPager.setOffscreenPageLimit(0);
		mViewPager.setAdapter(mAdapetr);
		mViewPager.setOnPageChangeListener(pageListener);
	}

	/**
	 * ViewPager切换监听方法
	 * */
	public OnPageChangeListener pageListener = new OnPageChangeListener() {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			mViewPager.setCurrentItem(position);
			selectTab(position);
		}
	};

	protected void initSlidingMenu() {
		side_drawer = new DrawerView(MainActivity.this).initSlidingMenu();
	}
	
	private long mExitTime;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(side_drawer.isMenuShowing() ||side_drawer.isSecondaryMenuShowing()){
				side_drawer.showContent();
			}else {
				if ((System.currentTimeMillis() - mExitTime) > 2000) {
					Toast.makeText(this, "在按一次退出",
							Toast.LENGTH_SHORT).show();
					mExitTime = System.currentTimeMillis();
				} else {
					finish();
				}
			}
			return true;
		}
		//拦截MENU按钮点击事件，让他无任何操作
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case DrawerView.RESULT_FOLLOW:
			// if (resultCode == CHANNELRESULT) {
			Log.i("onActivityResult", "onActivityResult");
			getdatafromdb();
			setChangelView();
			// }
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private DatabaseHelper getHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(this,
					DatabaseHelper.class);
		}
		return databaseHelper;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}

	private void getdatafromdb() {
		try {
			Dao<ItemFollows, Integer> itemFollowsDao = getHelper()
					.getItemFollowsDao();
			allFollowMap_TopItem.clear();
			for (String str : alltype) {
				ArrayList<ItemFollows> temp = (ArrayList<ItemFollows>) itemFollowsDao.queryForEq("type", str);
				if (temp.size() > 0) {
					allFollowMap_TopItem.put(str, temp);
				}
			}
			Log.i("getdatafromdb", allFollowMap_TopItem.size()+"");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
