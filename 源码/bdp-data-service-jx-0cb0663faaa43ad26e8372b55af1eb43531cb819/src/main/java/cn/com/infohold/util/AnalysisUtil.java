package cn.com.infohold.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.bson.BsonDocument;

import com.alibaba.fastjson.JSON;

import bdp.commons.dataservice.config.ConditionBean;
import bdp.commons.dataservice.config.ConfigureBean;
import bdp.commons.dataservice.config.ConfigureBeanConds;
import bdp.commons.dataservice.param.UpdateBean;
import cn.com.infohold.basic.util.file.PropUtil;
import cn.com.infohold.basic.util.jdbc.JdbcConBean;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.tools.util.StringUtil;
import java.util.concurrent.ForkJoinPool;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AnalysisUtil {
	public static final Integer DefaultParallelMax = 500;
	private static ForkJoinPool FJP = null;
	static {
		String parallelMaxString = PropUtil.getProperty("parallelMax");
		Integer parallelMax = DefaultParallelMax;
		try {
			parallelMax = Integer.parseInt(parallelMaxString);
		} catch (Exception ex) {
			log.warn(" parallelMaxString={}", parallelMaxString);
			log.warn(" parallelMax={}", parallelMax);
		}
		log.debug(" parallelMax={}", parallelMax);
		FJP = new ForkJoinPool(parallelMax);
	}

	public static ForkJoinPool getForkJoinPool() {
		return FJP;
	}

	private static final BasicJsonUtil BJU = BasicJsonUtil.getInstance();

	public static JdbcConBean setJdbcBeanSql(Map<String, Object> dataBaseInfoMap) {
		JdbcConBean bean = new JdbcConBean();
		String jdbcUrl = "";
		String db_name = (String) dataBaseInfoMap.get("db_name");
		String jdbc_url = dataBaseInfoMap.get("jdbc_url") + "";
		if (StringUtil.isNotEmpty(jdbc_url) && !"null".equals(jdbc_url)) {
			jdbcUrl = jdbc_url;
		} else if (dataBaseInfoMap.containsKey("db_url")) {
			if ("mysql".equals(dataBaseInfoMap.get("database_type"))) {
				jdbcUrl = "jdbc:mysql://" + dataBaseInfoMap.get("db_url") + "/" + db_name;
			} else if ("oracle".equals(dataBaseInfoMap.get("database_type"))) {
				jdbcUrl = "jdbc:oracle:thin:" + dataBaseInfoMap.get("db_url") + ":" + db_name;
			} else if ("postgresql".equals(dataBaseInfoMap.get("database_type"))) {
				jdbcUrl = "jdbc:postgresql://" + dataBaseInfoMap.get("db_url") + "/" + db_name;
			}
		} else {
			if ("mysql".equals(dataBaseInfoMap.get("db_type"))) {
				jdbcUrl = "jdbc:mysql://" + dataBaseInfoMap.get("db_host") + ":" + dataBaseInfoMap.get("db_port") + "/"
						+ db_name;
			} else if ("oracle".equals(dataBaseInfoMap.get("db_type"))) {
				jdbcUrl = "jdbc:oracle:thin:" + dataBaseInfoMap.get("db_host") + ":" + dataBaseInfoMap.get("db_port")
						+ ":" + db_name;
			} else if ("postgresql".equals(dataBaseInfoMap.get("db_type"))) {
				jdbcUrl = "jdbc:postgresql://" + dataBaseInfoMap.get("db_host") + ":" + dataBaseInfoMap.get("db_port")
						+ "/" + db_name;
			}
		}
		bean.setJdbcDriver(dataBaseInfoMap.get("db_driver").toString());
		bean.setJdbcUserName(dataBaseInfoMap.get("db_user").toString());
		bean.setJdbcURL(jdbcUrl);
		bean.setJdbcPassword(dataBaseInfoMap.get("db_password").toString());
		return bean;
	}

	public static <T> T toQueryBean(String json, Class<T> class1) {
		T bean = null;
		try {
			bean = class1.newInstance();
			bean = BasicJsonUtil.getInstance().toJavaBean(json, class1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	};

	/**
	 * 获得过滤条件
	 *
	 * @param queryBean
	 * @return
	 */
	public static BsonDocument conditionMosaicToBson(List<ConditionBean> conditions,
			List<ConfigureBeanConds> configureBeanConds) throws Exception {
		if (conditions == null) {
			return null;
		}
		BsonDocument filter = new BsonDocument();
		List<Object> tmps = new ArrayList<>();
		for (ConditionBean conditionBean : conditions) {
			HashMap<String, Object> jsonObj = new HashMap<>();
			getConditions(conditionBean, jsonObj, null, configureBeanConds);
			tmps.add(jsonObj);
		}
		Map<String, Object> and = new HashMap<>();
		and.put("$and", tmps);
		String andString = BJU.toJsonString(and);
		filter.putAll(BsonDocument.parse(andString));
		log.debug(filter);
		return filter;
	}

	/**
	 * 递归出条件组
	 *
	 * @param queryBeanCondition
	 * @param jsonObj
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static void getConditions(ConditionBean conditionBean, HashMap<String, Object> jsonObj, String type,
			List<ConfigureBeanConds> configureBeanConds) throws Exception {
		List<ConditionBean> andList = conditionBean.getAnd();
		List<ConditionBean> orList = conditionBean.getOr();

		if (andList != null && andList.size() > 0) {// 判断条件组是不是只有一个
			for (ConditionBean and : andList) {
				getConditions(and, jsonObj, "and", configureBeanConds);
			}
		} else if (orList != null && orList.size() > 0) {
			for (ConditionBean or : orList) {
				getConditions(or, jsonObj, "or", configureBeanConds);
			}
		} else if ("and".equals(type)) {// 如果是and节点
			HashMap<String, Object> condJson = parseCondition(conditionBean, configureBeanConds);
			String field1 = conditionBean.getField1();
			HashMap<String, Object> oldJson = (HashMap<String, Object>) jsonObj.get(field1);
			if (oldJson != null) {
				oldJson.putAll((HashMap<String, Object>) condJson.get(field1));
				jsonObj.put(field1, oldJson);
			} else {
				jsonObj.putAll(condJson);
			}
		} else if ("or".equals(type)) {// 如果是or节点
			List<HashMap<String, Object>> jsonList = (List<HashMap<String, Object>>) jsonObj.get("$or");
			if (jsonList == null) {
				jsonList = new ArrayList<HashMap<String, Object>>();
			}
			HashMap<String, Object> condJson = parseCondition(conditionBean, configureBeanConds);
			jsonList.add(condJson);
			jsonObj.put("$or", jsonList);
		} else {// 没有节点
			jsonObj.putAll(parseCondition(conditionBean, configureBeanConds));
		}
	}

	/**
	 * 解析条件节点数据
	 *
	 * @param ele
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> parseCondition(ConditionBean conditionBean,
			List<ConfigureBeanConds> configureBeanConds) throws Exception {
		String cond = transformation(conditionBean.getCond(), configureBeanConds);// 转换逻辑符号
		String field1 = conditionBean.getField1();
		Object value1 = conditionBean.getValue1();
		String field2 = conditionBean.getField2();
		HashMap<String, Object> condJson = new HashMap<String, Object>();
		if ("between".equals(cond)) {
			Object value2 = conditionBean.getValue2();
			HashMap<String, Object> json = new HashMap<String, Object>();
			json.put("$gte", value1);
			json.put("$lte", value2);
			condJson.put(field1, json);
		} else if ("like".equals(cond)) {
			// String value = value1.toString().replace("%", "/");
			// if (value.indexOf("/") != 0) {
			// value = "/" + value;
			// }
			// if (value.lastIndexOf("/") != value.length() - 1) {
			// value = value + "/";
			// }
			String value = Objects.toString(value1);
			// 正则表达式开始
			value = "^" + value + "$";
			value = value.replace("%", ".{0,}");
			HashMap<String, Object> like = new HashMap<String, Object>();
			like.put("$regex", value);
			condJson.put(field1, like);
		} else if ("not like".equals(cond)) {
			// String value = value1.toString().replace("%", "/");
			// if (value.indexOf("/") != 0) {
			// value = "/" + value;
			// }
			// if (value.lastIndexOf("/") != value.length() - 1) {
			// value = value + "/";
			// }
			// $regex
			String value = value1.toString().replace("%", "");
			HashMap<String, Object> like = new HashMap<String, Object>();
			like.put("$regex", value);
			HashMap<String, Object> tmpJson = new HashMap<String, Object>();
			tmpJson.put("$not", like);
			condJson.put(field1, tmpJson);
		} else if ("$in".equals(cond) || "$nin".equals(cond)) {
			// String value[] = value1.toString().substring(1,
			// value1.toString().length() - 1).split(",");
			List<Object> value = null;
			if (value1 instanceof List) {
				value = (List<Object>) value1;
			} else {
				throw new Exception("in  value1 type is bad ");
			}
			HashMap<String, Object> json = new HashMap<String, Object>();
			json.put(cond, value);
			condJson.put(field1, json);
		} else if (value1 != null && StringUtil.isNotEmpty(value1.toString())) {// 判断是常量还是字段
			HashMap<String, Object> json = new HashMap<String, Object>();
			json.put(cond, value1);
			HashMap<String, Object> oldJson = (HashMap<String, Object>) condJson.get(field1);
			if (oldJson != null) {
				oldJson.putAll(json);
				condJson.put(field1, oldJson);
			} else {
				condJson.put(field1, json);
			}
		} else if (StringUtil.isNotEmpty(field2)) {
			HashMap<String, Object> json = new HashMap<String, Object>();
			json.put(cond, "$" + field2);
			condJson.put(field1, json);
		}
		if ("isnull".equals(cond.replace(" ", ""))) {// null值查询
			condJson.put(field1, "null");
		}
		return condJson;
	}

	/**
	 * 转换逻辑判断条件表达式为mongo的
	 *
	 * @param text
	 * @return
	 */
	public static String transformation(String text, List<ConfigureBeanConds> configureBeanConds) {
		for (ConfigureBeanConds entity : configureBeanConds) {
			if (text.equals(entity.getSql_cond())) {
				text = entity.getMongo_cond();
			}
		}
		return text;
	}

	public static ConfigureBean getConfigureBean() {
		String json = PropUtil.getProperty("configureJson");
		ConfigureBean configureBean = AnalysisUtil.toQueryBean(json, ConfigureBean.class);
		return configureBean;
	}

	public static String conditionMosaicToSql(List<ConditionBean> conditions) {

		if (conditions == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer(" 1 = 1");
		List<String> list = new ArrayList<String>();

		for (ConditionBean queryBeanCondition : conditions) {
			getSqlConditions(queryBeanCondition, list, null);
		}

		for (String string : list) {
			sb.append(string);
		}
		log.debug(sb);
		return sb.toString();
	}

	public static void getSqlConditions(ConditionBean queryBeanCondition, List<String> list, String type) {
		List<ConditionBean> andList = queryBeanCondition.getAnd();
		List<ConditionBean> orList = queryBeanCondition.getOr();

		if (andList != null && andList.size() > 0) {// 判断条件组是不是只有一个
			for (ConditionBean and : andList) {
				getSqlConditions(and, list, "and");
			}
		} else if (orList != null && orList.size() > 0) {
			for (ConditionBean or : orList) {
				getSqlConditions(or, list, "or");
			}
		} else if ("and".equals(type)) {// 如果是and节点
			String condStr = " and " + parseSqlCondition(queryBeanCondition);
			list.add(condStr);
		} else if ("or".equals(type)) {// 如果是or节点
			String condStr = " or " + parseSqlCondition(queryBeanCondition);
			list.add(condStr);
		} else {// 没有节点
			String condStr = " and " + parseSqlCondition(queryBeanCondition);
			list.add(condStr);
		}
	}

	/**
	 * 解析sql条件节点数据
	 *
	 * @param ele
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String parseSqlCondition(ConditionBean queryBeanCondition) {
		String cond = queryBeanCondition.getCond();// 转换逻辑符号
		String field1 = queryBeanCondition.getField1();
		Object value1 = queryBeanCondition.getValue1();
		Object value2 = queryBeanCondition.getValue2();
		String field2 = queryBeanCondition.getField2();
		String result = "";
		if (StringUtil.isNotEmpty(field2)) {
			result = " " + field1 + cond + field2 + " ";

		} else if (StringUtil.isNotEmpty(value1 + "")) {// 细分各种清空
			if ("like".equals(cond)) {// 包含
				result = " " + field1 + " " + cond + " '%" + value1 + "%' ";
			} else if ("notLike".equals(cond)) {// 不包含
				result = " " + field1 + " not like '%" + value1 + "%' ";
			} else if ("start".equals(cond)) {// 开头包含
				result = " " + field1 + " like '" + value1 + "%' ";
			} else if ("end".equals(cond)) {// 结尾包含
				result = " " + field1 + " like '%" + value1 + "' ";
			} else if ("in".equals(cond)) {// in
				List<Object> list = null;
				if (value1 instanceof List) {
					list = (List<Object>) value1;
					if (list.size() > 0) {
						StringBuffer sb = new StringBuffer(" ");
						for (Object object : list) {
							if (object != null && object != "") {
								sb.append("'");
								sb.append(object);
								sb.append("' ,");
							}
						}
						if (sb.length() > 1) {
							result = " " + field1 + " " + cond + " (" + sb.substring(0, sb.length() - 1) + ") ";
						}
					}
				}

			} else if ("notin".equals(cond)) {// not in
				List<Object> list = null;
				if (value1 instanceof List) {
					list = (List<Object>) value1;
					if (list.size() > 0) {
						StringBuffer sb = new StringBuffer(" ");
						for (Object object : list) {
							if (object != null && object != "") {
								sb.append("'");
								sb.append(object);
								sb.append("' ,");
							}
						}
						if (sb.length() > 1) {
							result = " " + field1 + " not in (" + sb.substring(0, sb.length() - 1) + ") ";
						}
					}
				}

			} else if ("between".equals(cond)) {// 结尾包含
				result = " " + field1 + " between " + value1 + " and " + value2;
			} else {// 其他
				result = " " + field1 + " " + cond + " '" + value1 + "' ";
			}

		}
		return result;
	}

	public static void main(String[] args) {
		UpdateBean entity = new UpdateBean();
		entity.setTableCode("sys_menu");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("menu_name", "测试修改");
		entity.setData(map);
		List<ConditionBean> conditions = new ArrayList<ConditionBean>();
		ConditionBean conditionBean = new ConditionBean();
		List<ConditionBean> and = new ArrayList<ConditionBean>();
		ConditionBean conditionBean2 = new ConditionBean();
		conditionBean2.setField1("menu_name");
		conditionBean2.setCond("=");
		conditionBean2.setValue1("测试修改");
		and.add(conditionBean2);
		ConditionBean conditionBean1 = new ConditionBean();
		conditionBean1.setField1("test1");
		conditionBean1.setCond("=");
		conditionBean1.setValue1("value1");
		and.add(conditionBean1);
		conditionBean.setAnd(and);

		conditions.add(conditionBean);
		entity.setConditions(conditions);
		System.out.println(JSON.toJSONString(entity));
		System.out.println(conditionMosaicToSql(conditions));
	}

}
