package cn.com.infohold.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import bdp.commons.dataservice.bean.Condition;
import bdp.commons.dataservice.bean.Field;
import bdp.commons.dataservice.bean.Group;
import bdp.commons.dataservice.bean.Order;
import bdp.commons.dataservice.bean.SqlBean;
import bdp.commons.dataservice.bean.Table;
import bdp.commons.dataservice.param.QueryBeanCondition;
import bdp.commons.dataservice.ret.RetBean;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.service.impl.DataQueryServiceImpl;
import cn.com.infohold.tools.util.StringUtil;

public class SqlBeanHelper {

	/**
	 * 多表查询解析
	 * 
	 * @param sqlBean
	 * @return
	 */
	public static String AnalyticMultiTable(SqlBean sqlBean) {
		StringBuffer SQL = new StringBuffer("select * from (");
		StringBuffer sb = new StringBuffer("select ");
		sb.append(generationFieldSql(sqlBean));
		sb.append(" from ");
		sb.append(generationTableSql(sqlBean));
		sb.append(generationConditionSql(sqlBean));
		sb.append(generationGroupSql(sqlBean));
		SQL.append(sb.toString());
		SQL.append(") temp_table ");
		SQL.append(generationOrderSql(sqlBean));
		if (sqlBean.getSkip() >= 0) {
			SQL.append(generationLimitSql(sqlBean, sqlBean.getDb_connMap()));
		}
		return SQL.toString();
	}

	/**
	 * 多表查询解析
	 * 
	 * @param sqlBean
	 * @return
	 */
	public static String AnalyticMultiTableCount(SqlBean sqlBean) {
		StringBuffer SQL = new StringBuffer("select count(1) as count from (");
		StringBuffer sb = new StringBuffer("select ");
		sb.append(generationFieldSql(sqlBean));
		sb.append(" from ");
		sb.append(generationTableSql(sqlBean));
		sb.append(generationConditionSql(sqlBean));
		sb.append(generationGroupSql(sqlBean));
		SQL.append(sb.toString());
		SQL.append(") temp_table ");
		return SQL.toString();
	}

	/**
	 * 拼接排序SQL串
	 * 
	 * @param sqlBean
	 * @return
	 */
	private static String generationOrderSql(SqlBean sqlBean) {
		String resultSql = " ORDER BY ";
		List<String> orders = new ArrayList<String>();
		if (sqlBean.getOrders() != null && sqlBean.getOrders().size() > 0) {
			for (Order order : sqlBean.getOrders()) {
				String str = "temp_table." + order.getOrder_field() + " " + order.getOrder_type();
				orders.add(str);
			}
		}
		if (orders.size() > 0) {
			return resultSql + StringUtils.join(orders, " , ");
		} else {
			return "";
		}
	}

	/**
	 * 拼接排序SQL串
	 * 
	 * @param sqlBean
	 * @return
	 */
	private static String generationLimitSql(SqlBean sqlBean, Map<String, Object> propertyMap) {
		String db_type = (String) propertyMap.get("db_type");
		Integer limit = sqlBean.getLimit();
		Integer skip = sqlBean.getSkip();
		StringBuffer sb = new StringBuffer();
		if ("mysql".equals(db_type)) {
			if (null != limit && null != skip) {
				sb.append(" limit ");
				sb.append(skip + ", ");
				sb.append(limit);
			}
		} else if ("oracle".equals(db_type)) {
			if (null != limit && null != skip) {
				String tmpSb = sb.toString();
				sb.append(" SELECT *  FROM (SELECT tt.*, ROWNUM AS rowno  FROM ( ");
				sb.append(tmpSb + " ) tt");
				sb.append("  WHERE ROWNUM <= " + limit + ") table_alias WHERE table_alias.rowno >= " + skip);
			}
		} else if ("postgresql".equals(db_type)) {
			if (null != limit && null != skip) {
				sb.append(" limit ");
				sb.append(limit + " ");
				sb.append(" offset " + skip);
			}
		}
		return sb.toString();
	}

	/**
	 * 拼接分组SQL串
	 * 
	 * @param sqlBean
	 * @return
	 */
	private static String generationGroupSql(SqlBean sqlBean) {
		String resultSql = " GROUP BY ";
		List<String> groups = new ArrayList<String>();
		if (sqlBean.getGroups() != null && sqlBean.getGroups().size() > 0) {
			for (Group group : sqlBean.getGroups()) {
				if (StringUtil.isNotEmpty(group.getGroup_filed())) {
					if ("case".equals(group.getGroup_func())) {
						String str = getGroupCase(group);
						groups.add(str);
					} else {
						String str = group.getGroup_table().toLowerCase() + "." + group.getGroup_filed();
						groups.add(str);
					}
				}
			}
		}
		if (groups.size() > 0) {
			return resultSql + StringUtils.join(groups, " , ");
		} else {
			return "";
		}
	}

