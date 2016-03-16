package com.samtrest.easy_postboy;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class TemplateTag {
	private String tag;
	private String mapTag;
	private String dataTag;
	private List<RTFTextUnit> units;	

	public TemplateTag() {
		super();
		units = new ArrayList<RTFTextUnit>() ;
	}

	public String toString(){
		String str = "";
		int curPos = 0,begPos = 0;
		curPos = tag.indexOf("\\'", begPos);
		while (true){
			if(curPos == -1){
				str += tag.substring(begPos);
				break;
			}else{
				str += tag.substring(begPos, curPos);
				int c = (Integer.parseInt("05"+tag.substring(curPos+2,curPos+4), 16)-16);
				str += (char)c;
				begPos = curPos + 4;
				curPos = tag.indexOf("\\'", begPos);
			}
		}
		return str;
	}
	
	public String getTagValue() {
		return dataTag;
	}
	
	public String getMapTag() {
		return mapTag;
	}
	
	public void setMapTag(String mapTag) {
		this.mapTag = mapTag;
	}
	
	public String getDataTag() {
		return dataTag;
	}
	
	public void setDataTag(String dataTag) {
		this.dataTag = dataTag;
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
