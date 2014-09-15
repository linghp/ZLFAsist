
package com.cqvip.zlfassist.fragment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.activity.PeriodicalInfoActivity;
import com.cqvip.zlfassist.adapter.PeriodicalAdapter;
import com.cqvip.zlfassist.base.BaseFrgment;
import com.cqvip.zlfassist.bean.PeriodicalResult;
import com.cqvip.zlfassist.bean.Periodical;
import com.cqvip.zlfassist.bitmap.BitmapCache;
import com.cqvip.zlfassist.constant.C;
import com.cqvip.zlfassist.freshlistview.FreshListView;
import com.cqvip.zlfassist.freshlistview.FreshListView.OnItemClickListener;
import com.cqvip.zlfassist.freshlistview.FreshListView.OnStartListener;
import com.cqvip.zlfassist.http.VolleyManager;
import com.cqvip.zlfassist.tools.ViewSetting;
import com.google.gson.Gson;

public class PeriodicalListFragment extends BaseFrgment implements OnItemClickListener {

	
	private FreshListView listview;
	private int page;
	private RelativeLayout noResult_rl;
	private PeriodicalAdapter adapter;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
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
                refresh("",1,C.DEFAULT_COUNT);
            }

			
        });

        // 加载更多事件回调（可选）
        listview.setOnLoadMoreStartListener(new OnStartListener() {
            @Override
            public void onStart() {
            	page++;
                loadMore("", page,C.DEFAULT_COUNT);
            }

			
        });
		listview.setOnItemClickListener(this);
        refresh("",1,C.DEFAULT_COUNT);
		return v;
	}

	private void refresh(String key, int page, int defaultCount) {
		Map<String, String> gparams = new HashMap<String, String>();
		gparams = new HashMap<String, String>();
		gparams.put("classid", key);
		gparams.put("perpage", defaultCount+"");
		gparams.put("curpage", page+"");
		
		VolleyManager.requestVolley(gparams, C.BASE, Method.POST, backlistener, errorListener, mQueue);
	}
	private void loadMore(String key, int page, int defaultCount) {
		Map<String, String> gparams = new HashMap<String, String>();
		gparams = new HashMap<String, String>();
		gparams.put("classid", key);
		gparams.put("perpage", defaultCount+"");
		gparams.put("curpage", page+"");
		VolleyManager.requestVolley(gparams, C.BASE, Method.POST, backMorelistener, errorListener, mQueue);
	}
	Listener<String> backlistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
			customProgressDialog.dismiss();
			System.out.println(response);
			PeriodicalResult result = new Gson().fromJson(response, PeriodicalResult.class);
			if(result.isSuccess()){
				try {
					//List<Periodical> lists = result.getQklist();
					Periodical temp =Periodical.formObject(response,Periodical.TASK_PERIODICAL_SUBTYPE);
					List<Periodical> lists = temp.qklist;
if (lists != null && !lists.isEmpty()) {
					listview.setVisibility(View.VISIBLE);
					listview.setRefreshSuccess("加载成功"); // 通知加载成功
					
					noResult_rl.setVisibility(View.GONE);
					adapter = new PeriodicalAdapter(getActivity(), lists,new ImageLoader(mQueue, BitmapCache.cache));
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
			
		}
	};
	Listener<String> backMorelistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
				customProgressDialog.dismiss();
			System.out.println(response);
			PeriodicalResult result = new Gson().fromJson(response, PeriodicalResult.class);
			if(result.isSuccess()){
			//	List<Periodical> lists = result.getQklist();
				try {
					Periodical temp =Periodical.formObject(response,Periodical.TASK_PERIODICAL_SUBTYPE);
					List<Periodical> lists = temp.qklist;
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
			
		}
	};


	@Override
	public void onItemClick(FreshListView parent, View view, int position,
			long id) {
		Periodical periodical = adapter.getLists().get(position);
		// Book book = lists.get(position-1);
		 if(periodical!=null){
		Intent _intent = new Intent(getActivity(),PeriodicalInfoActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("periodical", periodical);
		_intent.putExtra("detaiinfo", bundle);
		startActivity(_intent);
		
	}
	}
	
}
