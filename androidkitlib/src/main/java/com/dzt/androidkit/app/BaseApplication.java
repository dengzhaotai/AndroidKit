package com.dzt.androidkit.app;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.dzt.androidkit.utils.JKit;


public abstract class BaseApplication extends MultiDexApplication {
	public static Context applicationContext;

	@Override
	public void onCreate() {
		super.onCreate();
		applicationContext = this;
		JKit.getInstance().init(this);
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
}
