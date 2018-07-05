package cn.com.infohold.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jcabi.aspects.Loggable;

import bdp.commons.authorization.resource.AuthOperation;
import bdp.commons.dataservice.bean.Condition;
import bdp.commons.dataservice.bean.Field;
import bdp.commons.dataservice.bean.Group;
import bdp.commons.dataservice.bean.Order;
import bdp.commons.dataservice.bean.SqlBean;
import bdp.commons.dataservice.bean.Table;
import bdp.commons.dataservice.param.BatchOperationBean;
import bdp.commons.dataservice.param.ExecuteBySqlBean;
import bdp.commons.dataservice.param.QueryBean;
import bdp.commons.dataservice.param.QueryBeanCondition;
import bdp.commons.dataservice.param.QueryBeanField;
import bdp.commons.dataservice.param.QueryBeanGroup;
import bdp.commons.dataservice.param.QueryBeanOrder;
import bdp.commons.dataservice.param.QuerySummary;
import bdp.commons.dataservice.ret.RetBean;
import bdp.commons.easyquery.param.QueryParams;
import bdp.commons.easyquery.ret.EasyQuery;
import bdp.commons.easyquery.ret.QueryConditions;
import bdp.commons.easyquery.ret.QueryFields;
import bdp.commons.easyquery.ret.QueryGroups;
import bdp.commons.easyquery.ret.QueryTables;
import cn.com.infohold.basic.util.file.PropUtil;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.service.IEasyQueryService;
import cn.com.infohold.service.IExportFileService;
import cn.com.infohold.tools.util.StringUtil;
import cn.com.infohold.util.CommonUtil;
import cn.com.infohold.util.MultiTableUtil;
import cn.com.infohold.util.PermissionUtil;
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
@Service
@Log4j2
public class EasyQueryServiceImpl implements IEasyQueryService {
	private static final BasicJsonUtil BJU = BasicJsonUtil.getInstance();
	private static final BasicHttpUtil BHU = BasicHttpUtil.getInstance();
	private static final String QUERYBYSQL = "/queryBySql";
	// private static final String SUMMARYQUERY = "/summaryQuery";
	private static final String QUERYMULTITABLE = "/queryMultiTable";

	@Autowired
	IExportFileService exportFileService;

	@Value("${db_code}")
	String db_code;
	@Value("${queryUrl}")
	String queryUrl;
	@Value("${exportMax}")
	String exportMax;

	@Value("${isFilter}")
	String isFilter;

