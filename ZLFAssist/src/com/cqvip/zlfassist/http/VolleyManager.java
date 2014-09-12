package com.cqvip.zlfassist.http;

import java.util.Map;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

public class VolleyManager {

	public static  void requestVolley(final Map<String, String> gparams,String addr,int method ,Listener<String> bl, ErrorListener el,RequestQueue mQueue) {
		try {
			StringRequest mys = new StringRequest(method, addr, bl, el) {
				protected Map<String, String> getParams()
						throws com.android.volley.AuthFailureError {
					return gparams;
				};
			};
			mys.setRetryPolicy(HttpUtils.setTimeout());
			mQueue.add(mys);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
