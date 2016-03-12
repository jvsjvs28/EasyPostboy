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

public class TagMapper {

	static  Logger log = LoggerFactory.getLogger(TagMapper.class );
	JSONObject tagMapperJson;
	
	public TagMapper(String tagStr) {
		super();
		tagMapperJson = new JSONObject();
		try {
			readJsonFromStr(tagStr);
		} catch (ParseException e) {
			log.error("ParseException {}\n{}",UICommonUtil.formatMessage(e));
		}
	}

	public TagMapper(File file) {
		super();
		tagMapperJson = new JSONObject();
		readJsonFromFile(file);
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

	public void parseJson(){
		JSONArray array = (JSONArray) tagMapperJson.get(Sets.JSON_MAPPER_MAIN_TAGNAME);
		Memory.setJsonElements(new TagObject[array.size()]);
		for (int i = 0; i < array.size(); i++) {
			JSONObject element = (JSONObject)((JSONObject)array.get(i)).get(Sets.JSON_ELEMENT); 
			Memory.getJsonElements()[i]= new TagObject(element);
		}
		TagObject[] ar= Memory.getJsonElements();
		String str = "[";
		for (int i = 0; i < Memory.getJsonElements().length; i++) {
			if (i > 0){
				str +=",";
			}
			str+=Memory.getJsonElements()[i].toJsonString();
		}
		str += "]";
	}

	public void readJsonFromStr(String str) throws ParseException{
		JSONParser parser = new JSONParser();
		tagMapperJson = (JSONObject) parser.parse(str);		
	}
	
	public void readJsonFromFile(File file){
		File theFile = file;
		if (theFile == null){
			if ("".equals(Memory.mainFrame.tagMapperFilenameField.getText())){
				Memory.mainFrame.tagMapperFilenameField.setText(Sets.JSON_FILENAME);
			}			try {
				theFile = new File(Memory.mainFrame.tagMapperFilenameField.getText());
			}catch (Exception e){
				log.error("Exception {}\n{}",UICommonUtil.formatMessage(e));
				Memory.tagMapper.populateDefaultMapperJson();
				return;
			}
		}
		try {
			StringBuffer sb = new StringBuffer();

			BufferedReader in = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(theFile), "UTF8"));
			String str;
			while ((str = in.readLine()) != null) {
				sb.append(str);
			}
			in.close();
			readJsonFromStr(sb.toString());
		}catch (UnsupportedEncodingException e){
			log.error("UnsupportedEncodingException {}\n{}",UICommonUtil.formatMessage(e));			System.out.println(e.getMessage());
		} catch (IOException e){
			log.error("IOException {}\n{}",UICommonUtil.formatMessage(e));		
		}catch (Exception e){
			log.error("Exception {}\n{}",UICommonUtil.formatMessage(e));
		}
	}

	public void populateDefaultMapperJson(){
		JSONArray array = new JSONArray();
		JSONObject element = null,subElement = null, param = null;
		JSONArray  internalDefArray = null,paramsArray = null;;

		element =  new JSONObject();		
		element.put(Sets.JSON_TAG,"<<מספר לקוח>>");
		element.put(Sets.JSON_INTERNAL,"client_id");
		internalDefArray = new JSONArray();
		subElement = new JSONObject();
		subElement.put(Sets.JSON_TYPE,Sets.TAG_TYPE_FIELD_IN_TABLE);
		subElement.put(Sets.JSON_VALUE,"rm_clients");
		internalDefArray.add(subElement);
		element.put(Sets.JSON_SOURCES, internalDefArray);			
		array.add(TagMapper.createJSONObject(Sets.JSON_ELEMENT, element));

		element =  new JSONObject();
		element.put(Sets.JSON_TAG,"<<שם פרטי לקוח>>");
		element.put(Sets.JSON_INTERNAL,"CLIENT_FIRST_NAME");		
		internalDefArray = new JSONArray();		
		subElement = new JSONObject();
		subElement.put(Sets.JSON_TYPE,Sets.TAG_TYPE_FIELD_IN_TABLE);
		subElement.put(Sets.JSON_VALUE,"rm_clients");
		internalDefArray.add(subElement);
		element.put(Sets.JSON_SOURCES, internalDefArray);	
		array.add(TagMapper.createJSONObject(Sets.JSON_ELEMENT, element));

		element =  new JSONObject();
		element.put(Sets.JSON_TAG,"<<שם משפחה לקוח>>");
		element.put(Sets.JSON_INTERNAL,"CLIENT_LAST_NAME");		
		internalDefArray = new JSONArray();		
		subElement = new JSONObject();
		subElement.put(Sets.JSON_TYPE,Sets.TAG_TYPE_FIELD_IN_TABLE);
		subElement.put(Sets.JSON_VALUE,"rm_clients");
		internalDefArray.add(subElement);
		element.put(Sets.JSON_SOURCES, internalDefArray);	
		array.add(TagMapper.createJSONObject(Sets.JSON_ELEMENT, element));

		element =  new JSONObject();
		element.put(Sets.JSON_TAG,"<<שם לקוח>>");
		element.put(Sets.JSON_INTERNAL,"CLIENT_LAST_NAME");		
		internalDefArray = new JSONArray();		
		subElement = new JSONObject();
		subElement.put(Sets.JSON_TYPE,Sets.TAG_TYPE_FUNCTION);
		subElement.put(Sets.JSON_VALUE,"easy_writer_support_pkg.get_client_name");
		paramsArray  = new JSONArray();
		param = new JSONObject();
		param.put(Sets.JSON_TYPE, Sets.TAG_TYPE_JSON);
		param.put(Sets.JSON_VALUE, "client_id");
		paramsArray.add(param);		
		subElement.put(Sets.JSON_PARAMETERS,paramsArray);
		internalDefArray.add(subElement);
		subElement = new JSONObject();
		subElement.put(Sets.JSON_TYPE,Sets.TAG_TYPE_FIELD_IN_TABLE);
		subElement.put(Sets.JSON_VALUE,"rm_clients");
		internalDefArray.add(subElement);
		subElement = new JSONObject();
		subElement.put(Sets.JSON_TYPE,Sets.TAG_TYPE_JSON_FUNCTION);
		subElement.put(Sets.JSON_VALUE,"CLIENT_FIRST_NAME + \" \"+ CLIENT_LAST_NAME");
		internalDefArray.add(subElement);
		element.put(Sets.JSON_SOURCES, internalDefArray);	
		array.add(TagMapper.createJSONObject(Sets.JSON_ELEMENT, element));

		element =  new JSONObject();
		element.put(Sets.JSON_TAG,"<<תאריך נוכחי>>");
		element.put(Sets.JSON_INTERNAL,"sysdate");		
		internalDefArray = new JSONArray();		
		subElement = new JSONObject();
		subElement.put(Sets.JSON_TYPE,Sets.TAG_TYPE_SYSDATE);
		internalDefArray.add(subElement);
		element.put(Sets.JSON_SOURCES, internalDefArray);	
		array.add(TagMapper.createJSONObject(Sets.JSON_ELEMENT, element));

		element =  new JSONObject();
		element.put(Sets.JSON_TAG,"<<תאריך פניה>>");
		element.put(Sets.JSON_INTERNAL,"sysdate");		
		internalDefArray = new JSONArray();		
		subElement = new JSONObject();
		subElement.put(Sets.JSON_TYPE,Sets.TAG_TYPE_SYSDATE);
		internalDefArray.add(subElement);
		element.put(Sets.JSON_SOURCES, internalDefArray);	
		array.add(TagMapper.createJSONObject(Sets.JSON_ELEMENT, element));

		element =  new JSONObject();
		element.put(Sets.JSON_TAG,"<<חוזה>>");
		element.put(Sets.JSON_INTERNAL,"contract_acc_num");		
		internalDefArray = new JSONArray();		
		subElement = new JSONObject();
		subElement.put(Sets.JSON_TYPE,Sets.TAG_TYPE_FIELD_IN_TABLE);
		subElement.put(Sets.JSON_VALUE,"rm_contract_acc");
		internalDefArray.add(subElement);
		element.put(Sets.JSON_SOURCES, internalDefArray);	
		array.add(TagMapper.createJSONObject(Sets.JSON_ELEMENT, element));

		element =  new JSONObject();
		element.put(Sets.JSON_TAG,"<<כתובת שורה1>>");
		element.put(Sets.JSON_INTERNAL,"address_line1");		
		internalDefArray = new JSONArray();		
		subElement = new JSONObject();
		subElement.put(Sets.JSON_TYPE,Sets.TAG_TYPE_FUNCTION);
		subElement.put(Sets.JSON_VALUE,"easy_writer_support_pkg.get_client_addr_line1");
		paramsArray  = new JSONArray();
		param = new JSONObject();
		param.put(Sets.JSON_TYPE, Sets.TAG_TYPE_JSON);
		param.put(Sets.JSON_VALUE, "client_id");
		paramsArray.add(param);		
		subElement.put(Sets.JSON_PARAMETERS,paramsArray);
		internalDefArray.add(subElement);
		element.put(Sets.JSON_SOURCES, internalDefArray);	
		array.add(TagMapper.createJSONObject(Sets.JSON_ELEMENT, element));

		element =  new JSONObject();
		element.put(Sets.JSON_TAG,"<<כתובת שורה2>>");
		element.put(Sets.JSON_INTERNAL,"address_line2");		
		internalDefArray = new JSONArray();		
		subElement = new JSONObject();
		subElement.put(Sets.JSON_TYPE,Sets.TAG_TYPE_FUNCTION);
		subElement.put(Sets.JSON_VALUE,"easy_writer_support_pkg.get_client_addr_line2");
		paramsArray  = new JSONArray();
		param = new JSONObject();
		param.put(Sets.JSON_TYPE, Sets.TAG_TYPE_JSON);
		param.put(Sets.JSON_VALUE, "client_id");
		paramsArray.add(param);		
		subElement.put(Sets.JSON_PARAMETERS,paramsArray);
		internalDefArray.add(subElement);
		element.put(Sets.JSON_SOURCES, internalDefArray);	
		array.add(TagMapper.createJSONObject(Sets.JSON_ELEMENT, element));

		tagMapperJson.put(Sets.JSON_MAPPER_MAIN_TAGNAME,array);

		//		System.out.println(json.toJSONString());

		File file;
		file = new File(Sets.JSON_FILENAME);
		try {
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			Writer out;
			out = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(file), "UTF8"));
			out.write(tagMapperJson.toJSONString());

			out.flush();
			out.close();
		}catch (IOException e) {
			log.error("IOException {}\n{}",UICommonUtil.formatMessage(e));
		}
	}

	public JSONObject getTagMapperJson() {
		return tagMapperJson;
	}

	public void setTagMapperJson(JSONObject tagMapperJson) {
		this.tagMapperJson = tagMapperJson;
	}
}
