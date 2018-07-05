package cn.com.infohold.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import bdp.commons.dataservice.ret.RetBean;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.easybdp.basic.util.http.BasicHttpUtil;

public class CommonUtil {

	private static final BasicJsonUtil BJU = BasicJsonUtil.getInstance();
	private static final BasicHttpUtil BHU = BasicHttpUtil.getInstance();
	public static JSONObject retBeanToJsonObject(RetBean retBean) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pageIndex", retBean.getCurPage());
		jsonObject.put("totalPage", retBean.getTotalPage());
		jsonObject.put("pageSize", retBean.getPageSize());
		jsonObject.put("totalCount", retBean.getTotal());
		jsonObject.put("list", retBean.getResults());
		return jsonObject;
	}

	public static <T> List<T> mapsToObjs(List<Map<String, Object>> list, Class<T> clazz) throws IOException {
		List<T> resultList = new ArrayList<T>();
		if (listIsNotNull(list)) {
			for (Map<String, Object> map : list) {
				String json = JSON.toJSONString(map);
				resultList.add(JSON.parseObject(json, clazz));
			}
		}

		return resultList;
	}
	public static <T> boolean listIsNotNull(List<T> list) throws IOException {
		boolean flag = false;
		if (null != list && !list.isEmpty()) {
			flag = true;
		}
		return flag;
	}
	
	public static RetBean post(String url, Object object) {
		RetBean retBean = new RetBean();
		try {
			String values = "";
			values = BJU.toJsonString(object);
			Map<String, String> param = new HashMap<>();
			param.put("param", values);
			String rets = BHU.postRequst(url, param);
			retBean = BJU.toJavaBean(rets, RetBean.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retBean.setRet_code("-1");
			retBean.setRet_message(e.getMessage());
		}

		return retBean;
	}
}
