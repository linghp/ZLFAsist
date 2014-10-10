
package com.cqvip.zlfassist.fragment;
import java.util.ArrayList;

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

import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.activity.ZKFollowinfoMainActivity;
import com.cqvip.zlfassist.activity.ZKPeriodicalInfoActivity;
import com.cqvip.zlfassist.adapter.ItemFollowsAdapter;
import com.cqvip.zlfassist.base.BaseFrgment;
import com.cqvip.zlfassist.bean.ItemFollows;
import com.cqvip.zlfassist.constant.C;

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
        adapter = new ItemFollowsAdapter(getActivity(), itemFollows_List);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
		return v;
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
