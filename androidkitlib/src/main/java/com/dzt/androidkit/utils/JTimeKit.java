package com.dzt.androidkit.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期工具类
 * Created by M02323 on 2017/6/10.
 */

public class JTimeKit {

	private static JTimeKit instance;
	private static SimpleDateFormat sdf = null;

	private JTimeKit() {
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
	}

	public static JTimeKit getInstance() {
		if (instance == null) {
			instance = new JTimeKit();
		}
		return instance;
	}

	/**
	 * 获取的当天时间    例： 几年几月几日
	 */
	public String getNowTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
		return date;
	}

	public String formatUTC(long l, String strPattern) {
		if (TextUtils.isEmpty(strPattern)) {
			strPattern = "yyyy-MM-dd HH:mm:ss";
		}
		sdf.applyPattern(strPattern);
		return sdf == null ? "NULL" : sdf.format(l);
	}

	/**
	 * 时间格式化
	 *
	 * @param second
	 * @return
	 */
	public String second2String(long second) {
		SimpleDateFormat sdf;
		long ms = second * 1000;
		if (second > 3600) {
			sdf = new SimpleDateFormat("HH:mm:ss", Locale.CHINESE);
		} else {
			sdf = new SimpleDateFormat("mm:ss", Locale.CHINESE);
		}
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		return sdf.format(ms);
	}

	/**
	 * 计算时间差
	 *
	 * @param starTime 开始时间
	 * @param endTime  结束时间
	 * @return 返回时间差   返回类型 天，时，分。
	 */
	public static String getTimeDifferenceDay(String starTime, String endTime) {
		String timeString = "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date parse = dateFormat.parse(starTime);
			Date parse1 = dateFormat.parse(endTime);

			long diff = parse1.getTime() - parse.getTime();

			long day = diff / (24 * 60 * 60 * 1000);
			long hour = (diff / (60 * 60 * 1000) - day * 24);
			long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
			long s = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
			long ms = (diff - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
					- min * 60 * 1000 - s * 1000);
			// 上面为day:Hour:min
			// 下面为Hour：min
			long hour1 = diff / (60 * 60 * 1000);
			String hourString = hour1 + "";
			long min1 = ((diff / (60 * 1000)) - hour1 * 60);
			timeString = day + "天" + hour + "小时" + min + "分";
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timeString;
	}

	/**
	 * 计算相差的小时
	 *
	 * @param starTime
	 * @param endTime
	 * @return 时
	 */
	public static String getTimeDifferenceHour(String starTime, String endTime) {
		String timeString = "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date parse = dateFormat.parse(starTime);
			Date parse1 = dateFormat.parse(endTime);

			long diff = parse1.getTime() - parse.getTime();
			String string = Long.toString(diff);
			float parseFloat = Float.parseFloat(string);
			float hour1 = parseFloat / (60 * 60 * 1000);
			timeString = Float.toString(hour1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timeString;
	}
}
