package cn.com.infohold.service.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import bdp.commons.authorization.resource.AuthResource;
import bdp.commons.authorization.resource.AuthResourceObject;
import bdp.commons.authorization.resource.AuthResourceOperation;
import bdp.commons.dataservice.config.ConditionBean;
import bdp.commons.dataservice.param.BatchOperationBean;
import bdp.commons.dataservice.param.ExecuteBatchSqlBean;
import bdp.commons.dataservice.param.InsertBean;
import bdp.commons.dataservice.param.QueryBean;
import bdp.commons.dataservice.param.QueryBeanCondition;
import bdp.commons.dataservice.param.QueryBeanOrder;
import bdp.commons.dataservice.ret.RetBean;
import bdp.commons.easyquery.ret.EasyQuery;
import bdp.commons.easyquery.ret.QueryConditions;
import bdp.commons.easyquery.ret.QueryFields;
import bdp.commons.easyquery.ret.QueryGroups;
import bdp.commons.easyquery.ret.QueryOrder;
import bdp.commons.easyquery.ret.QueryRetBean;
import bdp.commons.easyquery.ret.QueryTables;
import bdp.commons.util.BeanUtil;
import cn.com.infohold.basic.util.common.DateUtil;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.service.IQueryDefinitionService;
import cn.com.infohold.tools.util.StringUtil;
import cn.com.infohold.util.CommonUtil;
import cn.easybdp.basic.util.http.BasicHttpUtil;
import lombok.extern.log4j.Log4j2;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-08-03
 */
@Log4j2
@Service
public class QueryDefinitionServiceImpl implements IQueryDefinitionService {

	private static final BasicJsonUtil BJU = BasicJsonUtil.getInstance();

	private static final BasicHttpUtil BHU = BasicHttpUtil.getInstance();

	@Value("${query_condition_metadataCode}")
	private String query_condition_metadataCode;
	@Value("${query_fields_metadataCode}")
	private String query_fields_metadataCode;
	@Value("${query_group_metadataCode}")
	private String query_group_metadataCode;
	@Value("${query_order_metadataCode}")
	private String query_order_metadataCode;
	@Value("${query_tables_metadataCode}")
	private String query_tables_metadataCode;
	@Value("${query_template_metadataCode}")
	private String query_template_metadataCode;
	@Value("${queryUrl}")
	private String queryUrl;
	@Value("${metadataUrl}")
	private String metadataUrl;
	@Value("${db_code}")
	private String db_code;

	@Value("${auth_resource}")
	String auth_resource;
	@Value("${auth_resource_operation}")
	String auth_resource_operation;
	@Value("${auth_operation}")
	String auth_operation;
	@Value("${auth_resource_object}")
	String auth_resource_object;

	@Override
	public RetBean definitionSave(EasyQuery easyQuery) throws Exception {
		RetBean retBean = null;

		if (StringUtil.isNotEmpty(easyQuery.getId())) {// 更新操作
			retBean = definitionEdit(easyQuery);
		} else {// 新增操作
			retBean = definitionAdd(easyQuery);
		}

		return retBean;
	}

	public RetBean definitionAdd(EasyQuery easyQuery) throws Exception {
		ExecuteBatchSqlBean executeBatchSqlBean = new ExecuteBatchSqlBean();
		List<InsertBean> insertBeans = new ArrayList<InsertBean>();// 批量请求list
		String templateId = UUID.randomUUID().toString();// 新增生成uuid

		/**************************** 添加模版表 *******************************/

		easyQuery.setId(templateId);
		java.util.Random random = new java.util.Random();// 定义随机类
		int result = random.nextInt(100);// 返回[0,10)集合中的整数，注意不包括10
		easyQuery.setQuery_code("QUERY_" + DateUtil.getServerTime(null) + result);
		easyQuery.setCreate_time(DateUtil.getServerTime(DateUtil.DEFAULT_TIME_FORMAT_EN));
		templateAdd(easyQuery, insertBeans);

		/**************************** 添加业务对象表 ****************************/

		if (CommonUtil.listIsNotNull(easyQuery.getTables())) {
			tablesAdd(easyQuery, insertBeans);
		}

		/**************************** 添加维度表 ****************************/

		if (CommonUtil.listIsNotNull(easyQuery.getGroups())) {
			groupAdd(easyQuery, insertBeans);
		}

		/**************************** 添加输出属性表 ****************************/

		if (CommonUtil.listIsNotNull(easyQuery.getFields())) {
			fieldsAdd(easyQuery, insertBeans);
		}

		/**************************** 添加筛选器表 ****************************/

		if (CommonUtil.listIsNotNull(easyQuery.getConditions())) {
			conditionAdd(easyQuery, insertBeans);
		}

		AuthResource authResource = initResource(easyQuery, templateId);
		InsertBean arInsertBean = BeanUtil.toInsertBean(authResource);
		arInsertBean.setTableCode(auth_resource);
		arInsertBean.setInsert_time(easyQuery.getCreate_time());
		insertBeans.add(arInsertBean);
		InsertBean aroInsertBean = BeanUtil.toInsertBean(authResource.getAuthResourceOperations().toArray());
		aroInsertBean.setTableCode(auth_resource_operation);
		aroInsertBean.setInsert_time(easyQuery.getCreate_time());
		insertBeans.add(aroInsertBean);
		executeBatchSqlBean.setInsertBeans(insertBeans);
		executeBatchSqlBean.setDb_code(db_code);
		RetBean retBean = CommonUtil.post(queryUrl + "/insertBatch", executeBatchSqlBean);

		return retBean;
	}

