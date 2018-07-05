package cn.com.infohold.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import bdp.commons.dataservice.config.ConditionBean;
import bdp.commons.dataservice.param.DeteleBean;
import bdp.commons.dataservice.param.InsertBean;
import bdp.commons.dataservice.param.QueryBean;
import bdp.commons.dataservice.param.QueryBeanCondition;
import bdp.commons.dataservice.param.QueryBeanOrder;
import bdp.commons.dataservice.param.UpdateBean;
import bdp.commons.dataservice.ret.RetBean;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.core.BasePage;
import cn.com.infohold.core.service.impl.ServiceImpl;
import cn.com.infohold.dao.IViewTableCofigDao;
import cn.com.infohold.entity.ViewTableCofig;
import cn.com.infohold.service.ICommonsDataService;
import cn.com.infohold.service.IMetadataService;
import cn.com.infohold.service.IViewTableCofigService;
import cn.com.infohold.tools.util.StringUtil;
import lombok.extern.log4j.Log4j2;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-17
 */

@Log4j2(topic = "ViewTableCofigServiceImpl")
@Service
public class ViewTableCofigServiceImpl extends ServiceImpl<IViewTableCofigDao, ViewTableCofig>
		implements IViewTableCofigService {

	@Autowired
	IMetadataService metadataServiceImpl;
	@Autowired
	ICommonsDataService commonsDataServiceImpl;

	@Override
	public List<Map<String, Object>> selectData(String table, String json, String... orderBy)
			throws SQLException, Exception {
		ViewTableCofig entity = selectById(table);
		QueryBean queryBean = new QueryBean();
		queryBean.setTable(entity.getMetadataCode());
		List<QueryBeanCondition> conditions = new ArrayList<QueryBeanCondition>();
		if (!StringUtil.isEmpty(json)) {
			Map<Object, Object> map = BasicJsonUtil.getInstance().toJsonMap(json);
			for (Object key : map.keySet()) {
				if (!StringUtil.isEmpty(map.get(key).toString())) {
					QueryBeanCondition condition = new QueryBeanCondition();
					condition.setField1(key.toString());
					condition.setCond("=");
					condition.setValue1(map.get(key).toString());
					conditions.add(condition);
				}
			}
		}
		queryBean.setConditions(conditions);
		if (orderBy.length > 0) {
			List<QueryBeanOrder> orders = new ArrayList<QueryBeanOrder>();
			for (String field : orderBy) {
				QueryBeanOrder order = new QueryBeanOrder();
				order.setField(field);
				order.setType("asc");
				orders.add(order);
			}
			queryBean.setOrders(orders);
		}
		System.out.println(BasicJsonUtil.getInstance().toJsonString(queryBean));
		RetBean retBean = commonsDataServiceImpl.queryData(queryBean);
		List<Map<String, Object>> list = retBean.getResults();// queryData(sql);
		return list;
	}

	@Override
	public BasePage<Map<String, Object>> selectData(String table, String json, int indexPage, int parsSize)
			throws Exception {
		BasePage<Map<String, Object>> page = new BasePage<Map<String, Object>>(indexPage, parsSize);
		ViewTableCofig entity = selectById(table);
		QueryBean queryBean = new QueryBean();
		queryBean.setTable(entity.getMetadataCode());
		queryBean.setLimit(parsSize);
		queryBean.setSkip(parsSize * (indexPage - 1));
		queryBean.setIf_count(true);
		List<QueryBeanCondition> conditions = new ArrayList<QueryBeanCondition>();
		if (!StringUtil.isEmpty(json)) {
			Map<Object, Object> map = BasicJsonUtil.getInstance().toJsonMap(json);
			for (Object key : map.keySet()) {
				if (!StringUtil.isEmpty(map.get(key).toString())) {
					QueryBeanCondition condition = new QueryBeanCondition();
					condition.setField1(key.toString());
					condition.setCond("=");
					condition.setValue1(map.get(key).toString());
					conditions.add(condition);
				}
			}
		}
		queryBean.setConditions(conditions);
		RetBean retBean = commonsDataServiceImpl.queryData(queryBean);
		page.setRecords(retBean.getResults());
		page.setTotal(retBean.getCount());
		return page;
	}

	@Override
	public void delData(String table, String json) throws SQLException, Exception {
		ViewTableCofig entity = selectById(table);
		DeteleBean deteleBean = new DeteleBean();
		deteleBean.setTableCode(entity.getMetadataCode());
		List<ConditionBean> conditions = new ArrayList<ConditionBean>();
		if (!StringUtil.isEmpty(json)) {
			JSONObject jsonObject = JSONObject.parseObject(json);
			ConditionBean condition = new ConditionBean();
			condition.setField1(entity.getPkField());
			condition.setCond("=");
			condition.setValue1(jsonObject.getString("id"));
			conditions.add(condition);
		}
		deteleBean.setConditions(conditions);
		RetBean retBean = commonsDataServiceImpl.deleteData(deteleBean);
		log.debug(retBean.getRet_message());
	}

	@Override
	public void updateData(String table, String json) throws SQLException, Exception {
		ViewTableCofig entity = selectById(table);
		JSONObject jsonObject = JSONObject.parseObject(json);
		String id = jsonObject.getString(entity.getPkField());

		UpdateBean updateBean = new UpdateBean();
		updateBean.setTableCode(entity.getMetadataCode());
		List<ConditionBean> conditions = new ArrayList<ConditionBean>();
		if (StringUtil.isEmpty(id)) {
			for (String key : jsonObject.keySet()) {
				if (!StringUtil.isEmpty(jsonObject.get(key).toString())) {
					ConditionBean condition = new ConditionBean();
					condition.setField1(key);
					condition.setCond("=");
					condition.setValue1(jsonObject.get(key));
					conditions.add(condition);
				}
			}
		} else {
			ConditionBean condition = new ConditionBean();
			condition.setField1(entity.getPkField());
			condition.setCond("=");
			condition.setValue1(id);
			conditions.add(condition);
		}
		updateBean.setData(BasicJsonUtil.getInstance().toJsonStringMap(json));
		updateBean.setConditions(conditions);
		log.debug(BasicJsonUtil.getInstance().toJsonString(updateBean));
		RetBean retBean = commonsDataServiceImpl.updateData(updateBean);
		log.debug(retBean.getRet_message());
	}

	@Override
	public void addData(String table, String json) throws SQLException, Exception {
		ViewTableCofig entity = selectById(table);
		JSONObject jsonObject = JSONObject.parseObject(json);
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		if (!jsonObject.containsKey(entity.getPkField())) {
			map.put(entity.getPkField(), UUID.randomUUID().toString());
		}
		for (String key : jsonObject.keySet()) {
			map.put(key, jsonObject.getString(key));
		}
		maps.add(map);
		InsertBean insertBean = new InsertBean();
		insertBean.setTableCode(entity.getMetadataCode());
		insertBean.setData(maps);
		RetBean retBean = commonsDataServiceImpl.insertData(insertBean);
		log.debug(retBean.getRet_message());
	}

	@Override
	public List<Map<String, Object>> selectDataObj(String table, String json) throws SQLException, Exception {
		ViewTableCofig entity = selectById(table);
		JSONObject jsonObject = JSONObject.parseObject(json);
		JSONObject params = new JSONObject();
		params.put(entity.getPkField(), jsonObject.getString("id"));
		return selectData(table, params.toJSONString());
	}

	@Override
	public JSONObject selectTable(String tableName) throws SQLException, Exception {
		JSONObject jsonObject = new JSONObject();
		JSONObject json = metadataServiceImpl.selectMetadataByCode(tableName);
		JSONArray jsonArray2 = json.getJSONArray("list");
		JSONObject jsonObject1 = (JSONObject) jsonArray2.get(0);
		JSONArray jsonArray = jsonObject1.getJSONArray("children");
		JSONArray jsonArray1 = new JSONArray();
		for (Object object : jsonArray) {
			JSONObject obj = (JSONObject) object;
			JSONObject jsonObject2 = new JSONObject();
			jsonObject2.put("column_name", obj.getString("column_comment"));
			jsonObject2.put("column_code", obj.getString("column_name"));
			obj.remove("column_name");
			obj.remove("column_comment");
			jsonObject2.putAll(obj);
			jsonArray1.add(jsonObject2);
		}
		jsonObject.put("columns", jsonArray1);
		jsonObject.put("table_name", jsonObject1.getString("table_comment"));
		jsonObject.put("table_code", jsonObject1.getString("table_name"));
		jsonObject1.remove("children");
		jsonObject1.remove("table_name");
		jsonObject1.remove("table_comment");
		jsonObject.putAll(jsonObject1);
		return jsonObject;
	}

	@Override
	public void insertObj(String json) throws Exception {
		String t_id = UUID.randomUUID().toString();
		JSONObject jsonObject = JSONObject.parseObject(json);
		if (jsonObject.containsKey("table_id") && !StringUtil.isEmpty(jsonObject.getString("table_id"))) {
			t_id = jsonObject.getString("table_id");
			JSONArray jsonArray = jsonObject.getJSONArray("columns");
			for (Object object : jsonArray) {
				JSONObject obj = (JSONObject) object;
				if (!obj.containsKey("is_pk")) {
					obj.put("is_pk", "0");
				}
				if (!obj.containsKey("is_insert")) {
					obj.put("is_insert", "0");
				}
				if (!obj.containsKey("is_edit")) {
					obj.put("is_edit", "0");
				}
				if (!obj.containsKey("is_search")) {
					obj.put("is_search", "0");
				}
				if (!obj.containsKey("is_view")) {
					obj.put("is_view", "0");
				}
				updateData("2", obj.toJSONString());
			}
			jsonObject.remove("columns");
			if (!jsonObject.containsKey("is_delete")) {
				jsonObject.put("is_delete", "0");
			}
			if (!jsonObject.containsKey("is_insert")) {
				jsonObject.put("is_insert", "0");
			}
			if (!jsonObject.containsKey("is_edit")) {
				jsonObject.put("is_edit", "0");
			}
			if (!jsonObject.containsKey("is_search")) {
				jsonObject.put("is_search", "0");
			}
			updateData("1", jsonObject.toJSONString());
		} else {
			jsonObject.put("table_id", t_id);
			if (jsonObject.containsKey("columns")) {
				JSONArray jsonArray = jsonObject.getJSONArray("columns");
				for (Object object : jsonArray) {
					JSONObject obj = (JSONObject) object;
					obj.put("column_id", UUID.randomUUID().toString());
					obj.put("table_id", t_id);
					if (!obj.containsKey("is_pk")) {
						obj.put("is_pk", "0");
					}
					if (!obj.containsKey("is_insert")) {
						obj.put("is_insert", "0");
					}
					if (!obj.containsKey("is_edit")) {
						obj.put("is_edit", "0");
					}
					if (!obj.containsKey("is_search")) {
						obj.put("is_search", "0");
					}
					if (!obj.containsKey("is_view")) {
						obj.put("is_view", "0");
					}
					addData("2", obj.toJSONString());
				}
			}
			jsonObject.remove("columns");
			if (!jsonObject.containsKey("is_delete")) {
				jsonObject.put("is_delete", "0");
			}
			if (!jsonObject.containsKey("is_insert")) {
				jsonObject.put("is_insert", "0");
			}
			if (!jsonObject.containsKey("is_edit")) {
				jsonObject.put("is_edit", "0");
			}
			if (!jsonObject.containsKey("is_search")) {
				jsonObject.put("is_search", "0");
			}
			addData("1", jsonObject.toJSONString());
		}
	}

	
	@Override
	public RetBean selectDataByMetadataCode(String table, String json, int pageIndex, int pageSize)
			throws Exception {
		QueryBean queryBean = new QueryBean();
		pageIndex+=1;
		queryBean.setTable(table);
		queryBean.setLimit(pageSize);
		queryBean.setSkip(pageSize * (pageIndex - 1));
		queryBean.setIf_count(true);
		List<QueryBeanCondition> conditions = new ArrayList<QueryBeanCondition>();
		if (!StringUtil.isEmpty(json)) {
			Map<Object, Object> map = BasicJsonUtil.getInstance().toJsonMap(json);
			for (Object key : map.keySet()) {
				if (!StringUtil.isEmpty(map.get(key).toString())) {
					QueryBeanCondition condition = new QueryBeanCondition();
					condition.setField1(key.toString());
					condition.setCond("=");
					condition.setValue1(map.get(key).toString());
					conditions.add(condition);
				}
			}
		}
		queryBean.setConditions(conditions);
		RetBean retBean = commonsDataServiceImpl.queryData(queryBean);
		if (retBean != null && retBean.getRet_code() != null && "0".equals(retBean.getRet_code())) {
			retBean.setCurPage(pageIndex);
			retBean.setPageSize(pageSize);
			int totalCount = retBean.getCount();
			int totalPage = (totalCount + pageSize - 1) / pageSize;
			retBean.setTotal(totalCount);
			retBean.setTotalPage(totalPage);
		} else {
			retBean.setCurPage(0);
			retBean.setPageSize(0);
			retBean.setTotal(0);
			retBean.setTotalPage(0);
		}
		
		return retBean;
	}
	
}
