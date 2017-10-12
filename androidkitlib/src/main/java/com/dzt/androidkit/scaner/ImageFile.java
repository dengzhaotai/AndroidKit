package com.dzt.androidkit.scaner;

import java.io.Serializable;

public class ImageFile implements Serializable {

	private String fileName;

	private String filePath;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
