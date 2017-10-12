package com.dzt.androidkit.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by M02323 on 2017/6/10.
 * 网络相关操作工具
 */

public class JNetworkKit {
	public static final int NETWORK_NO = -1;   // no network
	public static final int NETWORK_WIFI = 1;    // wifi network
	public static final int NETWORK_2G = 2;    // "2G" networks
	public static final int NETWORK_3G = 3;    // "3G" networks
	public static final int NETWORK_4G = 4;    // "4G" networks
	public static final int NETWORK_UNKNOWN = 5;    // unknown network

	private static final int NETWORK_TYPE_GSM = 16;
	private static final int NETWORK_TYPE_TD_SCDMA = 17;
	private static final int NETWORK_TYPE_IWLAN = 18;

	private Context context;
	private ConnectivityManager cm;
	private TelephonyManager tm;
	private WifiManager wm;
	private LocationManager lm;

	private JNetworkKit() {
	}

	private static class SingletonHolder {
		static JNetworkKit sInstance = new JNetworkKit();
	}

	public static JNetworkKit getInstance() {
		return SingletonHolder.sInstance;
	}

	public void init(Context context) {
		this.context = context;
		cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		lm = ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
	}

	/**
	 * 需添加权限
	 *
	 * @return 网络类型
	 * @code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
	 * <p>
	 * 它主要负责的是
	 * 1 监视网络连接状态 包括（Wi-Fi, 2G, 3G, 4G）
	 * 2 当网络状态改变时发送广播通知
	 * 3 网络连接失败尝试连接其他网络
	 * 4 提供API，允许应用程序获取可用的网络状态
	 * <p>
	 * netTyped 的结果
	 * @link #NETWORK_NO      = -1; 当前无网络连接
	 * @link #NETWORK_WIFI    =  1; wifi的情况下
	 * @link #NETWORK_2G      =  2; 切换到2G环境下
	 * @link #NETWORK_3G      =  3; 切换到3G环境下
	 * @link #NETWORK_4G      =  4; 切换到4G环境下
	 * @link #NETWORK_UNKNOWN =  5; 未知网络
	 */
	public int getNetWorkType() {
		NetworkInfo ni = cm.getActiveNetworkInfo();// 获取当前网络状态
		int netType;
		if (ni != null && ni.isConnectedOrConnecting()) {
			switch (ni.getType()) {//获取当前网络的状态
				case ConnectivityManager.TYPE_WIFI:// wifi的情况下
					netType = NETWORK_WIFI;
					JToastKit.getInstance().showMessage("切换到wifi环境下");
					break;
				case ConnectivityManager.TYPE_MOBILE:
					switch (ni.getSubtype()) {
						case NETWORK_TYPE_GSM:
						case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
						case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
						case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
						case TelephonyManager.NETWORK_TYPE_1xRTT:
						case TelephonyManager.NETWORK_TYPE_IDEN:
							netType = NETWORK_2G;
							JToastKit.getInstance().showMessage("切换到2G环境下");
							break;
						case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
						case TelephonyManager.NETWORK_TYPE_UMTS:
						case TelephonyManager.NETWORK_TYPE_EVDO_0:
						case TelephonyManager.NETWORK_TYPE_HSDPA:
						case TelephonyManager.NETWORK_TYPE_HSUPA:
						case TelephonyManager.NETWORK_TYPE_HSPA:
						case TelephonyManager.NETWORK_TYPE_EVDO_B:
						case TelephonyManager.NETWORK_TYPE_EHRPD:
						case TelephonyManager.NETWORK_TYPE_HSPAP:
						case NETWORK_TYPE_TD_SCDMA:
							netType = NETWORK_3G;
							JToastKit.getInstance().showMessage("切换到3G环境下");
							break;
						case TelephonyManager.NETWORK_TYPE_LTE:

						case NETWORK_TYPE_IWLAN:
							netType = NETWORK_4G;
							JToastKit.getInstance().showMessage("切换到4G环境下");
							break;
						default:

							String subtypeName = ni.getSubtypeName();
							if (subtypeName.equalsIgnoreCase("TD-SCDMA")
									|| subtypeName.equalsIgnoreCase("WCDMA")
									|| subtypeName.equalsIgnoreCase("CDMA2000")) {
								netType = NETWORK_3G;
							} else {
								netType = NETWORK_UNKNOWN;
							}
							JToastKit.getInstance().showMessage("未知网络");
					}
					break;
				default:
					netType = 5;
					JToastKit.getInstance().showMessage("未知网络");
			}
		} else {
			netType = NETWORK_NO;
			JToastKit.getInstance().showMessage("当前无网络连接");
		}
		return netType;
	}

	/**
	 * 获取当前的网络类型(WIFI,2G,3G,4G)
	 * <p>依赖上面的方法</p>
	 *
	 * @return 网络类型名称
	 * <ul>
	 * <li>NETWORK_WIFI   </li>
	 * <li>NETWORK_4G     </li>
	 * <li>NETWORK_3G     </li>
	 * <li>NETWORK_2G     </li>
	 * <li>NETWORK_UNKNOWN</li>
	 * <li>NETWORK_NO     </li>
	 * </ul>
	 */
	public String getNetWorkTypeName() {
		switch (getNetWorkType()) {
			case NETWORK_WIFI:
				return "NETWORK_WIFI";
			case NETWORK_4G:
				return "NETWORK_4G";
			case NETWORK_3G:
				return "NETWORK_3G";
			case NETWORK_2G:
				return "NETWORK_2G";
			case NETWORK_NO:
				return "NETWORK_NO";
			default:
				return "NETWORK_UNKNOWN";
		}
	}

