package cn.com.infohold.controller.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.infohold.service.IServiceUrlService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/authorization")
public class AuthorizationController {

	@Autowired
	IServiceUrlService serviceUrlServiceImpl;

	@RequestMapping(value = "/resourceQuery", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String resourceQuery(@RequestParam String param) throws Exception {
		JSONObject resultJsonObject = new JSONObject();
		log.debug(param);
		Map<String, String> multiValueMap = new HashMap<String, String>();
		multiValueMap.put("param", param);
		String result = serviceUrlServiceImpl.post("bdp-authorization-service", "getResourceByJson", multiValueMap);
		resultJsonObject = JSONObject.parseObject(result);
		log.debug(resultJsonObject);
		return resultJsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/authAdd", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String authAdd(@RequestParam String param) throws Exception {
		JSONObject resultJsonObject = new JSONObject();
		log.debug(param);
		Map<String, String> multiValueMap = new HashMap<String, String>();
		multiValueMap.put("param", param);
		String result = serviceUrlServiceImpl.post("bdp-authorization-service", "authAdd", multiValueMap);
		resultJsonObject = JSONObject.parseObject(result);
		log.debug(resultJsonObject);
		return resultJsonObject.toJSONString();
	}
	@RequestMapping(value = "/authUpdate", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String authUpdate(@RequestParam String param) throws Exception {
		JSONObject resultJsonObject = new JSONObject();
		log.debug(param);
		Map<String, String> multiValueMap = new HashMap<String, String>();
		multiValueMap.put("param", param);
		String result = serviceUrlServiceImpl.post("bdp-authorization-service", "authUpdate", multiValueMap);
		resultJsonObject = JSONObject.parseObject(result);
		log.debug(resultJsonObject);
		return resultJsonObject.toJSONString();
	}
	@RequestMapping(value = "/authDel", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String authDel(@RequestParam String param) throws Exception {
		JSONObject resultJsonObject = new JSONObject();
		log.debug(param);
		Map<String, String> multiValueMap = new HashMap<String, String>();
		multiValueMap.put("param", param);
		String result = serviceUrlServiceImpl.post("bdp-authorization-service", "authDel", multiValueMap);
		resultJsonObject = JSONObject.parseObject(result);
		log.debug(resultJsonObject);
		return resultJsonObject.toJSONString();
	}
	@RequestMapping(value = "/authQuery", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String authQuery(@RequestParam String privilege_master_id) throws Exception {
		JSONObject resultJsonObject = new JSONObject();
		log.debug("用户或者角色ID="+privilege_master_id);
		Map<String, String> multiValueMap = new HashMap<String, String>();
		multiValueMap.put("privilege_master_id", privilege_master_id);
		String result = serviceUrlServiceImpl.post("bdp-authorization-service", "authQuery", multiValueMap);
		resultJsonObject = JSONObject.parseObject(result);
		log.debug(resultJsonObject);
		return resultJsonObject.toJSONString();
	}
	
}
