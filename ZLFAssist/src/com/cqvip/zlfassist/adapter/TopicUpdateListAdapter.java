package com.cqvip.zlfassist.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqvip.zlfassist.R;
import com.cqvip.zlfassist.base.AdapterBase;
import com.cqvip.zlfassist.tools.BaseTools;
import com.cqvip.zlfassist.zkbean.ZKContent;
import com.cqvip.zlfassist.zkbean.ZKTopic;
/**
 * 文章adapter
 * @author luojiang
 *
 */
public class TopicUpdateListAdapter extends AdapterBase<ZKTopic> {
	private Context context;
	public TopicUpdateListAdapter(Context context,ArrayList<ZKTopic> lists){
		this.context = context;
		this.mList =lists;
	}
	/**
	 * 增加更多数据
	 * 
	 * @param moreStatus
	 */
	public void addMoreData(List<ZKTopic> moreStatus) {
		this.mList.addAll(moreStatus);// 把新数据增加到原有集合
		this.notifyDataSetChanged();
	}
	private static class ViewHolder{
		private TextView title;
		private TextView writer;
		private TextView perodical;
		private TextView imburse;
		private TextView abst;
		private TextView keywords;
		private ImageView tipsNew;
	}
	@Override
	protected View getExView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.item_update_content, null);
				holder.title = (TextView) convertView.findViewById(R.id.tv_topic_title);
				holder.writer = (TextView) convertView.findViewById(R.id.tv_topic_writer);
				holder.perodical = (TextView) convertView.findViewById(R.id.tv_topic_perodical);
				holder.imburse = (TextView) convertView.findViewById(R.id.tv_topic_imburse);
				holder.abst = (TextView) convertView.findViewById(R.id.tv_topic_abst);
				holder.keywords = (TextView) convertView.findViewById(R.id.tv_topic_keyword);
				holder.tipsNew = (ImageView) convertView.findViewById(R.id.img_new);
				convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		ZKTopic  item = mList.get(position);
		if(item.isNew()){
			holder.tipsNew.setVisibility(View.VISIBLE);
		}else{
			holder.tipsNew.setVisibility(View.INVISIBLE);
		}
		holder.title.setText(item.getTitleC());
		holder.writer.setText(BaseTools.formContent(item.getShowwriter()));
		holder.perodical.setText(BaseTools.formPero(item.getMediaC())+BaseTools.formSecendContent(item.getMediasQk()));
		if(!TextUtils.isEmpty(item.getImburse())){
		holder.imburse.setVisibility(View.VISIBLE);
		holder.imburse.setText(item.getImburse());
		}else{
			holder.imburse.setVisibility(View.GONE);
		}
		if(!TextUtils.isEmpty(item.getRemarkC())){
			holder.abst.setVisibility(View.VISIBLE);
			holder.abst.setText(item.getRemarkC());
		}else{
			holder.abst.setVisibility(View.GONE);
		}
		holder.keywords.setText(item.getKeywordC());
		return convertView;
	}

	@Override
	protected void onReachBottom() {
		// TODO Auto-generated method stub
		
	}

}
