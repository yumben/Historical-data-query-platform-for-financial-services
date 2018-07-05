package cn.com.infohold.dao.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;

import bdp.commons.metadata.ret.MetaData;
import cn.com.infohold.basic.util.file.PropUtil;
import cn.com.infohold.core.dao.impl.MyBatisDaoImpl;
import cn.com.infohold.dao.IMetadataCatalogDao;
import cn.com.infohold.dao.IMetadataDao;
import cn.com.infohold.dao.IMetadataPropertyDao;
import cn.com.infohold.dao.IMetadataPropertyKeyDao;
import cn.com.infohold.dao.IMetadataRelationDao;
import cn.com.infohold.dao.IMetamodelClassDao;
import cn.com.infohold.dao.IMetamodelClasspropertyDao;
import cn.com.infohold.entity.Metadata;
import cn.com.infohold.entity.MetadataCatalog;
import cn.com.infohold.entity.MetadataProperty;
import cn.com.infohold.entity.MetadataPropertyKey;
import cn.com.infohold.entity.MetadataRelation;
import cn.com.infohold.entity.MetamodelClass;
import cn.com.infohold.entity.MetamodelClassproperty;
import cn.com.infohold.mapper.MetadataCatalogMapper;
import cn.com.infohold.mapper.MetadataMapper;
import cn.com.infohold.mapper.MetadataPropertyMapper;
import cn.com.infohold.mapper.MetadataRelationMapper;
import cn.com.infohold.service.IAppparService;
import cn.com.infohold.service.IMetadataDatabaseService;
import cn.com.infohold.tools.util.CommonUtil;
import cn.com.infohold.tools.util.StringUtil;
import cn.com.infohold.util.DatabaseUtil;
import cn.com.infohold.util.NumberUtil;
import cn.com.infohold.util.TypeConversionUtil;
import lombok.extern.log4j.Log4j2;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@Log4j2(topic = "MetadataDaoImpl")
@Service
public class MetadataDaoImpl extends MyBatisDaoImpl<MetadataMapper, Metadata> implements IMetadataDao {

	@Autowired
	MetadataMapper metadataMapper;
	@Autowired
	IMetadataRelationDao metadataRelationServiceImpl;
	@Autowired
	IMetamodelClassDao metamodelClassServiceImpl;
	@Autowired
	IMetamodelClasspropertyDao metamodelClasspropertyServiceImpl;
	@Autowired
	IMetadataPropertyDao metadataPropertyServiceImpl;
	@Autowired
	IAppparService appparService;
	@Autowired
	IMetadataCatalogDao metadataCatalogServiceImpl;
	@Autowired
	MetadataPropertyMapper metadataPropertyMapper;
	@Autowired
	MetadataRelationMapper metadataRelationMapper;
	@Autowired
	MetadataCatalogMapper metadataCatalogMapper;
	@Autowired
	IAppparService appparServiceImpl;
	@Autowired
	IMetadataPropertyKeyDao metadataPropertyKeyServiceImpl;
	@Autowired
	DatabaseUtil databaseUtil;

	@Autowired
	IMetadataDatabaseService metadataDatabaseServiceImpl;

	static {
		PropUtil.readProperties("properties/commons.properties");
	}

	// MetadataDatabaseServiceImpl metadataDatabaseServiceImpl =
	// (MetadataDatabaseServiceImpl)
	// BeanTools.getBean(MetadataDatabaseServiceImpl.class);
	@Override
	public List<Metadata> selectMetadataList(Metadata entity, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		EntityWrapper<Metadata> wrapper = new EntityWrapper<Metadata>();
		if (!"".equals(StringUtil.getString(entity.getMetadataId()))) {
			wrapper.like("metadata_id", entity.getMetadataId());
		}

		if (!"".equals(StringUtil.getString(entity.getCatalogId()))) {
			wrapper.eq("catalog_id", entity.getCatalogId());
		}

		if (!"".equals(StringUtil.getString(entity.getMetadataCode()))) {
			wrapper.eq("metadata_code", entity.getMetadataCode());
		}

		if (!"".equals(StringUtil.getString(entity.getMetadataName()))) {
			wrapper.eq("metadata_name", entity.getMetadataName());
		}

		if (!"".equals(StringUtil.getString(entity.getParentMetadata()))) {
			wrapper.eq("parent_metadata", entity.getParentMetadata());
		}

		if (!"".equals(StringUtil.getString(entity.getClassId()))) {
			wrapper.eq("class_id", entity.getClassId());
		}

		if (!"".equals(StringUtil.getString(entity.getNotshow()))) {
			wrapper.eq("notshow", entity.getNotshow());
		}

		if (!"".equals(StringUtil.getString(entity.getCreateDate()))) {
			wrapper.eq("create_date", entity.getCreateDate());
		}

		if (!"".equals(StringUtil.getString(entity.getEditDate()))) {
			wrapper.eq("edit_date", entity.getEditDate());
		}

		if (!"".equals(StringUtil.getString(entity.getEditName()))) {
			wrapper.eq("edit_name", entity.getEditName());
		}

		pageNo = CommonUtil.offsetCurrent(pageNo, pageSize);
		RowBounds rowBounds = new RowBounds(pageNo, pageSize);
		return metadataMapper.selectPage(rowBounds, wrapper);
	}

	@Override
	public List<Metadata> selectMetadataList(Metadata entity) {
		// TODO Auto-generated method stub
		EntityWrapper<Metadata> wrapper = new EntityWrapper<Metadata>();
		if (!"".equals(StringUtil.getString(entity.getMetadataId()))) {
			wrapper.like("metadata_id", entity.getMetadataId());
		}

		if (!"".equals(StringUtil.getString(entity.getCatalogId()))) {
			wrapper.eq("catalog_id", entity.getCatalogId());
		}

		if (!"".equals(StringUtil.getString(entity.getMetadataCode()))) {
			wrapper.eq("metadata_code", entity.getMetadataCode());
		}

		if (!"".equals(StringUtil.getString(entity.getMetadataName()))) {
			wrapper.eq("metadata_name", entity.getMetadataName());
		}

		if (!"".equals(StringUtil.getString(entity.getParentMetadata()))) {
			wrapper.eq("parent_metadata", entity.getParentMetadata());
		}

		if (!"".equals(StringUtil.getString(entity.getClassId()))) {
			wrapper.eq("class_id", entity.getClassId());
		}

		if (!"".equals(StringUtil.getString(entity.getNotshow()))) {
			wrapper.eq("notshow", entity.getNotshow());
		}

		if (!"".equals(StringUtil.getString(entity.getCreateDate()))) {
			wrapper.eq("create_date", entity.getCreateDate());
		}

		if (!"".equals(StringUtil.getString(entity.getEditDate()))) {
			wrapper.eq("edit_date", entity.getEditDate());
		}

		if (!"".equals(StringUtil.getString(entity.getEditName()))) {
			wrapper.eq("edit_name", entity.getEditName());
		}

		return metadataMapper.selectList(wrapper);
	}

	// @Override
	/*
	 * public JSONObject selectJson(String metadataId) { EntityWrapper<Metadata>
	 * ew = new EntityWrapper<Metadata>(); // 存放指标组件id List<String> idsList =
	 * new ArrayList<String>(); // 存放指标组件classid List<String> classIdsList = new
	 * ArrayList<String>(); //来源表的父级数据全部放这里 List<List<Metadata>> contexList =
	 * new ArrayList<List<Metadata>>(); // 根据id查询子类以及他本身的list List<Metadata>
	 * metadataIdList = metadataMapper .selectList(ew.eq("metadata_id",
	 * metadataId).or().eq("parent_metadata", metadataId)); //当前id的对象 Metadata
	 * currentMetadata = new Metadata(); for (Metadata metadata :
	 * metadataIdList) { if (!metadata.getMetadataId().equals(metadataId)) {
	 * idsList.add(metadata.getMetadataId());
	 * classIdsList.add(metadata.getClassId()); } else { currentMetadata =
	 * metadata; } } // 得到数据来源表的内容 List<Metadata> tableList =
	 * metadataMapper.getTableList(idsList, metadataId);
	 * selectParentById(contexList,tableList); Collections.reverse(contexList);
	 * List<Node> nodeList = new ArrayList<Node>(); List<Link> linkList = new
	 * ArrayList<Link>();
	 * 
	 * JSONObject locationInfo =
	 * assembledNode(contexList,nodeList,linkList,currentMetadata);//这里画图
	 * 
	 * BloodNodes bloodNodes = new BloodNodes();
	 * 
	 * // 设置目标节点 Node node = new Node(); node.level = 1; // node.y = vh *
	 * tableList.size(); node.y = 150; node.text =
	 * currentMetadata.getMetadataName(); node.nodeType = "index"; node.image =
	 * "/pms-web/resource/images/tags/tags_data.png"; // node.image =
	 * "tags_data.png"; node.x = locationInfo.getIntValue("x");
	 * //bloodNodes.nodes.add(node); nodeList.add(node); bloodNodes.nodes =
	 * nodeList; bloodNodes.links = linkList; for (Node n : bloodNodes.nodes) {
	 * log.debug(n.text + " [" + n.x + "," + n.y + "]" + "   level:" + n.level);
	 * }
	 * 
	 * 
	 * List<IndexInfo> indexInfos = metadataMapper.getIndexInfo(idsList,
	 * classIdsList); String dimension_index = ""; IndexInfo index = new
	 * IndexInfo();
	 * 
	 */
	/**
	 * *****************************拼装信息*********************
	 *//*
		 * List<IndexInfo> replaceInfos = new ArrayList<IndexInfo>(); for (int i
		 * = 0; i < indexInfos.size(); i++) { IndexInfo indexInfo =
		 * indexInfos.get(i); if
		 * (indexInfo.getClass_code().equals("dimension_index")) {
		 * dimension_index += indexInfo.getMetadata_name() + "、"; index =
		 * indexInfo; continue; } if
		 * (StringUtil.isNotEmpty(indexInfo.getProperty_value())) { String
		 * constraint = indexInfo.getMetadata_name() +"："+
		 * indexInfo.getProperty_value();
		 * indexInfo.setMetadata_name(constraint); }
		 * replaceInfos.add(indexInfo); } dimension_index =
		 * dimension_index.substring(0, dimension_index.length() - 1);
		 * index.setMetadata_name(dimension_index); replaceInfos.add(index);
		 */

