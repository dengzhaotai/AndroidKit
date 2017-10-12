package com.dzt.androidkit.scaner;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件扫描器
 */
public class FileScaner {

	private FileScaner() {

	}

	/**
	 * 图片扫描器
	 *
	 * @param context
	 * @return
	 */
	public static List<ImageFileDirectory> scanImageFile(Context context) {

		if (context == null) {
			return null;
		}

		Map<String, ImageFileDirectory> map = new HashMap<>();
		List<ImageFileDirectory> list = new ArrayList<>();
		List<ImageFileDirectory> temp = new ArrayList<>();
		Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		ContentResolver mContentResolver = context.getContentResolver();
		Cursor mCursor = null;
		try {
			mCursor = mContentResolver.query(mImageUri, new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME},
					MediaStore.Images.Media.MIME_TYPE + "= ? or "
							+ MediaStore.Images.Media.MIME_TYPE + "=?",
					new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
			while (mCursor.moveToNext()) {
				String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
				String name = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
				File parent = new File(path);
				String parentName = parent.getParentFile().getName();
				String parentPath = parent.getParentFile().getAbsolutePath();

				ImageFile file = new ImageFile();
				file.setFileName(name);
				file.setFilePath(path);
				if (!map.containsKey(parentName)) {
					ImageFileDirectory dir = new ImageFileDirectory();
					dir.setFileDirName(parentName);
					dir.setFileDirPath(parentPath);
					dir.addImageFile(file);
					list.add(dir);
					map.put(parentName, dir);
				} else {
					map.get(parentName).addImageFile(file);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mCursor != null) {
				mCursor.close();
			}
		}
		// 过滤空的文件夹
		for (ImageFileDirectory dir : list) {
			if (dir.getFiles() != null && dir.getFiles().size() > 0) {
				temp.add(dir);
			}
		}
		list.clear();
		return temp;
	}

	/**
	 * 扫描所有图片文件
	 *
	 * @param context
	 * @return
	 */
	public static List<ImageFile> scanImageFiles(Context context) {
		if (context == null) {
			return null;
		}
		List<ImageFile> temp = new ArrayList<>();
		Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		ContentResolver mContentResolver = context.getContentResolver();
		Cursor mCursor = null;
		try {
			mCursor = mContentResolver.query(mImageUri, new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME},
					MediaStore.Images.Media.MIME_TYPE + "= ? or "
							+ MediaStore.Images.Media.MIME_TYPE + "=?",
					new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
			while (mCursor.moveToNext()) {
				String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
				String name = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
				ImageFile file = new ImageFile();
				file.setFileName(name);
				file.setFilePath(path);
				temp.add(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mCursor != null) {
				mCursor.close();
			}
		}
		return temp;
	}
}
