package com.dzt.androidkit.utils;

import java.text.DecimalFormat;

/**
 * Created by M02323 on 2017/3/24.
 */

public class JMathKit {
	private static JMathKit instance;
	/**
	 * gb to byte
	 **/
	public static final long GB_2_BYTE = 1073741824;
	/**
	 * mb to byte
	 **/
	public static final long MB_2_BYTE = 1048576;
	/**
	 * kb to byte
	 **/
	public static final long KB_2_BYTE = 1024;

	public enum Size_Type {
		SIZE_TYPE_B(1), SIZE_TYPE_KB(2), SIZE_TYPE_MB(3), SIZE_TYPE_GB(4);
		int type;

		Size_Type(int type) {
			this.type = type;
		}

		public int getType() {
			return type;
		}
	}

	private JMathKit() {

	}

	public static JMathKit getInstance() {
		if (instance == null) {
			instance = new JMathKit();
		}
		return instance;
	}

	/**
	 * The data format (0 and # is a placeholder, the difference is a
	 * placeholder for the inadequacies of the 0, with 0 up, but not #)
	 *
	 * @param data
	 * @return
	 */
	public String dataFormat(Object data) {
		return dataFormat("#.0", data);
	}

	/**
	 * The data format (0 and # is a placeholder, the difference is a
	 * placeholder for the inadequacies of the 0, with 0 up, but not #)
	 *
	 * @param data
	 * @return
	 */
	public String dataFormat(String pattern, Object data) {
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		return decimalFormat.format(data);
	}

	/**
	 * 转换文件大小
	 *
	 * @param fileS
	 * @return
	 */
	public String FormatFileSize(long fileS) {
		if (fileS == 0) {
			return "0B";
		}
		String fileSizeString;
		if (fileS < KB_2_BYTE) {
			fileSizeString = FormatFileSize(fileS, Size_Type.SIZE_TYPE_B) + "B";
		} else if (fileS < MB_2_BYTE) {
			fileSizeString = FormatFileSize(fileS, Size_Type.SIZE_TYPE_KB) + "KB";
		} else if (fileS < GB_2_BYTE) {
			fileSizeString = FormatFileSize(fileS, Size_Type.SIZE_TYPE_MB) + "MB";
		} else {
			fileSizeString = FormatFileSize(fileS, Size_Type.SIZE_TYPE_GB) + "GB";
		}
		return fileSizeString;
	}

	/**
	 * Convert file size, specify the type of conversion
	 *
	 * @param fileS
	 * @param sizeType
	 * @return
	 */
	public double FormatFileSize(long fileS, Size_Type sizeType) {
		DecimalFormat df = new DecimalFormat("#.00");
		double fileSizeLong = 0;
		switch (sizeType) {
			case SIZE_TYPE_B:
				fileSizeLong = Double.valueOf(df.format((double) fileS));
				break;
			case SIZE_TYPE_KB:
				fileSizeLong = Double.valueOf(df.format((double) fileS / KB_2_BYTE));
				break;
			case SIZE_TYPE_MB:
				fileSizeLong = Double.valueOf(df.format((double) fileS / MB_2_BYTE));
				break;
			case SIZE_TYPE_GB:
				fileSizeLong = Double.valueOf(df
						.format((double) fileS / GB_2_BYTE));
				break;
			default:
				break;
		}
		return fileSizeLong;
	}

	public byte[] int2Byte(int value) {
		byte[] result = new byte[4];
		for (int i = 0; i < 4; i++) {
			int offset = 32 - (i + 1) * 8;
			result[i] = (byte) ((value >> offset) & 0xff);
		}
		return result;
	}

	public int byte2Int(byte[] data) {
		int st = 0;
		for (int i = 0; i < data.length; i++) {
			st <<= 8;
			st += (data[i] & 0xFF);
		}
		return st;
	}

	public byte[] long2Byte(long value) {
		byte[] result = new byte[8];
		for (int i = 0; i < 8; i++) {
			int offset = 64 - (i + 1) * 8;
			result[i] = (byte) ((value >> offset) & 0xff);
		}
		return result;
	}

	public long byte2Long(byte[] data) {
		long st = 0;
		for (int i = 0; i < data.length; i++) {
			st <<= 8;
			st += (data[i] & 0xFF);
		}
		return st;
	}

	/**
	 * 得到指定位数的浮点型
	 *
	 * @param value
	 * @param decimal 要保留的小数位数
	 * @return
	 */
	public String getDecimalFormat(double value, int decimal) {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(decimal);
		return df.format(value);
	}

	/**
	 * 整形转为四位十六进制IP
	 *
	 * @param ip
	 * @return
	 */
	public String intToIpString(int ip) {
		return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "."
				+ ((ip >> 24) & 0xFF);
	}
}
