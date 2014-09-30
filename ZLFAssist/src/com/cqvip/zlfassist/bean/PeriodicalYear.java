package com.cqvip.zlfassist.bean;

public class PeriodicalYear {
	String year;
	String _id;
	String[] num;
	
	public PeriodicalYear() {
	}
	public PeriodicalYear(String year, String _id, String[] num) {
		super();
		this.year = year;
		this._id = _id;
		this.num = num;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String[] getNum() {
		return num;
	}
	public void setNum(String[] num) {
		this.num = num;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	
}
