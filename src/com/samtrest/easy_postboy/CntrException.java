package com.samtrest.easy_postboy;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class CntrException extends Throwable {
	private static final long serialVersionUID = 7485414240459418027L;
	public CntrException(int i) {
		JOptionPane.showMessageDialog(new JFrame(),Memory.getReo()+i+"."); 
	    System.exit(123);
	}
	public CntrException(String i) {
		JOptionPane.showMessageDialog(new JFrame(),Memory.getReo()+i+"."); 
	    System.exit(123); 
	}
}
