package cn.com.infohold.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;

import cn.com.infohold.core.service.impl.ServiceImpl;
import cn.com.infohold.dao.IMetamodelClasspropertyDao;
import cn.com.infohold.entity.MetamodelClassproperty;
import cn.com.infohold.service.IMetamodelClasspropertyService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@Service
public class MetamodelClasspropertyServiceImpl extends ServiceImpl<IMetamodelClasspropertyDao, MetamodelClassproperty> implements IMetamodelClasspropertyService {
	
	/**
	 * 查询菜单
	 * @param entity 菜单实体
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<MetamodelClassproperty> selectMetamodelClasspropertyList(MetamodelClassproperty entity,int pageNo,int pageSize){
		
		return dao.selectMetamodelClasspropertyList(entity,pageNo,pageSize);
	}
	
	
	/**
	 * 查询继承关系
	 * @param entity 菜单实体
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @return
	 * @throws Exception
	 */
	@Override
	public Page<MetamodelClassproperty> queryByPropertyIDResultInfoAttr(String propertyId,Page<MetamodelClassproperty> page){
		return dao.queryByPropertyIDResultInfoAttr(propertyId,page);
	}
	
	/**
	 * 根据classID查询元数据属性
	 * @param classId
	 * @return
	 */
	@Override
	public List<MetamodelClassproperty> selectMetamodelClasspropertyList(String classId){
		
		return dao.selectMetamodelClasspropertyList(classId);
	}
	
	/**
	 * 根据元模型属性ID删除属性
	 */
	@Override
	public boolean deleteMetamodelClasspropertyById(String id){
		return dao.deleteMetamodelClasspropertyById(id);
	}
	
	/**
	 * 根据参数查询是否已经存在数据，返回总记录数
	 */
	@Override
	public String selectMetamodelClasspropertyCount(MetamodelClassproperty metamodelClassproperty){
		return dao.selectMetamodelClasspropertyCount(metamodelClassproperty);
	}
	/**
	 * 根据元模型属性对象查询元模型列表
	 */
	@Override
	public List<MetamodelClassproperty> selectMetamodelClasspropertyList(MetamodelClassproperty entity){
		return dao.selectMetamodelClasspropertyList(entity);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		dao.deleteByMap(map);
	}
}
