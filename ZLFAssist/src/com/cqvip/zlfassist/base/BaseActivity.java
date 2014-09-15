package com.cqvip.zlfassist.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.Volley;
import com.cqvip.zlfassist.exception.ErrorVolleyThrow;
import com.cqvip.zlfassist.view.CustomProgressDialog;

/**
 * Activity基类
 * @author luojiang
 *
 */
public class BaseActivity extends Activity {
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
}
