package cn.com.infohold.service;

import cn.com.infohold.entity.MetamodelClassproperty;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;

import cn.com.infohold.core.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface IMetamodelClasspropertyService extends IService<MetamodelClassproperty> {
	/**
	 * 查询菜单
	 * @param entity 菜单实体
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @return
	 * @throws Exception
	 */
	List<MetamodelClassproperty> selectMetamodelClasspropertyList(MetamodelClassproperty entity,int pageNo,int pageSize);
	
	
	/**
	 * 查询继承关系
	 * @param entity 菜单实体
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @return
	 * @throws Exception
	 */
	Page<MetamodelClassproperty> queryByPropertyIDResultInfoAttr(String propertyId,Page<MetamodelClassproperty> page);
	
	/**
	 * 根据classID查询元数据属性
	 * @param classId
	 * @return
	 */
	List<MetamodelClassproperty> selectMetamodelClasspropertyList(String classId);
	
	boolean deleteMetamodelClasspropertyById(String id);
	
	//根据参数查询是否已经存在数据，返回总记录数
	String selectMetamodelClasspropertyCount(MetamodelClassproperty metamodelClassproperty);
	
	List<MetamodelClassproperty> selectMetamodelClasspropertyList(MetamodelClassproperty entity);
	
	/**
	 * 根据map删除元模型属性
	 * @param map<String,Object>(property_id-元模型属性ID,datatype_id-数据类型ID,class_id-模型类Id,property_code-元模型属性代码,
	 * property_name-元模型属性名称,parent_property_id-父级元模型属性ID)
	 */
	void delete(Map<String, Object> map); 
}
