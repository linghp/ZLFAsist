package com.cqvip.zlfassist.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.adapter.ZKTopicListAdapter;
import com.cqvip.zlfassist.constant.C;
import com.cqvip.zlfassist.exception.ErrorVolleyThrow;
import com.cqvip.zlfassist.http.VolleyManager;
import com.cqvip.zlfassist.view.CustomProgressDialog;
import com.cqvip.zlfassist.zkbean.ZKTopic;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class ActivityDlg extends Activity  {
	protected RequestQueue mQueue;
	protected ErrorListener errorListener;// 错误处理
	protected CustomProgressDialog customProgressDialog;// 对话框
	private Map<String, String> gparams;
	
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_dlg);
	mQueue = Volley.newRequestQueue(this);
	customProgressDialog = CustomProgressDialog.createDialog(this);
	errorListener = new ErrorVolleyThrow(this, customProgressDialog);
	userlogin("cy","cy");
}

private void userlogin( String username,String pws) {
	gparams = new HashMap<String, String>();
	gparams.put("userid", username);
	gparams.put("password", pws);

	VolleyManager.requestVolley(gparams, C.SERVER +C.URL_LOGIN,
			Method.POST,backlistener,  errorListener, mQueue);
}

Listener<String> backlistener = new Listener<String>() {
	@Override
	public void onResponse(String response) {
		// TODO Auto-generated method stub
		
		try {
			
				return;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
};
	
}
