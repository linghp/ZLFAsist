package com.cqvip.zlfassist.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.adapter.ItemAdapter;
import com.cqvip.zlfassist.adapter.ItemFollowsAdapter;
import com.cqvip.zlfassist.base.BaseActionBarActivity;
import com.cqvip.zlfassist.bean.TopItem;
import com.cqvip.zlfassist.bean.ItemFollows;
import com.cqvip.zlfassist.constant.C;
import com.cqvip.zlfassist.db.DatabaseHelper;
import com.cqvip.zlfassist.freshlistview.FreshListView;
import com.cqvip.zlfassist.freshlistview.FreshListView.OnStartListener;
import com.cqvip.zlfassist.http.VolleyManager;
import com.cqvip.zlfassist.tools.ViewSetting;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

public class AddFollowActivity extends BaseActionBarActivity implements
		OnItemClickListener, OnClickListener {
	private final String LOG_TAG = getClass().getSimpleName();
	private ListView lv_category;
	private FreshListView lv_subcategory, lv_search;
	private Lv_subcategory_adapter lv_subcategory_adapter;
	private Lv_category_adapter lv_category_adapter;
	public final static String TAG = "AddFollowActivity";
	private ArrayList<ItemFollows> subcategoryNameList = new ArrayList<>();// 通用的listview数据
	private ArrayList<ItemFollows> itemFollowDBList = new ArrayList<>();// ItemFollows表的所有数据
	private SearchView searchView;
	private Context context;
	// private boolean isfirstsearch_searchview=true;
	private ArrayList<TopItem> topItems = new ArrayList<TopItem>();
	private Map<String, ArrayList<ItemFollows>> allItemFollowMap = new HashMap<>();

	private DatabaseHelper databaseHelper = null;
	private int page_search, page_category;//搜索的页数和关注的页数
	// private ItemFollowsAdapter searchAdapter;
	private Lv_search_adapter searchAdapter;
	private String keywordString;
	private String requestType;

	private int requestCount = 0;// 请求网络的次数，总共八次

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_follow);
		init();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getDate_left();
		// 获取ItemFollows表数据
		getdatafromdb();
	}

	private void getdatafromdb() {
		try {
			Dao<ItemFollows, Integer> itemFollowsDao = getHelper()
					.getItemFollowsDao();
			itemFollowDBList = (ArrayList<ItemFollows>) itemFollowsDao
					.queryForAll();
			for (ItemFollows itemFollows : itemFollowDBList) {
				Log.i(LOG_TAG + "--getdatafromdb", itemFollows.getName());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void init() {
		context = this;
		lv_category = (ListView) findViewById(R.id.lv_category);
		lv_subcategory = (FreshListView) findViewById(R.id.lv_subcategory);
		lv_search = (FreshListView) findViewById(R.id.lv_search);
		ViewSetting.settingListview(lv_subcategory, this);
		ViewSetting.settingListview(lv_search, this);
		lv_category_adapter = new Lv_category_adapter(this, topItems);

		lv_subcategory_adapter = new Lv_subcategory_adapter(this,
				subcategoryNameList);
		lv_category.setAdapter(lv_category_adapter);
		lv_subcategory.setAdapter(lv_subcategory_adapter);
		lv_category_adapter.setSelectItem(0);
		lv_category.setOnItemClickListener(this);
		page_search = 1;
		page_category = 1;

		// 加载更多事件回调（可选）
		lv_subcategory.setOnLoadMoreStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				Log.i(LOG_TAG, "setOnLoadMoreStartListener");
				page_category++;
				getDateMore(requestType, backlistener_more_subcategory,
						page_category, C.DEFAULT_COUNT);
			}

		});
		lv_search.setOnLoadMoreStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				page_search++;
				loadMore(requestType, keywordString, page_search,
						C.DEFAULT_COUNT);
			};
		});
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

	private void getDate_left() {
		customProgressDialog.show();
		VolleyManager.requestVolley(null, C.SERVER + C.URL_TOP, Method.GET,
				backlistener_category, errorListener, mQueue);
	}

	private void getDate_right(String type, Listener<String> listener,
			int pageindex, int pagesize) {
		// customProgressDialog.show();

		// VolleyManager.requestVolley(null, C.SERVER + C.URL_TOPLIST +
		// "?object="
		// + type + "&pageindex=1&pagesize=20&sort=score", Method.GET,
		// backlistener_subcategory, errorListener, mQueue);
		Map<String, String> gparams = new HashMap<String, String>();
		gparams = new HashMap<String, String>();
		gparams.put("object", type);
		gparams.put("sort", "score");
		gparams.put("pagesize", pagesize + "");
		gparams.put("pageindex", pageindex + "");
		VolleyManager.requestVolley(gparams, C.SERVER + C.URL_TOPLIST,
				Method.POST, listener, errorListener, mQueue);
	}

	private void getDateMore(String mtype,
			Listener<String> backlistener_more_subcategory, int page_category,
			int defaultCount) {
		Map<String, String> gparams = new HashMap<String, String>();
		gparams = new HashMap<String, String>();
		gparams.put("object", mtype);
		Log.i("mtype", mtype);
		gparams.put("sort", "score");
		gparams.put("pageindex", page_category + "");
		gparams.put("pagesize", defaultCount + "");
		VolleyManager.requestVolley(gparams, C.SERVER + C.URL_TOPLIST,
				Method.POST, backlistener_more_subcategory, errorListener,
				mQueue);

	};

	Listener<String> backlistener_category = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			requestCount++;
			try {
				topItems.clear();
				topItems.addAll(TopItem.formList(response));
				requestType = topItems.get(0).getType();
				for (TopItem topItem : topItems) {
					getDate_right(topItem.getType(), backlistener_subcategory,
							1, C.DEFAULT_COUNT);
				}
				lv_category_adapter.notifyDataSetChanged();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(context, "解析错误", 1).show();
			}

		}

	};
	Listener<String> backlistener_subcategory = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			requestCount++;
			if (requestCount == 8) {
				if (customProgressDialog != null
						&& customProgressDialog.isShowing())
					customProgressDialog.dismiss();
			}
			try {
				// subcategoryNameList.clear();
				ArrayList<ItemFollows> lists = (ArrayList<ItemFollows>) ItemFollows
						.formList(response);

				if (lists.size() > 0) {
					allItemFollowMap.put(lists.get(0).getType(), lists);
				}
				if (requestCount == 8) {
					subcategoryNameList.clear();
					subcategoryNameList.addAll(allItemFollowMap
							.get(requestType));
					lv_subcategory.setAdapter(lv_subcategory_adapter);
				lv_subcategory.startLoadMore(); // 开启LoadingMore功能
				}
//				if (lists != null && !lists.isEmpty()) {
//					// Log.i("VISIBLE", "lists" + lists.size());
//					// noResult_rl.setVisibility(View.GONE);
//					// searchAdapter = new Lv_search_adapter(context, lists);
//					if (lists.size() < C.DEFAULT_COUNT) {
//						lv_subcategory.stopLoadMore();
//					} else {
//						lv_subcategory.setAdapter(lv_subcategory_adapter);
//						lv_subcategory.startLoadMore(); // 开启LoadingMore功能
//					}
//				} else {
//					// lv_search.setRefreshFail("加载失败");
//					// lv_search.setVisibility(View.GONE);
//					// noResult_rl.setVisibility(View.VISIBLE);
//				}

				// lv_subcategory_adapter.notifyDataSetChanged();
			} catch (Exception e) {
				e.printStackTrace();
				// lv_subcategory_adapter.notifyDataSetChanged();
				lv_subcategory.setAdapter(lv_subcategory_adapter);
				Toast.makeText(context, "解析错误", 1).show();
			}

		}
	};
	Listener<String> backlistener_more_subcategory = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			Log.i("backlistener_more_subcategory",
					"backlistener_more_subcategory");
			try {
				ArrayList<ItemFollows> lists = (ArrayList<ItemFollows>) ItemFollows
						.formList(response);
				Log.i("backlistener_more_subcategory--lists.size()", lists.size()+"");
				//subcategoryNameList.clear();
			    subcategoryNameList.addAll(lists);
			    lv_subcategory_adapter.notifyDataSetChanged();
				// try {
				// // subcategoryNameList.clear();
				// ArrayList<ItemFollows> lists = (ArrayList<ItemFollows>)
				// ItemFollows
				// .formList(response);
				// subcategoryNameList.addAll(lists);
				// if (subcategoryNameList.size() > 0) {
				// allItemFollowMap.put(lists.get(0).getType(),
				// subcategoryNameList);
				// }

				if (lists != null && !lists.isEmpty()
						&& lists.size() == C.DEFAULT_COUNT) {
					lv_subcategory_adapter.notifyDataSetChanged();
					lv_subcategory.setLoadMoreSuccess();
				} else if (lists != null && lists.size() > 0) {
					lv_subcategory_adapter.notifyDataSetChanged();
					lv_subcategory.stopLoadMore();
					Log.i("stopLoadMore", "stopLoadMore");
				} else {
					lv_subcategory.stopLoadMore();
					Log.i("stopLoadMore", "stopLoadMore");
				}

				// lv_subcategory.setAdapter(lv_subcategory_adapter);
			} catch (Exception e) {
				e.printStackTrace();
				// lv_subcategory_adapter.notifyDataSetChanged();
				// lv_subcategory.setAdapter(lv_subcategory_adapter);
				Toast.makeText(context, "解析错误", 1).show();
			}

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_follow, menu);
		searchView = (SearchView) MenuItemCompat.getActionView(menu
				.findItem(R.id.action_search));
		searchView.setOnQueryTextListener(mOnQueryTextListener);
		searchView.setOnCloseListener(new SearchView.OnCloseListener() {

			@Override
			public boolean onClose() {
				lv_search.setVisibility(View.GONE);
				lv_search.startAnimation(AnimationUtils.loadAnimation(
						AddFollowActivity.this, R.anim.header_disappear));
				return false;
			}
		});
		return true;
	}

	// The following callbacks are called for the
	// SearchView.OnQueryChangeListener
	// For more about using SearchView, see src/.../view/SearchView1.java and
	// SearchView2.java
	private final SearchView.OnQueryTextListener mOnQueryTextListener = new SearchView.OnQueryTextListener() {
		@Override
		public boolean onQueryTextChange(String newText) {
			// lv_search.setAdapter(new
			// Lv_search_adapter(AddFollowActivity.this,
			// subcategoryNameList));
			// if (lv_search.getVisibility() == View.GONE) {
			// lv_search.setVisibility(View.VISIBLE);
			// lv_search.startAnimation(AnimationUtils.loadAnimation(
			// AddFollowActivity.this, R.anim.header_appear));
			// }
			return true;
		}

		@Override
		public boolean onQueryTextSubmit(String query) {
			page_search = 1;
			keywordString = query;

			refresh(requestType, query, page_search, C.DEFAULT_COUNT);

			// Toast.makeText(AddFollowActivity.this,
			// "Searching for: " + query + "...", Toast.LENGTH_SHORT)
			// .show();
			// 隐藏键盘
			hideKeybord();
			return true;
		}
	};

	/**
	 * 隐藏键盘
	 */
	private void hideKeybord() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
		}
	}

	private void refresh(String type, String keyword, int page, int defaultCount) {
		Map<String, String> gparams = new HashMap<String, String>();
		gparams = new HashMap<String, String>();
		gparams.put("object", type);
		gparams.put("keyword", keyword);
		gparams.put("pagesize", defaultCount + "");
		gparams.put("pageindex", page + "");
		Log.i("keyword", "keyword" + keyword);
		VolleyManager.requestVolley(gparams, C.SERVER + C.URL_SEARCH,
				Method.POST, back_search_listener, errorListener, mQueue);
	}

	private void loadMore(String type, String keyword, int page,
			int defaultCount) {
		Map<String, String> gparams = new HashMap<String, String>();
		gparams = new HashMap<String, String>();
		gparams.put("object", type);
		gparams.put("keyword", keyword);
		gparams.put("pagesize", defaultCount + "");
		gparams.put("pageindex", page + "");
		Log.i("keyword", "keyword" + keyword);
		VolleyManager.requestVolley(gparams, C.SERVER + C.URL_SEARCH,
				Method.POST, back_search_Morelistener, errorListener, mQueue);
	}

	Listener<String> back_search_listener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			if (customProgressDialog != null
					&& customProgressDialog.isShowing())
				customProgressDialog.dismiss();

			lv_search.setAdapter(new Lv_search_adapter(AddFollowActivity.this,
					subcategoryNameList));
			if (lv_search.getVisibility() == View.GONE) {
				lv_search.setVisibility(View.VISIBLE);
				lv_search.startAnimation(AnimationUtils.loadAnimation(
						AddFollowActivity.this, R.anim.header_appear));
			}

			try {
				ArrayList<ItemFollows> lists = ItemFollows.formList(response);
				if (lists != null && !lists.isEmpty()) {
					lv_search.setVisibility(View.VISIBLE);
					// lv_search.setRefreshSuccess("加载成功"); // 通知加载成功
					Log.i("VISIBLE", "lists" + lists.size());
					// noResult_rl.setVisibility(View.GONE);
					searchAdapter = new Lv_search_adapter(context, lists);
					if (lists.size() < C.DEFAULT_COUNT) {
						lv_search.setAdapter(searchAdapter);
						lv_search.stopLoadMore();
					} else {
						lv_search.setAdapter(searchAdapter);
						lv_search.startLoadMore(); // 开启LoadingMore功能
					}
				} else {
					// lv_search.setRefreshFail("加载失败");
					// lv_search.setVisibility(View.GONE);
					// noResult_rl.setVisibility(View.VISIBLE);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};
	Listener<String> back_search_Morelistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			try {
				ArrayList<ItemFollows> lists = ItemFollows.formList(response);
				if (lists != null && !lists.isEmpty()) {
					lv_search.setVisibility(View.VISIBLE);
					// lv_search.setRefreshSuccess("加载成功"); // 通知加载成功
					// noResult_rl.setVisibility(View.GONE);
					if (lists != null && !lists.isEmpty()
							&& lists.size() == C.DEFAULT_COUNT) {
						searchAdapter.addMoreData(lists);
						lv_search.setLoadMoreSuccess();
					} else if (lists != null && lists.size() > 0) {
						// searchAdapter.addMoreData(lists);
						lv_search.stopLoadMore();
					} else {
						lv_search.stopLoadMore();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	static class Lv_category_adapter extends BaseAdapter {
		private Context context;
		private ArrayList<TopItem> categoryNames;
		private int selectItem = -1;

		public Lv_category_adapter(Context context,
				ArrayList<TopItem> categoryNames) {
			this.context = context;
			this.categoryNames = categoryNames;
		}

		@Override
		public int getCount() {
			return categoryNames.size();
		}

		@Override
		public Object getItem(int position) {
			return categoryNames.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// 注释此块是因为当点击item后，滑动listview，有的item值为空，不解。
			// final ViewHolder holder;
			// if (convertView == null) {
			// convertView = LayoutInflater.from(context).inflate(
			// R.layout.activity_my_follow_item1, null);
			// holder = new ViewHolder();
			// holder.title = (TextView) convertView
			// .findViewById(R.id.tv_category);
			// convertView.setTag(holder);
			// } else {
			// holder = (ViewHolder) convertView.getTag();
			// }
			// if (position == selectItem) {
			// holder.title.setBackgroundColor(Color.RED);
			// } else {
			// holder.title.setBackgroundColor(Color.WHITE);
			// }
			// Log.i(TAG, position+"--lv_category_getView");
			// holder.title.setText(categoryNames[position]);
			// if(convertView==null)
			convertView = LayoutInflater.from(context).inflate(
					R.layout.activity_my_follow_item1, null);

			TextView title = (TextView) convertView
					.findViewById(R.id.tv_category);
			if (position == selectItem) {
				title.setBackgroundColor(Color.RED);
			} else {
				title.setBackgroundColor(Color.WHITE);
			}
			// Log.i(TAG, position + "--lv_category_getView");
			title.setText(categoryNames.get(position).getName());
			convertView.setTag(categoryNames.get(position).getType());
			return convertView;
		}

		public void setSelectItem(int selectItem) {
			this.selectItem = selectItem;
			this.notifyDataSetChanged();
		}
	}

	class Lv_subcategory_adapter extends BaseAdapter {
		private Context context;
		private ArrayList<ItemFollows> subcategoryNames;

		public Lv_subcategory_adapter(Context context,
				ArrayList<ItemFollows> subcategoryNames) {
			this.context = context;
			this.subcategoryNames = subcategoryNames;
		}

		@Override
		public int getCount() {
			return subcategoryNames.size();
		}

		@Override
		public Object getItem(int position) {
			return subcategoryNames.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.activity_my_follow_item2, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView
						.findViewById(R.id.tv_subcategory);
				holder.iv_add = (ImageView) convertView
						.findViewById(R.id.iv_add);
				holder.iv_add.setOnClickListener((AddFollowActivity) context);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (itemFollowDBList.contains(subcategoryNames.get(position))) {
				holder.iv_add
						.setBackgroundResource(R.drawable.biz_news_column_subscribe_cancel);
				holder.iv_add.setTag(R.id.itemfollow_add_tag, true);
				//Log.i("getView1", subcategoryNames.get(position).getId());
			} else {
				holder.iv_add
						.setBackgroundResource(R.drawable.biz_news_column_subscribe_add_selector);
				holder.iv_add.setTag(R.id.itemfollow_add_tag, false);
				//Log.i("getView2", "2");
			}
			holder.title.setText(subcategoryNames.get(position).getName());
			holder.iv_add.setTag(position);
			return convertView;
		}

		class ViewHolder {
			TextView title;
			ImageView iv_add;
		}
	}

	static class Lv_search_adapter extends BaseAdapter {
		private Context context;
		private ArrayList<ItemFollows> subcategoryNames;

		public Lv_search_adapter(Context context,
				ArrayList<ItemFollows> subcategoryNames) {
			this.context = context;
			this.subcategoryNames = subcategoryNames;
		}

		public void addMoreData(ArrayList<ItemFollows> lists) {
			this.subcategoryNames.addAll(lists);
			this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return subcategoryNames.size();
		}

		@Override
		public Object getItem(int position) {
			return subcategoryNames.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.activity_my_follow_item2, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView
						.findViewById(R.id.tv_subcategory);
				holder.iv_add = (ImageView) convertView
						.findViewById(R.id.iv_add);
				holder.iv_add.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Log.i(TAG, "onClick--iv_add");
						Animation animation = AnimationUtils.loadAnimation(
								context, R.anim.base_loading_small_anim);
						v.setBackgroundResource(R.drawable.base_loading_small_icon);
						v.startAnimation(animation);
						final View temp_v = v;
						animation.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {

							}

							@Override
							public void onAnimationRepeat(Animation animation) {

							}

							@Override
							public void onAnimationEnd(Animation animation) {
								temp_v.setBackgroundResource(R.drawable.biz_news_column_subscribe_cancel);
							}
						});
					}
				});
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.title.setText(subcategoryNames.get(position).getName());
			return convertView;
		}

		static class ViewHolder {
			TextView title;
			ImageView iv_add;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.lv_category:
			if(!requestType.equals(view.getTag())){//重置页数
				page_category=1;
			}
			requestType = (String) view.getTag();
			lv_category_adapter.setSelectItem(position);
			// Log.i(TAG + "onItemClick", position +
			// "--lv_category_onItemClick");
			// lv_category_adapter.notifyDataSetChanged();
			// subcategoryNameList.clear();
			// subcategoryNameList.addAll(testData());
			// lv_subcategory.setAdapter(lv_subcategory_adapter);
			// lv_subcategory_adapter.notifyDataSetChanged();
			String tag = (String) view.getTag();
			Log.i(LOG_TAG, tag);
			ArrayList<ItemFollows> arrayList_temp = allItemFollowMap.get(tag);
			Log.i("allItemFollowMap", arrayList_temp.size() + "");
			if (arrayList_temp != null && arrayList_temp.size() > 0) {
				subcategoryNameList.clear();
				subcategoryNameList.addAll(arrayList_temp);
				// lv_subcategory_adapter.notifyDataSetChanged();
				// lv_subcategory.invalidateViews();
				lv_subcategory.setAdapter(lv_subcategory_adapter);
			} else {
				getDate_right(tag, backlistener_subcategory, 1, C.DEFAULT_COUNT);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_add:
			Log.i(TAG, "onClick--iv_add");
			if ((boolean) v.getTag(R.id.itemfollow_add_tag)) {
				deleteDB(subcategoryNameList.get((int) v.getTag()));
			} else {
				saveDB(subcategoryNameList.get((int) v.getTag()));
			}
			// v.setBackgroundResource(R.drawable.biz_news_column_subscribe_cancel);
			getdatafromdb();
			lv_subcategory_adapter.notifyDataSetChanged();
			// Animation animation = AnimationUtils.loadAnimation(this,
			// R.anim.base_loading_small_anim);
			// v.setBackgroundResource(R.drawable.base_loading_small_icon);
			// v.startAnimation(animation);
			// final View temp_v = v;
			// animation.setAnimationListener(new AnimationListener() {
			//
			// @Override
			// public void onAnimationStart(Animation animation) {
			//
			// }
			//
			// @Override
			// public void onAnimationRepeat(Animation animation) {
			//
			// }
			//
			// @Override
			// public void onAnimationEnd(Animation animation) {
			// temp_v.setBackgroundResource(R.drawable.biz_news_column_subscribe_cancel);
			// saveDB();
			// }
			// });
			break;

		default:
			break;
		}
	}

	protected void saveDB(ItemFollows itemFollows) {
		try {
			Dao<ItemFollows, Integer> itemFollowsDao = getHelper()
					.getItemFollowsDao();
			// ItemFollows itemFollows = new ItemFollows();
			// store it in the database
			itemFollows.setDatetime(System.currentTimeMillis());
			itemFollowsDao.create(itemFollows);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void deleteDB(ItemFollows itemFollows) {
		try {
			Dao<ItemFollows, Integer> itemFollowsDao = getHelper()
					.getItemFollowsDao();
			// ItemFollows itemFollows = new ItemFollows();
			// store it in the database
			itemFollowsDao.delete(itemFollows);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
