package cn.com.infohold.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.jcabi.aspects.Loggable;

import bdp.commons.dataservice.ret.RetBean;
import bdp.commons.easyquery.ret.EasyQuery;
import bdp.commons.easyquery.ret.QueryRetBean;
import cn.com.infohold.service.IQueryDefinitionService;
import cn.com.infohold.util.AnalysisUtil;
import lombok.extern.log4j.Log4j2;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author mojiaxing
 * @since 2017-08-03
 */
@RestController
@Log4j2
public class QueryDefinitionController {
	@Autowired
	IQueryDefinitionService queryDefinitionService;
	
	
	@Loggable(Loggable.DEBUG)
	@RequestMapping(value = "/definitionSave")
	public RetBean definitionSave(@RequestParam String param) throws Exception {
		RetBean ret = null;
		try {
			EasyQuery easyQuery = AnalysisUtil.toQueryBean(param, EasyQuery.class);
			ret = queryDefinitionService.definitionSave(easyQuery);
		} catch (Exception ex) {
			ret.setRet_code("-1");
			ret.setRet_message(ex.getLocalizedMessage());
			log.error(ex);
			throw ex;
		}
		return ret;
	}
	
	@Loggable(Loggable.DEBUG)
	@RequestMapping(value = "/isRepeatQueryCode")
	public JSONObject isRepeatQueryCode(@RequestParam String param) throws Exception {
		JSONObject ret = null;
		try {
			ret = queryDefinitionService.isRepeatQueryCode(param);
		} catch (Exception ex) {
			log.error(ex);
			throw ex;
		}
		return ret;
	}
	
	@Loggable(Loggable.DEBUG)
	@RequestMapping(value = "/definitionDelete")
	public QueryRetBean definitionDelete(@RequestParam String query_template_id) throws Exception {
		QueryRetBean ret = null;
		try {
			ret = queryDefinitionService.definitionDelete(query_template_id);
		} catch (Exception ex) {
			ret.setRet_code("-1");
			ret.setRet_message(ex.getLocalizedMessage());
			log.error(ex);
			throw ex;
		}
		return ret;
	}
	
	@Loggable(Loggable.DEBUG)
	@RequestMapping(value = "/definitionGet")
	@ResponseBody
	public QueryRetBean definitionGet(@RequestParam String query_template_id) throws Exception {
		QueryRetBean ret = new QueryRetBean();
		try {
			ret = queryDefinitionService.definitionGet(query_template_id);
			ret.setRet_code("0");
		} catch (Exception ex) {
			ret.setRet_code("-1");
			ret.setRet_message(ex.getLocalizedMessage());
			log.error(ex);
			throw ex;
		}
		return ret;
	}
}
