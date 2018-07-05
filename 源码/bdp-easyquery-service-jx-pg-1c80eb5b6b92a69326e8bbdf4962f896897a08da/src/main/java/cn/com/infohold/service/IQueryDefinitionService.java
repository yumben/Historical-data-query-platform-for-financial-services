package cn.com.infohold.service;

import com.alibaba.fastjson.JSONObject;

import bdp.commons.dataservice.ret.RetBean;
import bdp.commons.easyquery.ret.EasyQuery;
import bdp.commons.easyquery.ret.QueryRetBean;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-08-03
 */
public interface IQueryDefinitionService {

	RetBean definitionSave(EasyQuery easyQuery) throws Exception;
	JSONObject isRepeatQueryCode(String param) throws Exception;
	QueryRetBean definitionDelete(String query_template_id) throws Exception;
	QueryRetBean definitionGet(String query_template_id) throws Exception;
	
}
