package com.dzt.androidkit.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.dzt.androidkit.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dzt on 2017/9/18.
 * 加载App资源，使用单例
 * 如需使用，请在Application中初始化AppResourceKit.getInstance().init(this);
 */

public class AppResourceKit {
	private Resources mRes;
	private Context context;

	private AppResourceKit() {
	}

	private static class SingletonHolder {
		static AppResourceKit sInstance = new AppResourceKit();
	}

	public static AppResourceKit getInstance() {
		return SingletonHolder.sInstance;
	}

	public void init(Context context) {
		this.context = context;
		mRes = context.getResources();
	}

	public Context getContext(){
		return context;
	}

	public Resources getResources(){
		return mRes;
	}

	public int getDimensionPixelSize(int rid) {
		return mRes == null ? 0 : mRes.getDimensionPixelSize(rid);
	}

	public int getDimensionPixelOffset(int rid) {
		return mRes == null ? 0 : mRes.getDimensionPixelOffset(rid);
	}

	public Drawable getDrawable(int rid) {
		return mRes == null ? null : mRes.getDrawable(rid);
	}

	public int getColor(int rid) {
		return mRes == null ? 0 : mRes.getColor(rid);
	}

	public ColorStateList getColorStateList(int rid) {
		return mRes == null ? null : mRes.getColorStateList(rid);
	}

	public String getString(int rid) {
		return mRes == null ? null : mRes.getString(rid);
	}

	public String[] getStringArray(int rid) {
		return mRes == null ? null : mRes.getStringArray(rid);
	}

	private static final String DATABASE_NAME = "city.db";
	public void importCityDb(){
		// 判断保持城市的数据库文件是否存在
		File file = new File(context.getDatabasePath(DATABASE_NAME).getAbsolutePath());
		if (!file.exists()) {// 如果不存在，则导入数据库文件
			//数据库文件
			File dbFile = context.getDatabasePath(DATABASE_NAME);
			try {
				if (!dbFile.getParentFile().exists()) {
					dbFile.getParentFile().mkdir();
				}
				if (!dbFile.exists()) {
					dbFile.createNewFile();
				}
				//加载欲导入的数据库
				InputStream is = context.getResources().openRawResource(R.raw.city);
				FileOutputStream fos = new FileOutputStream(dbFile);
				byte[] buffer = new byte[is.available()];
				is.read(buffer);
				fos.write(buffer);
				is.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
