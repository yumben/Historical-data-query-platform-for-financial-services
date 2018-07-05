package cn.com.infohold.controller.authoperation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import bdp.commons.dataservice.param.QueryBean;
import bdp.commons.dataservice.ret.RetBean;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.service.IExportFileService;
import cn.com.infohold.service.IServiceUrlService;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequestMapping("/authOperation")
public class AuthOperationController {

	@Autowired
	IServiceUrlService serviceUrlServiceImpl;
	@Autowired
	IExportFileService exportFileServiceImpl;
	String SERVICE_NAME = "bdp-authorization-service";
	
	@RequestMapping(value = "/addAuthOperation")
	public JSONObject addAuthOperation(HttpServletRequest request) throws IOException {
		JSONObject jsonObject = new JSONObject();
		try {
			String param = request.getParameter("param");
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("param", param);
			String result = serviceUrlServiceImpl.post(SERVICE_NAME, "/authOperation/addAuthOperation", multiValueMap);
			jsonObject = JSONObject.parseObject(result);
		} catch (Exception e) {
			jsonObject.put("code", "-1");
			jsonObject.put("msg", e.getMessage());
			log.error(e);
		}

		return jsonObject;
	}
	
	@RequestMapping(value = "/editAuthOperation", produces = "text/html;charset=UTF-8")
	public JSONObject editAuthOperation(HttpServletRequest request) throws IOException {
		JSONObject jsonObject = new JSONObject();
		try {
			String param = request.getParameter("param");
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("param", param);
			String result = serviceUrlServiceImpl.post(SERVICE_NAME, "/authOperation/editAuthOperation", multiValueMap);
			jsonObject = JSONObject.parseObject(result);
		} catch (Exception e) {
			jsonObject.put("code", "-1");
			jsonObject.put("msg", e.getMessage());
			log.error(e);
		}
		return jsonObject;
	}
	
	@RequestMapping(value = "/delAuthOperation", produces = "text/html;charset=UTF-8")
	public JSONObject delAuthOperation(HttpServletRequest request) throws IOException {
		JSONObject jsonObject = new JSONObject();
		try {
			String id = request.getParameter("id");
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("id", id);
			String result = serviceUrlServiceImpl.post(SERVICE_NAME, "/authOperation/delAuthOperation", multiValueMap);
			jsonObject = JSONObject.parseObject(result);
		} catch (Exception e) {
			jsonObject.put("code", "-1");
			jsonObject.put("msg", e.getMessage());
			log.error(e);
		}
		return jsonObject;
	}
	
	@RequestMapping(value = "/selectAuthOperation")
	public JSONObject selectAuthOperation(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			QueryBean queryParams = new QueryBean();
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("param", BasicJsonUtil.getInstance().toJsonString(queryParams));
			String result = serviceUrlServiceImpl.post(SERVICE_NAME, "/authOperation/selectAuthOperation", multiValueMap);
			RetBean retBean = BasicJsonUtil.getInstance().toJavaBean(result, RetBean.class);
			jsonObject.put("code", retBean.getRet_code());
			jsonObject.put("msg", retBean.getRet_message());
			jsonObject.put("list", retBean.getResults());
			log.debug("jsonObject={}", jsonObject.toJSONString());
		} catch (Exception e) {
			jsonObject.put("code", "-1");
			jsonObject.put("msg", e.getMessage());
			log.error(e);
		}

		return jsonObject;
	}
	
	@RequestMapping(value = "/getAuthOperation")
	public JSONObject getAuthOperation(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			String id = request.getParameter("id");
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("chart_catalog_id", id);
			String result = serviceUrlServiceImpl.post(SERVICE_NAME, "/authOperation/getAuthOperation", multiValueMap);
			RetBean retBean = BasicJsonUtil.getInstance().toJavaBean(result, RetBean.class);
			jsonObject.put("code", retBean.getRet_code());
			jsonObject.put("msg", retBean.getRet_message());
			jsonObject.put("list", retBean.getResults());
			log.debug("jsonObject={}", jsonObject.toJSONString());
		} catch (Exception e) {
			jsonObject.put("code", "-1");
			jsonObject.put("msg", e.getMessage());
			log.error(e);
		}

		return jsonObject;
	}
}
