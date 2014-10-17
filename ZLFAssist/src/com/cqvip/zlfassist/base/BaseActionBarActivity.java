package com.cqvip.zlfassist.base;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.Volley;
import com.cqvip.zlfassist.exception.ErrorVolleyThrow;
import com.cqvip.zlfassist.view.CustomProgressDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * Activity基类
 * @author luojiang
 *
 */
public class BaseActionBarActivity extends ActionBarActivity {
	/**网络模块*/
	protected RequestQueue mQueue;
	protected ErrorListener errorListener;// 错误处理
	protected CustomProgressDialog customProgressDialog;// 对话框

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		mQueue = Volley.newRequestQueue(this);
		customProgressDialog = CustomProgressDialog.createDialog(this);
		errorListener = new ErrorVolleyThrow(this, customProgressDialog);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onDestroy() {
		if(customProgressDialog!=null&&customProgressDialog.isShowing()){
			customProgressDialog.dismiss();
		}
		super.onDestroy();
	}
}
