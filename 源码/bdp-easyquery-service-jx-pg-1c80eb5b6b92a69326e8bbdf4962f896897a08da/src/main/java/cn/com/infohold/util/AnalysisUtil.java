package cn.com.infohold.util;

import cn.com.infohold.basic.util.json.BasicJsonUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AnalysisUtil {

	private static final BasicJsonUtil BJU = BasicJsonUtil.getInstance();

	public static <T> T toQueryBean(String json, Class<T> class1) {
		T bean = null;
		try {
			bean = class1.newInstance();
			bean = BasicJsonUtil.getInstance().toJavaBean(json, class1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	};

}
