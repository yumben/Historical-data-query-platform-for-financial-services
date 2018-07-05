package cn.com.infohold.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import cn.com.infohold.service.IServiceUrlService;

@RestController
@RequestMapping("/metadataController")
public class MetadataControll  {
	@Autowired
	IServiceUrlService serviceUrlServiceImpl;

	@RequestMapping(value = "/metadata")
	@ResponseBody
	public JSONObject execute(HttpServletRequest request) {
		Map<String, String> multiValueMap = new HashMap<String, String>();
		String param = request.getParameter("param");
		String pageIndex = request.getParameter("pageIndex");
		String pageSize = request.getParameter("pageSize");
		JSONObject jsonObject2 = new JSONObject();
		String menuUrl = request.getParameter("menuUrl");
		JSONObject jsonObject = new JSONObject();
		if (param != null) {
			jsonObject = (JSONObject) JSON.parse(param);
			if (jsonObject == null) {
				jsonObject = new JSONObject();
			}
		}
		jsonObject.put("pageNo", pageIndex);
		jsonObject.put("pageSize", pageSize);
		multiValueMap = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<Map<String, String>>() {
		});

		try {
			String json = serviceUrlServiceImpl.post("bdp-metadata-service", menuUrl, multiValueMap);
			jsonObject2 = new JSONObject();
			jsonObject2 = JSON.parseObject(json);
			System.out.println(json);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonObject2;
	}

}
