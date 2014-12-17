package com.cqvip.zlfassist.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.adapter.ZKTopicListAdapter;
import com.cqvip.zlfassist.base.BaseActionBarActivity;
import com.cqvip.zlfassist.bean.ItemFollows;
import com.cqvip.zlfassist.bean.JudgeResult;
import com.cqvip.zlfassist.constant.C;
import com.cqvip.zlfassist.db.DBManager;
import com.cqvip.zlfassist.db.DatabaseHelper;
import com.cqvip.zlfassist.http.VolleyManager;
import com.cqvip.zlfassist.zkbean.ZKTopic;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

public class AddFavorActivity extends BaseActionBarActivity implements
		OnItemClickListener {

	private Context context;
	private ListView listView;
	private View mEmptyView;
	private ZKTopicListAdapter adapter;
	private DatabaseHelper databaseHelper = null;
	ArrayList<ZKTopic> lists = new ArrayList<>();
	private boolean isFormScan = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_favor);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(getString(R.string.favor));
		listView = (ListView) findViewById(R.id.lv_favorlilst);
		mEmptyView = findViewById(R.id.empty);
		adapter = new ZKTopicListAdapter(context, lists);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		isFormScan = getIntent().getBooleanExtra("flag", false);
		if (isFormScan) {
			String topicid = getIntent().getStringExtra("id");
			getDate(topicid);
		} else {
			getdatafromdb();
		}
	}

	/**
	 * 请求网络获取文章id
	 * 
	 * @param topicid
	 */
	private void getDate(String topicid) {
		customProgressDialog.show();
		HashMap<String, String> gparams = new HashMap<String, String>();
		gparams.put("key", topicid);
		gparams.put("type", "article");
		Log.i("param", "result:" + topicid + "article");
		VolleyManager.requestVolley(gparams, C.SERVER + C.URL_TOPIC_DETAIL,
				Method.POST, backlistener, errorListener, mQueue);

	}

	Listener<String> backlistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			if (customProgressDialog != null
					&& customProgressDialog.isShowing())
				customProgressDialog.dismiss();

			try {
				JudgeResult result = new JudgeResult(response);
				if (result.getState().endsWith("00")) {
					ZKTopic item = ZKTopic.formObject(response);
					// 插入数据库
					DBManager dao = new DBManager(AddFavorActivity.this);
					// 判断是否收藏
					if (dao.isFavoriteTopic(item)) {
						Toast.makeText(AddFavorActivity.this, "该文章已收藏", 1)
								.show();
						getdatafromdb();
					} else {
						// 插入数据库
						boolean isSucess = dao.saveTopic(item);
						if (!isSucess) {
							Toast.makeText(AddFavorActivity.this, "收藏失败", 1)
									.show();
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			getdatafromdb();
		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			finish();
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ZKTopic item = adapter.getList().get(position);
		if (item != null) {
			Bundle bundle = new Bundle();
			Intent _intent = new Intent(context, DetailContentActivity.class);
			bundle.putSerializable("item", item);
			_intent.putExtra("info", bundle);
			startActivityForResult(_intent, 1);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		getdatafromdb();
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
			Dao<ZKTopic, Integer> favorDao = getHelper().getFavorDao();
			ArrayList<ZKTopic> temp = (ArrayList<ZKTopic>) favorDao
					.queryBuilder().orderBy("datetime", false).query();
			Log.i("getdatafromdb", temp.size() + "");
			lists.clear();
			if (temp == null || temp.size() == 0) {
				mEmptyView.setVisibility(View.VISIBLE);
			} else {
				mEmptyView.setVisibility(View.GONE);
				lists.addAll(temp);
			}
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
