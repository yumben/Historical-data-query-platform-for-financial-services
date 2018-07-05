package cn.com.infohold.controller.easytarget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import bdp.commons.dataservice.param.QueryBean;
import bdp.commons.dataservice.param.QueryBeanCondition;
import bdp.commons.dataservice.ret.RetBean;
import bdp.commons.easytarget.entity.TargetResultObj;
import bdp.commons.easytarget.entity.TargetSourceObj;
import bdp.commons.easytarget.entity.TargetTemplate;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.service.IExportFileService;
import cn.com.infohold.service.IMetadataService;
import cn.com.infohold.service.IServiceUrlService;
import cn.com.infohold.tools.util.StringUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/easyTarget")
public class EasyTargetController {
	@Autowired
	IServiceUrlService serviceUrlServiceImpl;
	@Autowired
	IExportFileService exportFileServiceImpl;
	@Autowired
	IMetadataService metadataServiceImpl;

	String SERVICE_NAME = "bdp-easytarget-service";

	@RequestMapping(value = "/select")
	public JSONObject select(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String curPage = request.getParameter("curPage");
		String pageSize = request.getParameter("pageSize");
		String target_code = request.getParameter("target_code");
		String target_name = request.getParameter("target_name");

		try {
			QueryBean queryParams = new QueryBean();
			if (curPage != null) {
				queryParams.setSkip((Integer.valueOf(curPage) - 1) * Integer.valueOf(pageSize));
			} else {
				queryParams.setSkip(0);
			}
			if (pageSize != null) {
				queryParams.setLimit(Integer.valueOf(pageSize));
			} else {
				queryParams.setLimit(10);
			}
			List<QueryBeanCondition> conditions = new ArrayList<QueryBeanCondition>();
			if (!StringUtil.isEmpty(target_code)) {
				QueryBeanCondition chartConditions = new QueryBeanCondition();
				chartConditions.setField1("target_code");
				chartConditions.setCond(" like ");
				chartConditions.setValue1("%" + target_code + "%");
				conditions.add(chartConditions);
			}
			if (!StringUtil.isEmpty(target_name)) {
				QueryBeanCondition chartConditions = new QueryBeanCondition();
				chartConditions.setField1("target_name");
				chartConditions.setCond(" like ");
				chartConditions.setValue1("%" + target_name + "%");
				conditions.add(chartConditions);
			}
			queryParams.setConditions(conditions);
			queryParams.setIf_count(true);
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("param", BasicJsonUtil.getInstance().toJsonString(queryParams));
			String result = serviceUrlServiceImpl.post(SERVICE_NAME, "/easyTarget/select", multiValueMap);
			RetBean retBean = BasicJsonUtil.getInstance().toJavaBean(result, RetBean.class);
			jsonObject.put("code", retBean.getRet_code());
			jsonObject.put("msg", retBean.getRet_message());
			jsonObject.put("curPage", Integer.valueOf(curPage));
			jsonObject.put("pageSize", Integer.valueOf(pageSize));
			jsonObject.put("totalCout", retBean.getCount());
			if (retBean.getCount() != null) {
				Integer totalPage = retBean.getCount() / queryParams.getLimit();
				if (retBean.getCount() % queryParams.getLimit() == 0) {
					jsonObject.put("totalPage", totalPage);
				} else {
					jsonObject.put("totalPage", totalPage + 1);
				}
			}
			jsonObject.put("list", retBean.getResults());
			log.debug("jsonObject={}", jsonObject.toJSONString());
		} catch (Exception e) {
			jsonObject.put("code", "-1");
			jsonObject.put("msg", e.getMessage());
			log.error(e);
		}

		return jsonObject;
	}

	@RequestMapping(value = "/selectObj")
	public JSONObject selectObj(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String param = request.getParameter("id");

		try {
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("id", param);
			String result = serviceUrlServiceImpl.post(SERVICE_NAME, "/easyTarget/get", multiValueMap);
			jsonObject = JSONObject.parseObject(result);
			log.debug("jsonObject={}", jsonObject.toJSONString());
		} catch (Exception e) {
			jsonObject.put("code", "-1");
			jsonObject.put("msg", e.getMessage());
			log.error(e);
		}

		return jsonObject;
	}

