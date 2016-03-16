package com.samtrest.easy_postboy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TagMapper extends EPJsonUtility{

	static  Logger log = LoggerFactory.getLogger(TagMapper.class );
	JSONObject tagMapperJson;
	
	public TagMapper(String tagStr) {
		super();
		try {
			tagMapperJson = readJsonFromStr(tagStr);
			Memory.tagMapper = this;
		} catch (ParseException e) {
			log.error("ParseException {}\n{}",UICommonUtil.formatMessage(e));
		}
	}

	public TagMapper(File file) {
		super();
		try {
			tagMapperJson = readJsonFromFile(file);
			Memory.tagMapper = this;
			} catch (IOException | ParseException e) {
			log.error("Exception {}",UICommonUtil.formatMessage(e));
		}
	}

	public static JSONObject createJSONObject(Object key,Object value){
		JSONObject jSON = new JSONObject();
		jSON.put(key, value);
		return jSON;
	}

	public void printJson(){
		JSONArray array = (JSONArray) tagMapperJson.get(Sets.JSON_MAPPER_MAIN_TAGNAME);
		for (int i = 0; i < array.size(); i++) {
			JSONObject element = (JSONObject)((JSONObject)array.get(i)).get(Sets.JSON_ELEMENT);
			log.trace(element.toJSONString());
		}
	}

	public JSONObject getTagMapperJson() {
		return tagMapperJson;
	}

	public void setTagMapperJson(JSONObject tagMapperJson) {
		this.tagMapperJson = tagMapperJson;
	}
}
