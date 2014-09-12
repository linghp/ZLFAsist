package com.cqvip.zlfassist.http;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RetryPolicy;
import com.cqvip.zlfassist.constant.C;



public class HttpUtils  {
	
	/**
	 * vollery设置超时20s
	 * @return
	 */
	public static RetryPolicy setTimeout(){
		RetryPolicy retryPolicy = new DefaultRetryPolicy(C.SOCKET_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		return retryPolicy;
	}
}
