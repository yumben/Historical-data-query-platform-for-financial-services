package cn.com.infohold.controller.dashboardPage;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import bdp.commons.dataservice.ret.RetBean;
import bdp.commons.easyquery.param.QueryParams;
import cn.com.infohold.basic.util.file.PropUtil;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.service.IServiceUrlService;
import lombok.extern.log4j.Log4j2;


@Log4j2(topic = "DashboardPageController")
@RestController
@RequestMapping("/dashboardPage")
public class DashboardPageController  {
	@Autowired
	IServiceUrlService serviceUrlServiceImpl;

	@RequestMapping(value = "/addDashboardPage", produces = "text/html;charset=UTF-8")
	public String addDashboardPage(@RequestParam String param) throws Exception {
		JSONObject resultJsonObject = new JSONObject();
		log.debug(param);
		Map<String, String> multiValueMap = new HashMap<String, String>();
		multiValueMap.put("param", param);
		String result = serviceUrlServiceImpl.post("bdp-easychart-service", "addDashboardPage", multiValueMap);
		resultJsonObject = JSONObject.parseObject(result);
		log.debug(resultJsonObject);
		return resultJsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/editDashboardPage", produces = "text/html;charset=UTF-8")
	public String editDashboardPage(@RequestParam String param) throws Exception {
		JSONObject resultJsonObject = new JSONObject();
		log.debug(param);
		Map<String, String> multiValueMap = new HashMap<String, String>();
		multiValueMap.put("param", param);
		String result = serviceUrlServiceImpl.post("bdp-easychart-service", "editDashboardPage", multiValueMap);
		resultJsonObject = JSONObject.parseObject(result);
		log.debug(resultJsonObject);
		return resultJsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/addDashboardEle", produces = "text/html;charset=UTF-8")
	public String addDashboardEle(@RequestParam String param) throws Exception {
		JSONObject resultJsonObject = new JSONObject();
		log.debug(param);
		Map<String, String> multiValueMap = new HashMap<String, String>();
		multiValueMap.put("param", param);
		String result = serviceUrlServiceImpl.post("bdp-easychart-service", "addDashboardEle", multiValueMap);
		resultJsonObject = JSONObject.parseObject(result);
		log.debug(resultJsonObject);
		return resultJsonObject.toJSONString();
	}

	@RequestMapping(value = "/selectDashboardPage", produces = "text/html;charset=UTF-8")
	public String selectDashboardPage(@RequestParam String dashboard_id, @RequestParam String token) throws Exception {
		JSONObject resultJsonObject = new JSONObject();
		Map<String, String> multiValueMap = new HashMap<String, String>();
		multiValueMap.put("dashboard_id", dashboard_id);
		multiValueMap.put("token", token);
		String result = serviceUrlServiceImpl.post("bdp-easychart-service", "selectDashboardPage", multiValueMap);
		resultJsonObject = JSONObject.parseObject(result);
		log.debug(resultJsonObject);
		return resultJsonObject.toJSONString();
	}

	@RequestMapping(value = "/delDashboardPage", produces = "text/html;charset=UTF-8")
	public String delDashboardPage(@RequestParam String dashboard_id) throws Exception {
		JSONObject resultJsonObject = new JSONObject();
		Map<String, String> multiValueMap = new HashMap<String, String>();
		multiValueMap.put("dashboard_id", dashboard_id);
		//multiValueMap.put("token", token);
		String result = serviceUrlServiceImpl.post("bdp-easychart-service", "delDashboardPage", multiValueMap);
		resultJsonObject = JSONObject.parseObject(result);
		log.debug(resultJsonObject);
		return resultJsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/delDashboardPageEle", produces = "text/html;charset=UTF-8")
	public String delDashboardPageEle(@RequestParam String dashboard_ele_id, @RequestParam String token) throws Exception {
		JSONObject resultJsonObject = new JSONObject();
		Map<String, String> multiValueMap = new HashMap<String, String>();
		multiValueMap.put("dashboard_ele_id", dashboard_ele_id);
		multiValueMap.put("token", token);
		String result = serviceUrlServiceImpl.post("bdp-easychart-service", "delDashboardPageEle", multiValueMap);
		resultJsonObject = JSONObject.parseObject(result);
		log.debug(resultJsonObject);
		return resultJsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/editDashboardPageEle", produces = "text/html;charset=UTF-8")
	public String editDashboardPageEle(@RequestParam String param, @RequestParam String token) throws Exception {
		JSONObject resultJsonObject = new JSONObject();
		Map<String, String> multiValueMap = new HashMap<String, String>();
		multiValueMap.put("param", param);
		multiValueMap.put("token", token);
		String result = serviceUrlServiceImpl.post("bdp-easychart-service", "editDashboardPageEle", multiValueMap);
		resultJsonObject = JSONObject.parseObject(result);
		log.debug(resultJsonObject);
		return resultJsonObject.toJSONString();
	}

	@RequestMapping(value = "/selectAllDashboardPage", produces = "text/html;charset=UTF-8")
	public String selectAllDashboardPage(@RequestParam String token) throws Exception {
		JSONObject resultJsonObject = new JSONObject();
		Map<String, String> multiValueMap = new HashMap<String, String>();
		multiValueMap.put("token", token);
		String result = serviceUrlServiceImpl.post("bdp-easychart-service", "selectAllDashboardPage", multiValueMap);
		resultJsonObject = JSONObject.parseObject(result);
		log.debug(resultJsonObject);
		return resultJsonObject.toJSONString();
	}
	@RequestMapping(value = "/queryDashboardTableAction", produces = "text/html;charset=UTF-8")
	public String queryDashboardTableAction(@RequestParam String modelId,@RequestParam String token) throws Exception {
		JSONObject resultJsonObject = new JSONObject();
		String name = "";
		String value = "";
		if ("1".equals(modelId)) {
			
			name = "用户基本信息";
			value = PropUtil.readJsonFile("json/userInfo.json",false);
			
		}else if("2".equals(modelId)){
			name = "用户信息";
			value = PropUtil.readJsonFile("json/user.json",false);
			
		}else {
		//	value = "70000";
		}
		resultJsonObject.put("name", name);
		resultJsonObject.put("value", value);
		return resultJsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/queryDashboardIndexAction", produces = "text/html;charset=UTF-8")
	public String queryDashboardIndexAction(@RequestParam String modelId,@RequestParam String token) throws Exception {
		JSONObject resultJsonObject = new JSONObject();
		String name = "";
		String value = "";
		if ("1".equals(modelId)) {
			name = "订单数";
			value = "50000";
		}else if("2".equals(modelId)){
			name = "贷款总笔数";
			value = "60000";
		}else {
			name = "账户总数";
			value = "70000";
		}
		resultJsonObject.put("name", name);
		resultJsonObject.put("value", value);
		return resultJsonObject.toJSONString();
	}

	@RequestMapping(value = "/selectDashboardCatalog", produces = "text/html;charset=UTF-8")
	public String selectTemplateTree(HttpServletRequest request) throws Exception {
		String token = request.getParameter("token");
		JSONObject resultJsonObject = new JSONObject();
		Map<String, String> multiValueMap = new HashMap<String, String>();
		QueryParams queryParams = new QueryParams();
		queryParams.setToken(token);

		multiValueMap.put("param", BasicJsonUtil.getInstance().toJsonString(queryParams));
		String result = serviceUrlServiceImpl.post("bdp-easychart-service", "dashboardCatalog/selectCatalog", multiValueMap);
		JSONObject jsonObject = JSONObject.parseObject(result);
		RetBean retBean = BasicJsonUtil.getInstance().toJavaBean(jsonObject.toJSONString(), RetBean.class);
		resultJsonObject.put("ret_code", retBean.getRet_code());
		resultJsonObject.put("ret_message", retBean.getRet_message());
		resultJsonObject.put("pageIndex", 0);
		resultJsonObject.put("pageSize", 9999);
		log.debug("retBean.getObjectList()="+retBean.getObjectList().toString());
		resultJsonObject.put("list", retBean.getObjectList());

		log.debug(resultJsonObject);
		return resultJsonObject.toJSONString();
	}

}
