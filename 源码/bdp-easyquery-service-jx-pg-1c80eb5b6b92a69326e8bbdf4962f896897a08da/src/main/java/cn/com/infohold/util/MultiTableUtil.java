package cn.com.infohold.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import bdp.commons.dataservice.bean.Condition;
import bdp.commons.dataservice.bean.Field;
import bdp.commons.dataservice.bean.Group;
import bdp.commons.dataservice.bean.Order;
import bdp.commons.dataservice.bean.Table;
import bdp.commons.dataservice.ret.RetBean;
import bdp.commons.easyquery.param.QueryParams;
import bdp.commons.easyquery.ret.QueryConditions;
import bdp.commons.easyquery.ret.QueryFields;
import bdp.commons.easyquery.ret.QueryGroups;
import bdp.commons.easyquery.ret.QueryOrder;
import bdp.commons.easyquery.ret.QueryTables;
import cn.com.infohold.basic.util.file.PropUtil;
import cn.com.infohold.tools.util.StringUtil;

public class MultiTableUtil {

	public static List<Table> getTables(QueryParams queryParams, List<QueryTables> queryTables, String db_code,
			List<Condition> tableConditions) {

		List<Table> tables = new ArrayList<Table>();
		List<String> ids = new ArrayList<String>();

		for (QueryTables queryTable : queryTables) {// 先循环一次去metadataid查外键关系
			ids.add(queryTable.getMetadata_id());
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ids", StringUtils.join(ids, ","));
		map.put("db_code", db_code);
		RetBean retBean = CommonUtil.post(PropUtil.getProperty("selectRelationInfoJson"), map);

		Map<String, List<Map<String, Object>>> retMap = retBean.getResults().stream()
				.collect(Collectors.groupingBy(sub -> Objects.toString(sub.get("metadata_id"))));

		List<String> removeList = new ArrayList<String>();
		out: for (String key : retMap.keySet()) {
			List<Map<String, Object>> retMaps = retMap.get(key);
			for (Map<String, Object> tableInfo : retMaps) {
				for (QueryTables queryTable : queryTables) {// 先循环一次去metadataid查外键关系
					if ("referenced_table_name".equals(tableInfo.get("property_code") + "")
							&& queryTable.getTable_name().equals(tableInfo.get("property_value") + "")) {
						removeList.add(key);
						continue out;
					}
				}
			}
		}

		Map<String, List<Map<String, Object>>> removeMap = new HashMap<String, List<Map<String, Object>>>();
		for (String removeKey : removeList) {
			removeMap.put(removeKey, retMap.get(removeKey));
		}

		retMap = removeMap;// 到这里拿出了副表应该是对的

		for (QueryTables queryTable : queryTables) {
			Table table = null;
			table = new Table();
			table.setTable_name(queryTable.getTable_name());
			tables.add(table);
		}
		for (String key : retMap.keySet()) {
			List<Map<String, Object>> retMaps = retMap.get(key);
			Condition condition = new Condition();
			condition.setCondition_cond("=");
			for (Map<String, Object> tableInfo : retMaps) {

				if ("column_name".equals(tableInfo.get("property_code") + "")) {// 外键字段
					condition.setCondition_field1(tableInfo.get("property_value") + "");
				} else if ("referenced_table_name".equals(tableInfo.get("property_code") + "")) {// 关联表
					condition.setCondition_table2(tableInfo.get("property_value") + "");
				} else if ("referenced_column_name".equals(tableInfo.get("property_code") + "")) {// 关联字段
					condition.setCondition_field2(tableInfo.get("property_value") + "");
				}

				for (QueryTables queryTable : queryTables) {
					if (queryTable.getMetadata_id().equals(tableInfo.get("parent") + "")) {
						condition.setCondition_table(queryTable.getTable_name());
					}
				}
			}
			tableConditions.add(condition);
		}
		return tables;
	}

	public static List<Field> getFields(List<QueryFields> fields) throws IOException {

		List<Field> fieldList = new ArrayList<Field>();
		if (!CommonUtil.listIsNotNull(fields)) {
			return null;
		}
		for (QueryFields queryField : fields) {
			Field field = new Field();
			field.setField_name(queryField.getName());
			field.setFiled_alias(queryField.getAlias());
			field.setFiled_cn_name(queryField.getTitle_name());
			field.setFiled_func(queryField.getFunc());
			field.setFiled_table(queryField.getField_table());
			field.setFiled_type(queryField.getField_type());
			field.setIs_total_field(queryField.getIs_total_field());
			fieldList.add(field);
		}

		return fieldList;
	}

	public static List<Condition> getConditions(List<QueryConditions> conditions) throws IOException {
		if (!CommonUtil.listIsNotNull(conditions)) {
			return null;
		}
		List<Condition> conditionList = new ArrayList<Condition>();

		for (QueryConditions queryCondition : conditions) {
			Condition condition = new Condition();
			condition.setCondition_cond(queryCondition.getCond());
			condition.setCondition_field1(queryCondition.getField1());
			condition.setCondition_field2(queryCondition.getField2());
			condition.setCondition_table(queryCondition.getCondition_table());
			condition.setCondition_value1(queryCondition.getValue1());
			condition.setCondition_value2(queryCondition.getValue2());
			conditionList.add(condition);
		}

		return conditionList;
	}

	public static List<Group> getGroups(List<QueryGroups> groups) throws IOException {
		if (!CommonUtil.listIsNotNull(groups)) {
			return null;
		}
		List<Group> groupList = new ArrayList<Group>();

		for (QueryGroups queryGroup : groups) {
			Group group = new Group();
			group.setGroup_filed(queryGroup.getField());
			group.setGroup_func(queryGroup.getFun());
			group.setGroup_table(queryGroup.getGroup_table());
			groupList.add(group);
		}
		return groupList;
	}

	public static List<Order> getOrders(List<QueryOrder> orders) throws IOException {
		if (!CommonUtil.listIsNotNull(orders)) {
			return null;
		}
		List<Order> orderList = new ArrayList<Order>();

		for (QueryOrder queryOrder : orders) {
			Order order = new Order();
			order.setOrder_field(queryOrder.getField());
			order.setOrder_table(queryOrder.getOrder_table());
			order.setOrder_type(queryOrder.getOrder_type());
			orderList.add(order);
		}
		return orderList;
	}
}