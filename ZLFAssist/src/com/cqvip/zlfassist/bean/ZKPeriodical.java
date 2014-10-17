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
	private String gch; // 
	private String gch5; // 
	private String titleC; // 
	private String aliasid; // 
	private String zkfwcount;// 
	private String zkfwinfo;// 
	private String issn;// 
	private String subjects; // 

	private String organizers;// 
	private String publisher;// 
	private String lngrangeid; // 
	private String zkbycount;// 
	private String mediatypeid; 
	private String zkhindex;// 
	private String numcount;// 
	private String periodtype;// 
	private String classtypes;
	private String funds;
	private String lastnum;
	private String cn;
	private String writers;
	private String impactfactor;
	private String mediapic;//图片
	private ArrayList<PeriodicalYear> yearsnumlist;
	
	public ZKPeriodical() {
		
	}

	public ZKPeriodical(String result) throws JSONException {
		JSONObject mJsonObjectjson = new JSONObject(result);
		JSONObject json = mJsonObjectjson.getJSONObject("obj");
		JSONArray jsonArray = mJsonObjectjson.getJSONArray("num");
		_id = checkJson(json,"_id");
				gch = checkJson(json,"gch");
				gch5 = checkJson(json,"gch5");
				titleC = checkJson(json,"media");
				aliasid = checkJson(json,"aliasid");
				zkfwcount = checkJson(json,"zkfwcount");
				zkfwinfo = checkJson(json,"zkfwinfo");
				issn = checkJson(json,"issn");
				subjects = checkJson(json,"subjects");
				organizers = checkJson(json,"organizers");
				publisher = checkJson(json,"publisher");
				lngrangeid = checkJson(json,"lngrangeid");
				zkbycount = checkJson(json,"zkbycount");
				mediatypeid = checkJson(json,"mediatypeid");
				zkhindex = checkJson(json,"zkhindex");
				numcount = checkJson(json,"numcount");
				periodtype = checkJson(json,"periodtype");
				classtypes = checkJson(json,"classtypes");
				funds = checkJson(json,"funds");
				lastnum = checkJson(json,"lastnum");
				cn = checkJson(json,"cn");
				writers = checkJson(json,"writers");
				impactfactor = checkJson(json,"impactfactor");
				mediapic = checkJson(json,"mediapic");
				yearsnumlist = formList(jsonArray);
			
	}

	private String  checkJson(JSONObject json,String name) throws JSONException {
		if(!json.isNull(name)){
		return  json.getString(name);
		}
		return null;
	}
	public static ZKPeriodical formObject(String result) throws JSONException{
		  GeneralResult gResult = new GeneralResult(result);
		  String res = gResult.getResult();
		  if(!TextUtils.isEmpty(res)){
				//JSONObject json = new JSONObject(res);
				return new ZKPeriodical(res);
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
				String tempid = js.getString("gch5");
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

	public String getFunds() {
		return funds;
	}

	public String getMediapic() {
		return mediapic;
	}

}
