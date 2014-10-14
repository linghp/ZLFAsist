package com.cqvip.zlfassist.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.adapter.ZKTopicListAdapter;
import com.cqvip.zlfassist.base.BaseActionBarActivity;
import com.cqvip.zlfassist.base.BaseActivity;
import com.cqvip.zlfassist.bean.ItemFollows;
import com.cqvip.zlfassist.constant.C;
import com.cqvip.zlfassist.freshlistview.FreshListView;
import com.cqvip.zlfassist.freshlistview.FreshListView.OnItemClickListener;
import com.cqvip.zlfassist.freshlistview.FreshListView.OnStartListener;
import com.cqvip.zlfassist.http.VolleyManager;
import com.cqvip.zlfassist.tools.ViewSetting;
import com.cqvip.zlfassist.zkbean.ZKTopic;
/**
 * 主题，作者，基金，地区，学科等类别下面的相关文章
 * @author luojiang
 *
 */
public class ZKFollowinfoMainActivity extends BaseActionBarActivity implements OnItemClickListener {
	private FreshListView listview;
	private Context context;
	private ImageView img_back;
	private String requestid;
	private String requesttype;
	private Map<String, String> gparams;
	private ZKTopicListAdapter adapter;
	private ItemFollows perio;
	private int page;
	private RelativeLayout noResult_rl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.follow_content_main);
		context = this;
		Bundle bundle = getIntent().getBundleExtra("info");
		perio = (ItemFollows) bundle.getSerializable("item");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(perio.getName());
		requestid = perio.getId();
		requesttype = perio.getType();
		findView();
		initdate(requesttype,requestid,1,C.DEFAULT_COUNT);
	}
	/**
	 * 发送请求获取数据
	 * @param defaultCount 
	 * @param i 
	 * @param requesttype2 
	 * @param requestid2 
	 */
	private void initdate(String requesttype2, String requestid2, int page, int defaultCount) {
		gparams = new HashMap<String, String>();
		gparams.put("relation", requesttype2);
		gparams.put("id", requestid2);
		gparams.put("pagesize", defaultCount+"");
		gparams.put("pageindex", page+"");
		customProgressDialog.show();
		Log.i("gparams",requestid2+"relation"+requesttype2);
		VolleyManager.requestVolley(gparams, C.SERVER +C.URL_TOPIC_LIST,
				Method.POST,backlistener,  errorListener, mQueue);
	}
	private void loadMore(String requesttype2, String requestid2, int page, int defaultCount) {
		gparams = new HashMap<String, String>();
		gparams.put("relation", requesttype2);
		gparams.put("id", requestid2);
		gparams.put("pagesize", defaultCount+"");
		gparams.put("pageindex", page+"");
		customProgressDialog.show();
		Log.i("gparams",requestid2+"relation"+requesttype2);
		VolleyManager.requestVolley(gparams, C.SERVER_URL +C.URL_TOPIC_LIST,
				Method.POST,backmorelistener,  errorListener, mQueue);
	}
	private void findView() {
		noResult_rl = (RelativeLayout) findViewById(R.id.noresult_rl);
		listview =  (FreshListView) findViewById(R.id.lv_follow);
		ViewSetting.settingListview(listview, this);
		//upView = LayoutInflater.from(this).inflate(R.layout.follow_content_up, null);
		page = 1;
      // 下拉刷新事件回调（可选）
      listview.setOnRefreshStartListener(new OnStartListener() {
          @Override
          public void onStart() {
          	page = 1;
              initdate(requesttype,requestid,page,C.DEFAULT_COUNT);
          }
      });
      // 加载更多事件回调（可选）
      listview.setOnLoadMoreStartListener(new OnStartListener() {
          @Override
          public void onStart() {
          	page++;
              loadMore(requesttype,requestid,page,C.DEFAULT_COUNT);
          }
      });
		listview.setOnItemClickListener(this);
		
	}
	Listener<String> backlistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
			customProgressDialog.dismiss();
			try {
				ArrayList<ZKTopic> lists = ZKTopic.formList(response);
				if (lists != null && !lists.isEmpty()) {
									listview.setVisibility(View.VISIBLE);
									listview.setRefreshSuccess("加载成功"); // 通知加载成功
									Log.i("VISIBLE","lists"+lists.size());
									noResult_rl.setVisibility(View.GONE);
									adapter = new ZKTopicListAdapter(context, lists);
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
				e.printStackTrace();
			}
		}
	};
	Listener<String> backmorelistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
				customProgressDialog.dismiss();
			try {
				ArrayList<ZKTopic> lists = ZKTopic.formList(response);
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
				e.printStackTrace();
			}
		}
	};

	@Override
	public void onItemClick(FreshListView parent, View view, int position,
			long id) {
		ZKTopic zkTopic =  adapter.getList().get(position);
		Intent _intent = new Intent(context,DetailContentActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("item", zkTopic);
		_intent.putExtra("info", bundle);
		startActivity(_intent);
	}
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if(itemId == android.R.id.home){
			finish();
		}
//		if(itemId == R.id.action_about){
//			Intent _intent = new Intent(context,ZKFollowinfoUpActivity.class);
//			Bundle bundle = new Bundle();
//			bundle.putSerializable("item",perio );
//			_intent.putExtra("info", bundle);
//			startActivity(_intent);
//		}
		return false;
	    }
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.info_content, menu);
//		return true;
//	}
	
}