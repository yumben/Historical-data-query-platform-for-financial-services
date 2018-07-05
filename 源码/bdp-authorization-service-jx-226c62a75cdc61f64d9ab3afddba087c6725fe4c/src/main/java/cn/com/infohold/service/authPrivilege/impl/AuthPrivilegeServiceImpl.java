package cn.com.infohold.service.authPrivilege.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jcabi.aspects.Loggable;

import bdp.commons.authorization.auth.AuthPrivilege;
import bdp.commons.authorization.auth.AuthPrivilegeObject;
import bdp.commons.authorization.auth.AuthPrivilegeObjectItems;
import bdp.commons.authorization.auth.AuthPrivilegeOperation;
import bdp.commons.authorization.auth.AuthPrivilegesBean;
import bdp.commons.dataservice.param.ExecuteBySqlBean;
import bdp.commons.dataservice.param.InsertBean;
import bdp.commons.dataservice.ret.RetBean;
import cn.com.infohold.basic.util.common.DateUtil;
import cn.com.infohold.basic.util.file.PropUtil;
import cn.com.infohold.service.authPrivilege.IAuthPrivilegeService;
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
public class AuthPrivilegeServiceImpl implements IAuthPrivilegeService {

	@Value("${db_code}")
	String db_code;
	@Value("${dataUrl}")
	String dataUrl;
	@Value("${auth_privilege}")
	String auth_privilege;
	@Value("${auth_privilege_operation}")
	String auth_privilege_operation;
	@Value("${auth_privilege_object}")
	String auth_privilege_object;
	@Value("${auth_privilege_object_items}")
	String auth_privilege_object_items;
	
	@Value("${queryBySql}")
	String queryBySql;
	@Value("${insertBatch}")
	String insertBatch;
	@Value("${executeBatchSql}")
	String executeBatchSql;
	
	@Value("${queryBatchSql}")
	String queryBatchSql;
	
	static {
		PropUtil.readProperties("properties/sql_auth.properties");
	}
	
	@Loggable(Loggable.DEBUG)
	@Override
	public RetBean authAdd(AuthPrivilegesBean authPrivilegesBean) throws IOException, IllegalArgumentException, IllegalAccessException {
		List<AuthPrivilege> authPrivilegeList = authPrivilegesBean.getAuthPrivilegeList();
		RetBean retBeanAuth = null;
		String privilege_master_id = authPrivilegesBean.getPrivilege_master_id();
		//资源权限列表
		List<Map<String, Object>> authlist = new ArrayList<Map<String, Object>>();
		//资源操作权限列表
		List<Map<String, Object>> authOperationlist = new ArrayList<Map<String, Object>>();
		//数据对象列表
		List<Map<String, Object>> authObjectList = new ArrayList<Map<String,Object>>();
		//数据字段列表
		List<Map<String, Object>> authObjectItemList = new ArrayList<Map<String,Object>>();
		
		String createTime=DateUtil.getServerTime(DateUtil.DEFAULT_TIME_FORMAT_EN);
		
		
		/************ 保存资源权限之前先把原来的资源权限删掉 ***************************/
		retBeanAuth = authDel(privilege_master_id);
		//删除成功才做新增
		if (retBeanAuth !=null && retBeanAuth.getRet_code()!=null && "0".equals(retBeanAuth.getRet_code()) && CommonUtil.listIsNotNull(authPrivilegeList)) {
			
			/*********************** 解析资源权限 **********************************/
			for (AuthPrivilege authPrivilege : authPrivilegeList) {
				String privilege_id = UUID.randomUUID().toString();
				Map<String, Object> insertAuthMap = AnalysisUtil.AnalysisObject(authPrivilege);
				insertAuthMap.put("privilege_id", privilege_id);
				insertAuthMap.put("create_time", createTime);
				authlist.add(insertAuthMap);

				//遍历资源操作权限
				if (CommonUtil.listIsNotNull(authPrivilege.getAuthPrivilegeOperationList())) {
					for (AuthPrivilegeOperation authPrivilegeOperation : authPrivilege.getAuthPrivilegeOperationList()) {
						String privilege_operation_id = UUID.randomUUID().toString();
						Map<String, Object> insertAuthOperationMap = AnalysisUtil.AnalysisObject(authPrivilegeOperation);
						insertAuthOperationMap.put("privilege_id", privilege_id);
						insertAuthOperationMap.put("privilege_operation_id", privilege_operation_id);
						authOperationlist.add(insertAuthOperationMap);
						
					}
				}
				
				//遍历资源对象权限
				String auth_object_id = "";
				if (CommonUtil.listIsNotNull(authPrivilege.getAuthPrivilegeObjectList())) {
					for (AuthPrivilegeObject authPrivilegeObject : authPrivilege.getAuthPrivilegeObjectList()) {
						auth_object_id = UUID.randomUUID().toString();
						Map<String, Object> insertAuthObjectMap = AnalysisUtil.AnalysisObject(authPrivilegeObject);
						insertAuthObjectMap.put("privilege_id", privilege_id);
						insertAuthObjectMap.put("id", auth_object_id);
						authObjectList.add(insertAuthObjectMap);
					}
				}
				//遍历数据字段权限
				if (CommonUtil.listIsNotNull(authPrivilege.getAuthPrivilegeObjectItemsList())) {
					for (AuthPrivilegeObjectItems authPrivilegeObjectItems : authPrivilege.getAuthPrivilegeObjectItemsList()) {
						String id = UUID.randomUUID().toString();
						Map<String, Object> insertAuthObjectItemMap = AnalysisUtil.AnalysisObject(authPrivilegeObjectItems);
						insertAuthObjectItemMap.put("privilege_id", privilege_id);
						insertAuthObjectItemMap.put("resource_object_id", auth_object_id);
						insertAuthObjectItemMap.put("id", id);
						authObjectItemList.add(insertAuthObjectItemMap);
					}
				}
			}
			
			
			/******************** 保存资源权限信息 ***************************/
			List<InsertBean> list = new ArrayList<InsertBean>();
			JSONObject reqObj = new JSONObject();
			InsertBean insertBean = new InsertBean();
			// 新增资源权限
			if (CommonUtil.listIsNotNull(authlist)) {
				insertBean.setTableCode(auth_privilege);
				insertBean.setData(authlist);
				list.add(insertBean);
			}
			
			//增加资源操作权限
			if (CommonUtil.listIsNotNull(authOperationlist)) {
				insertBean = new InsertBean();
				insertBean.setTableCode(auth_privilege_operation);
				insertBean.setData(authOperationlist);
				list.add(insertBean);
			}
			
			//增加数据权限
			if (CommonUtil.listIsNotNull(authObjectList)) {
				insertBean = new InsertBean();
				insertBean.setTableCode(auth_privilege_object);
				insertBean.setData(authObjectList);
				list.add(insertBean);
			}
			
			//增加数据权限
			if (CommonUtil.listIsNotNull(authObjectItemList)) {
				insertBean = new InsertBean();
				insertBean.setTableCode(auth_privilege_object_items);
				insertBean.setData(authObjectItemList);
				list.add(insertBean);
			}
			
			//调用数据服务增加权限信息
			reqObj.put("insertBeans", list);
			log.debug("添加调数据服务开始");
			retBeanAuth = CommonUtil.post(dataUrl + insertBatch, reqObj);
			log.debug("添加调数据服务开始");
			log.debug("保存权限信息完毕："+retBeanAuth.toString());
		}
		return retBeanAuth;
	}


