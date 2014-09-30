package com.cqvip.zlfassist.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.adapter.ZKTopicListAdapter;
import com.cqvip.zlfassist.base.BaseActionBarActivity;
import com.cqvip.zlfassist.zkbean.ZKTopic;

public class NotifactionUpdateActivity extends BaseActionBarActivity implements OnItemClickListener {

	private Context context;
	private ListView listView;
	private ZKTopicListAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
 		setContentView(R.layout.activity_notification);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("文章引用通知");
		listView = (ListView) findViewById(R.id.lv_notification);
		ArrayList<ZKTopic> lists =formDate();
		adapter = new ZKTopicListAdapter(context, lists);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}
	
	
	  private ArrayList<ZKTopic> formDate() {
		  ArrayList<ZKTopic> lists = new ArrayList<>();
		  ZKTopic  topic = new ZKTopic();
		  topic.setId("123");
		  topic.setTitleC("大肠癌细胞短期培养法筛选抗癌药物的初步探讨");
		  topic.setShowwriter("张宗显;戴珊星");
		  topic.setShoworgan("浙江省肿瘤医院");
		  topic.setMediaC("癌症：英文版");
		  topic.setMediasQk("989年第1期 63-64,共2页");
		  topic.setRemarkC("浙江省肿瘤医院肿瘤研究所大肠癌细胞短期培养法筛选抗癌药物,");
		  topic.setClasstypes("医药卫生—药品");
		  topic.setClass_("R979.1");
		  topic.setKeywordC("大肠癌细胞");
		  lists.add(topic);
		  lists.add(topic);
		  lists.add(topic);
		  lists.add(topic);
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
		 if(item!=null){
				Bundle bundle = new Bundle();
				Intent _intent = new Intent(context,DetailContentActivity.class);
				bundle.putSerializable("item", item);
				_intent.putExtra("info", bundle);
				startActivity(_intent);
				
			}
		
	}
}