	/**
	 * *****************************拼装信息*********************
	 *//*
		 * 
		 * JSONObject jsonObject = new JSONObject(); jsonObject.put("vo",
		 * bloodNodes); jsonObject.put("indexInfo", replaceInfos);
		 * jsonObject.put("locationInfo", locationInfo); return jsonObject; }
		 */

	/*
	 * @Override public JSONObject getMetaData(String metadataId) {
	 * List<Metadata> fromMetadatas =
	 * metadataMapper.getFromMetaData(metadataId); Metadata toMetadatas =
	 * metadataMapper.selectMetadataById(metadataId); BloodNodes bloodNodes =
	 * new BloodNodes(); if (fromMetadatas.size() > 0) {
	 * 
	 * // tagsRuleNodes.text = vo.tags_name; int level = 1; bloodNodes.level =
	 * 1; int vh = 400 / fromMetadatas.size() - 30; int currentY = 0; //
	 * 设置数据来源的节点 for (Metadata metadata : fromMetadatas) { Node node = new
	 * Node(); selectContextById(metadata.getMetadataId(),node.getListMap());
	 * node.level = 1; node.y = level == 1 ? 0 : currentY + vh; currentY =
	 * node.y; node.x = 150; node.text = metadata.getMetadataName();
	 * node.nodeType = "tags"; node.image =
	 * "/pms-web/resource/images/tags/base_tags.png"; // node.image =
	 * "tags_data.png";
	 * 
	 * if (bloodNodes.level <= level) { node.level = level; }
	 * 
	 * bloodNodes.nodes.add(node); level++; Link link = new Link();
	 * link.fromNode = metadata.getMetadataName(); link.toNode =
	 * toMetadatas.getMetadataName(); bloodNodes.links.add(link); }
	 * 
	 * 
	 * // 设置目标节点 Node node = new Node();
	 * selectContextById(metadataId,node.getListMap()); node.level = 1; //
	 * node.y = vh * tableList.size(); node.y =130; node.text =
	 * toMetadatas.getMetadataName(); node.nodeType = "index"; node.image =
	 * "/pms-web/resource/images/tags/tags_data.png"; // node.image =
	 * "tags_data.png"; node.x = 400; bloodNodes.nodes.add(node); for (Node n :
	 * bloodNodes.nodes) { log.debug(n.text + " [" + n.x + "," + n.y + "]" +
	 * "   level:" + n.level); } log.debug(bloodNodes.level); } JSONObject
	 * jsonObject = new JSONObject(); jsonObject.put("vo", bloodNodes); return
	 * jsonObject; }
	 */

