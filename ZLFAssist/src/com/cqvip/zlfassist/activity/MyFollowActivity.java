package com.cqvip.zlfassist.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cqvip.zlfassist.R;

public class MyFollowActivity extends ActionBarActivity {
	private ListView lv_category;
	private ListView lv_subcategory;
	private Lv_subcategory_adapter lv_subcategory_adapter;
	private Lv_category_adapter lv_category_adapter;
	private String[] categoryNames={"1","2"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_follow);
		init();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void init() {
		lv_category=(ListView) findViewById(R.id.lv_category);
		lv_subcategory=(ListView) findViewById(R.id.lv_subcategory);
		lv_category_adapter=new Lv_category_adapter(this, categoryNames);
		lv_subcategory_adapter=new Lv_subcategory_adapter(this, categoryNames);
		lv_category.setAdapter(lv_category_adapter);
		lv_subcategory.setAdapter(lv_subcategory_adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_follow, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	static class Lv_category_adapter extends BaseAdapter {
		private Context context;
		private String[] categoryNames;

		public Lv_category_adapter(Context context, String[] categoryNames) {
			this.context = context;
			this.categoryNames = categoryNames;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return categoryNames.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return categoryNames[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder ;
			if(convertView==null){
				convertView=LayoutInflater.from(context).inflate(R.layout.activity_my_follow_item1, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView.findViewById(R.id.tv_category);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
	        holder.title.setText(categoryNames[position]);
			return convertView;
		}

		  static class ViewHolder{
				TextView title;
				}
	}
	
	static class Lv_subcategory_adapter extends BaseAdapter {
		private Context context;
		private String[] categoryNames;
		
		public Lv_subcategory_adapter(Context context, String[] categoryNames) {
			this.context = context;
			this.categoryNames = categoryNames;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return categoryNames.length;
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return categoryNames[position];
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder ;
			if(convertView==null){
				convertView=LayoutInflater.from(context).inflate(R.layout.activity_my_follow_item1, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView.findViewById(R.id.tv_category);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.title.setText(categoryNames[position]);
			return convertView;
		}
		
		static class ViewHolder{
			TextView title;
		}
	}
}
