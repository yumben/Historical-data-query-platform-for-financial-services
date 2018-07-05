package cn.com.infohold.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bdp.commons.authorization.auth.LoginBean;
import bdp.commons.dataservice.param.QueryBean;
import bdp.commons.dataservice.param.QueryBeanCondition;
import bdp.commons.dataservice.ret.RetBean;
import bdp.commons.easyquery.param.QueryParams;
import bdp.commons.easyquery.ret.QueryConditions;
import cn.com.infohold.basic.util.file.PropUtil;
import cn.com.infohold.tools.util.StringUtil;

public class PermissionUtil {

	@SuppressWarnings("unchecked")
	public static List<QueryBeanCondition> getDataPermission(QueryParams queryParams) {
		List<String> notInList = new ArrayList<String>();
		List<QueryBeanCondition> conditions = new ArrayList<QueryBeanCondition>();
		List<Map<String, Object>> dpList = getDataPermission(queryParams.getQuery_template_id());
		if (dpList.size() > 0) {
			Map<String, Object> map = dpList.get(0);
			List<String> orgList = getUnitList(queryParams, notInList);
			List<Map<String, Object>> robj = (List<Map<String, Object>>) map.get("resource_object");
			if (robj != null && orgList.size() > 0 && robj.size() > 0) {
				QueryBeanCondition condition = new QueryBeanCondition();
				boolean flag = true;// 是否还要带入权限范围
				String unitId = robj.get(0).get("data_permission_column").toString();
				queryParams.setUnitId(unitId);
				if (!StringUtil.isEmpty(unitId)) {

					for (QueryConditions queryCondition : queryParams.getConditions()) {
						if (queryCondition.getValue1() instanceof List) {
							List<Object> list = (List<Object>) queryCondition.getValue1();
							if (unitId.equals(queryCondition.getField1()) && list.size() > 0) {
								flag = false;
								break;
							}
						}

					}
					if (flag) {
						condition.setField1(unitId);
						condition.setCond("in");
						condition.setValue1(orgList);
						conditions.add(condition);
					}
					QueryBeanCondition notCondition = new QueryBeanCondition();
					notCondition.setField1(unitId);
					notCondition.setCond("notin");
					notCondition.setValue1(notInList);
					conditions.add(notCondition);
				}
			}

		}
		return conditions;
	}

	@SuppressWarnings("unchecked")
	public static QueryConditions getDefaultCondition(QueryParams queryParams) {

		QueryConditions condition = new QueryConditions();
		List<Map<String, Object>> dpList = getDataPermission(queryParams.getQuery_template_id());
		if (dpList.size() > 0) {
			Map<String, Object> map = dpList.get(0);
			Map<String, Object> orgList = getDefineUnitList(queryParams.getToken());
			List<Map<String, Object>> robj = (List<Map<String, Object>>) map.get("resource_object");
			if (robj != null && orgList.size() > 0 && robj.size() > 0) {
				if (!StringUtil.isEmpty(robj.get(0).get("data_permission_column").toString())) {
					condition.setField1(robj.get(0).get("data_permission_column").toString());
					condition.setValue1(orgList.get("unit_ids"));
					condition.setValfield(orgList.get("unit_names") + "");
				}
			}
		}
		return condition;
	}

	// public static List<QueryBeanCondition> getDataPermission(QueryParams
	// queryParams, String unitId) {
	// List<QueryBeanCondition> conditions = new
	// ArrayList<QueryBeanCondition>();
	// List<String> orgList = getUnitList(queryParams.getToken());
	// if (orgList.size() > 0) {
	// QueryBeanCondition condition = new QueryBeanCondition();
	// condition.setField1(unitId);
	// condition.setCond("in");
	// condition.setValue1(orgList);
	// conditions.add(condition);
	// }
	// return conditions;
	// }

