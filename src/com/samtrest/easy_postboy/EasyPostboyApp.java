package com.samtrest.easy_postboy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EasyPostboyApp {

	static  Logger log = LoggerFactory.getLogger(EasyPostboyApp.class );
	File file;

	public EasyPostboyApp(boolean isVisible) {
		super();
		initiateApp(isVisible);
	}

	private void initiateApp(boolean isVisible){
		initProps();		
		Memory.mainFrame = new MainFrame("Easy Postboy",isVisible);
	}

	public FileContent[] runEasyPostboyStr(String template, String mapperJson,
			String dataJson){
		ArrayList<String> errors = null;
		JSONParser parser = new JSONParser();

		Memory.rtfProcessor = new RtfTemplateProcessor(template);
		Memory.tagMapper = new TagMapper(mapperJson);
		Memory.dataProcessor = new DataProcessor(dataJson);

		errors = Memory.rtfProcessor.checkTemplateForMapper();
		if (errors.size() != 0){
			log.error(" {} Mapping errors found",errors.size());
			for (String error : errors) {
				log.error("Tag {} not found",error);
			}
			return null;
		}else{
			errors = Memory.rtfProcessor.checkTemplateForData();
			if (errors.size() != 0){
				log.error(" {} Data errors found",errors.size());
				for (String error : errors) {
					log.error("Value {} not found",error);
				}
				return null;
			}
		}
		return createDocs();
	}

	public FileContent[] createDocs(){
		int offset,rownum ;
		String tagText;
		FileContent[] resultRtfs = null;
		ArrayList<FileContent> docs = new ArrayList<FileContent>();		
		JSONArray dataRows = (JSONArray) Memory.dataProcessor.dataJson.get(Sets.JSON_MAPPER_MAIN_TAGNAME);

		for (int k = 0; k < dataRows.size(); k++) {
			ArrayList<String> content = new ArrayList<String>();
			for (String string : Memory.rtfProcessor.getLines()) {
				content.add(string);
			}
			JSONObject row = (JSONObject)((JSONObject)dataRows.get(k)).get(Sets.JSON_ROW);
			String fileName = (String)row.get(Sets.JSON_DATA_FILE_ID);
			for (TemplateTag tag : Memory.rtfProcessor.tags){
				if (!row.containsKey(tag.getDataTag())){
					log.error("\"{}\" field not found in data row {} \n    {}",tag.getDataTag(),k+1,row.toString());
				}else{
					if (!tag.getTag().equals(Sets.JSON_DATA_FILE_ID)){
						int currRowNum = tag.getUnits().get(0).getRowNum(), 
								offsetCorrection = 0;
						for (int i = 0; i < tag.getUnits().size(); i++) {
							rownum = tag.getUnits().get(i).getRowNum();
							offset = tag.getUnits().get(i).getOffset();
							if (currRowNum == rownum){
								offset += offsetCorrection;
							}else{
								offsetCorrection = 0;
								currRowNum = rownum;
							}
							tagText = tag.getUnits().get(i).getText();
							String theLine = content.get(rownum);

							String replaceStr = "";
							if( i==0){
								replaceStr = (String)row.get(tag.getDataTag());
								replaceStr = DataProcessor.toRtfSequence(replaceStr);
							}
							try{
								String newLine = theLine.substring(0,offset)+
										replaceStr+
										theLine.substring(offset+tagText.length());
								content.set(rownum, newLine);
							} catch(Exception e){
								log.error("Replace exception in {} row, offset: {}, tag: {} \n len:{}\n line: {} \n{}",
										rownum+1,offset+1,tagText,theLine.length(),theLine,
										UICommonUtil.formatMessage(e));
							}
							offsetCorrection += replaceStr.length() - tagText.length();
						}
					}
				}
			}
			StringBuffer lines = new StringBuffer();
			for (String line : content) {
				lines.append(line+'\n');
			}
			docs.add(new FileContent(fileName, lines.toString()));
		}
		resultRtfs = new FileContent[docs.size()];
		for (int i = 0; i < docs.size(); i++) {
			resultRtfs[i] = docs.get(i);
		}
		return resultRtfs;
	}

	public static void main(String[] args) {
		EasyPostboyApp ewApp = null;
		boolean isVisual = true;
		boolean isAllParamsAvailable = false;
		if (args.length > 0){
			for (int i = 0; i < args.length; i++) {
				if ("-tag-file".equals(args[i])){
					Memory.tagMapperJsonFile = new File(args[i+1]);
				}else if ("-data-file".equals(args[i])){
					Memory.dataJsonFile = new File(args[i+1]);
				}else if ("-template-file".equals(args[i])){
					Memory.templateFile = new File(args[i+1]);
				}else if ("-frame".equals(args[i])){
					isVisual = "true".equalsIgnoreCase(args[i+1]);
				}else if("-out-dir".equals(args[i])){
					Memory.outDir = args[i+1];
				}				
				i++;
			}
			if (Memory.tagMapperJsonFile != null){
				Memory.mainFrame.tagMapperFilenameField.setText(Memory.tagMapperJsonFile.getAbsolutePath());
			}
			if (Memory.templateFile != null){
				Memory.mainFrame.fileTemplateNameField.setText(Memory.templateFile.getAbsolutePath());
			}
			if (Memory.dataJsonFile != null){
				Memory.mainFrame.dataFilenameField.setText(Memory.dataJsonFile.getAbsolutePath());
			}
		}

		if (Memory.templateFile != null){
			Memory.rtfProcessor = new RtfTemplateProcessor(Memory.templateFile);
		}

		if (Memory.tagMapperJsonFile != null){
			Memory.tagMapper = new TagMapper(Memory.tagMapperJsonFile);
		}

		if (Memory.dataJsonFile != null){
			Memory.dataProcessor = new DataProcessor(Memory.dataJsonFile);
		}

		isAllParamsAvailable =  Memory.rtfProcessor != null &&
				Memory.tagMapper != null &&
				Memory.dataProcessor != null;

		Memory.easyPostboyApp = new EasyPostboyApp(!isAllParamsAvailable || isVisual);

		if (isAllParamsAvailable){
			FileContent[] docs = Memory.easyPostboyApp.runEasyPostboyStr(
					Memory.rtfProcessor.getTemplate(), 
					Memory.tagMapper.getTagMapperJson().toJSONString(),
					Memory.dataProcessor.getDataJson().toJSONString());
		}
	}

	public static void createFiles(FileContent[] docs){		
		for (int i = 0; i < docs.length; i++) {
			String fileName = Memory.outDir+docs[i].getFileName()+".doc";
			try ( PrintWriter out = new PrintWriter( fileName ) ){
				out.println( docs[i].getContent() );
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
			log.error("File \"epProperties\" FileNotFoundException {}\n{}",UICommonUtil.formatMessage(e));
		} catch (IOException e) {
			log.error("IOException {}",UICommonUtil.formatMessage(e));
		}
		Memory.getEpProps().setProperty("VER",Sets.POSTBOY_VERSION);

		Memory.verticalToolBar = new ToolbarActions();

		if (Memory.getEpProps().getProperty(Sets.TEMPLATE_FILE_PROPERTY_NAME) == null){
			Memory.getEpProps().setProperty(Sets.TEMPLATE_FILE_PROPERTY_NAME,"");
		}else{
			Memory.rtfProcessor = new RtfTemplateProcessor(new File((String)Memory.getEpProps().get(Sets.TEMPLATE_FILE_PROPERTY_NAME)));
			if (Memory.rtfProcessor.template == null){
				Memory.rtfProcessor = null;
			}
		}
		if (Memory.getEpProps().getProperty(Sets.MAP_FILE_PROPERTY_NAME) == null){
			Memory.getEpProps().setProperty(Sets.MAP_FILE_PROPERTY_NAME,"");
		}else{
			Memory.tagMapper = new TagMapper(new File((String)Memory.getEpProps().get(Sets.MAP_FILE_PROPERTY_NAME)));
			if (Memory.tagMapper.tagMapperJson == null){
				Memory.tagMapper = null;
			}
		}
		if (Memory.getEpProps().getProperty(Sets.DATA_FILE_PROPERTY_NAME) == null){
			Memory.getEpProps().setProperty(Sets.DATA_FILE_PROPERTY_NAME,"");
		}else{
			Memory.dataProcessor = new DataProcessor(new File((String)Memory.getEpProps().get(Sets.DATA_FILE_PROPERTY_NAME)));
			if (Memory.dataProcessor.dataJson == null){
				Memory.dataProcessor = null;
			}
		}
	}
}