	private AuthResource initResource(EasyQuery easyQuery, String templateId) {
		AuthResource authResource = new AuthResource();
		authResource.setResource_id(templateId);
		authResource.setResource_business_id(templateId);
		authResource.setResource_url("/bdp-web/easyquery/easyquery.html?id=" + templateId);
		authResource.setResource_code(easyQuery.getQuery_code());
		authResource.setResource_name(easyQuery.getQuery_name());
		authResource.setResource_type("easyquery");
		authResource.setResource_desc("");
		List<AuthResourceOperation> authResourceOperations = new ArrayList<AuthResourceOperation>();
		AuthResourceOperation search = new AuthResourceOperation();
		search.setId(UUID.randomUUID().toString());
		search.setResource_id(templateId);
		search.setOperation_id("b04de815-ed14-4206-ac92-8cf72d577f07");
		search.setOperation_code("search");
		search.setOperation_name("查询");
		authResourceOperations.add(search);

		AuthResourceOperation export = new AuthResourceOperation();
		export.setId(UUID.randomUUID().toString());
		export.setResource_id(templateId);
		export.setOperation_id("ae6d1e12-3053-4ed6-b19e-3e79334eff57");
		export.setOperation_code("export");
		export.setOperation_name("导出");
		authResourceOperations.add(export);

		List<AuthResourceObject> authResourceObjects = new ArrayList<AuthResourceObject>();
		for (QueryTables tb : easyQuery.getTables()) {
			AuthResourceObject authResourceObject = new AuthResourceObject();
			authResourceObject.setId(UUID.randomUUID().toString());
			authResourceObject.setResource_id(templateId);
			authResourceObject.setData_permission_column("");
			authResourceObject.setObject_metadata_id(tb.getMetadata_id());
			authResourceObject.setObject_code(tb.getTable_name());
			authResourceObject.setObject_name(tb.getTable_comment());
			authResourceObjects.add(authResourceObject);
		}
		authResource.setAuthResourceObject(authResourceObjects);
		authResource.setAuthResourceOperations(authResourceOperations);
		return authResource;
	}

	public void groupAdd(EasyQuery easyQuery, List<InsertBean> insertBeans) throws Exception {
		List<QueryGroups> queryGroups = easyQuery.getGroups();

		for (QueryGroups queryGroup : queryGroups) {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			queryGroup.setQuery_template_id(easyQuery.getId());
			queryGroup.setId(UUID.randomUUID().toString());
			Field[] fields = queryGroup.getClass().getDeclaredFields();

			for (Field field : fields) {
				field.setAccessible(true);// 设置可以访问私有属性
				Object object = field.get(queryGroup);
				if (null != object) {
					map.put(field.getName(), object);
				}
			}

			list.add(map);

			InsertBean insertBean = new InsertBean();
			insertBean.setInsert_time(easyQuery.getCreate_time());
			insertBean.setData(list);
			insertBean.setTableCode(query_group_metadataCode);
			insertBeans.add(insertBean);

			// JSONObject reqObj = new JSONObject();
			// reqObj.put("insert_time", easyQuery.getCreate_time());
			// reqObj.put("tableCode", query_group_metadataCode);
			// reqObj.put("data", list);
			//
			// Map<String, String> reqMap = new HashMap<String, String>();
			// reqMap.put("param", reqObj.toJSONString());
			// String text = BHU.postRequst(queryUrl + "/insert", reqMap,
			// "UTF-8");
			// System.out.println("text4 " + text);
			// retBean = BJU.toJavaBean(text, QueryRetBean.class);
			// list.clear();
		}
		// return retBean;
	}

