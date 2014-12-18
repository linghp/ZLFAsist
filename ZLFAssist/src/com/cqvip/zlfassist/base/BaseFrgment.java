package com.cqvip.zlfassist.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.Volley;
import com.cqvip.zlfassist.bitmap.BitmapCache;
import com.cqvip.zlfassist.exception.ErrorVolleyThrow;
import com.cqvip.zlfassist.view.CustomProgressDialog;

/**
 * Fragment基类
 * @author luojiang
 *
 */
public class BaseFrgment extends Fragment {
	/**网络模块*/
	protected RequestQueue mQueue;
	protected ErrorListener volleyErrorListener;// 错误处理
	protected CustomProgressDialog customProgressDialog;// 对话框
	protected ErrorListener errorListener;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		mQueue = Volley.newRequestQueue(getActivity());
		customProgressDialog = CustomProgressDialog.createDialog(getActivity());
		volleyErrorListener = new ErrorVolleyThrow(getActivity(), customProgressDialog);
		errorListener = new  ErrorVolleyThrow(getActivity(), customProgressDialog);
	}
}
