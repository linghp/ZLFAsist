package com.cqvip.zlfassist.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.JsonArray;
/**
 * 通用返回json格式，包含state返回状态吗，msg返回提示信息,result内容
 * @author luojiang
 *
 */
public class JudgeResult {
	    	  
	   private String msg; 
	   private String state; 
	   
	   public JudgeResult(String result) throws JSONException{
			JSONObject json = new JSONObject(result);
			msg = json.getString("msg");
			state = json.getString("state");
	   }
	public String getMsg() {
		return msg;
	}
	public String getState() {
		return state;
	}

		   
}
