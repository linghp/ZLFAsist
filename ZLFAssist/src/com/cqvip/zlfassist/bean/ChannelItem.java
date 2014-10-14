package com.cqvip.zlfassist.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/** 
 * ITEM的对应可序化队列属性
 *  */
@DatabaseTable(tableName = "ChannelSort")
public class ChannelItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6465237897027410019L;
	/** 
	 * 栏目对应type
	 *  */
	@DatabaseField(id=true)
	public String type;
	/** 
	 * 栏目对应NAME
	 *  */
	@DatabaseField
	public String name;
	/** 
	 * 栏目在整体中的排序顺序  rank
	 *  */
	public Integer orderId;
	/** 
	 * 栏目是否选中
	 *  */
	public Integer selected;

	public ChannelItem() {
	}

	public ChannelItem(String type, String name, int orderId,int selected) {
		this.type = type;
		this.name = name;
		this.orderId = Integer.valueOf(orderId);
		this.selected = Integer.valueOf(selected);
	}
	public ChannelItem(String type,String name) {
		this.type = type;
		this.name = name;
	}


	public String getName() {
		return this.name;
	}

	public int getOrderId() {
		return this.orderId.intValue();
	}

	public Integer getSelected() {
		return this.selected;
	}


	public void setName(String paramString) {
		this.name = paramString;
	}

	public void setOrderId(int paramInt) {
		this.orderId = Integer.valueOf(paramInt);
	}

	public void setSelected(Integer paramInteger) {
		this.selected = paramInteger;
	}

	public String toString() {
		return "ChannelItem [type=" + this.type + ", name=" + this.name
				+ ", selected=" + this.selected + "]";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof ChannelItem){
			ChannelItem t=(ChannelItem) o;
		//	Log.i("equals", this.id+"----"+t.getId());
			return this.getType().equals(t.getType());
		}
		return super.equals(o);
	}
}