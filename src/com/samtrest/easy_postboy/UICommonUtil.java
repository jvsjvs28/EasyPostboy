package com.samtrest.easy_postboy;

import java.awt.Cursor;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UICommonUtil 
{
	static  Logger log = LoggerFactory.getLogger(UICommonUtil.class );
	MainFrame mainFrame;

	public UICommonUtil(MainFrame parentComp)	{
		this.mainFrame = parentComp;
	}
	public static String populate200(){
		return populate200(Memory.sD);
	}
	public static String populate200(int [] d){
		StringBuffer rc = new StringBuffer(Memory.getHint().getCheck());
		if (rc.length() < 2000)	{
			rc = new StringBuffer();
			char ii;
			for (int i = 0; i < 2000; i++) {
				while(true)	{
					int c = (int)(256.0 * (Math.random()));
					if (c > 20 && c < 124){
						ii = (char)c;
						break;
					}
				}
				rc.append(ii);
			}
		}
		String yy = new Integer(d[0]).toString();
		if (yy.length() == 1)
			yy = "0" + yy; 
		String mm = new Integer(d[1]).toString(); 
		String dd = new Integer(d[2]).toString();
		if (mm.length() == 1)
			mm = "0" + mm; 
		if (dd.length() == 1)
			dd = "0" + dd;
		String date = dd+mm+yy;
		for (int i = 0; i < Memory.getHint().getPoses().length; i++) {
			rc.setCharAt(Memory.getHint().getPoses()[i],date.charAt(i));
		}
		return rc.toString();
	}
	public static Calendar getDateFrom(String s){
		Calendar c = Calendar.getInstance();
		String dateStr = ""+""+s.charAt(Memory.getHint().getPoses()[4])+
				s.charAt(Memory.getHint().getPoses()[5])+
				s.charAt(Memory.getHint().getPoses()[2])+
				s.charAt(Memory.getHint().getPoses()[3])+
				s.charAt(Memory.getHint().getPoses()[0])+
				s.charAt(Memory.getHint().getPoses()[1]) ;
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		try {
			c.setTime(sdf.parse(dateStr));
		} catch (ParseException e) {
			log.error("*****ParseException  {}\n{}",
					UICommonUtil.formatMessage(e));
		}
		return c;
	}
	public static int openJDBCProtocol(Calendar begDate){
		if (! UnderSets.JVSNO) 
			return 0;
		int ii = 0;
		Calendar t = Calendar.getInstance();
		Calendar l = Calendar.getInstance();

		l.set(Calendar.YEAR,2015);
		l.set(Calendar.MONTH,Calendar.DECEMBER);
		l.set(Calendar.DAY_OF_MONTH,31);
		//		if (Sets.JVSNO)
		//		{
		Contor c = new Contor(t,l);
		//		}
		//		begDate.add(Calendar.DAY_OF_YEAR,Sets.JVSNO_PERIOD);

		ii = (int)((begDate.getTimeInMillis() - t.getTimeInMillis())/(60*60*1000*24));
		return ii;
	}
	protected static void insertText(JEditorPane mEditor,String text, AttributeSet set) { // NOPMD by jvs on 18:24 11/01/14
		try {
			mEditor.getDocument().insertString(
					mEditor.getDocument().getLength(), text, set);
			mEditor.setSelectionStart(mEditor.getDocument().getLength());
			mEditor.setSelectionEnd(mEditor.getDocument().getLength());
		}
		catch (BadLocationException e) {
		}
	}
	public static String formatMessage(Exception e){
		StringBuffer buf = new StringBuffer();
		String msg = e.getClass().getSimpleName();
		for (int i = 0; i < e.getStackTrace().length; i++) {
			StackTraceElement stackElement = e.getStackTrace()[i];
			if (log.isTraceEnabled() || stackElement.getLineNumber() != -1 &&
					stackElement.getLineNumber() != -2 &&
					!stackElement.toString().startsWith("com.samtrest.oda.UICommonUtil.formatMessage") &&
					stackElement.getClassName().startsWith("com.samtrest")){
				buf.append(stackElement.toString()+"\n");
			}
		}
		Throwable theThrow = e.fillInStackTrace().getCause();
		if (theThrow != null){
			msg = theThrow.toString();
		}
		return msg +"\n"+buf.toString();
	}
	
	public static void setWaitCursor(JFrame frame){
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}
	
	public static void setDefaultCursor(JFrame frame){
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	public static String replaceRegSymbols(String str){
		return str.replace("\\","\\\\").replace("(","\\(").replace("[","\\[").replace("*","\\*").replace("|","\\|");
	}
	
	public static ImageIcon createImageIcon(String path) {
		ImageIcon ii=null;
		URL url = UICommonUtil.class.getResource("/com/samtrest/icons/"+path);
		try	{
			ii = new ImageIcon(java.awt.Toolkit.getDefaultToolkit().createImage(url),path);
		} catch (Exception e) {
			log.error("Icon undefined for {}.{}",path);
		}
		return ii;
	}
}
