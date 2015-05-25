package com.cqvip.zlfassist.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import com.android.volley.RequestQueue;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.adapter.ItemFollowsAdapter;
import com.cqvip.zlfassist.adapter.ZKTopicListAdapter;
import com.cqvip.zlfassist.bean.GeneralResult;
import com.cqvip.zlfassist.bean.ItemFollows;
import com.cqvip.zlfassist.bean.ItemUpdate;
import com.cqvip.zlfassist.constant.C;
import com.cqvip.zlfassist.exception.ErrorVolleyThrow;
import com.cqvip.zlfassist.freshlistview.FreshListView;
import com.cqvip.zlfassist.freshlistview.FreshListView.OnItemClickListener;
import com.cqvip.zlfassist.freshlistview.FreshListView.OnStartListener;
import com.cqvip.zlfassist.http.VolleyManager;
import com.cqvip.zlfassist.tools.ViewSetting;
import com.cqvip.zlfassist.view.CustomProgressDialog;
import com.cqvip.zlfassist.zkbean.ZKTopic;
import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class SreachActivity extends Activity implements OnItemClickListener {

	protected RequestQueue mQueue;
	protected ErrorListener errorListener;// 错误处理
	protected CustomProgressDialog customProgressDialog;// 对话框
	private FreshListView listview;
	private RelativeLayout sreach_rl;
	private ZKTopicListAdapter adapter;
	private EditText sreahtxt;
	private int page;	
	private Map<String, String> gparams;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sreach);
		sreahtxt = (EditText) findViewById(R.id.et_search_keyword);
		mQueue = Volley.newRequestQueue(this);
		customProgressDialog = CustomProgressDialog.createDialog(this);
		errorListener = new ErrorVolleyThrow(this, customProgressDialog);
		
		listview=(FreshListView)findViewById(R.id.sreach_list);
		ViewSetting.settingListview(listview, this);
		sreach_rl=(RelativeLayout)findViewById(R.id.sreach_rl);
		sreahtxt.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if ((actionId == 0 || actionId == 3) && event != null) {
					page = 1;
					 getdate(page);
				}
				return false;
			}
		});
		
//		  listview.setOnRefreshStartListener(new OnStartListener() {
//	          @Override
//	          public void onStart() {
////	          	page = 1;
////	              initdate(requesttype,requestid,page,C.DEFAULT_COUNT);
//	          }
//	      });
	      // 加载更多事件回调（可选）
	      listview.setOnLoadMoreStartListener(new OnStartListener() {
	          @Override
	          public void onStart() {
	          	page++;
	          	getdate(page);
	          }
	      });
	      
			listview.setOnItemClickListener(SreachActivity.this);
	}
	
	private void getdate( int page) {
		gparams = new HashMap<String, String>();
		gparams.put("keyword", sreahtxt.getText().toString());
		gparams.put("object", "article");
		gparams.put("pagesize", C.DEFAULT_COUNT+"");
		gparams.put("pageindex", page+"");
//		Log.i("gparams",requestid2+"relation"+requesttype2);
		if(page==1)
		{
		VolleyManager.requestVolley(gparams, C.SERVER +C.URL_SEARCH,
				Method.POST,backlistener,  errorListener, mQueue);
		}
		else
		{
			VolleyManager.requestVolley(gparams, C.SERVER +C.URL_SEARCH,
					Method.POST,backmorelistener,  errorListener, mQueue);
		}
	}
	
	@Override
	public void onItemClick(FreshListView parent, View view, int position,
			long id) {
		ZKTopic zkTopic =  adapter.getList().get(position);
		Intent _intent = new Intent(SreachActivity.this,DetailContentActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("item", zkTopic);
		_intent.putExtra("info", bundle);
		startActivity(_intent);
	}
	

	Listener<String> backlistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
			customProgressDialog.dismiss();
			try {
				ArrayList<ZKTopic> lists = ZKTopic.formList(response);
if (lists != null && !lists.isEmpty()) {
					listview.setVisibility(View.VISIBLE);
					listview.setRefreshSuccess("加载成功"); // 通知加载成功
					//Log.i("VISIBLE","lists"+lists.size());
					sreach_rl.setVisibility(View.GONE);
					adapter = new ZKTopicListAdapter(SreachActivity.this, lists);
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
					sreach_rl.setVisibility(View.VISIBLE);
}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
	};

	Listener<String> backmorelistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {

			try {
				ArrayList<ZKTopic> lists = ZKTopic.formList(response);
				if (lists != null && !lists.isEmpty()) {
					listview.setVisibility(View.VISIBLE);
					listview.setRefreshSuccess("加载成功"); // 通知加载成功
					sreach_rl.setVisibility(View.GONE);
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
					sreach_rl.setVisibility(View.VISIBLE);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
}
