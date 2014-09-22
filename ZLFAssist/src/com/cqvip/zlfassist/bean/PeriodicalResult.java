package com.cqvip.zlfassist.bean;

import java.util.ArrayList;
/**
 * 
 * @author luojiang
 *
 */
public class PeriodicalResult<T> {
	    	  
	  private boolean success;
	   private String message; 
	   private int recordcount; 
	   private ArrayList<T> qklist; 
	   

	public PeriodicalResult() {
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getRecordcount() {
		return recordcount;
	}

	public void setRecordcount(int recordcount) {
		this.recordcount = recordcount;
	}

	public ArrayList<T> getQklist() {
		return qklist;
	}

	public void setQklist(ArrayList<T> qklist) {
		this.qklist = qklist;
	}
	   
		   
}
