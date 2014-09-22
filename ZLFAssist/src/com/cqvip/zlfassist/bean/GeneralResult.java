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
public class GeneralResult {
	    	  
	   private String msg; 
	   private String state; 
	   private  String result; 
	   
	   public GeneralResult(String result) throws JSONException{
			JSONObject json = new JSONObject(result);
			msg = json.getString("msg");
			state = json.getString("state");
			this.result = json.getString("result");
	   }
	   public static String formResult(String result) throws JSONException{
		 GeneralResult objectGeneralResult =new  GeneralResult(result);
		 String resString = objectGeneralResult.getResult();
		 Log.i("strr",resString);
		 return resString;
	   }
	public String getMsg() {
		return msg;
	}
	public String getState() {
		return state;
	}
	public String getResult() {
		return result;
	}

		   
}
