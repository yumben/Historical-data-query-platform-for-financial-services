package cn.com.infohold.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtil {
	/**
	 * 将对象转换为Long类型返回，如果没有记录则返回0
	 * @param obj
	 * @return
	 */
	public static  long getLongFromObject(Object obj) {
		long oid = 0;
		if (obj == null) {
			return oid;
		}
		if (obj instanceof Number) {
			oid = ((Number) obj).longValue();
		}
		return oid;
	}
	
	public static int getTotalPage(int totalNo,int pageSize) {
		int totalPage = totalNo / pageSize;
		if (totalNo % pageSize != 0) {
			totalPage += 1;
		}
		return totalPage;
	}
	
	/**
	 * 
	 * @Title: bytesToHexString 
	 * @Description: TODO(把字节数组转换成16进制字符串) 
	 * @param bArray 字节数组
	 * @return String
	 * @throws
	 */
	public static final String bytesToHexString(byte[] bArray) {
	    StringBuffer sb = new StringBuffer(bArray.length);
	    String sTemp;
	    for (int i = 0; i < bArray.length; i++) {
	      sTemp = Integer.toHexString(0xFF & bArray[i]);
	      if (sTemp.length() < 2){
	        sb.append(0);
	      }
	      sb.append(sTemp.toUpperCase());
	    }
	    return sb.toString();
	}
	
	/**
	 * 判断字符串式否为纯数字或者数字开头的字符串，true-是，false-否
	 */
	public static final boolean checkStrIsNumber(String str){
		if (str != null) {
			Pattern pattern = Pattern.compile("[0-9]*");
			Matcher isNum = pattern.matcher(str.charAt(0)+"");
			if (isNum.matches()) {
				return true;
			}else {
				pattern = Pattern.compile("[0-9]{1,}");
				isNum = pattern.matcher((CharSequence)str);
				if (isNum.matches()) {
					return true;
				}
				return false;
			}
		}else {
			return false;
		}
		
		
	}
}
