package com.cqvip.zlfassist.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.cqvip.zlfassist.constant.C;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/** 
 * 关注对象
 *  */
public class ItemFollows implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6465237897027410019L;
	/** 
	 * 栏目对应ID
	 *  */
	private String id;
	/** 
	 * 栏目对应NAME
	 *  */
	private String name;
	/** 
	 * 栏目在整体中的排序顺序  rank
	 *  */
	private String about;
	/** 
	 * 栏目是否选中
	 *  */
	private String subject;
	private String type;
	private long datetime = System.currentTimeMillis();
	
	public ItemFollows(String type,JSONObject result) throws JSONException{
		 	this.type = type;
			switch (type) {
			case C.MEDIA:
				id = result.getString("gch");
				name = result.getString("media");
				subject = result.getString("subjects");
				about = result.getString("publisher");
				break;
			case C.SUBJECT:
				id = result.getString("_id");
				name = result.getString("subject");
				subject = result.getString("subjects");
				break;
			case C.WRITER:
				id = result.getString("_id");
				name = result.getString("writer");
				subject = result.getString("subjects");
				about = result.getString("organ");
				break;
			case C.ORGAN:
				id = result.getString("_id");
				name = result.getString("organ");
				subject = result.getString("subjects");
				break;
			case C.DOMAIN:
				id = result.getString("_id");
				name = result.getString("classtypename");
				subject = result.getString("subjects");
				break;
			case C.AREA:
				id = result.getString("_id");
				name = result.getString("areaname");
				subject = result.getString("subjects");
				break;
			case C.FUND:
				id = result.getString("_id");
				name = result.getString("fund");
				subject = result.getString("subjects");
				break;
			default:
				break;
			}
		}
		public static List<ItemFollows> formList(String  result) throws JSONException{
			GeneralResult gr = new GeneralResult(result);
			String res = gr.getResult();
			if(!TextUtils.isEmpty(res)){
				JSONObject jsonObject = new JSONObject(res);
				String type = jsonObject.getString("type");
			
				JSONArray ary = jsonObject.getJSONArray("list");
				int count = ary.length();
				if(count <=0){
					return null;
				}
				 List<ItemFollows>	follows = new ArrayList<ItemFollows>(count);
				for(int i = 0;i<count;i++){
					follows.add(new ItemFollows(type,ary.getJSONObject(i)));
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
		public long getDatetime() {
			return datetime;
		}
		@Override
		public String toString() {
			return "ItemFollows [id=" + id + ", name=" + name + ", about="
					+ about + ", subject=" + subject + ", type=" + type
					+ ", datetime=" + datetime + "]";
		}
		
}