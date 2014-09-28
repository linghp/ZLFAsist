
package com.cqvip.zlfassist.fragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.activity.DetailContentActivity;
import com.cqvip.zlfassist.activity.ZKFollowinfoMainActivity;
import com.cqvip.zlfassist.adapter.ItemFollowsAdapter;
import com.cqvip.zlfassist.base.BaseFrgment;
import com.cqvip.zlfassist.bean.ItemFollows;
import com.cqvip.zlfassist.constant.C;
import com.cqvip.zlfassist.freshlistview.FreshListView;
import com.cqvip.zlfassist.freshlistview.FreshListView.OnItemClickListener;
import com.cqvip.zlfassist.freshlistview.FreshListView.OnStartListener;
import com.cqvip.zlfassist.http.VolleyManager;
import com.cqvip.zlfassist.tools.ViewSetting;
import com.cqvip.zlfassist.zkbean.ZKTopic;

public class FollowsFragment extends BaseFrgment implements OnItemClickListener {

	
	private FreshListView listview;
	private int page;
	private RelativeLayout noResult_rl;
	private ItemFollowsAdapter adapter;
	private String  followType;

	public static FollowsFragment newInstance(String  hometype) {
		FollowsFragment f = new FollowsFragment();
        Bundle args = new Bundle();
        args.putString("type", hometype);
        f.setArguments(args);
        return f;
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		followType = getArguments().getString("type");
		Log.i("followType","followType"+followType);
		View v = inflater.inflate(R.layout.fragment_news, null);
		listview = (FreshListView) v.findViewById(R.id.lv_fresh);
		noResult_rl = (RelativeLayout) v.findViewById(R.id.noresult_rl);
		page = 1;
		  // 设置默认偏移量，主要用于实现透明标题栏功能。（可选）
        ViewSetting.settingListview(listview,getActivity());
        

        // 下拉刷新事件回调（可选）
        listview.setOnRefreshStartListener(new OnStartListener() {
            @Override
            public void onStart() {
            	page = 1;
                refresh(followType,1,C.DEFAULT_COUNT);
            }

			
        });

        // 加载更多事件回调（可选）
        listview.setOnLoadMoreStartListener(new OnStartListener() {
            @Override
            public void onStart() {
            	page++;
                loadMore(followType, page,C.DEFAULT_COUNT);
            }

			
        });
		listview.setOnItemClickListener(this);
        refresh(followType,1,C.DEFAULT_COUNT);
		return v;
	}

	private void refresh(String key, int page, int defaultCount) {
		Map<String, String> gparams = new HashMap<String, String>();
		gparams = new HashMap<String, String>();
		gparams.put("object", key);
		gparams.put("pagesize", defaultCount+"");
		gparams.put("pageindex", page+"");
		Log.i("key","key"+key);
		VolleyManager.requestVolley(gparams, C.SERVER+C.URL_TOPLIST, Method.POST, backlistener, errorListener, mQueue);
	}
	private void loadMore(String key, int page, int defaultCount) {
		Map<String, String> gparams = new HashMap<String, String>();
		gparams = new HashMap<String, String>();
		gparams.put("object", key);
		gparams.put("pagesize", defaultCount+"");
		gparams.put("pageindex", page+"");
		VolleyManager.requestVolley(gparams,  C.SERVER+C.URL_TOPLIST, Method.POST, backMorelistener, errorListener, mQueue);
	}
	Listener<String> backlistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
			customProgressDialog.dismiss();
			try {
			List<ItemFollows> lists = ItemFollows.formList(response);
if (lists != null && !lists.isEmpty()) {
					listview.setVisibility(View.VISIBLE);
					listview.setRefreshSuccess("加载成功"); // 通知加载成功
					Log.i("VISIBLE","lists"+lists.size());
					noResult_rl.setVisibility(View.GONE);
					adapter = new ItemFollowsAdapter(getActivity(), lists);
					if(lists.size()<C.DEFAULT_COUNT){
						listview.setAdapter(adapter);
						listview.stopLoadMore();
					}else{
						listview.setAdapter(adapter);
						listview.startLoadMore(); // 开启LoadingMore功能
					}
} else {
					listview.setRefreshFail("加载失败");
					listview.setVisibility(View.GONE);
					noResult_rl.setVisibility(View.VISIBLE);
}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
	};
	Listener<String> backMorelistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
				customProgressDialog.dismiss();
				try {
					List<ItemFollows> lists = ItemFollows.formList(response);
					if (lists != null && !lists.isEmpty()) {
						listview.setVisibility(View.VISIBLE);
						listview.setRefreshSuccess("加载成功"); // 通知加载成功
						noResult_rl.setVisibility(View.GONE);
						if (lists != null && !lists.isEmpty()&&lists.size()==C.DEFAULT_COUNT) {
							adapter.addMoreData(lists);
							listview.setLoadMoreSuccess();
						} else if(lists != null &&lists.size()>0){
							adapter.addMoreData(lists);
							listview.stopLoadMore();
						}else
						{
							listview.stopLoadMore();
						}
					} else {
						listview.setVisibility(View.GONE);
						noResult_rl.setVisibility(View.VISIBLE);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	};


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
