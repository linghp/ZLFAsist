package com.cqvip.zlfassist.activity;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.base.BaseActionBarActivity;
import com.cqvip.zlfassist.bean.JudgeResult;
import com.cqvip.zlfassist.constant.C;
import com.cqvip.zlfassist.http.VolleyManager;
import com.cqvip.zlfassist.tools.BaseTools;
import com.cqvip.zlfassist.zkbean.ZKContent;
import com.cqvip.zlfassist.zkbean.ZKTopic;

public class DetailContentActivity extends BaseActionBarActivity {

	private TextView title, author, organ, comeform, abst, keyword, classid,tips;
	private ZKContent topic;
	private String requestId;
	private ZKTopic zkTopic;
	private static final String[] SHOWTIPS = { "作者：", "机构：", "出处：", "关键词：",
			"分类号：" };

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
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == android.R.id.home) {
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
