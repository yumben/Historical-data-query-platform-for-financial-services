package cn.com.infohold.service.resource.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import bdp.commons.authorization.resource.AuthOperation;
import bdp.commons.authorization.resource.AuthResource;
import bdp.commons.authorization.resource.AuthResourceOperation;
import bdp.commons.dataservice.config.ConditionBean;
import bdp.commons.dataservice.param.BatchOperationBean;
import bdp.commons.dataservice.param.ExecuteBatchSqlBean;
import bdp.commons.dataservice.param.ExecuteBySqlBean;
import bdp.commons.dataservice.param.InsertBean;
import bdp.commons.dataservice.param.QueryBean;
import bdp.commons.dataservice.param.QueryBeanCondition;
import bdp.commons.dataservice.ret.RetBean;
import bdp.commons.util.BeanUtil;
import cn.com.infohold.basic.util.common.DateUtil;
import cn.com.infohold.service.resource.IResourceService;
import cn.com.infohold.tools.util.StringUtil;
import cn.com.infohold.util.AnalysisUtil;
import cn.com.infohold.util.CommonUtil;
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
public class ResourceServiceImpl implements IResourceService {

	@Value("${db_code}")
	String db_code;
	@Value("${dataUrl}")
	String dataUrl;
	@Value("${auth_resource}")
	String auth_resource;
	@Value("${auth_resource_operation}")
	String auth_resource_operation;
	@Value("${auth_operation}")
	String auth_operation;
	@Value("${auth_resource_object}")
	String auth_resource_object;

	// @Override
	// public RetBean resourceQuery(QueryBean queryBean, String id) {
	// RetBean retBean = null;
	// QueryBean queryOperation = new QueryBean();
	// List<QueryBean> list = new ArrayList<QueryBean>();
	// BatchOperationBean batchOperationBean = new BatchOperationBean();
	//
	// queryBean.setTable(auth_resource);//查资源表
	// queryOperation.setTable(auth_resource_operation);//查操作
	// if (StringUtil.isNotEmpty(id)) {// 修改回显
	// List<QueryBeanCondition> conditions = new
	// ArrayList<QueryBeanCondition>();
	// QueryBeanCondition templateBean = new QueryBeanCondition();
	// templateBean.setField1("resource_id");
	// templateBean.setValue1(id);
	// templateBean.setCond("=");
	// conditions.add(templateBean);
	//
	// List<QueryBeanCondition> conditions1 = new
	// ArrayList<QueryBeanCondition>();
	// QueryBeanCondition templateBean1 = new QueryBeanCondition();
	// templateBean1.setField1("resource_id");
	// templateBean1.setValue1(id);
	// templateBean1.setCond("=");
	// conditions1.add(templateBean1);
	//
	// queryBean.setConditions(conditions);
	// queryOperation.setConditions(conditions1);
	// }
	// list.add(queryBean);
	// list.add(queryOperation);
	// batchOperationBean.setQueryBeans(list);
	// retBean = CommonUtil.post(dataUrl + "/queryByList",
	// JSON.parseObject(JSON.toJSONString(batchOperationBean)));// 新增资源
	// Map<String, List<Map<String, Object>>> map = retBean.getBatchResults();
	// Map<String, List<Map<String, Object>>> resultMap = new HashMap<String,
	// List<Map<String,Object>>>();//确保都是同样的key
	// resultMap.put("resource", map.get(auth_resource));
	// resultMap.put("resource_operation", map.get(auth_resource_operation));
	// retBean.setBatchResults(resultMap);
	// return retBean;
	// }

	@Override
	public RetBean resourceInsert(AuthResource authResource) throws Exception {
		RetBean retBean = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String resourceId = UUID.randomUUID().toString();
		authResource.setResource_id(resourceId);

		Map<String, Object> insertResourceMap = AnalysisUtil.AnalysisObject(authResource);
		list.add(insertResourceMap);
		JSONObject reqObj = new JSONObject();
		reqObj.put("tableCode", auth_resource);
		reqObj.put("data", list);
		retBean = CommonUtil.post(dataUrl + "/insert", reqObj);// 新增资源

		retBean = subsetInsert(authResource.getAuthResourceOperations(), resourceId);// 新增子集
		return retBean;
	}