	/**
	 * 打开网络设置界面
	 */
	public void openSetting(Activity activity) {
		Intent intent = new Intent("/");
		ComponentName cm = new ComponentName("com.android.settings",
				"com.android.settings.WirelessSettings");
		intent.setComponent(cm);
		intent.setAction(Intent.ACTION_VIEW);
		activity.startActivityForResult(intent, 0);
	}

	/**
	 * 打开网络设置界面
	 * <p>3.0以下打开设置界面</p>
	 */
	public void openWirelessSettings() {
		if (Build.VERSION.SDK_INT > 10) {
			context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
		} else {
			context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
		}
	}

	/**
	 * Network control
	 *
	 * @param enabled
	 */
	public void controlNetWork(boolean enabled) {
		controlWifi(enabled);
		controlMobileNetWork(enabled);
	}

	/**
	 * wifi control
	 *
	 * @param enabled 打开或关闭Wifi
	 */
	public void controlWifi(boolean enabled) {
		if (wm.getWifiState() == WifiManager.WIFI_STATE_ENABLING
				|| wm.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
			wm.setWifiEnabled(enabled);
		} else {
			wm.setWifiEnabled(enabled);
		}
	}

	/**
	 * 判断有无网络链接
	 *
	 * @return
	 */
	public boolean checkNetworkInfo() {
		NetworkInfo.State mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		NetworkInfo.State wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING)
			return true;
		if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING)
			return true;
		return false;
	}

	/**
	 * 判断网络是否连接
	 *
	 * @return
	 */
	public boolean isNetworkConnected() {
		if (cm == null)
			return false;
		NetworkInfo info = cm.getActiveNetworkInfo();
		return info == null ? false : info.isConnected();
	}

	/**
	 * 判断是否是wifi连接
	 */
	public boolean isWifiConnected() {
		if (cm == null)
			return false;
		NetworkInfo networkINfo = cm.getActiveNetworkInfo();
		return networkINfo == null ? false : (networkINfo.getType() == ConnectivityManager.TYPE_WIFI);
	}

	/**
	 * 判断是否为3G网络
	 */
	public boolean is3rd() {
		if (cm == null)
			return false;
		NetworkInfo networkINfo = cm.getActiveNetworkInfo();
		return networkINfo != null
				&& networkINfo.getType() == ConnectivityManager.TYPE_MOBILE;
	}

	/**
	 * 判断网络是否是4G
	 * 需添加权限
	 *
	 * @code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
	 */
	public boolean is4G() {
		if (cm == null)
			return false;
		NetworkInfo info = cm.getActiveNetworkInfo();
		return info != null && info.isAvailable() && info.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE;
	}

	/**
	 * GPS是否打开
	 *
	 * @return
	 */
	public boolean isGpsEnabled() {
		List<String> accessibleProviders = lm.getProviders(true);
		return accessibleProviders != null && accessibleProviders.size() > 0;
	}

	/**
	 * network control
	 *
	 * @param enabled
	 */
	public void controlMobileNetWork(boolean enabled) {
		try {
			final Class conmanClass = Class
					.forName(cm.getClass().getName());
			final Field iConnectivityManagerField = conmanClass
					.getDeclaredField("mService");
			iConnectivityManagerField.setAccessible(true);
			final Object iConnectivityManager = iConnectivityManagerField
					.get(cm);
			final Class iConnectivityManagerClass = Class
					.forName(iConnectivityManager.getClass().getName());
			final Method setMobileDataEnabledMethod = iConnectivityManagerClass
					.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
			setMobileDataEnabledMethod.setAccessible(true);
			setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	/**
	 * 判断网络连接是否可用
	 *
	 * @return
	 */
	public boolean isNetworkAvailable() {
		if (cm == null)
			return false;
		//如果仅仅是用来判断网络连接
		//则可以使用 cm.getActiveNetworkInfo().isAvailable();
		NetworkInfo[] info = cm.getAllNetworkInfo();
		if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断网络是否可用
	 * 需添加权限
	 *
	 * @code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
	 */
	public boolean isAvailable() {
		NetworkInfo info = cm.getActiveNetworkInfo();
		return info != null && info.isAvailable();
	}

	/**
	 * 获取移动网络运营商名称
	 * <p>如中国联通、中国移动、中国电信</p>
	 *
	 * @return 移动网络运营商名称
	 */
	public String getNetworkOperatorName() {
		return tm == null ? null : tm.getNetworkOperatorName();
	}

	/**
	 * 获取移动终端类型
	 *
	 * @return 手机制式
	 * <ul>
	 * <li>{@link TelephonyManager#PHONE_TYPE_NONE } : 0 手机制式未知</li>
	 * <li>{@link TelephonyManager#PHONE_TYPE_GSM  } : 1 手机制式为GSM，移动和联通</li>
	 * <li>{@link TelephonyManager#PHONE_TYPE_CDMA } : 2 手机制式为CDMA，电信</li>
	 * <li>{@link TelephonyManager#PHONE_TYPE_SIP  } : 3</li>
	 * </ul>
	 */
	public int getPhoneType() {
		return tm == null ? -1 : tm.getPhoneType();
	}

	/**
	 * 检测网络是否可上网
	 * 使用：pingIpAddress("www.baidu.com");
	 * exec("/system/bin/ping -c 3 -w 100 " + ipAddress);
	 *
	 * @param ipAddress
	 * @return
	 */
	public boolean pingIpAddress(String ipAddress) {
		try {
			//ping网址3次
			Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ipAddress);
			//ping状态
			int status = p.waitFor();
			if (status == 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
