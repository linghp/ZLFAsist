package com.cqvip.zlfassist.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.base.BaseActivity;
import com.cqvip.zlfassist.bean.PeriodicalResult;
import com.cqvip.zlfassist.constant.C;
import com.google.gson.Gson;

public class WelcomActivity extends BaseActivity {
	private static final String JSON_DATA = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcom);
	}
	public void begin(View v){
		startActivity(new Intent(WelcomActivity.this,MainActivity.class));
		finish();
	}
	public void json(View v) throws JSONException{
		JSONObject json = new JSONObject();
		json.put("id", 1000);
		json.put("body", "小黑");
		json.put("number", 0.13);
		json.put("created_at", "1989-05-15");
		String jsondata = json.toString();
	//	System.out.println(jsondata);
		Gson gson = new Gson();
		Foo foo = gson.fromJson(jsondata, Foo.class);
		request();
	}

	public class Foo {
	    public int id;
	    public String body;
	    public float number;
	    public String created_at;
		public int getId() {
			return id;
		}
		public String getBody() {
			return body;
		}
		public float getNumber() {
			return number;
		}
		public String getCreated_at() {
			return created_at;
		}
		@Override
		public String toString() {
			return "Foo [id=" + id + ", body=" + body + ", number=" + number
					+ ", created_at=" + created_at + "]";
		}
	    
	}
	
	
	private void request() {
		
			// 网络访问,获取首页
			customProgressDialog.show();
			int page = 1;
			getData(page);
	}


	private void getData(int page) {
		Map<String, String> gparams = new HashMap<String, String>();
		Listener<String> listner;
			listner = backlistener;
		requestVolley(null, C.BASE, listner, Method.POST);
	}

	private void requestVolley(final Map<String, String> gparams, String url,
			Listener<String> listener, int post) {
		StringRequest mys = new StringRequest(post, url, listener,
				errorListener) {
			protected Map<String, String> getParams()
					throws com.android.volley.AuthFailureError {
				return gparams;
			};
		};
	//	mys.setRetryPolicy(HttpUtils.setTimeout());
		mQueue.add(mys);
	}

	private Listener<String> backlistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			customProgressDialog.dismiss();
			PeriodicalResult result = new Gson().fromJson(response, PeriodicalResult.class);
			
			System.out.println(result.getQklist().get(1));
		}
	};
}
