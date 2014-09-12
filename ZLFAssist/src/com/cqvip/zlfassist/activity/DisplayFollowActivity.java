package com.cqvip.zlfassist.activity;

import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.R.anim;
import com.cqvip.zlfassist.R.color;
import com.cqvip.zlfassist.R.id;
import com.cqvip.zlfassist.R.layout;
import com.cqvip.zlfassist.R.menu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DisplayFollowActivity extends ActionBarActivity implements
		OnRefreshListener, OnItemClickListener {
	public static final String[] TITLES = { "Henry IV (1)", "Henry V",
			"Henry VIII", "Richard II", "Richard III", "Merchant of Venice",
			"Othello", "King Lear", "Henry IV (1)", "Henry V", "Henry VIII",
			"Richard II", "Richard III", "Merchant of Venice", "Othello",
			"King Lear", "Henry IV (1)", "Henry V", "Henry VIII", "Richard II",
			"Richard III", "Merchant of Venice", "Othello", "King Lear",
			"Henry IV (1)", "Henry V", "Henry VIII", "Richard II",
			"Richard III", "Merchant of Venice", "Othello", "King Lear" };
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
		mSwipeRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_widget);
		mSwipeRefreshWidget.setColorScheme(R.color.color1, R.color.color2,
				R.color.color3, R.color.color4);
		mList = (ListView) findViewById(R.id.Lv_followcontent);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, TITLES);
		mList.addFooterView(getLayoutInflater().inflate(
				R.layout.item_displayfollow_list_footer, null));
		mList.setAdapter(arrayAdapter);
		mList.setOnItemClickListener(this);
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
			startActivity(new Intent(this, MyFollowActivity.class));
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == TITLES.length) {
			startActivity(new Intent(this, MyFollowActivity.class));
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
		}
	}
}