	@SuppressWarnings("unchecked")
	public RetBean getEasyQueryDataJsonBySingle(QueryParams queryParams) throws Exception {
		RetBean retBean = new RetBean();
		// 查询bean
		QueryBean queryBean = CommonUtil.getCommonParam(queryParams);

		// 数据查询并返回
		retBean = CommonUtil.post(PropUtil.getProperty("queryDataList"), queryBean, "bdp-data-service");

		Map<String, Object> totalResult = retBean.getTotalResult();

		// if (is_compute) {
		List<Map<String, Object>> tempMaps = new ArrayList<Map<String, Object>>();
		if (retBean.getResults() != null) {
			tempMaps.addAll(retBean.getResults());
		}

		for (QueryFields queryField : queryParams.getAll_fields()) {
			if ("false".equals(queryField.getIs_compute())) {
				String field = StringUtil.isNotEmpty(queryField.getAlias()) ? queryField.getAlias()
						: queryField.getName();
				for (Map<String, Object> tempMap : tempMaps) {
					tempMap.put(field, tempMap.get(field));
				}
			}
		}

		for (QueryFields queryField : queryParams.getAll_fields()) {// 计算字段
			if ("true".equals(queryField.getIs_compute())) {
				String expression = queryField.getCompute_expression();
				String field = StringUtil.isNotEmpty(queryField.getAlias()) ? queryField.getAlias()
						: queryField.getName();
				for (int i = 0; i < retBean.getResults().size() && i < tempMaps.size(); i++) {
					Map<String, Object> retMap = retBean.getResults().get(i);
					Map<String, Object> tempMap = tempMaps.get(i);
					retMap.put(field, expressionCompute(expression, tempMap));
				}
			}
		}

		for (QueryFields queryField : queryParams.getFields()) {// 单位千分位
			String field = StringUtil.isNotEmpty(queryField.getAlias()) ? queryField.getAlias() : queryField.getName();

			if (null != totalResult) {
//				Map<String, Object> total = (Map<String, Object>) totalResult.get("total");
				Map<String, Object> pageTotal = (Map<String, Object>) totalResult.get("pageTotal");
//				BigDecimal totalBd = null;
				BigDecimal pageTotalBd = null;

				if ( null != pageTotal
						&& null != pageTotal.get(field)) {
//					totalBd = new BigDecimal(total.get(field) + "");
					pageTotalBd = new BigDecimal(pageTotal.get(field) + "");
					if (StringUtil.isEmpty(queryField.getField_decimal_point())) {// 没有设置小数位
//						if (totalBd.doubleValue() % 1.0 == 0) {// 如果小数位为0，则返回整数
//							totalBd = totalBd.setScale(0);
//						}
						if (pageTotalBd.doubleValue() % 1.0 == 0) {// 如果小数位为0，则返回整数
							pageTotalBd = pageTotalBd.setScale(0);
						}
						if ("true".equals(queryField.getField_unit_limit())) {
							if ("2".equals(queryParams.getUnit())) {
								BigDecimal bd1 = new BigDecimal("1000");
//								totalBd = totalBd.divide(bd1);
								pageTotalBd = pageTotalBd.divide(bd1);
							} else if ("3".equals(queryParams.getUnit())) {
								BigDecimal bd1 = new BigDecimal("10000");
//								totalBd = totalBd.divide(bd1);
								pageTotalBd = pageTotalBd.divide(bd1);
							} else if ("4".equals(queryParams.getUnit())) {
								BigDecimal bd1 = new BigDecimal("100000000");
//								totalBd = totalBd.divide(bd1);
								pageTotalBd = pageTotalBd.divide(bd1);
							}

						}
					} else {// 有设置小数位
						int scale = Integer.parseInt(queryField.getField_decimal_point());
//						totalBd = totalBd.setScale(scale, BigDecimal.ROUND_HALF_UP);
						pageTotalBd = pageTotalBd.setScale(scale, BigDecimal.ROUND_HALF_UP);
						if ("true".equals(queryField.getField_unit_limit())) {
							if ("2".equals(queryParams.getUnit())) {
								BigDecimal bd1 = new BigDecimal("1000");
//								totalBd = totalBd.divide(bd1, scale, BigDecimal.ROUND_HALF_UP);
								pageTotalBd = pageTotalBd.divide(bd1, scale, BigDecimal.ROUND_HALF_UP);
							} else if ("3".equals(queryParams.getUnit())) {
								BigDecimal bd1 = new BigDecimal("10000");
//								totalBd = totalBd.divide(bd1, scale, BigDecimal.ROUND_HALF_UP);
								pageTotalBd = pageTotalBd.divide(bd1, scale, BigDecimal.ROUND_HALF_UP);
							} else if ("4".equals(queryParams.getUnit())) {
								BigDecimal bd1 = new BigDecimal("100000000");
//								totalBd = totalBd.divide(bd1, scale, BigDecimal.ROUND_HALF_UP);
								pageTotalBd = pageTotalBd.divide(bd1, scale, BigDecimal.ROUND_HALF_UP);
							}

						}
					}
//					total.put(field, totalBd.toPlainString());
					pageTotal.put(field, pageTotalBd.toPlainString());
				}

			}

			for (int i = 0; i < retBean.getResults().size() && i < tempMaps.size(); i++) {
				Map<String, Object> retMap = retBean.getResults().get(i);
				if (null != retMap.get(field) && ("decimal".equals(queryField.getField_type())
						|| "number".equals(queryField.getField_type()))) {
					// log.info("报错的field = " + field + " value = " +
					// retMap.get(field));
					BigDecimal bd = null;
					if (StringUtil.isNotEmpty(retMap.get(field) + "")) {
						bd = new BigDecimal(retMap.get(field) + "");
					} else {
						bd = new BigDecimal("0");
					}

					if (StringUtil.isEmpty(queryField.getField_decimal_point())) {// 没有设置小数位
						if (bd.doubleValue() % 1.0 == 0) {// 如果小数位为0，则返回整数
							bd = bd.setScale(0);
						}
						if ("true".equals(queryField.getField_unit_limit())) {
							if ("2".equals(queryParams.getUnit())) {
								BigDecimal bd1 = new BigDecimal("1000");
								bd = bd.divide(bd1);
							} else if ("3".equals(queryParams.getUnit())) {
								BigDecimal bd1 = new BigDecimal("10000");
								bd = bd.divide(bd1);
							} else if ("4".equals(queryParams.getUnit())) {
								BigDecimal bd1 = new BigDecimal("100000000");
								bd = bd.divide(bd1);
							}

						}
					} else {// 有设置小数位
						int scale = Integer.parseInt(queryField.getField_decimal_point());
						bd = bd.setScale(scale, BigDecimal.ROUND_HALF_UP);
						if ("true".equals(queryField.getField_unit_limit())) {
							if ("2".equals(queryParams.getUnit())) {
								BigDecimal bd1 = new BigDecimal("1000");
								bd = bd.divide(bd1, scale, BigDecimal.ROUND_HALF_UP);
							} else if ("3".equals(queryParams.getUnit())) {
								BigDecimal bd1 = new BigDecimal("10000");
								bd = bd.divide(bd1, scale, BigDecimal.ROUND_HALF_UP);
							} else if ("4".equals(queryParams.getUnit())) {
								BigDecimal bd1 = new BigDecimal("100000000");
								bd = bd.divide(bd1, scale, BigDecimal.ROUND_HALF_UP);
							}

						}
					}
					retMap.put(field, bd.toPlainString());
				}
			}

			// }
		}
		Long maxSize = Long.parseLong(exportMax);// 把导出限制传到页面
		List<Object> totalVar = new ArrayList<Object>();
		totalVar.add(maxSize);
		retBean.setObjectList(totalVar);
		// }
		return retBean;

	}