	@Loggable(Loggable.DEBUG)
	public RetBean authDel(String privilege_master_id) throws IOException {
		RetBean retBean = null;
		if (StringUtil.isNotEmpty(privilege_master_id)) {
			/********** 删除操作权限 ***********/
			JSONObject reqObj = new JSONObject();
		    ArrayList<ExecuteBySqlBean> list = new ArrayList<ExecuteBySqlBean>();
		    ExecuteBySqlBean executeBySqlBean = new ExecuteBySqlBean();
		    //删除操作权限
		    executeBySqlBean.setSql(PropUtil.getProperty("delete_auth_privilege_operation"));
		    executeBySqlBean.setObjects(new Object[]{privilege_master_id});
		    list.add(executeBySqlBean);
		  
		     //删除数据对象单元权限
		    executeBySqlBean = new ExecuteBySqlBean();
		    executeBySqlBean.setSql(PropUtil.getProperty("delete_auth_privilege_object_items"));
		    executeBySqlBean.setObjects(new Object[]{privilege_master_id});
		    list.add(executeBySqlBean);
		    //删除数据对象权限
		    executeBySqlBean = new ExecuteBySqlBean();
		    executeBySqlBean.setSql(PropUtil.getProperty("delete_auth_privilege_object"));
		    executeBySqlBean.setObjects(new Object[]{privilege_master_id});
		    list.add(executeBySqlBean);
		   
		    //删除资源权限
		    executeBySqlBean = new ExecuteBySqlBean();
		    executeBySqlBean.setSql(PropUtil.getProperty("delete_auth_privilege"));
		    executeBySqlBean.setObjects(new Object[]{privilege_master_id});
		    list.add(executeBySqlBean);
		    reqObj.put("db_code", db_code);
		    reqObj.put("executeBySqlBeans", list);
		    log.debug("删除调数据服务开始");
		    retBean = CommonUtil.post(dataUrl + executeBatchSql, reqObj);
		    log.debug("删除调数据服务结束");
		  
		}
		return retBean;
	}

