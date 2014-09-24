package com.cqvip.zlfassist.zkbean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ZkDoamin {

	public ZkDoamin() {
	}

	@Expose
	private String zkbyinfo;
	@Expose
	private String zkfwcount;
	@Expose
	private String zkfwinfo;
	@Expose
	private String subjects;
	@Expose
	private String childclassids;
	@Expose
	private String classtypename;
	@Expose
	private String classtypes;
	@Expose
	private String zkbycount;
	@Expose
	private String funds;
	@Expose
	private String level;
	@SerializedName("_id")
	@Expose
	private String id;
	@Expose
	private String classidlevel;
	@Expose
	private String medias;
	@Expose
	private String organs;
	@Expose
	private String writers;
	@Expose
	private String fwdetail;
	@Expose
	private String zkhindex;

	public String getZkbyinfo() {
		return zkbyinfo;
	}

	public void setZkbyinfo(String zkbyinfo) {
		this.zkbyinfo = zkbyinfo;
	}

	public String getZkfwcount() {
		return zkfwcount;
	}

	public void setZkfwcount(String zkfwcount) {
		this.zkfwcount = zkfwcount;
	}

	public String getZkfwinfo() {
		return zkfwinfo;
	}

	public void setZkfwinfo(String zkfwinfo) {
		this.zkfwinfo = zkfwinfo;
	}

	public String getSubjects() {
		return subjects;
	}

	public void setSubjects(String subjects) {
		this.subjects = subjects;
	}

	public String getChildclassids() {
		return childclassids;
	}

	public void setChildclassids(String childclassids) {
		this.childclassids = childclassids;
	}

	public String getClasstypename() {
		return classtypename;
	}

	public void setClasstypename(String classtypename) {
		this.classtypename = classtypename;
	}

	public String getClasstypes() {
		return classtypes;
	}

	public void setClasstypes(String classtypes) {
		this.classtypes = classtypes;
	}

	public String getZkbycount() {
		return zkbycount;
	}

	public void setZkbycount(String zkbycount) {
		this.zkbycount = zkbycount;
	}

	public String getFunds() {
		return funds;
	}

	public void setFunds(String funds) {
		this.funds = funds;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClassidlevel() {
		return classidlevel;
	}

	public void setClassidlevel(String classidlevel) {
		this.classidlevel = classidlevel;
	}

	public String getMedias() {
		return medias;
	}

	public void setMedias(String medias) {
		this.medias = medias;
	}

	public String getOrgans() {
		return organs;
	}

	public void setOrgans(String organs) {
		this.organs = organs;
	}

	public String getWriters() {
		return writers;
	}

	public void setWriters(String writers) {
		this.writers = writers;
	}

	public String getFwdetail() {
		return fwdetail;
	}

	public void setFwdetail(String fwdetail) {
		this.fwdetail = fwdetail;
	}

	public String getZkhindex() {
		return zkhindex;
	}

	public void setZkhindex(String zkhindex) {
		this.zkhindex = zkhindex;
	}

}
