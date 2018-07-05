package cn.com.infohold.service;

import cn.com.infohold.entity.MetamodelRelation;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.com.infohold.core.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface IMetamodelRelationService extends IService<MetamodelRelation> {
	/**
	 * 根据Map查询模型关联列表
	 * @param map<String,Object>(relation_id-关联ID，relation_code-关联代码，relation_name-关联名称，class_id，
	 * classed_id-被关联模型类ID，relation_type_id-关联类型ID，describes-描述，create_date-创建时间)，map可为空
	 * @return
	 */
	List<MetamodelRelation> selectList(Map<String, Object> map);
	/**
	 * 根据实体对象查询元模型关联列表
	 * @param entity
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<MetamodelRelation> selectMetamodelRelationList(MetamodelRelation entity,int pageNo,int pageSize);
	
	/**
	 * 根据关联ID查询关联对象
	 * @param id
	 * @return
	 */
	MetamodelRelation selectMetamodelRelationById(String id);
	
	/**
	 *根据元模型ID查询关联关系列表
	 * @param classId
	 * @return
	 */
	List<MetamodelRelation> queryByClassIdRelationInfo(String classId);
	
	
	//MetamodelRelation queryByIdRelationInfo(String classId);
	
	/**
	 * 根据关联关系ID删除关联关系
	 * @param id
	 * @return
	 */
	JSONObject delMetamodelRelationById(String id);
}
