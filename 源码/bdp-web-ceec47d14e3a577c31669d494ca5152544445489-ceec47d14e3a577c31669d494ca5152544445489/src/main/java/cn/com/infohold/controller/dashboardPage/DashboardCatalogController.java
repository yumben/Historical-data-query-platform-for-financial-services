package cn.com.infohold.controller.dashboardPage;

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
import cn.com.infohold.controller.easytarget.EasyTargetCatalogController;
import cn.com.infohold.service.IExportFileService;
import cn.com.infohold.service.IServiceUrlService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/dashboardCatalog")
public class DashboardCatalogController {

	@Autowired
	IServiceUrlService serviceUrlServiceImpl;
	@Autowired
	IExportFileService exportFileServiceImpl;
	String SERVICE_NAME = "bdp-easychart-service";
	
	@RequestMapping(value = "/add")
	public JSONObject add(HttpServletRequest request) throws IOException {
		JSONObject jsonObject = new JSONObject();
		try {
			String param = request.getParameter("param");
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("param", param);
			String result = serviceUrlServiceImpl.post(SERVICE_NAME, "/dashboardCatalog/add", multiValueMap);
			jsonObject = JSONObject.parseObject(result);
		} catch (Exception e) {
			jsonObject.put("code", "-1");
			jsonObject.put("msg", e.getMessage());
			log.error(e);
		}

		return jsonObject;
	}
	
	@RequestMapping(value = "/edit")
	public JSONObject edit(HttpServletRequest request) throws IOException {
		JSONObject jsonObject = new JSONObject();
		try {
			String param = request.getParameter("param");
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("param", param);
			String result = serviceUrlServiceImpl.post(SERVICE_NAME, "/dashboardCatalog/edit", multiValueMap);
			jsonObject = JSONObject.parseObject(result);
		} catch (Exception e) {
			jsonObject.put("code", "-1");
			jsonObject.put("msg", e.getMessage());
			log.error(e);
		}
		return jsonObject;
	}
	
	@RequestMapping(value = "/delete")
	public JSONObject delete(HttpServletRequest request) throws IOException {
		JSONObject jsonObject = new JSONObject();
		try {
			String dashboard_catalog_id = request.getParameter("dashboard_catalog_id");
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("dashboard_catalog_id", dashboard_catalog_id);
			String result = serviceUrlServiceImpl.post(SERVICE_NAME, "dashboardCatalog/del", multiValueMap);
			jsonObject = JSONObject.parseObject(result);
		} catch (Exception e) {
			jsonObject.put("code", "-1");
			jsonObject.put("msg", e.getMessage());
			log.error(e);
		}
		return jsonObject;
	}
	
	@RequestMapping(value = "/select")
	public JSONObject select(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			QueryBean queryParams = new QueryBean();
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("param", BasicJsonUtil.getInstance().toJsonString(queryParams));
			String result = serviceUrlServiceImpl.post(SERVICE_NAME, "/dashboardCatalog/select", multiValueMap);
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
	
	@RequestMapping(value = "/get")
	public JSONObject get(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			String id = request.getParameter("id");
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("dashboard_catalog_id", id);
			String result = serviceUrlServiceImpl.post(SERVICE_NAME, "/dashboardCatalog/get", multiValueMap);
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
