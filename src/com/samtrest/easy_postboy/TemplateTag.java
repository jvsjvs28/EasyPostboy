package com.samtrest.easy_postboy;

import java.util.ArrayList;
import java.util.List;

public class TemplateTag {
	String tag;
	List<RTFTextUnit> units;	
	
	public TemplateTag() {
		super();
		units = new ArrayList<RTFTextUnit>() ;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public List<RTFTextUnit> getUnits() {
		return units;
	}
	public void setUnits(List<RTFTextUnit> units) {
		this.units = units;
	}
	
	

}
