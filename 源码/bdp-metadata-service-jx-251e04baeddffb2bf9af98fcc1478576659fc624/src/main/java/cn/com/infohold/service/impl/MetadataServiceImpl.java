package cn.com.infohold.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import bdp.commons.dataservice.bean.Condition;
import bdp.commons.dataservice.bean.Field;
import bdp.commons.dataservice.bean.SqlBean;
import bdp.commons.dataservice.bean.Table;
import bdp.commons.dataservice.param.ExecuteBySqlBean;
import bdp.commons.dataservice.ret.RetBean;
import bdp.commons.metadata.ret.MetaData;
import cn.com.infohold.basic.util.file.PropUtil;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.core.service.impl.ServiceImpl;
import cn.com.infohold.dao.IMetadataDao;
import cn.com.infohold.entity.Metadata;
import cn.com.infohold.entity.MetadataProperty;
import cn.com.infohold.entity.MetamodelClass;
import cn.com.infohold.entity.MetamodelClassproperty;
import cn.com.infohold.service.IMetadataPropertyService;
import cn.com.infohold.service.IMetadataService;
import cn.com.infohold.service.IMetamodelClassService;
import cn.com.infohold.service.IMetamodelClasspropertyService;
import cn.com.infohold.tools.util.StringUtil;
import cn.com.infohold.util.CommonUtil;
import cn.easybdp.basic.util.http.BasicHttpUtil;
import lombok.extern.log4j.Log4j2;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@Log4j2
// (topic = "MetadataServiceImpl")
@Service
public class MetadataServiceImpl extends ServiceImpl<IMetadataDao, Metadata> implements IMetadataService {

	@Autowired
	private IMetamodelClasspropertyService metamodelClasspropertyService;
	@Autowired
	private IMetadataPropertyService metadataPropertyService;
	@Autowired
	IMetamodelClassService metamodelClassServiceImpl;

	@Override
	public JSONObject addMetadataList(String json) {
		return dao.addMetadataList(json);
	}

	@Override
	public List<Metadata> queryMetadataVOByParentIdAndCode(String parentId, String code, String metadataId) {
		return dao.queryMetadataVOByParentIdAndCode(parentId, code, metadataId);
	}

	@Override
	public List<Metadata> queryMetaDataByparentId(String parentId) {
		return dao.queryMetaDataByparentId(parentId);
	}

	@Override
	public List<Metadata> queryMetaDataByCatalogParentId(String parentId, String classType) {
		return dao.queryMetaDataByCatalogParentId(parentId, classType);
	}

	@Override
	public List<Metadata> queryMetaDataByParentIdAndClassId(String parentId, String classId, String classType) {
		return dao.queryMetaDataByParentIdAndClassId(parentId, classId, classType);
	}

	@Override
	public List<Metadata> queryMetaDataByParentIdAndNotId(String parentId, String metadataId) {
		return dao.queryMetaDataByParentIdAndNotId(parentId, metadataId);
	}

	/**
	 * 根据元数据ID查询元数据，包括模型名和路径
	 * 
	 * @param metadataId
	 * @return
	 */
	@Override
	public Metadata queryMetadataById(String metadataId) {
		return dao.queryMetadataById(metadataId);
	}

	@Override
	public JSONObject selectMetadataJson(String metadataId) {
		return dao.selectMetadataJson(metadataId);
	}

	@Override
	public JSONArray selectMetadataJsonByCatalogType(String CatalogType) {
		return dao.selectMetadataJsonByCatalogType(CatalogType);
	}

	@Override
	public JSONObject deleteMetadataByIdList(JSONArray idlist, String flag) {
		return dao.deleteMetadataByIdList(idlist, flag);
	}

	@Override
	public JSONObject deleteMetadata(String metadataId) {
		return dao.deleteMetadata(metadataId);
	}

	@Override
	public JSONObject updateMetadataJson(String json) {
		return dao.updateMetadataJson(json);
	}

	@Override
	public JSONArray selectMetadataListByParentId(String parentId) {
		return dao.selectMetadataListByParentId(parentId);
	}

	@Override
	public List<Metadata> selectMetadataList(Metadata entity) {
		return dao.selectMetadataList(entity);
	}

	@Override
	public JSONObject queryMetaDataListByCodeOrName(String codeOrName, int pageNo, int pageSize) {
		return dao.queryMetaDataListByCodeOrName(codeOrName, pageNo, pageSize);
	}

	@Override
	public JSONObject queryMetadataRelationshipBymetadataId(String metadataId, int pageNo, int pageSize) {
		return dao.queryMetadataRelationshipBymetadataId(metadataId, pageNo, pageSize);
	}

	@Override
	public List<Metadata> queryMetadataByCatalogBy(String catalogId, String classId, String metadatas) {
		return dao.queryMetadataByCatalogBy(catalogId, classId, metadatas);
	}

	@Override
	public JSONObject queryRelationAndMetaDataByClassId(String classId, String relationId, String metadataId,
			int pageNo, int pageSize) {

		return dao.queryRelationAndMetaDataByClassId(classId, relationId, metadataId, pageNo, pageSize);
	}

