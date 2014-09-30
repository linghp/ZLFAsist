package com.cqvip.zlfassist.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;


/**
 * 
 * @author luojiang
 */
public class ZKPeriodical implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5074005664334226259L;
	private String _id;
	private String gch; // id
	private String gch5; // id
	private String titleC; // id
	private String aliasid; // ����
	private String zkfwcount;// Ӣ������
	private String zkfwinfo;// ͼƬ
	private String issn;// ����
	private String subjects; // ͳһ����

	private String organizers;// ���ͳһ����
	private String publisher;// ����
	private String lngrangeid; // ���
	private String zkbycount;// ���ܵ�λ
	private String mediatypeid; // ���쵥λ
	private String zkhindex;// ����
	private String numcount;// �¿�
	private String periodtype;// ���ٿ�
	private String classtypes;
	private String lastnum;
	private String cn;
	private String writers;
	private String impactfactor;
	private ArrayList<PeriodicalYear> yearsnumlist;
	
	public ZKPeriodical() {
		
	}

	public ZKPeriodical(String result) throws JSONException {
		JSONObject json = new JSONObject(result);
		_id = json.getString("_id");
				gch = json.getString("gch");
				gch5 = json.getString("gch5");
				titleC = json.getString("media");
				aliasid = json.getString("aliasid");
				zkfwcount = json.getString("zkfwcount");
				zkfwinfo = json.getString("zkfwinfo");
				issn = json.getString("issn");
				subjects = json.getString("subjects");
				organizers = json.getString("organizers");
				publisher = json.getString("publisher");
				lngrangeid = json.getString("lngrangeid");
				zkbycount = json.getString("zkbycount");
				mediatypeid = json.getString("mediatypeid");
				zkhindex = json.getString("zkhindex");
				numcount = json.getString("numcount");
				periodtype = json.getString("periodtype");
				classtypes = json.getString("classtypes");
				lastnum = json.getString("lastnum");
				cn = json.getString("cn");
				writers = json.getString("writers");
				impactfactor = json.getString("impactfactor");
				yearsnumlist = formList(json.getJSONArray("num"));
			
	}
	public static ZKPeriodical formObject(String result) throws JSONException{
		  GeneralResult gResult = new GeneralResult(result);
		  String res = gResult.getResult();
		  if(!TextUtils.isEmpty(res)){
				JSONObject json = new JSONObject(res);
				return new ZKPeriodical(json.getString("info"));
		  }
		  return null;
	}
	

	private ArrayList<PeriodicalYear> formList(JSONArray array)
			throws JSONException {
		ArrayList<PeriodicalYear> mlists = new ArrayList<PeriodicalYear>();
		int count = array.length();
		if (count <= 0) {
			return null;
		}
		for (int i = 0; i < count; i++) {
				JSONObject js = (JSONObject) array.get(i);
				String year = js.getString("year");
				String tempnum = js.getString("num");
				String tempid = js.getString("id");
				String[] num = tempnum.split(",");
				mlists.add(new PeriodicalYear(year, tempid, num));
		}
		return mlists;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String get_id() {
		return _id;
	}

	public String getGch() {
		return gch;
	}

	public String getGch5() {
		return gch5;
	}

	public String getTitleC() {
		return titleC;
	}

	public String getAliasid() {
		return aliasid;
	}

	public String getZkfwcount() {
		return zkfwcount;
	}

	public String getZkfwinfo() {
		return zkfwinfo;
	}

	public String getIssn() {
		return issn;
	}

	public String getSubjects() {
		return subjects;
	}

	public String getOrganizers() {
		return organizers;
	}

	public String getPublisher() {
		return publisher;
	}

	public String getLngrangeid() {
		return lngrangeid;
	}

	public String getZkbycount() {
		return zkbycount;
	}

	public String getMediatypeid() {
		return mediatypeid;
	}

	public String getZkhindex() {
		return zkhindex;
	}

	public String getNumcount() {
		return numcount;
	}

	public String getPeriodtype() {
		return periodtype;
	}

	public String getClasstypes() {
		return classtypes;
	}

	public String getLastnum() {
		return lastnum;
	}

	public String getCn() {
		return cn;
	}

	public String getWriters() {
		return writers;
	}

	public String getImpactfactor() {
		return impactfactor;
	}

	public ArrayList<PeriodicalYear> getYearsnumlist() {
		return yearsnumlist;
	}

}
