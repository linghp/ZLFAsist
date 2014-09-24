package com.cqvip.zlfassist.bean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

/**
 * 总分类
 * @author luojiang
 *
 */
public class TopItem {
	private String name;
	private String type;
	
	public TopItem() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ShortItem [Name=" + name + ", Type=" + type + "]";
	}

	public static ArrayList<TopItem> formList(String result) throws JSONException{
		GeneralResult gr = new GeneralResult(result);
		String res = gr.getResult();
		if(!TextUtils.isEmpty(res)){
		JSONObject json = new JSONObject(gr.getResult());
		Type listType = new TypeToken<ArrayList<TopItem>>(){}.getType();
		ArrayList<TopItem> lists= new Gson().fromJson(json.getString("list"), listType);
		return lists;
		}
		return null;
		
	}
	

	
}
