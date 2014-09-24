package com.cqvip.zlfassist.zkbean;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.cqvip.zlfassist.bean.GeneralResult;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

public class ZkWriter {

	public ZkWriter() {
	}

	@Expose
	private String zkbyinfo;
	@Expose
	private String position;
	@Expose
	private String zkfwcount;
	@Expose
	private String yywriters;
	@Expose
	private String zkfwinfo;
	@Expose
	private String subjects;
	@Expose
	private String classtypes;
	@Expose
	private String zkbycount;
	@Expose
	private String oldorgans;
	@Expose
	private String funds;
	@SerializedName("_id")
	@Expose
	private String id;
	@Expose
	private String oldorganids;
	@Expose
	private String organ;
	@Expose
	private String organid;
	@Expose
	private String studentids;
	@Expose
	private String medias;
	@Expose
	private String bywriters;
	@Expose
	private String writers;
	@Expose
	private String fwdetail;
	@Expose
	private String writer;
	@Expose
	private String tutorids;
	@Expose
	private String zkhindex;
	@Expose
	private String oldwriterids;

	public String getZkbyinfo() {
		return zkbyinfo;
	}

	public void setZkbyinfo(String zkbyinfo) {
		this.zkbyinfo = zkbyinfo;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getZkfwcount() {
		return zkfwcount;
	}

	public void setZkfwcount(String zkfwcount) {
		this.zkfwcount = zkfwcount;
	}

	public String getYywriters() {
		return yywriters;
	}

	public void setYywriters(String yywriters) {
		this.yywriters = yywriters;
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

	public String getOldorgans() {
		return oldorgans;
	}

	public void setOldorgans(String oldorgans) {
		this.oldorgans = oldorgans;
	}

	public String getFunds() {
		return funds;
	}

	public void setFunds(String funds) {
		this.funds = funds;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOldorganids() {
		return oldorganids;
	}

	public void setOldorganids(String oldorganids) {
		this.oldorganids = oldorganids;
	}

	public String getOrgan() {
		return organ;
	}

	public void setOrgan(String organ) {
		this.organ = organ;
	}

	public String getOrganid() {
		return organid;
	}

	public void setOrganid(String organid) {
		this.organid = organid;
	}

	public String getStudentids() {
		return studentids;
	}

	public void setStudentids(String studentids) {
		this.studentids = studentids;
	}

	public String getMedias() {
		return medias;
	}

	public void setMedias(String medias) {
		this.medias = medias;
	}

	public String getBywriters() {
		return bywriters;
	}

	public void setBywriters(String bywriters) {
		this.bywriters = bywriters;
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

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getTutorids() {
		return tutorids;
	}

	public void setTutorids(String tutorids) {
		this.tutorids = tutorids;
	}

	public String getZkhindex() {
		return zkhindex;
	}

	public void setZkhindex(String zkhindex) {
		this.zkhindex = zkhindex;
	}

	public String getOldwriterids() {
		return oldwriterids;
	}

	public void setOldwriterids(String oldwriterids) {
		this.oldwriterids = oldwriterids;
	}
	public static  ArrayList<ZkWriter> formList(String result) throws JSONException{
		GeneralResult gr = new GeneralResult(result);
		String res = gr.getResult();
		if(!TextUtils.isEmpty(res)){
		JSONObject json = new JSONObject(gr.getResult());
		Type listType = new TypeToken<ArrayList<ZkWriter>>(){}.getType();
		ArrayList<ZkWriter> lists= new Gson().fromJson(json.getString("list"), listType);
		return lists;
		}
		return null;
	}

	@Override
	public String toString() {
		return "ZkWriter [zkbyinfo=" + zkbyinfo + ", position=" + position
				+ ", zkfwcount=" + zkfwcount + ", yywriters=" + yywriters
				+ ", zkfwinfo=" + zkfwinfo + ", subjects=" + subjects
				+ ", classtypes=" + classtypes + ", zkbycount=" + zkbycount
				+ ", oldorgans=" + oldorgans + ", funds=" + funds + ", id="
				+ id + ", oldorganids=" + oldorganids + ", organ=" + organ
				+ ", organid=" + organid + ", studentids=" + studentids
				+ ", medias=" + medias + ", bywriters=" + bywriters
				+ ", writers=" + writers + ", fwdetail=" + fwdetail
				+ ", writer=" + writer + ", tutorids=" + tutorids
				+ ", zkhindex=" + zkhindex + ", oldwriterids=" + oldwriterids
				+ "]";
	}
	
}
