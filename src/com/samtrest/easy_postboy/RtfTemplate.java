package com.samtrest.easy_postboy;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RtfTemplate {
	List<String> lines;
	List<TemplateTag> tags;
	StringBuffer txt;
	static  Logger log = LoggerFactory.getLogger(RtfTemplate.class );
	int offset=0, rowNumber=0;

	public RtfTemplate(File file){
		try {
			Scanner sc = new Scanner(file);
			populateLines(sc);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public RtfTemplate(String templateText){
		Scanner sc =  new Scanner(templateText);
		populateLines(sc);
	}
	
	private void populateLines(Scanner sc){
		lines = new ArrayList<String>();
		txt = new StringBuffer();
		while (sc.hasNextLine()) {
			String string = sc.nextLine();
			lines.add(string);
			txt.append(string);
		}
		sc.close();
		populateTagsFromTemplate();		
		if (log.isDebugEnabled()){
			printTags();
		}
	}
	
	private void printTags(){
		for (TemplateTag tag : tags) {
			log.trace("Tag: {} {}",tag.toString(),tag.getTag());
			for (RTFTextUnit unit : tag.getUnits()) {
				log.trace("  Fragment: {} {} {}",unit.getRowNum(),
						  unit.getOffset(),unit.getText());
			}
		}
	}

	private TemplateTag getNextTag(int offset,int rowNumber){
		TemplateTag tag = new TemplateTag();
		String tagStr = "",line="",token,firstToken="";
		int currPosition=0;

		for (int i = rowNumber; i < lines.size(); i++) {
			offset = lines.get(i).indexOf(" "+Sets.BEGIN_TAG_DELIMITER,offset);
			if(offset != -1){
				rowNumber = i;
				break;
			}
		}
		if (offset == -1){
			return null;
		}
		tagStr = Sets.BEGIN_TAG_DELIMITER;
		tag.getUnits().add(new RTFTextUnit(rowNumber,offset,Sets.BEGIN_TAG_DELIMITER));
		currPosition = offset + Sets.BEGIN_TAG_DELIMITER.length()+1;

		for (int i = rowNumber; i < lines.size(); i++) {
			line = lines.get(i).substring(currPosition);
			StringTokenizer tokenizer = new StringTokenizer(line," ",true);
			while(tokenizer.hasMoreTokens()){
				token = tokenizer.nextToken();
				int pos = token.indexOf('}');
				if (pos > 0){
					firstToken = token.substring(0,pos);
				}else{
					firstToken = token;
				}
				if ( !" ".equals(firstToken) &&
					 !firstToken.startsWith("\\")&& 
					 !firstToken.startsWith("}") || 
						(firstToken.startsWith("\\tab") || 
						 firstToken.startsWith("\\'"))){
					tagStr += firstToken;
					tag.getUnits().add(new RTFTextUnit(i,currPosition,firstToken));
				}
				currPosition += firstToken.length();
				if (firstToken.endsWith(Sets.END_TAG_DELIMITER)){
					break;
				}
			}
			if (firstToken.endsWith(Sets.END_TAG_DELIMITER)){
				break;
			}
			currPosition = 0;
		}
		offset = currPosition;
		tag.setTag(tagStr);
		return tag;
	}

	private void populateTagsFromTemplate(){
		tags =  new ArrayList<TemplateTag>();
		TemplateTag tag = getNextTag(offset, rowNumber);
		while (tag != null){
			tags.add(tag);
			rowNumber = tag.getUnits().get(tag.getUnits().size()-1).getRowNum();
			offset = tag.getUnits().get(tag.getUnits().size()-1).getOffset()+1;
			log.trace("Line: {}, offset:{}, Tag: {}",rowNumber,offset,tag.getTag());
			tag = getNextTag(offset, rowNumber);
		}
	}

	public List<String> getLines() {
		return lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

	public List<TemplateTag> getTags() {
		return tags;
	}

	public void setTags(List<TemplateTag> tags) {
		this.tags = tags;
	}

	public StringBuffer getTxt() {
		return txt;
	}

	public void setTxt(StringBuffer txt) {
		this.txt = txt;
	}
}
