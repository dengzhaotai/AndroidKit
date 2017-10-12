package com.dzt.androidkit.utils;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

/**
 * Created by M02323 on 2017/7/12.
 */

public class JMemoryKit {


	/**
	 * 检查磁盘空间是否大于10mb
	 *
	 * @return true 大于
	 */
	public static boolean isDiskAvailable() {
		long size = getDiskAvailableSize();
		return size > 10 * 1024 * 1024; // > 10bm
	}

	/**
	 * 获取磁盘可用空间
	 *
	 * @return byte 单位 kb
	 */
	public static long getDiskAvailableSize() {
		if (!sdcardIsReadyForWrite())
			return 0;
		String sdPath = getSDCardPath();
		if(TextUtils.isEmpty(sdPath))
			return 0;
		StatFs stat = new StatFs(sdPath);
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
		// (availableBlocks * blockSize)/1024 KIB 单位
		// (availableBlocks * blockSize)/1024 /1024 MIB单位
	}

	/**
	 * Detection of SD can write
	 *
	 * @return
	 */
	private static boolean sdcardIsReadyForWrite() {
		String state = Environment.getExternalStorageState();
		return state != null ? state.equals(Environment.MEDIA_MOUNTED) : false;
	}

	/**
	 * Access to the path of the SDCard
	 *
	 * @return
	 */
	private static String getSDCardPath() {
		if (!sdcardIsReadyForWrite()) {
			return null;
		}
		return Environment.getExternalStorageDirectory()
				.getAbsolutePath();
	}
}
