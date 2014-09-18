package com.cqvip.zlfassist.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.cqvip.zlfassist.R;

public class DisplayFollowActivity extends ActionBarActivity implements
		OnRefreshListener, OnItemClickListener, OnItemLongClickListener,
		OnClickListener {
	private Context context;
	private List<String> list = new ArrayList<String>();
	// private List<String> selectid = new ArrayList<String>();
	private Set<String> mSelectedIds = new HashSet<String>();
	public boolean isedit = false; // 是否处于编辑状态
	private Adapter adapter;
	private ViewGroup mSelectionMenuView;
	private Button cancel, delete;
	public static final String[] TITLES = { "Henry IV (1)", "Henry V",
			"Henry VIII", "Richard II", "Richard III", "Merchant of Venice",
			"Othello", "King Lear", "11", "22", "33", "44", "55", "66", "777",
			"88" };
	private SwipeRefreshLayout mSwipeRefreshWidget;
	private ListView mList;

	private Handler mHandler = new Handler();
	private final Runnable mRefreshDone = new Runnable() {

		@Override
		public void run() {
			mSwipeRefreshWidget.setRefreshing(false);
		}

	};

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
		list = new ArrayList<String>(Arrays.asList(TITLES));
		context = this;
		adapter = new Adapter(context, list);
		mList.addFooterView(getLayoutInflater().inflate(
				R.layout.item_displayfollow_list_footer, null));
		mList.setAdapter(adapter);
		mList.setOnItemClickListener(this);
		mList.setOnItemLongClickListener(this);
		mSwipeRefreshWidget.setOnRefreshListener(this);
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
			startActivity(new Intent(this, AddFollowActivity.class));
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			return true;
		}
		return false;
	}

	private void refresh() {
		mHandler.removeCallbacks(mRefreshDone);
		mHandler.postDelayed(mRefreshDone, 1000);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.selection_delete:
//		for (String str : mSelectedIds) {
//			list.remove(str);
//		}
			list.removeAll(mSelectedIds);
			adapter.notifyDataSetChanged();
			clearSelection();
			break;

		case R.id.deselect_all:
			clearSelection();
			isedit=false;
			break;

		default:
			break;
		}
	}

	private void clearSelection() {
		mSelectedIds.clear();
		showOrHideSelectionMenu();
		mList.invalidateViews();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == TITLES.length) {
			startActivity(new Intent(this, AddFollowActivity.class));
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
		} else if (isedit) {

		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		isedit = true;
		mList.invalidateViews();
		//mList.invalidate();
		return true;
	}

	class Adapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater = null;
		private List<String> list;

		public Adapter(Context context, List<String> list) {
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

//				holder.cb
//						.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//							@Override
//							public void onCheckedChanged(
//									CompoundButton buttonView, boolean isChecked) {
//								if(position>=mList.getFirstVisiblePosition()&&position<=mList.getLastVisiblePosition()){
//								if (isChecked) {
//									mSelectedIds.add(list.get(position));
//									Log.i("onCheckedChanged", list.get(position));
//								} else {
//									mSelectedIds.remove(list.get(position));
//								}
//								delete.setText("删除("+mSelectedIds.size()+")");
//								showOrHideSelectionMenu();
//								}
//							}
//						});
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.title.setText(list.get(position));
			holder.cb.setChecked(false);
			if (isedit) {
				holder.cb.setVisibility(View.VISIBLE);
			}else{
				holder.cb.setVisibility(View.GONE);
			}
			if (isedit && mSelectedIds.contains(list.get(position))) {
				holder.cb.setChecked(true);
			}
			
			holder.cb.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					holder.cb.toggle();
					Log.i("getView", "onClick--"+position);
					String str=list.get(position);
					if(mSelectedIds.contains(str)){
						mSelectedIds.remove(str);
					}else{
						mSelectedIds.add(str);
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
		boolean shouldBeVisible = !mSelectedIds.isEmpty();
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

}
