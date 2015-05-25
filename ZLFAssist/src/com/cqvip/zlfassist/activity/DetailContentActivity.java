package com.cqvip.zlfassist.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.artifex.mupdfdemo.MuPDFActivity;
import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.base.BaseActionBarActivity;
import com.cqvip.zlfassist.bean.DownloaderSimpleInfo;
import com.cqvip.zlfassist.bean.JudgeResult;
import com.cqvip.zlfassist.constant.C;
import com.cqvip.zlfassist.db.DBManager;
import com.cqvip.zlfassist.db.DatabaseHelper;
import com.cqvip.zlfassist.download.DownloadList;
import com.cqvip.zlfassist.exception.ErrorVolleyThrow;
import com.cqvip.zlfassist.http.VolleyManager;
import com.cqvip.zlfassist.tools.BaseTools;
import com.cqvip.zlfassist.view.CustomProgressDialog;
import com.cqvip.zlfassist.zkbean.ZKContent;
import com.cqvip.zlfassist.zkbean.ZKTopic;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.mozillaonline.providers.DownloadManager;
import com.mozillaonline.providers.DownloadManager.Request;

public class DetailContentActivity extends BaseActionBarActivity implements
		OnClickListener {
	private final String LOG_TAG = getClass().getSimpleName();
	private TextView title, author, organ, comeform, abst, keyword, classid,
			tips;
	private ZKContent topic;
	private String requestId;
	private ZKTopic zkTopic;
	private TextView shareTextView, readTextView, favorTextView;
	private ArrayList<ZKTopic> zkTopics_list = new ArrayList<>();
	private static final String[] SHOWTIPS = { "作者：", "机构：", "出处：", "关键词：",
			"分类号：" };
	private DatabaseHelper databaseHelper = null;
	private MenuItem menuItem_favor;
	private boolean isfavor = false;
	private boolean isCanRead = false;// 是否可以阅读
	

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
		if (!TextUtils.isEmpty(zkTopic.getRemarkC())) {
			tips.setVisibility(View.VISIBLE);
			abst.setText(zkTopic.getRemarkC());
		} else {
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
		DBManager manager = new DBManager(this);
		if (manager.isFavoriteTopic(zkTopic)) {
			changeDisplayContent_Favor("取消收藏",R.drawable.favored);
		}
		// else{
		// favorTextView.setText("收藏");
		// }
		if (manager.isDownload(requestId)) {
			isCanRead = true;
		}
	}

	private void changeDisplayContent_Favor(String title,int id) {
		favorTextView.setText(title);
		Drawable drawable = getResources().getDrawable(id);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());// 必须设置图片大小，否则不显示
		favorTextView.setCompoundDrawables(null, drawable, null, null);
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
				//Log.i("backlistener", "response=======" + response);
				if (result.getState().equals("00")) {
					topic = ZKContent.formObject(response);
					//Log.i("topic", topic.toString());
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
			//Log.i(LOG_TAG, "isFavor");
			for (ZKTopic zkTopic : zkTopics_list) {
				if (zkTopic.getId().equals(topic.getId())
						&& zkTopic.getType().equals(topic.getType())) {
					isfavor = true;
					return;
				}
			}
			isfavor = false;
			;
		}

	};

	

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.detail_content, menu);
	// menuItem_favor=menu.findItem(R.id.action_addfavor);
	// return true;
	// }
	//
	// @Override
	// public boolean onPrepareOptionsMenu(Menu menu) {
	// // TODO Auto-generated method stub
	// if(isfavor){
	// menuItem_favor.setTitle(R.string.action_cancelfavor);
	// }else{
	// menuItem_favor.setTitle(R.string.action_addfavor);
	// }
	// return super.onPrepareOptionsMenu(menu);
	// }

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
			if (isfavor) {
				deleteDB(zkTopic);
			} else {
				saveDB(zkTopic);
			}
			return true;
		}
		if (id == android.R.id.home) {

			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void queryDB() {
		try {
			Dao<ZKTopic, Integer> favorDao = getHelper().getFavorDao();
			zkTopics_list = (ArrayList<ZKTopic>) favorDao.queryForAll();
			for (ZKTopic zkTopic : zkTopics_list) {
				//Log.i(LOG_TAG + "--getdatafromdb", zkTopic.getTitleC());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void saveDB(ZKTopic zkTopic) {
		try {
			Dao<ZKTopic, Integer> favorDao = getHelper().getFavorDao();
			// ItemFollows itemFollows = new ItemFollows();
			// store it in the database
			// itemFollows.setDatetime(System.currentTimeMillis());
			favorDao.create(zkTopic);
			Toast.makeText(this, "收藏成功", 1).show();
			changeDisplayContent_Favor("取消收藏",R.drawable.favored);
			isfavor = true;
		} catch (SQLException e) {
			e.printStackTrace();
			Toast.makeText(this, "收藏失败", 1).show();
		}
	}

	protected void deleteDB(ZKTopic zkTopic) {
		try {
			Dao<ZKTopic, Integer> favorDao = getHelper().getFavorDao();
			// ItemFollows itemFollows = new ItemFollows();
			// store it in the database
			// itemFollows.setDatetime(System.currentTimeMillis());
			favorDao.delete(zkTopic);
			Toast.makeText(this, "取消收藏成功", 1).show();
			changeDisplayContent_Favor("收藏", R.drawable.favor);
			isfavor = false;
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
	
	private String getuserid()
	{
		SharedPreferences mySharedPreferences=getSharedPreferences("user_info", 
				Activity.MODE_PRIVATE); 	
		try{
		JSONObject json = new JSONObject(mySharedPreferences.getString("usernamejsonstr", ""));
		 return json.getString("userid");
		}catch (Exception e)
		{
			return "";
		}
	}
	
	private void sendDownloadUrlCheck()
	{
		Map<String, String> gparams = new HashMap<String, String>();
		gparams.put("userid", getuserid());
		gparams.put("id", zkTopic.getId());

		VolleyManager.requestVolley(gparams, C.SERVER +C.URL_DOWNLOAD,
				Method.POST,dlurlbacklistener,  errorListener, mQueue);
	}
	
	Listener<String> dlurlbacklistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			if (customProgressDialog != null
					&& customProgressDialog.isShowing())
				customProgressDialog.dismiss();
			try {
				JSONObject result = new JSONObject(response);
				if (result.getInt("state")==0) {
					result=result.getJSONObject("result");
					downloadfile(result.getString("url"));
				} else {
					Toast.makeText(DetailContentActivity.this, result.getString("msg"), Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	private void saveDB(DownloaderSimpleInfo downloaderSimpleInfo) {
		try {
			Dao<DownloaderSimpleInfo, Integer> downloaderSimpleInfoDao = getHelper()
					.getDownloaderSimpleInfoDao();
			downloaderSimpleInfoDao.create(downloaderSimpleInfo);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).saveUserChannel(userAdapter.getChannnelLst());
	}
	 private long startDownload(String url) {
			//String url = "http://www.pptok.com/wp-content/uploads/2012/06/huanbao-1.jpg";
			//url = "http://www.it.com.cn/dghome/img/2009/06/23/17/090623_tv_tf2_13h.jpg";
			//String url = "http://down.mumayi.com/41052/mbaidu";
			Uri srcUri = Uri.parse(url);
			DownloadManager.Request request = new Request(srcUri);
			request.setDestinationInExternalPublicDir(
				Environment.DIRECTORY_DOWNLOADS, "/");
			request.setDescription("正在下载");
			 DownloadManager mDownloadManager = new DownloadManager(getContentResolver(),
					 getPackageName());
			return mDownloadManager.enqueue(request);
		    }
	 
	 public void  downloadfile(String url)
	 {
			long downloadid=startDownload(url);
			DownloaderSimpleInfo downloaderSimpleInfo=new DownloaderSimpleInfo(downloadid,zkTopic.getId(), zkTopic.getTitleC(), url);
			saveDB(downloaderSimpleInfo);
			//Log.i("captureact", name+"--"+id+"--"+url);
			//跳转
			Intent intent_download = new Intent(this,DownloadList.class);
			startActivity(intent_download);
			finish();
	 }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_item_favor:
			if (isfavor) {
				deleteDB(zkTopic);
			} else {
				saveDB(zkTopic);
			}
			break;
		case R.id.btn_item_read:
			if (isCanRead) {
				DBManager manager = new DBManager(DetailContentActivity.this);
				String path = manager.getReadPath(requestId);
				Uri uri = Uri.parse(path);
				Intent intent = new Intent(this, MuPDFActivity.class);
				intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
				intent.setAction(Intent.ACTION_VIEW);
				intent.setData(uri);
				startActivity(intent);
			} else {
				sendDownloadUrlCheck();
			}
			break;
		case R.id.btn_item_share:
			BaseTools.bookshare_bysharesdk(this, zkTopic, null);
			break;

		default:
			break;
		}

	}
}
