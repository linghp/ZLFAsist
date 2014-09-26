package com.cqvip.zlfassist.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.adapter.ItemFollowsAdapter;
import com.cqvip.zlfassist.adapter.ZKTopicListAdapter;
import com.cqvip.zlfassist.base.BaseActivity;
import com.cqvip.zlfassist.bean.EBook;
import com.cqvip.zlfassist.bean.ItemFollows;
import com.cqvip.zlfassist.bean.JudgeResult;
import com.cqvip.zlfassist.bitmap.BitmapCache;
import com.cqvip.zlfassist.constant.C;
import com.cqvip.zlfassist.freshlistview.FreshListView;
import com.cqvip.zlfassist.freshlistview.FreshListView.OnItemClickListener;
import com.cqvip.zlfassist.freshlistview.FreshListView.OnStartListener;
import com.cqvip.zlfassist.http.HttpUtils;
import com.cqvip.zlfassist.http.VolleyManager;
import com.cqvip.zlfassist.tools.ViewSetting;
import com.cqvip.zlfassist.view.picker.ArrayWheelAdapter;
import com.cqvip.zlfassist.view.picker.OnWheelChangedListener;
import com.cqvip.zlfassist.view.picker.WheelView;
import com.cqvip.zlfassist.zkbean.ZKContent;
import com.cqvip.zlfassist.zkbean.ZKTopic;
import com.cqvip.zlfassist.zkbean.ZKTopic_Info;
/**
 * 主题，作者，基金，地区，学科等类别的相关信息
 * @author luojiang
 *
 */
public class ZKFollowinfoUpActivity extends BaseActivity  {
	private FreshListView listview;
	private Context context;
	private TextView title,mcount,organ,classtype,subject,about,fund;
	private View upView;
	private ImageView img_back;
	private String requestid;
	private String requesttype;
	private Map<String, String> gparams;
	private ZKTopicListAdapter adapter;
	private ItemFollows perio;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.follow_content_up);
		context = this;
		
		Bundle bundle = getIntent().getBundleExtra("info");
		perio = (ItemFollows) bundle.getSerializable("item");
		requestid = perio.getId();
		requesttype = perio.getType();
		
		findView();
		initViewFirst();
		initdate(requesttype,requestid,1,C.DEFAULT_COUNT);
		setListener();
	}
	private void initViewFirst() {
		title.setText(perio.getName());
		subject.setText("发文主题："+perio.getSubject());
		if (!TextUtils.isEmpty(perio.getAbout())) {
				organ.setVisibility(View.VISIBLE);
				organ.setText("供职机构："+perio.getAbout());
	    } 
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
		gparams.put("type", requesttype2);
		gparams.put("key", requestid2);
		customProgressDialog.show();
		VolleyManager.requestVolley(gparams, C.SERVER +C.URL_TOPIC_INFO,
				Method.POST,backlistener,  errorListener, mQueue);
	}
	private void findView() {
		TextView tv = (TextView) findViewById(R.id.tv_title);
		tv.setText(requesttype);
		img_back = (ImageView)  findViewById(R.id.img_back);
		img_back.setVisibility(View.VISIBLE);
		title = (TextView) findViewById(R.id.tv_folloup_title);
		mcount  = (TextView) findViewById(R.id.tv_folloup_count);
		organ  = (TextView) findViewById(R.id.tv_folloup_orgniziton);
		classtype  = (TextView) findViewById(R.id.tv_folloup_classtype);
		subject = (TextView) findViewById(R.id.tv_folloup_subject);
		about = (TextView) findViewById(R.id.tv_folloup_periodical);
		fund = (TextView) findViewById(R.id.tv_folloup_fund);
	}
	private void setListener() {
		img_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	Listener<String> backlistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
			customProgressDialog.dismiss();
					try {
						JudgeResult result = new JudgeResult(response);
						Log.i("backlistener","response======="+response);
						if(result.getState().equals("00")){
						
							ZKTopic_Info info =ZKTopic_Info.formObject(response);
							 Log.i("topic", info.toString());
							 if(info!=null){
							 setView(info);
							 }
						}else{
							Toast.makeText(ZKFollowinfoUpActivity.this, result.getMsg(), 1).show();	
						}
				
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(ZKFollowinfoUpActivity.this, "解析错误", 1).show();	
			}
		}

		
	};
	private void setView(ZKTopic_Info info) {
	//title
		mcount. setText("作品数："+info.getZkfwcount()+"，被引用量："+info.getZkbycount()+",H指数"+info.getZkhindex());
		if(perio.getType().equals(C.WRITER)){
		organ. setText("供职机构："+info.getOrgan());
		}else {
			organ. setText("相关作者："+info.getWriters());
		}
		classtype. setText("发文领域："+info.getClasstypes());
	//	subject. setText();
		about. setText("发文期刊："+info.getMedias());
		fund. setText("所获基金："+info.getFunds());
		
	}

	
}