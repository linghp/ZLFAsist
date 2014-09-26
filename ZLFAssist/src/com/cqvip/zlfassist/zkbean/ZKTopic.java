package com.cqvip.zlfassist.zkbean;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.cqvip.zlfassist.bean.GeneralResult;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

public class ZKTopic implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2693691966425442702L;

	public ZKTopic() {
	}

	@SerializedName("keyword_e")
	@Expose
	private String keywordE;
	@Expose
	private String zkcouplingcount;
	@Expose
	private String volumn;
	@SerializedName("keyword_c")
	@Expose
	private String keywordC;
	@Expose
	private String zkrefercount;
	@Expose
	private String issn;
	@Expose
	private String range;
	@Expose
	private String subjectnum;
	@Expose
	private String endpage;
	@Expose
	private String beginpage;
	@Expose
	private String type;
	@Expose
	private String processdate;
	@SerializedName("title_e")
	@Expose
	private String titleE;
	@Expose
	private String zkbycount;
	@Expose
	private String vol;
	@SerializedName("title_c")
	@Expose
	private String titleC;
	@SerializedName("_id")
	@Expose
	private String id;
	@Expose
	private String showwriter;
	@SerializedName("author_e")
	@Expose
	private String authorE;
	@Expose
	private String pagecount;
	@Expose
	private String organs;
	@SerializedName("zkbycount_sec")
	@Expose
	private String zkbycountSec;
	@SerializedName("media_e")
	@Expose
	private String mediaE;
	@SerializedName("media_c")
	@Expose
	private String mediaC;
	@Expose
	private String imburse;
	@SerializedName("zkrefercount_sec")
	@Expose
	private String zkrefercountSec;
	@SerializedName("medias_qk")
	@Expose
	private String mediasQk;
	@SerializedName("class")
	@Expose
	private String _class;
	@Expose
	private String zkstrbyids;
	@Expose
	private String specialnum;
	@Expose
	private String classtypes;
	@Expose
	private String num;
	@SerializedName("remark_e")
	@Expose
	private String remarkE;
	@Expose
	private String showorgan;
	@Expose
	private String zkbycouplingcount;
	@SerializedName("remark_c")
	@Expose
	private String remarkC;
	@Expose
	private String writers;
	@Expose
	private String years;
	@Expose
	private String gch;
	@SerializedName("zkreferids_real")
	@Expose
	private String zkreferidsReal;

	public String getKeywordE() {
		return keywordE;
	}

	public void setKeywordE(String keywordE) {
		this.keywordE = keywordE;
	}

	public String getZkcouplingcount() {
		return zkcouplingcount;
	}

	public void setZkcouplingcount(String zkcouplingcount) {
		this.zkcouplingcount = zkcouplingcount;
	}

	public String getVolumn() {
		return volumn;
	}

	public void setVolumn(String volumn) {
		this.volumn = volumn;
	}

	public String getKeywordC() {
		return keywordC;
	}

	public void setKeywordC(String keywordC) {
		this.keywordC = keywordC;
	}

	public String getZkrefercount() {
		return zkrefercount;
	}

	public void setZkrefercount(String zkrefercount) {
		this.zkrefercount = zkrefercount;
	}

	public String getIssn() {
		return issn;
	}

	public void setIssn(String issn) {
		this.issn = issn;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getSubjectnum() {
		return subjectnum;
	}

	public void setSubjectnum(String subjectnum) {
		this.subjectnum = subjectnum;
	}

	public String getEndpage() {
		return endpage;
	}

	public void setEndpage(String endpage) {
		this.endpage = endpage;
	}

	public String getBeginpage() {
		return beginpage;
	}

	public void setBeginpage(String beginpage) {
		this.beginpage = beginpage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProcessdate() {
		return processdate;
	}

	public void setProcessdate(String processdate) {
		this.processdate = processdate;
	}

	public String getTitleE() {
		return titleE;
	}

	public void setTitleE(String titleE) {
		this.titleE = titleE;
	}

	public String getZkbycount() {
		return zkbycount;
	}

	public void setZkbycount(String zkbycount) {
		this.zkbycount = zkbycount;
	}

	public String getVol() {
		return vol;
	}

	public void setVol(String vol) {
		this.vol = vol;
	}

	public String getTitleC() {
		return titleC;
	}

	public void setTitleC(String titleC) {
		this.titleC = titleC;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShowwriter() {
		return showwriter;
	}

	public void setShowwriter(String showwriter) {
		this.showwriter = showwriter;
	}

	public String getAuthorE() {
		return authorE;
	}

	public void setAuthorE(String authorE) {
		this.authorE = authorE;
	}

	public String getPagecount() {
		return pagecount;
	}

	public void setPagecount(String pagecount) {
		this.pagecount = pagecount;
	}

	public String getOrgans() {
		return organs;
	}

	public void setOrgans(String organs) {
		this.organs = organs;
	}

	public String getZkbycountSec() {
		return zkbycountSec;
	}

	public void setZkbycountSec(String zkbycountSec) {
		this.zkbycountSec = zkbycountSec;
	}

	public String getMediaE() {
		return mediaE;
	}

	public void setMediaE(String mediaE) {
		this.mediaE = mediaE;
	}

	public String getMediaC() {
		return mediaC;
	}

	public void setMediaC(String mediaC) {
		this.mediaC = mediaC;
	}

	public String getImburse() {
		return imburse;
	}

	public void setImburse(String imburse) {
		this.imburse = imburse;
	}

	public String getZkrefercountSec() {
		return zkrefercountSec;
	}

	public void setZkrefercountSec(String zkrefercountSec) {
		this.zkrefercountSec = zkrefercountSec;
	}

	public String getMediasQk() {
		return mediasQk;
	}

	public void setMediasQk(String mediasQk) {
		this.mediasQk = mediasQk;
	}

	public String getClass_() {
		return _class;
	}

	public void setClass_(String _class) {
		this._class = _class;
	}

	public String getZkstrbyids() {
		return zkstrbyids;
	}

	public void setZkstrbyids(String zkstrbyids) {
		this.zkstrbyids = zkstrbyids;
	}

	public String getSpecialnum() {
		return specialnum;
	}

	public void setSpecialnum(String specialnum) {
		this.specialnum = specialnum;
	}

	public String getClasstypes() {
		return classtypes;
	}

	public void setClasstypes(String classtypes) {
		this.classtypes = classtypes;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getRemarkE() {
		return remarkE;
	}

	public void setRemarkE(String remarkE) {
		this.remarkE = remarkE;
	}

	public String getShoworgan() {
		return showorgan;
	}

	public void setShoworgan(String showorgan) {
		this.showorgan = showorgan;
	}

	public String getZkbycouplingcount() {
		return zkbycouplingcount;
	}

	public void setZkbycouplingcount(String zkbycouplingcount) {
		this.zkbycouplingcount = zkbycouplingcount;
	}

	public String getRemarkC() {
		return remarkC;
	}

	public void setRemarkC(String remarkC) {
		this.remarkC = remarkC;
	}

	public String getWriters() {
		return writers;
	}

	public void setWriters(String writers) {
		this.writers = writers;
	}

	public String getYears() {
		return years;
	}

	public void setYears(String years) {
		this.years = years;
	}

	public String getGch() {
		return gch;
	}

	public void setGch(String gch) {
		this.gch = gch;
	}

	public String getZkreferidsReal() {
		return zkreferidsReal;
	}

	public void setZkreferidsReal(String zkreferidsReal) {
		this.zkreferidsReal = zkreferidsReal;
	}
	public   static ArrayList<ZKTopic> formList(String result) throws JSONException{
		GeneralResult gr = new GeneralResult(result);
		String res = gr.getResult();
		Log.i("fromJson", res);
		if(!TextUtils.isEmpty(res)){
			JSONObject json = new JSONObject(gr.getResult());
			Type listType = new TypeToken<ArrayList<ZKTopic>>(){}.getType();
			ArrayList<ZKTopic> lists= new Gson().fromJson(json.getString("list"), listType);
			Log.i("fromJson","lsits"+lists.size());
			return lists;
		}
		return null;
	}
}