	@Override
	public RetBean resourceUpdate(AuthResource authResource) throws Exception {
		RetBean retBean = null;
		String resourceId = authResource.getResource_id();
		retBean = subsetDelete(resourceId);

		// 查询条件
		List<ConditionBean> conditions = new ArrayList<ConditionBean>();
		ConditionBean conditionBean = new ConditionBean();
		conditionBean.setField1("resource_id");
		conditionBean.setValue1(resourceId);
		conditionBean.setCond("=");
		conditions.add(conditionBean);

		Map<String, Object> updateResourceMap = AnalysisUtil.AnalysisObject(authResource);
		JSONObject reqObj = new JSONObject();
		reqObj.put("conditions", conditions);
		reqObj.put("tableCode", auth_resource);
		reqObj.put("data", updateResourceMap);
		retBean = CommonUtil.post(dataUrl + "/update", reqObj);// 新增资源

		retBean = subsetInsert(authResource.getAuthResourceOperations(), resourceId);// 新增子集
		return retBean;
	}

	@Override
	public RetBean resourceDelete(String id) throws Exception {
		RetBean retBean = null;
		JSONObject reqObj = new JSONObject();
		// 查询条件
		List<ConditionBean> conditions = new ArrayList<ConditionBean>();
		ConditionBean conditionBean = new ConditionBean();
		conditionBean.setField1("resource_id");
		conditionBean.setValue1(id);
		conditionBean.setCond("=");
		conditions.add(conditionBean);
		reqObj.put("conditions", conditions);
		reqObj.put("tableCode", auth_resource);
		retBean = CommonUtil.post(dataUrl + "/delete", reqObj);// 删除操作

		reqObj.put("tableCode", auth_resource_operation);
		retBean = CommonUtil.post(dataUrl + "/delete", reqObj);// 删除子表
		return retBean;
	}

	public RetBean subsetDelete(String id) throws Exception {
		RetBean retBean = null;
		JSONObject reqObj = new JSONObject();
		// 查询条件
		List<ConditionBean> conditions = new ArrayList<ConditionBean>();
		ConditionBean conditionBean = new ConditionBean();
		conditionBean.setField1("resource_id");
		conditionBean.setValue1(id);
		conditionBean.setCond("=");
		conditions.add(conditionBean);
		reqObj.put("conditions", conditions);

		// tebles
		reqObj.put("tableCode", auth_resource_operation);
		retBean = CommonUtil.post(dataUrl + "/delete", reqObj);// 删除操作
		return retBean;
	}

	public RetBean subsetInsert(List<AuthResourceOperation> authResourceOperations, String resourceId)
			throws Exception {
		RetBean retBean = null;
		JSONObject reqObj = new JSONObject();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (CommonUtil.listIsNotNull(authResourceOperations)) {
			for (AuthResourceOperation authResourceOperation : authResourceOperations) {// 新增操作
				authResourceOperation.setResource_id(resourceId);
				Map<String, Object> insertResourceOperationMap = AnalysisUtil.AnalysisObject(authResourceOperation);
				list.add(insertResourceOperationMap);

			}
			reqObj.put("tableCode", auth_resource_operation);
			reqObj.put("data", list);
			retBean = CommonUtil.post(dataUrl + "/insert", reqObj);// 新增操作
		}
		return retBean;
	}

