package com.cqvip.zlfassist.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

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
import com.cqvip.zlfassist.db.DBManager;
import com.cqvip.zlfassist.zkbean.ZKTopic;
import com.j256.ormlite.dao.Dao;

public class NotifactionUpdateActivity extends BaseActionBarActivity implements OnItemClickListener {

	private Context context;
	private ListView listView;
	private ZKTopicListAdapter adapter;
	private HashMap<String, Boolean> allIds = null;
	private 	ArrayList<ZKTopic> updateList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
 		setContentView(R.layout.activity_notification);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		listView = (ListView) findViewById(R.id.lv_notification);
		allIds = (HashMap<String, Boolean>) getIntent().getSerializableExtra("ids");
		DBManager man = new DBManager(this);
		if(allIds!=null&&!allIds.isEmpty()){
		 updateList =sortLists(man.queryFavorits(),allIds);
		}else{
			updateList = man.queryFavorits();
		}
		adapter = new ZKTopicListAdapter(context, updateList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	
	}
	private ArrayList<ZKTopic> sortLists(ArrayList<ZKTopic> lists, HashMap<String,Boolean> updates) {
		for (Map.Entry<String, Boolean> entry : updates.entrySet()) {
				if(entry.getValue()){
						for (int i = 0; i < lists.size(); i++) {
								if(entry.getKey().equals(lists.get(i).getId())){
										lists.get(i).setNew(true);
								}
						}
					}
			  }
		 Comparator<ZKTopic> comparator = new Comparator<ZKTopic>(){

				@Override
				public int compare(ZKTopic s1, ZKTopic s2) {
						    //先排年龄
							   if(s1.isNew()!=s2.isNew()){
								   if(s1.isNew()){
									   return -1;
								   }else{
									   return 1;
								   }
							   }else{
								   return -1;
							   }
				}
				  };
				  Collections.sort(lists,comparator);
		return lists;
	}

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
		item.setNew(false);
		adapter.notifyDataSetChanged();
		 if(item!=null){
				Bundle bundle = new Bundle();
				Intent _intent = new Intent(context,DetailContentActivity.class);
				bundle.putSerializable("item", item);
				_intent.putExtra("info", bundle);
				startActivity(_intent);
				
			}
		
	}
}
