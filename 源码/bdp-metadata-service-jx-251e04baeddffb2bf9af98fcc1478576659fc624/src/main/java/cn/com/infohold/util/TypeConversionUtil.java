package cn.com.infohold.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.infohold.entity.MetadataProperty;
import lombok.extern.log4j.Log4j2;

/**
 * 类型转换工具类
 * @author fengzhenyuan
 * @since  2017-10-24
 */
@Log4j2
public class TypeConversionUtil {

	/**
	 * JSONArray转String[]
	 * @param jsonArray 需要转换的jsonArray
	 * @return String[]
	 */
	public static String[] JSONArrayToString(String jsonStr){
		if (jsonStr ==null) {
			return null;
		}
		JSONArray jsonArray = JSONObject.parseArray(jsonStr);
		
		String[] str = new String[jsonArray.size()];
		for(int i=0;i<jsonArray.size();i++){
			str[i] = jsonArray.getString(i);
		}
		
		return str;
	}
	
	/**
	 * JSONArray转String[]
	 * @param jsonArray 需要转换的jsonArray
	 * @return String[]
	 */
	public static String[] JSONArrayToString(JSONArray jsonArray){

		if (jsonArray == null) {
			return null;
		}
		String[] str = new String[jsonArray.size()];
		for(int i=0;i<jsonArray.size();i++){
			str[i] = jsonArray.getString(i);
		}
		
		return str;
	}
	
	/**
	 * JSONObject转String[]
	 * @param JSONObject 需要转换的JSONObject
	 * @return String[]
	 */
	/*public static String[] JSONObjectToArr(JSONObject jsonObject){

		if (jsonObject == null) {
			return null;
		}
		String[] str = new String[jsonObject.size()];
		for(int i=0;i<jsonObject.size();i++){
			str[i] = jsonObject.getString(i);
		}
		
		return str;
	}*/
	
	public static void main(String[] args) {
		
		List<MetadataProperty> list = new ArrayList<MetadataProperty>();
		MetadataProperty m1 = new MetadataProperty();
		m1.setMetadataId("1");
		MetadataProperty m2 = new MetadataProperty();
		m2.setMetadataId("2");
		list.add(m1);
		list.add(m2);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("proList", list);
		log.debug("JSONObject.toJSON(list)={}",JSONObject.toJSON(list));
		
		List<MetadataProperty> array = JSON.
				parseArray(JSONObject.toJSON(list).toString(), MetadataProperty.class);
		log.debug("array.size()={}",array.size());
		
	}
	
}
