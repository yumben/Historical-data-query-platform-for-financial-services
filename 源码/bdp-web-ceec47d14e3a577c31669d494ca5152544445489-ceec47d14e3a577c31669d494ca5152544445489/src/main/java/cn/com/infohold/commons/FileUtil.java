package cn.com.infohold.commons;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.channels.FileChannel;

public class FileUtil {

	/**
	 * 创建文件夹
	 * @param dir
	 */
	public static void makeDir(String dir){
		File newDir = new File(dir);
		if(!newDir.exists()){
			newDir.mkdirs();
		}
	}
	/**
	 * 删除单个文件
	 * @param sPath 被删除文件的路径+文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		Boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}
    
    /**
     * 判断该文件目录下是否存在文件，如果为空，则删除该目录
     * @param sPath
     * @return
     */
	public static boolean deleteFolder(String Path) {
		File file = new File(Path);
		String sPath = file.getParent();
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		if (files.length == 0) {
			dirFile.delete();
			return true;
		}
        return false;
    }
 
    /**
     * 删除目录（文件夹）以及目录下的文件
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
	public static boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		Boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}
	private static String charset="UTF-8";
	/**
	 * 写文件
	 * @param content
	 * @param path
	 * @return
	 */
	public static boolean writeFile(String content,String path){
		boolean flag = true;
		File file = new File(path);
		if(file.exists()){
			file.delete();
		}
		FileOutputStream fos = null;
		OutputStreamWriter osw=null;
		Writer w=null;
		try {
			file.createNewFile();
			fos = new FileOutputStream(file);
		    osw = new OutputStreamWriter(fos,charset);
			w = new BufferedWriter(osw);
			w.write(content);
			w.flush();
		} catch (IOException e) {
			e.printStackTrace();
			flag=false;
		}finally{
			if(w!=null){
				try {
					w.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(osw!=null){
				try {
					osw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}
	/**
	 * 写文件
	 * @param content
	 * @param path
	 * @param charset
	 * @return
	 */
	public static boolean writeFile(String content,String path,String charset){
		boolean flag = true;
		File file = new File(path);
		if(file.exists()){
			file.delete();
		}
		FileOutputStream fos = null;
		OutputStreamWriter osw=null;
		Writer w=null;
		try {
			file.createNewFile();
			fos = new FileOutputStream(file);
		    osw = new OutputStreamWriter(fos,charset);
			w = new BufferedWriter(osw);
			w.write(content);
			w.flush();
		} catch (IOException e) {
			e.printStackTrace();
			flag=false;
		}finally{
			if(w!=null){
				try {
					w.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(osw!=null){
				try {
					osw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}
	
	/**
	 * 复制文件
	 * @param source 源文件
	 * @param target 目标文件
	 */
	public static void copyFile(File source,File target){
		FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(source);
            fo = new FileOutputStream(target);
            in = fi.getChannel();// 得到对应的文件通道
            out = fo.getChannel();// 得到对应的文件通道
            in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {if(fi!=null){fi.close();}} catch (IOException e) {e.printStackTrace();}
            try {if(in!=null){in.close();}} catch (IOException e) {e.printStackTrace();}
            try {if(fo!=null){fo.close();}} catch (IOException e) {e.printStackTrace();}
            try {if(out!=null){out.close();}} catch (IOException e) {e.printStackTrace();}
            
        }
	}
	
	/**
	 * 获取文件大小
	 * @param filePath 文件路径
	 * @return 返回文件大小，单位是byte
	 */
	public static long getFileSize(String filePath){
		FileChannel fc = null;
		FileInputStream fis = null;
		long size = 0;
		try{
			fis = new FileInputStream(new File(filePath));
			fc = fis.getChannel();
			size = fc.size();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try {if(fis!=null){fis.close();}} catch (IOException e) {e.printStackTrace();}
			try {if(fc!=null){fc.close();}} catch (IOException e) {e.printStackTrace();}
		}
		return size;
	}
	
	/**
	 * 读取文件
	 * @param file 
	 * @param bufferSize 缓冲大小
	 * @return
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	public static BufferedReader readerFile(File file,int bufferSize,String charset) throws FileNotFoundException, UnsupportedEncodingException{
		return new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(file)),charset), bufferSize);
	}
	
	/**
	 * 读取文件
	 * @param file 
	 * @param bufferSize 缓冲大小
	 * @return
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	public static BufferedReader readerFile(String file,int bufferSize,String charset) throws FileNotFoundException, UnsupportedEncodingException{
		return new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(file)),charset), bufferSize);
	}
	
	/**
	 * 写加载日志
	 * @param logFile
	 * @param msg
	 */
	public static void appendLog(String logFile,String msg,boolean IsAppend){
		FileWriter fw = null;
		BufferedWriter bw = null ;
		try{
			fw = new FileWriter(logFile, IsAppend);
			bw = new BufferedWriter(fw);
			if(IsAppend){
				fw.append(msg).append("\n");
			}else{
				fw.write(msg);
			}
		}catch(Exception e){
			
		}finally{
			try{
				if(bw!=null){
					bw.close();
				}
			}catch(Exception e){
				
			}
			try{
				if(fw!=null){
					fw.close();
				}
			}catch(Exception e){
				
			}
		}
	}
}
