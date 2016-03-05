package com.samtrest.easy_postboy;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class TemplateTag {
	private String tag;
	private List<RTFTextUnit> units;	

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
	public String toString(){
		String str = "";
		int curPos = 0,begPos = 0;
		curPos = tag.indexOf("\\'", begPos);
		while (curPos != -1){
			int c = (Integer.parseInt("05"+tag.substring(curPos+2,curPos+4), 16)-16);
			str += (char)c;
			begPos += 4;
			curPos = tag.indexOf('\\', begPos);
		}
		System.out.println(str);
		return str;
	}
}
