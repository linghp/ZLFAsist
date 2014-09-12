package com.cqvip.zlfassist.bean;

import java.util.ArrayList;
/**
 * 
 * @author luojiang
 *
 */
public class PeriodicalResult {
	    	  
	  private boolean success;
	   private String message; 
	   private int recordcount; 
	   private ArrayList<Periodical> qklist; 
	   
	   public PeriodicalResult() {
	
	}
	   
	   public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}

	public int getRecordcount() {
		return recordcount;
	}

	public ArrayList<Periodical> getQklist() {
		return qklist;
	}

	public class Periodical{
		   public String gch;
		   public String name;
		   public String ename;
		   public String imgurl;
		   public boolean isrange;
		   public String cnno;
		   public String issn;
		   public String changestate;
		   
		   
		public String getGch() {
			return gch;
		}


		public void setGch(String gch) {
			this.gch = gch;
		}


		public String getName() {
			return name;
		}


		public void setName(String name) {
			this.name = name;
		}


		public String getEname() {
			return ename;
		}


		public void setEname(String ename) {
			this.ename = ename;
		}


		public String getImgurl() {
			return imgurl;
		}


		public void setImgurl(String imgurl) {
			this.imgurl = imgurl;
		}


		public boolean isIsrange() {
			return isrange;
		}


		public void setIsrange(boolean isrange) {
			this.isrange = isrange;
		}


		public String getCnno() {
			return cnno;
		}


		public void setCnno(String cnno) {
			this.cnno = cnno;
		}


		public String getIssn() {
			return issn;
		}


		public void setIssn(String issn) {
			this.issn = issn;
		}


		public String getChangestate() {
			return changestate;
		}


		public void setChangestate(String changestate) {
			this.changestate = changestate;
		}


		@Override
		public String toString() {
			return "ChildMagzine [gch=" + gch + ", name=" + name + ", ename="
					+ ename + ", imgurl=" + imgurl + ", isrange=" + isrange
					+ ", cnno=" + cnno + ", issn=" + issn + ", changestate="
					+ changestate + "]";
		}

		   
	   
	   }

	@Override
	public String toString() {
		return "Magazine [success=" + success + ", message=" + message
				+ ", recordcount=" + recordcount + ", qklist=" + qklist + "]";
	}
	   
		   
}
