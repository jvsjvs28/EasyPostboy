package com.samtrest.easy_postboy;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		if (!"".equals(Memory.mainFrame.fileTemplateNameField.getText())){
			Memory.rtfProcessor = new RtfTemplate(
					new File(Memory.mainFrame.fileTemplateNameField.getText()));
		}
		Memory.tagMapper = new TagMapper();
		try {
			Memory.tagMapper.getJSONFromFile("");
			Memory.tagMapper.parseJSON();

			if (log.isDebugEnabled()){
				Memory.tagMapper.printJSON();
			}
		}catch (Exception e){
			Memory.tagMapper.putDefaultJSON();
		}
	}

	public static void main(String[] args) {
		EasyPostboyApp ewApp = null;
		if (args.length > 0){
			if ("-file".equals(args[0])){
				new EasyPostboyApp(new File(args[1]));
			}
			if ("-frame".equals(args[0])){
				new EasyPostboyApp("true".equalsIgnoreCase(args[1]));
			}
		}else{
			new EasyPostboyApp(false);
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
		} catch (FileNotFoundException e)
		{
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