	public Object expressionCompute(String expression, Map<String, Object> retMap) throws ScriptException {
		for (String mapField : retMap.keySet()) {
			expression = expression.replace("{" + mapField + "}", "(" + retMap.get(mapField) + ")");
			// log.info("expression = " + expression);
		}
		return CommonUtil.getResult(expression);
	}

	public RetBean getEasyQueryDataJsonByMulti(QueryParams queryParams, List<QueryTables> queryTables)
			throws IOException {
		RetBean retBean = new RetBean();
		SqlBean sqlBean = new SqlBean();

		sqlBean.setDb_metadata(queryParams.getMetadata_id());

		if (queryParams.getPageSize() != null) {
			sqlBean.setLimit(queryParams.getPageSize());
		}
		if (queryParams.getCurPage() != null) {
			sqlBean.setSkip(queryParams.getPageSize() * (queryParams.getCurPage() - 1));
		}
		List<Condition> tableConditions = new ArrayList<Condition>();// table的条件
		List<Table> tables = MultiTableUtil.getTables(queryParams, queryTables, db_code, tableConditions); // 表名
		List<Field> fields = MultiTableUtil.getFields(queryParams.getFields()); // 返回字段
		List<Condition> conditions = MultiTableUtil.getConditions(queryParams.getConditions()); // 条件

		List<Group> groups = MultiTableUtil.getGroups(queryParams.getGroups()); // 分组
		List<Order> orders = MultiTableUtil.getOrders(queryParams.getOrders()); // 排序

		if (CommonUtil.listIsNotNull(conditions)) {
			tableConditions.addAll(conditions);
		}

		sqlBean.setTables(tables);
		sqlBean.setFields(fields);

		sqlBean.setConditions(tableConditions);
		sqlBean.setGroups(groups);
		sqlBean.setOrders(orders);
		retBean = CommonUtil.post(queryUrl + QUERYMULTITABLE, sqlBean);

		return retBean;
	}

	@Loggable(Loggable.DEBUG)
	public RetBean queryDataList(QueryParams queryParams) throws Exception {
		long s = System.currentTimeMillis();
		RetBean retBean = null;
		// 取出表

		retBean = getEasyQueryDataJsonBySingle(queryParams);

		long s2 = System.currentTimeMillis();
		log.debug("getEasyQueryDataJson简易查询耗时时间：" + (s2 - s) + "ms ");
		return retBean;
	}

	@Loggable(Loggable.DEBUG)
	public RetBean queryTotalCount(QueryParams queryParams) throws Exception {
		long s = System.currentTimeMillis();

		QueryBean queryBean = CommonUtil.getCommonParam(queryParams);
		// 数据查询并返回
		RetBean retBean = CommonUtil.post(PropUtil.getProperty("queryTotalCount"), queryBean, "bdp-data-service");
		if (retBean != null && retBean.getRet_code() != null && "0".equals(retBean.getRet_code())) {
			retBean.setPageSize(queryParams.getPageSize());
			int totalCount = 0;
			if (null != queryParams.getFirst_limit()
					&& Integer.parseInt(queryParams.getFirst_limit()) < retBean.getCount()) {
				totalCount = "".equals(queryParams.getFirst_limit()) ? 100
						: Integer.parseInt(queryParams.getFirst_limit());
			} else {
				totalCount = null != retBean.getCount() ? retBean.getCount() : 0;
			}
			int totalPage = (totalCount + queryParams.getPageSize() - 1) / queryParams.getPageSize();
			retBean.setTotal(totalCount);
			retBean.setTotalPage(totalPage);
		} else {
			retBean.setCurPage(0);
			retBean.setPageSize(0);
			retBean.setTotal(0);
			retBean.setTotalPage(0);
		}
		long s2 = System.currentTimeMillis();
		log.debug("getEasyQueryDataJson简易查询耗时时间：" + (s2 - s) + "ms ");
		return retBean;
	}

