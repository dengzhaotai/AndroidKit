package com.dzt.androidkit.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by dzt on 2017/10/10.
 */
public class JLogKit {

	private final static SimpleDateFormat LOG_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 日志的输出格式
	private final static SimpleDateFormat FILE_SUFFIX = new SimpleDateFormat("yyyy-MM-dd");// 日志文件格式
	public final String SEPARATOR = ",";
	private static Boolean LOG_SWITCH = true; // 日志文件总开关
	private static Boolean LOG_TO_FILE = false; // 日志写入文件开关
	private static String LOG_TAG = "TAG"; // 默认的tag
	private static char LOG_TYPE = 'v';// 输入日志类型，v代表输出所有信息,w则只输出警告...
	private static int LOG_SAVE_DAYS = 7;// sd卡中日志文件的最多保存天数
	private static String LOG_FILE_PATH; // 日志文件保存路径
	private static String LOG_FILE_NAME;// 日志文件保存名称

	private JLogKit() {
	}

	private static class SingletonHolder {
		static JLogKit sInstance = new JLogKit();
	}

	public static JLogKit getInstance() {
		return SingletonHolder.sInstance;
	}

	public void init(Context context) { // 在Application中初始化
		LOG_FILE_PATH = getDiskCacheDir(context) + "/crash";
		LOG_FILE_NAME = "Log";
	}

	public void setTag(String tag) {
		LOG_TAG = tag;
	}

	/****************************
	 * Warn
	 *********************************/
	public void w(Object msg) {
		w(LOG_TAG, msg);
	}

	public void w(String tag, Object msg) {
		w(tag, msg, null);
	}

	public void w(String tag, Object msg, Throwable tr) {
		log(tag, msg.toString(), tr, 'w');
	}

	/***************************
	 * Error
	 ********************************/
	public void e(Object msg) {
		e(LOG_TAG, msg);
	}

	public void e(String tag, Object msg) {
		e(tag, msg, null);
	}

	public void e(String tag, Object msg, Throwable tr) {
		log(tag, msg.toString(), tr, 'e');
	}

	/***************************
	 * Debug
	 ********************************/
	public void d(Object msg) {
		d(LOG_TAG, msg);
	}

	public void d(String tag, Object msg) {// 调试信息
		d(tag, msg, null);
	}

	public void d(String tag, Object msg, Throwable tr) {
		log(tag, msg.toString(), tr, 'd');
	}

	/****************************
	 * Info
	 *********************************/
	public void i(Object msg) {
		i(LOG_TAG, msg);
	}

	public void i(String tag, Object msg) {
		i(tag, msg, null);
	}

	public void i(String tag, Object msg, Throwable tr) {
		log(tag, msg.toString(), tr, 'i');
	}

	/**************************
	 * Verbose
	 ********************************/
	public void v(Object msg) {
		v(LOG_TAG, msg);
	}

	public void v(String tag, Object msg) {
		v(tag, msg, null);
	}

	public void v(String tag, Object msg, Throwable tr) {
		log(tag, msg.toString(), tr, 'v');
	}

	/**
	 * 根据tag, msg和等级，输出日志
	 *
	 * @param tag
	 * @param msg
	 * @param level
	 */
	private void log(String tag, String msg, Throwable tr, char level) {
		if (LOG_SWITCH) {
			StackTraceElement stackTraceElement = Thread.currentThread()
					.getStackTrace()[3];
			//TODO 输出的Log文件名与行号不对，待修改
			String log = getLogInfo(stackTraceElement) + msg;
			if ('e' == level && ('e' == LOG_TYPE || 'v' == LOG_TYPE)) { // 输出错误信息
				Log.e(tag, log, tr);
			} else if ('w' == level && ('w' == LOG_TYPE || 'v' == LOG_TYPE)) {
				Log.w(tag, log, tr);
			} else if ('d' == level && ('d' == LOG_TYPE || 'v' == LOG_TYPE)) {
				Log.d(tag, log, tr);
			} else if ('i' == level && ('d' == LOG_TYPE || 'v' == LOG_TYPE)) {
				Log.i(tag, log, tr);
			} else {
				Log.v(tag, log, tr);
			}
			if (LOG_TO_FILE)
				log2File(String.valueOf(level), tag, log + tr == null ? "" : "\n" + Log.getStackTraceString(tr));
		}
	}

	private String getLogInfo(StackTraceElement stackTraceElement) {
		StringBuilder sb = new StringBuilder();

		// long threadID = Thread.currentThread().getId();

		String fileName = stackTraceElement.getFileName();

		// String className = stackTraceElement.getClassName();

		// String methodName = stackTraceElement.getMethodName();

		int lineNumber = stackTraceElement.getLineNumber();

		sb.append("[ ");
		// sb.append("threadID= " + threadID).append(SEPARATOR);
		// sb.append("method= " + methodName).append(SEPARATOR);
		sb.append("Name= ").append(fileName).append(SEPARATOR);
		sb.append("Number= ").append(lineNumber);
		sb.append(" ] ");
		return sb.toString();
	}

	public void printStack() {
		Log.e(LOG_TAG, Log.getStackTraceString(new Throwable()));
	}

	/**
	 * 打开日志文件并写入日志
	 *
	 * @return
	 **/
	private synchronized void log2File(String mylogtype, String tag, String text) {
		Date nowTime = new Date();
		String date = FILE_SUFFIX.format(nowTime);
		String dateLogContent = LOG_FORMAT.format(nowTime) + ":" + mylogtype + ":" + tag + ":" + text; // 日志输出格式
		File destDir = new File(LOG_FILE_PATH);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		File file = new File(LOG_FILE_PATH, LOG_FILE_NAME + date);
		try {
			FileWriter filerWriter = new FileWriter(file, true);
			BufferedWriter bufWriter = new BufferedWriter(filerWriter);
			bufWriter.write(dateLogContent);
			bufWriter.newLine();
			bufWriter.close();
			filerWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除指定的日志文件
	 */
	public void delFile() {// 删除日志文件
		String needDelFiel = FILE_SUFFIX.format(getDateBefore());
		File file = new File(LOG_FILE_PATH, needDelFiel + LOG_FILE_NAME);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 得到LOG_SAVE_DAYS天前的日期
	 *
	 * @return
	 */
	private Date getDateBefore() {
		Date nowTime = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(nowTime);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - LOG_SAVE_DAYS);
		return now.getTime();
	}

	private boolean sdcardIsReadyForWrite() {
		String state = Environment.getExternalStorageState();
		return state != null ? state.equals(Environment.MEDIA_MOUNTED) : false;
	}

	private String getDiskCacheDir(Context context) {
		if (sdcardIsReadyForWrite()
				|| !Environment.isExternalStorageRemovable()) {
			return context.getExternalCacheDir().getAbsolutePath();
		} else {
			return context.getCacheDir().getAbsolutePath();
		}
	}
}