	@Override
	public List<Metadata> queryMataByclassId(String classId) {
		return dao.queryMataByclassId(classId);
	}

	@Override
	public Map<Object, Object> checkMetadataTypeByClassId(String classId, String metadataId, String parentId) {
		return dao.checkMetadataTypeByClassId(classId, metadataId, parentId);
	}

	@Override
	public JSONObject addMetadataJSon(String json) {
		log.debug("添加元数据json串开始。。。");
		JSONObject returnJson = new JSONObject();
		JSONObject requestObj = JSON.parseObject(json);
		if ("".equals(json) || json == null) {
			returnJson.put("code", -1);
			returnJson.put("msg", "请求json串不能为空");
			return returnJson;
		}

		String classId = (String) requestObj.get("classId");
		String metadataCode = (String) requestObj.get("metadataCode");
		String metadataName = (String) requestObj.get("metadataName");
		String catalogId = (String) requestObj.get("catalogId");
		String parentMetadata = (String) requestObj.get("parentMetadata");
		JSONArray propertyStr = requestObj.getJSONArray("property");
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = format.format(date);
		if (parentMetadata == null || "".equals(parentMetadata)) {
			parentMetadata = null;
		} else {// 如果存在父级ID则同级目录不允许元数据code相同
			List<Metadata> existList = queryMetadataVOByParentIdAndCode(parentMetadata, metadataCode, null);
			if (existList != null && existList.size() > 0) {
				returnJson.put("code", -1);
				returnJson.put("msg", metadataCode + "--元数据已经存在");
				log.debug("添加元数据json串结束。。。");
				return returnJson;
			}
		}

		// 先判断是否为数据库底层操作
		Metadata metadataVO = new Metadata();
		metadataVO.setMetadataCode(metadataCode);
		metadataVO.setClassId(classId);
		metadataVO.setParentMetadata(parentMetadata);
		// 先保存元数据
		Metadata metadata = new Metadata();
		metadata.setCatalogId(catalogId);
		metadata.setMetadataCode(metadataCode);
		metadata.setMetadataName(metadataName);

		metadata.setParentMetadata(parentMetadata);
		metadata.setClassId(classId);
		metadata.setCreateDate(currentTime);
		dao.insert(metadata);

		// 保存元数据属性
		String metadataId = metadata.getMetadataId();

		// 元数据属性不为空时保存元数据属性
		if (requestObj.containsKey("property")) {
			JSONArray proJsonObject = requestObj.getJSONArray("property");
			if (proJsonObject.size() > 0) {
				JSONObject jsonObject = (JSONObject) proJsonObject.get(0);
				Map<String, Object> colunmMap = new HashMap<String, Object>();
				colunmMap.put("class_id", classId);
				List<MetadataProperty> properties = new ArrayList<MetadataProperty>();
				List<MetamodelClassproperty> classproperties = metamodelClasspropertyService.selectByMap(colunmMap);
				for (MetamodelClassproperty metamodelClassproperty : classproperties) {
					MetadataProperty metadataProperty = new MetadataProperty();
					metadataProperty.setClassPropertyId(metamodelClassproperty.getPropertyId());
					metadataProperty.setMetadataId(metadata.getMetadataId());
					String propertyValue = StringUtil
							.getString(jsonObject.get(metamodelClassproperty.getPropertyCode()));
					metadataProperty.setPropertyValue(propertyValue);
					properties.add(metadataProperty);
				}
				metadataPropertyService.insertBatch(properties);
			}

		}
		// 获取子集childrens
		if (requestObj.containsKey("childrens")) {
			JSONArray childJsonObject = requestObj.getJSONArray("childrens");
			for (Object object : childJsonObject) {
				JSONObject jsonObject = (JSONObject) object;
				jsonObject.put("parentMetadata", metadata.getMetadataId());
				jsonObject.put("catalogId", catalogId);
				addMetadataJSon(jsonObject.toJSONString());
			}
		}
		returnJson.put("metadataId", metadataId);
		returnJson.put("code", 0);
		returnJson.put("msg", "添加成功");

		log.debug("添加元数据json串结束。。。");
		return returnJson;
	}

	@Override
	public JSONObject deleteMetadata(Map<String, Object> map) {
		// TODO Auto-generated method stub

		return dao.deleteMetadata(map);
	}

	@Override
	public JSONObject selectMetadata(Map<String, Object> map, String level) {
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("metadataList", dao.selectMetadataByMap(map, level));
		jsonObject.put("code", "0");
		jsonObject.put("msg", "交易成功");
		return jsonObject;
	}

