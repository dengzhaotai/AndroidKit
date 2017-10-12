package com.dzt.androidkit.scaner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageFileDirectory implements Serializable {

	private String fileDirName;

	private String fileDirPath;

	private final List<ImageFile> files = new ArrayList<ImageFile>();

	public String getFileDirName() {
		return fileDirName;
	}

	public void setFileDirName(String fileDirName) {
		this.fileDirName = fileDirName;
	}

	public String getFileDirPath() {
		return fileDirPath;
	}

	public void setFileDirPath(String fileDirPath) {
		this.fileDirPath = fileDirPath;
	}

	public List<ImageFile> getFiles() {
		return files;
	}

	public void addImageFile(ImageFile file) {
		if (file != null) {
			files.add(file);
		}
	}

	public void clear() {
		files.clear();
	}

	@Override
	public boolean equals(Object o) {
		
		ImageFileDirectory obj = (ImageFileDirectory) o ;
		
		return obj != null && fileDirName != null && obj.getFileDirName().equals(fileDirName);
	}
}
