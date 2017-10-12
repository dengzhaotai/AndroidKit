package com.dzt.androidkit.utils;

import android.content.Context;

/**
 * Created by dzt on 2017/10/10.
 * 统一初始化工具类，防止忘记，如不需要可注释初始化
 */

public class JKit {
	private JKit() {
	}

	private static class SingletonHolder {
		static JKit sInstance = new JKit();
	}

	public static JKit getInstance() {
		return SingletonHolder.sInstance;
	}

	public void init(Context context) {
		AppResourceKit.getInstance().init(context);
		JLogKit.getInstance().init(context);
		JToastKit.getInstance().init(context);
		JCrashKit.getInstance().init(context);
		ActivityStackManager.getInstance().init();
		JPreferenceKit.getInstance().init(context);
		JDeviceKit.getInstance().init(context);
		JNetworkKit.getInstance().init(context);
		JLocationKit.getInstance().init(context);
	}
}
