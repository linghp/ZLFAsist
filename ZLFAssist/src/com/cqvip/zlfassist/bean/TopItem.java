package com.cqvip.zlfassist.bean;
/**
 * 总分类
 * @author luojiang
 *
 */
public class TopItem {
	private String Name;
	private String Type;
	
	public TopItem() {
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	@Override
	public String toString() {
		return "ShortItem [Name=" + Name + ", Type=" + Type + "]";
	}


	
}
