package com.samtrest.easy_postboy;

import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Contor {
	static  Logger log = LoggerFactory.getLogger(Contor.class );
	int ii;
	public Contor(int k){
		Calendar l = UICommonUtil.getDateFrom(Memory.getHint().getCheck()),
		         d = Calendar.getInstance();
		d.setTime(new Date());
		if (d.after(l))	{
			new CntrException(Sets.CNTR1); //214
		}
		if (k == 2 ||k == 3){
			if (!Memory.getIp().equals(XMLException.getK())){
				new CntrException(Sets.CNTR2); //209
			}
		}else if(k == 4||k == 5){
			if (!Memory.getHst().equalsIgnoreCase(XMLException.getK())){
				new CntrException(Sets.CNTR3); //201
			}
		} else if(k == 6||k == 7){
			if (!Memory.getDmn().equalsIgnoreCase(XMLException.getK()))	{
				new CntrException(Sets.CNTR4); //184
			}
//		} else if(k == 8||k == 9){
//			if (!Memory.getReposDbConnection().getDBHost().equalsIgnoreCase(XMLException.getK())){
//				new CntrException(Sets.CNTR5); //118
//			}
		} 
	}
	
	public Contor(Calendar t,Calendar l){
		if (t.after(l)){
			try	{
				ii = 2/0;
			} catch (Exception e) {
				log.error("End of Job.\n Exception {}\n{}",UICommonUtil.formatMessage(e));
				JOptionPane.showMessageDialog(new JFrame(),"Send Error stack to support.");
				System.exit(666);
			}
		}
	}
}
