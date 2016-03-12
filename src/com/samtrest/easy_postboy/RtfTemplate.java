package com.samtrest.easy_postboy;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RtfTemplate {
	List<String> lines;
	List<TemplateTag> tags;
	static  Logger log = LoggerFactory.getLogger(RtfTemplate.class );
	int offset=0, rowNumber=0;
	String template;
	
	public RtfTemplate(File file){
		try {
			Scanner sc = new Scanner(file);
			populateLines(sc);
		} catch (FileNotFoundException e) {
			log.error("FileNotFoundException {}\n{}",UICommonUtil.formatMessage(e));
		}
	}
	public RtfTemplate(String templateText){
		Scanner sc =  new Scanner(templateText);
		populateLines(sc);
	}
	
	private void populateLines(Scanner sc){
		lines = new ArrayList<String>();
		StringBuffer txt = new StringBuffer();
		while (sc.hasNextLine()) {
			String string = sc.nextLine();
			lines.add(string);
			txt.append(string);
		}
		sc.close();
		template = txt.toString();
		populateTagsFromTemplate();		
		if (log.isTraceEnabled()){
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
		offset++;
		tagStr = Sets.BEGIN_TAG_DELIMITER;
		tag.getUnits().add(new RTFTextUnit(rowNumber,offset,Sets.BEGIN_TAG_DELIMITER));
		offset += Sets.BEGIN_TAG_DELIMITER.length();
		currPosition = offset;

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
					tag.getUnits().add(new RTFTextUnit(i,offset,firstToken));
				}
				currPosition += firstToken.length();
				if (firstToken.endsWith(Sets.END_TAG_DELIMITER)){
					break;
				}
				offset += token.length();
			}
			if (firstToken.endsWith(Sets.END_TAG_DELIMITER)){
				break;
			}
			currPosition = 0;
			offset = 0;
		}
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
			tag = getNextTag(offset, rowNumber);
		}
	}
	
	public ArrayList<String> checkTemplateForMapper(){
		ArrayList<String> errorTags = new ArrayList<String>();
		
		for (TemplateTag tag : tags){
			if (!Memory.tagMapper.getTagMapperJson().containsKey(tag.getTag().toString())){
				errorTags.add(tag.toString());
				log.debug(tag.toString());
			}
		}
		return errorTags;
	}
	public ArrayList<String> checkTemplateForData(){
		ArrayList<String> errorTags = new ArrayList<String>();
		
		for (TemplateTag tag : tags){
			if (!Memory.tagMapper.getTagMapperJson().containsKey(tag.getTag().toString())){
				errorTags.add(tag.toString());
				log.debug(tag.toString());
			}
		}
		return errorTags;
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
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
}
