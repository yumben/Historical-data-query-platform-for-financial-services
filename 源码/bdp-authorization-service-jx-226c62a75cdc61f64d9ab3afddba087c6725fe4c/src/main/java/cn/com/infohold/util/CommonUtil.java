package cn.com.infohold.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import bdp.commons.dataservice.ret.RetBean;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.easybdp.basic.util.http.BasicHttpUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CommonUtil {

	private static final BasicJsonUtil BJU = BasicJsonUtil.getInstance();
	private static final BasicHttpUtil BHU = BasicHttpUtil.getInstance();

	public static RetBean post(String url,JSONObject reqObj) {
		RetBean retBean = new RetBean();
		try {
			Map<String, String> param = new HashMap<>();
			param.put("param", reqObj.toJSONString());
			log.debug("post param = " +param.toString());
			String rets = BHU.postRequst(url, param);
			retBean = BJU.toJavaBean(rets, RetBean.class);
			log.debug("post retBean = " +retBean.toString());
		} catch (IOException e) {
			e.printStackTrace();
			retBean.setRet_code("-1");
			retBean.setRet_message(e.getMessage());
		}

		return retBean;
	}
	
	public static RetBean post(String url,String json) {
		RetBean retBean = new RetBean();
		try {
			Map<String, String> param = new HashMap<>();
			param.put("param", json);
			String rets = BHU.postRequst(url, param);
			retBean = BJU.toJavaBean(rets, RetBean.class);
			log.debug("post retBean = " +retBean.toString());
		} catch (IOException e) {
			e.printStackTrace();
			retBean.setRet_code("-1");
			retBean.setRet_message(e.getMessage());
		}

		return retBean;
	}
	
	public static <T> boolean listIsNotNull(List<T> list) throws IOException {
		boolean flag = false;
		if (null != list && !list.isEmpty()) {
			flag = true;
		}
		return flag;
	}
}