	@Loggable(Loggable.DEBUG)
	public RetBean queryTotal(QueryParams queryParams) throws Exception {
		long s = System.currentTimeMillis();
		QueryBean queryBean = CommonUtil.getCommonParam(queryParams);
		// 数据查询并返回
		RetBean retBean = CommonUtil.post(PropUtil.getProperty("queryTotal"), queryBean, "bdp-data-service");
		long s2 = System.currentTimeMillis();
		log.debug("getEasyQueryDataJson简易查询耗时时间：" + (s2 - s) + "ms ");
		return retBean;
	}

	@SuppressWarnings("unchecked")
	@Loggable(Loggable.DEBUG)
	public RetBean getSummaryEasyQueryDataJson(QueryParams queryParams) throws IOException {
		return null;

	}

	public List<Map<String, Object>> getUnitDefineData(List<String> inValues) {
		StringBuffer sb = new StringBuffer("SELECT * FROM unit_info_define bb WHERE bb.unit_id IN (");
		for (String id : inValues) {
			sb.append("'" + id + "',");
		}
		String unitDefineSql = sb.substring(0, sb.length() - 1) + ") ORDER BY bb.manage_level DESC";
		ExecuteBySqlBean executeBySqlBean = new ExecuteBySqlBean();
		executeBySqlBean.setDb_code(db_code);
		executeBySqlBean.setSql(unitDefineSql);
		RetBean unitDefineRet = CommonUtil.post(queryUrl + QUERYBYSQL, executeBySqlBean);
		List<Map<String, Object>> unitDefineList = unitDefineRet.getResults();
		return unitDefineList;
	}

	public Map<String, Object> getTotalMap(List<Map<String, Object>> resultList, QueryBean queryBean,
			QueryParams queryParams) {
		Map<String, BigDecimal> pageMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> totalMap = new HashMap<String, BigDecimal>();
		Map<String, Object> retmap = new HashMap<String, Object>();
		for (QueryBeanField entity : queryBean.getFields()) {
			if ("1".equals(entity.getIs_total_field())) {// 取出所有数值型
				String name = StringUtil.isNotEmpty(entity.getAlias()) ? entity.getAlias() : entity.getName();
				String fun = entity.getFunction();

				List<Map<String, Object>> pageList = resultList.subList(queryBean.getSkip(),
						queryBean.getSkip() + queryParams.getPageSize());
				for (Map<String, Object> totalDataMap : pageList) {// 存当前合计字段的值
					Object ob = totalDataMap.get(name);
					BigDecimal number = new BigDecimal(ob + "");
					if (null != pageMap.get(name)) {// 有就取出来加上
						BigDecimal tmp = pageMap.get(name);
						tmp = tmp.add(number);
						pageMap.put(name, tmp);
					} else {
						pageMap.put(name, number);
					}
				}
				if ("avg".equals(fun)) {// 小计要是平均
					BigDecimal avgNumber = pageMap.get(name);
					BigDecimal divisor = new BigDecimal(Double.toString(resultList.size()));
					pageMap.put(name, avgNumber.divide(divisor, 6));
				}

				for (Map<String, Object> totalDataMap : resultList) {// 存当前合计字段的值
					Object ob = totalDataMap.get(name);
					BigDecimal number = new BigDecimal(ob + "");
					if (null != totalMap.get(name)) {// 有就取出来加上
						BigDecimal tmp = totalMap.get(name);
						tmp = tmp.add(number);
						totalMap.put(name, tmp);
					} else {
						totalMap.put(name, number);
					}
				}
				if ("avg".equals(fun)) {// 小计要是平均
					BigDecimal avgNumber = totalMap.get(name);
					BigDecimal divisor = new BigDecimal(Double.toString(resultList.size()));
					totalMap.put(name, avgNumber.divide(divisor, 6));
				}

			}
		}
		retmap.put("pageTotal", pageMap);
		retmap.put("total", totalMap);
		return retmap;
	}

