
package com.cqvip.zlfassist.fragment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.activity.ZKFollowinfoMainActivity;
import com.cqvip.zlfassist.activity.ZKPeriodicalInfoActivity;
import com.cqvip.zlfassist.adapter.ItemFollowsAdapter;
import com.cqvip.zlfassist.base.BaseFrgment;
import com.cqvip.zlfassist.bean.GeneralResult;
import com.cqvip.zlfassist.bean.ItemFollows;
import com.cqvip.zlfassist.bean.ItemUpdate;
import com.cqvip.zlfassist.constant.C;
import com.cqvip.zlfassist.http.VolleyManager;

public class ZKFollowListFragment extends BaseFrgment implements OnItemClickListener {

	
	private ListView listview;
	private int page;
	private RelativeLayout noResult_rl;
	private ItemFollowsAdapter adapter;
	private ArrayList<ItemFollows> itemFollows_List;

	public ZKFollowListFragment() {
		// TODO Auto-generated constructor stub
	}
	
	public ZKFollowListFragment(ArrayList<ItemFollows> itemFollows_List) {
		this.itemFollows_List=itemFollows_List;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("ZKFollowListFragment", "onCreateView");
		View v = inflater.inflate(R.layout.follow_fragment_news, null);
		listview = (ListView) v.findViewById(R.id.lv_fresh);
		noResult_rl = (RelativeLayout) v.findViewById(R.id.noresult_rl);
		page = 1;
		  // 设置默认偏移量，主要用于实现透明标题栏功能。（可选）
       // ViewSetting.settingListview(listview,getActivity());
      
       // itemFollows_List=(ArrayList<ItemFollows>) getArguments().getSerializable("itemfollows_list");
        Log.i("ZKFollowListFragment", itemFollows_List.size()+"--"+getArguments().getString("type"));
        String type = getArguments().getString("type");
        if(type.equals(C.MEDIA)){
        	//获取更新数据
        	getDate(itemFollows_List);
        }
        adapter = new ItemFollowsAdapter(getActivity(), itemFollows_List);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
		return v;
	}

	private void getDate(ArrayList<ItemFollows> itemFollows_List2) {
		String keysString = getAllIds(itemFollows_List2);
		Map<String, String> map = new HashMap<>();
		map.put("key", keysString);
		map.put("type","media");
		//map.put("datetime", "1413181135");
		map.put("datetime", "0");
		Log.i("getDate","key"+keysString);
		VolleyManager.requestVolley(map, C.SERVER+C.URL_UPDATE_PERICAL, Method.POST, backlistener, errorListener, mQueue);
		
	}
	Listener<String> backlistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
//			if(customProgressDialog!=null&&customProgressDialog.isShowing())
//			customProgressDialog.dismiss();
			GeneralResult result;
			try {
				result = new GeneralResult(response);
				if(result.getState().equals("00")){
					ItemUpdate items = new ItemUpdate(result.getResult());
					itemFollows_List = sortLists(itemFollows_List, items.getUpdateList());
					adapter.notifyDataSetChanged();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};
	private ArrayList<ItemFollows> sortLists(ArrayList<ItemFollows> lists, HashMap<String,Boolean> updates) {
		for (Map.Entry<String, Boolean> entry : updates.entrySet()) {
				if(entry.getValue()){
						for (int i = 0; i < lists.size(); i++) {
								if(entry.getKey().equals(lists.get(i).getId())){
										lists.get(i).setNew(true);
								}
						}
					}
			  }
		 Comparator<ItemFollows> comparator = new Comparator<ItemFollows>(){

				@Override
				public int compare(ItemFollows s1, ItemFollows s2) {
						    //先排年龄
							   if(s1.isNew()!=s2.isNew()){
								   if(s1.isNew()){
									   return -1;
								   }else{
									   return 1;
								   }
							   }else{
								   return -1;
							   }
				}
				  };
				  Collections.sort(lists,comparator);
		return lists;
	}
	private String  getAllIds(ArrayList<ItemFollows> itemFollows_List2) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < itemFollows_List2.size(); i++) {
			String id = itemFollows_List2.get(i).getId();
			stringBuilder.append(id);
			if(itemFollows_List2.size()>0&&i<itemFollows_List2.size()-1){
			stringBuilder.append(",");
			}
		}
		return stringBuilder.toString();
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ItemFollows item = adapter.getList().get(position);
		Intent _intent ;
		// Book book = lists.get(position-1);
		 if(item!=null){
			 if(item.getType().equals(C.MEDIA)){
				 _intent  = new Intent(getActivity(),ZKPeriodicalInfoActivity.class);
			 }else{
		_intent  = new Intent(getActivity(),ZKFollowinfoMainActivity.class);
			 }
			 Bundle bundle = new Bundle();
			 bundle.putSerializable("item", item);
			 _intent.putExtra("info", bundle);
			 startActivity(_intent);
		 }
	}
}
