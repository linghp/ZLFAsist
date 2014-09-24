package com.cqvip.zlfassist.bean;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

public class ParseManager<T> {
	
	public   ArrayList<T> formList(String result) throws JSONException{
		GeneralResult gr = new GeneralResult(result);
		String res = gr.getResult();
		if(!TextUtils.isEmpty(res)){
			JSONObject json = new JSONObject(gr.getResult());
			Type listType = new TypeToken<ArrayList<T>>(){}.getType();
			ArrayList<T> lists= new Gson().fromJson(json.getString("list"), listType);
			return lists;
		}
		return null;
	}

	}