package com.samtrest.easy_postboy;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnderSets {
	static  Logger log = LoggerFactory.getLogger(UnderSets.class );
	//	ODA variables
	public static boolean JVSNO = false;
	public static int JVSNO_DAYS;
	public static int JVSNO_PERIOD = 30;
	public static String FIRST_PARAM = "@@@";
	public static String NOT = "noisrev lairt ni elbaliava ton s'tI";
	public static String LIMIT = "noitatimil noisrev lairT";
	public static void set(){
		try{
			ByteArrayInputStream bios = new ByteArrayInputStream(Memory.bytes);
			ObjectInputStream  ois = new ObjectInputStream(bios);
			Memory.setHint((Hint)ois.readObject());
			ois.close();
			StringBuffer b = new StringBuffer();
			b.append(Memory.getHint().getCheck().charAt(201));
			b.append(Memory.getHint().getCheck().charAt(1512));
			b.append(Memory.getHint().getCheck().charAt(1512));
			b.append(Memory.getHint().getCheck().charAt(1243));
			b.append(Memory.getHint().getCheck().charAt(1512));
			Memory.setReo(b.toString()+" ");
			UnderSets.JVSNO = !UnderSets.FIRST_PARAM.equals("jvs") && Memory.getHint().getTy().equals("0");
		} catch (Exception e) {
			log.error("Exception {}",
					UICommonUtil.formatMessage(e));
			System.exit(666);
		}
	}
}
