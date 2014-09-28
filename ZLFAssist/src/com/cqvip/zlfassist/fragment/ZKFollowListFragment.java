
package com.cqvip.zlfassist.fragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.activity.ZKFollowinfoMainActivity;
import com.cqvip.zlfassist.adapter.ItemFollowsAdapter;
import com.cqvip.zlfassist.base.BaseFrgment;
import com.cqvip.zlfassist.bean.ItemFollows;
import com.cqvip.zlfassist.constant.C;
import com.cqvip.zlfassist.freshlistview.FreshListView;
import com.cqvip.zlfassist.freshlistview.FreshListView.OnItemClickListener;
import com.cqvip.zlfassist.http.VolleyManager;
import com.cqvip.zlfassist.tools.ViewSetting;

public class ZKFollowListFragment extends BaseFrgment implements OnItemClickListener {

	
	private FreshListView listview;
	private int page;
	private RelativeLayout noResult_rl;
	private ItemFollowsAdapter adapter;
	private ArrayList<ItemFollows> itemFollows_List;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_news, null);
		listview = (FreshListView) v.findViewById(R.id.lv_fresh);
		noResult_rl = (RelativeLayout) v.findViewById(R.id.noresult_rl);
		page = 1;
		  // 设置默认偏移量，主要用于实现透明标题栏功能。（可选）
       // ViewSetting.settingListview(listview,getActivity());
      
        itemFollows_List=(ArrayList<ItemFollows>) getArguments().getSerializable("itemfollows_list");
        adapter = new ItemFollowsAdapter(getActivity(), itemFollows_List);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
		return v;
	}



	@Override
	public void onItemClick(FreshListView parent, View view, int position,
			long id) {
		ItemFollows item = adapter.getList().get(position);
		// Book book = lists.get(position-1);
		 if(item!=null){
			 if(item.getType().equals(C.MEDIA)){
				 return;
				 //TODO
			 }else{
		Intent _intent = new Intent(getActivity(),ZKFollowinfoMainActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("item", item);
		_intent.putExtra("info", bundle);
		startActivity(_intent);
			 }
  	}
	}

}