	/**
	 * 拼装返回的list
	 * 
	 * @param parentMap
	 * @param queryParams
	 * @param unitId
	 * @return
	 * @throws IOException
	 */
	public List<Map<String, Object>> getRetList(Map<String, List<Map<String, Object>>> parentMap,
			QueryParams queryParams, String unitId) throws IOException {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();// 返回list

		List<String> avgList = new ArrayList<String>();// 用来记录avg的字段
		for (QueryFields queryField : queryParams.getFields()) {// 遍历字段获取他们的函数
			String name = StringUtil.isNotEmpty(queryField.getAlias()) ? queryField.getAlias() : queryField.getName();
			String fun = queryField.getFunc();
			if ("avg".equals(fun)) {
				avgList.add(name);
			}
		}

		for (String parentId : parentMap.keySet()) {
			Map<String, Object> retMap = new HashMap<String, Object>();
			int subCount = 1;
			for (Map<String, Object> subMap : parentMap.get(parentId)) {// 根据4级id取出他们的子集数据

				for (QueryFields queryField : queryParams.getFields()) {// 遍历字段获取他们的函数
					String name = StringUtil.isNotEmpty(queryField.getAlias()) ? queryField.getAlias()
							: queryField.getName();
					Object ob = subMap.get(name);
					if ("1".equals(queryField.getIs_total_field())) {
						String fun = queryField.getFunc();

						BigDecimal number = new BigDecimal(ob + "");
						if (null != retMap.get(name)) {
							if ("sum".equals(fun) || "avg".equals(fun)) {
								BigDecimal tmp = (BigDecimal) retMap.get(name);
								tmp = tmp.add(number);
								retMap.put(name, tmp);
							} else if ("count".equals(fun)) {
								int count = (int) retMap.get(name);
								retMap.put(name, count + 1);
							}
						} else {
							if ("sum".equals(fun) || "avg".equals(fun)) {
								retMap.put(name, number);
							} else if ("count".equals(fun)) {
								retMap.put(name, 1);
							}
						}
					} else {
						retMap.put(name, ob);
					}

				}
				subCount++;
			}

			if (CommonUtil.listIsNotNull(avgList)) {
				for (String avgName : avgList) {
					BigDecimal number = new BigDecimal(subCount + "");
					BigDecimal tmp = (BigDecimal) retMap.get(avgName);
					tmp = tmp.divide(number, 6);
					retMap.put(avgName, tmp);
				}
			}

			retMap.put(unitId, parentId);
			resultList.add(retMap);
		}
		return resultList;
	}

