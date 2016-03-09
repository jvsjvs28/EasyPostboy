package com.samtrest.easy_postboy;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.read.ListAppender;

public class EasyPostboyApp {

	static  Logger log = LoggerFactory.getLogger(EasyPostboyApp.class );
	File file;

	public EasyPostboyApp(boolean isVisible) {
		super();
		initiateApp(isVisible);
	}
	public EasyPostboyApp(File file) {
		super();
		this.file = file;
		initiateApp(true);
	}

	private void initiateApp(boolean isVisible){
		Memory.mainFrame = new MainFrame("Easy Postboy",isVisible);
		initProps();
	}

	public String[] runEasyPostboy(String template, String mapperJson,
			                   String dataJson){
		ArrayList<String> errors = null;
		JSONParser parser = new JSONParser();
		
		Memory.rtfProcessor = new RtfTemplate(template);
		Memory.tagMapper = new TagMapper();
		try {
			Memory.tagMapper.setJson((JSONObject)parser.parse(mapperJson));
		} catch (ParseException e) {
			log.error("Map json ParseException {}\n{}",UICommonUtil.formatMessage(e));
			return null;
		}
		errors = Memory.rtfProcessor.checkTemplateForMapper();
		
		for (String error : errors) {
			log.error("Tag {} not found",error);
		}
		if (errors.size() != 0){
			System.out.println("Errors found");
			return null;
		}
		try {
			Memory.data = (JSONObject)parser.parse(dataJson);
		} catch (ParseException e) {
			log.error("Data json ParseException {}\n{}",UICommonUtil.formatMessage(e));
			return null;
		}
		errors = Memory.rtfProcessor.checkTemplateForData();
		
		for (String error : errors) {
			log.error("Tag {} not found",error);
		}
		if (errors.size() != 0){
			System.out.println("Errors found");
			return null;
		}
			
		return createDocs();
	}
	
	private String[] createDocs(){
		String [] resultRtfs = null;
		ArrayList<String> docs = new ArrayList<String>();		
		
		return resultRtfs;
	}
	
	public static void main(String[] args) {
		EasyPostboyApp ewApp = null;
		if (args.length > 0){
			for (int i = 0; i < args.length; i++) {
				if ("-file".equals(args[i])){
					ewApp = new EasyPostboyApp(new File(args[i+1]));
				}else if ("-frame".equals(args[i])){
					ewApp = new EasyPostboyApp("true".equalsIgnoreCase(args[i+1]));
				}else if("-dir".equals(args[i])){
					Memory.setOutDir(args[i+1]);
				}				
				i++;
			}
		}else{
			ewApp = new EasyPostboyApp(true);
		}
		if (!"".equals(Memory.getOutDir())){
			if (!"".equals(Memory.mainFrame.fileTemplateNameField.getText())){
				Memory.rtfProcessor = new RtfTemplate(
						new File(Memory.mainFrame.fileTemplateNameField.getText()));
				Memory.tagMapper = new TagMapper();
				Memory.tagMapper.readJsonFromFile(null);

				String [] docs = ewApp.runEasyPostboy(Memory.rtfProcessor.getTemplate(), 
							             Memory.tagMapper.getJson().toJSONString(),
							             "");
			    createFiles(docs);
			}
		}
	}

	public static void createFiles(String [] docs){
		for (int i = 0; i < docs.length; i++) {
			String fileName = "ep_"+(i+1)+".doc";
			try ( PrintWriter out = new PrintWriter( fileName ) ){
			    out.println( docs[i] );
			} catch (Exception e) {
				log.error("File {} Exception {}\n{}",fileName,UICommonUtil.formatMessage(e));
			}
		}
		
	}
	
	public static void saveProps(){
		FileOutputStream out;
		try {
			out = new FileOutputStream("epProperties");
			Memory.getEpProps().store(out, "---- Postboy Properties ----");

			out.close();
		} catch (FileNotFoundException e) {
			log.error("FileNotFoundException {}",UICommonUtil.formatMessage(e));
		} catch (IOException e) {
			log.error("IOException {}",UICommonUtil.formatMessage(e));
		}
	}
	public static void initProps() 
	{
		FileInputStream inp;
		Memory.setEpProps( new Properties());
		try {
			inp = new FileInputStream("epProperties");
			Memory.getEpProps().load(inp);
			inp.close();
		} catch (FileNotFoundException e){
			log.error("File \"epProperties\" Not Found Exception {}\n{}",UICommonUtil.formatMessage(e));
		} catch (IOException e) {
			log.error("IOException {}",UICommonUtil.formatMessage(e));
		}
		Memory.getEpProps().setProperty("VER",Sets.POSTBOY_VERSION);

		if (Memory.getEpProps().getProperty(Sets.TEMPLATE_PROPERTY_NAME) == null){
			Memory.getEpProps().setProperty(Sets.TEMPLATE_PROPERTY_NAME,"");
		}
		Memory.mainFrame.fileTemplateNameField.setText((String)Memory.getEpProps().get(Sets.TEMPLATE_PROPERTY_NAME));

		if (Memory.getEpProps().getProperty(Sets.JSON_PROPERTY_NAME) == null){
			Memory.getEpProps().setProperty(Sets.JSON_PROPERTY_NAME,"");
		}
		Memory.mainFrame.fileJSONNameField.setText((String)Memory.getEpProps().get(Sets.JSON_PROPERTY_NAME));
	}

}
