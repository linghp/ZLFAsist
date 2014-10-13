package com.cqvip.zlfassist.activity;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.adapter.DragAdapter;
import com.cqvip.zlfassist.base.BaseActionBarActivity;
import com.cqvip.zlfassist.bean.ChannelItem;
import com.cqvip.zlfassist.db.DatabaseHelper;
import com.cqvip.zlfassist.view.DragGrid;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

/**
 * 频道管理
 */
public class ChannelActivity extends BaseActionBarActivity {
	public static String TAG = "ChannelActivity";
	/** 用户栏目的GRIDVIEW */
	private DragGrid userGridView;
	/** 用户栏目对应的适配器，可以拖动 */
	DragAdapter userAdapter;
	/** 用户栏目列表 */
	ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();
	/** 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。 */
	boolean isMove = false;
	private DatabaseHelper databaseHelper = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.channel);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		initView();
		initData();
	}

	/** 初始化数据 */
	private void initData() {
		// userChannelList =
		// ((ArrayList<ChannelItem>)ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).getUserChannel());
		try {
			Dao<ChannelItem, Integer> channelSortDao = getHelper()
					.getChannelSortDao();
			userChannelList.add(new ChannelItem("", "文章"));
			userChannelList.addAll((ArrayList<ChannelItem>) channelSortDao
					.queryForAll());
			userAdapter = new DragAdapter(this, userChannelList);
			userGridView.setAdapter(userAdapter);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** 初始化布局 */
	private void initView() {
		userGridView = (DragGrid) findViewById(R.id.userGridView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		final int id = item.getItemId();
		switch (id) {
		case android.R.id.home:
			if (userAdapter.isListChanged()) {
				myStartActivity();
			} else {
				finish();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/** 退出时候保存选择后数据库的设置 */
	private void saveChannel() {
		try {
			Dao<ChannelItem, Integer> channelSortDao = getHelper()
					.getChannelSortDao();
			userChannelList.remove(0);
			channelSortDao.delete(userChannelList);
			userChannelList = (ArrayList<ChannelItem>) userAdapter
					.getChannnelLst();
			for (ChannelItem channelItem : userChannelList) {
				Log.i(TAG, channelItem.toString());
				channelSortDao.create(channelItem);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).saveUserChannel(userAdapter.getChannnelLst());
	}

	@Override
	public void onBackPressed() {
		if (userAdapter.isListChanged()) {
			// finish();
			myStartActivity();
			Log.d(TAG, "数据发生改变");
		} else {
			super.onBackPressed();
		}
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	private void myStartActivity() {
		saveChannel();
		startActivity(new Intent(this, MainActivity.class));
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
}
