package com.cqvip.zlfassist.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.integer;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
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

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.adapter.ZKTopicListAdapter;
import com.cqvip.zlfassist.base.BaseActivity;
import com.cqvip.zlfassist.bean.EBook;
import com.cqvip.zlfassist.bean.ItemFollows;
import com.cqvip.zlfassist.bean.Periodical;
import com.cqvip.zlfassist.bean.PeriodicalYear;
import com.cqvip.zlfassist.bean.ZKPeriodical;
import com.cqvip.zlfassist.constant.C;
import com.cqvip.zlfassist.http.HttpUtils;
import com.cqvip.zlfassist.http.VolleyManager;
import com.cqvip.zlfassist.view.picker.ArrayWheelAdapter;
import com.cqvip.zlfassist.view.picker.OnWheelChangedListener;
import com.cqvip.zlfassist.view.picker.WheelView;
import com.cqvip.zlfassist.zkbean.ZKTopic;

public class ZKPeriodicalInfoActivity extends BaseActivity {
	private String mYear = null;
	private String mMonth = null;
	private ListView listview;
	private Context context;
	private TextView txt_date;
	private TextView title,subjects,directordept,publisher,chiefedit,pubcycle,price,num,remark,tips;
	private View upView;
	private ImageView img,img_back;
	private String gch;
	private Map<String, String> gparams;
	private ArrayList<PeriodicalYear> yearlist;
	private String year,month;//记录日期是否发生变化
	private int yaer_record,month_record;
	private ZKTopicListAdapter adapter;
	private ItemFollows perio;
	private View progress;
	private ArrayList<ZKTopic> lists;//目录
	private boolean isFirstFlag = false;
	private RelativeLayout rlFromAndDate;
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_periodical_content_main);
		context = this;
		
		Bundle bundle = getIntent().getBundleExtra("info");
		perio = (ItemFollows) bundle.getSerializable("item");
		findView();
		initViewFirst();
		initdate(perio.getId());
	
		setListener();
		listview.setAdapter(null);
		txt_date.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(yearlist==null){
					return;
				}
				final Dialog dialog = new Dialog(v.getContext(), R.style.dialog_fullscreen);
				dialog.setContentView(R.layout.dialog_picker);
				//记录下位置
				final int ty = yaer_record;
				final int tm = month_record;
				//判断日期是否发生变化
				 WheelView country = (WheelView) dialog.findViewById(R.id.country);
			        final int length = yearlist.size() ;
			        final String[] count = new String[length];
			        for(int i=0;i<length;i++){
			        	count[i]=yearlist.get(i).getYear();
			        }
			        country.setVisibleItems(5);
			        country.setLabel("年");
			        country.setAdapter(new ArrayWheelAdapter<String>(count));
			        final WheelView city = (WheelView) dialog.findViewById(R.id.city);
			        final Button confirm = (Button) dialog.findViewById(R.id.btn_date_confirm);
			        city.setAdapter(new ArrayWheelAdapter<String>(yearlist.get(0).getNum()));
			        city.setVisibleItems(5);
			        city.setLabel("期");
			        city.setCyclic(true);
			        country.addChangingListener(new OnWheelChangedListener() {
						public void onChanged(WheelView wheel, int oldValue, int newValue) {
							String[] mNum = yearlist.get(newValue).getNum();
							city.setAdapter(new ArrayWheelAdapter<String>(mNum));
							//期数小于oldvalue
							if( yearlist.get(newValue).getNum().length< yearlist.get(oldValue).getNum().length){
							city.setCurrentItem(yearlist.get(newValue).getNum().length / 2);
							}
							mYear = yearlist.get(newValue).getYear();
							//confirm.setText(mYear+"年第"+mMonth+"期");
							//记录下位置
							yaer_record = newValue;
						}
					});
			        city.addChangingListener(new OnWheelChangedListener() {
						public void onChanged(WheelView wheel, int oldValue, int newValue) {
							//mMonth =  newValue+1+"";
							mMonth =  yearlist.get(yaer_record).getNum()[newValue];
							//confirm.setText(mYear+"年第"+mMonth+"期");
							//记录下位置
							month_record = newValue;
						}
					});
			        
			        country.setCurrentItem(yaer_record);
			        String[] arr = yearlist.get(yaer_record).getNum();
			        city.setCurrentItem(month_record);
			        mYear =  yearlist.get(yaer_record).getYear();
			        mMonth = arr[month_record];
				//confirm.setText(mYear+"年第"+mMonth+"期");
				
			        confirm.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						//期数是否发生变化
						if(ty!=yaer_record||tm!=month_record){
							//发生变化，请求网络
						
							//非第一次请求
							if(lists!=null&&adapter!=null){
								isFirstFlag = false;
							}
							initNewstYeardate(yearlist.get(yaer_record),month_record);
						}
						txt_date.setText(mYear+"年第"+mMonth+"期：目录");
						//判断是否日期发生变化，变化则重新加载
					}

					private void initNewstYeardate(
							PeriodicalYear periodicalYear, int month_record) {
						progress.setVisibility(View.VISIBLE);
						String[] tmpary = periodicalYear.getNum();
						String id = periodicalYear.get_id();
						String year = periodicalYear.getYear();
						String num =tmpary[month_record];
						Map gparams = new HashMap<String, String>();
						gparams.put("id", id+"|"+year+"|"+num);
						gparams.put("pageindex", 1+"");
						gparams.put("pagesize",50+"");
						VolleyManager.requestVolley(gparams, C.SERVER+C.URL_PERIDICAL_LIST, Method.POST, backlistener_content, errorListener, mQueue);
					}
				});
				dialog.setCancelable(false);
				dialog.show();
			}
		});
		
	}
	private void initViewFirst() {
		title.setText(perio.getName());
		subjects.setText("发文主题："+perio.getSubject());
	}
	/**
	 * 发送请求获取数据
	 * @param id 
	 */
	private void initdate(String id) {
		customProgressDialog.show();
		Map<String, String> map = new HashMap<>();
		map.put("key", id);
		map.put("type","media");
		VolleyManager.requestVolley(map, C.SERVER+C.URL_PERIDICAL_INFO, Method.POST, backlistener, errorListener, mQueue);
	}
	/**
	 * 发送请求获取数据
	 * @param periodicalYear 
	 * @param mMonth2 
	 */
	private void initNewstYeardate(PeriodicalYear periodicalYear, String mMonth2) {
		progress.setVisibility(View.VISIBLE);
		String[] tmpary = periodicalYear.getNum();
		String id = periodicalYear.get_id();
		String year = periodicalYear.getYear();
		String num =tmpary[tmpary.length-1];
		Map gparams = new HashMap<String, String>();
		gparams.put("id", id+"|"+year+"|"+num);
		gparams.put("pageindex", 1+"");
		gparams.put("pagesize",50+"");
		VolleyManager.requestVolley(gparams, C.SERVER+C.URL_PERIDICAL_LIST, Method.POST, backlistener_content, errorListener, mQueue);
	}
	private void findView() {
		listview = (ListView) findViewById(R.id.listView1);
		upView = LayoutInflater.from(this).inflate(R.layout.activity_periodical_content_up, null);
		listview.addHeaderView(upView);
		progress = findViewById(R.id.footer_progress);
		//标题
		  View v = findViewById(R.id.head_bar);
	     TextView tv = (TextView) v.findViewById(R.id.tv_title);
	     tv.setText("期刊");
	     img_back = (ImageView) v.findViewById(R.id.img_back);
	     img_back.setVisibility(View.VISIBLE);
		//内容
		title = (TextView)findViewById(R.id.periodical_title_txt);
		directordept  = (TextView)findViewById(R.id.periodical_host1_txt);
		subjects  = (TextView)findViewById(R.id.periodical_host2_txt);
//		chiefedit  = (TextView)findViewById(R.id.periodical_author_txt);
//		pubcycle = (TextView)findViewById(R.id.periodical_time_txt);
//		price = (TextView)findViewById(R.id.periodical_price_txt);
//		num = (TextView)findViewById(R.id.periodical_num_txt);
//		remark = (TextView)findViewById(R.id.periodical_content_abst);
//		tips =  (TextView)findViewById(R.id.txt_null_tips);
//		rlFromAndDate = (RelativeLayout)findViewById(R.id.rlFromAndDate);
//		//期刊日期
//		txt_date = (TextView) findViewById(R.id.txt_year_month);
//		//图片
//		img = (ImageView) findViewById(R.id.periodical_icon_img);
		
	}
	private void setListener() {
		img_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				if(id!=-1){
//				if(adapter!=null&&adapter.getLists()!=null){
//				EBook book = adapter.getLists().get(position-1);
//				if (book != null) {
//					Intent _intent = new Intent(context, EbookDetailActivity.class);
//					Bundle bundle = new Bundle();
//					bundle.putSerializable("book", book);
//					_intent.putExtra("detaiinfo", bundle);
//					startActivity(_intent);
//				}
//			  }
//			}
				startActivity(new Intent(ZKPeriodicalInfoActivity.this,DetailContentActivity.class));
		  }
		});
		
	}
	private void initView(ZKPeriodical periodical) {
		
		directordept.setText("主办单位："+periodical.getPublisher());
//		publisher.setText(getResources().getString(R.string.title_perio_publisher)+periodical.getPublisher());
//		chiefedit.setText(getResources().getString(R.string.title_perio_chiefeditor)+(periodical.getChiefeditor()==null?"":periodical.getChiefeditor()));
//		pubcycle.setText(getResources().getString(R.string.title_perio_type)+periodical.getPubcycle()+","+periodical.getSize());
//		remark.setText(getResources().getString(R.string.title_perio_remark)+periodical.getRemark());
		
		//初始化期刊日期
		if(yearlist==null){
			txt_date.setVisibility(View.GONE);
			rlFromAndDate.setVisibility(View.GONE);
			tips.setVisibility(View.VISIBLE);
			tips.setText("抱歉！该杂志暂未被");
		}else{
		
		year = yearlist.get(0).getYear();
		String[] arr = yearlist.get(0).getNum();
		month = arr[arr.length-1];
		txt_date.setText(year+"年第"+month+"期：目录");
		yaer_record = 0;
		month_record = arr.length-1;
		}
	}
	Listener<String> backlistener_content = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
			customProgressDialog.dismiss();
			progress.setVisibility(View.GONE);
			try {
				//第一次setAdapter
				if(isFirstFlag){
				lists = ZKTopic.formList(response);
				if(lists!=null&&!lists.isEmpty()){
				adapter = new ZKTopicListAdapter(context,lists);
				listview.setAdapter(adapter);
				}
				}else{
				//第二次改变
				lists.clear();
				ArrayList<ZKTopic> mlists = ZKTopic.formList(response);
				adapter.addMoreData(mlists);
				//adapter.notifyDataSetChanged();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};
	Listener<String> backlistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
			customProgressDialog.dismiss();
			try {
				ZKPeriodical periodical =ZKPeriodical.formObject(response);	
				if(periodical!=null){
				yearlist = periodical.getYearsnumlist();
				//完成view的初始化;
				System.out.println(yearlist);
				
				initView(periodical);
				//获取最新期刊目录书籍
				if(yearlist!=null){
			
				//第一次请求
				isFirstFlag = true;
				initNewstYeardate(yearlist.get(0),null);
//				requestVolley(C.SERVER_URL + "/zk/search.aspx",
//						backlistener_content, Method.POST);
				}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};
	
	private void requestVolley(String addr, Listener<String> bl, int method) {
		try {
			StringRequest mys = new StringRequest(method, addr, bl, errorListener) {
				protected Map<String, String> getParams()
						throws com.android.volley.AuthFailureError {
					return gparams;
				};
			};
			mys.setRetryPolicy(HttpUtils.setTimeout());mQueue.add(mys);
			mQueue.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	class MyAdapter extends BaseAdapter{
		private Context context;
		private List<EBook> mlists;
		public MyAdapter(Context context){
			this.context = context;
		}
		public MyAdapter(Context context2, List<EBook> lists) {
			this.context = context2;
			this.mlists = lists;
		}
		public List<EBook> getLists(){
			return mlists;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(mlists != null){
				return mlists.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mlists.get(position);
		}

		@Override
		public long getItemId(int position) {
			if(this.getCount()>0&&position < this.getCount()){
			return position;
			}
			return -1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v;
			if(convertView==null){
				v =LayoutInflater.from(context).inflate(R.layout.item_news, null);
			}else{
				v = convertView;
			}
			TextView tv = (TextView) v.findViewById(R.id.tv_item_topic);
			tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			tv.setText(mlists.get(position).getTitle_c());
			return v;
		}
		
	}
}