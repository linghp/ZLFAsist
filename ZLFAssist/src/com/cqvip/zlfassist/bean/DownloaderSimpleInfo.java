package com.cqvip.zlfassist.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/** 
 * 
 *  */
public class DownloaderSimpleInfo {

	/** 
	 * 下载的id
	 *  */
	@DatabaseField(id=true)
	public long downloadId;
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
	@DatabaseField
	public String url;
	
	@DatabaseField
	private long datetime;
	
	@DatabaseField
	private String filepath;


	public String getFilepath() {
		return filepath;
	}


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


	public long getDownloadId() {
		return downloadId;
	}


	public long getDatetime() {
		return datetime;
	}


	public DownloaderSimpleInfo(long downloadId,String id, String name, String url) {
		super();
		this.downloadId = downloadId;
		this.id = id;
		this.name = name;
		this.url = url;
		this.datetime=System.currentTimeMillis();
	}

	public DownloaderSimpleInfo(long downloadId, String id, String name,
			String url, long datetime, String filepath) {
		super();
		this.downloadId = downloadId;
		this.id = id;
		this.name = name;
		this.url = url;
		this.datetime = datetime;
		this.filepath = filepath;
	}


	@Override
	public boolean equals(Object o) {
		if(o instanceof DownloaderSimpleInfo){
			DownloaderSimpleInfo t=(DownloaderSimpleInfo) o;
		//	Log.i("equals", this.id+"----"+t.getId());
			return this.getDownloadId()==t.getDownloadId();
		}
		return super.equals(o);
	}

}