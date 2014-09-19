package com.cqvip.zlfassist.activity;

import org.json.JSONException;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.R.id;
import com.cqvip.zlfassist.base.BaseActionBarActivity;
import com.cqvip.zlfassist.bean.GeneralResult;
import com.cqvip.zlfassist.bean.TopicContent;
import com.cqvip.zlfassist.constant.C;
import com.cqvip.zlfassist.http.VolleyManager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class DetailContentActivity extends BaseActionBarActivity {

	private TextView title,author,organ,comeform,abst,keyword,classid;
	private TopicContent topic ;
	private static final String[] SHOWTIPS = {"作者","机构地区","出处","摘要","关键词","分类号"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_content);
		getSupportActionBar().setTitle("详细");
		intiView();
		getDate();
	}

	private void intiView() {
		title = (TextView) findViewById(R.id.tv_title);
		author = (TextView) findViewById(R.id.tv_detail_author);
		organ = (TextView) findViewById(R.id.tv_detail_orgniziton);
		comeform = (TextView) findViewById(R.id.tv_detail_comefrom);
		abst = (TextView) findViewById(R.id.tv_detail_abstract);
		keyword = (TextView) findViewById(R.id.tv_detail_keyword);
		classid = (TextView) findViewById(R.id.tv_detail_classno);
		
	}

	private void getDate() {
		customProgressDialog.show();
			VolleyManager.requestVolley(null, C.SERVER_Content,Method.GET, backlistener, errorListener, mQueue);
		
	}

	Listener<String> backlistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
			customProgressDialog.dismiss();
			
			try {
				//GeneralResult result = new GeneralResult(response);
				Log.i("backlistener","response======="+response);
				 topic = new Gson().fromJson(GeneralResult.formResult(response), TopicContent.class); 
				// topic = new Gson().fromJson(GeneralResult.formResult(response), TopicContent.class);
				 Log.i("topic", topic.toString());
				 if(topic!=null){
				 setView();
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
		author.setText(SHOWTIPS[0]+topic.getShowwriter());
		organ.setText(SHOWTIPS[1]+topic.getShoworgan());
		comeform.setText(SHOWTIPS[2]+"《"+topic.getMediaC()+"》"+topic.getMediasQk());
		abst.setText(SHOWTIPS[3]+topic.getRemarkC());
		keyword.setText(SHOWTIPS[4]+topic.getKeywordC());
		classid.setText(SHOWTIPS[5]+topic.getClass_()+topic.getClasstypes());
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
		if( id == android.R.id.home){
			finish();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
