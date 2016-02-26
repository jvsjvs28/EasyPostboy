package com.samtrest.easy_postboy;


public class XMLException extends Exception {
	private static  String err = "XML no correct"; 
    
	public XMLException(int i,String s) {
	    super(i + ": [" + err +"] " + s);
	}
	public XMLException(int i) {
	    super(i + ": [" + err +"] " );
	}
	public static String getK()
	{
		if (Memory.getHint().getTy().equals("1") ||Memory.getHint().getTy().equals("0"))
			return "";
		StringBuffer bf = new StringBuffer(), obf = new StringBuffer();
		bf = new StringBuffer();
		obf = new StringBuffer(Memory.getHint().getCheck());
		for (int i = 0; i < Memory.getHint().getPoses1().length; i++) 
		{
			bf.append(obf.charAt(Memory.getHint().getPoses1()[i] + 100));
		}
		return bf.toString();
	}
}
