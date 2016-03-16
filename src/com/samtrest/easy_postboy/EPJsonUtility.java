package com.samtrest.easy_postboy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public abstract class EPJsonUtility {


	public JSONObject readJsonFromStr(String str) throws ParseException{
		JSONObject json = new JSONObject();
		JSONParser parser = new JSONParser();
		json = (JSONObject) parser.parse(str);
		return json;
	}

	public JSONObject readJsonFromFile(File theFile) throws IOException, ParseException{
		if (theFile == null){
			return null;
		}
		StringBuffer sb = new StringBuffer();

		BufferedReader in = new BufferedReader(
				new InputStreamReader(
//						new FileInputStream(theFile), "UTF8"));
		                new FileInputStream(theFile)));
		String str;
		while ((str = in.readLine()) != null) {
			sb.append(str);
		}
		in.close();
		return readJsonFromStr(sb.toString());
	}

}
