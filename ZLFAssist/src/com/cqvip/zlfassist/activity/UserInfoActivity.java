package com.cqvip.zlfassist.activity;

import java.util.Map;

import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.Volley;
import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.exception.ErrorVolleyThrow;
import com.cqvip.zlfassist.view.CustomProgressDialog;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class UserInfoActivity extends Activity{
	protected RequestQueue mQueue;
	protected ErrorListener errorListener;// 错误处理
	protected CustomProgressDialog customProgressDialog;// 对话框
	private Map<String, String> gparams;
	private TextView user_info_userNick,user_info_userName,user_info_userUnit;
	private SharedPreferences mySharedPreferences;
	private Button user_info_logout_btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		mQueue = Volley.newRequestQueue(this);
		customProgressDialog = CustomProgressDialog.createDialog(this);
		errorListener = new ErrorVolleyThrow(this, customProgressDialog);
		user_info_userNick=(TextView)findViewById(R.id.user_info_userNick);
		user_info_userName=(TextView)findViewById(R.id.user_info_userName);
		user_info_userUnit=(TextView)findViewById(R.id.user_info_userUnit);
		
		user_info_logout_btn=(Button)findViewById(R.id.user_info_logout_btn);
		user_info_logout_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor editor = mySharedPreferences.edit(); 
				editor.putBoolean("login_f", false);
				editor.commit();
				finish();
			}
		});
		
		mySharedPreferences= getSharedPreferences("user_info", 
				Activity.MODE_PRIVATE); 
		
		Boolean login_f=mySharedPreferences.getBoolean("login_f", false);
		if(login_f)
		{
			show_user_info();
		}
		else
		{
			show_user_logindlg();
		}
	}
	
	private void show_user_logindlg()
	{
		this.startActivityForResult(new Intent(UserInfoActivity.this,ActivityDlg.class), 1);
	}
	
	private void show_user_info()
	{
		try{
			String userstr=mySharedPreferences.getString("usernamejsonstr", "");
			JSONObject json = new JSONObject(userstr);
			user_info_userNick.setText("昵称："+json.getString("userNick"));
			user_info_userName.setText("账号:"+json.getString("userName"));
			user_info_userUnit.setText("单位:"+json.getString("userUnit"));
		}
		catch (Exception e) {
		
			show_user_logindlg();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==1)
		{
			show_user_info();
		}
		else
		{
			finish();
		}
	}
}