	@Override
	public List<Map<Object, Object>> selectMetadataMap(Map<String, Object> map, String level) throws IOException {
		List<Metadata> list = dao.selectByMap(map);
		List<Map<Object, Object>> maps = new ArrayList<Map<Object, Object>>();
		for (Metadata m : list) {
			Map<Object, Object> map2 = BasicJsonUtil.getInstance()
					.toJsonMap(BasicJsonUtil.getInstance().toJsonString(m));
			Map<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("metadata_id", m.getMetadataId());
			List<MetadataProperty> properties = metadataPropertyService.selectByMap(columnMap);
			Map<String, Object> columnMap1 = new HashMap<String, Object>();
			columnMap1.put("class_id", m.getClassId());
			List<MetamodelClassproperty> classproperties = metamodelClasspropertyService.selectByMap(columnMap1);
			map2.put("property", properties);
			map2.put("class_property", classproperties);
			List<MetamodelClass> modelClassList = metamodelClassServiceImpl
					.queryMetamodelByMetadataId(m.getMetadataId());
			map2.put("modelClassList", modelClassList);

			Map<String, Object> columnMap_children = new HashMap<String, Object>();
			columnMap_children.put("parent_metadata", m.getMetadataId());

			Map<String, Object> columnMap_parent = new HashMap<String, Object>();
			columnMap_parent.put("metadata_id", m.getParentMetadata());

			if (level.equals("c")) {
				map2.put("children", selectMetadataMap(columnMap_children, "i"));
			} else if (level.equals("p")) {
				map2.put("parent", selectMetadataMap(columnMap_parent, "i"));
			} else if (level.equals("a")) {
				map2.put("children", selectMetadataMap(columnMap_children, "i"));
				map2.put("parent", selectMetadataMap(columnMap_parent, "i"));
			}
			maps.add(map2);
		}

		return maps;
	}

	@Override
	public JSONArray selectRelationMetadataJson(String param) {
		return dao.selectRelationMetadataJson(param);
	}

	@Override
	public RetBean selectRelationInfoJson(String idList, String db_code)
			throws UnsupportedEncodingException, IOException {
		BasicHttpUtil bhu = BasicHttpUtil.getInstance();
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sb = new StringBuffer();
		for (String str : idList.split(",")) {
			sb.append("'");
			sb.append(str);
			sb.append("',");
		}

		String sql = "SELECT bb.metadata_id,dd.metadata_id AS parent,dd.metadata_name, bb.class_property_id, bb.property_value, cc.property_code, cc.property_name FROM metadata aa, metadata_property bb, metamodel_classproperty cc, metadata dd WHERE aa.parent_metadata IN ( "
				+ sb.substring(0, sb.length() - 1)
				+ " ) AND aa.parent_metadata = dd.metadata_id AND aa.class_id = '232' AND bb.metadata_id = aa.metadata_id AND cc.property_id = bb.class_property_id AND cc.property_code != 'constraint_name'";

		ExecuteBySqlBean executeBySqlBean = new ExecuteBySqlBean();
		executeBySqlBean.setDb_code(db_code);
		executeBySqlBean.setSql(sql);
		map.put("param", JSON.toJSONString(executeBySqlBean));
		String json = bhu.postRequst(PropUtil.getProperty("bdp-data-service") + "queryBySql", map);
		RetBean ret = JSON.parseObject(json, RetBean.class);
		return ret;
	}

	@Override
	public RetBean selectMetadataBySqlBean(SqlBean sqlBean) throws UnsupportedEncodingException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		long startTime = System.currentTimeMillis();
		map.put("param", JSON.toJSONString(sqlBean));
		BasicHttpUtil bhu = BasicHttpUtil.getInstance();

		String json = bhu.postRequst(PropUtil.getProperty("bdp-data-service") + PropUtil.getProperty("queryMultiTable"),
				map);
		long endTime = System.currentTimeMillis();
		log.debug("selectMetadataBySqlBean {} use time {} ms", sqlBean == null ? "" : sqlBean.toString(),
				endTime - startTime);
		RetBean ret = JSON.parseObject(json, RetBean.class);
		log.debug("retBean=" + ret.getResults());
		if (ret.getResults() != null && ret.getResults().size() > 0) {
			// 把元数据的属性封装到元数据对象中
			List<Object> list = CommonUtil.packageMetadata(ret.getResults());
			ret.setObjectList(list);
			ret.setResults(null);
		}
		return ret;
	}

	@Override
	public RetBean selectMetadataByids(SqlBean sqlBean) throws UnsupportedEncodingException, IOException {
		List<Table> tables = new ArrayList<Table>();
		tables.add(new Table("metadata"));
		tables.add(new Table("metadata_property"));
		tables.add(new Table("metamodel_class"));
		tables.add(new Table("metamodel_classproperty"));
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

		sqlBean.setConditions(conditions);
		sqlBean.setDb_metadata(PropUtil.getProperty("metadata-db"));
		sqlBean.setSkip(-1);
		RetBean rets = selectMetadataBySqlBean(sqlBean);
		List<Map<String, Object>> results = rets.getResults().stream()
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
		RetBean retBean = new RetBean();
		retBean.setResults(results);
		retBean.setCount(results.size());
		retBean.setRet_code("0");
		return retBean;
	}

	@Override
	public Metadata selectMetadataCodeById(String metadataId) {
		return dao.selectMetadataCodeById(metadataId);
	}
}
