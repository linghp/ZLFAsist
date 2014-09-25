package com.cqvip.zlfassist.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.base.AdapterBase;
import com.cqvip.zlfassist.bean.ItemFollows;

public class ItemFollowsAdapter extends AdapterBase<ItemFollows> {
	private Context context;
	public ItemFollowsAdapter(Context context,List<ItemFollows> lists){
		this.context = context;
		mList =lists;
	}
	private static class ViewHolder{
		private TextView title;
		private TextView subject;
		private TextView about;
	}
	
	@Override
	protected View getExView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.item_follows, null);
				holder.title = (TextView) convertView.findViewById(R.id.tv_follow_title);
				holder.subject = (TextView) convertView.findViewById(R.id.tv_follow_topic);
				holder.about = (TextView) convertView.findViewById(R.id.tv_follow_about);
				//holder.abstrac = (TextView) convertView.findViewById(R.id.tx_item_abstract);
				convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		ItemFollows itemFollows = mList.get(position);
		holder.title.setText(itemFollows.getName());
		if(!TextUtils.isEmpty(itemFollows.getAbout())){
		holder.about.setVisibility(View.VISIBLE);
		holder.about.setText("单位："+itemFollows.getAbout());
		}else{
			holder.about.setVisibility(View.GONE);
		}
		holder.subject.setText("主题："+itemFollows.getSubject());
		return convertView;
	}

	@Override
	protected void onReachBottom() {
		// TODO Auto-generated method stub
		
	}

}
