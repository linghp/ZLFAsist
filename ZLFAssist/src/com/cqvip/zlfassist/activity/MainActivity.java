package com.cqvip.zlfassist.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends FragmentActivity {
	/** 自定义HorizontalScrollView */
	private final String LOG_TAG = getClass().getSimpleName();
	private ColumnHorizontalScrollView mColumnHorizontalScrollView;
	LinearLayout mRadioGroup_content;
	LinearLayout ll_more_columns;
	RelativeLayout rl_column;
	private ViewPager mViewPager;
	private ImageView button_more_columns;
	private static final String[] WORDS = { C.MEDIA, C.SUBJECT, C.WRITER,
			C.ORGAN, C.FUND, C.DOMAIN, C.AREA };
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
	public final static int CHANNELREQUEST = 2;
	/** 调整返回的RESULTCODE */
	// public final static int CHANNELRESULT = 10;

	private DatabaseHelper databaseHelper = null;
	private String[] alltype = { C.MEDIA, C.SUBJECT, C.WRITER, C.ORGAN, C.FUND,
			C.DOMAIN, C.AREA };
	private Map<String, ArrayList<ItemFollows>> allFollowMap_TopItem = new LinkedHashMap<>();// 上面菜单显示的
	private Map<String, String> keyvalue = new HashMap<>();// 菜单键值对
	private Map<String, ArrayList<ItemFollows>> alltypecontent_map = new HashMap<>();// 保存各个类别内容的引用
	private Set<String> keys;// 保存上面显示的菜单顺序

	private static boolean isFirstCreate = true;

	private DrawerView drawerView;
	// private ArrayList<String> topItemType_List;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(LOG_TAG, "onCreate");
		myStartHelpActivity();
		setContentView(R.layout.main);
		mScreenWidth = BaseTools.getWindowsWidth(this);
		mItemWidth = mScreenWidth / 7;// 一个Item宽度为屏幕的1/7
		initkeyvalue();
		getdatafromdb();
		initView();
		initSlidingMenu();

		umeng();
	}

	private void myStartHelpActivity() {
		// 读取SharedPreferences中需要的数据
		SharedPreferences preferences = getSharedPreferences("count",
				MODE_PRIVATE);
		int count = preferences.getInt("count", 0);
		// 判断程序与第几次运行，如果是第一次运行则跳转到引导页面
		if (count == 0) {
			Editor editor = preferences.edit();
			// 存入数据
			editor.putInt("count", ++count);
			// 提交修改
			editor.commit();
			Intent intent = new Intent(this, HelperActivity.class);
			startActivity(intent);
		}
	}

	private void umeng() {
		// 更新
		if (isFirstCreate) {
			UmengUpdateAgent.setUpdateCheckConfig(false);
			UmengUpdateAgent.update(this);
			isFirstCreate = false;
		}
		// 统计
		// MobclickAgent.setDebugMode(true);//打开调试模式（debug）后，数据实时发送，不受发送策略控制
		MobclickAgent.updateOnlineConfig(this);// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。在没有取到在线配置的发送策略的情况下，会使用默认的发送策略：启动时发送。
												// 你可以在友盟后台“设置->发送策略”页面自定义数据发送的频率。
	}

	// @Override
	// protected void onStart() {
	// Log.i(LOG_TAG, "onStart");
	// super.onStart();
	// }
	//
	// @Override
	// protected void onRestart() {
	// Log.i(LOG_TAG, "onRestart");
	// super.onRestart();
	// }
	//
	// @Override
	// protected void onResume() {
	// Log.i(LOG_TAG, "onResume");
	// super.onResume();
	// }

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
				startActivityForResult(new Intent(MainActivity.this,
						DisplayFollowActivity.class), DrawerView.RESULT_FOLLOW);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
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

		button_more_columns
				.setOnLongClickListener(new View.OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						Log.i("setOnLongClickListener",
								"setOnLongClickListener");
						Intent intent = new Intent(MainActivity.this,
								ChannelActivity.class);
						startActivityForResult(intent, CHANNELREQUEST);
						return true;
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
		// lists.add(new ChannelItem("", "文章", 1, 1));
		if (keys != null)
			for (String item : keys) {
				lists.add(new ChannelItem(item, keyvalue.get(item), 1, 1));
			}

		// lists.add(new ChannelItem(2, "期刊", 2, 1));
		// lists.add(new ChannelItem(3, "主题", 3, 1));
		// lists.add(new ChannelItem(4, "作者", 4, 1));
		// lists.add(new ChannelItem(5, "机构", 5, 1));
		// lists.add(new ChannelItem(6, "基金", 6, 1));
		// lists.add(new ChannelItem(7, "学科", 7, 1));
		// lists.add(new ChannelItem(8, "地区", 1, 0));
		return lists;
	}

	/**
	 * 初始化Column栏目项
	 * */
	private void initTabColumn() {
		mRadioGroup_content.removeAllViews();
		int count = userChannelList.size() + 1;
		mColumnHorizontalScrollView.setParam(this, mScreenWidth,
				mRadioGroup_content, shade_left, shade_right, ll_more_columns,
				rl_column);
		for (int i = 0; i < count; i++) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					mItemWidth, LayoutParams.WRAP_CONTENT);
			params.leftMargin = 5;
			params.rightMargin = 5;
			// TextView localTextView = (TextView)
			// mInflater.inflate(R.layout.column_radio_item, null);
			TextView columnTextView = new TextView(this);
			columnTextView.setTextAppearance(this,
					R.style.top_category_scroll_view_item_text);
			// localTextView.setBackground(getResources().getDrawable(R.drawable.top_category_scroll_text_view_bg));
			columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
			columnTextView.setGravity(Gravity.CENTER);
			columnTextView.setPadding(5, 5, 5, 5);
			if (i == 0) {
				columnTextView.setId(i);
				columnTextView.setText("文章");
			} else {
				columnTextView.setId(i);
				columnTextView.setText(userChannelList.get(i - 1).getName());
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
					// Toast.makeText(getApplicationContext(),
					// userChannelList.get(v.getId()).getName(),
					// Toast.LENGTH_SHORT).show();
				}
			});
			columnTextView
					.setOnLongClickListener(new View.OnLongClickListener() {

						@Override
						public boolean onLongClick(View v) {
							Log.i("setOnLongClickListener",
									"setOnLongClickListener");
							Intent intent = new Intent(MainActivity.this,
									ChannelActivity.class);
							startActivityForResult(intent, CHANNELREQUEST);
							return true;
						}
					});
			mRadioGroup_content.addView(columnTextView, i, params);
		}
	}

	/**
	 * 选择的Column里面的Tab
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
		fragments.clear();// 清空
		Bundle txtdata = new Bundle();
		// txtdata.putString("text", "文章");
		// txtdata.putInt("id", 1);
		ZKTopicListFragment zktopicfragment = new ZKTopicListFragment();
		// zktopicfragment.setArguments(txtdata);
		fragments.add(zktopicfragment);

		int count = userChannelList.size();
		for (int i = 0; i < count; i++) {
			Bundle data = new Bundle();
			// data.putSerializable("itemfollows_list",
			// allFollowMap_TopItem.get(userChannelList.get(i).getType()));
			data.putString("type", userChannelList.get(i).getType());
			Log.i("MainActivity_initFragment",
					userChannelList.get(i).getType()
							+ "--"
							+ allFollowMap_TopItem.get(
									userChannelList.get(i).getType()).size());
			ZKFollowListFragment followsFragment = new ZKFollowListFragment(
					allFollowMap_TopItem.get(userChannelList.get(i).getType()));
			// PeriodicalListFragment newfragment = new
			// PeriodicalListFragment();
			followsFragment.setArguments(data);
			fragments.add(followsFragment);
		}
		NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(
				getSupportFragmentManager(), fragments);
		mViewPager.setOffscreenPageLimit(7);
		mViewPager.setAdapter(mAdapetr);
		mViewPager.setCurrentItem(columnSelectIndex);
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
			if (side_drawer.isMenuShowing()
					|| side_drawer.isSecondaryMenuShowing()) {
				side_drawer.showContent();
			} else {
				if ((System.currentTimeMillis() - mExitTime) > 2000) {
					Toast.makeText(this, "在按一次退出", Toast.LENGTH_SHORT).show();
					mExitTime = System.currentTimeMillis();
				} else {
					// finish();
					MobclickAgent.onKillProcess(this);

					int pid = android.os.Process.myPid();
					android.os.Process.killProcess(pid);
				}
			}
			return true;
		}
		// 拦截MENU按钮点击事件，让他无任何操作
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
		case CHANNELREQUEST:// 现在用startactivity来重新启动此activity
			// getdatafromdb();
			// setChangelView();
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

	/**
	 * 获取关注后的类别和每个类别下的内容
	 */
	private void getdatafromdb() {
		List<ChannelItem> channelItems = new ArrayList<>();
		try {
			Dao<ItemFollows, Integer> itemFollowsDao = getHelper()
					.getItemFollowsDao();
			allFollowMap_TopItem.clear();
			// 获取type分组信息，按时间排序，确保关注分组顺序
			// http://stackoverflow.com/questions/12190786/ormlite-select-distinct-fields
			ArrayList<ItemFollows> temp1 = (ArrayList<ItemFollows>) itemFollowsDao
					.queryBuilder().distinct().selectColumns("type").query();

			for (ItemFollows itemFollows : temp1) {
				String type = itemFollows.getType();
				ChannelItem channelItem = new ChannelItem(type,
						keyvalue.get(type));
				channelItems.add(channelItem);
				ArrayList<ItemFollows> temp = (ArrayList<ItemFollows>) itemFollowsDao
						.queryForEq("type", type);
				if (alltypecontent_map.get(type) == null) {// 判断map是否为空，为空就赋值，不为空就清空赋值，保持引用不变,避免返回更新不了内容
					alltypecontent_map.put(type, temp);
				} else {
					ArrayList<ItemFollows> temp2 = alltypecontent_map.get(type);
					temp2.clear();
					temp2.addAll(temp);
				}
				Log.i("MainActivity_getdatafromdb", temp.size() + "--"
						+ itemFollows.getType());
				allFollowMap_TopItem.put(itemFollows.getType(),
						alltypecontent_map.get(type));
			}
			keys = allFollowMap_TopItem.keySet();
			Log.i("getdatafromdb", allFollowMap_TopItem.size() + "");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getdatafromdb_sort(channelItems);
	}

	/**
	 * 获取排序后保存的关注类别
	 */
	private void getdatafromdb_sort(List<ChannelItem> channelItems) {
		try {
			Dao<ChannelItem, Integer> channelSortDao = getHelper()
					.getChannelSortDao();
			// 获取type分组信息，按时间排序，确保关注分组顺序
			// http://stackoverflow.com/questions/12190786/ormlite-select-distinct-fields
			ArrayList<ChannelItem> temp1 = (ArrayList<ChannelItem>) channelSortDao
					.queryForAll();
			if (temp1.size() > 0) {
				Map<String, String> temp_map = new LinkedHashMap<>();
				ArrayList<ChannelItem> temp2 = new ArrayList<>();
				// 先清除（清除排序表中的没有的类别）
				for (ChannelItem channelItem_clear2 : temp1) {// 和总类别的表比较
					for (ChannelItem channelItem_clear : channelItems) {
						if (channelItem_clear2.equals(channelItem_clear)) {
							temp_map.put(channelItem_clear.getType(), "");
							temp2.add(channelItem_clear);
						}
					}
				}
				// 再添加
				for (ChannelItem channelItem_add : channelItems) {// 和总类别的表比较
					if (!temp2.contains(channelItem_add)) {
						temp2.add(channelItem_add);
						temp_map.put(channelItem_add.getType(), "");
					}
				}
				// for (ChannelItem channelItem : temp1) {
				// temp2.add(channelItem);
				// temp_map.put(channelItem.getType(), "");
				// if(true){
				// for (ChannelItem channelItem2 : channelItems) {//和总类别的表比较
				// if(!channelItem2.type.equals(channelItem.getType())){
				// temp_map.put(channelItem2.getType(), "");
				// temp2.add(channelItem2);
				// }
				// }

				channelSortDao.delete(temp1);
				for (ChannelItem channelItem3 : temp2) {
					Log.i("getdatafromdb_sort", channelItem3.toString());
					channelSortDao.create(channelItem3);
				}

				// }

				keys = temp_map.keySet();
			} else {// 初始化数据
				Dao<ChannelItem, Integer> channelSortDao2 = getHelper()
						.getChannelSortDao();
				ArrayList<ChannelItem> lists = getUserItems();
				if (lists.size() > 0) {
					for (ChannelItem channelItem : lists) {
						channelSortDao2.create(channelItem);
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