	@Loggable(Loggable.DEBUG)
	@Override
	public RetBean authQuery(String privilege_master_id) throws IOException {
		log.debug("privilege_master_id=="+privilege_master_id);
		JSONObject reqObj = new JSONObject();
		ArrayList<ExecuteBySqlBean> executeBySqlBeanList = new ArrayList<ExecuteBySqlBean>();
		List<Map<String, Object>> results = new ArrayList<Map<String,Object>>();
		//查询资源操作权限
		ExecuteBySqlBean executeBySqlBean = new ExecuteBySqlBean();
		executeBySqlBean.setDb_code(db_code);
		executeBySqlBean.setSql(PropUtil.getProperty("select_auth"));
		executeBySqlBean.setObjects(new Object[]{privilege_master_id});
		executeBySqlBean.setFlagKey("select_auth");
		executeBySqlBeanList.add(executeBySqlBean);
		
		//查询数据权限
		executeBySqlBean = new ExecuteBySqlBean();
		executeBySqlBean.setDb_code(db_code);
		executeBySqlBean.setSql(PropUtil.getProperty("select_auth_object"));
		executeBySqlBean.setObjects(new Object[]{privilege_master_id});
		executeBySqlBean.setFlagKey("select_auth_object");
		executeBySqlBeanList.add(executeBySqlBean);
		
		log.debug("查询调数据服务开始");
		reqObj.put("executeBySqlBeans", executeBySqlBeanList);
		RetBean retBeanAuth = CommonUtil.post(dataUrl + queryBatchSql, reqObj);
		log.debug("查询调数据服务结束");
		
		
		Map<String, List<Map<String, Object>>> batchResults = retBeanAuth.getBatchResults();
		if (batchResults!=null) {
			if (batchResults.get("select_auth")!=null) {
				results.addAll(batchResults.get("select_auth"));
			}
			if (batchResults.get("select_auth_object")!=null) {
				results.addAll(batchResults.get("select_auth_object"));
			}
			
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (CommonUtil.listIsNotNull(results)) {//转换json格式返回，重新组装返回的权限对象，一个资源封装成一个对象，包含该资源的操作权限列表
			for (Map<String, Object> map : results) {
				boolean operateExist=false;//操作权限存在标识
				boolean objectExist=false;//数据对象权限存在标识
				boolean objectItemExist=false;//数据对象单元权限存在标识
				String resourceId = map.get("resource_id") + "";
				AuthPrivilegeOperation authPrivilegeOperation = new AuthPrivilegeOperation();
				if (map.get("operation_id")!=null) {//判断操作权限是否存在
					authPrivilegeOperation.setOperation_id(map.get("operation_id")+"");
					authPrivilegeOperation.setResource_id(resourceId);
					operateExist = true;
				}
				AuthPrivilegeObject authPrivilegeObject = new AuthPrivilegeObject();
				if (map.get("object_id")!=null) {//判断数据对象权限是否存在
					authPrivilegeObject.setObject_id(map.get("object_id")+"");
					authPrivilegeObject.setResource_id(resourceId);
					objectExist = true;
				}
				AuthPrivilegeObjectItems authPrivilegeObjectItems= new AuthPrivilegeObjectItems();
				if (map.get("item_id")!=null) {//数据对象单元权限是否存在
					authPrivilegeObjectItems.setItem_id(map.get("item_id")+"");
					objectItemExist = true;
				}
				if (null == resultMap.get(resourceId)) {
					AuthPrivilege authPrivilege = JSON.parseObject(JSON.toJSONString(map), AuthPrivilege.class);//资源权限对象
					List<AuthPrivilegeOperation> authPrivilegeOperationList = new ArrayList<AuthPrivilegeOperation>();//操作权限列表
					List<AuthPrivilegeObject> authPrivilegeObjectList = new ArrayList<AuthPrivilegeObject>();//数据权限列表
					List<AuthPrivilegeObjectItems> authPrivilegeObjectItemsList = new ArrayList<AuthPrivilegeObjectItems>();//数据对象单元权限列表
					if (operateExist) {
						authPrivilegeOperationList.add(authPrivilegeOperation);
					}
					if (objectExist) {
						authPrivilegeObjectList.add(authPrivilegeObject);
					}
					if (objectItemExist) {
						authPrivilegeObjectItemsList.add(authPrivilegeObjectItems);
					}
					authPrivilege.setPrivilege_id(map.get("privilege_id")+"");
					authPrivilege.setAuthPrivilegeOperationList(authPrivilegeOperationList);
					authPrivilege.setAuthPrivilegeObjectList(authPrivilegeObjectList);
					authPrivilege.setAuthPrivilegeObjectItemsList(authPrivilegeObjectItemsList);
					resultMap.put(resourceId, authPrivilege);
				} else {
					AuthPrivilege authPrivilege = (AuthPrivilege) resultMap.get(resourceId);
					authPrivilege.setPrivilege_id(map.get("privilege_id")+"");
					if (operateExist) {
						authPrivilege.getAuthPrivilegeOperationList().add(authPrivilegeOperation);
					}
					if (objectExist) {
						authPrivilege.getAuthPrivilegeObjectList().add(authPrivilegeObject);
					}
					if (objectItemExist) {
						authPrivilege.getAuthPrivilegeObjectItemsList().add(authPrivilegeObjectItems);
					}
					
				}
				
			}
		}
		ArrayList<Object> retList = new ArrayList<Object>();
		//遍历map集合最终得到list保存到返回bean的objectList
		for (Entry<String, Object> entry : resultMap.entrySet()) {
			retList.add(entry.getValue());
		}
		retBeanAuth.setResults(null);
		retBeanAuth.setBatchResults(null);
		retBeanAuth.setObjectList(retList);
	
		log.debug("查询权限返回="+retBeanAuth.toString());
		return retBeanAuth;
	}

}