	public void fieldsAdd(EasyQuery easyQuery, List<InsertBean> insertBeans) throws Exception {
		List<QueryFields> queryFields = easyQuery.getFields();

		for (QueryFields queryField : queryFields) {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			queryField.setQuery_template_id(easyQuery.getId());
			queryField.setId(UUID.randomUUID().toString());
			Field[] fields = queryField.getClass().getDeclaredFields();

			for (Field field : fields) {
				field.setAccessible(true);// 设置可以访问私有属性
				Object object = field.get(queryField);
				if (null != object) {
					map.put(field.getName(), object);
				}
			}

			list.add(map);

			InsertBean insertBean = new InsertBean();
			insertBean.setInsert_time(easyQuery.getCreate_time());
			insertBean.setData(list);
			insertBean.setTableCode(query_fields_metadataCode);
			insertBeans.add(insertBean);

			// JSONObject reqObj = new JSONObject();
			// reqObj.put("insert_time", easyQuery.getCreate_time());
			// reqObj.put("tableCode", query_fields_metadataCode);
			// reqObj.put("data", list);
			//
			// Map<String, String> reqMap = new HashMap<String, String>();
			// reqMap.put("param", reqObj.toJSONString());
			// String text = BHU.postRequst(queryUrl + "/insert", reqMap,
			// "UTF-8");
			// System.out.println("text3 " + text);
			// retBean = BJU.toJavaBean(text, QueryRetBean.class);
			// list.clear();
		}

		// return retBean;
	}

	public void conditionAdd(EasyQuery easyQuery, List<InsertBean> insertBeans) throws Exception {
		List<QueryConditions> queryConditions = easyQuery.getConditions();

		for (QueryConditions queryCondition : queryConditions) {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			queryCondition.setQuery_template_id(easyQuery.getId());
			queryCondition.setId(UUID.randomUUID().toString());
			Field[] fields = queryCondition.getClass().getDeclaredFields();

			for (Field field : fields) {
				field.setAccessible(true);// 设置可以访问私有属性
				Object object = field.get(queryCondition);
				if (null != object) {
					map.put(field.getName(), object);
				}
			}

			list.add(map);

			InsertBean insertBean = new InsertBean();
			insertBean.setInsert_time(easyQuery.getCreate_time());
			insertBean.setData(list);
			insertBean.setTableCode(query_condition_metadataCode);
			insertBeans.add(insertBean);
		}
	}

	public void tablesAdd(EasyQuery easyQuery, List<InsertBean> insertBeans) throws Exception {
		List<QueryTables> queryTables = easyQuery.getTables();

		for (QueryTables queryTable : queryTables) {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			queryTable.setQuery_template_id(easyQuery.getId());
			queryTable.setId(UUID.randomUUID().toString());
			Field[] fields = queryTable.getClass().getDeclaredFields();

			for (Field field : fields) {
				field.setAccessible(true);// 设置可以访问私有属性
				Object object = field.get(queryTable);
				if (null != object) {
					map.put(field.getName(), object);
				}

			}

			list.add(map);

			InsertBean insertBean = new InsertBean();
			insertBean.setInsert_time(easyQuery.getCreate_time());
			insertBean.setData(list);
			insertBean.setTableCode(query_tables_metadataCode);
			insertBeans.add(insertBean);
		}
	}

	public void templateAdd(EasyQuery easyQuery, List<InsertBean> insertBeans) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Field[] fields = easyQuery.getClass().getDeclaredFields();

		for (Field field : fields) {
			String fieldType = field.getType().getName();
			if ("java.lang.String".equals(fieldType)) {// 反射取到模版的数据
				field.setAccessible(true);// 设置可以访问私有属性
				map.put(field.getName(), field.get(easyQuery));
			}
		}

		list.add(map);

