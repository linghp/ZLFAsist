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
import com.j256.ormlite.field.DatabaseField;

/**
 * 子分类
 * @author luojiang
 *
 */
public class TopSubItem {
	@DatabaseField
	private String name;
	@DatabaseField
	private String type;
	@DatabaseField
	private String id;
	@DatabaseField
	private long time= System.currentTimeMillis();
	
	public TopSubItem() {
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

	public static ArrayList<TopSubItem> formList(String result) throws JSONException{
		GeneralResult gr = new GeneralResult(result);
		String res = gr.getResult();
		if(!TextUtils.isEmpty(res)){
		JSONObject json = new JSONObject(gr.getResult());
		Type listType = new TypeToken<ArrayList<TopSubItem>>(){}.getType();
		ArrayList<TopSubItem> lists= new Gson().fromJson(json.getString("list"), listType);
		return lists;
		}
		return null;
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(long time) {
		this.time = time;
	}
	

	
}
