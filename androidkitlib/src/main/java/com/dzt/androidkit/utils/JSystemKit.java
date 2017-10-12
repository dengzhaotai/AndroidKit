package com.dzt.androidkit.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.util.List;

/**
 * Created by M02323 on 2017/7/8.
 */

public class JSystemKit {
	/**
	 * 获取手机系统SDK版本
	 *
	 * @return 如API 17 则返回 17
	 */
	public static int getSDKVersion() {
		return Build.VERSION.SDK_INT;
	}

	/**
	 * 获取系统版本
	 *
	 * @return 形如2.3.3
	 */
	public static String getSystemVersion() {
		return Build.VERSION.RELEASE;
	}

	public static boolean isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					// 后台运行
					return true;
				} else {
					// 前台运行
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * 回到home，后台运行
	 *
	 * @param context
	 */
	public static void goHome(Context context) {
		Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
		mHomeIntent.addCategory(Intent.CATEGORY_HOME);
		mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		context.startActivity(mHomeIntent);
	}

	/**
	 * 获取设备的可用内存大小
	 *
	 * @param cxt
	 * @return 当前内存大小
	 */
	public static int getDeviceUsableMemory(Context cxt) {
		ActivityManager am = (ActivityManager) cxt
				.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(mi);
		// 返回当前系统的可用内存
		return (int) (mi.availMem / (1024 * 1024));
	}

	/**
	 * 清理后台进程与服务
	 *
	 * @param cxt
	 * @return 被清理的数量
	 */
	public static int gc(Context cxt) {
		long i = getDeviceUsableMemory(cxt);
		int count = 0; // 清理掉的进程数
		ActivityManager am = (ActivityManager) cxt
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 获取正在运行的service列表
		List<ActivityManager.RunningServiceInfo> serviceList = am.getRunningServices(100);
		if (serviceList != null)
			for (ActivityManager.RunningServiceInfo service : serviceList) {
				if (service.pid == android.os.Process.myPid())
					continue;
				try {
					android.os.Process.killProcess(service.pid);
					count++;
				} catch (Exception e) {
					e.getStackTrace();
					continue;
				}
			}

		// 获取正在运行的进程列表
		List<ActivityManager.RunningAppProcessInfo> processList = am.getRunningAppProcesses();
		if (processList != null)
			for (ActivityManager.RunningAppProcessInfo process : processList) {
				// 一般数值大于RunningAppProcessInfo.IMPORTANCE_SERVICE的进程都长时间没用或者空进程了
				// 一般数值大于RunningAppProcessInfo.IMPORTANCE_VISIBLE的进程都是非可见进程，也就是在后台运行着
				if (process.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
					// pkgList 得到该进程下运行的包名
					String[] pkgList = process.pkgList;
					for (String pkgName : pkgList) {
						try {
							am.killBackgroundProcesses(pkgName);
							count++;
						} catch (Exception e) { // 防止意外发生
							e.getStackTrace();
							continue;
						}
					}
				}
			}
		return count;
	}
}
