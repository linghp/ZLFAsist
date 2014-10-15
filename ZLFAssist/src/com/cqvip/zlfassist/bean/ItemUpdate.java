package com.cqvip.zlfassist.bean;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemUpdate {
		String datetime;
		HashMap<String,Boolean> updateList;
		public ItemUpdate(String result) throws JSONException {
			JSONObject json = new JSONObject(result);
			JSONArray jsonArray = json.getJSONArray("list");
			this.datetime =json.getString("datetime");
			this.updateList = formMap( jsonArray);
		}
		private HashMap<String, Boolean> formMap(JSONArray jsonArray) throws JSONException {
			int count = jsonArray.length();
			if (count <= 0) {
				return null;
			}
			HashMap<String, Boolean> maps = new HashMap<>();
			for (int i = 0; i < count; i++) {
					JSONObject js = (JSONObject) jsonArray.get(i);
					String id = js.getString("id");
					String state = js.getString("state");
					if(state.equals("true")){
					maps.put(id, true);
					}else{
						maps.put(id, false);
					}
			}
			return maps;
		}
		public String getDatetime() {
			return datetime;
		}
		public HashMap<String, Boolean> getUpdateList() {
			return updateList;
		}
		
}