	@RequestMapping(value = "/selectDataObj")
	public JSONObject selectDataObj(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String param = request.getParameter("id");
		try {
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("id", param);
			String result = serviceUrlServiceImpl.post(SERVICE_NAME, "/easyTarget/get", multiValueMap);
			TargetTemplate targetTemplate = BasicJsonUtil.getInstance().toJavaBean(result, TargetTemplate.class);
			List<TargetResultObj> targetResultObjs = targetTemplate.getTargetResultObj();
			List<TargetSourceObj> targetSourceObjs = targetTemplate.getTargetSourceObj();
			JSONArray list = new JSONArray();
			for (TargetResultObj obj : targetResultObjs) {
				JSONObject tempJsonObject = metadataServiceImpl.selectMetadataByIds(obj.getMetadata_id());
				tempJsonObject.put("object_type", "out");
				list.add(tempJsonObject);
			}
			for (TargetSourceObj obj : targetSourceObjs) {
				JSONObject tempJsonObject = metadataServiceImpl.selectMetadataByIds(obj.getMetadata_id());
				tempJsonObject.put("object_type", "in");
				list.add(tempJsonObject);
			}
			jsonObject.put("list", list);
			return jsonObject;
		} catch (Exception e) {
			jsonObject.put("code", "-1");
			jsonObject.put("msg", e.getMessage());
			log.error(e);
		}
		return jsonObject;
	}

	@RequestMapping(value = "/add")
	public JSONObject add(HttpServletRequest request) throws IOException {
		JSONObject jsonObject = new JSONObject();
		try {
			String param = request.getParameter("param");
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("param", param);
			String result = serviceUrlServiceImpl.post(SERVICE_NAME, "/easyTarget/add", multiValueMap);
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
			String result = serviceUrlServiceImpl.post(SERVICE_NAME, "/easyTarget/edit", multiValueMap);
			jsonObject = JSONObject.parseObject(result);
		} catch (Exception e) {
			jsonObject.put("code", "-1");
			jsonObject.put("msg", e.getMessage());
			log.error(e);
		}
		return jsonObject;
	}

	@RequestMapping(value = "/delete", produces = "text/html;charset=UTF-8")
	public JSONObject delete(HttpServletRequest request) throws IOException {
		JSONObject jsonObject = new JSONObject();
		try {
			String id = request.getParameter("id");
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("id", id);
			String result = serviceUrlServiceImpl.post(SERVICE_NAME, "/easyTarget/del", multiValueMap);
			jsonObject = JSONObject.parseObject(result);
		} catch (Exception e) {
			jsonObject.put("code", "-1");
			jsonObject.put("msg", e.getMessage());
			log.error(e);
		}
		return jsonObject;
	}

	@RequestMapping(value = "/selectTemplateList")
	public JSONObject selectTemplateList(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("curPage", "1");
			multiValueMap.put("pageSize", "999999");
			String result = serviceUrlServiceImpl.post(SERVICE_NAME, "/easyTarget/selectList", multiValueMap);
			jsonObject = JSONObject.parseObject(result);
			log.debug("jsonObject={}", jsonObject.toJSONString());
		} catch (Exception e) {
			jsonObject.put("code", "-1");
			jsonObject.put("msg", e.getMessage());
			log.error(e);
		}

		return jsonObject;
	}

	@RequestMapping(value = "/execTarget")
	public JSONObject execTarget(@RequestParam String id) throws Exception {
		JSONObject jsonObject = new JSONObject();
		try {
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("id", id);
			String result = serviceUrlServiceImpl.post(SERVICE_NAME, "/easyTarget/execTarget", multiValueMap);
			jsonObject = JSONObject.parseObject(result);
		} catch (Exception e) {
			jsonObject.put("code", "-1");
			jsonObject.put("msg", e.getMessage());
			log.error(e);
		}
		return jsonObject;
	}
}