	@Override
	public RetBean resourceQuery(QueryBean queryBean) throws Exception {
		ExecuteBySqlBean execute = new ExecuteBySqlBean();
		StringBuffer sb = new StringBuffer(
				" SELECT aa.resource_id, aa.resource_code, aa.resource_name, aa.resource_type, aa.resource_url, aa.resource_desc, cc.operation_code, cc.operation_name, cc.operation_type,cc.id,cc.operation_desc FROM auth_resource aa, auth_resource_operation bb, auth_operation cc  WHERE aa.resource_id = bb.resource_id AND bb.operation_id = cc. ID ");
		Integer limit = queryBean.getLimit();
		Integer skip = queryBean.getSkip();
		Boolean if_count = (queryBean.getIf_count() != null) ? queryBean.getIf_count() : false;
		execute.setDb_code(db_code);

		if (null != limit && null != skip) {
			sb.append(" limit ");
			sb.append(limit + " ");
			sb.append(" offset " + skip);
		}
		log.debug("SQL===" + sb.toString());
		execute.setSql(sb.toString());
		RetBean retBean = CommonUtil.post(dataUrl + "/queryBySql", JSON.parseObject(JSON.toJSONString(execute)));
		List<Map<String, Object>> list = retBean.getResults();

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (CommonUtil.listIsNotNull(list)) {// 转换json格式返回
			for (Map<String, Object> map : list) {
				String resourceId = map.get("resource_id") + "";

				AuthOperation authOperation = new AuthOperation();
				authOperation.setId(map.get("id") + "");
				authOperation.setOperation_code(map.get("operation_name") + "");
				authOperation.setOperation_name(map.get("operation_code") + "");
				authOperation.setOperation_type(map.get("operation_type") + "");

				if (null == resultMap.get(resourceId)) {
					AuthResource authResource = JSON.parseObject(JSON.toJSONString(map), AuthResource.class);
					List<AuthOperation> authOperations = new ArrayList<AuthOperation>();
					authOperations.add(authOperation);
					authResource.setAuthOperation(authOperations);
					resultMap.put(resourceId, authResource);
				} else {
					AuthResource authResource = (AuthResource) resultMap.get(resourceId);
					authResource.getAuthOperation().add(authOperation);
				}

			}
		}
		list.clear();
		list.add(resultMap);
		retBean.setResults(list);

		if (if_count) {
			String countSql = "SELECT count(*) AS count FROM auth_resource aa, auth_resource_operation bb, auth_operation cc WHERE aa.resource_id = bb.resource_id AND bb.operation_id = cc. ID";
			execute.setSql(countSql);
			RetBean countRetBean = CommonUtil.post(dataUrl + "/queryBySql",
					JSON.parseObject(JSON.toJSONString(execute)));
			String object = countRetBean.getResults().get(0).get("count") + "";
			if (StringUtil.isNotEmpty(object)) {
				retBean.setCount(Integer.parseInt(object));
			} else {
				retBean.setRet_code("-1");
				retBean.setRet_message("查询出错");
			}
		}
		log.debug("retBean===" + retBean.getRet_code());
		return retBean;
	}

	@Override
	public RetBean resourceQueryById(String Id) throws Exception {
		ExecuteBySqlBean execute = new ExecuteBySqlBean();
		StringBuffer sb = new StringBuffer(
				" SELECT aa.resource_id, aa.resource_code, aa.resource_name, aa.resource_type, aa.resource_url, aa.resource_desc, cc.operation_code, cc.operation_name, cc.operation_type,cc.id,cc.operation_desc FROM auth_resource aa, auth_resource_operation bb, auth_operation cc  WHERE aa.resource_id = bb.resource_id AND bb.operation_id = cc. ID ");
		sb.append(" AND aa.resource_id='" + Id + "'");
		execute.setDb_code(db_code);

		execute.setSql(sb.toString());
		RetBean retBean = CommonUtil.post(dataUrl + "/queryBySql", JSON.parseObject(JSON.toJSONString(execute)));
		List<Map<String, Object>> list = retBean.getResults();

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (CommonUtil.listIsNotNull(list)) {// 转换json格式返回
			for (Map<String, Object> map : list) {
				String resourceId = map.get("resource_id") + "";

				AuthOperation authOperation = new AuthOperation();
				authOperation.setId(map.get("id") + "");
				authOperation.setOperation_code(map.get("operation_name") + "");
				authOperation.setOperation_name(map.get("operation_code") + "");
				authOperation.setOperation_type(map.get("operation_type") + "");

				if (null == resultMap.get(resourceId)) {
					AuthResource authResource = JSON.parseObject(JSON.toJSONString(map), AuthResource.class);
					List<AuthOperation> authOperations = new ArrayList<AuthOperation>();
					authOperations.add(authOperation);
					authResource.setAuthOperation(authOperations);
					resultMap.put(resourceId, authResource);
				} else {
					AuthResource authResource = (AuthResource) resultMap.get(resourceId);
					authResource.getAuthOperation().add(authOperation);
				}

			}
		}
		list.clear();
		list.add(resultMap);
		retBean.setResults(list);
		return retBean;
	}

