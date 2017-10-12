package com.dzt.androidkit.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Android Operating file tools
 *
 * @author
 */
public class JFileKit {
	/**
	 * Detection of SD is readable
	 *
	 * @return
	 */
	public static boolean sdcardIsReadyForRead() {
		String state = Environment.getExternalStorageState();
		return state != null ? state.equals(Environment.MEDIA_MOUNTED_READ_ONLY) : false;
	}

	/**
	 * Detection of SD can write
	 *
	 * @return
	 */
	public static boolean sdcardIsReadyForWrite() {
		String state = Environment.getExternalStorageState();
		return state != null ? state.equals(Environment.MEDIA_MOUNTED) : false;
	}

	/**
	 * Access to the path of the SDCard
	 *
	 * @return
	 */
	public static String getSDCardPath() {
		if (!sdcardIsReadyForWrite()) {
			return null;
		}
		return Environment.getExternalStorageDirectory()
				.getAbsolutePath();
	}

	/**
	 * Access to the path of the hard disk cache
	 *
	 * @author blue
	 */
	public static String getDiskCacheDir(Context context) {
		if (sdcardIsReadyForWrite()
				|| !Environment.isExternalStorageRemovable()) {
			return context.getExternalCacheDir().getAbsolutePath();
		} else {
			return context.getCacheDir().getAbsolutePath();
		}
	}

	/**
	 * Get the path address of the hard disk cache based on the incoming
	 * uniqueName
	 *
	 * @param context
	 * @param uniqueName
	 * @return
	 */
	public static File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath;
		if (sdcardIsReadyForWrite()
				|| !Environment.isExternalStorageRemovable()) {
			cachePath = context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
	}

	/**
	 * Create a directory or file in SDCard
	 * <p/>
	 * If you have a file with the same name, delete the file and then create
	 * the same name. The folder does not do any operations.
	 *
	 * @param path Folder relative path
	 */
	public static String createFileOnSDCard(String path) {
		if (!sdcardIsReadyForWrite()) {
			return null;
		}
		if (TextUtils.isEmpty(path))
			return null;
		if (!path.startsWith(File.separator)) {
			path = File.separator + path;
		}
		String sdPath = getSDCardPath();
		if (TextUtils.isEmpty(sdPath))
			return null;
		String filePath = sdPath + path;
		File file = new File(filePath);
		if (file.exists() && file.isFile()) {
			file.delete();
		}
		if (!createFolder(file)) {
			return null;
		}
		return filePath;
	}

	/**
	 * 判断文件是否存在，不存在则判断是否创建成功
	 *
	 * @param filePath 文件路径
	 * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
	 */
	public static boolean createOrExistsFile(String filePath) {
		return createOrExistsFile(getFileByPath(filePath));
	}

