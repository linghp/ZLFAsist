package com.cqvip.zlfassist.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/** 
 * 
 *  */
public class DownloaderSimpleInfo {

	/** 
	 * 对应id
	 *  */
	@DatabaseField
	public String id;
	/** 
	 * 对应NAME
	 *  */
	@DatabaseField
	public String name;
	/** 
	 * url
	 *  */
	@DatabaseField(id=true)
	public String url;
	
	@DatabaseField
	private long datetime;


	public DownloaderSimpleInfo() {
		
	}


	public String getId() {
		return id;
	}


	public String getName() {
		return name;
	}


	public String getUrl() {
		return url;
	}


	public DownloaderSimpleInfo(String id, String name, String url) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
		this.datetime=System.currentTimeMillis();
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof DownloaderSimpleInfo){
			DownloaderSimpleInfo t=(DownloaderSimpleInfo) o;
		//	Log.i("equals", this.id+"----"+t.getId());
			return this.getUrl().equals(t.getUrl());
		}
		return super.equals(o);
	}

}