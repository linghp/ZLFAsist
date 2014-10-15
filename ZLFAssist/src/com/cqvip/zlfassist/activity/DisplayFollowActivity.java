package com.cqvip.zlfassist.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.base.BaseActionBarActivity;
import com.cqvip.zlfassist.bean.GeneralResult;
import com.cqvip.zlfassist.bean.ItemFollows;
import com.cqvip.zlfassist.constant.C;
import com.cqvip.zlfassist.db.DatabaseHelper;
import com.cqvip.zlfassist.http.VolleyManager;
import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

public class DisplayFollowActivity extends BaseActionBarActivity implements
		OnRefreshListener, OnItemClickListener, OnItemLongClickListener,
		OnClickListener {
	private Context context;

	public boolean isedit = false; // 是否处于编辑状态
	private Adapter adapter;
	private ViewGroup mSelectionMenuView;
	private Button cancel, delete;
	private SwipeRefreshLayout mSwipeRefreshWidget;
	private ListView mList;
	private Map<String, String> gparams;
	private Handler mHandler = new Handler();
	private final Runnable mRefreshDone = new Runnable() {

		@Override
		public void run() {
			getdatafromdb();
			mSwipeRefreshWidget.setRefreshing(false);
		}

	};
	private DatabaseHelper databaseHelper = null;
	private ArrayList<ItemFollows> allFollowItem = new ArrayList<>();// 所有的关注
	private ArrayList<ItemFollows> selectedFollowItem = new ArrayList<>();// 选中的关注

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_display_follow);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mSelectionMenuView = (ViewGroup) findViewById(R.id.selection_menu);
		delete = (Button) findViewById(R.id.selection_delete);
		cancel = (Button) findViewById(R.id.deselect_all);
		delete.setOnClickListener(this);
		cancel.setOnClickListener(this);
		mSwipeRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_widget);
		mSwipeRefreshWidget.setColorSchemeResources(R.color.color1,
				R.color.color2, R.color.color3, R.color.color4);
		mList = (ListView) findViewById(R.id.Lv_followcontent);

		// list = new ArrayList<String>(Arrays.asList(TITLES));
		context = this;
		adapter = new Adapter(context, allFollowItem);
		mList.addFooterView(getLayoutInflater().inflate(
				R.layout.item_displayfollow_list_footer, null));
		mList.setAdapter(adapter);
		mList.setOnItemClickListener(this);
		mList.setOnItemLongClickListener(this);
		mSwipeRefreshWidget.setOnRefreshListener(this);
		
		boolean  flag = getIntent().getBooleanExtra("flag", false);
		if(flag){
			String idString = getIntent().getStringExtra("id");
			String type = getIntent().getStringExtra("type");
			//获取数据
			getScanDate(idString,type);
			
		}else{

		getdatafromdb();
		}
	}

	private void getScanDate(String idString, String type) {
		 HashMap<String, String> gparams = new HashMap<String, String>();
			gparams.put("key", idString);
			gparams.put("type", type);
			Log.i("param","result:"+idString+type);
		//	VolleyManager.requestVolley(gparams, C.SERVER+C.URL_TOPIC_INFO, Method.POST, backlistener_content, errorListener, mQueue);
		
	}

	@Override
	public void onRefresh() {
		refresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_follow, menu);
		return true;
	}

	/**
	 * Click handler for the menu item to force a refresh.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final int id = item.getItemId();
		switch (id) {
		case R.id.force_refresh:
			mSwipeRefreshWidget.setRefreshing(true);
			refresh();
			return true;
		case R.id.follow_add:
			myStartActivity();
			return true;
		case android.R.id.home:
			finish();
			return true;
		}
		return false;
	}

	private void myStartActivity() {
		startActivityForResult(new Intent(this, AddFollowActivity.class),1);
		overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_left);
	}

	private void refresh() {
		mHandler.removeCallbacks(mRefreshDone);
		mHandler.postDelayed(mRefreshDone, 1000);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.selection_delete:
			// for (String str : mSelectedIds) {
			// list.remove(str);
			// }
			allFollowItem.removeAll(selectedFollowItem);
			deleteDB(selectedFollowItem);
			adapter.notifyDataSetChanged();
			isedit = false;
			clearSelection();
			break;

		case R.id.deselect_all:
			isedit = false;
			clearSelection();
			break;

		default:
			break;
		}
	}

	private void clearSelection() {
		selectedFollowItem.clear();
		showOrHideSelectionMenu();
		mList.invalidateViews();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == allFollowItem.size()) {
			myStartActivity();
		} else if (isedit) {

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		getdatafromdb();
		Log.i("onActivityResult", "display");
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		isedit = true;
		mList.invalidateViews();
		// mList.invalidate();
		return true;
	}

	class Adapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater = null;
		private ArrayList<ItemFollows> list;

		public Adapter(Context context, ArrayList<ItemFollows> list) {
			this.context = context;
			this.list = list;
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder holder;
			// TODO Auto-generated method stub
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(
						R.layout.item_displayfollow_list, null);
				holder.title = (TextView) convertView
						.findViewById(R.id.tv_name);
				holder.cb = (CheckBox) convertView.findViewById(R.id.check);

				// holder.cb
				// .setOnCheckedChangeListener(new OnCheckedChangeListener() {
				//
				// @Override
				// public void onCheckedChanged(
				// CompoundButton buttonView, boolean isChecked) {
				// if(position>=mList.getFirstVisiblePosition()&&position<=mList.getLastVisiblePosition()){
				// if (isChecked) {
				// mSelectedIds.add(list.get(position));
				// Log.i("onCheckedChanged", list.get(position));
				// } else {
				// mSelectedIds.remove(list.get(position));
				// }
				// delete.setText("删除("+mSelectedIds.size()+")");
				// showOrHideSelectionMenu();
				// }
				// }
				// });
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.title.setText(list.get(position).getName());
			holder.cb.setChecked(false);
			if (isedit) {
				holder.cb.setVisibility(View.VISIBLE);
			} else {
				holder.cb.setVisibility(View.GONE);
			}
			if (isedit && selectedFollowItem.contains(list.get(position))) {
				holder.cb.setChecked(true);
			}

			holder.cb.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					holder.cb.toggle();
					Log.i("getView", "onClick--" + position);
					if (selectedFollowItem.contains(list.get(position))) {
						selectedFollowItem.remove(list.get(position));
					} else {
						selectedFollowItem.add(list.get(position));
					}
					showOrHideSelectionMenu();
				}
			});
			return convertView;
		}

		class ViewHolder {
			TextView title;
			CheckBox cb;
		}
	}

	private void showOrHideSelectionMenu() {
		mList.invalidateViews();
		boolean shouldBeVisible = !selectedFollowItem.isEmpty();
		boolean isVisible = mSelectionMenuView.getVisibility() == View.VISIBLE;
		if (shouldBeVisible) {
			if (!isVisible) {
				// show menu
				mSelectionMenuView.setVisibility(View.VISIBLE);
				mSelectionMenuView.startAnimation(AnimationUtils.loadAnimation(
						this, R.anim.footer_appear));
			}
		} else if (!shouldBeVisible && isVisible) {
			// hide menu
			mSelectionMenuView.setVisibility(View.GONE);
			mSelectionMenuView.startAnimation(AnimationUtils.loadAnimation(
					this, R.anim.footer_disappear));
		}
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
			allFollowItem.clear();
			ArrayList<ItemFollows> temp = (ArrayList<ItemFollows>) itemFollowsDao
					.queryBuilder().orderBy("datetime", false).query();
			Log.i("getdatafromdb", allFollowItem.size() + "");
			allFollowItem.addAll(temp);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		adapter.notifyDataSetChanged();
	}

	protected void deleteDB(ArrayList<ItemFollows> itemFollows) {
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
