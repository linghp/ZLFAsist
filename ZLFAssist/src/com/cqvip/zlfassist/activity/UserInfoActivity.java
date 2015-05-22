package com.cqvip.zlfassist.activity;

import java.util.Map;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.Volley;
import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.exception.ErrorVolleyThrow;
import com.cqvip.zlfassist.view.CustomProgressDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class UserInfoActivity extends Activity{
	protected RequestQueue mQueue;
	protected ErrorListener errorListener;// 错误处理
	protected CustomProgressDialog customProgressDialog;// 对话框
	private Map<String, String> gparams;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		mQueue = Volley.newRequestQueue(this);
		customProgressDialog = CustomProgressDialog.createDialog(this);
		errorListener = new ErrorVolleyThrow(this, customProgressDialog);
		
		this.startActivity(new Intent(UserInfoActivity.this,ActivityDlg.class));
	}
}