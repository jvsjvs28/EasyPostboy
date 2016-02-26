package com.samtrest.easy_postboy;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EasyPostboyApp {

	static  Logger log = LoggerFactory.getLogger(EasyPostboyApp.class );
	File file;

	public EasyPostboyApp() {
		super();
	}
	public EasyPostboyApp(File file) {
		super();
		this.file = file;
	}

	private void showMainFrame(){
		Memory.mainFrame = new MainFrame("Easy Postboy");
		Memory.mainFrame.fileTemplateNameField.setText("c:\\My_projects\\E_my_projects\\EasyPostboy\\template0.rtf");
		Memory.rtfProcessor = new RtfTemplate(Memory.mainFrame.fileTemplateNameField.getText());
	}

	public static void main(String[] args) {
		EasyPostboyApp ewApp = null;
		Memory.setEpProps( new Properties());
		if (args.length > 0){
			ewApp = new EasyPostboyApp(new File(args[0]));
		}else{
			ewApp = new EasyPostboyApp();
		}
		Memory.tagMapper = new TagMapper();
		try {
			Memory.tagMapper.getJSONFromFile("");
		}catch (Exception e){
			Memory.tagMapper.putDefaultJSON();
		}
		Memory.tagMapper.parseJSON();

		if (log.isDebugEnabled()){
			Memory.tagMapper.printJSON();
		}
		ewApp.showMainFrame();
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

}
