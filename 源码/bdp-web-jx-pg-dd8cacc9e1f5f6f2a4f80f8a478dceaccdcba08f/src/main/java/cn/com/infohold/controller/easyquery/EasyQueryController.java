package cn.com.infohold.controller.easyquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import bdp.commons.dataservice.ret.RetBean;
import bdp.commons.easyquery.param.QueryParams;
import bdp.commons.easyquery.ret.EasyQuery;
import bdp.commons.easyquery.ret.QueryConditions;
import cn.com.infohold.basic.util.common.DateUtil;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.bean.TreeNodeBean;
import cn.com.infohold.service.IEasyQueryService;
import cn.com.infohold.service.IExportFileService;
import cn.com.infohold.service.IServiceUrlService;
import cn.com.infohold.tools.util.StringUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "EasyQueryController")
@Controller
@RequestMapping("/easyQuery")
public class EasyQueryController {

	@Autowired
	IServiceUrlService serviceUrlServiceImpl;
	@Autowired
	IExportFileService exportFileServiceImpl;
	@Autowired
	IEasyQueryService easyQueryServiceImpl;

	@RequestMapping(value = "/definitionSave", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String definitionSave(@RequestParam String param) throws Exception {
		JSONObject resultJsonObject = new JSONObject();
		log.debug(param);
		Map<String, String> multiValueMap = new HashMap<String, String>();
		multiValueMap.put("param", param);
		String result = serviceUrlServiceImpl.post("bdp-easyquery-service", "definitionSave", multiValueMap);
		resultJsonObject = JSONObject.parseObject(result);
		return resultJsonObject.toJSONString();
	}

	@RequestMapping(value = "/isRepeatQueryCode", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String isRepeatQueryCode(@RequestParam String param) throws Exception {
		JSONObject resultJsonObject = new JSONObject();
		Map<String, String> multiValueMap = new HashMap<String, String>();
		multiValueMap.put("param", param);
		String result = serviceUrlServiceImpl.post("bdp-easyquery-service", "isRepeatQueryCode", multiValueMap);
		resultJsonObject = JSONObject.parseObject(result);
		return resultJsonObject.toJSONString();
	}

	@RequestMapping(value = "/definitionDelete", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String definitionDelete(@RequestParam String id) throws Exception {
		JSONObject resultJsonObject = new JSONObject();
		Map<String, String> multiValueMap = new HashMap<String, String>();
		multiValueMap.put("query_template_id", id);
		String result = serviceUrlServiceImpl.post("bdp-easyquery-service", "definitionDelete", multiValueMap);
		resultJsonObject = JSONObject.parseObject(result);
		return resultJsonObject.toJSONString();
	}

	@RequestMapping(value = "/selectEasyQueryTemplate", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String selectEasyQueryTemplate(@RequestParam String id, @RequestParam String token) throws Exception {
		JSONObject resultJsonObject = new JSONObject();
		Map<String, String> multiValueMap = new HashMap<String, String>();
		multiValueMap.put("query_template_id", id);
		multiValueMap.put("token", token);
		EasyQuery easyQuery = easyQueryServiceImpl.selectEasyQueryTemplate(id, token);
		resultJsonObject = JSONObject.parseObject(BasicJsonUtil.getInstance().toJsonString(easyQuery));// JSONObject.parseObject(result);
		return resultJsonObject.toJSONString();
	}

	@RequestMapping(value = "/definitionGet", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String definitionGet(@RequestParam String id) throws Exception {
		JSONObject resultJsonObject = new JSONObject();
		Map<String, String> multiValueMap = new HashMap<String, String>();
		multiValueMap.put("query_template_id", id);
		String result = serviceUrlServiceImpl.post("bdp-easyquery-service", "definitionGet", multiValueMap);
		resultJsonObject = JSONObject.parseObject(result);
		return resultJsonObject.toJSONString();
	}

	@RequestMapping(value = "/selectTemplate", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String selectTemplate(@RequestParam int pageIndex, @RequestParam int pageSize, HttpServletRequest request)
			throws Exception {
		String idkey = request.getParameter("idkey");
		String token = request.getParameter("token");
		JSONObject resultJsonObject = new JSONObject();
		Map<String, String> multiValueMap = new HashMap<String, String>();
		QueryParams queryParams = new QueryParams();
		queryParams.setToken(token);
		queryParams.setCurPage(pageIndex + 1);
		queryParams.setPageSize(pageSize);
		List<QueryConditions> conditions = new ArrayList<QueryConditions>();
		if (!StringUtil.isEmpty(idkey)) {
			QueryConditions condition = new QueryConditions();
			condition.setField1("query_type");
			condition.setCond("=");
			condition.setValue1(idkey);
			conditions.add(condition);
			queryParams.setConditions(conditions);
		}
		multiValueMap.put("param", BasicJsonUtil.getInstance().toJsonString(queryParams));
		String result = serviceUrlServiceImpl.post("bdp-easyquery-service", "selectTemplate", multiValueMap);
		JSONObject jsonObject = JSONObject.parseObject(result);
		RetBean retBean = BasicJsonUtil.getInstance().toJavaBean(jsonObject.toJSONString(), RetBean.class);
		resultJsonObject.put("pageIndex", pageIndex);
		resultJsonObject.put("pageSize", pageSize);
		resultJsonObject.put("totalCount", retBean.getCount());
		resultJsonObject.put("totalPage", retBean.getTotalPage());
		resultJsonObject.put("list", retBean.getResults());
		return resultJsonObject.toJSONString();
	}

	@RequestMapping(value = "/queryDataList", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String queryDataList(@RequestParam String param, HttpServletRequest request) throws Exception {
		long s = System.currentTimeMillis();
		String token = request.getParameter("token");
		JSONObject resultJsonObject = new JSONObject();
		if (!StringUtil.isEmpty(token)) {
			Map<String, String> multiValueMap = new HashMap<String, String>();
			JSONObject paramJsonObject = JSONObject.parseObject(param);
			paramJsonObject.put("token", token);
			multiValueMap.put("param", paramJsonObject.toJSONString());
			String result = serviceUrlServiceImpl.post("bdp-easyquery-service", "queryDataList", multiValueMap);
			resultJsonObject = JSONObject.parseObject(result);
		}
		long s2 = System.currentTimeMillis();
		log.debug("queryDataList简易查询耗时时间：" + (s2 - s) + "ms ");
		return resultJsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/queryTotalCount", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String queryTotalCount(@RequestParam String param, HttpServletRequest request) throws Exception {
		long s = System.currentTimeMillis();
		String token = request.getParameter("token");
		JSONObject resultJsonObject = new JSONObject();
		if (!StringUtil.isEmpty(token)) {
			Map<String, String> multiValueMap = new HashMap<String, String>();
			JSONObject paramJsonObject = JSONObject.parseObject(param);
			paramJsonObject.put("token", token);
			multiValueMap.put("param", paramJsonObject.toJSONString());
			String result = serviceUrlServiceImpl.post("bdp-easyquery-service", "queryTotalCount", multiValueMap);
			resultJsonObject = JSONObject.parseObject(result);
		}
		long s2 = System.currentTimeMillis();
		log.debug("queryTotalCount简易查询耗时时间：" + (s2 - s) + "ms ");
		return resultJsonObject.toJSONString();
	}

	
	@RequestMapping(value = "/queryTotal", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String queryTotal(@RequestParam String param, HttpServletRequest request) throws Exception {
		long s = System.currentTimeMillis();
		String token = request.getParameter("token");
		JSONObject resultJsonObject = new JSONObject();
		if (!StringUtil.isEmpty(token)) {
			Map<String, String> multiValueMap = new HashMap<String, String>();
			JSONObject paramJsonObject = JSONObject.parseObject(param);
			paramJsonObject.put("token", token);
			multiValueMap.put("param", paramJsonObject.toJSONString());
			String result = serviceUrlServiceImpl.post("bdp-easyquery-service", "queryTotal", multiValueMap);
			resultJsonObject = JSONObject.parseObject(result);
		}
		long s2 = System.currentTimeMillis();
		log.debug("queryTotal简易查询耗时时间：" + (s2 - s) + "ms ");
		return resultJsonObject.toJSONString();
	}


	@RequestMapping(value = "/exportEasyqueryData")
	public void exportEasyqueryData(@RequestParam String param, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String token = request.getParameter("token");
		long s1 = System.currentTimeMillis();
		if (!StringUtil.isEmpty(token)) {
			Map<String, String> multiValueMap = new HashMap<String, String>();
			JSONObject paramJsonObject = JSONObject.parseObject(param);
			paramJsonObject.put("token", token);
			multiValueMap.put("param", paramJsonObject.toJSONString());
			String result = serviceUrlServiceImpl.post("bdp-easyquery-service", "exportEasyqueryData", multiValueMap);
			long s2 = System.currentTimeMillis();
			log.debug("查询耗时 = " + (s2 - s1) + "ms");
			try {
				if (StringUtil.isNotEmpty(result)) {
					String[] retPath = result.split(",");
					long s3 = System.currentTimeMillis();
					response.setHeader("content-type", "application/octet-stream");
					response.setContentType("application/octet-stream;charset=UTF-8");
					response.setHeader("Content-Disposition",
							"attachment;filename=" + new String(retPath[0].getBytes("gbk"), "iso-8859-1"));
					exportFileServiceImpl.exportFile(response,retPath);

					long s4 = System.currentTimeMillis();
					log.debug("导出耗时 = " + (s4 - s3) + "ms");
				}
				
			} catch (Exception e) {
				log.debug(e);
			}
		}
	}

	@RequestMapping(value = "/selectTemplateTree", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String selectTemplateTree(HttpServletRequest request) throws Exception {
		String token = request.getParameter("token");
		JSONObject resultJsonObject = new JSONObject();
		Map<String, String> multiValueMap = new HashMap<String, String>();
		QueryParams queryParams = new QueryParams();
		queryParams.setToken(token);

		multiValueMap.put("param", BasicJsonUtil.getInstance().toJsonString(queryParams));
		String result = serviceUrlServiceImpl.post("bdp-easyquery-service", "selectTemplateTree", multiValueMap);
		JSONObject jsonObject = JSONObject.parseObject(result);
		RetBean retBean = BasicJsonUtil.getInstance().toJavaBean(jsonObject.toJSONString(), RetBean.class);
		resultJsonObject.put("pageIndex", 0);
		resultJsonObject.put("pageSize", 9999);
		List<Map<String, Object>> list = retBean.getResults();
		resultJsonObject.put("list", list);
		return resultJsonObject.toJSONString();
	}

	@RequestMapping(value = "/queryTemplateMenu")
	@ResponseBody
	public JSONObject queryTemplateMenu(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONObject objjson = new JSONObject();
		try {
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("metadataCode", "metaqueryType");
			multiValueMap.put("level", "c");
			String json = serviceUrlServiceImpl.post("bdp-metadata-service", "/metadata/selectMetadataJson",
					multiValueMap);
			objjson = JSONObject.parseObject(json);
			JSONObject jsonObject = objjson.getJSONObject("metadataList");
			List<TreeNodeBean> result = new ArrayList<TreeNodeBean>();
			TreeNodeBean nodeBean = new TreeNodeBean();
			List<TreeNodeBean> list = new ArrayList<TreeNodeBean>();
			nodeBean.setKey(jsonObject.getString("metadataId"));
			nodeBean.setCode(jsonObject.getString("metadataCode"));
			nodeBean.setNodeType(jsonObject.getString("classId"));
			nodeBean.setText(jsonObject.getString("metadataName"));
			JSONArray jsonArray = jsonObject.getJSONArray("children");
			for (Object object : jsonArray) {
				JSONObject obj = (JSONObject) object;
				TreeNodeBean temp = new TreeNodeBean();
				temp.setKey(obj.getString("metadataId"));
				temp.setCode(obj.getString("metadataCode"));
				temp.setNodeType(obj.getString("classId"));
				temp.setText(obj.getString("metadataName"));
				list.add(temp);
			}
			nodeBean.setNodes(list);
			result.add(nodeBean);
			resultJson.put("catalogBeans", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultJson;
	}
}