	private static String getGroupCase(Group group) {
		StringBuffer sb = new StringBuffer(" CASE ");
		JSONArray jsonArray = JSON.parseArray(group.getParam());
		for (int i = 0; i < jsonArray.size(); i++) {
			int index = i + 1;
			if (index % 2 != 0 && jsonArray.size() - 1 == i) {// 最后一个 而且是单数
				String str = jsonArray.getString(i);
				sb.append(" ELSE ");
				sb.append("'" + str + "'");
				sb.append(" END ");
			} else if (index % 2 != 0) {// 单数
				QueryBeanCondition condition = jsonArray.getObject(i, QueryBeanCondition.class);
				sb.append(" WHEN ");
				sb.append(conditionMosaicToSql(condition));
			} else {// 双数
				String str = jsonArray.getString(i);
				sb.append(" THEN ");
				sb.append("'" + str + "'");
			}
		}
		return sb.toString();
	}

	/**
	 * 获得sql过滤条件
	 *
	 * @param queryBean
	 * @return
	 */
	private static String conditionMosaicToSql(QueryBeanCondition condition) {

		StringBuffer sb = new StringBuffer(" 1 = 1");
		List<String> list = new ArrayList<String>();

		getSqlConditions(condition, list, null);

		for (String string : list) {
			sb.append(string);
		}
		// log.info(sb);
		return sb.toString();
	}

	private static void getSqlConditions(QueryBeanCondition queryBeanCondition, List<String> list, String type) {
		List<QueryBeanCondition> andList = queryBeanCondition.getAnd();
		List<QueryBeanCondition> orList = queryBeanCondition.getOr();

		if (andList != null && andList.size() > 0) {// 判断条件组是不是只有一个
			for (QueryBeanCondition and : andList) {
				getSqlConditions(and, list, "and");
			}
		} else if (orList != null && orList.size() > 0) {
			for (QueryBeanCondition or : orList) {
				getSqlConditions(or, list, "or");
			}
		} else if ("and".equals(type)) {// 如果是and节点
			if (StringUtil.isNotEmpty(parseSqlCondition(queryBeanCondition))) {
				String condStr = " and " + parseSqlCondition(queryBeanCondition);
				list.add(condStr);
			}
		} else if ("or".equals(type)) {// 如果是or节点
			if (StringUtil.isNotEmpty(parseSqlCondition(queryBeanCondition))) {
				String condStr = " or " + parseSqlCondition(queryBeanCondition);
				list.add(condStr);
			}
		} else {// 没有节点
			if (StringUtil.isNotEmpty(parseSqlCondition(queryBeanCondition))) {
				String condStr = " and " + parseSqlCondition(queryBeanCondition);
				list.add(condStr);
			}

		}
	}

