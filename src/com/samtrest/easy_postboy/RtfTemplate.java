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

	public RtfTemplate(String pathTemplate){
		Scanner sc;
		try {
			sc = new Scanner(new File(pathTemplate));
			lines = new ArrayList<String>();
			txt = new StringBuffer();
			while (sc.hasNextLine()) {
				String string = sc.nextLine();
				lines.add(string);
				txt.append(string);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		populateTags();
	}

	private TemplateTag getNextTag(int offset,int rowNumber){
		TemplateTag tag = new TemplateTag();
		String tagStr = "",line="",token;
		int currPosition=0;
		StringBuffer buffer = new StringBuffer();

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
		tag.getUnits().add(new RTFTextUnit(rowNumber,offset,Sets.BEGIN_TAG_DELIMITER));
		currPosition = offset + Sets.BEGIN_TAG_DELIMITER.length()+1;

		for (int i = rowNumber; i < lines.size(); i++) {
			line = lines.get(i).substring(currPosition);
			log.debug("Line"+i+": "+line);
			StringTokenizer tokenizer = new StringTokenizer(line,"\\ {}",true);
			while(tokenizer.hasMoreTokens()){
				token = tokenizer.nextToken();
				if (token.equals("\\")){
					token += tokenizer.nextToken();
				}
				if (token.equals("\tab") || token.startsWith("\\'") || 
						!token.startsWith("\\") && !token.startsWith("{")&&
						!token.startsWith("}")){
					buffer.append(token);
					log.debug(token);
					if (token.equals(Sets.END_TAG_DELIMITER)){
						break;
					}
				}
			}
			if (buffer.length() != 0){
				log.debug(buffer.toString());
				if (buffer.toString().equals(Sets.END_TAG_DELIMITER)){
					break;
				}
			}
			//			String fragment = buffer.toString();
			//			tag.getUnits().add(new RTFTextUnit(i,offset,fragment));
			//			tagStr += fragment;
			//			if (tagStr.endsWith(Sets.END_TAG_DELIMITER)){
			//				break;
			//			}
			//			if (tagStr.endsWith(Sets.END_TAG_DELIMITER)){
			//				break;
			//			}
			currPosition = 0;
			buffer = new StringBuffer();
		}
		tag.setTag(tagStr);
		return tag;
	}

	private void populateTags(){
		int offset=0, rowNumber=0;
		tags =  new ArrayList<TemplateTag>();
		TemplateTag tag = getNextTag(offset, rowNumber);
		while (tag != null){
			tags.add(tag);
			rowNumber = tag.getUnits().get(tag.getUnits().size()-1).getRowNum();
			offset = tag.getUnits().get(tag.getUnits().size()-1).getOffset()+1;
			System.out.println(tag.getTag());
			tag = getNextTag(offset, rowNumber);
		}

	}
}
