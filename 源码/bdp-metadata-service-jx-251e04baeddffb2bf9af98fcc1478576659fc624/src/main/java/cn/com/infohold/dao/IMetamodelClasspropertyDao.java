package cn.com.infohold.dao;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;

import cn.com.infohold.core.dao.IDao;
import cn.com.infohold.entity.MetamodelClassproperty;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface IMetamodelClasspropertyDao extends IDao<MetamodelClassproperty> {
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
}
