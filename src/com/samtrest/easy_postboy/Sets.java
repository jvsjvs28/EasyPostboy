package com.samtrest.easy_postboy;

import java.awt.Font;

public class Sets
{
	public static final String POSTBOY_VERSION = "0.01.01";
	public static final String WHAT_NEW = "";
	public static final String JSON_MAPPER_MAIN_TAGNAME = "POSTBOY_JSON";
	public static final String JSON_ELEMENT = "ELEMENT";
	public static final String JSON_TAG = "TAG";
	public static final String JSON_TYPE = "TYPE";
	public static final String JSON_INTERNAL = "INTERNAL";
	public static final String JSON_SOURCES = "SOURCES";
	public static final String JSON_VALUE = "VALUE";
	public static final String JSON_PARAMETERS = "PARAMS";

	public static final String JSON_DATA_MAIN_TAGNAME = "EP_DATA";
	public static final String JSON_DATA_FILE_ID = "FILE_ID";

	public static final String TAG_TYPE_FIELD = "FIELD";
	public static final String TAG_TYPE_FIELD_IN_TABLE = "TABLE";
	public static final String TAG_TYPE_FUNCTION = "FUNCTION";
	public static final String TAG_TYPE_SYSDATE = "SYSDATE";
	public static final String TAG_TYPE_JSON_FUNCTION = "JSON_FUNCTION";
	public static final String TAG_TYPE_SELECT = "SELECT";
	public static final String TAG_TYPE_JSON = "JSON";

	public static final int SNTX350 = 350;
	public static final int SNTX351 = 351;
	public static final int SNTX352 = 352;
	public static final int CNTR1 = (int)Math.pow(9938375, (double) 1/3);
	public static final int CNTR2 = (int)Math.pow(9237000, (double) 1/3);
	public static final int CNTR3 = (int)Math.pow(8237000, (double) 1/3);
	public static final int CNTR4 = (int)Math.pow(6237956, (double) 1/3);
	public static final int CNTR5 = (int)Math.pow(1677721, (double) 1/3);
	public static  String ODA_URL = "http://www.samtrest.com";

	private static String FONT_FAMILY = "Arial";    //"Time New Roman"; //"Courier New";
	public static Font   BOLD_FONT = new Font(FONT_FAMILY, Font.BOLD, 12);
	public static Font   PLAIN_FONT = new Font(FONT_FAMILY, Font.PLAIN, 12);
	
	public static String JSON_FILENAME = "ep.json"; 
	//   HSQLDB
	public static String HSQLDBDir ="db";
	public static String HSQLDBPort ="9005";
	public static String HSQLDBName ="ODA";
	public static String HSQLDBUser ="sa";
	public static String HSQLDBPwd ="";
	public static String HSQLDBHost ="localhost";
	
	public static final String BEGIN_TAG_DELIMITER = "<<";
	public static final String END_TAG_DELIMITER = ">>";
	public static final String [] IGNORE_KEYS ={
			"*","fonttbl","colortbl","datastore","themedata"
	};
    public static final String TEMPLATE_PROPERTY_NAME = "Template file";
    public static final String JSON_PROPERTY_NAME = "JSON file";
	
}
