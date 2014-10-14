package com.cqvip.zlfassist.adapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
	/**
	 * 增加更多数据
	 * 
	 * @param moreStatus
	 */
	public void addMoreData(List<ItemFollows> moreStatus) {
		this.mList.addAll(moreStatus);// 把新数据增加到原有集合
		this.notifyDataSetChanged();
	}
	private static class ViewHolder{
		private TextView title;
		private TextView subject;
		private TextView about;
		private ImageView tipsNew;
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
				holder.tipsNew = (ImageView) convertView.findViewById(R.id.img_new);
				//holder.abstrac = (TextView) convertView.findViewById(R.id.tx_item_abstract);
				convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		ItemFollows itemFollows = mList.get(position);
		if(itemFollows.isNew()){
			holder.tipsNew.setVisibility(View.VISIBLE);
		}else{
			holder.tipsNew.setVisibility(View.INVISIBLE);
		}
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
