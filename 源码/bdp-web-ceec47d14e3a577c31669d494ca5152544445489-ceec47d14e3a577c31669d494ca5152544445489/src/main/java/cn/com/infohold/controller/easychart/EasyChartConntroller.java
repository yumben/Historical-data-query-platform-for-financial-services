package cn.com.infohold.controller.easychart;

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

import bdp.commons.dataservice.param.QueryBean;
import bdp.commons.dataservice.param.QueryBeanCondition;
import bdp.commons.dataservice.ret.RetBean;
import bdp.commons.easychart.param.EasyChartParamBean;
import bdp.commons.easychart.ret.EasyChart;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.service.IExportFileService;
import cn.com.infohold.service.IServiceUrlService;
import cn.com.infohold.tools.util.StringUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/easyChart")
public class EasyChartConntroller {

	@Autowired
	IServiceUrlService serviceUrlServiceImpl;
	@Autowired
	IExportFileService exportFileServiceImpl;

	@RequestMapping(value = "/select")
	public JSONObject select(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String curPage = request.getParameter("curPage");
		String pageSize = request.getParameter("pageSize");
		String chart_code = request.getParameter("chart_code");
		String chart_name = request.getParameter("chart_name");

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
			if (!StringUtil.isEmpty(chart_code)) {
				QueryBeanCondition chartConditions = new QueryBeanCondition();
				chartConditions.setField1("chart_code");
				chartConditions.setCond(" like ");
				chartConditions.setValue1("%" + chart_code + "%");
				conditions.add(chartConditions);
			}
			if (!StringUtil.isEmpty(chart_name)) {
				QueryBeanCondition chartConditions = new QueryBeanCondition();
				chartConditions.setField1("chart_name");
				chartConditions.setCond(" like ");
				chartConditions.setValue1("%" + chart_name + "%");
				conditions.add(chartConditions);
			}
			queryParams.setConditions(conditions);
			queryParams.setIf_count(true);
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("param", BasicJsonUtil.getInstance().toJsonString(queryParams));
			String result = serviceUrlServiceImpl.post("bdp-easychart-service", "select", multiValueMap);
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

	@RequestMapping(value = "/selectTemplateObj")
	public JSONObject selectObj(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String param = request.getParameter("echart_template_id");

		try {
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("echart_template_id", param);
			String result = serviceUrlServiceImpl.post("bdp-easychart-service", "selectEasyChart", multiValueMap);
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
	public JSONObject easyChartAdd(HttpServletRequest request) throws IOException {
		JSONObject jsonObject = new JSONObject();
		try {
			String param = request.getParameter("param");
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("param", param);
			String result = serviceUrlServiceImpl.post("bdp-easychart-service", "addEasyChart", multiValueMap);
			jsonObject = JSONObject.parseObject(result);
		} catch (Exception e) {
			jsonObject.put("code", "-1");
			jsonObject.put("msg", e.getMessage());
			log.error(e);
		}

		return jsonObject;
	}

	@RequestMapping(value = "/edit")
	public JSONObject easyChartEdit(HttpServletRequest request) throws IOException {
		JSONObject jsonObject = new JSONObject();
		try {
			String param = request.getParameter("param");
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("param", param);
			String result = serviceUrlServiceImpl.post("bdp-easychart-service", "editEasyChart", multiValueMap);
			jsonObject = JSONObject.parseObject(result);
		} catch (Exception e) {
			jsonObject.put("code", "-1");
			jsonObject.put("msg", e.getMessage());
			log.error(e);
		}
		return jsonObject;
	}

	@RequestMapping(value = "/delete", produces = "text/html;charset=UTF-8")
	public JSONObject easyChartDel(HttpServletRequest request) throws IOException {
		JSONObject jsonObject = new JSONObject();
		try {
			String id = request.getParameter("id");
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("echart_template_id", id);
			String result = serviceUrlServiceImpl.post("bdp-easychart-service", "delEasyChart", multiValueMap);
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
		String param = request.getParameter("param");

		try {

			EasyChartParamBean easyChart = BasicJsonUtil.getInstance().toJavaBean(param, EasyChartParamBean.class);
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("param", BasicJsonUtil.getInstance().toJsonString(easyChart));
			String result = serviceUrlServiceImpl.post("bdp-easychart-service", "selectChartTemplate", multiValueMap);
			jsonObject = JSONObject.parseObject(result);
			log.debug("jsonObject={}", jsonObject.toJSONString());
		} catch (Exception e) {
			jsonObject.put("code", "-1");
			jsonObject.put("msg", e.getMessage());
			log.error(e);
		}

		return jsonObject;
	}

	/**
	 * 查询业务数据
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/tempQuery")
	public JSONObject tempQuery(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String param = request.getParameter("param");
		try {
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("param", param);
			String result = serviceUrlServiceImpl.post("bdp-easychart-service", "tempQuery", multiValueMap);
			jsonObject = JSONObject.parseObject(result);
			log.debug("jsonObject={}", jsonObject.toJSONString());
		} catch (Exception e) {
			jsonObject.put("code", "-1");
			jsonObject.put("msg", e.getMessage());
			log.error(e);
		}

		return jsonObject;
	}

	/**
	 * 根据模板ID查询业务数据
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/tempQueryByTemplateId")
	public JSONObject tempQueryByTemplateId(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String echart_template_id = request.getParameter("echart_template_id");
		try {
			Map<String, String> multiValueMap = new HashMap<String, String>();
			multiValueMap.put("echart_template_id", echart_template_id);
			String result = serviceUrlServiceImpl.post("bdp-easychart-service", "tempQueryByTemplateId", multiValueMap);
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