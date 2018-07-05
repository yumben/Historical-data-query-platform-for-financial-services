package cn.com.infohold.dao;

import java.util.List;
import java.util.Map;

import cn.com.infohold.core.dao.IDao;
import cn.com.infohold.entity.MetamodelRelationType;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface IMetamodelRelationTypeDao extends IDao<MetamodelRelationType> {
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
	 * @param map(relationTypeId-关联类型ID,relationTypeName-关联类型名称,describes-描述,parentrelaId-父关联类型id)
	 * @return
	 */
	List<MetamodelRelationType> selectList(Map<String, String> map);
}
