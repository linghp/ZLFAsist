package com.cqvip.zlfassist.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
import com.cqvip.zlfassist.base.BaseActionBarActivity;
import com.cqvip.zlfassist.bean.TopItem;
import com.cqvip.zlfassist.bean.ItemFollows;
import com.cqvip.zlfassist.constant.C;
import com.cqvip.zlfassist.db.DatabaseHelper;
import com.cqvip.zlfassist.http.VolleyManager;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

public class AddFollowActivity extends BaseActionBarActivity implements
		OnItemClickListener, OnClickListener {
	private final String LOG_TAG = getClass().getSimpleName();
	private ListView lv_category, lv_subcategory, lv_search;
	private Lv_subcategory_adapter lv_subcategory_adapter;
	private Lv_category_adapter lv_category_adapter;
	public final static String TAG = "AddFollowActivity";
	private ArrayList<ItemFollows> subcategoryNameList = new ArrayList<>();//通用的listview数据
	private ArrayList<ItemFollows> itemFollowDBList = new ArrayList<>();//ItemFollows表的所有数据
	private SearchView searchView;
	private Context context;
	// private boolean isfirstsearch_searchview=true;
	private ArrayList<TopItem> topItems = new ArrayList<TopItem>();
	private Map<String, ArrayList<ItemFollows>> allItemFollowMap = new HashMap<>();

	private DatabaseHelper databaseHelper = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_follow);
		init();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getDate_left();
		//获取ItemFollows表数据
		getdatafromdb();
	}
	
	private void getdatafromdb() {
		try {
			Dao<ItemFollows, Integer>	itemFollowsDao = getHelper()
					.getItemFollowsDao();
			itemFollowDBList=(ArrayList<ItemFollows>) itemFollowsDao.queryForAll();
			for (ItemFollows itemFollows : itemFollowDBList) {
				Log.i(LOG_TAG+"--getdatafromdb", itemFollows.getName());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	private void init() {
		context = this;
		lv_category = (ListView) findViewById(R.id.lv_category);
		lv_subcategory = (ListView) findViewById(R.id.lv_subcategory);
		lv_search = (ListView) findViewById(R.id.lv_search);
		lv_category_adapter = new Lv_category_adapter(this, topItems);

		lv_subcategory_adapter = new Lv_subcategory_adapter(this,
				subcategoryNameList);
		lv_category.setAdapter(lv_category_adapter);
		lv_subcategory.setAdapter(lv_subcategory_adapter);
		lv_category_adapter.setSelectItem(0);
		lv_category.setOnItemClickListener(this);
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

	private void getDate_right(String type) {
		// customProgressDialog.show();
		VolleyManager.requestVolley(null, C.SERVER + C.URL_TOPLIST + "?object="
				+ type + "&pageindex=1&pagesize=20&sort=score", Method.GET,
				backlistener_subcategory, errorListener, mQueue);
	}

	Listener<String> backlistener_category = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			if (customProgressDialog != null
					&& customProgressDialog.isShowing())
				customProgressDialog.dismiss();

			try {
				topItems.clear();
				topItems.addAll(TopItem.formList(response));
				getDate_right(topItems.get(0).getType());
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
			try {
				subcategoryNameList.clear();
				ArrayList<ItemFollows> arrayList_temp = (ArrayList<ItemFollows>) ItemFollows
						.formList(response);
				subcategoryNameList.addAll(arrayList_temp);
				if (subcategoryNameList.size() > 0) {
					allItemFollowMap.put(arrayList_temp.get(0).getType(),
							arrayList_temp);
				}
				//lv_subcategory_adapter.notifyDataSetChanged();
				lv_subcategory.setAdapter(lv_subcategory_adapter);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//lv_subcategory_adapter.notifyDataSetChanged();
				lv_subcategory.setAdapter(lv_subcategory_adapter);
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
			lv_search.setAdapter(new Lv_search_adapter(AddFollowActivity.this,
					subcategoryNameList));
			if (lv_search.getVisibility() == View.GONE) {
				lv_search.setVisibility(View.VISIBLE);
				lv_search.startAnimation(AnimationUtils.loadAnimation(
						AddFollowActivity.this, R.anim.header_appear));
			}
			return true;
		}

		@Override
		public boolean onQueryTextSubmit(String query) {
			Toast.makeText(AddFollowActivity.this,
					"Searching for: " + query + "...", Toast.LENGTH_SHORT)
					.show();
			return true;
		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
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
			// TODO Auto-generated method stub
			return categoryNames.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return categoryNames.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
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
			// TODO Auto-generated method stub
			return subcategoryNames.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return subcategoryNames.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
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
			if(itemFollowDBList.contains(subcategoryNames.get(position))){
				holder.iv_add.setBackgroundResource(R.drawable.biz_news_column_subscribe_cancel);
				holder.iv_add.setTag(R.id.itemfollow_add_tag, true);
				Log.i("getView1", subcategoryNames.get(position).getId());
			}else{
				holder.iv_add.setBackgroundResource(R.drawable.biz_news_column_subscribe_add_selector);
				holder.iv_add.setTag(R.id.itemfollow_add_tag, false);
				Log.i("getView2", "2");
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

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return subcategoryNames.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return subcategoryNames.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
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
								// TODO Auto-generated method stub

							}

							@Override
							public void onAnimationRepeat(Animation animation) {
								// TODO Auto-generated method stub

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
				if (arrayList_temp != null&&arrayList_temp.size() > 0) {
					subcategoryNameList.clear();
					subcategoryNameList.addAll(arrayList_temp);
//					lv_subcategory_adapter.notifyDataSetChanged();
//					lv_subcategory.invalidateViews();
					lv_subcategory.setAdapter(lv_subcategory_adapter);
				} else {
					getDate_right(tag);
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
			if((boolean)v.getTag(R.id.itemfollow_add_tag)){
				deleteDB(subcategoryNameList.get((int)v.getTag()));
			}else{
				saveDB(subcategoryNameList.get((int)v.getTag()));
			}
			//v.setBackgroundResource(R.drawable.biz_news_column_subscribe_cancel);
			getdatafromdb();
			lv_subcategory_adapter.notifyDataSetChanged();
//			Animation animation = AnimationUtils.loadAnimation(this,
//					R.anim.base_loading_small_anim);
			//v.setBackgroundResource(R.drawable.base_loading_small_icon);
			//v.startAnimation(animation);
//			final View temp_v = v;
//			animation.setAnimationListener(new AnimationListener() {
//
//				@Override
//				public void onAnimationStart(Animation animation) {
//					// TODO Auto-generated method stub
//
//				}
//
//				@Override
//				public void onAnimationRepeat(Animation animation) {
//					// TODO Auto-generated method stub
//
//				}
//
//				@Override
//				public void onAnimationEnd(Animation animation) {
//					temp_v.setBackgroundResource(R.drawable.biz_news_column_subscribe_cancel);
//					saveDB();
//				}
//			});
			break;

		default:
			break;
		}
	}

	protected void saveDB(ItemFollows itemFollows) {
		try {
			Dao<ItemFollows, Integer> itemFollowsDao = getHelper()
					.getItemFollowsDao();
			//ItemFollows itemFollows = new ItemFollows();
			// store it in the database
			itemFollowsDao.create(itemFollows);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void deleteDB(ItemFollows itemFollows) {
		try {
			Dao<ItemFollows, Integer> itemFollowsDao = getHelper()
					.getItemFollowsDao();
			//ItemFollows itemFollows = new ItemFollows();
			// store it in the database
			itemFollowsDao.delete(itemFollows);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
