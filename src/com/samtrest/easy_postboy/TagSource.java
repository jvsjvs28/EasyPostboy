package com.samtrest.easy_postboy;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TagSource {
	static  Logger log = LoggerFactory.getLogger(TagSource.class );
	String value,type;
	TagSource[]params = new TagSource[0];

	public TagSource(JSONObject json){
		value = (String)json.get(Sets.JSON_VALUE);
		type  = (String)json.get(Sets.JSON_TYPE);
		if (Sets.TAG_TYPE_FUNCTION.equalsIgnoreCase(type)){
			JSONArray array = (JSONArray) json.get(Sets.JSON_PARAMETERS);
			params = new TagSource[array.size()];
			for (int i = 0; i < array.size(); i++) {
				params[i] = new TagSource((JSONObject)array.get(i));
			}
		}
	}

	public String toJsonString(){
		String jsonStr="",paramStr="";
		if (params.length > 0){		
			for (int i = 0; i < params.length; i++) {
				TagSource param = params[i];
				paramStr +=(i>0?",":"")+"{"+
						TagObject.jsonSyntacsys(param.type,param.value)+"}";
			}
		}

		jsonStr = "{"+ 
				TagObject.jsonSyntacsys(Sets.JSON_TYPE, type)+','+
				TagObject.jsonSyntacsys(Sets.JSON_VALUE, value);
				if (!"".equals(paramStr)){
					jsonStr += ",\""+Sets.JSON_PARAMETERS+"\":["+paramStr+']';
				}
				jsonStr +="}";

		return jsonStr;
	}
}
