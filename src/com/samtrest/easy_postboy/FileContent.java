package com.samtrest.easy_postboy;

public class FileContent {
	String fileName;
	String content;
	public FileContent(String fileName, String content) {
		super();
		this.fileName = fileName;
		this.content = content;
	}
	public String getFileName() {
		return fileName;
	}
	public String getContent() {
		return content;
	}

}
