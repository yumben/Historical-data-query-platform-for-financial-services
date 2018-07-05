package cn.com.infohold.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.alibaba.fastjson.JSON;

import bdp.commons.dataservice.param.QueryBean;
import bdp.commons.dataservice.param.QueryBeanCondition;
import bdp.commons.dataservice.param.QueryBeanField;
import bdp.commons.dataservice.param.QueryBeanGroup;
import bdp.commons.dataservice.param.QueryBeanOrder;
import bdp.commons.dataservice.ret.RetBean;
import bdp.commons.easyquery.param.QueryParams;
import bdp.commons.easyquery.ret.QueryConditions;
import bdp.commons.easyquery.ret.QueryFields;
import bdp.commons.easyquery.ret.QueryGroups;
import bdp.commons.easyquery.ret.QueryOrder;
import bdp.commons.easyquery.ret.QueryTables;
import cn.com.infohold.basic.util.file.PropUtil;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.tools.util.StringUtil;
import cn.easybdp.basic.util.http.BasicHttpUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CommonUtil {

	private static final BasicJsonUtil BJU = BasicJsonUtil.getInstance();
	private static final BasicHttpUtil BHU = BasicHttpUtil.getInstance();
	private static boolean isRightFormat = true;

	public static RetBean post(Object object) {
		long s = System.currentTimeMillis();
		RetBean retBean = new RetBean();
		try {
			String url = PropUtil.getProperty("queryUrl");
			String values = "";
			values = BJU.toJsonString(object);
			Map<String, String> param = new HashMap<>();
			param.put("param", values);
			String rets = BHU.postRequst(url, param);
			retBean = BJU.toJavaBean(rets, RetBean.class);
		} catch (IOException e) {
			e.printStackTrace();
			retBean.setRet_code("-1");
			retBean.setRet_message(e.getMessage());
		}

		long s2 = System.currentTimeMillis();
		log.debug("post简易查询耗时时间：" + (s2 - s) + "ms ");
		return retBean;
	}

	public static RetBean post(String url, Object object) {
		long s = System.currentTimeMillis();
		RetBean retBean = new RetBean();
		try {
			String values = "";
			values = BJU.toJsonString(object);
			Map<String, String> param = new HashMap<>();
			param.put("param", values);
			String rets = BHU.postRequst(url, param);
			retBean = BJU.toJavaBean(rets, RetBean.class);
		} catch (IOException e) {
			e.printStackTrace();
			retBean.setRet_code("-1");
			retBean.setRet_message(e.getMessage());
		}
		long s2 = System.currentTimeMillis();
		log.debug("post简易查询耗时时间：" + (s2 - s) + "ms ");
		return retBean;
	}

	/**
	 * 传访问的服务名供日志打印
	 * 
	 * @param url
	 * @param object
	 * @param serviceName
	 * @return
	 */
	public static RetBean post(String url, Object object, String serviceName) {
		long s = System.currentTimeMillis();
		RetBean retBean = new RetBean();
		try {
			String values = "";
			values = BJU.toJsonString(object);
			Map<String, String> param = new HashMap<>();
			param.put("param", values);
			String rets = BHU.postRequst(url, param);
			retBean = BJU.toJavaBean(rets, RetBean.class);
		} catch (IOException e) {
			e.printStackTrace();
			retBean.setRet_code("-1");
			retBean.setRet_message(e.getMessage());
		}
		long s2 = System.currentTimeMillis();
		log.debug("post简易查询 " + serviceName + " 耗时时间：" + (s2 - s) + "ms ");
		return retBean;
	}

	/**
	 * QueryFields 转换成 QueryBeanField
	 * 
	 * @param fieldsList
	 * @return
	 */
	public static List<QueryBeanField> fieldChangForm(QueryParams queryParams) {
		List<QueryFields> fieldsList = queryParams.getFields();
		List<QueryBeanField> fields = new ArrayList<QueryBeanField>();
		boolean is_compute = false;
		if (fieldsList != null && fieldsList.size() > 0) {
			for (QueryFields queryFields : fieldsList) {
				QueryBeanField queryBeanField = new QueryBeanField();
				queryBeanField.setName(queryFields.getName());
				queryBeanField.setAlias(queryFields.getAlias());
				queryBeanField.setTitle_name(queryFields.getTitle_name());

				if ("true".equals(queryFields.getIs_compute())) {// 有计算字段就要重新拼
					is_compute = true;
					fields.clear();
					break;
				} else {
					if (PropUtil.getProperty("notempty").equals(queryFields.getFunc())) {
						queryBeanField.setFunction(PropUtil.getProperty("notempty"));
					} else if (PropUtil.getProperty("nonemptycount").equals(queryFields.getFunc())) {
						// 非空计数
						queryBeanField.setFunction(PropUtil.getProperty("nonemptycount"));
					} else {
						queryBeanField.setFunction(queryFields.getFunc());
					}

					queryBeanField.setFiled_type(queryFields.getField_type());
					queryBeanField.setIs_total_field(queryFields.getIs_total_field());
					fields.add(queryBeanField);
				}
			}

			if (is_compute) {// 有计算字段就要重新拼所有的字段进去查
				List<QueryFields> allFieldsList = queryParams.getAll_fields();

				if (allFieldsList != null && allFieldsList.size() > 0) {
					for (QueryFields queryFields : allFieldsList) {

						if ("false".equals(queryFields.getIs_compute())) {
							QueryBeanField queryBeanField = new QueryBeanField();
							queryBeanField.setName(queryFields.getName());
							queryBeanField.setAlias(queryFields.getAlias());
							queryBeanField.setTitle_name(queryFields.getTitle_name());
							if (PropUtil.getProperty("notempty").equals(queryFields.getFunc())) {
								queryBeanField.setFunction(PropUtil.getProperty("notempty"));
							} else if (PropUtil.getProperty("nonemptycount").equals(queryFields.getFunc())) {
								// 非空计数
								queryBeanField.setFunction(PropUtil.getProperty("nonemptycount"));
							} else {
								queryBeanField.setFunction(queryFields.getFunc());
							}

							queryBeanField.setFiled_type(queryFields.getField_type());
							queryBeanField.setIs_total_field(queryFields.getIs_total_field());
							fields.add(queryBeanField);
						}
					}

				}
			}
		}

		return fields;
	}

	/**
	 * QueryFields 转换成 QueryBeanField(汇总)
	 * 
	 * @param fieldsList
	 * @return
	 */
	public static List<QueryBeanField> fieldChangSummaryForm(List<QueryFields> fieldsList) {
		List<QueryBeanField> fields = new ArrayList<QueryBeanField>();
		if (fieldsList != null && fieldsList.size() > 0) {
			for (QueryFields queryFields : fieldsList) {
				QueryBeanField queryBeanField = new QueryBeanField();
				queryBeanField.setName(queryFields.getName());
				queryBeanField.setAlias(queryFields.getAlias());
				queryBeanField.setTitle_name(queryFields.getTitle_name());
				queryBeanField.setFiled_type(queryFields.getField_type());
				queryBeanField.setIs_total_field(queryFields.getIs_total_field());
				fields.add(queryBeanField);
			}
		}

		return fields;
	}

	/**
	 * QueryConditions 转换成 QueryBeanCondition
	 * 
	 * @param conditionsList
	 * @return
	 */
	public static List<QueryBeanCondition> conditionsChangForm(List<QueryConditions> conditionsList) {
		List<QueryBeanCondition> conditions = new ArrayList<QueryBeanCondition>();
		if (conditionsList != null && conditionsList.size() > 0) {
			for (QueryConditions queryConditions : conditionsList) {

				if (StringUtil.isNotEmpty(queryConditions.getField2())
						|| StringUtil.isNotEmpty(queryConditions.getValue1() + "")) {
					QueryBeanCondition queryBeanCondition = new QueryBeanCondition();
					queryBeanCondition.setField1(queryConditions.getField1());
					queryBeanCondition.setCond(queryConditions.getCond());
					queryBeanCondition.setValue1(queryConditions.getValue1());
					queryBeanCondition.setFun1(queryConditions.getFun1());
					queryBeanCondition.setField2(queryConditions.getField2());
					queryBeanCondition.setValue2(queryConditions.getValue2());
					queryBeanCondition.setFun2(queryConditions.getFun2());

					conditions.add(queryBeanCondition);
				}
			}
		}
		return conditions;
	}

	/**
	 * QueryGroups 转换成 QueryBeanGroup
	 * 
	 * @param queryGroupsList
	 * @return
	 */
	public static Map<String, Object> groupsChangForm(List<QueryGroups> queryGroupsList,
			List<QueryBeanField> fieldsList) {
		List<QueryBeanGroup> queryBeanGroups = new ArrayList<QueryBeanGroup>();
		Map<String, Object> retMap = new HashMap<String, Object>();
		if (queryGroupsList != null && queryGroupsList.size() > 0) {
			for (QueryGroups queryGroups : queryGroupsList) {
				QueryBeanGroup queryBeanGroup = new QueryBeanGroup();
				queryBeanGroup.setField(queryGroups.getField());
				queryBeanGroup.setFunction(queryGroups.getFun());
				queryBeanGroups.add(queryBeanGroup);
				if (StringUtil.isNotEmpty(queryGroups.getField())) {
					QueryBeanField queryFields = new QueryBeanField();
					queryFields.setName(queryGroups.getField());
					// queryFields.setFunction("group");
					queryFields.setTitle_name(queryGroups.getField_name());
					queryFields.setDimension(true);
					fieldsList.add(queryFields);
				}
			}
			retMap.put("queryBeanGroups", queryBeanGroups);
		}

		// 分组字段也放到展示字段
		retMap.put("fieldsList", fieldsList);
		return retMap;
		// return queryBeanGroups;
	}

	public static <T> List<T> mapsToObjs(List<Map<String, Object>> list, Class<T> clazz) throws IOException {
		List<T> resultList = new ArrayList<T>();
		if (listIsNotNull(list)) {
			for (Map<String, Object> map : list) {
				String json = JSON.toJSONString(map);
				resultList.add(JSON.parseObject(json, clazz));
			}
		}

		return resultList;
	}

	public static <T> boolean listIsNotNull(List<T> list) throws IOException {
		boolean flag = false;
		if (null != list && !list.isEmpty()) {
			flag = true;
		}
		return flag;
	}

	/**
	 * QueryOrder 转换成 QueryBeanOrder
	 * 
	 * @param fieldsList
	 * @return
	 */
	public static List<QueryBeanOrder> ordersChangForm(List<QueryOrder> ordersList) {
		List<QueryBeanOrder> queryBeanOrders = new ArrayList<QueryBeanOrder>();
		if (ordersList != null && ordersList.size() > 0) {
			for (QueryOrder queryOrders : ordersList) {
				QueryBeanOrder queryBeanOrder = new QueryBeanOrder();
				queryBeanOrder.setField(queryOrders.getField());
				queryBeanOrder.setType(queryOrders.getOrder_type());
				queryBeanOrder.setAlias(queryOrders.getAlias());
				queryBeanOrders.add(queryBeanOrder);
			}
		}

		return queryBeanOrders;
	}

	public static List<QueryBeanCondition> getFieldCondition(List<QueryFields> fields) {
		List<QueryBeanCondition> conditions = new ArrayList<QueryBeanCondition>();
		if (fields != null && fields.size() > 0) {
			for (QueryFields queryField : fields) {
				if (StringUtil.isNotEmpty(queryField.getFunc())
						&& (PropUtil.getProperty("notempty").equals(queryField.getFunc())
								|| PropUtil.getProperty("nonemptycount").equals(queryField.getFunc()))) {
					QueryBeanCondition condition = new QueryBeanCondition();
					condition.setField1(queryField.getName());
					condition.setCond("!=");
					condition.setValue1("null");
					conditions.add(condition);
					condition = new QueryBeanCondition();
					condition.setField1(queryField.getName());
					condition.setCond("is not null");
					conditions.add(condition);
					condition = new QueryBeanCondition();
					condition.setField1(queryField.getName());
					condition.setCond("!=");
					condition.setValue1("");
					conditions.add(condition);
				}
			}

		}
		return conditions;
	}

	public static List<String> sortStringArray(String s) {
		// 将字符串进行分割，转成字符串数组
		String[] c = s.split(",");
		int[] array = new int[c.length];
		for (int i = 0; i < c.length; i++) {
			// 将字符串中的元素转成int数据类型并储存到int数组中去
			array[i] = Integer.parseInt(c[i]);
		}
		// 对int数组中的元素进行排序
		Arrays.sort(array);
		int temp;
		for (int i = 0; i < array.length; i++) {
			for (int j = i + 1; j < array.length; j++) {
				if (array[i] < array[j]) {
					temp = array[i];
					array[i] = array[j];
					array[j] = temp; // 两个数交换位置
				}
			}
		}
		// 将int数组转换为字符串输出
		return intArrayToString(array);
	}

	// 将int数组中的元素转成字符串并输出
	private static List<String> intArrayToString(int[] arr) {
		List<String> sb = new ArrayList<String>();
		for (int i = 0; i < arr.length; i++) {
			sb.add(arr[i] + "");
		}
		return sb;
	}

	public static Object getResult(String formula) throws ScriptException {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		String js = formula;
		String rte = engine.eval(js) + "";
		// log.info("rte = " + rte);
		// if ("Infinity".equals(rte)) {
		// rte = "0";
		// }
		if (!isNumeric(rte)) {
			rte = "0";
			return rte;
		} else {
			BigDecimal bd1 = new BigDecimal(rte);
			return bd1.toPlainString();

		}

		// BigDecimal bd = new BigDecimal(rte);
		// Double resultDouble = Double.parseDouble(bd.setScale(2,
		// BigDecimal.ROUND_HALF_UP).toPlainString());
		// return bd.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
	}

	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				if (!"E".equals(str.charAt(i) + "")) {// 科学计数法也要判断
					if (!".".equals(str.charAt(i) + "") && !"-".equals(str.charAt(i) + "")) {// 小数点和负数也要判断
						return false;
					}

				}
			}
		}
		return true;
	}

	public static QueryBean getCommonParam(QueryParams queryParams) throws Exception {

		List<QueryTables> queryTables = selectQueryTables(queryParams.getQuery_template_id());

		// 查询bean
		QueryBean queryBean = new QueryBean();
		// 查询bean字段list
		List<QueryBeanField> fields = new ArrayList<QueryBeanField>();
		// 查询bean条件list
		List<QueryBeanCondition> conditions = new ArrayList<QueryBeanCondition>();
		// 查询bean分组List
		List<QueryBeanGroup> queryBeanGroups = new ArrayList<QueryBeanGroup>();
		// 排序列表
		List<QueryBeanOrder> queryBeanOrders = new ArrayList<QueryBeanOrder>();

		String tableName = "";
		if (queryTables != null && queryTables.size() > 0) {
			// 根据元数据ID查询表所对应的元数据Code
			String metadataId = queryTables.get(0).getMetadata_id();
			Map<String, String> map = new HashMap<String, String>();
			map.put("metadataId", metadataId);
			long s = System.currentTimeMillis();
			String jsonString = BHU.postRequst(PropUtil.getProperty("metadataUrl"), map);
			long s1 = System.currentTimeMillis();
			log.debug("查询 bdp-metadata-service 耗时 ：{}", (s1 - s));
			Map<Object, Object> retMap = BJU.toJsonMap(jsonString);
			if (retMap != null) {
				tableName = (String) retMap.get("metadataCode");
			}
		}
		if (StringUtil.isEmpty(tableName)) {
			return null;
		}
		queryBean.setTable(tableName);

		// 组装条件
		conditions = CommonUtil.conditionsChangForm(queryParams.getConditions());

		List<QueryBeanCondition> datapList = PermissionUtil.getDataPermission(queryParams);

		String unitId = null != queryParams.getUnitId() ? queryParams.getUnitId() : "";

		if (CommonUtil.listIsNotNull(datapList)) {
			unitId = datapList.get(0).getField1();
			// if ("false".equals(queryParams.getIs_remove())) {
			// removeUnit(datapList, unitId);// 去除全行in
			// }
		}

		// removeUnit(conditions, unitId);// 去除全行in
		conditions.addAll(datapList);

		queryBean.setConditions(conditions);

		// 组装字段
		fields = CommonUtil.fieldChangForm(queryParams);
		queryBean.setFields(fields);

		Map<String, Object> map = CommonUtil.groupsChangForm(queryParams.getGroups(), fields);
		if (map != null) {
			queryBeanGroups = (List<QueryBeanGroup>) map.get("queryBeanGroups");
			fields = (List<QueryBeanField>) map.get("fieldsList");
		}
		queryBean.setFields(fields);
		queryBean.setGroups(queryBeanGroups);

		// 页数页码
		if (queryParams.getPageSize() != null) {
			queryBean.setLimit(queryParams.getPageSize());
		}
		if (queryParams.getCurPage() != null) {
			queryBean.setSkip(queryParams.getPageSize() * (queryParams.getCurPage() - 1));
		}
		if (queryParams.getFirst_limit() != null) {
			queryBean.setFirst_limit(queryParams.getFirst_limit());
		}

		// 返回总行数
		queryBean.setIf_count(true);
		// 排序
		queryBeanOrders = CommonUtil.ordersChangForm(queryParams.getOrders());
		queryBean.setOrders(queryBeanOrders);

		return queryBean;
	}

	@SuppressWarnings("unchecked")
	public static void removeUnit(List<QueryBeanCondition> conditions, String unitId) {
		for (QueryBeanCondition queryBeanCondition : conditions) {
			if (unitId.equals(queryBeanCondition.getField1())) {
				List<String> unitList = (List<String>) queryBeanCondition.getValue1();

				if (unitList.contains("01000") || unitList.contains("01109")) {
					conditions.remove(queryBeanCondition);// 去除全行in
					break;
				}
			}
		}
	}

	public static List<QueryTables> selectQueryTables(String query_template_id) throws IOException {
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

		// 查询表
		RetBean retBean = CommonUtil.post(PropUtil.getProperty("queryUrl"), queryBean, "bdp-data-service");
		List<QueryTables> listq = new ArrayList<QueryTables>();
		if (retBean != null) {
			List<Map<String, Object>> list = retBean.getResults();
			listq = CommonUtil.mapsToObjs(list, QueryTables.class);
		}
		return listq;
	}
}
