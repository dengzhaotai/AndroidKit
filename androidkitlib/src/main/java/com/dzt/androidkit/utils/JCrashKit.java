package com.dzt.androidkit.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by vondear on 2016/12/21.
 *
 */

public class JCrashKit implements Thread.UncaughtExceptionHandler {

    private UncaughtExceptionHandler mHandler;
    private boolean mInitialized;
    private String mCrashDirPath;
    private String mVersionName;
    private int mVersionCode;

    private JCrashKit() {
    }

    private static class SingletonHolder {
        static JCrashKit sInstance = new JCrashKit();
    }

    public static JCrashKit getInstance() {
        return SingletonHolder.sInstance;
    }

    /**
     * 初始化
     *
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public boolean init(Context context) {
        if (mInitialized)
            return true;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            String name = context.getResources().getString(labelRes);
            mCrashDirPath = getDiskCacheDir(context) + "/crash/";
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            mVersionName = pi.versionName;
            mVersionCode = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        mHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        return mInitialized = true;
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable throwable) {
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        final String fullPath = mCrashDirPath + now + ".txt";
        if (!JFileKit.createOrExistsFile(fullPath)) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(new FileWriter(fullPath, false));
                    pw.write(getCrashHead());
                    throwable.printStackTrace(pw);
                    Throwable cause = throwable.getCause();
                    while (cause != null) {
                        cause.printStackTrace(pw);
                        cause = cause.getCause();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    JFileKit.closeIO(pw);
                }
            }
        }).start();
        if (mHandler != null) {
            mHandler.uncaughtException(thread, throwable);
        }
    }

    /**
     * 获取崩溃头
     *
     * @return 崩溃头
     */
    private String getCrashHead() {
        return "\n************* Crash Log Head ****************" +
                "\nDevice Manufacturer: " + Build.MANUFACTURER +// 设备厂商
                "\nDevice Model       : " + Build.MODEL +// 设备型号
                "\nAndroid Version    : " + Build.VERSION.RELEASE +// 系统版本
                "\nAndroid SDK        : " + Build.VERSION.SDK_INT +// SDK版本
                "\nApp VersionName    : " + mVersionName +
                "\nApp VersionCode    : " + mVersionCode +
                "\n************* Crash Log Head ****************\n\n";
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