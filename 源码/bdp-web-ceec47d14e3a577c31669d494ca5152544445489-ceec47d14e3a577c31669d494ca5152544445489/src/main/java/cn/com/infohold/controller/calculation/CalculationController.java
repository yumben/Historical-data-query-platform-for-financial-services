package cn.com.infohold.controller.calculation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import bdp.commons.calculation.entity.Calculation;
import bdp.commons.dataservice.param.QueryBean;
import bdp.commons.dataservice.param.QueryBeanCondition;
import bdp.commons.dataservice.ret.RetBean;
import bdp.commons.easychart.param.EasyChartParamBean;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.service.IServiceUrlService;
import cn.com.infohold.tools.util.StringUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/calculation")
public class CalculationController {
	@Autowired
	IServiceUrlService serviceUrlServiceImpl;
	String SERVICE_NAME = "bdp-easytarget-service";

	@RequestMapping(value = "/select")
	public JSONObject select(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String curPage = request.getParameter("curPage");
		String pageSize = request.getParameter("pageSize");
		String calculation_code = request.getParameter("calculation_code");
		String calculation_name = request.getParameter("calculation_name");
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
			if (!StringUtil.isEmpty(calculation_code)) {
				QueryBeanCondition chartConditions = new QueryBeanCondition();
				chartConditions.setField1("calculation_code");
				chartConditions.setCond("=");
				chartConditions.setValue1(calculation_code);
				conditions.add(chartConditions);
			}
			if (!StringUtil.isEmpty(calculation_name)) {
				QueryBeanCondition chartConditions = new QueryBeanCondition();
				chartConditions.setField1("calculation_name");
				chartConditions.setCond(" like ");
				chartConditions.setValue1("%" + calculation_name + "%");
				conditions.add(chartConditions);
			}
			queryParams.setConditions(conditions);
			queryParams.setIf_count(true);
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("param", BasicJsonUtil.getInstance().toJsonString(queryParams));
			String result = serviceUrlServiceImpl.post(SERVICE_NAME, "/calculation/select", multiValueMap);
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

	@RequestMapping(value = "/get")
	public JSONObject get(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String param = request.getParameter("id");

		try {
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("id", param);
			String result = serviceUrlServiceImpl.post(SERVICE_NAME, "/calculation/get", multiValueMap);
			jsonObject = JSONObject.parseObject(result);
			log.debug("jsonObject={}", jsonObject.toJSONString());
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
			Calculation easyChart = BasicJsonUtil.getInstance().toJavaBean(param, Calculation.class);
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("param", BasicJsonUtil.getInstance().toJsonString(easyChart));
			String result = serviceUrlServiceImpl.post(SERVICE_NAME, "/calculation/add", multiValueMap);
			jsonObject = JSONObject.parseObject(result);
		} catch (Exception e) {
			jsonObject.put("code", "-1");
			jsonObject.put("msg", e.getMessage());
			log.error(e);
		}

		return jsonObject;
	}

	@RequestMapping(value = "/edit", produces = "text/html;charset=UTF-8")
	public JSONObject edit(HttpServletRequest request) throws IOException {
		JSONObject jsonObject = new JSONObject();
		try {
			String param = request.getParameter("param");
			Calculation easyChart = BasicJsonUtil.getInstance().toJavaBean(param, Calculation.class);
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("param", BasicJsonUtil.getInstance().toJsonString(easyChart));
			String result = serviceUrlServiceImpl.post(SERVICE_NAME, "/calculation/edit", multiValueMap);
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
			multiValueMap.put("calculation_id", id);
			String result = serviceUrlServiceImpl.post(SERVICE_NAME, "/calculation/del", multiValueMap);
			jsonObject = JSONObject.parseObject(result);
		} catch (Exception e) {
			jsonObject.put("code", "-1");
			jsonObject.put("msg", e.getMessage());
			log.error(e);
		}
		return jsonObject;
	}

	@RequestMapping(value = "/selectList")
	public JSONObject selectList(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String param = request.getParameter("param");

		try {

			EasyChartParamBean easyChart = BasicJsonUtil.getInstance().toJavaBean(param, EasyChartParamBean.class);
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("param", BasicJsonUtil.getInstance().toJsonString(easyChart));
			String result = serviceUrlServiceImpl.post(SERVICE_NAME, "/calculation/selectList", multiValueMap);
			jsonObject = JSONObject.parseObject(result);
			log.debug("jsonObject={}", jsonObject.toJSONString());
		} catch (Exception e) {
			jsonObject.put("code", "-1");
			jsonObject.put("msg", e.getMessage());
			log.error(e);
		}

		return jsonObject;
	}
	@RequestMapping(value = "/selectTree")
	public JSONObject selectTree(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			Map<String, String> multiValueMap = new HashMap<String, String>();
			String result = serviceUrlServiceImpl.post(SERVICE_NAME, "calculation/selectTree", multiValueMap);
			jsonObject = JSONObject.parseObject(result);
			log.debug("jsonObject={}", jsonObject.toJSONString());
		} catch (Exception e) {
			jsonObject.put("code", "-1");
			jsonObject.put("msg", e.getMessage());
			log.error(e);
		}

		return jsonObject;
	}
}
