package com.samtrest.easy_postboy;

import java.io.File;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataProcessor extends EPJsonUtility{
	static  Logger log = LoggerFactory.getLogger(DataProcessor.class );
	JSONObject dataJson;

	public DataProcessor(File file) {
		super();
		try {
			dataJson = readJsonFromFile(file);
			Memory.dataProcessor = this; 
		} catch (IOException | ParseException e) {
			log.error("Exception {}",UICommonUtil.formatMessage(e));
		}
	}

	public DataProcessor(String str) {
		super();
		try {
			dataJson = readJsonFromStr(str);
		} catch (ParseException e) {
			log.error("ParseException {}",UICommonUtil.formatMessage(e));
		}
	}

	static String toRtfSequence(String dataStr){
		String str = "";
		String add;
		byte [] bytes = dataStr.getBytes();
		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			if (b > 0){
				add = ""+dataStr.charAt(i);
			}else{
				add = "\\'"+ Integer.toHexString(dataStr.charAt(i)+16).substring(1);
			}			
			str += add;
		}
		return str;
	}

	public JSONObject getDataJson() {
		return dataJson;
	}

	public void setDataJson(JSONObject data) {
		this.dataJson = data;
	}
}