		InsertBean insertBean = new InsertBean();
		insertBean.setInsert_time(easyQuery.getCreate_time());
		insertBean.setData(list);
		insertBean.setTableCode(query_template_metadataCode);
		insertBeans.add(insertBean);

	}

	public RetBean definitionEdit(EasyQuery easyQuery) throws Exception {
		QueryRetBean queryRetBean = null;
		RetBean retBean = new RetBean();
		ExecuteBatchSqlBean executeBatchSqlBean = new ExecuteBatchSqlBean();
		List<InsertBean> insertBeans = new ArrayList<InsertBean>();// 批量请求list

		/**************************** 删除子集表 *******************************/
		queryRetBean = subsetDelete(easyQuery.getId());

		/**************************** 修改模版表 *******************************/

		easyQuery.setLast_update_time(DateUtil.getServerTime(DateUtil.DEFAULT_TIME_FORMAT_EN));
		queryRetBean = templateEdit(easyQuery);

		/**************************** 添加业务对象表 ****************************/

		if (CommonUtil.listIsNotNull(easyQuery.getTables())) {
			tablesAdd(easyQuery, insertBeans);
		}

		/**************************** 添加维度表 ****************************/

		if (CommonUtil.listIsNotNull(easyQuery.getGroups())) {
			groupAdd(easyQuery, insertBeans);
		}

		/**************************** 添加输出属性表 ****************************/

		if (CommonUtil.listIsNotNull(easyQuery.getFields())) {
			fieldsAdd(easyQuery, insertBeans);
		}

		/**************************** 添加筛选器表 ****************************/

		if (CommonUtil.listIsNotNull(easyQuery.getConditions())) {
			conditionAdd(easyQuery, insertBeans);
		}

		if ("-1".equals(queryRetBean.getRet_code())) {
			retBean.setRet_code(queryRetBean.getRet_code());
			retBean.setRet_message(queryRetBean.getRet_message());
			return retBean;
		}
		executeBatchSqlBean.setInsertBeans(insertBeans);
		executeBatchSqlBean.setDb_code(db_code);
		retBean = CommonUtil.post(queryUrl + "/insertBatch", executeBatchSqlBean);

		return retBean;
	}

	public QueryRetBean templateEdit(EasyQuery easyQuery) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = easyQuery.getClass().getDeclaredFields();

		for (Field field : fields) {
			String fieldType = field.getType().getName();
			if ("java.lang.String".equals(fieldType)) {// 反射取到模版的数据
				field.setAccessible(true);// 设置可以访问私有属性
				map.put(field.getName(), field.get(easyQuery));
			}
		}

		JSONObject reqObj = new JSONObject();
		reqObj.put("update_time", easyQuery.getLast_update_time());
		reqObj.put("tableCode", query_template_metadataCode);
		reqObj.put("data", map);
		// 查询条件
		List<ConditionBean> conditions = new ArrayList<ConditionBean>();
		ConditionBean conditionBean = new ConditionBean();
		conditionBean.setField1("id");
		conditionBean.setValue1(easyQuery.getId());
		conditionBean.setCond("=");
		conditions.add(conditionBean);
		reqObj.put("conditions", conditions);

		Map<String, String> reqMap = new HashMap<String, String>();
		reqMap.put("param", reqObj.toJSONString());
		String text = BHU.postRequst(queryUrl + "/update", reqMap, "UTF-8");
		QueryRetBean retBean = BJU.toJavaBean(text, QueryRetBean.class);
		return retBean;
	}

	public QueryRetBean subsetDelete(String id) throws Exception {
		QueryRetBean retBean = null;
		JSONObject reqObj = new JSONObject();
		Map<String, String> reqMap = new HashMap<String, String>();
		reqObj.put("delete_time", DateUtil.getServerTime(DateUtil.DEFAULT_TIME_FORMAT_EN));
		String text = "";
		// 查询条件
		List<ConditionBean> conditions = new ArrayList<ConditionBean>();
		ConditionBean conditionBean = new ConditionBean();
		conditionBean.setField1("query_template_id");
		conditionBean.setValue1(id);
		conditionBean.setCond("=");
		conditions.add(conditionBean);
		reqObj.put("conditions", conditions);

		// tebles
		reqObj.put("tableCode", query_tables_metadataCode);
		reqMap.put("param", reqObj.toJSONString());
		text = BHU.postRequst(queryUrl + "/delete", reqMap, "UTF-8");
		retBean = BJU.toJavaBean(text, QueryRetBean.class);

		// fields
		reqObj.put("tableCode", query_fields_metadataCode);
		reqMap.put("param", reqObj.toJSONString());
		text = BHU.postRequst(queryUrl + "/delete", reqMap, "UTF-8");
		retBean = BJU.toJavaBean(text, QueryRetBean.class);

		// condition
		reqObj.put("tableCode", query_condition_metadataCode);
		reqMap.put("param", reqObj.toJSONString());
		text = BHU.postRequst(queryUrl + "/delete", reqMap, "UTF-8");
		retBean = BJU.toJavaBean(text, QueryRetBean.class);

		// group
		reqObj.put("tableCode", query_group_metadataCode);
		reqMap.put("param", reqObj.toJSONString());
		text = BHU.postRequst(queryUrl + "/delete", reqMap, "UTF-8");
		retBean = BJU.toJavaBean(text, QueryRetBean.class);
		return retBean;
	}

	@Override
	public QueryRetBean definitionDelete(String query_template_id) throws Exception {
		QueryRetBean retBean = null;

		JSONObject reqObj = new JSONObject();
		Map<String, String> reqMap = new HashMap<String, String>();
		reqObj.put("delete_time", DateUtil.getServerTime(DateUtil.DEFAULT_TIME_FORMAT_EN));
		String text = "";
		// 查询条件
		List<ConditionBean> conditions = new ArrayList<ConditionBean>();
		ConditionBean conditionBean = new ConditionBean();
		conditionBean.setField1("id");
		conditionBean.setValue1(query_template_id);
		conditionBean.setCond("=");
		conditions.add(conditionBean);
		reqObj.put("conditions", conditions);

		// 删除模版表
		reqObj.put("tableCode", query_template_metadataCode);
		reqMap.put("param", reqObj.toJSONString());
		text = BHU.postRequst(queryUrl + "/delete", reqMap, "UTF-8");
		retBean = BJU.toJavaBean(text, QueryRetBean.class);

		retBean = subsetDelete(query_template_id);// 删除子集

		return retBean;
	}

	// 查询模板
	public QueryBean geneQueryTemplateBean(String query_template_id) {
		Map<String, String> reqMap = new HashMap<String, String>();
		QueryBean queryBean = new QueryBean();
		List<QueryBeanCondition> conditions = new ArrayList<QueryBeanCondition>();
		QueryBeanCondition templateBean = new QueryBeanCondition();
		templateBean.setField1("id");
		templateBean.setValue1(query_template_id);
		templateBean.setCond("=");
		conditions.add(templateBean);
		queryBean.setConditions(conditions);
		queryBean.setTable(query_template_metadataCode);
		// reqMap.put("param", JSON.toJSONString(queryBean));
		return queryBean;
	}

	// 查询表名
	public QueryBean geneQueryTablesBean(String query_template_id) {
		Map<String, String> reqMap = new HashMap<String, String>();
		QueryBean queryBean = new QueryBean();
		List<QueryBeanCondition> conditions = new ArrayList<QueryBeanCondition>();
		QueryBeanCondition conditionBean = new QueryBeanCondition();
		conditionBean.setField1("query_template_id");
		conditionBean.setValue1(query_template_id);
		conditionBean.setCond("=");
		conditions.add(conditionBean);

		queryBean.setConditions(conditions);
		queryBean.setTable(query_tables_metadataCode);
		// reqMap.put("param", JSON.toJSONString(queryBean));
		return queryBean;
	}

	// 查询字段名
	public QueryBean geneQueryFieldsBean(String query_template_id) {
		Map<String, String> reqMap = new HashMap<String, String>();
		QueryBean queryBean = new QueryBean();
		List<QueryBeanCondition> conditions = new ArrayList<QueryBeanCondition>();
		QueryBeanCondition conditionBean = new QueryBeanCondition();
		conditionBean.setField1("query_template_id");
		conditionBean.setValue1(query_template_id);
		conditionBean.setCond("=");
		conditions.add(conditionBean);

		List<QueryBeanOrder> queryBeanOrders = new ArrayList<QueryBeanOrder>();// 默认排序
		QueryBeanOrder queryBeanOrder = new QueryBeanOrder();
		queryBeanOrder.setField("order_index");
		queryBeanOrder.setType("ASC");
		queryBeanOrders.add(queryBeanOrder);

		queryBean.setConditions(conditions);
		queryBean.setOrders(queryBeanOrders);
		queryBean.setTable(query_fields_metadataCode);

		// reqMap.put("param", JSON.toJSONString(queryBean));
		return queryBean;
	}

	// 查询条件
	public QueryBean geneQueryConditionsBean(String query_template_id) {
		Map<String, String> reqMap = new HashMap<String, String>();
		QueryBean queryBean = new QueryBean();
		List<QueryBeanCondition> conditions = new ArrayList<QueryBeanCondition>();
		QueryBeanCondition conditionBean = new QueryBeanCondition();
		conditionBean.setField1("query_template_id");
		conditionBean.setValue1(query_template_id);
		conditionBean.setCond("=");
		conditions.add(conditionBean);

		List<QueryBeanOrder> orders = new ArrayList<QueryBeanOrder>();
		QueryBeanOrder queryBeanOrder = new QueryBeanOrder();
		queryBeanOrder.setField("order_index");
		queryBeanOrder.setType("ASC");
		orders.add(queryBeanOrder);
		queryBean.setOrders(orders);

		queryBean.setConditions(conditions);
		queryBean.setTable(query_condition_metadataCode);
		// reqMap.put("param", JSON.toJSONString(queryBean));
		return queryBean;
	}

	// 查询分组
	public QueryBean geneQueryGroupBean(String query_template_id) {
		Map<String, String> reqMap = new HashMap<String, String>();
		QueryBean queryBean = new QueryBean();
		List<QueryBeanCondition> conditions = new ArrayList<QueryBeanCondition>();
		QueryBeanCondition conditionBean = new QueryBeanCondition();
		conditionBean.setField1("query_template_id");
		conditionBean.setValue1(query_template_id);
		conditionBean.setCond("=");
		conditions.add(conditionBean);

		queryBean.setConditions(conditions);
		queryBean.setTable(query_group_metadataCode);
		return queryBean;
	}

	@Override
	public QueryRetBean definitionGet(String query_template_id) throws Exception {
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis();
		String qurl = queryUrl + "/queryByList";
		String chartCode = "UTF-8";
		BatchOperationBean bob = new BatchOperationBean();
		List<QueryBean> batchQuery = new ArrayList<>();
		Map<String, String> reqMap = new HashMap<String, String>();
		QueryRetBean ret = null;

		List<Map<String, Object>> templateList = null;
		List<Map<String, Object>> teblesteList = null;
		List<Map<String, Object>> fieldsList = null;
		List<Map<String, Object>> conditionList = null;
		List<Map<String, Object>> groupList = null;
		try {
			// 模版查询
			QueryBean templateMap = geneQueryTemplateBean(query_template_id);
			batchQuery.add(templateMap);
			// tebles
			QueryBean tableMap = geneQueryTablesBean(query_template_id);
			batchQuery.add(tableMap);
			// fields
			QueryBean fieldMap = geneQueryFieldsBean(query_template_id);
			batchQuery.add(fieldMap);
			// condition
			QueryBean conditionMap = geneQueryConditionsBean(query_template_id);
			batchQuery.add(conditionMap);
			// group
			QueryBean groupMap = geneQueryGroupBean(query_template_id);
			batchQuery.add(groupMap);
			bob.setQueryBeans(batchQuery);
			// 批量查询
			reqMap.put("param", JSON.toJSONString(bob));
			String text = BHU.postRequst(qurl, reqMap, chartCode);
			endTime = System.currentTimeMillis();
			log.debug("posturl={} ,time={} , data={}", qurl, endTime - startTime, reqMap);
			RetBean brBean = BJU.toJavaBean(text, RetBean.class);
			Map<String, List<Map<String, Object>>> retMap = brBean.getBatchResults();
			// 转为对象
			templateList = retMap.get(query_template_metadataCode);
			teblesteList = retMap.get(query_tables_metadataCode);
			fieldsList = retMap.get(query_fields_metadataCode);
			conditionList = retMap.get(query_condition_metadataCode);
			groupList = retMap.get(query_group_metadataCode);
			startTime = System.currentTimeMillis();
			ret = AssemblingJson(templateList, teblesteList, fieldsList, conditionList, groupList);
			endTime = System.currentTimeMillis();
			log.debug("AssemblingJson usetime={} ", endTime - startTime);
		} catch (Exception ex) {
			log.error("ex={}", ex);
			log.error("qurl={},reqMap={}", qurl, reqMap);
			log.error("templateRetBean={}", templateList);
			log.error("tablesRetBean={}", teblesteList);
			log.error("fieldsRetBean={}", fieldsList);
			log.error("conditionRetBean={}", conditionList);
			log.error("groupRetBean={}", groupList);
			throw ex;
		}
		return ret;
	}

	private QueryRetBean AssemblingJson(List<Map<String, Object>> templateList, List<Map<String, Object>> teblesteList,
			List<Map<String, Object>> fieldsList, List<Map<String, Object>> conditionList,
			List<Map<String, Object>> groupList) throws IOException {
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis();

		QueryRetBean ret = new QueryRetBean();
		Map<String, Object> retMap = new HashMap<String, Object>();// 返回map
		// 模版查询
		// List<Map<String, Object>> templateList =
		// templateRetBean.getResults();
		// log.debug("templateList={}", templateList);
		List<EasyQuery> templateEntitylist = CommonUtil.mapsToObjs(templateList, EasyQuery.class);
		// tebles
		// List<Map<String, Object>> teblesteList = tablesRetBean.getResults();
		List<QueryTables> teblesEntitylist = CommonUtil.mapsToObjs(teblesteList, QueryTables.class);
		// fields
		// List<Map<String, Object>> fieldsList = fieldsRetBean.getResults();
		List<QueryFields> fieldsEntitylist = CommonUtil.mapsToObjs(fieldsList, QueryFields.class);
		// condition
		// List<Map<String, Object>> conditionList =
		// conditionRetBean.getResults();
		List<QueryConditions> conditionEntitylist = CommonUtil.mapsToObjs(conditionList, QueryConditions.class);
		// group
		// List<Map<String, Object>> groupList = groupRetBean.getResults();
		List<QueryGroups> groupEntitylist = CommonUtil.mapsToObjs(groupList, QueryGroups.class);

		Map<String, String> reqMap = new HashMap<String, String>();
		List<JSONObject> retList = new ArrayList<JSONObject>();// 多表预留为list

		for (QueryTables queryTables : teblesEntitylist) {// 取表和元数据信息
			reqMap.clear();
			String metadataId = queryTables.getMetadata_id();
			reqMap.put("metadataId", metadataId);
			startTime = System.currentTimeMillis();
			String text = BHU.postRequst(metadataUrl, reqMap, "UTF-8");
			endTime = System.currentTimeMillis();
			log.debug("post metadataUrl={},useTime={},param={}", metadataUrl, endTime - startTime, reqMap);
			JSONObject metadataObj = JSON.parseObject(text).getJSONObject("list");
			metadataObj = jsonTransformation(metadataObj);
			retList.add(metadataObj);
			beanFillByMetadata(teblesEntitylist, fieldsEntitylist, conditionEntitylist, groupEntitylist, metadataObj);// 填充bean
		}

		EasyQuery templateEntity = templateEntitylist.get(0);
		templateEntity.setConditions(conditionEntitylist);
		templateEntity.setGroups(groupEntitylist);
		templateEntity.setFields(fieldsEntitylist);
		templateEntity.setTables(teblesEntitylist);

		for (JSONObject obj : retList) {// 拼装前台的字段obj是最外层
			JSONArray children = obj.getJSONArray("children");

			obj.put("metadata_id", obj.getString("metadataId"));
			obj.remove("metadataId");
			for (Object object : children) {// 字段层
				JSONObject json = (JSONObject) object;
				JSONObject property = json.getJSONObject("property");
				property.remove("column_name");
				Set<String> propertySet = property.keySet();
				for (String key : propertySet) {
					json.put(key, property.get(key));
				}
				json.remove("property");
			}

			obj.put("columns", children);
			obj.remove("children");

			obj.getJSONObject("property").remove("table_name");
			for (String key : obj.getJSONObject("property").keySet()) {
				obj.put(key, obj.getJSONObject("property").get(key));
			}
			obj.remove("property");
		}

		retMap.put("metadata", retList);
		retMap.put("querytemplate", templateEntity);
		ret.setResultJson(retMap);
		return ret;
	}

	private QueryRetBean AssemblingJson(QueryRetBean templateRetBean, QueryRetBean tablesRetBean,
			QueryRetBean fieldsRetBean, QueryRetBean conditionRetBean, QueryRetBean groupRetBean,
			QueryRetBean orderRetBean) throws IOException {
		QueryRetBean ret = new QueryRetBean();
		Map<String, Object> retMap = new HashMap<String, Object>();// 返回map
		// 模版查询
		List<Map<String, Object>> templateList = templateRetBean.getResults();
		List<EasyQuery> templateEntitylist = CommonUtil.mapsToObjs(templateList, EasyQuery.class);
		// tebles
		List<Map<String, Object>> teblesteList = tablesRetBean.getResults();
		List<QueryTables> teblesEntitylist = CommonUtil.mapsToObjs(teblesteList, QueryTables.class);
		// fields
		List<Map<String, Object>> fieldsList = fieldsRetBean.getResults();
		List<QueryFields> fieldsEntitylist = CommonUtil.mapsToObjs(fieldsList, QueryFields.class);
		// condition
		List<Map<String, Object>> conditionList = conditionRetBean.getResults();
		List<QueryConditions> conditionEntitylist = CommonUtil.mapsToObjs(conditionList, QueryConditions.class);
		// group
		List<Map<String, Object>> groupList = groupRetBean.getResults();
		List<QueryGroups> groupEntitylist = CommonUtil.mapsToObjs(groupList, QueryGroups.class);

		// order
		List<Map<String, Object>> orderList = orderRetBean.getResults();
		List<QueryOrder> orderEntitylist = CommonUtil.mapsToObjs(orderList, QueryOrder.class);

		Map<String, String> reqMap = new HashMap<String, String>();
		List<JSONObject> retList = new ArrayList<JSONObject>();// 多表预留为list
		for (QueryTables queryTables : teblesEntitylist) {// 取表和元数据信息
			String metadataId = queryTables.getMetadata_id();
			reqMap.put("metadataId", metadataId);
			String text = BHU.postRequst(metadataUrl, reqMap, "UTF-8");
			JSONObject metadataObj = JSON.parseObject(text).getJSONObject("list");
			metadataObj = jsonTransformation(metadataObj);
			retList.add(metadataObj);
			beanFillByMetadata(teblesEntitylist, fieldsEntitylist, conditionEntitylist, groupEntitylist, metadataObj);// 填充bean
		}

		EasyQuery templateEntity = templateEntitylist.get(0);
		templateEntity.setConditions(conditionEntitylist);
		templateEntity.setGroups(groupEntitylist);
		templateEntity.setFields(fieldsEntitylist);
		templateEntity.setTables(teblesEntitylist);

		for (JSONObject obj : retList) {// 拼装前台的字段obj是最外层
			JSONArray children = obj.getJSONArray("children");

			obj.put("metadata_id", obj.getString("metadataId"));
			obj.remove("metadataId");
			for (Object object : children) {// 字段层
				JSONObject json = (JSONObject) object;
				JSONObject property = json.getJSONObject("property");
				property.remove("column_name");
				Set<String> propertySet = property.keySet();
				for (String key : propertySet) {
					json.put(key, property.get(key));
				}
				json.remove("property");
			}

			obj.put("columns", children);
			obj.remove("children");

			obj.getJSONObject("property").remove("table_name");
			for (String key : obj.getJSONObject("property").keySet()) {
				obj.put(key, obj.getJSONObject("property").get(key));
			}
			obj.remove("property");
		}

		retMap.put("metadata", retList);
		retMap.put("querytemplate", templateEntity);
		ret.setResultJson(retMap);
		return ret;
	};

	private void beanFillByMetadata(List<QueryTables> teblesEntitylist, List<QueryFields> fieldsEntitylist,
			List<QueryConditions> conditionEntitylist, List<QueryGroups> groupEntitylist, JSONObject metadataObj) {
		JSONArray childrens = metadataObj.getJSONArray("children");

		// table
		for (QueryTables queryTable : teblesEntitylist) {
			if (queryTable.getMetadata_id().equals(metadataObj.getString("metadataId"))) {
				queryTable.setTable_title(metadataObj.getJSONObject("property").getString("table_comment"));
			}

		}

		// fields
		for (QueryFields queryFields : fieldsEntitylist) {
			for (Object object : childrens) {
				JSONObject jsonObject = (JSONObject) object;
				if (jsonObject.getString("metadataId").equals(queryFields.getMetadata_id())) {
					queryFields.setField_name(jsonObject.getJSONObject("property").getString("column_comment"));
					queryFields.setField_type(jsonObject.getJSONObject("property").getString("data_type"));
					break;
				}

			}
		}

		// conditions
		for (QueryConditions queryConditions : conditionEntitylist) {
			for (Object object : childrens) {
				JSONObject jsonObject = (JSONObject) object;
				if (queryConditions.getMetadata_id().equals(jsonObject.getString("metadataId"))) {
					queryConditions.setField_name(jsonObject.getJSONObject("property").getString("column_comment"));
					queryConditions.setField_type(jsonObject.getJSONObject("property").getString("data_type"));
					break;
				}

			}
		}

		// groups
		for (QueryGroups queryGroups : groupEntitylist) {
			for (Object object : childrens) {
				JSONObject jsonObject = (JSONObject) object;
				if (queryGroups.getMetadata_id().equals(jsonObject.getString("metadataId"))) {
					queryGroups.setField_name(jsonObject.getJSONObject("property").getString("column_comment"));
					queryGroups.setField_type(jsonObject.getJSONObject("property").getString("data_type"));
					break;
				}

			}
		}
	}

	private JSONObject jsonTransformation(JSONObject metadataObj) {
		JSONArray jsonArray = metadataObj.getJSONArray("children");
		JSONArray jsonArray1 = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		for (Object object : jsonArray) {
			JSONObject obj = (JSONObject) object;
			JSONObject jsonObject2 = new JSONObject();
			jsonObject2.put("column_name", obj.getJSONObject("property").getString("column_comment"));
			jsonObject2.put("column_code", obj.getJSONObject("property").getString("column_name"));
			jsonObject2.put("metadata_id", obj.getString("metadataId"));
			jsonObject2.putAll(obj);
			jsonArray1.add(jsonObject2);
		}
		metadataObj.put("children", jsonArray1);
		jsonObject.put("table_name", metadataObj.getJSONObject("property").getString("table_comment"));
		jsonObject.put("table_code", metadataObj.getJSONObject("property").getString("table_name"));
		jsonObject.putAll(metadataObj);
		// System.out.println(jsonObject);
		return jsonObject;
	}

	@Override
	public JSONObject isRepeatQueryCode(String param) throws Exception {
		JSONObject jsonObject = new JSONObject();
		JSONObject paramJson = JSONObject.parseObject(param);
		QueryBean queryBean = new QueryBean();
		queryBean.setTable(query_template_metadataCode);
		RetBean retBean = CommonUtil.post(queryBean);
		jsonObject.put("msg", false);
		for (Map<String, Object> map : retBean.getResults()) {
			String query_code = map.get("query_code") + "";
			String templateId = map.get("id") + "";
			if (paramJson.getString("queryCode").equals(query_code)
					&& !paramJson.getString("templateId").equals(templateId)) {// 判断代码是否有重复的
				jsonObject.put("msg", true);
				break;
			}
		}

		return jsonObject;
	}
}