	@Override
	public void selectContextById(String metadataId, List<Map<String, String>> listMap) {
		Metadata metadata = metadataMapper.selectContextById(metadataId);
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtil.isNotEmpty(metadata.getParentMetadata())) {
			Metadata parentmetadata = metadataMapper.selectContextById(metadata.getParentMetadata());
			map.put("metadataName", parentmetadata.getMetadataName());
			map.put("className", parentmetadata.getClassName());
			listMap.add(map);
			selectContextById(metadata.getParentMetadata(), listMap);
		}
	}

	/**
	 * 递归获得每一级的元数据
	 *
	 * @param contexList
	 *            来源表所有父级
	 * @param tableList
	 *            来源表
	 */
	public void selectParentById(List<List<Metadata>> contexList, List<Metadata> tableList) {
		contexList.add(tableList);
		EntityWrapper<Metadata> ew = new EntityWrapper<Metadata>();
		List<String> parentIdsList = new ArrayList<String>();
		Set<String> distinctSet = new HashSet<String>();
		boolean flag = false;
		for (Metadata metadata : tableList) {
			if (StringUtil.isNotEmpty(metadata.getParentMetadata())) {
				distinctSet.add(metadata.getParentMetadata());
				flag = true;// 进来这里说明还有下一级
			}
		}
		if (flag) {
			parentIdsList.addAll(distinctSet);// 去重
			ew.in("metadata_id", parentIdsList);
			List<Metadata> parentList = metadataMapper.selectList(ew);
			selectParentById(contexList, parentList);
		}
	}

	/**
	 * 组装节点信息
	 *
	 * @param contexList
	 *            装了数据的list
	 * @param nodeList
	 *            节点list
	 * @param linkList
	 *            线list
	 * @param currentMetadata
	 *            当前指标的对象
	 */
	/*
	 * public JSONObject assembledNode(List<List<Metadata>> contexList,
	 * List<Node> nodeList, List<Link> linkList, Metadata currentMetadata) {
	 * List<Metadata> metadataName = new ArrayList<Metadata>(); List<Metadata>
	 * parentMetadataName = new ArrayList<Metadata>(); int currentY =
	 * 100,currentX = 10,htmlY = 0;//确定节点的位置信息 JSONObject locationInfo = new
	 * JSONObject(); for (int i = 0; i < contexList.size(); i++)
	 * {//遍历大的集合去出每一级的记录 List<Metadata> metadataList = contexList.get(i); for
	 * (Metadata metadata : metadataList) {
	 * 
	 * Node node = new Node(); node.level = 1; node.y = currentY; currentY =
	 * node.y + 75; node.x = currentX; node.text = metadata.getMetadataName();
	 * node.nodeType = "tags"; node.image =
	 * "/pms-web/resource/images/tags/base_tags.png";
	 * metadataName.add(metadata);
	 * 
	 * if (i + 1 == contexList.size()) {//如果是最后一级了就和当前指标进行连线 Link link = new
	 * Link(); link.fromNode = metadata.getMetadataName(); link.toNode =
	 * currentMetadata.getMetadataName(); linkList.add(link); }
	 * nodeList.add(node); if (parentMetadataName.size() > 0) {//和父级连线 for
	 * (Metadata parentMetadata : parentMetadataName) { if
	 * (parentMetadata.getMetadataId().equals(metadata.getParentMetadata())) {
	 * Link link = new Link(); link.fromNode = parentMetadata.getMetadataName();
	 * link.toNode = metadata.getMetadataName(); linkList.add(link); } } }
	 * currentY++; } parentMetadataName.clear();
	 * parentMetadataName.addAll(metadataName); metadataName.clear(); if (htmlY
	 * < currentY) { htmlY = currentY; } currentY = 100; currentX += 200; }
	 * locationInfo.put("x", currentX); locationInfo.put("y", htmlY); return
	 * locationInfo; }
	 */
	/**
	 * 添加元数据
	 */
	@Override
	public JSONObject addMetadataList(String json) {
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
		JSONArray propertyStr1 = requestObj.getJSONArray("propertyArr");
		String[] propertyArr = TypeConversionUtil.JSONArrayToString(propertyStr1);
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = format.format(date);

		// 元数据code唯一校验
		if (checkMetadataCodeIsExist(metadataCode, null)) {
			returnJson.put("code", -1);
			returnJson.put("msg", "元数据代码已经存在，请重新输入");
			log.debug("元数据代码已经存在，请重新输入");
			return returnJson;
		}

		// 先判断是否为数据库底层操作
		Metadata metadataVO = new Metadata();
		metadataVO.setMetadataCode(metadataCode);
		metadataVO.setClassId(classId);
		metadataVO.setParentMetadata(parentMetadata);
		Map<String, String> resMap = metadataDatabaseServiceImpl.addCheckDatabaseOperator(metadataVO, propertyArr);
		if (resMap != null && resMap.size() != 0 && !"0000".equals(resMap.get("returnCode"))) {
			returnJson.put("code", -1);
			returnJson.put("msg", resMap.get("msg"));

			log.debug("添加元数据数据库操作校验不通过。。。");
			return returnJson;
		}

		// 先保存元数据
		Metadata metadata = new Metadata();
		metadata.setCatalogId(catalogId);
		metadata.setMetadataCode(metadataCode);
		metadata.setMetadataName(metadataName);

		metadata.setParentMetadata(parentMetadata);
		metadata.setClassId(classId);
		metadata.setCreateDate(currentTime);
		metadataMapper.insert(metadata);

		// 保存元数据属性
		String metadataId = metadata.getMetadataId();
		List<MetadataProperty> addProList = new ArrayList<MetadataProperty>();
		JSONArray proJsonObject = requestObj.getJSONArray("property");
		// 元数据属性不为空时保存元数据属性
		if (proJsonObject != null) {
			String orderString = "";
			// 判断是否为仪表盘模型 1-仪表盘页面，2-仪表盘元素
			/*
			 * if ("1".equals(metadataType)) { savePannelPage(metadataId,
			 * (JSONObject)proJsonObject.get(0)); }else if
			 * ("2".equals(metadataType)) {////判断是否为仪表盘元素 orderString =
			 * savePannelPageEle(metadataId,pannelPageId,
			 * (JSONObject)proJsonObject.get(0));
			 * 
			 * }
			 */

			List<MetamodelClassproperty> classproperties = metamodelClasspropertyServiceImpl
					.selectMetamodelClasspropertyList(classId);
			for (int i = 0; i < proJsonObject.size(); i++) {
				JSONObject property = proJsonObject.getJSONObject(i); // 从jsonArray中解析Object
				property.put("item_orderby", orderString);
				Iterator<String> it = property.keySet().iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					for (int j = 0; j < classproperties.size(); j++) {
						MetamodelClassproperty metamodelClassproperty = classproperties.get(j);
						if (key.equals(metamodelClassproperty.getPropertyCode())) {
							MetadataProperty metadataProperty = new MetadataProperty();
							metadataProperty.setClassPropertyId(metamodelClassproperty.getPropertyId());
							metadataProperty.setMetadataId(metadataId);
							metadataProperty.setCreateDate(currentTime);
							metadataProperty.setPropertyValue(property.get(key) + "");
							addProList.add(metadataProperty);
							break;
						}
					}
				}

			}

		}

		// 获取子集childrens
		JSONArray childJsonObject = requestObj.getJSONArray("childrens");
		if (childJsonObject != null) {
			addProList = traversalChildMetadata(childJsonObject, metadataId, catalogId, addProList);
		}

		// 批量保存元数据属性值
		if (addProList != null && addProList.size() > 0) {
			for (int i = 0; i < addProList.size(); i++) {
				metadataPropertyServiceImpl.insert(addProList.get(i));
			}
		}
		returnJson.put("metadataId", metadataId);
		returnJson.put("code", 0);
		returnJson.put("msg", "添加成功");

		log.debug("添加元数据json串结束。。。");
		return returnJson;
	}

	/**
	 * 保存子集元数据
	 *
	 * @param childJsonObject
	 * @param parentMetadataId
	 * @param catalogId
	 * @param addProList
	 * @return
	 */
	public List<MetadataProperty> traversalChildMetadata(JSONArray childJsonObject, String parentMetadataId,
			String catalogId, List<MetadataProperty> addProList) {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = format.format(date);
		if (childJsonObject != null) {
			for (int i = 0; i < childJsonObject.size(); i++) {
				JSONObject jsonObject = childJsonObject.getJSONObject(i);
				String classId = (String) jsonObject.get("classId");
				String metadataCode = (String) jsonObject.get("metadataCode");
				String metadataName = (String) jsonObject.get("metadataName");
				JSONArray proJsonObject = jsonObject.getJSONArray("property");
				String[] propertyArr = TypeConversionUtil.JSONArrayToString(proJsonObject);
				// 元数据code唯一校验
				if (checkMetadataCodeIsExist(metadataCode, null)) {
					log.debug(metadataCode + "-元数据代码已经存在，请重新输入");
					return null;
				}

				// 先判断是否为数据库底层操作
				Metadata metadataVO = new Metadata();
				metadataVO.setMetadataCode(metadataCode);
				metadataVO.setClassId(classId);
				metadataVO.setParentMetadata(parentMetadataId);
				Map<String, String> resMap = metadataDatabaseServiceImpl.addCheckDatabaseOperator(metadataVO,
						propertyArr);
				if (resMap != null && !"0000".equals(resMap.get("returnCode"))) {

					log.debug("添加元数据数据库操作校验不通过。。。");
					return null;
				}

				// 先保存元数据
				Metadata metadata = new Metadata();
				metadata.setCatalogId(catalogId);
				metadata.setMetadataCode(metadataCode);
				metadata.setMetadataName(metadataName);
				metadata.setParentMetadata(parentMetadataId);
				metadata.setClassId(classId);
				metadata.setCreateDate(currentTime);
				metadataMapper.insert(metadata);

				// 保存元数据属性
				String metadataId = metadata.getMetadataId();
				List<MetamodelClassproperty> classproperties = metamodelClasspropertyServiceImpl
						.selectMetamodelClasspropertyList(classId);
				if (proJsonObject != null) {
					for (int j = 0; j < proJsonObject.size(); j++) {
						JSONObject property = proJsonObject.getJSONObject(j); // 从jsonArray中解析Object
						Iterator<String> it = property.keySet().iterator();
						while (it.hasNext()) {
							String key = (String) it.next();
							for (int k = 0; k < classproperties.size(); k++) {
								MetamodelClassproperty metamodelClassproperty = classproperties.get(k);
								if (key.equals(metamodelClassproperty.getPropertyCode())) {
									MetadataProperty metadataProperty = new MetadataProperty();
									metadataProperty.setClassPropertyId(metamodelClassproperty.getPropertyId());
									metadataProperty.setMetadataId(metadataId);
									metadataProperty.setCreateDate(currentTime);
									metadataProperty.setPropertyValue((String) property.get(key));
									addProList.add(metadataProperty);
								}
							}
						}
					}
					// 判断是否为仪表盘模型 1-仪表盘页面，2-仪表盘元素
					/*
					 * if (metadataType.equals("1")) {
					 * savePannelPage(metadataId,
					 * (JSONObject)proJsonObject.get(0)); }else if
					 * (metadataType.equals("2")) {////判断是否为仪表盘元素
					 * savePannelPageEle(metadataId,pannelPageId,
					 * (JSONObject)proJsonObject.get(0)); }
					 */
				}

				// 获取子集childrens
				JSONArray childJsonObjectLast = jsonObject.getJSONArray("childrens");
				if (childJsonObjectLast != null) {
					addProList = traversalChildMetadata(childJsonObjectLast, metadataId, catalogId, addProList);

				} else {
					continue;
				}

			}

		}

		return addProList;
	}

	/**
	 * 根据父ID查询元数据
	 */
	@Override
	public List<Metadata> queryMetaDataByparentId(String parentId) {
		EntityWrapper<Metadata> wrapper = new EntityWrapper<Metadata>();
		if (!"".equals(StringUtil.getString(parentId))) {
			wrapper.like("parent_metadata", parentId);
		}
		return metadataMapper.selectList(wrapper);
	}

	/**
	 * 根据父ID和code查询元数据
	 */
	@Override
	public List<Metadata> queryMetadataVOByParentIdAndCode(String parentId, String code, String metadataId) {
		EntityWrapper<Metadata> wrapper = new EntityWrapper<Metadata>();
		if (!"".equals(StringUtil.getString(parentId))) {
			wrapper.eq("parent_metadata", parentId);
		}
		if (!"".equals(StringUtil.getString(code))) {
			wrapper.eq("metadata_code", code);
		}
		if (!"".equals(StringUtil.getString(metadataId))) {
			wrapper.notIn("metadata_id", metadataId);
		}
		return metadataMapper.selectList(wrapper);
	}

	/**
	 * 根据metadataId 查询元数据json串
	 */
	@Override
	public JSONObject selectMetadataJson(String metadataId) {
		log.debug("查询元数据json串开始。。。");
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis();

		JSONObject json = new JSONObject();

		// Metadata metadata = metadataMapper.selectMetadataById(metadataId);
		Metadata metadata = metadataMapper.selectById(metadataId);
		endTime = System.currentTimeMillis();
		log.debug("metadataMapper.selectById {} use time {}", metadataId, endTime - startTime);
		startTime = System.currentTimeMillis();
		if (metadata != null) {
			json.put("metadataId", metadata.getMetadataId());
			json.put("metadataCode", metadata.getMetadataCode());
			json.put("metadataName", metadata.getMetadataName());
			json.put("catalogId", metadata.getCatalogId());
			json.put("classId", metadata.getClassId());
			json.put("parentMetadata", metadata.getParentMetadata());
			json.put("notshow", metadata.getNotshow());
			json.put("createDate", metadata.getCreateDate());

			JSONObject property = metadataPropertyServiceImpl
					.queryMetaDataPropertyByMetadataId(metadata.getMetadataId());
			endTime = System.currentTimeMillis();
			log.debug("metadataPropertyServiceImpl.queryMetaDataPropertyByMetadataId {} use time {}",
					metadata.getMetadataId(), endTime - startTime);
			startTime = System.currentTimeMillis();
			json.put("property", property);
			JSONArray children = new JSONArray();
			// children =
			// selectMetadataListByParentId(metadata.getMetadataId());
			List<String> childrenList = new ArrayList<>();
			childrenList.add(metadata.getMetadataId());
			Map<String, JSONArray> child = selectMetadataListByParentIdList(childrenList);
			if (child != null) {
				children = child.get(metadata.getMetadataId());
			}
			endTime = System.currentTimeMillis();
			log.debug("selectMetadataListByParentIdList {} use time {}", metadata.getMetadataId(), endTime - startTime);
			startTime = System.currentTimeMillis();
			// String children = "";
			json.put("children", children);
		}
		log.debug("查询元数据json串结束。。。");
		return json;
	}

	/**
	 * 更新元数据
	 */
	@Override
	public JSONObject updateMetadataJson(String json) {
		log.debug("更新元数据json串开始。。。");
		JSONObject returnJson = new JSONObject();
		Map<String, String> resMap = null;
		JSONObject requestObj = JSON.parseObject(json);
		String metadataId = (String) requestObj.get("metadataId");
		String metadataCode = (String) requestObj.get("metadatCode");
		String metadataName = (String) requestObj.get("metadatName");
		JSONArray jsonArray = requestObj.getJSONArray("propertyArr");
		String[] propertyArr = TypeConversionUtil.JSONArrayToString(jsonArray);
		List<Metadata> updataMetadataList = new ArrayList<Metadata>();
		List<MetadataProperty> updataMetadataPropertyList = new ArrayList<MetadataProperty>();
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = format.format(date);
		try {
			// 元数据代码唯一校验
			if (checkMetadataCodeIsExist(metadataCode, metadataId)) {
				returnJson.put("code", -1);
				returnJson.put("msg", "元数据代码已经存在，请重新输入");
				log.debug("元数据代码已经存在，请重新输入");
				return returnJson;
			}
			// Metadata metadata= metadataMapper.selectMetadataById(metadataId);
			Metadata metadata = metadataMapper.selectById(metadataId);
			if (metadata != null) {
				if (propertyArr != null && propertyArr.length > 0) {// 属性信息为空，则不改动数据库操作，否则需要改数据库操作
					// 判断是否修改数据库或者表或者表字段
					resMap = metadataDatabaseServiceImpl.updateCheckDatabaseOperator(metadata, propertyArr);
					if (resMap != null && resMap.size() != 0 && !"0000".equals(resMap.get("returnCode"))) {
						returnJson.put("code", -1);
						returnJson.put("msg", resMap.get("msg"));

						log.debug("添加元数据数据库操作校验不通过。。。");
						return returnJson;
					}
				}

				metadata.setMetadataCode(metadataCode);
				metadata.setMetadataName(metadataName);
				metadata.setEditDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				updataMetadataList.add(metadata);
				JSONArray proJsonObject = requestObj.getJSONArray("property");

				if (propertyArr != null && propertyArr.length > 0) {
					// 删除元数据属性
					Map<String, Object> columnMap = new HashMap<String, Object>();
					columnMap.put("metadata_id", metadataId);
					metadataPropertyMapper.deleteByMap(columnMap);

					List<MetamodelClassproperty> classproperties = metamodelClasspropertyServiceImpl
							.selectMetamodelClasspropertyList(metadata.getClassId());
					for (int i = 0; i < proJsonObject.size(); i++) {// 插入元数据属性
						JSONObject property = proJsonObject.getJSONObject(i); // 从jsonArray中解析Object
						Iterator<String> it = property.keySet().iterator();
						while (it.hasNext()) {
							String key = (String) it.next();
							for (int j = 0; j < classproperties.size(); j++) {
								MetamodelClassproperty metamodelClassproperty = classproperties.get(j);
								if (key.equals(metamodelClassproperty.getPropertyCode())) {
									MetadataProperty metadataProperty = new MetadataProperty();
									metadataProperty.setClassPropertyId(metamodelClassproperty.getPropertyId());
									metadataProperty.setMetadataId(metadataId);
									metadataProperty.setCreateDate(currentTime);
									metadataProperty.setPropertyValue(property.get(key) + "");
									metadataPropertyMapper.insert(metadataProperty);
								}
							}
						}

					}

				}

				// 更新子集
				JSONArray childJsonObject = requestObj.getJSONArray("childrens");
				if (childJsonObject != null) {
					returnJson = updateSubJsonObject(childJsonObject, updataMetadataList, updataMetadataPropertyList,
							currentTime);
					if (returnJson != null) {
						if (returnJson.get("updataMetadataList") != null) {
							updateBatchById(updataMetadataList);
						}
						if (returnJson.get("updataMetadataPropertyList") != null) {
							metadataPropertyServiceImpl.updateBatchById(updataMetadataPropertyList);
						}
					}
				} else {

					if (updataMetadataList != null && updataMetadataList.size() > 0) {
						updateBatchById(updataMetadataList);
					}
					if (updataMetadataPropertyList != null && updataMetadataPropertyList.size() > 0) {
						metadataPropertyServiceImpl.updateBatchById(updataMetadataPropertyList);
					}

					returnJson.put("code", 0);
					returnJson.put("msg", "修改成功");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			returnJson.put("code", -2);
			returnJson.put("msg", "操作失败");
			return returnJson;
		}

		log.debug("更新元数据json串结束。。。");
		returnJson.remove("updataMetadataList");
		returnJson.remove("updataMetadataPropertyList");
		return returnJson;
	}

	/**
	 * 更新子集元数据
	 */
	@Override
	public JSONObject updateSubJsonObject(JSONArray childJsonObject, List<Metadata> updataMetadataList,
			List<MetadataProperty> updataMetadataPropertyList, String time) {
		log.debug("更新子集元数据json串开始。。。");
		JSONObject returnJson = new JSONObject();
		Map<String, String> resMap = null;
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = format.format(date);
		if (childJsonObject != null) {
			for (int i = 0; i < childJsonObject.size(); i++) {
				JSONObject jsonObject = childJsonObject.getJSONObject(i);
				String metadataId = (String) jsonObject.get("metadataId");
				String metadataCode = (String) jsonObject.get("metadataCode");
				String metadataName = (String) jsonObject.get("metadataName");

				// 元数据代码唯一校验
				if (checkMetadataCodeIsExist(metadataCode, metadataId)) {
					returnJson.put("code", -1);
					returnJson.put("msg", "元数据代码已经存在，请重新输入");
					log.debug("元数据代码已经存在，请重新输入");
					return returnJson;
				}

				Metadata metadata = metadataMapper.selectById(metadataId);

				if (metadata != null) {

					metadata.setMetadataCode(metadataCode);
					metadata.setMetadataName(metadataName);
					metadata.setEditDate(currentTime);
					updataMetadataList.add(metadata);
					JSONArray proJsonObject = jsonObject.getJSONArray("property");
					String[] propertyArr = TypeConversionUtil.JSONArrayToString(proJsonObject);
					// 判断是否修改数据库或者表或者表字段
					resMap = metadataDatabaseServiceImpl.updateCheckDatabaseOperator(metadata, propertyArr);
					if (resMap != null && resMap.size() != 0 && !"0000".equals(resMap.get("returnCode"))) {
						returnJson.put("code", -1);
						returnJson.put("msg", resMap.get("msg"));

						log.debug("添加元数据数据库操作校验不通过。。。");
						return returnJson;
					}
					List<MetadataProperty> propertyList = metadataPropertyMapper
							.queryMetaDataPropertyByMetadataId(metadataId);
					if (proJsonObject != null) {
						for (int j = 0; j < proJsonObject.size(); j++) {
							JSONObject property = proJsonObject.getJSONObject(j); // 从jsonArray中解析Object
							Iterator<String> it = property.keySet().iterator();
							while (it.hasNext()) {
								String key = (String) it.next();
								for (int k = 0; k < propertyList.size(); k++) {
									MetadataProperty metadataProperty = propertyList.get(k);
									if (key.equals(metadataProperty.getPropertyCode())) {
										metadataProperty.setPropertyValue((String) property.get(key));
										metadataProperty.setEditDate(currentTime);
										updataMetadataPropertyList.add(metadataProperty);
									}
								}
							}
						}
					}

					// 更新子集
					JSONArray childJsonObject1 = jsonObject.getJSONArray("childrens");
					if (childJsonObject1 != null) {
						returnJson = updateSubJsonObject(childJsonObject1, updataMetadataList,
								updataMetadataPropertyList, currentTime);
					} else {
						continue;
					}
				}

			}

		}

		log.debug("更新子集元数据json串结束。。。");
		returnJson.put("code", 0);
		returnJson.put("msg", "更新成功");
		returnJson.put("updataMetadataList", updataMetadataList);
		returnJson.put("updataMetadataPropertyList", updataMetadataPropertyList);
		return returnJson;
	}

	/**
	 * 删除元数据，级联删除
	 */
	@Override
	public JSONObject deleteMetadata(String metadataId) {
		log.debug("删除元数据开始。。。");
		JSONObject json = new JSONObject();
		// 需要当前元数据及字迹元数据删除metadata、metadata_property、metadata_relation三张表
		List<String> delMetadatas = new ArrayList<String>();
		delMetadatas.add(metadataId);
		try {
			delMetadatas = queryMetaDataByparentId(metadataId, delMetadatas);
			if (delMetadatas != null && delMetadatas.size() > 1) {
				json.put("code", -1);
				json.put("msg", "元数据存在子级元数据，不允许删除");
				log.debug("元数据存在子级元数据。。。");
			} else if (delMetadatas != null && delMetadatas.size() == 1) {
				List<MetadataRelation> relationlist = metadataRelationMapper.selectMetadataRelation(delMetadatas);
				boolean condbFlag;
				if (relationlist == null || relationlist.size() == 0) {
					Metadata metadataVO = metadataMapper.selectById(metadataId);
					if (metadataVO != null) {
						MetamodelClass mvo = metamodelClassServiceImpl
								.queryModelClassByClassId(metadataVO.getClassId().toString());
						Map<String, String> appMap = appparService.selectAppparList("modelclass");
						if (appMap.get("field").equals(mvo.getClassCode())) {// 删除字段
							condbFlag = metadataDatabaseServiceImpl.delTableColumn(metadataVO);
						} else if ((appMap.get("mongodbField")).equals(mvo.getClassCode())) {// 删除字段
							condbFlag = metadataDatabaseServiceImpl.delTableColumn(metadataVO);
						} else if ((appMap.get("mongodbCol")).equals(mvo.getClassCode())) {// 删除字段
							condbFlag = metadataDatabaseServiceImpl.delTable(metadataVO);
						} else if ((appMap.get("key")).equals(mvo.getClassCode())) {// 删除主键

						} else if ((appMap.get("fkey")).equals(mvo.getClassCode())) {// 删除外键

						}
					}
					metadataRelationMapper.deleteByMetadataIdListInMetadataId(delMetadatas);
					metadataPropertyMapper.deleteByMetadataIdList(delMetadatas);
					// metadataPropertyMapper.delete(delMetadatas);
					// metadataMapper.deleteMetadataIdList(delMetadatas);
					metadataMapper.deleteBatchIds(delMetadatas);
					json.put("code", 0);
					json.put("msg", "删除成功");
				} else {
					json.put("code", -1);
					json.put("msg", "元数据已关联应用，不允许删除");
					log.debug("元数据有关联应用不给删除。。。");
					return json;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", -2);
			json.put("msg", "操作失败");
			log.debug("抛出异常。。。");
		}

		log.debug("删除元数据结束。。。");
		return json;
	}

	/**
	 * 根据元数据id递归查询所有的子集
	 */
	@Override
	public List<String> queryMetaDataByparentId(String parentId, List<String> metaList) {
		log.debug("递归查询元数据子集开始。。。");
		EntityWrapper<Metadata> wrapper = new EntityWrapper<Metadata>();
		if (!"".equals(StringUtil.getString(parentId))) {
			wrapper.like("parent_metadata", parentId);
		}
		List<Metadata> list = metadataMapper.selectList(wrapper);
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				String metadataId = list.get(i).getMetadataId();
				metaList.add(metadataId);
				metaList = queryMetaDataByparentId(metadataId, metaList);
			}
		}
		log.debug("递归查询元数据子集结束。。。");
		return metaList;
	}

	/**
	 * 根据父ID查询子集元数据，返回json
	 */
	@Override
	public JSONArray selectMetadataListByParentId(String parentId) {
		List<Metadata> subMetadataList = queryMetaDataByparentId(parentId);
		JSONArray children = new JSONArray();
		if (subMetadataList == null || subMetadataList.isEmpty()) {
			return children;
		}
		if (subMetadataList != null && subMetadataList.size() > 0) {
			for (int i = 0; i < subMetadataList.size(); i++) {
				Metadata metadata = subMetadataList.get(i);
				JSONObject metaJsonObject = new JSONObject();
				metaJsonObject.put("metadataId", metadata.getMetadataId());
				metaJsonObject.put("metadataCode", metadata.getMetadataCode());
				metaJsonObject.put("metadataName", metadata.getMetadataName());
				metaJsonObject.put("catalogId", metadata.getCatalogId());
				metaJsonObject.put("classId", metadata.getClassId());
				metaJsonObject.put("parentMetadata", metadata.getParentMetadata());
				metaJsonObject.put("notshow", metadata.getNotshow());
				metaJsonObject.put("createDate", metadata.getCreateDate());
				JSONObject property = metadataPropertyServiceImpl
						.queryMetaDataPropertyByMetadataId(metadata.getMetadataId());
				metaJsonObject.put("property", property);
				JSONArray children1 = new JSONArray();
				children1 = selectMetadataListByParentId(metadata.getMetadataId());
				// String children = "";
				metaJsonObject.put("children", children1);
				children.add(metaJsonObject);

			}
		}
		return children;
	}

	/**
	 * 根据目录类型 查询元数据json串
	 */
	@Override
	public JSONArray selectMetadataJsonByCatalogType(String catalogType) {
		log.debug("查询元数据json串开始。。。");
		JSONArray allArr = new JSONArray();

		// 根据目录类型获取目录IDlist
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("catalog_type", catalogType);
		List<MetadataCatalog> metadataCatalogList = metadataCatalogMapper.selectByMap(map);
		// List<MetadataCatalog> metadataCatalogList =
		// metadataCatalogMapper.selectCatalogByCatalogType(catalogType);
		if (metadataCatalogList != null && metadataCatalogList.size() > 0) {
			List<String> catalogList = new ArrayList<String>();
			// 根据目录ID获取元数据list

			for (int i = 0; i < metadataCatalogList.size(); i++) {
				MetadataCatalog metadataCatalog = metadataCatalogList.get(i);
				catalogList.add(metadataCatalog.getCatalogId());
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("catalogId", metadataCatalog.getCatalogId());
				jsonObject.put("catalogCode", metadataCatalog.getCatalogCode());
				jsonObject.put("catalogName", metadataCatalog.getCatalogName());
				jsonObject.put("metadataName", metadataCatalog.getCatalogName());
				jsonObject.put("id", metadataCatalog.getCatalogId());
				// jsonObject.put("catalogName",
				// metadataCatalog.getCatalogName());
				Map<String, Object> columnMap = new HashMap<String, Object>();
				columnMap.put("catalog_id", metadataCatalog.getCatalogId());
				List<Metadata> metadataList = metadataMapper.selectByMap(columnMap);
				// List<Metadata> metadataList =
				// metadataMapper.selectMetadataByCatalogId(metadataCatalog.getCatalogId());
				JSONArray metaArr = new JSONArray();
				for (int j = 0; j < metadataList.size(); j++) {
					JSONObject json = new JSONObject();
					Metadata metadata = metadataList.get(j);
					if (metadata.getParentMetadata() == null || "".equals(metadata.getParentMetadata())) {
						json.put("id", metadata.getMetadataId());
						json.put("metadataCode", metadata.getMetadataCode());
						json.put("metadataName", metadata.getMetadataName());
						json.put("catalogId", metadata.getCatalogId());
						json.put("classId", metadata.getClassId());
						json.put("parentMetadata", 0);
						json.put("notshow", metadata.getNotshow());
						json.put("createDate", metadata.getCreateDate());
						JSONObject property = metadataPropertyServiceImpl
								.queryMetaDataPropertyByMetadataId(metadata.getMetadataId());
						json.put("property", property);
						JSONArray children = new JSONArray();
						children = selectMetadataListByParentId(metadata.getMetadataId());
						json.put("children", children);
						metaArr.add(json);
					}

				}
				jsonObject.put("children", metaArr);

				allArr.add(jsonObject);
			}

		}

		log.debug("查询元数据json串结束。。。");
		return allArr;
	}

	/*
	 * public JSONObject savePannelPage(String metadataId, JSONObject
	 * proJsonObject ) { JSONObject returnJson = new JSONObject(); PannelPage
	 * pannelPage = new PannelPage(); Date date= new Date(); DateFormat
	 * format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String
	 * currentTime=format.format(date);
	 * pannelPage.setDashboardCode((String)proJsonObject.get("dashboard_code"));
	 * pannelPage.setDashboardName((String)proJsonObject.get("dashboard_name"));
	 * pannelPage.setMetadataId(metadataId);
	 * pannelPage.setCreateDate(currentTime);
	 * pannelPageMapper.insert(pannelPage); return returnJson; } public String
	 * savePannelPageEle(String metadataId, String pannelPageId, JSONObject
	 * proJsonObject ) { PannelPageEle pannelPageEle = new PannelPageEle(); Date
	 * date= new Date(); int max = 1; String getMaxOrder =
	 * pannelPageEleMapper.getMaxOrder(pannelPageId); if (getMaxOrder != null &&
	 * getMaxOrder.length()>0) { max = Integer.parseInt(getMaxOrder)+1; }
	 * 
	 * DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String
	 * currentTime=format.format(date); pannelPageEle.setMetadataId(metadataId);
	 * pannelPageEle.setItemType(proJsonObject.get("item_type")+"");
	 * pannelPageEle.setItemWidth(proJsonObject.get("item_width")+"");
	 * pannelPageEle.setItemHeight(proJsonObject.get("item_height")+"");
	 * pannelPageEle.setItemOrderby(max+"");
	 * pannelPageEle.setItemModelid(proJsonObject.get("item_modelid")+"");
	 * pannelPageEle.setCreateDate(currentTime);
	 * pannelPageEleMapper.insert(pannelPageEle); PannelPageEleRel pageEleRel =
	 * new PannelPageEleRel();
	 * pageEleRel.setPanneleleId(pannelPageEle.getPanneleleId());
	 * pageEleRel.setPannelpageId(pannelPageId);
	 * pageEleRel.setCreateDate(currentTime);
	 * pannelPageEleRelMapper.insert(pageEleRel); return max+""; }
	 */
	@Override
	public List<Metadata> queryMetaDataByCatalogParentId(String parentId, String classType) {
		if (parentId != null && !parentId.equals("")) {
			// 模型类型不为空
			if (classType != null && !classType.equals("")) {
				return metadataMapper.selectMetadataByParentIdAndClassType(parentId, classType);
			} else {

				return metadataMapper.selectMetadataByParentId(parentId);
			}
		}
		return null;

	}

	@Override
	public List<Metadata> queryMetaDataByParentIdAndClassId(String parentId, String classId, String classType) {
		if (parentId != null && !parentId.equals("")) {
			// 模型类型不为空
			if (classType != null && !classType.equals("")) {
				return metadataMapper.queryMetaDataByParentIdAndClassIdAndClassType(parentId, classId, classType);
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("parent_metadata", parentId);
				map.put("class_id", classId);
				return metadataMapper.selectByMap(map);
				// return
				// metadataMapper.queryMetaDataByParentIdAndClassId(parentId,classId);
			}
		}
		return null;

	}

	@Override
	public List<Metadata> queryMetaDataByParentIdAndNotId(String parentId, String metadataId) {
		if (parentId != null && !parentId.equals("")) {
			EntityWrapper<Metadata> wrapper = new EntityWrapper<Metadata>();
			wrapper.eq("parent_metadata", parentId);
			wrapper.notIn("metadata_id", metadataId);
			return metadataMapper.selectList(wrapper);
			// return
			// metadataMapper.queryMetaDataByParentIdAndNotId(parentId,metadataId);
		}
		return null;

	}

	@Override
	public List<Metadata> queryMetaDataByParentId(String parentId) {
		EntityWrapper<Metadata> wrapper = new EntityWrapper<Metadata>();
		if (!"".equals(StringUtil.getString(parentId))) {
			wrapper.like("parent_metadata", parentId);
		}
		wrapper.orderBy("class_id", true);
		return metadataMapper.selectList(wrapper);

	}

	@Override
	public List<Metadata> queryParentByChild(List<Metadata> list) {

		List<Metadata> listMetadataVOs = new ArrayList<Metadata>();
		for (int i = 0; i < list.size(); i++) {
			Metadata metadataVO = list.get(i);
			Metadata metadataVO2 = selectById(metadataVO.getParentMetadata());

			for (int j = 0; j < listMetadataVOs.size(); j++) {
				if (listMetadataVOs.get(j).getMetadataId().equals(metadataVO2.getMetadataId())) {
					break;
				} else {
					listMetadataVOs.add(metadataVO2);
				}
			}
		}
		return listMetadataVOs;
	}

	@Override
	public Metadata queryMetadataById(String metadataId) {
		Metadata metadata = new Metadata();
		metadata = metadataMapper.queryMetadataById(metadataId);
		String catalogId = metadata.getCatalogId();
		if (catalogId != null && catalogId.length() > 0) {
			String contextCatalog = metadataCatalogServiceImpl.queryContextCatalog(catalogId);
			metadata.setContextCatalog(contextCatalog);
		}
		return metadata;

	}

	@Override
	public Metadata queryMetadataVO(List list, String metadataId) {
		Metadata VO = new Metadata();
		for (int i = 0; i < list.size(); i++) {
			VO = (Metadata) list.get(i);
			if (VO.getMetadataId().equals(metadataId)) {
				break;
			} else {
				VO = null;
			}
		}
		return VO;
	}

	@Override
	public JSONObject queryMetaDataListByCodeOrName(String codeOrName, int pageNo, int pageSize) {
		JSONObject jsonObject = new JSONObject();
		List<Metadata> retrunList = new ArrayList<Metadata>();
		// pageNo+=1;
		Page<Metadata> page = new Page<Metadata>(pageNo, pageSize);
		codeOrName = "%" + codeOrName + "%";
		List<Metadata> list = metadataMapper.queryMetaDataListByCodeOrName(page, codeOrName);

		if (list != null) {
			page.setRecords(list);
			for (int i = 0; i < list.size(); i++) {
				Metadata metadataVO = list.get(i);
				String catalogId = metadataVO.getCatalogId();
				if (catalogId != null) {
					// 查询完整目录名称
					String contextCatalog = metadataCatalogServiceImpl.queryContextCatalog(catalogId);
					metadataVO.setContextCatalog(contextCatalog);
				}

				retrunList.add(metadataVO);

			}
			jsonObject.put("totalCount", page.getTotal());
			jsonObject.put("totalPage", page.getPages());
			jsonObject.put("list", retrunList);
		} else {
			jsonObject.put("totalCount", 0);
			jsonObject.put("totalPage", 0);
			jsonObject.put("list", "");
		}

		return jsonObject;

	}

	@Override
	public JSONObject queryMetadataRelationshipBymetadataId(String metadataId, int pageNo, int pageSize) {
		JSONObject jsonObject = new JSONObject();
		List<Metadata> list = new ArrayList<Metadata>();
		List<Metadata> listAll = new ArrayList<Metadata>();
		List<Metadata> listFilter = new ArrayList<Metadata>();
		Map<String, String> existMap = new HashMap<String, String>();
		try {

			if (metadataId != null && metadataId.length() > 0) {
				String metadataRelationIdStr = "";
				String beMetadataRelationIdStr = "";
				SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss.SSS");
				Metadata metadataVO = selectById(metadataId);
				List<String> contextCatalogList = metadataCatalogServiceImpl
						.queryContextCatalog(metadataVO.getCatalogId(), metadataId);
				String contextCatalog = "";
				if (contextCatalogList != null && contextCatalogList.size() > 0) {
					contextCatalog = contextCatalogList.get(0);
				}
				metadataVO.setContextCatalog(contextCatalog);
				Map<String, String> catalogMap = new HashMap<String, String>();
				// 查询元数据关联关联信息
				List<MetadataRelation> resultList = metadataRelationServiceImpl
						.queryMetadataRelationshipBymetadataId(metadataId);
				if (resultList != null && resultList.size() > 0) {
					for (int i = 0; i < resultList.size(); i++) {
						MetadataRelation metadataRelationVO = resultList.get(i);
						if (metadataId.equals(metadataRelationVO.getMetadataId() + "")) {// 元数据关联对象
							metadataRelationIdStr += metadataRelationVO.getMetadataRelationed() + ",";
						} else {// 元数据为被关联对象
							beMetadataRelationIdStr += metadataRelationVO.getMetadataId() + ",";
						}
					}
					// 查询元数据对应路径Map
					String metadataIdStr = "";
					if (metadataRelationIdStr.length() > 0) {

						metadataRelationIdStr = metadataRelationIdStr.substring(0, metadataRelationIdStr.length() - 1);
						if (beMetadataRelationIdStr.length() > 0) {
							beMetadataRelationIdStr = beMetadataRelationIdStr.substring(0,
									beMetadataRelationIdStr.length() - 1);
							metadataIdStr = metadataRelationIdStr + "," + beMetadataRelationIdStr;
						} else {
							metadataIdStr = metadataRelationIdStr;
						}
						catalogMap = metadataCatalogServiceImpl.queryContextCatalogMap(metadataIdStr);
					} else {
						if (beMetadataRelationIdStr.length() > 0) {
							beMetadataRelationIdStr = beMetadataRelationIdStr.substring(0,
									beMetadataRelationIdStr.length() - 1);
							metadataIdStr = beMetadataRelationIdStr;
							catalogMap = metadataCatalogServiceImpl.queryContextCatalogMap(metadataIdStr);
						}
					}

					if (metadataRelationIdStr.length() > 0) {
						// metadataRelationIdStr =
						// metadataRelationIdStr.substring(0,
						// metadataRelationIdStr.length()-1);
						List<Metadata> metadataRelationList = queryMetaDataByInMetadataIdStr(metadataRelationIdStr);
						if (metadataRelationList != null) {
							for (int i = 0; i < metadataRelationList.size(); i++) {
								Metadata metadataVOR = metadataRelationList.get(i);
								// Map<String, String> retMap=
								// metadataObjectService.queryContextCatalogMap(metadataVOR.getCatalogId(),metadataVOR.getId());
								// metadataVOR.setRelationMeta(retMap.get("contextCatalog"));
								metadataVOR.setRelationMeta(catalogMap.get(metadataVOR.getMetadataId()));
								metadataVOR.setContextCatalog(contextCatalog);
								listAll.add(metadataVOR);
							}
						}
					}

					if (beMetadataRelationIdStr.length() > 0) {
						// beMetadataRelationIdStr =
						// beMetadataRelationIdStr.substring(0,
						// beMetadataRelationIdStr.length()-1);
						List<Metadata> beMetadataRelationList = queryMetaDataByInMetadataIdStr(beMetadataRelationIdStr);
						if (beMetadataRelationList != null) {
							for (int i = 0; i < beMetadataRelationList.size(); i++) {
								Metadata metadataVOR = beMetadataRelationList.get(i);
								// Map<String, String> retMap=
								// metadataObjectService.queryContextCatalogMap(metadataVOR.getCatalogId(),metadataVOR.getId());
								// metadataVOR.setBeRelationMeta(retMap.get("contextCatalog"));
								metadataVOR.setBeRelationMeta(catalogMap.get(metadataVOR.getMetadataId()));
								metadataVOR.setContextCatalog(contextCatalog);
								listAll.add(metadataVOR);
							}
						}
					}

				}
				// 去重
				if (listAll != null && listAll.size() > 0) {
					for (int i = 0; i < listAll.size(); i++) {
						Metadata metadataVOLast = listAll.get(i);
						if (existMap != null && existMap.get(metadataVOLast.getMetadataId()) != null) {
							continue;
						}
						existMap.put(metadataVOLast.getMetadataId(), metadataVOLast.getMetadataName());
						listFilter.add(metadataVOLast);
					}

				}
				// 根据页码返回数据
				for (int i = pageSize * pageNo; i < pageSize * pageNo + pageSize; i++) {
					if (listFilter == null || listFilter.size() == i) {
						break;
					}
					list.add(listFilter.get(i));
				}
				jsonObject.put("list", list);
				int totalCount = listFilter.size();
				int totalPage = NumberUtil.getTotalPage(totalCount, pageSize);
				jsonObject.put("totalCount", totalCount);
				jsonObject.put("totalPage", totalPage);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return jsonObject;
	}

	@Override
	public List<Metadata> queryMetaDataByInMetadataIdStr(String metadataRelationIdStr) {
		EntityWrapper<Metadata> wrapper = new EntityWrapper<Metadata>();
		String[] arr = metadataRelationIdStr.split(",");
		wrapper.in("metadata_id", arr);
		return metadataMapper.selectList(wrapper);

	}

	@Override
	public List<Metadata> queryMataByclassId(String classId) {
		try {
			if (classId != null && !"".endsWith(classId)) {
				return metadataMapper.queryMataByclassId(classId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}

	@Override
	public List<Metadata> queryMetadataByCatalogBy(String catalogId, String classId, String metadatas) {
		try {
			EntityWrapper<Metadata> wrapper = new EntityWrapper<Metadata>();
			if (!"".equals(StringUtil.getString(catalogId))) {
				wrapper.eq("catalog_id", catalogId);
			}
			if (!"".equals(StringUtil.getString(classId))) {
				wrapper.eq("class_id", classId);
			}
			if (!"".equals(StringUtil.getString(metadatas))) {
				wrapper.notIn("metadata_id", metadatas);
			}
			return metadataMapper.selectList(wrapper);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public JSONObject queryRelationAndMetaDataByClassId(String classId, String relationId, String metadataId,
			int pageNo, int pageSize) {
		JSONObject jsonObject = new JSONObject();
		List<Metadata> metadataVOList = new ArrayList<Metadata>();
		try {
			Page<Metadata> page = new Page<Metadata>(pageNo, pageSize);
			List<Metadata> list = metadataMapper.queryRelationAndMetaDataByClassId(page, classId, relationId,
					metadataId);
			page.setRecords(list);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Metadata vo = list.get(i);

					String catalogId = vo.getCatalogId();
					if (catalogId != null) {
						List<String> contextCatalog = metadataCatalogServiceImpl.queryContextCatalog(catalogId,
								vo.getMetadataRelationed());
						vo.setContextCatalog(contextCatalog.get(0));
					}
					metadataVOList.add(vo);
				}
			}
			jsonObject.put("list", metadataVOList);
			jsonObject.put("totalCount", page.getTotal());
			jsonObject.put("totalPage", page.getPages());
			jsonObject.put("code", 0);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", -2);
		}
		return jsonObject;
	}

	@Override
	public List<Metadata> queryMetadataByCatalogAndCode(String catalogId, String code, String metadataId) {
		try {
			EntityWrapper<Metadata> wrapper = new EntityWrapper<Metadata>();
			if (!"".equals(StringUtil.getString(catalogId))) {
				wrapper.eq("catalog_id", catalogId);
			}
			if (!"".equals(StringUtil.getString(code))) {
				wrapper.eq("metadata_code", code);
			}
			if (!"".equals(StringUtil.getString(metadataId))) {
				wrapper.notIn("metadata_id", metadataId);
			}
			return metadataMapper.selectList(wrapper);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public JSONObject deleteMetadataByIdList(JSONArray idlist, String flag) {
		List<String> list = new ArrayList<String>();
		JSONObject jsonObject = new JSONObject();
		try {

			for (int i = 0; i < idlist.size(); i++) {
				list.add(idlist.getString(i));
				list = queryMetaDataByparentId(idlist.getString(i), list);
			}
			if (list != null) {
				// 判断是否有被关联的关联关系
				List<MetadataRelation> relationlist = metadataRelationMapper.selectMetadataRelation(list);
				if (relationlist != null && relationlist.size() > 0) {
					// 存在被关联关系不允许删除
					jsonObject.put("code", -1);
					jsonObject.put("msg", "元数据已被其他数据关联，不允许删除");
					return jsonObject;
				}
				log.debug("删除标识flag=" + StringUtil.getString(flag));
				// 判断是否为数据库操作
				if ("table".equals(flag)) {// 表操作
					for (int i = 0; i < idlist.size(); i++) {
						Metadata metadata = selectById(idlist.getString(i));
						if (metadata != null) {
							metadataDatabaseServiceImpl.delTable(metadata);
						}
					}
				} else if ("field".equals(flag)) {// 字段操作
					for (int i = 0; i < idlist.size(); i++) {
						Metadata metadata = selectById(idlist.getString(i));
						if (metadata != null) {
							metadataDatabaseServiceImpl.delTableColumn(metadata);
						}
					}
				}

				// 删除关联关系
				metadataRelationMapper.deleteByMetadataIdListInMetadataId(list);
				// 删除属性信息
				metadataPropertyMapper.deleteByMetadataIdList(list);
				// 删除元数据
				// metadataMapper.deleteMetadataIdList(list);
				metadataMapper.deleteBatchIds(list);
			}
			jsonObject.put("code", 0);
			jsonObject.put("msg", "删除成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			jsonObject.put("code", -2);
			jsonObject.put("msg", "操作异常");
		}

		return jsonObject;
	}

	@Override
	public Map<Object, Object> checkMetadataTypeByClassId(String classId, String metadataId, String parentId) {
		Map<Object, Object> returnMap = new HashMap<Object, Object>();
		List<MetadataPropertyKey> dictionaryVOList = new ArrayList<MetadataPropertyKey>();
		String dbFlag = "";
		try {
			MetamodelClass mvo = metamodelClassServiceImpl.queryModelClassByClassId(classId);
			Map<String, String> appparList = appparServiceImpl.selectAppparList("modelclass");
			if (appparList.get("db").equals(mvo.getClassCode())) {// 元素据类型为数据库数据库
				// 字典表中获取数据库驱动类型
				dictionaryVOList = metadataPropertyKeyServiceImpl.queryMetadataPropertyKeyVOByCode("dbDriver");
				dbFlag = "db";

			} else if (appparList.get("field").equals(mvo.getClassCode())) {
				// 判断是哪种数据库，根据数据库的不同选择不同的字段类型
				String dbType = "";
				if (metadataId != null && metadataId != "") {
					dbType = databaseUtil.getDbTypeByfield(metadataId);
				} else {
					dbType = databaseUtil.getDbTypeByTable(parentId);
				}

				if ("mysql".equals(dbType)) {
					dictionaryVOList = metadataPropertyKeyServiceImpl.queryMetadataPropertyKeyVOByCode("mysqlDataType");
				} else if ("oracle".equals(dbType)) {
					dictionaryVOList = metadataPropertyKeyServiceImpl
							.queryMetadataPropertyKeyVOByCode("oracleDataType");
				}
				// 字典表中获取数据库字段类型
				dbFlag = "filed";
			} else if (appparList.get("mongodb").equals(mvo.getClassCode())) {
				// 判断是mongodb数据库
				dictionaryVOList = metadataPropertyKeyServiceImpl.queryMetadataPropertyKeyVOByCode("pgDriver");
				dbFlag = "db";
			} else if (appparList.get("mongodbField").equals(mvo.getClassCode())) {
				// 判断是mongodb数据库
				dictionaryVOList = metadataPropertyKeyServiceImpl.queryMetadataPropertyKeyVOByCode("postgresDataType");
				dbFlag = "filed";
			}
			returnMap.put("dictionaryVOList", dictionaryVOList);
			returnMap.put("dbFlag", dbFlag);
		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put("code", -2);
		}

		return returnMap;
	}

	public static void main(String[] args) {
		EntityWrapper<Metadata> wrapper = new EntityWrapper<Metadata>();
		wrapper.eq("catalog_id", "1");
		wrapper.andNew("parent_metadata IS NULL").or("parent_metadata = ''");
		log.debug(wrapper.getSqlSegment());
	}

	@Override
	public JSONObject deleteMetadata(Map<String, Object> map) {
		log.debug("删除元数据开始。。。");
		JSONObject json = new JSONObject();

		try {
			List<Metadata> metadataList = selectByMap(map);
			if (metadataList == null || metadataList.size() < 1) {
				json.put("code", -1);
				json.put("msg", "元数据不存在");
				log.debug("元数据不存在");
			}
			String metadataId = metadataList.get(0).getMetadataId();
			// 需要当前元数据及字迹元数据删除metadata、metadata_property、metadata_relation三张表
			List<String> delMetadatas = new ArrayList<String>();
			delMetadatas.add(metadataId);
			delMetadatas = queryMetaDataByparentId(metadataId, delMetadatas);
			if (delMetadatas != null && delMetadatas.size() > 1) {
				json.put("code", -1);
				json.put("msg", "元数据存在子级元数据，不允许删除");
				log.debug("元数据存在子级元数据。。。");
			} else if (delMetadatas != null && delMetadatas.size() == 1) {
				List<MetadataRelation> relationlist = metadataRelationMapper.selectMetadataRelation(delMetadatas);
				boolean condbFlag;
				if (relationlist == null || relationlist.size() == 0) {
					Metadata metadataVO = metadataMapper.selectById(metadataId);
					if (metadataVO != null) {
						MetamodelClass mvo = metamodelClassServiceImpl
								.queryModelClassByClassId(metadataVO.getClassId().toString());
						Map<String, String> appMap = appparService.selectAppparList("modelclass");
						if (appMap.get("field").equals(mvo.getClassCode())) {// 删除字段
							condbFlag = metadataDatabaseServiceImpl.delTableColumn(metadataVO);
						} else if ((appMap.get("mongodbField")).equals(mvo.getClassCode())) {// 删除字段
							condbFlag = metadataDatabaseServiceImpl.delTableColumn(metadataVO);
						} else if ((appMap.get("mongodbCol")).equals(mvo.getClassCode())) {// 删除字段
							condbFlag = metadataDatabaseServiceImpl.delTable(metadataVO);
						} else if ((appMap.get("key")).equals(mvo.getClassCode())) {// 删除主键

						} else if ((appMap.get("fkey")).equals(mvo.getClassCode())) {// 删除外键

						}
					}
					metadataRelationMapper.deleteByMetadataIdListInMetadataId(delMetadatas);
					metadataPropertyMapper.deleteByMetadataIdList(delMetadatas);
					// metadataPropertyMapper.delete(delMetadatas);
					// metadataMapper.deleteMetadataIdList(delMetadatas);
					metadataMapper.deleteBatchIds(delMetadatas);
					json.put("code", 0);
					json.put("msg", "删除成功");
				} else {
					json.put("code", -1);
					json.put("msg", "元数据已关联应用，不允许删除");
					log.debug("元数据有关联应用不给删除。。。");
					return json;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", -2);
			json.put("msg", "操作失败");
			log.debug("抛出异常。。。");
		}

		log.debug("删除元数据结束。。。");
		return json;
	}

	/**
	 * 根据metadataId 查询元数据json串
	 */
	@Override
	public JSONObject selectMetadata(Map<String, Object> map, String level) {
		log.debug("查询元数据json串开始。。。");
		JSONObject json = new JSONObject();
		List<Metadata> list = metadataMapper.selectByMap(map);
		if (list == null || list.size() == 0) {
			json.put("code", "-1");
			json.put("msg", "元数据不存在");
			return json;
		}
		Metadata metadata = list.get(0);
		if (metadata != null) {
			json.put("metadataId", metadata.getMetadataId());
			json.put("metadataCode", metadata.getMetadataCode());
			json.put("metadataName", metadata.getMetadataName());
			json.put("catalogId", metadata.getCatalogId());
			json.put("classId", metadata.getClassId());
			json.put("parentMetadata", metadata.getParentMetadata());
			json.put("notshow", metadata.getNotshow());
			json.put("createDate", metadata.getCreateDate());
			JSONObject property = metadataPropertyServiceImpl
					.queryMetaDataPropertyByMetadataId(metadata.getMetadataId());
			json.put("property", property);
			if ("c".equals(level) || "a".equals(level)) {// 查子集
				JSONArray children = new JSONArray();
				children = selectMetadataListByParentId(metadata.getMetadataId());
				// String children = "";
				json.put("children", children);
			}
			if ("p".equals(level) || "a".equals(level)) {// 查父级ID
				JSONObject parent = selectMetadataById(metadata.getParentMetadata());
				json.put("parent", parent);
			}

		}

		log.debug("查询元数据json串结束。。。");
		return json;
	}

	/**
	 * 根据metadataId 查询元数据json串
	 */
	@Override
	public JSONObject selectMetadataById(String metadataId) {
		log.debug("selectMetadataById begin");
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		if (StringUtil.isEmpty(metadataId)) {
			return null;
		}
		JSONObject json = new JSONObject();
		endTime = System.currentTimeMillis();
		Metadata metadata = metadataMapper.selectById(metadataId);
		log.debug("查询元数据基本信息 metadataMapper.selectById({}) use time {} ms ", metadataId, endTime - startTime);
		startTime = System.currentTimeMillis();
		if (metadata != null) {
			json.put("metadataId", metadata.getMetadataId());
			json.put("metadataCode", metadata.getMetadataCode());
			json.put("metadataName", metadata.getMetadataName());
			json.put("catalogId", metadata.getCatalogId());
			json.put("classId", metadata.getClassId());
			json.put("parentMetadata", metadata.getParentMetadata());
			json.put("notshow", metadata.getNotshow());
			json.put("createDate", metadata.getCreateDate());
			endTime = System.currentTimeMillis();
			JSONObject property = metadataPropertyServiceImpl
					.queryMetaDataPropertyByMetadataId(metadata.getMetadataId());
			log.debug("查询元数据属性 metadataPropertyServiceImpl.queryMetaDataPropertyByMetadataId({}) use time {} ms ",
					metadataId, endTime - startTime);
			startTime = System.currentTimeMillis();
			json.put("property", property);
		}
		log.debug("selectMetadataById end");
		return json;
	}

	/**
	 * 校验元数据代码是否存在
	 *
	 * @param metadataCode（必填）元数据代码
	 * @param metadataId（非必填）元数据ID，作用是排除当前元数据的ID的元数据Code
	 * @return
	 */
	boolean checkMetadataCodeIsExist(String metadataCode, String metadataId) {
		EntityWrapper<Metadata> wrapper = new EntityWrapper<Metadata>();
		wrapper.eq("metadata_code", metadataCode);
		if (!StringUtil.isEmpty(metadataId)) {
			wrapper.notIn("metadata_id", metadataId);
		}
		List<Metadata> metadataList = metadataMapper.selectList(wrapper);
		if (metadataList == null || metadataList.size() < 1) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public JSONObject selectMetadataByMap(Map<String, Object> map, String level) {
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis();

		log.debug("selectMetadataByMap begin");
		JSONObject json = new JSONObject();
		List<Metadata> list = metadataMapper.queryMetadataByMap(map);

		endTime = System.currentTimeMillis();
		log.debug("metadataMapper.selectMetadataByMap {} use time {}", map, endTime - startTime);
		startTime = System.currentTimeMillis();

		if (list == null || list.size() == 0) {
			json.put("code", "-1");
			json.put("msg", "元数据不存在");
			return json;
		}
		Metadata metadata1 = list.get(0);
		json.put("metadataId", metadata1.getMetadataId());
		json.put("metadataCode", metadata1.getMetadataCode());
		json.put("metadataName", metadata1.getMetadataName());
		json.put("catalogId", metadata1.getCatalogId());
		json.put("classId", metadata1.getClassId());
		json.put("parentMetadata", metadata1.getParentMetadata());
		json.put("notshow", metadata1.getNotshow());
		json.put("createDate", metadata1.getCreateDate());
		JSONObject proJsonObject = new JSONObject();
		for (Metadata metadata : list) {
			if (StringUtil.isNotEmpty(metadata.getPropertyCode())) {
				proJsonObject.put(metadata.getPropertyCode(), metadata.getPropertyValue());
			}
		}
		json.put("property", proJsonObject);
		List<String> metadataIdList = new ArrayList<String>();// 作为自己父级Id查询所有子集
		metadataIdList.add(metadata1.getMetadataId());
		if ("c".equals(level) || "a".equals(level)) {// 查子集
			Map<String, JSONArray> childrenMap = selectMetadataListByParentIdList(metadataIdList);

			endTime = System.currentTimeMillis();
			log.debug("selectMetadataListByParentIdList {} use time {}", metadataIdList, endTime - startTime);
			startTime = System.currentTimeMillis();

			if (childrenMap != null) {
				JSONArray children = childrenMap.get(metadata1.getMetadataId());
				json.put("children", children);
			}

		}
		if ("p".equals(level) || "a".equals(level)) {// 查父级ID
			JSONObject parent = selectMetadataById(metadata1.getParentMetadata());

			endTime = System.currentTimeMillis();
			log.debug("selectMetadataById {} use time {}", metadata1.getParentMetadata(), endTime - startTime);
			startTime = System.currentTimeMillis();

			json.put("parent", parent);
		}

		log.debug("selectMetadataByMap end ");
		return json;
	}

	public Map<String, JSONArray> selectMetadataListByParentIdList(List<String> metadataIdList) {
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		if (metadataIdList == null || metadataIdList.size() == 0) {
			return null;
		}
		endTime = System.currentTimeMillis();
		List<Metadata> list = metadataMapper.querySubMetadataByListId(metadataIdList);
		log.debug("查询子元数据信息 metadataMapper.querySubMetadataByListId({}) use time {} ms ", metadataIdList,
				endTime - startTime);
		startTime = System.currentTimeMillis();
		if (list == null || list.size() == 0) {// 转换json格式返回，重新组装返回的权限对象，一个资源封装成一个对象，包含该资源的操作权限列表
			return null;
		}
		Map<String, Metadata> resultMap = new LinkedHashMap<String, Metadata>();
		Metadata json = new Metadata();
		List<String> pidList = new ArrayList<String>();
		// 组装每一个元数据的属性
		for (Metadata metadata : list) {
			String metadataId = metadata.getMetadataId();

			if (null == resultMap.get(metadataId)) {
				json = new Metadata();
				pidList.add(metadata.getMetadataId());
				json.setMetadataId(metadata.getMetadataId());
				json.setMetadataCode(metadata.getMetadataCode());
				json.setMetadataName(metadata.getMetadataName());
				json.setCatalogId(metadata.getCatalogId());
				json.setClassId(metadata.getClassId());
				json.setParentMetadata(metadata.getParentMetadata());
				json.setNotshow(metadata.getNotshow());
				json.setCreateDate(metadata.getCreateDate());
				JSONObject proJsonObject = new JSONObject();
				if (StringUtil.isNotEmpty(metadata.getPropertyCode())) {
					proJsonObject.put(metadata.getPropertyCode(), metadata.getPropertyValue());
				}
				json.setProperty(proJsonObject);
				resultMap.put(metadataId, json);

			} else {

				resultMap.get(metadataId).getProperty().put(metadata.getPropertyCode(), metadata.getPropertyValue());
			}

		}

		Map<String, JSONArray> childrenMap = selectMetadataListByParentIdList(pidList);
		if (childrenMap != null) {// 子集数组
			for (Entry<String, JSONArray> children : childrenMap.entrySet()) {
				resultMap.get(children.getKey()).setChildren(children.getValue());
			}
		}

		// 每一个父级Map
		Map<String, JSONArray> arrMap = new HashMap<String, JSONArray>();
		for (String parentId : metadataIdList) {
			if (null == arrMap.get(parentId)) {
				JSONArray children = new JSONArray();
				arrMap.put(parentId, children);
			}
		}
		// log.debug("resultMap = " + resultMap.toString());
		// 组装每一个父级ID下的子集
		for (Entry<String, Metadata> entry : resultMap.entrySet()) {
			Metadata metadata = entry.getValue();
			arrMap.get(metadata.getParentMetadata()).add(entry.getValue());
		}

		return arrMap;
	}

	@Override
	public JSONArray selectRelationMetadataJson(String param) {
		log.debug("查询元数据关联表接口 selectRelationMetadataJson 开始");
		String table_class_id = PropUtil.getProperty("table_class_id");
		long startTime = System.currentTimeMillis();
		List<String> metadataIdList = new ArrayList<String>();
		List<Metadata> list = new ArrayList<Metadata>();
		// 查询全部标识
		if (StringUtil.isEmpty(param)) {// 传空值，查所有
			list = metadataMapper.queryAllRelationMetadata(table_class_id);
		} else {
			String listArr[] = param.split(",");
			metadataIdList = Arrays.asList(listArr);
			list = metadataMapper.queryRelationMetadataByListId(table_class_id, metadataIdList);
		}
		if (list == null || list.size() == 0) {// 如果查询列表空，则返回空
			return null;
		}

		Map<String, MetaData> resultMap = new HashMap<String, MetaData>();// 关联表结果集
		MetaData json = new MetaData();
		List<String> pidList = new ArrayList<String>();
		// 组装每一个元数据的属性
		for (Metadata metadata : list) {
			String metadataId = metadata.getMetadataId();
			if (null == resultMap.get(metadataId)) {
				json = new MetaData();
				pidList.add(metadata.getParentMetadata());
				json.setMetadataId(metadata.getMetadataId());
				json.setMetadataCode(metadata.getMetadataCode());
				json.setMetadataName(metadata.getMetadataName());
				json.setCatalogId(metadata.getCatalogId());
				json.setClassId(metadata.getClassId());
				json.setParentMetadata(metadata.getParentMetadata());
				json.setNotshow(metadata.getNotshow());
				json.setCreateDate(metadata.getCreateDate());
				json.setRelationMeta(metadata.getRelationMeta());
				json.setBeRelationMeta(metadata.getBeRelationMeta());
				JSONObject proJsonObject = new JSONObject();
				if (StringUtil.isNotEmpty(metadata.getPropertyCode())) {
					proJsonObject.put(metadata.getPropertyCode(), metadata.getPropertyValue());
				}
				json.setProperty(proJsonObject);
				resultMap.put(metadataId, json);
			} else {
				resultMap.get(metadataId).getProperty().put(metadata.getPropertyCode(), metadata.getPropertyValue());
			}

		}

		JSONArray returnArray = new JSONArray();
		Map<String, MetaData> parentMap = selectMetadataListByIdList(pidList);
		if (parentMap != null) {// 父集数组,把子集全部放到父级对象下面
			for (Entry<String, MetaData> parent : parentMap.entrySet()) {
				MetaData parentMetadata = parent.getValue();
				for (Entry<String, MetaData> metadataMap : resultMap.entrySet()) {
					MetaData metadata = metadataMap.getValue();
					if (metadata.getParentMetadata().equals(parentMetadata.getMetadataId())) {
						if (parentMetadata.getChildren() == null) {
							List<MetaData> clist = new ArrayList<MetaData>();
							clist.add(metadata);
							parentMetadata.setChildren(clist);
						} else {
							parentMetadata.getChildren().add(metadata);
						}
					}
				}
				returnArray.add(parentMetadata);
			}

		}

		long endTime = System.currentTimeMillis();
		log.debug("selectRelationMetadataJson {} use time {} ms", param == null ? "" : param, endTime - startTime);
		return returnArray;

	}

	private Map<String, MetaData> selectMetadataListByIdList(List<String> metadataIdList) {
		if (metadataIdList == null || metadataIdList.size() == 0) {
			return null;
		}

		List<Metadata> list = metadataMapper.queryMetadataByListId(metadataIdList);
		if (list == null || list.size() == 0) {// 转换json格式返回，重新组装返回的权限对象，一个资源封装成一个对象，包含该资源的操作权限列表
			return null;
		}
		Map<String, MetaData> resultMap = new HashMap<String, MetaData>();
		MetaData json = new MetaData();
		List<String> pidList = new ArrayList<String>();// 父级IDlist
		// 组装每一个元数据的属性
		for (Metadata metadata : list) {
			String metadataId = metadata.getMetadataId();

			if (null == resultMap.get(metadataId)) {
				json = new MetaData();
				pidList.add(metadata.getParentMetadata());
				json.setMetadataId(metadata.getMetadataId());
				json.setMetadataCode(metadata.getMetadataCode());
				json.setMetadataName(metadata.getMetadataName());
				json.setCatalogId(metadata.getCatalogId());
				json.setClassId(metadata.getClassId());
				json.setParentMetadata(metadata.getParentMetadata());
				json.setNotshow(metadata.getNotshow());
				json.setCreateDate(metadata.getCreateDate());
				JSONObject proJsonObject = new JSONObject();
				if (StringUtil.isNotEmpty(metadata.getPropertyCode())) {
					proJsonObject.put(metadata.getPropertyCode(), metadata.getPropertyValue());
				}
				json.setProperty(proJsonObject);
				resultMap.put(metadataId, json);

			} else {

				resultMap.get(metadataId).getProperty().put(metadata.getPropertyCode(), metadata.getPropertyValue());
			}

		}

		/*
		 * 暂时只查表，不在往下查询 Map<String, Metadata> pMap =
		 * selectMetadataListByIdList(pidList); if (pMap != null) {//父集数组 for
		 * (Entry<String, Metadata> parent : pMap.entrySet()) {
		 * resultMap.get(parent.getKey()).setParent(parent.getValue()); } } if
		 * (pMap != null) {//父集数组,把子集全部放到父级对象下面 for (Entry<String, Metadata>
		 * parent : pMap.entrySet()) { Metadata parentMetadata =
		 * parent.getValue(); for (Entry<String, Metadata> metadataMap :
		 * resultMap.entrySet()) { Metadata metadata = metadataMap.getValue();
		 * if
		 * (metadata.getParentMetadata().equals(parentMetadata.getMetadataId()))
		 * { if (parentMetadata.getChildren()==null) { JSONArray jsonArray = new
		 * JSONArray(); jsonArray.add(metadata);
		 * parentMetadata.setChildren(jsonArray); }else {
		 * parentMetadata.getChildren().add(metadata); } } }
		 * //returnArray.add(parentMetadata);
		 * resultMap.put(parent.getKey(),parentMetadata); }
		 * 
		 * }
		 */
		return resultMap;
	}

	@Override
	public Metadata selectMetadataCodeById(String metadataId) {
		Metadata metadata = new Metadata();
		metadata = metadataMapper.selectMetadataCodeById(metadataId);
		return metadata;

	}
}
