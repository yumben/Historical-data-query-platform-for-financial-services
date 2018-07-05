package cn.com.infohold.service;

import cn.com.infohold.entity.MetamodelRelationType;

import java.util.List;
import java.util.Map;

import cn.com.infohold.core.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface IMetamodelRelationTypeService extends IService<MetamodelRelationType> {
	/**
	 * 根据模型关系类型查询模型关系
	 * @param entity
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<MetamodelRelationType> selectMetamodelRelationTypeList(MetamodelRelationType entity,int pageNo,int pageSize);
	/**
	 * 根据条件查询模型关系类型列表
	 * @param map(relation_type_id-关联类型ID,relation_type_name-关联类型名称,
	 * describes-描述,parentrela_id-父关联类型id),map可为空
	 * @return
	 */
	List<MetamodelRelationType> selectList(Map<String, Object> map);
}
