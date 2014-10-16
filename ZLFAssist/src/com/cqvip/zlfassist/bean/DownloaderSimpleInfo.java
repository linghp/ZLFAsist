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
	@DatabaseField(id=true)
	public String id;
	/** 
	 * 对应NAME
	 *  */
	@DatabaseField
	public String name;
	/** 
	 * url
	 *  */
	@DatabaseField
	public String url;


	public DownloaderSimpleInfo() {
		
	}


	public DownloaderSimpleInfo(String id, String name, String url) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
	}


}