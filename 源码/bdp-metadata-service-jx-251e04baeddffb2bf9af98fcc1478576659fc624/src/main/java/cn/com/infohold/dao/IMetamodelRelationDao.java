package cn.com.infohold.dao;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.com.infohold.core.dao.IDao;
import cn.com.infohold.entity.MetamodelRelation;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface IMetamodelRelationDao extends IDao<MetamodelRelation> {
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
