package com.cqvip.zlfassist.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.cqvip.zlfassist.constant.C;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.j256.ormlite.field.DatabaseField;

/**
 * 关注对象
 * */
public class ItemFollows implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6465237897027410019L;
	/**
	 * 栏目对应唯一标识type加上id
	 * */
	@DatabaseField(id=true)
	private String type_id;
	/**
	 * 栏目对应ID
	 * */
	@DatabaseField
	private String id;
	/**
	 * 馆藏号
	 * */
	@DatabaseField
	private String gch;
	/**
	 * 栏目对应NAME
	 * */
	@DatabaseField
	private String name;
	/**
	 * 机构
	 * */
	@DatabaseField
	private String about;
	/**
	 * 主题
	 * */
	@DatabaseField
	private String subject;
	/**
	 * 所属类别
	 * */
	@DatabaseField
	private String type;
	@DatabaseField
	private long datetime;

	private boolean isNew = false;
	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public void setDatetime(long datetime) {
		this.datetime = datetime;
	}

	public ItemFollows() {
		// TODO Auto-generated constructor stub
	}

	public ItemFollows(String type, JSONObject result) throws JSONException {
		this.type = type;
		switch (type) {
		case C.MEDIA:
			id = result.optString("_id");
			gch = result.optString("gch");
			name = result.optString("media");
			subject = result.optString("subjects");
			about = result.optString("publisher");
			break;
		case C.SUBJECT:
			id = result.optString("_id");
			name = result.optString("subject");
			subject = result.optString("subjects");
			break;
		case C.WRITER:
			id = result.optString("_id");
			name = result.optString("writer");
			subject = result.optString("subjects");
			about = result.optString("organ");
			break;
		case C.ORGAN:
			id = result.optString("_id");
			name = result.optString("organ");
			subject = result.optString("subjects");
			break;
		case C.DOMAIN:
			id = result.optString("_id");
			name = result.optString("classtypename");
			subject = result.optString("subjects");
			break;
		case C.AREA:
			id = result.optString("_id");
			name = result.optString("areaname");
			subject = result.optString("subjects");
			break;
		case C.FUND:
			id = result.optString("_id");
			name = result.optString("fund");
			subject = result.optString("subjects");
			break;
		default:
			break;
		}
		type_id=type+"_"+id;
	}

	public String getType_id() {
		return type_id;
	}
	public static ItemFollows formObject(String result) throws JSONException{
		GeneralResult gr = new GeneralResult(result);
		String res = gr.getResult();
		//Log.i("ItemFollows formObject", res);
		if (!TextUtils.isEmpty(res)) {
			JSONObject jsonObject = new JSONObject(res);
			String type = jsonObject.getString("type");

			JSONObject  json = jsonObject.getJSONObject("obj");
				return new ItemFollows(type,json);
			}
		return null;
	}

	public static ArrayList<ItemFollows> formList(String result)
			throws JSONException {
		//Log.i("JSONException","result"+result);
		GeneralResult gr = new GeneralResult(result);
		String res = gr.getResult();
		if (!TextUtils.isEmpty(res)) {
			JSONObject jsonObject = new JSONObject(res);
			String type = jsonObject.getString("type");

			JSONArray ary = jsonObject.getJSONArray("list");
			int count = ary.length();
			if (count <= 0) {
				return null;
			}
			ArrayList<ItemFollows> follows = new ArrayList<ItemFollows>(count);
			for (int i = 0; i < count; i++) {
				follows.add(new ItemFollows(type, ary.getJSONObject(i)));
			}
			return follows;
		}
		return null;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAbout() {
		return about;
	}

	public String getSubject() {
		return subject;
	}

	public String getType() {
		return type;
	}

	public String getGch() {
		return gch;
	}

	
	public long getDatetime() {
		return datetime;
	}

	@Override
	public String toString() {
		return "ItemFollows [id=" + id + ", name=" + name + ", about=" + about
				+ ", subject=" + subject + ", type=" + type + ", datetime="
				+ datetime + "]";
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof ItemFollows){
			ItemFollows t=(ItemFollows) o;
		//	//Log.i("equals", this.id+"----"+t.getId());
			return this.getId().equals(t.getId())&&this.getType().equals(t.getType());
		}
		return super.equals(o);
	}
}