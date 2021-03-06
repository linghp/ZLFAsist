package com.cqvip.zlfassist.base;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class AdapterBase<T> extends BaseAdapter {
	
	protected List<T> mList = new ArrayList<T>();
	private Context context;
	
	public List<T> getList(){
		return mList;
	}
	
	public List<T> getmList() {
		return mList;
	}

	public void setmList(List<T> mList) {
		this.mList = mList;
	}

	public void appendToList(List<T> list) {
		if (list == null) {
			return;
		}
		mList.addAll(list);
		notifyDataSetChanged();
	}

	public void appendToTopList(List<T> list) {
		if (list == null) {
			return;
		}
		mList.addAll(0, list);
		notifyDataSetChanged();
	}

	public void clear() {
		mList.clear();
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mList==null){
			return 0;
		}
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
//		if(position > mList.size()-1){
//			return null;
//		}
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
//		if (position == getCount() - 1) {
//			onReachBottom();
//		}
		return getExView(position, convertView, parent);
	}
	

	protected abstract View getExView(int position, View convertView, ViewGroup parent);
	protected abstract void onReachBottom();


}