	/**
	 * 判断文件是否存在，不存在则判断是否创建成功
	 *
	 * @param file 文件
	 * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
	 */
	public static boolean createOrExistsFile(File file) {
		if (file == null) return false;
		// 如果存在，是文件则返回true，是目录则返回false
		if (file.exists()) return file.isFile();
		if (!createOrExistsDir(file.getParentFile())) return false;
		try {
			return file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 判断目录是否存在，不存在则判断是否创建成功
	 *
	 * @param file 文件
	 * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
	 */
	public static boolean createOrExistsDir(File file) {
		// 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
		return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
	}

	/**
	 * Delete SDCard files or folders
	 *
	 * @param path file path
	 */
	public static boolean deleteFileOnSDCard(String path) {
		if (!sdcardIsReadyForWrite()) {
			return false;
		}
		String sdPath = getSDCardPath();
		if (TextUtils.isEmpty(sdPath))
			return false;
		if (!path.startsWith(File.separator)) {
			path = sdPath + File.separator + path;
		}
		File file = new File(path);
		return deleteFile(file);
	}

	/**
	 * get data/data/package/files folder
	 * <p/>
	 * The directory is automatically deleted when you uninstall the program
	 *
	 * @param context
	 * @return
	 */
	public static String getDataFolderPath(Context context) {
		return context.getFilesDir().getAbsolutePath();
	}

	public static String getDatabaseDir(Context context) {
		return (context.getFilesDir().getPath() + "/databases");
	}

	/**
	 * in data/data/package/files create file or folder
	 * <p/>
	 * If you have a file with the same name, delete the file and then create
	 * the same name. The folder does not do any operations.
	 * <p/>
	 * The directory is automatically deleted when you uninstall the program
	 *
	 * @param path Folder absolute path
	 */
	public static String createFileOnDataFolder(Context contenxt, String path) {
		if (!path.startsWith(File.separator)) {
			path = getDataFolderPath(contenxt) + File.separator + path;
		}

		File file = new File(path);
		deleteFile(file);

		if (!createFolder(file)) {
			return null;
		}
		return path;
	}

	/**
	 * delete data/data/package/files file or folder
	 * <p/>
	 * The directory is automatically deleted when you uninstall the program
	 *
	 * @param path file path
	 */
	public static boolean deleteFileOnDataFolder(String path) {
		if (!path.startsWith(File.separator)) {
			path = File.separator + path;
		}
		String sdPath = getSDCardPath();
		if (TextUtils.isEmpty(sdPath))
			return false;
		File file = new File(sdPath + path);
		return deleteFile(file);
	}

	/**
	 * Recursively delete all files in the specified folder (including the
	 * folder)
	 *
	 * @param file
	 * @author andrew
	 */
	public static boolean deleteAll(File file) {
		if (file == null || !file.exists()) {
			return true;
		}
		if (file.isFile() || file.list().length == 0) {
			return file.delete();
		} else {
			File[] files = file.listFiles();
			for (File f : files) {
				deleteAll(f);// delete file
			}
			return file.delete();
		}
	}

	public static boolean deleteFile(File file) {
		return file.isFile() && file.exists() ? file.delete() : false;
	}

	/**
	 * Deletes all files in the specified folder, but does not delete the
	 * directory.
	 *
	 * @param path
	 */
	public static void deleteAll(String path) {
		File file = new File(path);
		File[] files = file.listFiles();
		for (File f : files) {
			deleteAll(f);
			f.delete();
		}
	}


	public static double getFileOrFolderSize(String filePath) {
		return getFileOrFolderSize(filePath, JMathKit.Size_Type.SIZE_TYPE_KB);
	}

	/**
	 * Gets the size of the specified unit of the specified file
	 *
	 * @param filePath file path
	 * @param sizeType get size type 1 is byte、2 is KB、3 is MB、4 is GB
	 * @return size
	 */
	public static double getFileOrFolderSize(String filePath, JMathKit.Size_Type sizeType) {
		File file = new File(filePath);
		long blockSize = 0;
		try {
			if (file.isDirectory()) {
				blockSize = getDirSize(file);
			} else {
				blockSize = getFileSize(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JMathKit.getInstance().FormatFileSize(blockSize, sizeType);
	}

	/**
	 * Calling this method automatically calculates the size of the specified
	 * file or the specified folder
	 *
	 * @param filePath file path
	 * @return Calculated with B, KB, MB, GB of the string
	 */
	public static String getAutoFileOrFolderSize(String filePath) {
		File file = new File(filePath);
		long blockSize = 0;
		try {
			if (file.isDirectory()) {
				blockSize = getDirSize(file);
			} else {
				blockSize = getFileSize(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JMathKit.getInstance().FormatFileSize(blockSize);
	}

	/**
	 * Gets the specified file size
	 *
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private static long getFileSize(File file) {
		long size = 0;
		if (file.exists()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				size = fis.available();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				closeQuietly(fis);
			}
		}
		return size;
	}

	/**
	 * 得到文件或目录的大小
	 *
	 * @param file
	 * @return
	 */
	public static long getFileOrFolderSize(File file) {
		if (!file.exists())
			return 0;
		if (!file.isDirectory())
			return file.length();

		long length = 0;
		File[] list = file.listFiles();
		// 文件夹被删除时, 子文件正在被写入, 文件属性异常返回null.
		if (list != null) {
			for (File item : list) {
				length += getFileOrFolderSize(item);
			}
		}
		return length;
	}

	/**
	 * 从InputStream中复制到指定文件下
	 *
	 * @param path  文件路径
	 * @param name  文件名
	 * @param input 源文件流
	 * @return
	 */
	public static boolean copyFromInput(String path, String name, InputStream input) {
		File file;
		OutputStream output = null;   //创建一个写入字节流对象
		try {
			createFolder(path);
			file = createFile(path, name); //根据传入的文件名创建
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];   //每次读取4K
			int num;      //需要根据读取的字节大小写入文件
			while ((num = (input.read(buffer))) != -1) {
				output.write(buffer, 0, num);
			}
			output.flush();  //清空缓存
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeQuietly(output);
		}
		return true;
	}

	/**
	 * 将一个InputStream字节流写入到SD卡中
	 */
	public static File write2SDFromInput(String dir, String name, InputStream input) {
		File file = null;
		OutputStream output = null;   //创建一个写入字节流对象
		String sdPath = getSDCardPath();
		if (TextUtils.isEmpty(sdPath))
			return null;
		try {
			createFolder(sdPath, dir);    //根据传入的路径创建目录
			file = createFile(sdPath + File.separator + dir, name); //根据传入的文件名创建
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];   //每次读取4K
			int num;      //需要根据读取的字节大小写入文件
			while ((num = (input.read(buffer))) != -1) {
				output.write(buffer, 0, num);
			}
			output.flush();  //清空缓存
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeQuietly(output);
		}
		return file;
	}

	/**
	 * 把传入的字符流写入到SD卡中
	 *
	 * @param dir
	 * @param name
	 * @param input
	 * @return
	 */
	public static File write2SDFromWrite(String dir, String name, BufferedReader input) {
		File file = null;
		FileWriter output = null;   //创建一个写入字符流对象
		BufferedWriter bufw = null;
		try {
			String sdPath = getSDCardPath();
			if (TextUtils.isEmpty(sdPath))
				return null;
			createFolder(sdPath, dir);    //根据传入的路径创建目录
			file = createFile(sdPath + File.separator + dir, name); //根据传入的文件名创建
			output = new FileWriter(file);
			bufw = new BufferedWriter(output);
			String line;
			while ((line = (input.readLine())) != null) {
				bufw.write(line);
				bufw.newLine();
			}
			bufw.flush();  //清空缓存
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeQuietly(bufw);
		}
		return file;
	}

	public static File createFile(String path, String name) throws IOException {
		File file = new File(path + name);
		file.createNewFile();
		return file;
	}

	public static boolean createFile(String path) throws IOException {
		File file = new File(path);
		return file.exists() ? true : file.createNewFile();
	}

	public static boolean createFile(File file) throws IOException {
		return file.exists() ? true : file.createNewFile();
	}

	/**
	 * 根据文件路径获取文件
	 *
	 * @param filePath 文件路径
	 * @return 文件
	 */
	public static File getFileByPath(String filePath) {
		return TextUtils.isEmpty(filePath) ? null : new File(filePath);
	}

	/**
	 * 清除自定义目录下的文件
	 *
	 * @param dir 目录
	 * @return {@code true}: 清除成功<br>{@code false}: 清除失败
	 */
	public static boolean cleanCustomCache(File dir) {
		return deleteFilesInDir(dir);
	}

	/**
	 * 清除自定义目录下的文件
	 *
	 * @param dirPath 目录路径
	 * @return {@code true}: 清除成功<br>{@code false}: 清除失败
	 */
	public static boolean cleanCustomCache(String dirPath) {
		return deleteFilesInDir(dirPath);
	}

	/**
	 * 清除外部缓存
	 * <p>/storage/emulated/0/android/data/com.xxx.xxx/cache</p>
	 *
	 * @return {@code true}: 清除成功<br>{@code false}: 清除失败
	 */
	public static boolean cleanExternalCache(Context context) {
		return sdcardIsReadyForWrite() && deleteFilesInDir(context.getExternalCacheDir());
	}

	/**
	 * 清除内部文件
	 * <p>/data/data/com.xxx.xxx/files</p>
	 *
	 * @return {@code true}: 清除成功<br>{@code false}: 清除失败
	 */
	public static boolean cleanInternalFiles(Context context) {
		return deleteFilesInDir(context.getFilesDir());
	}

	/**
	 * 清除内部SP
	 * <p>/data/data/com.xxx.xxx/shared_prefs</p>
	 *
	 * @return {@code true}: 清除成功<br>{@code false}: 清除失败
	 */
	public static boolean cleanInternalSP(Context context) {
		return deleteFilesInDir(context.getFilesDir().getParent() + File.separator + "shared_prefs");
	}

	/**
	 * 清除内部数据库
	 * <p>/data/data/com.xxx.xxx/databases</p>
	 *
	 * @return {@code true}: 清除成功<br>{@code false}: 清除失败
	 */
	public static boolean cleanInternalDbs(Context context) {
		return deleteFilesInDir(context.getFilesDir().getParent() + File.separator + "databases");
	}

	/**
	 * 删除目录下的所有文件
	 *
	 * @param dirPath 目录路径
	 * @return {@code true}: 删除成功<br>{@code false}: 删除失败
	 */
	public static boolean deleteFilesInDir(String dirPath) {
		return deleteFilesInDir(getFileByPath(dirPath));
	}

	/**
	 * 清除内部缓存
	 * <p>/data/data/com.xxx.xxx/cache</p>
	 *
	 * @return {@code true}: 清除成功<br>{@code false}: 清除失败
	 */
	public static boolean cleanInternalCache(Context context) {
		return deleteFilesInDir(context.getCacheDir());
	}

	/**
	 * 删除目录下的所有文件
	 *
	 * @param dir 目录
	 * @return {@code true}: 删除成功<br>{@code false}: 删除失败
	 */
	public static boolean deleteFilesInDir(File dir) {
		if (dir == null) return false;
		// 目录不存在返回true
		if (!dir.exists())
			return true;
		// 不是目录返回false
		if (!dir.isDirectory())
			return false;
		// 现在文件存在且是文件夹
		File[] files = dir.listFiles();
		if (files != null && files.length != 0) {
			for (File file : files) {
				if (file.isFile()) {
					if (!deleteFile(file))
						return false;
				} else if (file.isDirectory()) {
					if (!deleteDir(file))
						return false;
				}
			}
		}
		return true;
	}

	/**
	 * 删除目录
	 *
	 * @param dir 目录
	 * @return {@code true}: 删除成功<br>{@code false}: 删除失败
	 */
	public static boolean deleteDir(File dir) {
		if (dir == null)
			return false;
		// 目录不存在返回true
		if (!dir.exists())
			return true;
		// 不是目录返回false
		if (!dir.isDirectory())
			return false;
		// 现在文件存在且是文件夹
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				if (!deleteFile(file))
					return false;
			} else if (file.isDirectory()) {
				if (!deleteDir(file))
					return false;
			}
		}
		return dir.delete();
	}

	/**
	 * 判断指定路径文件是否存在
	 *
	 * @param path
	 * @param name
	 * @return
	 */
	public static boolean isFileExist(String path, String name) {
		File file = new File(path + name);
		return isFileExist(file);
	}

	/**
	 * 判断文件是否存在
	 *
	 * @param file
	 * @return
	 */
	public static boolean isFileExist(File file) {
		return file == null ? false : file.exists();
	}

	/**
	 * 创建指定路径文件夹
	 *
	 * @param path
	 * @param dir
	 * @return
	 */
	public static boolean createFolder(String path, String dir) {
		return createFolder(path + File.separator + dir);
	}

	/**
	 * 创建指定路径文件夹
	 *
	 * @param path
	 * @return
	 */
	public static boolean createFolder(String path) {
		File file = new File(path);
		return createFolder(file);
	}

	/**
	 * 创建文件夹
	 *
	 * @param file
	 * @return
	 */
	public static boolean createFolder(File file) {
		return file.exists() ? true : file.mkdirs();
	}

	/**
	 * Get the specified folder
	 *
	 * @param dir
	 * @return
	 * @throws Exception
	 */
	private static long getDirSize(File dir) throws Exception {
		if (dir == null || !dir.isDirectory())
			return getFileSize(dir);
		long size = 0;
		File files[] = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				size += getDirSize(file);  //递归调用继续统计
			} else {
				size += getFileSize(file);
			}
		}
		return size;
	}

	/**
	 * 复制文件到指定文件
	 *
	 * @param fromPath 源文件
	 * @param toPath   复制到的文件
	 * @return true 成功，false 失败
	 */
	public static boolean copy(String fromPath, String toPath) {
		boolean result = false;
		File from = new File(fromPath);
		if (!from.exists()) {
			return false;
		}

		File toFile = new File(toPath);
		deleteAll(toFile);
		File toDir = toFile.getParentFile();
		if (createFolder(toDir)) {
			FileInputStream in = null;
			FileOutputStream out = null;
			try {
				in = new FileInputStream(from);
				out = new FileOutputStream(toFile);
				copy(in, out);
				result = true;
			} catch (IOException ex) {
				ex.printStackTrace();
				result = false;
			} finally {
				closeQuietly(in);
				closeQuietly(out);
			}
		}
		return result;
	}

	public static void copy(InputStream in, OutputStream out) throws IOException {
		if (!(in instanceof BufferedInputStream)) {
			in = new BufferedInputStream(in);
		}
		if (!(out instanceof BufferedOutputStream)) {
			out = new BufferedOutputStream(out);
		}
		int len;
		byte[] buffer = new byte[1024];
		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
		out.flush();
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}

	public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
		Cursor cursor = null;
		String column = MediaStore.Images.Media.DATA;
		String[] projection = {column};
		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	public static void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 关闭IO
	 *
	 * @param closeables closeable
	 */
	public static void closeIO(Closeable... closeables) {
		if (closeables == null)
			return;
		try {
			for (Closeable closeable : closeables) {
				if (closeable != null) {
					closeable.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
