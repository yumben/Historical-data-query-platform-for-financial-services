package cn.com.infohold.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import bdp.commons.dataservice.param.QueryBean;
import bdp.commons.dataservice.param.QueryBeanCondition;
import bdp.commons.dataservice.param.QueryBeanField;
import bdp.commons.dataservice.param.QueryBeanGroup;
import bdp.commons.dataservice.param.QueryBeanOrder;
import bdp.commons.dataservice.ret.RetBean;
import bdp.commons.easyquery.ret.QueryConditions;
import bdp.commons.easyquery.ret.QueryFields;
import bdp.commons.easyquery.ret.QueryGroups;
import bdp.commons.easyquery.ret.QueryOrder;
import bdp.commons.metadata.ret.MetaData;
import bdp.commons.metadata.ret.MetadataBean;
import cn.com.infohold.basic.util.file.PropUtil;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.entity.Metadata;
import cn.com.infohold.tools.util.StringUtil;
import cn.easybdp.basic.util.http.BasicHttpUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CommonUtil {

	private static final BasicJsonUtil BJU = BasicJsonUtil.getInstance();
	private static final BasicHttpUtil BHU = BasicHttpUtil.getInstance();

	static {
		PropUtil.readProperties("properties/commons.properties");
	}

	public static RetBean post(QueryBean queryBean) {
		RetBean retBean = new RetBean();
		try {
			String url = PropUtil.getProperty("bdp-data-service");
			String values = "";
			values = BJU.toJsonString(queryBean);
			Map<String, String> param = new HashMap<>();
			param.put("param", values);
			String rets = BHU.postRequst(url, param);
			retBean = BJU.toJavaBean(rets, RetBean.class);
		} catch (IOException e) {
			e.printStackTrace();
			retBean.setRet_code("-1");
			retBean.setRet_message(e.getMessage());
		}

		return retBean;
	}

	/**
	 * QueryFields 转换成 QueryBeanField
	 * 
	 * @param fieldsList
	 * @return
	 */
	public static List<QueryBeanField> fieldChangForm(List<QueryFields> fieldsList) {
		List<QueryBeanField> fields = new ArrayList<QueryBeanField>();
		if (fieldsList != null && fieldsList.size() > 0) {
			for (QueryFields queryFields : fieldsList) {
				QueryBeanField queryBeanField = new QueryBeanField();
				queryBeanField.setName(queryFields.getName());
				queryBeanField.setAlias(queryFields.getAlias());
				queryBeanField.setTitle_name(queryFields.getTitle_name());
				queryBeanField.setFunction(queryFields.getFunc());
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
				
				if (StringUtil.isNotEmpty(queryConditions.getField2()) || StringUtil.isNotEmpty(queryConditions.getValue1()+"")) {
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
	public static Map<String, Object> groupsChangForm(List<QueryGroups> queryGroupsList,List<QueryBeanField> fieldsList) {
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
					//queryFields.setFunction("group");
					queryFields.setTitle_name(queryGroups.getField_name());
					queryFields.setDimension(true);
					fieldsList.add(queryFields);
				}
			}
			retMap.put("queryBeanGroups", queryBeanGroups);
		}
		
		//分组字段也放到展示字段
		retMap.put("fieldsList", fieldsList);
		return retMap;
		//return queryBeanGroups;
	}

	public static <T> List<T> mapsToObjs(List<Map<String, Object>> list, Class<T> clazz) throws IOException {
		List<T> resultList = new ArrayList<T>();
		if (listIsNotNull(list)) {
			for (Map<String, Object> map : list) {
				String json = JSON.toJSONString(map);
				resultList.add( JSON.parseObject(json,clazz));
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
				queryBeanOrders.add(queryBeanOrder);
			}
		}

		return queryBeanOrders;
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
	
	public static List<Object> packageMetadata(List<Map<String, Object>> list) throws IOException {
		 Map<String, MetadataBean> resultMap = new HashMap<String, MetadataBean>();//关联表结果集
		 MetadataBean json = new MetadataBean();
		 List<Object> returnList = new ArrayList<Object>();
		 for (Map<String, Object> map:list) {
			 MetadataBean metadata = CommonUtil.toQueryBean(BasicJsonUtil.getInstance().toJsonTreeMapStringFO(map), MetadataBean.class);
            String metadataId = metadata.getMetadata_id();
            if (null == resultMap.get(metadataId)) {
                json = new MetadataBean();
                json.setMetadata_id(metadata.getMetadata_id());
                json.setMetadata_code(metadata.getMetadata_code());
                json.setMetadata_name(metadata.getMetadata_name());
                json.setCatalog_id(metadata.getCatalog_id());
                json.setClass_id(metadata.getClass_id());
                json.setParent_metadata(metadata.getParent_metadata());
                json.setNotshow(metadata.getNotshow());
                JSONObject proJsonObject = new JSONObject();
                if (StringUtil.isNotEmpty(metadata.getProperty_code())) {
                    proJsonObject.put(metadata.getProperty_code(), metadata.getProperty_value());
                }
                json.setProperty(proJsonObject);
                resultMap.put(metadataId, json);
            } else {
                resultMap.get(metadataId).getProperty().put(metadata.getProperty_code(), metadata.getProperty_value());
            }

         }
		for (Entry<String, MetadataBean> metadataMap : resultMap.entrySet()) {
			 returnList.add(metadataMap.getValue());
		}
		return returnList;
	};
}
