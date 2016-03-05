package com.samtrest.easy_postboy;

public class RTFTextUnit {
	
	private int rowNum,offset;
	private String text;
	
	public RTFTextUnit(int rowNum, int offset, String text) {
		super();
		this.rowNum = rowNum;
		this.offset = offset;
		this.text = text;
	}
	public int getRowNum() {
		return rowNum;
	}
	public int getOffset() {
		return offset;
	}
	public String getText() {
		return text;
	}
	
	
}
