package com.cqvip.zlfassist.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.adapter.ZKTopicListAdapter;
import com.cqvip.zlfassist.constant.C;
import com.cqvip.zlfassist.exception.ErrorVolleyThrow;
import com.cqvip.zlfassist.http.VolleyManager;
import com.cqvip.zlfassist.view.CustomProgressDialog;
import com.cqvip.zlfassist.zkbean.ZKTopic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

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
	private View log_in_layout;
	private AutoCompleteTextView log_in_username;
	private EditText log_in_passwords;
	private Button login_cancel_btn,login_ok_btn;
	
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_dlg);
	mQueue = Volley.newRequestQueue(this);
	customProgressDialog = CustomProgressDialog.createDialog(this);
	errorListener = new ErrorVolleyThrow(this, customProgressDialog);
	log_in_layout=(View)findViewById(R.id.log_in_layout);
	log_in_layout.setVisibility(View.VISIBLE);
	log_in_username=(AutoCompleteTextView)findViewById(R.id.log_in_username);
	log_in_passwords=(EditText)findViewById(R.id.log_in_passwords);
	login_cancel_btn=(Button)findViewById(R.id.login_cancel_btn);
	login_ok_btn=(Button)findViewById(R.id.login_ok_btn);
	
	log_in_username.setText("cy");
	log_in_passwords.setText("cy");
	
	login_cancel_btn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			setResult(0);
			finish();	
		}
	});
	login_ok_btn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(log_in_username.getText().toString().length()>0&&log_in_passwords.getText().toString().length()>0)
			{
			userlogin(log_in_username.getText().toString(),log_in_passwords.getText().toString());
			}
			else
			{
				Toast.makeText(ActivityDlg.this, "请填写 用户名和密码。", Toast.LENGTH_LONG).show();
			}
		}
	});
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
				JSONObject json = new JSONObject(response);
				if(json.getInt("state")==0)
				{
					JSONObject tmp=json.getJSONObject("result");
					tmp=tmp.getJSONObject("obj");
					SharedPreferences mySharedPreferences= getSharedPreferences("user_info", 
							Activity.MODE_PRIVATE); 
					SharedPreferences.Editor editor = mySharedPreferences.edit(); 
					editor.putString("usernamejsonstr", tmp.toString());
					editor.putBoolean("login_f", true); 
					editor.putString("pws", log_in_passwords.getText().toString()); 
					editor.commit(); 
					setResult(1);
					ActivityDlg.this.finish();
				}
				else
				{
					Toast.makeText(ActivityDlg.this,json.getString("msg"), Toast.LENGTH_LONG).show();
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
};
	
}
