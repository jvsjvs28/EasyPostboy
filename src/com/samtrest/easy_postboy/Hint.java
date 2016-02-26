package com.samtrest.easy_postboy;

import java.io.Serializable;

public class Hint implements Serializable{
	private static final long serialVersionUID = 1500803892971084037L;
	private String ident;
	private String check ="";
	private float version;
	private int [] poses = {2,84,56,28,33,1};
	private int [] poses1;
	private String ty = "0";
	public Hint() 
	{
		Memory.setHint(this);
		version =Float.valueOf(Sets.POSTBOY_VERSION.substring(0,Sets.POSTBOY_VERSION.lastIndexOf("."))).floatValue();
	}
	public String getCheck() {
		return check;
	}
	public void setCheck(String check) {
		this.check = check;
	}
	public float getVersion() {
		return version;
	}
	public void setVersion(float version) {
		this.version = version;
	}
	public int[] getPoses() {
		return poses;
	}
	public void setPoses(int[] poses) 
	{
		this.poses = poses;
	}
	public String getTy() {
		return ty;
	}
	public void setTy(String ty) {
		this.ty = ty;
	}
	public void setPoses1(int[] poses1) {
		this.poses1 = poses1;
	}
	public int[] getPoses1() {
		return poses1;
	}
	public String getIdent() {
		return ident;
	}
	public void setIdent(String ident) {
		this.ident = ident;
	}
	
}
