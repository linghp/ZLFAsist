package com.cqvip.zlfassist.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cqvip.zlfassist.R;

public class AddFollowActivity extends ActionBarActivity implements
		OnItemClickListener,OnClickListener {
	private ListView lv_category,lv_subcategory,lv_search;
	private Lv_subcategory_adapter lv_subcategory_adapter;
	private Lv_category_adapter lv_category_adapter;
	private String[] categoryNames = { "1", "2", "2", "2", "2", "2", "2", "2",
			"2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2",
			"2", "2", "2", "2", "2", "2", "2", "2", "2", "2" };
	public final static String TAG = "AddFollowActivity";
	private List<String> subcategoryNameList;
	private SearchView searchView;
	//private boolean isfirstsearch_searchview=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_follow);
		init();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void init() {
		lv_category = (ListView) findViewById(R.id.lv_category);
		lv_subcategory = (ListView) findViewById(R.id.lv_subcategory);
		lv_search = (ListView) findViewById(R.id.lv_search);
		lv_category_adapter = new Lv_category_adapter(this, categoryNames);
		subcategoryNameList=testData();
		lv_subcategory_adapter = new Lv_subcategory_adapter(this, subcategoryNameList);
		lv_category.setAdapter(lv_category_adapter);
		lv_subcategory.setAdapter(lv_subcategory_adapter);
		lv_category_adapter.setSelectItem(0);
		lv_category.setOnItemClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_follow, menu);
        searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(mOnQueryTextListener);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
			
			@Override
			public boolean onClose() {
				lv_search.setVisibility(View.GONE);
				lv_search.startAnimation(AnimationUtils.loadAnimation(
			    		AddFollowActivity.this, R.anim.header_disappear));
				return false;
			}
		});
		return true;
	}
	
    // The following callbacks are called for the SearchView.OnQueryChangeListener
    // For more about using SearchView, see src/.../view/SearchView1.java and SearchView2.java
    private final SearchView.OnQueryTextListener mOnQueryTextListener =
            new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextChange(String newText) {
        lv_search.setAdapter(new Lv_search_adapter(AddFollowActivity.this, subcategoryNameList));   
        if(lv_search.getVisibility()==View.GONE){
        lv_search.setVisibility(View.VISIBLE);
		lv_search.startAnimation(AnimationUtils.loadAnimation(
	    		AddFollowActivity.this, R.anim.header_appear));
        }
            return true;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            Toast.makeText(AddFollowActivity.this,
                    "Searching for: " + query + "...", Toast.LENGTH_SHORT).show();
            return true;
        }
    };

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
		private int selectItem = -1;

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
			//注释此块是因为当点击item后，滑动listview，有的item值为空，不解。 
			// final ViewHolder holder;
			// if (convertView == null) {
			// convertView = LayoutInflater.from(context).inflate(
			// R.layout.activity_my_follow_item1, null);
			// holder = new ViewHolder();
			// holder.title = (TextView) convertView
			// .findViewById(R.id.tv_category);
			// convertView.setTag(holder);
			// } else {
			// holder = (ViewHolder) convertView.getTag();
			// }
			// if (position == selectItem) {
			// holder.title.setBackgroundColor(Color.RED);
			// } else {
			// holder.title.setBackgroundColor(Color.WHITE);
			// }
			// Log.i(TAG, position+"--lv_category_getView");
			// holder.title.setText(categoryNames[position]);
			//if(convertView==null)
			convertView = LayoutInflater.from(context).inflate(R.layout.activity_my_follow_item1, null);
			TextView title = (TextView) convertView
					.findViewById(R.id.tv_category);
			if (position == selectItem) {
				title.setBackgroundColor(Color.RED);
			} else {
				title.setBackgroundColor(Color.WHITE);
			}
			//Log.i(TAG, position + "--lv_category_getView");
			title.setText(categoryNames[position]);
			return convertView;
		}

		public void setSelectItem(int selectItem) {
			this.selectItem = selectItem;
		    this.notifyDataSetChanged();
		}
	}

	static class Lv_subcategory_adapter extends BaseAdapter {
		private Context context;
		private List<String> subcategoryNames;

		public Lv_subcategory_adapter(Context context, List<String> subcategoryNames) {
			this.context = context;
			this.subcategoryNames = subcategoryNames;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return subcategoryNames.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return subcategoryNames.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.activity_my_follow_item2, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView.findViewById(R.id.tv_subcategory);
				holder.iv_add=(ImageView) convertView.findViewById(R.id.iv_add);
				holder.iv_add.setOnClickListener((AddFollowActivity)context);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.title.setText(subcategoryNames.get(position));
			return convertView;
		}

		static class ViewHolder {
			TextView title;
			ImageView iv_add;
		}
	}
	static class Lv_search_adapter extends BaseAdapter {
		private Context context;
		private List<String> subcategoryNames;

		public Lv_search_adapter(Context context, List<String> subcategoryNames) {
			this.context = context;
			this.subcategoryNames = subcategoryNames;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return subcategoryNames.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return subcategoryNames.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.activity_my_follow_item2, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView.findViewById(R.id.tv_subcategory);
				holder.iv_add=(ImageView) convertView.findViewById(R.id.iv_add);
				holder.iv_add.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Log.i(TAG, "onClick--iv_add");
						Animation animation=AnimationUtils.loadAnimation(context, R.anim.base_loading_small_anim); 
						v.setBackgroundResource(R.drawable.base_loading_small_icon);
						v.startAnimation(animation);
						final View temp_v=v;
						animation.setAnimationListener(new AnimationListener() {
							
							@Override
							public void onAnimationStart(Animation animation) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onAnimationRepeat(Animation animation) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onAnimationEnd(Animation animation) {
								temp_v.setBackgroundResource(R.drawable.biz_news_column_subscribe_cancel);
							}
						});
					}
				});
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.title.setText(subcategoryNames.get(position));
			return convertView;
		}

		static class ViewHolder {
			TextView title;
			ImageView iv_add;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.lv_category:
			lv_category_adapter.setSelectItem(position);
			//Log.i(TAG + "onItemClick", position + "--lv_category_onItemClick");
			//lv_category_adapter.notifyDataSetChanged();
			subcategoryNameList.clear();
			subcategoryNameList.addAll(testData());
			lv_subcategory.setAdapter(lv_subcategory_adapter);
			//lv_subcategory_adapter.notifyDataSetChanged();
			break;

		default:
			break;
		}
	}
	
	private List<String> testData(){
		List<String> list=new  ArrayList<String>();
		for (int i = 0; i <30; i++) {
			list.add(new Random().nextInt(100)+"");
		}
		return list;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_add:
			Log.i(TAG, "onClick--iv_add");
			Animation animation=AnimationUtils.loadAnimation(this, R.anim.base_loading_small_anim); 
			v.setBackgroundResource(R.drawable.base_loading_small_icon);
			v.startAnimation(animation);
			final View temp_v=v;
			animation.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					temp_v.setBackgroundResource(R.drawable.biz_news_column_subscribe_cancel);
				}
			});
			break;

		default:
			break;
		}
	}
}
