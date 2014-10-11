package com.cqvip.zlfassist.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.base.BaseActionBarActivity;
import com.cqvip.zlfassist.bean.ItemFollows;
import com.cqvip.zlfassist.bean.JudgeResult;
import com.cqvip.zlfassist.constant.C;
import com.cqvip.zlfassist.db.DatabaseHelper;
import com.cqvip.zlfassist.http.VolleyManager;
import com.cqvip.zlfassist.tools.BaseTools;
import com.cqvip.zlfassist.zkbean.ZKContent;
import com.cqvip.zlfassist.zkbean.ZKTopic;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

public class DetailContentActivity extends BaseActionBarActivity implements OnClickListener {
	private final String LOG_TAG = getClass().getSimpleName();
	private TextView title, author, organ, comeform, abst, keyword, classid,tips;
	private ZKContent topic;
	private String requestId;
	private ZKTopic zkTopic;
	private TextView shareTextView ,readTextView,favorTextView;
	private ArrayList<ZKTopic> zkTopics_list=new ArrayList<>();
	private static final String[] SHOWTIPS = { "作者：", "机构：", "出处：", "关键词：",
			"分类号：" };
	private DatabaseHelper databaseHelper = null;
	private MenuItem menuItem_favor;
	private boolean isfavor =false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_content);
		getSupportActionBar().setTitle("详细");
		Bundle bundle = getIntent().getBundleExtra("info");
		zkTopic = (ZKTopic) bundle.getSerializable("item");
		requestId = zkTopic.getId();
		intiView();
		initfirstView();
		queryDB();
		getDate(requestId);
		
	}

	private void initfirstView() {
		title.setText(zkTopic.getTitleC());
		author.setText(BaseTools.formaddTips(zkTopic.getShowwriter(),
				SHOWTIPS[0]));
		organ.setText(BaseTools.formaddTips(zkTopic.getShoworgan(), SHOWTIPS[1]));
		comeform.setText(BaseTools.formaddTips((BaseTools.formPero(zkTopic
				.getMediaC()) + BaseTools.formSecendContent(zkTopic
				.getMediasQk())), SHOWTIPS[2]));
		keyword.setText(BaseTools.formaddTips(zkTopic.getKeywordC(),
				SHOWTIPS[3]));
		classid.setText(BaseTools.formaddTips((BaseTools.formContent(zkTopic
				.getClass_()) + BaseTools.formaaddBracket(zkTopic
				.getClasstypes())), SHOWTIPS[4]));
		if(!TextUtils.isEmpty(zkTopic.getRemarkC())){
			tips.setVisibility(View.VISIBLE);
			abst.setText(zkTopic.getRemarkC());
		}else{
			tips.setVisibility(View.GONE);
		}
	}

	private void intiView() {
		title = (TextView) findViewById(R.id.tv_title);
		author = (TextView) findViewById(R.id.tv_detail_author);
		organ = (TextView) findViewById(R.id.tv_detail_orgniziton);
		comeform = (TextView) findViewById(R.id.tv_detail_comefrom);
		abst = (TextView) findViewById(R.id.tv_detail_abstract);
		keyword = (TextView) findViewById(R.id.tv_detail_keyword);
		classid = (TextView) findViewById(R.id.tv_detail_classno);
		tips = (TextView) findViewById(R.id.tv_tips_abst);
		shareTextView = (TextView) findViewById(R.id.btn_item_share);
		favorTextView = (TextView) findViewById(R.id.btn_item_favor);
		readTextView = (TextView) findViewById(R.id.btn_item_read);
		shareTextView.setOnClickListener(this);
		favorTextView.setOnClickListener(this);
		readTextView.setOnClickListener(this);
	}

	private void getDate(String requestId2) {
		customProgressDialog.show();
		// http://192.168.20.57:9999/getinfo.ashx?type=article&key=123
		Map<String, String> gparams = new HashMap<>();
		gparams.put("type", "article");
		gparams.put("key", requestId2);
		VolleyManager.requestVolley(gparams, C.SERVER + C.URL_TOPIC_DETAIL,
				Method.POST, backlistener, errorListener, mQueue);

	}

	Listener<String> backlistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			if (customProgressDialog != null
					&& customProgressDialog.isShowing())
				customProgressDialog.dismiss();
			try {
				JudgeResult result = new JudgeResult(response);
				Log.i("backlistener", "response=======" + response);
				if (result.getState().equals("00")) {
					topic = ZKContent.formObject(response);
					Log.i("topic", topic.toString());
					if (topic != null) {
						isFavor(topic);
						// setView();
					}
				} else {
					Toast.makeText(DetailContentActivity.this, result.getMsg(),
							1).show();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(DetailContentActivity.this, "解析错误", 1).show();
			}

		}

		private void isFavor(ZKContent topic) {
			Log.i(LOG_TAG, "isFavor");
			for (ZKTopic zkTopic : zkTopics_list) {
				if(zkTopic.getId().equals(topic.getId())&&zkTopic.getType().equals(topic.getType())){
					isfavor=true;
					return;
				}
			}
				isfavor=false;;
		}

	};
	
	

	private void setView() {
		title.setText(topic.getTitleC());
		author.setText(SHOWTIPS[0] + topic.getShowwriter());
		organ.setText(SHOWTIPS[1] + topic.getShoworgan());
		comeform.setText(SHOWTIPS[2] + "《" + topic.getMediaC() + "》"
				+ topic.getMediasQk());
		abst.setText(SHOWTIPS[3] + topic.getRemarkC());
		keyword.setText(SHOWTIPS[4] + topic.getKeywordC());
		classid.setText(SHOWTIPS[5] + topic.getClass_() + topic.getClasstypes());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail_content, menu);
		menuItem_favor=menu.findItem(R.id.action_addfavor);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		if(isfavor){
			menuItem_favor.setTitle(R.string.action_cancelfavor);
		}else{
			menuItem_favor.setTitle(R.string.action_addfavor);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		int id = item.getItemId();
		if (id == R.id.action_share) {
			BaseTools.bookshare_bysharesdk(this, zkTopic, null);
			return true;
		}
		if (id == R.id.action_addfavor) {
			if(isfavor){
			deleteDB(zkTopic);
			}else{
			saveDB(zkTopic);
			}
			return true;
		}
		if( id == android.R.id.home){

			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
	private void queryDB() {
		try {
			Dao<ZKTopic, Integer> favorDao = getHelper()
					.getFavorDao();
			zkTopics_list =  (ArrayList<ZKTopic>) favorDao.queryForAll();
			for (ZKTopic zkTopic : zkTopics_list) {
				Log.i(LOG_TAG + "--getdatafromdb", zkTopic.getTitleC());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected void saveDB(ZKTopic zkTopic) {
		try {
			Dao<ZKTopic, Integer> favorDao = getHelper()
					.getFavorDao();
			// ItemFollows itemFollows = new ItemFollows();
			// store it in the database
			//itemFollows.setDatetime(System.currentTimeMillis());
			favorDao.create(zkTopic);
			Toast.makeText(this, "收藏成功", 1).show();
			isfavor=true;
		} catch (SQLException e) {
			e.printStackTrace();
			Toast.makeText(this, "收藏失败", 1).show();
		}
	}
	
	protected void deleteDB(ZKTopic zkTopic) {
		try {
			Dao<ZKTopic, Integer> favorDao = getHelper()
					.getFavorDao();
			// ItemFollows itemFollows = new ItemFollows();
			// store it in the database
			//itemFollows.setDatetime(System.currentTimeMillis());
			favorDao.delete(zkTopic);
			Toast.makeText(this, "取消收藏成功", 1).show();
			isfavor=false;
		} catch (SQLException e) {
			e.printStackTrace();
			Toast.makeText(this, "取消收藏失败", 1).show();
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_item_favor:
			if(isfavor){
				deleteDB(zkTopic);
				}else{
				saveDB(zkTopic);
				}
			break;
		case R.id.btn_item_read:
			
			break;
		case R.id.btn_item_share:
			BaseTools.bookshare_bysharesdk(this, zkTopic, null);
			break;

		default:
			break;
		}
		
		
	}
}