	@Override
	public RetBean getResourceByJson(QueryBean queryBean) throws Exception {
		RetBean retBean = null;
		queryBean.setTable(auth_resource);
		retBean = CommonUtil.post(dataUrl + "/query", JSON.toJSONString(queryBean));
		List<Map<String, Object>> resourceList = retBean.getResults();
		List<String> resourceIdArray = new ArrayList<String>();
		if (CommonUtil.listIsNotNull(resourceList)) {
			BatchOperationBean batchOperationBean = new BatchOperationBean();

			for (Map<String, Object> map : resourceList) {
				String resource_id = map.get("resource_id") + "";
				resourceIdArray.add(resource_id);
			}

			QueryBean resourceOperationQueryBean = new QueryBean();
			QueryBean resourceObjectQueryBean = new QueryBean();

			List<QueryBeanCondition> resourceOperationconditions = new ArrayList<QueryBeanCondition>();
			List<QueryBeanCondition> resourceObjectconditions = new ArrayList<QueryBeanCondition>();

			QueryBeanCondition resourceOperationBean = new QueryBeanCondition();// 拼操作表条件
			resourceOperationBean.setField1("resource_id");
			resourceOperationBean.setValue1(resourceIdArray);
			resourceOperationBean.setCond("in");
			resourceOperationconditions.add(resourceOperationBean);

			QueryBeanCondition resourceObjectBean = new QueryBeanCondition();// 拼对象表条件
			resourceObjectBean.setField1("resource_id");
			resourceObjectBean.setValue1(resourceIdArray);
			resourceObjectBean.setCond("in");
			resourceObjectconditions.add(resourceObjectBean);

			resourceOperationQueryBean.setConditions(resourceOperationconditions);
			resourceObjectQueryBean.setConditions(resourceObjectconditions);
			resourceOperationQueryBean.setTable(auth_resource_operation);
			resourceObjectQueryBean.setTable(auth_resource_object);

			List<QueryBean> queryBeans = new ArrayList<QueryBean>();
			queryBeans.add(resourceObjectQueryBean);
			queryBeans.add(resourceOperationQueryBean);
			batchOperationBean.setQueryBeans(queryBeans);

			RetBean batchretBean = CommonUtil.post(dataUrl + "/queryByList",
					JSON.parseObject(JSON.toJSONString(batchOperationBean)));

			Map<String, List<Map<String, Object>>> map = batchretBean.getBatchResults();
			System.out.println(JSON.toJSONString(map));

			for (Map<String, Object> resourceMap : resourceList) {
				List<Map<String, Object>> resourceOperations = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> resourceObjects = new ArrayList<Map<String, Object>>();
				String resource_id = resourceMap.get("resource_id") + "";
				for (Map<String, Object> resourceOperationMap : map.get(auth_resource_operation)) {// 操作表内容填充进资源
					if (resource_id.equals(resourceOperationMap.get("resource_id") + "")) {
						resourceOperations.add(resourceOperationMap);
					}
				}
				resourceMap.put("resource_operation", resourceOperations);

				for (Map<String, Object> resourceObjectMap : map.get(auth_resource_object)) {// 对象表内容填充进资源
					if (resource_id.equals(resourceObjectMap.get("resource_id") + "")) {

						resourceObjects.add(resourceObjectMap);
					}
				}
				resourceMap.put("resource_object", resourceObjects);
			}
		}
		return retBean;
	}

	@Override
	public RetBean insertObj(AuthResource authResource) throws Exception {
		ExecuteBatchSqlBean executeBatchSqlBean = new ExecuteBatchSqlBean();
		String insert_time = DateUtil.getServerTime(DateUtil.DEFAULT_TIME_FORMAT_EN);

		List<InsertBean> insertBeans = new ArrayList<InsertBean>();// 批量请求list
		InsertBean arInsertBean = BeanUtil.toInsertBean(authResource);
		arInsertBean.setTableCode(auth_resource);
		arInsertBean.setInsert_time(insert_time);
		if (arInsertBean.getData().size() > 0)
			insertBeans.add(arInsertBean);

		InsertBean aroInsertBean = BeanUtil.toInsertBean(authResource.getAuthResourceObject().toArray());
		aroInsertBean.setInsert_time(insert_time);
		aroInsertBean.setTableCode(auth_resource_object);
		if (aroInsertBean.getData().size() > 0)
			insertBeans.add(BeanUtil.toInsertBean(aroInsertBean));

		InsertBean aoInsertBean = BeanUtil.toInsertBean(authResource.getAuthResourceOperations().toArray());
		aoInsertBean.setInsert_time(insert_time);
		aoInsertBean.setTableCode(auth_resource_operation);
		if (aoInsertBean.getData().size() > 0)
			insertBeans.add(BeanUtil.toInsertBean(aoInsertBean));
		
		executeBatchSqlBean.setInsertBeans(insertBeans);

		executeBatchSqlBean.setDb_code(db_code);
		RetBean retBean = CommonUtil.post(dataUrl + "/insertBatch",
				JSON.parseObject(JSON.toJSONString(executeBatchSqlBean)));
		return retBean;
	}

}
