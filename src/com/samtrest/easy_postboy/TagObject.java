package com.samtrest.easy_postboy;

import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TagObject {

	static  Logger log = LoggerFactory.getLogger(TagObject.class );
	private String tagName,internalName;
	private TagSource[] sources;
	private String value;

	public TagObject(JSONObject json) {
		tagName = (String) json.get(Sets.JSON_TAG);
		internalName = (String) json.get(Sets.JSON_INTERNAL);
		JSONArray array = (JSONArray)json.get(Sets.JSON_SOURCES);
		sources = new TagSource[array.size()];
		for (int i = 0; i < array.size(); i++) {
			sources[i] = new TagSource((JSONObject)array.get(i));
		}
	}
	
	public static String jsonSyntacsys(String t,String v){
		return "\""+t+"\":\""+(v == null?"":v.replaceAll("\"", "\\\""))+"\"";
	}
	public String toJsonString(){
		String json="";
		
		json = "";
		for (int i = 0; i < sources.length; i++) {
			TagSource source = sources[i];
			json +=(i>0?",":"")+ source.toJsonString();
		}
		
		json = "{\""+Sets.JSON_ELEMENT+ 
				"\":{"+jsonSyntacsys(Sets.JSON_TAG, tagName)+','+
				jsonSyntacsys(Sets.JSON_INTERNAL, internalName)+
				",\""+Sets.JSON_SOURCES + "\":["+json+"]}}";
		
		return json;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getInternalName() {
		return internalName;
	}

	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}

	public TagSource[] getSources() {
		return sources;
	}

	public void setSources(TagSource[] sources) {
		this.sources = sources;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
