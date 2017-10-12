package com.dzt.androidkit.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * CRC校验工具类
 * Created by dzt on 2017/3/24.
 */

public class JCrcKit {
	//CRC字节数，可改变
	public static final int CRC_LEN = 2;

	/**
	 * 为Byte数组添加 CRC_LEN 位CRC校验
	 *
	 * @param data
	 * @return
	 */
	public static byte[] generateCRC(byte[] data) {
		int MASK = 0x0001;
		int CRCSEED = 0x0810;
		int remain = 0;

		byte val;
		for (int i = 0; i < data.length; i++) {
			val = data[i];
			for (int j = 0; j < 8; j++) {
				if (((val ^ remain) & MASK) != 0) {
					remain ^= CRCSEED;
					remain >>= 1;
					remain |= 0x8000;
				} else {
					remain >>= 1;
				}
				val >>= 1;
			}
		}

		byte[] crcByte = new byte[CRC_LEN];
		for (int i = 0; i < CRC_LEN; i++) {
			int offset = CRC_LEN * 8 - (i + 1) * 8;
			crcByte[i] = (byte) ((remain >> offset) & 0xff);
		}

		// 将新生成的byte数组添加到原数据结尾并返回
		return concatBytes(data, crcByte);
	}

	/***
	 * CRC校验是否通过
	 *
	 * @param srcByte
	 * @return
	 */
	public static boolean isPassCRC(byte[] srcByte) {
		// 取出除crc校验位的其他数组，进行计算，得到CRC校验结果
		int calcCRC = calcCRC(srcByte, 0, srcByte.length - CRC_LEN);
		// 取出CRC校验位，进行计算
		byte[] binary = getBytesByindex(srcByte,
				srcByte.length - CRC_LEN, srcByte.length - 1);
		int receive = byte2Int(binary);
		// 比较
		return (calcCRC == receive);
	}

	private static int byte2Int(byte[] data) {
		int st = 0;
		for (int i = 0; i < data.length; i++) {
			st <<= 8;
			st += (data[i] & 0xFF);
		}
		return st;
	}

	/**
	 * 根据起始和结束下标截取byte数组
	 *
	 * @param bytes
	 * @param start
	 * @param end
	 * @return
	 */
	private static byte[] getBytesByindex(byte[] bytes, int start, int end) {
		byte[] returnBytes = new byte[end - start + 1];
		for (int i = 0; i < returnBytes.length; i++) {
			returnBytes[i] = bytes[start + i];
		}
		return returnBytes;
	}

	/**
	 * 对buf中offset以前crcLen长度的字节作crc校验，返回校验结果
	 *
	 * @param buf    byte[]
	 * @param offset int
	 * @param crcLen int　crc校验的长度
	 * @return int　crc结果
	 */
	private static int calcCRC(byte[] buf, int offset, int crcLen) {
		int MASK = 0x0001;
		int CRCSEED = 0x0810;
		int start = offset;
		int end = offset + crcLen;
		int remain = 0;

		byte val;
		for (int i = start; i < end; i++) {
			val = buf[i];
			for (int j = 0; j < 8; j++) {
				if (((val ^ remain) & MASK) != 0) {
					remain ^= CRCSEED;
					remain >>= 1;
					remain |= 0x8000;
				} else {
					remain >>= 1;
				}
				val >>= 1;
			}
		}
		return remain;
	}

	/**
	 * 多个byte数组合并
	 *
	 * @param first
	 * @param rest
	 * @return
	 */
	public static byte[] concatBytes(byte[] first, byte[]... rest) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			baos.write(first);
			for (byte[] array : rest) {
				baos.write(array);
			}
			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
