package com.dzt.androidkit.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

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
}