	@Loggable(Loggable.DEBUG)
	public EasyQuery easyQueryTemplate(String query_template_id, String token) throws IOException {
		long gStartTime = System.currentTimeMillis();
		long startTime = 0;
		long endTime = 0;
		startTime = System.currentTimeMillis();
		BatchOperationBean batchOperationBean = new BatchOperationBean();
		List<QueryBean> queryBeans = new ArrayList<QueryBean>();
		EasyQuery easyQuery = new EasyQuery();
		// 请求bean
		QueryBean queryBean = new QueryBean();
		List<QueryBeanCondition> conditions = new ArrayList<QueryBeanCondition>();
		// 表
		queryBean.setTable(PropUtil.getProperty("query_template_metadataCode"));
		// 条件 查询模板
		QueryBeanCondition queryBeanCondition = new QueryBeanCondition();
		queryBeanCondition.setField1("id");
		queryBeanCondition.setCond("=");
		queryBeanCondition.setValue1(query_template_id);
		conditions.add(queryBeanCondition);
		queryBean.setConditions(conditions);

		// 查询模板
		queryBeans.add(queryBean);

		// 查询表
		selectQueryTables(query_template_id, queryBeans);

		// 查询字段
		selectQueryFields(query_template_id, queryBeans);

		// 查询条件
		selectQueryConditions(query_template_id, queryBeans);

		// 查询分组
		selectQueryGroups(query_template_id, queryBeans);

		batchOperationBean.setQueryBeans(queryBeans);
		endTime = System.currentTimeMillis();
		log.debug("gene batch query use time {}ms", endTime - startTime);

		startTime = System.currentTimeMillis();
		RetBean retBean = CommonUtil.post(PropUtil.getProperty("queryUrl1") + "/queryByList", batchOperationBean);
		endTime = System.currentTimeMillis();
		log.debug("get  batch query data use time {}ms", endTime - startTime);

		Map<String, List<Map<String, Object>>> map = retBean.getBatchResults();

		if (!"-1".equals(retBean.getRet_code())
				&& null != map.get(PropUtil.getProperty("query_template_metadataCode"))) {

			easyQuery = CommonUtil
					.mapsToObjs(map.get(PropUtil.getProperty("query_template_metadataCode")), EasyQuery.class).get(0);

			easyQuery.setTables(CommonUtil.mapsToObjs(map.get(PropUtil.getProperty("query_tables_metadataCode")),
					QueryTables.class));

			easyQuery.setFields(CommonUtil.mapsToObjs(map.get(PropUtil.getProperty("query_fields_metadataCode")),
					QueryFields.class));

			easyQuery.setConditions(getDefaultCondition(map.get(PropUtil.getProperty("query_condition_metadataCode")),
					query_template_id, token));

			easyQuery.setGroups(CommonUtil.mapsToObjs(map.get(PropUtil.getProperty("query_group_metadataCode")),
					QueryGroups.class));

			if ("true".equals(isFilter) && StringUtil.isNotEmpty(token)) {// 是否开启权限
				String filterSql = "SELECT ee.operation_code AS operation_code, ee.operation_name AS operation_name FROM"
						+ " auth_resource aa, auth_privilege bb, auth_privilege_operation cc, auth_token dd, auth_operation ee"
						+ " WHERE aa.resource_business_id = '" + query_template_id + "'"
						+ " AND aa.resource_id = bb.resource_id AND dd.token = '" + token + "'"
						+ " AND dd.user_id = bb.privilege_master_id AND cc.resource_id = aa.resource_id AND cc.privilege_id = bb.privilege_id AND ee. ID = cc.operation_id";
				ExecuteBySqlBean executeBySqlBean = new ExecuteBySqlBean();
				executeBySqlBean.setSql(filterSql);
				executeBySqlBean.setDb_code(db_code);
				startTime = System.currentTimeMillis();
				RetBean filteRet = CommonUtil.post(queryUrl + "/queryBySql", executeBySqlBean);
				endTime = System.currentTimeMillis();
				log.debug("run filterSql use time {}ms", endTime - startTime);
				List<Map<String, Object>> filteList = filteRet.getResults();
				List<AuthOperation> authOperations = CommonUtil.mapsToObjs(filteList, AuthOperation.class);
				easyQuery.setAuthOperations(authOperations);
			} else {
				List<AuthOperation> authOperations = new ArrayList<AuthOperation>();
				AuthOperation authOperation = new AuthOperation();
				authOperation.setOperation_code("close");// 用来表示关闭了权限开关
				authOperations.add(authOperation);
				easyQuery.setAuthOperations(authOperations);
			}
		}

		// 查询排序,暂时不实现
		long gEndTime = System.currentTimeMillis();
		log.debug("easyQueryTemplate use time {}ms", (gEndTime - gStartTime) + "ms");
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
					QueryConditions defaultConditions = PermissionUtil.getDefaultCondition(queryParams);
					queryConditions.setField1(defaultConditions.getField1());
					queryConditions.setValue1(defaultConditions.getValue1());
					queryConditions.setValfield(defaultConditions.getValfield());
					resultList.add(queryConditions);
				} else {
					resultList.add(queryConditions);
				}
			}
		}
		return resultList;
	}

	public void selectQueryTables(String query_template_id, List<QueryBean> queryBeans) throws IOException {
		// 请求bean
		QueryBean queryBean = new QueryBean();
		List<QueryBeanCondition> conditions = new ArrayList<QueryBeanCondition>();

		// 表
		queryBean.setTable(PropUtil.getProperty("query_tables_metadataCode"));

		// 条件 查询模板
		QueryBeanCondition queryBeanCondition = new QueryBeanCondition();
		queryBeanCondition.setField1("query_template_id");
		queryBeanCondition.setCond("=");
		queryBeanCondition.setValue1(query_template_id);
		conditions.add(queryBeanCondition);
		queryBean.setConditions(conditions);

		queryBeans.add(queryBean);
	}

	public void selectQueryGroups(String query_template_id, List<QueryBean> queryBeans) throws IOException {
		// 请求bean
		QueryBean queryBean = new QueryBean();
		List<QueryBeanCondition> conditions = new ArrayList<QueryBeanCondition>();
		// 表
		queryBean.setTable(PropUtil.getProperty("query_group_metadataCode"));
		// 条件 查询模板
		QueryBeanCondition queryBeanCondition = new QueryBeanCondition();
		queryBeanCondition.setField1("query_template_id");
		queryBeanCondition.setCond("=");
		queryBeanCondition.setValue1(query_template_id);
		conditions.add(queryBeanCondition);
		queryBean.setConditions(conditions);

		queryBeans.add(queryBean);
	}

	public void selectQueryFields(String query_template_id, List<QueryBean> queryBeans) throws IOException {
		// 请求bean
		QueryBean queryBean = new QueryBean();
		List<QueryBeanCondition> conditions = new ArrayList<QueryBeanCondition>();
		// 表
		queryBean.setTable(PropUtil.getProperty("query_fields_metadataCode"));
		// 条件 查询模板
		QueryBeanCondition queryBeanCondition = new QueryBeanCondition();
		queryBeanCondition.setField1("query_template_id");
		queryBeanCondition.setCond("=");
		queryBeanCondition.setValue1(query_template_id);
		conditions.add(queryBeanCondition);
		queryBean.setConditions(conditions);

		// 排序
		List<QueryBeanOrder> queryBeanOrders = new ArrayList<QueryBeanOrder>();
		QueryBeanOrder queryBeanOrder = new QueryBeanOrder();
		queryBeanOrder.setField("order_index");// 按字段排序
		queryBeanOrder.setType("ASC");
		queryBeanOrders.add(queryBeanOrder);
		queryBean.setOrders(queryBeanOrders);

		queryBeans.add(queryBean);
	}

	public void selectQueryConditions(String query_template_id, List<QueryBean> queryBeans) throws IOException {
		// 请求bean
		QueryBean queryBean = new QueryBean();
		List<QueryBeanCondition> conditions = new ArrayList<QueryBeanCondition>();
		// 表
		queryBean.setTable(PropUtil.getProperty("query_condition_metadataCode"));
		// 条件 查询模板
		QueryBeanCondition queryBeanCondition = new QueryBeanCondition();
		queryBeanCondition.setField1("query_template_id");
		queryBeanCondition.setCond("=");
		queryBeanCondition.setValue1(query_template_id);
		conditions.add(queryBeanCondition);
		queryBean.setConditions(conditions);

		List<QueryBeanOrder> orders = new ArrayList<QueryBeanOrder>();
		QueryBeanOrder queryBeanOrder = new QueryBeanOrder();
		queryBeanOrder.setField("order_index");
		queryBeanOrder.setType("ASC");
		orders.add(queryBeanOrder);
		queryBean.setOrders(orders);
		queryBeans.add(queryBean);
	}

	public RetBean selectTemplate(QueryParams queryParams, boolean isHavPrivilege) throws IOException {
		// 查询bean
		QueryBean queryBean = new QueryBean();
		RetBean retBean = new RetBean();
		// 排序
		List<QueryBeanOrder> queryBeanOrders = new ArrayList<QueryBeanOrder>();
		queryBeanOrders = CommonUtil.ordersChangForm(queryParams.getOrders());
		QueryBeanOrder queryBeanOrder = new QueryBeanOrder();
		queryBeanOrder.setField("create_time");// 按创建时间排序
		queryBeanOrder.setType("DESC");
		queryBeanOrders.add(queryBeanOrder);
		queryBean.setOrders(queryBeanOrders);
		// 页数页码
		if (queryParams.getPageSize() != null) {
			queryBean.setLimit(queryParams.getPageSize());
		}
		if (queryParams.getCurPage() != null) {
			queryBean.setSkip(queryParams.getPageSize() * (queryParams.getCurPage() - 1));
		}
		List<QueryBeanCondition> conditions = new ArrayList<QueryBeanCondition>();
		// 获取模版列表的权限过滤资源id
		String token = queryParams.getToken();
		if ("true".equals(isFilter)) {
			if (StringUtil.isNotEmpty(token) && isHavPrivilege) {
				String filterSql = "SELECT cc.resource_id as resource_id FROM auth_privilege aa, auth_token bb, auth_resource cc WHERE aa.privilege_master_id = bb.user_id AND aa.resource_id = cc.resource_id AND cc.resource_type = 'easyquery' AND bb.token = '"
						+ token + "'";
				ExecuteBySqlBean executeBySqlBean = new ExecuteBySqlBean();
				executeBySqlBean.setSql(filterSql);
				executeBySqlBean.setDb_code(db_code);
				RetBean filteRet = CommonUtil.post(queryUrl + "/queryBySql", executeBySqlBean);
				List<Map<String, Object>> filteList = filteRet.getResults();
				String[] resourceId = new String[filteList.size()];
				// 如果权限为空，则不执行查询条件，直接返回空
				if (filteList == null || filteList.size() <= 0) {
					retBean.setRet_code("0");
					return retBean;
				}
				for (int i = 0; i < filteList.size(); i++) {
					resourceId[i] = filteList.get(i).get("resource_id") + "";
				}
				QueryBeanCondition filteCondition = new QueryBeanCondition();
				filteCondition.setField1("id");
				filteCondition.setCond("in");
				filteCondition.setValue1(Arrays.asList(resourceId));
				conditions.add(filteCondition);
			}
		}
		if (CommonUtil.listIsNotNull(queryParams.getConditions())) {
			for (QueryConditions bean : queryParams.getConditions()) {
				QueryBeanCondition condition = new QueryBeanCondition();
				condition.setField1(bean.getField1());
				condition.setCond(bean.getCond());
				condition.setValue1(bean.getValue1());
				conditions.add(condition);
			}
		}

		queryBean.setConditions(conditions);
		queryBean.setTable(PropUtil.getProperty("query_template_metadataCode"));
		queryBean.setIf_count(true);
		retBean = CommonUtil.post(queryBean);
		retBean.setCurPage(queryParams.getCurPage());
		retBean.setPageSize(queryParams.getPageSize());
		int totalCount = retBean.getCount();
		int totalPage = (totalCount + queryParams.getPageSize() - 1) / queryParams.getPageSize();
		retBean.setTotal(totalCount);
		retBean.setTotalPage(totalPage);
		return retBean;
	}

	@Override
	public RetBean selectTemplateTree(QueryParams queryParams) throws Exception {
		RetBean ret = selectTemplate(queryParams, true);
		List<Map<String, Object>> list = ret.getResults();
		Map<String, String> multiValueMap = new HashMap<String, String>();
		multiValueMap.put("metadataCode", "metaqueryType");
		multiValueMap.put("level", "c");
		String jsonString = BHU.postRequst(PropUtil.getProperty("selectMetadataJson"), multiValueMap);
		JSONObject jsonObject = JSONObject.parseObject(jsonString);
		JSONObject metadataList = jsonObject.getJSONObject("metadataList");
		JSONArray jsonArray = metadataList.getJSONArray("children");
		List<Map<String, Object>> tree = new ArrayList<Map<String, Object>>();
		for (Object json : jsonArray) {
			JSONObject json2 = (JSONObject) json;
			Map<String, Object> treeMap = new LinkedHashMap<String, Object>();
			String keyString = json2.get("metadataId").toString();
			treeMap.put("value", json2.get("metadataId").toString());
			treeMap.put("id", json2.get("metadataId"));
			treeMap.put("url", "");
			treeMap.put("label", json2.get("metadataName").toString());
			List<Map<String, Object>> childrens = new ArrayList<Map<String, Object>>();
			if (CommonUtil.listIsNotNull(list)) {
				for (Map<String, Object> map : list) {
					if (keyString.equals(map.get("query_type"))) {
						Map<String, Object> node = new HashMap<String, Object>();
						node.put("value", map.get("id"));
						node.put("id", map.get("id"));
						node.put("url", "");
						node.put("label", map.get("query_name").toString());
						childrens.add(node);
					}
				}
			}
			treeMap.put("childrens", childrens);
			tree.add(treeMap);
		}
		RetBean result = new RetBean();
		result.setResults(tree);
		return result;
	}

	@Override
	public String exportEasyqueryData(QueryParams queryParams) throws Exception {
		Long maxSize = Long.parseLong(exportMax);
		queryParams.setPageSize(maxSize.intValue());
		queryParams.setCurPage(null);

		if (null != queryParams.getFirst_limit()) {
			int limit = "".equals(queryParams.getFirst_limit()) ? 100 : Integer.parseInt(queryParams.getFirst_limit());
			queryParams.setPageSize(limit);
		}

		RetBean retBean = queryDataList(queryParams);
		List<Map<String, Object>> exportList = retBean.getResults();

		long exportListSize = exportList.size();
		String retPath = "";
		if (!(exportListSize >= maxSize) || !(exportListSize > 1048576)) {// 判断当前数据是否超过配置，或者超过最大条数
			retPath = exportFileService.geneExportFile(queryParams, exportList);
		}
		return retPath;
	}
}