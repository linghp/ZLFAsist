package com.cqvip.zlfassist.activity;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.adapter.ZKTopicListAdapter;
import com.cqvip.zlfassist.base.BaseActionBarActivity;
import com.cqvip.zlfassist.bean.ItemFollows;
import com.cqvip.zlfassist.db.DatabaseHelper;
import com.cqvip.zlfassist.zkbean.ZKTopic;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

public class AddFavorActivity extends BaseActionBarActivity implements OnItemClickListener {

	private Context context;
	private ListView listView;
	private ZKTopicListAdapter adapter;
	private DatabaseHelper databaseHelper = null;
	ArrayList<ZKTopic> lists=new ArrayList<>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_favor);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("我的收藏");
		listView = (ListView) findViewById(R.id.lv_favorlilst);
		adapter = new ZKTopicListAdapter(context, lists);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		getdatafromdb();
	}
//	  private ArrayList<ZKTopic> formDate() {
//		  ArrayList<ZKTopic> lists = new ArrayList<>();
//		  ZKTopic  topic = new ZKTopic();
//		  topic.setId("123");
//		  topic.setTitleC("大肠癌细胞短期培养法筛选抗癌药物的初步探讨");
//		  topic.setShowwriter("张宗显;戴珊星");
//		  topic.setShoworgan("浙江省肿瘤医院");
//		  topic.setMediaC("癌症：英文版");
//		  topic.setMediasQk("989年第1期 63-64,共2页");
//		  topic.setRemarkC("浙江省肿瘤医院肿瘤研究所大肠癌细胞短期培养法筛选抗癌药物,");
//		  topic.setClasstypes("医药卫生—药品");
//		  topic.setClass_("R979.1");
//		  topic.setKeywordC("大肠癌细胞");
//		  lists.add(topic);
//		  lists.add(topic);
//		  lists.add(topic);
//		  lists.add(topic);
//		return lists;
//	}
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if(itemId == android.R.id.home){
			finish();
		}
		return false;
	    }
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ZKTopic item = adapter.getList().get(position);
		 if(item!=null){
				Bundle bundle = new Bundle();
				Intent _intent = new Intent(context,DetailContentActivity.class);
				bundle.putSerializable("item", item);
				_intent.putExtra("info", bundle);
				startActivity(_intent);
				
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
			Dao<ZKTopic, Integer> favorDao = getHelper().getFavorDao();
			ArrayList<ZKTopic> temp = (ArrayList<ZKTopic>) favorDao.queryForAll();
			Log.i("getdatafromdb", temp.size() + "");
			lists.clear();
			lists.addAll(temp);
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
