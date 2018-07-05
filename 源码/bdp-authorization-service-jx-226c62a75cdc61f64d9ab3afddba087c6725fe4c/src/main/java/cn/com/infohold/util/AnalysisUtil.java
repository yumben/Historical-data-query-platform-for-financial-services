package cn.com.infohold.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import bdp.commons.dataservice.param.QueryBean;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AnalysisUtil {

	private static final BasicJsonUtil BJU = BasicJsonUtil.getInstance();

	public static <T> T toJavaBean(String json, Class<T> class1) {
		T bean = null;
		try {
			bean = class1.newInstance();
			bean = BJU.toJavaBean(json, class1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	};

	public static Map<String, Object> AnalysisObject(Object object)
			throws IllegalArgumentException, IllegalAccessException {
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);// 设置可以访问私有属性
			String fieldType = field.getType().getName();
			Object value = field.get(object);
			if ("java.lang.String".equals(fieldType) && null != value) {// 反射取到模版的数据
				map.put(field.getName(), value);
			}
		}

		return map;
	};

	
}