	@SuppressWarnings("unchecked")
	private static String parseSqlCondition(QueryBeanCondition queryBeanCondition) {
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

	private static String generationFieldSql(SqlBean sqlBean) {
		List<String> fields = new ArrayList<String>();
		if (sqlBean.getGroups() != null && sqlBean.getGroups().size() > 0) {
			for (Group group : sqlBean.getGroups()) {
				if (StringUtil.isNotEmpty(group.getGroup_filed())) {
					if ("case".equals(group.getGroup_func())) {
						String str = getGroupCase(group) + " AS " + group.getGroup_filed();
						fields.add(str);
					} else {
						fields.add(group.getGroup_table().toLowerCase() + "." + group.getGroup_filed());
					}
				}
			}
		}
		if (sqlBean.getFields() != null && sqlBean.getFields().size() > 0) {
			for (Field field : sqlBean.getFields()) {
				String alias = StringUtil.isEmpty(field.getFiled_alias()) ? " as " + field.getField_name()
						: " as " + field.getFiled_alias();
				if (field.getFiled_func() != null && !"".equals(field.getFiled_func())) {
					if ("count_distinct".equals(field.getFiled_func())) {
						String fieldstr = "COUNT" + "( DISTINCT " + field.getFiled_table().toLowerCase() + "."
								+ field.getField_name() + ") " + alias;
						fields.add(fieldstr);
					} else if ("nonemptycount".equals(field.getFiled_func())) {
						String fieldstr = "COUNT" + "(" + field.getFiled_table().toLowerCase() + "."
								+ field.getField_name() + ") " + alias;
						fields.add(fieldstr);
					} else if ("notempty".equals(field.getFiled_func())) {
						String fieldstr = "COUNT" + "( DISTINCT " + field.getFiled_table().toLowerCase() + "."
								+ field.getField_name() + ") " + alias;
						fields.add(fieldstr);
					} else {
						String fieldstr = field.getFiled_func() + "(" + field.getFiled_table().toLowerCase() + "."
								+ field.getField_name() + ") " + alias;
						fields.add(fieldstr);
					}

				} else {
					String fieldstr = field.getFiled_table().toLowerCase() + "." + field.getField_name() + alias;
					fields.add(fieldstr);
				}
			}
			return StringUtils.join(fields, ",");
		} else {
			return "*";
		}
	}

	private static String generationTableSql(SqlBean sqlBean) {
		List<String> tables = new ArrayList<String>();
		boolean isAllInnerJoin = true;
		for (Table table : sqlBean.getTables()) {
			if (StringUtil.isEmpty(table.getJoin_type())) {
				String tablestr = table.getTable_name() + " as " + table.getTable_name().toLowerCase();
				tables.add(0, tablestr);
			} else {
				String tablestr = table.getJoin_type() + " " + table.getTable_name() + " as "
						+ table.getTable_name().toLowerCase() + " ON " + table.getTable_name().toLowerCase() + "."
						+ table.getJoin_field() + "=" + table.getRelation_table().toLowerCase() + "."
						+ table.getRelation_field();
				tables.add(tablestr);
				isAllInnerJoin = false;
			}
		}
		if (isAllInnerJoin) {
			return StringUtils.join(tables, " , ");
		} else {
			return StringUtils.join(tables, " ");
		}
	}

	@SuppressWarnings("unchecked")
	private static String generationConditionSql(SqlBean sqlBean) {
		String resultSql = " where ";
		List<String> Conditions = new ArrayList<String>();
		if (sqlBean.getConditions() != null && sqlBean.getConditions().size() > 0) {
			for (Condition condition : sqlBean.getConditions()) {
				if (StringUtil.isNotEmpty(condition.getCondition_value1() + "")) {
					String conditionstr = "";
					if ("in".equals(condition.getCondition_cond())) {
						if (condition.getCondition_value1() instanceof List) {
							StringBuffer vBuffer = new StringBuffer();
							List<Object> list = (List<Object>) condition.getCondition_value1();
							for (int i = 0; i < list.size(); i++) {
								vBuffer.append("'" + list.get(i) + "'");
								if (i != list.size() - 1) {
									vBuffer.append(",");
								}
							}
							conditionstr = condition.getCondition_table().toLowerCase() + "."
									+ condition.getCondition_field1() + " " + condition.getCondition_cond() + " ("
									+ vBuffer + ")";
						}

					} else if ("notin".equals(condition.getCondition_cond())) {
						if (condition.getCondition_value1() instanceof List) {
							StringBuffer vBuffer = new StringBuffer();
							List<Object> list = (List<Object>) condition.getCondition_value1();
							for (int i = 0; i < list.size(); i++) {
								vBuffer.append("'" + list.get(i) + "'");
								if (i != list.size() - 1) {
									vBuffer.append(",");
								}
							}
							conditionstr = condition.getCondition_table().toLowerCase() + "."
									+ condition.getCondition_field1() + " not in(" + vBuffer + ")";
						}
					} else if ("between".equals(condition.getCondition_cond())) {
						conditionstr = condition.getCondition_table().toLowerCase() + "."
								+ condition.getCondition_field1() + " " + condition.getCondition_cond() + " '"
								+ condition.getCondition_value1() + "' AND '" + condition.getCondition_value2() + "' ";
					}else if ("like".equals(condition.getCondition_cond())) {// 不包含
						conditionstr = " " + condition.getCondition_field1() + " like '%" + condition.getCondition_value1() + "%' ";
					} else if ("start".equals(condition.getCondition_cond())) {// 开头包含
						conditionstr = " " + condition.getCondition_field1() + " like '" + condition.getCondition_value1() + "%' ";
					} else if ("end".equals(condition.getCondition_cond())) {// 结尾包含
						conditionstr = " " + condition.getCondition_field1() + " like '%" + condition.getCondition_value1() + "' ";
					} else {
						if (StringUtil.isEmpty(condition.getCondition_field2())) {
							conditionstr = condition.getCondition_table().toLowerCase() + "."
									+ condition.getCondition_field1() + " " + condition.getCondition_cond() + " '"
									+ condition.getCondition_value1() + "'";
						} else {
							conditionstr = condition.getCondition_table().toLowerCase() + "."
									+ condition.getCondition_field1() + " " + condition.getCondition_cond()
									+ condition.getCondition_table2().toLowerCase() + "."
									+ condition.getCondition_field2() + " ";
						}
					}
					Conditions.add(conditionstr);
				}
			}
		}
		if (Conditions.size() > 0) {
			return resultSql + StringUtils.join(Conditions, " and ");
		} else {
			return "";
		}
	}

	public static void main(String[] args) throws Exception {
		SqlBean sqlBean = new SqlBean();

		List<Table> tables = new ArrayList<Table>();
		Table table = new Table();
		table.setTable_name("metadata");
		tables.add(table);
		Table table1 = new Table();
		table1.setTable_name("metadata_property");
		tables.add(table1);

		Table metamodel_class = new Table();
		metamodel_class.setTable_name("metamodel_class");
		tables.add(metamodel_class);
		Table metamodel_classproperty = new Table();
		metamodel_classproperty.setTable_name("metamodel_classproperty");
		tables.add(metamodel_classproperty);

		sqlBean.setTables(tables);

		List<Field> fields = new ArrayList<Field>();
		fields.add(new Field("metadata", "metadata_id"));
		fields.add(new Field("metadata", "metadata_code"));
		fields.add(new Field("metadata", "metadata_name"));
		fields.add(new Field("metadata", "parent_metadata"));
		fields.add(new Field("metamodel_class", "class_code"));
		fields.add(new Field("metamodel_class", "class_name"));
		fields.add(new Field("metamodel_classproperty", "property_code"));
		fields.add(new Field("metamodel_classproperty", "property_name"));
		fields.add(new Field("metadata_property", "property_value"));
		sqlBean.setFields(fields);

		List<Condition> conditions = new ArrayList<Condition>();
		Condition metamodel_classcondition = new Condition();
		metamodel_classcondition.setCondition_table("metamodel_class");
		metamodel_classcondition.setCondition_field1("class_id");
		metamodel_classcondition.setCondition_cond("=");
		metamodel_classcondition.setCondition_table2("metadata");
		metamodel_classcondition.setCondition_field2("class_id");
		conditions.add(metamodel_classcondition);

		Condition condition = new Condition();
		condition.setCondition_table("metadata_property");
		condition.setCondition_field1("metadata_id");
		condition.setCondition_cond("=");
		condition.setCondition_table2("metadata");
		condition.setCondition_field2("metadata_id");
		conditions.add(condition);

		Condition metamodel_classpropertycondition = new Condition();
		metamodel_classpropertycondition.setCondition_table("metamodel_classproperty");
		metamodel_classpropertycondition.setCondition_field1("property_id");
		metamodel_classpropertycondition.setCondition_cond("=");
		metamodel_classpropertycondition.setCondition_table2("metadata_property");
		metamodel_classpropertycondition.setCondition_field2("class_property_id");
		conditions.add(metamodel_classpropertycondition);

		Condition condition1 = new Condition();
		condition1.setCondition_table("metadata");
		condition1.setCondition_field1("metadata_id");
		condition1.setCondition_cond("in");
		List<String> mList = new ArrayList<String>();
		mList.add("38219");
		mList.add("37387");
		mList.add("37386");
		mList.add("37385");
		condition1.setCondition_value1(mList);
		conditions.add(condition1);
		sqlBean.setConditions(conditions);

		Map<String, Object> propertyMap = new HashMap<String, Object>();
		propertyMap.put("db_name", "bdp_basic");
		propertyMap.put("db_type", "postgresql");
		propertyMap.put("db_host", "192.168.31.251");
		propertyMap.put("db_port", "5432");
		propertyMap.put("db_driver", "org.postgresql.Driver");
		propertyMap.put("db_user", "postgres");
		propertyMap.put("db_password", "lifanhong");
		sqlBean.setDb_connMap(propertyMap);
		sqlBean.setDb_metadata("metadata");
		sqlBean.setSkip(-1);
		DataQueryServiceImpl dataQueryServiceImpl = new DataQueryServiceImpl();
		RetBean list = dataQueryServiceImpl.queryMultiTable(sqlBean);
		List<Map<String, Object>> result = list.getResults().stream()
				.collect(Collectors.groupingBy(e -> e.get("metadata_id"))).entrySet().stream().map(e -> {
					Map<String, Object> map = new HashMap<>();
					List<Map<String, Object>> propertys = e.getValue();
					map.put("metadata_id", e.getKey());
					map.put("metadata_code", propertys.get(0).get("metadata_code"));
					map.put("metadata_name", propertys.get(0).get("metadata_name"));
					map.put("parent_metadata", propertys.get(0).get("parent_metadata"));
					map.put("class_code", propertys.get(0).get("class_code"));
					map.put("class_name", propertys.get(0).get("class_name"));
					for (Map<String, Object> m : propertys) {
						m.remove("metadata_id");
						m.remove("metadata_code");
						m.remove("metadata_name");
						m.remove("parent_metadata");
						m.remove("class_code");
						m.remove("class_name");
					}
					map.put("propertys", propertys);
					return map;
				}).collect(Collectors.toList());
		for (Map<String, Object> o : result) {
			System.out.println(BasicJsonUtil.getInstance().toJsonString(o));
		}
	}
}
