package com.dzt.kit;

import com.dzt.androidkit.app.BaseApplication;
import com.dzt.androidkit.utils.JLogKit;

/**
 * Created by dzt on 2017/10/11.
 * 使用Android Material Design UI
 * 把一些基础类和工具类封装成库，方便调用
 */

public class LocalApp extends BaseApplication{
	@Override
	public void onCreate() {
		super.onCreate();
		JLogKit.getInstance().setTag("kit->");
		JLogKit.getInstance().setLog2File(true);
	}
}
