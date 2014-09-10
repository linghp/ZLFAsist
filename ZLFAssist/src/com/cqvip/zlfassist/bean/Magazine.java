package com.cqvip.zlfassist.bean;

import java.util.ArrayList;

public class Magazine {
	    	  
	  private boolean success;
	   private String message; 
	   private int recordcount; 
	   private ArrayList<ChildMagzine> qklist; 
	   
	   public Magazine() {
	
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

	public ArrayList<ChildMagzine> getQklist() {
		return qklist;
	}

	public class ChildMagzine{
		   public String gch;
		   public String name;
		   public String ename;
		   public String imgurl;
		   public boolean isrange;
		   public String cnno;
		   public String issn;
		   public String changestate;
		   
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