	/**
	 * 获取业务表的机构id字段的conditions
	 * 
	 * @param queryParams
	 * @return
	 */
	public static List<QueryBeanCondition> getBusinessUnitIdConditions(String unitId, List<String> inValue) {
		List<QueryBeanCondition> conditions = new ArrayList<QueryBeanCondition>();
		QueryBeanCondition condition = new QueryBeanCondition();
		if (!StringUtil.isEmpty(unitId)) {
			condition.setField1(unitId);
			condition.setCond("in");
			condition.setValue1(inValue);
			conditions.add(condition);
		}
		return conditions;
	}

	/**
	 * 获取业务表的机构id字段
	 * 
	 * @param queryParams
	 * @return
	 */
	public static String getBusinessUnitId(QueryParams queryParams) {
		List<Map<String, Object>> dpList = getDataPermission(queryParams.getQuery_template_id());
		if (dpList.size() > 0) {
			Map<String, Object> map = dpList.get(0);
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> robj = (List<Map<String, Object>>) map.get("resource_object");
			if (robj != null && robj.size() > 0) {
				return robj.get(0).get("data_permission_column").toString();
			}
		}
		return null;
	}

	private static List<String> getUnitList(QueryParams queryParams, List<String> notInList) {
		String token = queryParams.getToken();
		String is_remove = queryParams.getIs_remove();
		List<String> result = new ArrayList<String>();
		LoginBean loginBean = new LoginBean();
		loginBean.setToken(token);
		// loginBean.setUnitId(unit_id);
		// bdp-authorization-service
		RetBean retBean = CommonUtil.post(PropUtil.getProperty("bdp-authorization-service") + "/getUnitList", loginBean,
				"bdp-authorization-service");
		List<Map<String, Object>> list = retBean.getResults();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> m : retBean.getResults()) {
				if ("true".equals(is_remove)) {
					result.add(m.get("unit_id").toString());
				} else {
					String user_unit_id = m.get("user_unit_id") + "";
					String manage_unit = m.get("manage_unit") + "";
					if ("0".equals(m.get("is_remove") + "") || (m.get("unit_id") + "").equals(user_unit_id)
							|| (manage_unit).equals(user_unit_id)) {
						result.add(m.get("unit_id").toString());
					} else {
						notInList.add(m.get("unit_id").toString());
					}
				}
			}
		}
		return result;
	}

	private static Map<String, Object> getDefineUnitList(String token) {
		Map<String, Object> result = new HashMap<String, Object>();
		LoginBean loginBean = new LoginBean();
		loginBean.setToken(token);
		// loginBean.setUnitId(unit_id);
		// bdp-authorization-service
		RetBean retBean = CommonUtil.post(PropUtil.getProperty("bdp-authorization-service") + "/getUnitList",
				loginBean);
		List<Map<String, Object>> list = retBean.getResults();
		List<String> unit_ids = new ArrayList<String>();
		StringBuffer unit_names = new StringBuffer();
		for (Map<String, Object> m : list) {
			unit_ids.add(m.get("unit_id").toString());
			unit_names.append(m.get("unit_id") + "--" + m.get("unit_name") + ",");
		}
		result.put("unit_ids", unit_ids);
		result.put("unit_names", unit_names.substring(0, unit_names.length() - 1));
		return result;
	}

	private static List<Map<String, Object>> getDataPermission(String query_template_id) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		QueryBean queryBean = new QueryBean();
		List<QueryBeanCondition> conditions = new ArrayList<QueryBeanCondition>();
		// 条件 查询模板
		QueryBeanCondition queryBeanCondition = new QueryBeanCondition();
		queryBeanCondition.setField1("resource_business_id");
		queryBeanCondition.setCond("=");
		queryBeanCondition.setValue1(query_template_id);
		conditions.add(queryBeanCondition);
		queryBean.setConditions(conditions);
		RetBean retBean = CommonUtil.post(PropUtil.getProperty("bdp-authorization-service") + "/getResourceByJson",
				queryBean, "bdp-authorization-service");
		if ("0".equals(retBean.getRet_code())) {
			result = retBean.getResults();
		}
		return result;
	}

}
