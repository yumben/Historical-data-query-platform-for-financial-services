package cn.com.infohold.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import bdp.commons.authorization.resource.AuthOperation;
import bdp.commons.easyquery.param.QueryParams;
import bdp.commons.easyquery.ret.EasyQuery;
import bdp.commons.easyquery.ret.QueryConditions;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.mapper.ServiceUrlMapper;
import cn.com.infohold.service.IEasyQueryService;
import cn.com.infohold.service.IUnitService;
import cn.com.infohold.tools.util.StringUtil;
import cn.com.infohold.util.CommonUtil;
import cn.com.infohold.util.DateThis;
import lombok.extern.log4j.Log4j2;
@Log4j2
@Service
public class EasyQueryServiceImpl implements IEasyQueryService {

	@Autowired
	private ServiceUrlMapper serviceUrlMapper;
	@Autowired
	IUnitService unitServiceImpl;

	@Override
	public EasyQuery selectEasyQueryTemplate(String id, String token) throws Exception {
		EasyQuery easyQuery = new EasyQuery();
		JSONObject jsonObject = new JSONObject();

		List<Map<String, Object>> query_template = serviceUrlMapper.selectQueryTemplate(id);
		jsonObject.putAll(query_template.get(0));

		List<Map<String, Object>> query_condition = serviceUrlMapper.selectQueryCondition(id);
		jsonObject.put("conditions", getDefaultCondition(query_condition, id, token));

		List<Map<String, Object>> query_fields = serviceUrlMapper.selectQueryFields(id);
		jsonObject.put("fields", query_fields);

		List<Map<String, Object>> query_group = serviceUrlMapper.selectQueryGroup(id);
		jsonObject.put("groups", query_group);

		List<Map<String, Object>> query_tables = serviceUrlMapper.selectQueryTables(id);
		jsonObject.put("tables", query_tables);

		List<Map<String, Object>> query_order = serviceUrlMapper.selectQueryOrder(id);
		jsonObject.put("orders", query_order);

		easyQuery = BasicJsonUtil.getInstance().toJavaBean(jsonObject.toJSONString(), EasyQuery.class);
		if (StringUtil.isNotEmpty(token)) {// 是否开启权限
			List<Map<String, Object>> filteList = serviceUrlMapper.selectAuthOperations(id, token);
			List<AuthOperation> authOperations = CommonUtil.mapsToObjs(filteList, AuthOperation.class);
			easyQuery.setAuthOperations(authOperations);
		} else {
			List<AuthOperation> authOperations = new ArrayList<AuthOperation>();
			AuthOperation authOperation = new AuthOperation();
			authOperation.setOperation_code("close");// 用来表示关闭了权限开关
			authOperations.add(authOperation);
			easyQuery.setAuthOperations(authOperations);
		}
		return easyQuery;
	}

	public List<QueryConditions> getDefaultCondition(List<Map<String, Object>> conditions, String query_template_id,
			String token) throws IOException {
		List<QueryConditions> resultList = new ArrayList<QueryConditions>();
		if (CommonUtil.listIsNotNull(conditions)) {
			for (Map<String, Object> map : conditions) {
				String json = JSON.toJSONString(map);
				QueryConditions queryConditions = JSON.parseObject(json, QueryConditions.class);
	 			if ("unit_id".equals(queryConditions.getDynamic_field())) {
					QueryParams queryParams = new QueryParams();
					queryParams.setQuery_template_id(query_template_id);
					queryParams.setToken(token);
					Map<String, Object> defaultConditions = unitServiceImpl.getDefineUnitList(token);
					queryConditions.setValue1(defaultConditions.get("unit_ids").toString());
					queryConditions.setValfield(defaultConditions.get("unit_names").toString());
					resultList.add(queryConditions);
				} else if (queryConditions.getDynamic_field()!=null &&(queryConditions.getDynamic_field().endsWith("_end")||queryConditions.getDynamic_field().endsWith("_start"))) {
					//获取系统当前时间的冬天获取系统的月初，月末，年初年末等
					resultList.add(getDateThis(queryConditions));
				}else {
					resultList.add(queryConditions);
				}
			}
		}
		return resultList;
	}

	/**
	 * 根据系统时间获取月初、月末、季初、季末、年初、年末
	 * @param queryConditions
	 * @param query_template_id
	 * @param token
	 * @return
	 */
	private QueryConditions getDateThis(QueryConditions queryConditions) {
		String dateTime = "";
		DateThis dateThis = new DateThis();
		if ("early_month_start".equals(queryConditions.getDynamic_field())) {
			//上月初
			dateTime = dateThis.thisEarlyMonth();
		}else if ("early_month_end".equals(queryConditions.getDynamic_field())) {
			//上月末
			dateTime = dateThis.thisEarlyMonthEnd();
		}else if ("month_start".equals(queryConditions.getDynamic_field())) {
			//月初
			dateTime = dateThis.thisMonth();
		}else if ("month_end".equals(queryConditions.getDynamic_field())) {
			//月末
			dateTime = dateThis.thisMonthEnd();
		}else if ("season_start".equals(queryConditions.getDynamic_field())) {
			//季初
			dateTime = dateThis.thisSeason();
		}else if ("season_end".equals(queryConditions.getDynamic_field())) {
			//季末
			dateTime = dateThis.thisSeasonEnd();
		}else if ("year_start".equals(queryConditions.getDynamic_field())) {
			//年初
			dateTime = dateThis.thisYear();
		}else if ("year_end".equals(queryConditions.getDynamic_field())) {
			//年末
			dateTime = dateThis.thisYearEnd();
		}else {
			
		}
		log.info("根據系統時間获取月初月末等时间="+dateTime);
		queryConditions.setValue1(dateTime);
		return queryConditions;
	}

}
