package cn.com.infohold.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;

import cn.com.infohold.core.service.impl.ServiceImpl;
import cn.com.infohold.dao.IMetamodelClassDao;
import cn.com.infohold.entity.MetamodelClass;
import cn.com.infohold.entity.MetamodelClassgroup;
import cn.com.infohold.entity.MetamodelClassproperty;
import cn.com.infohold.entity.MetamodelRelation;
import cn.com.infohold.service.IMetamodelClassService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@Service
public class MetamodelClassServiceImpl extends ServiceImpl<IMetamodelClassDao, MetamodelClass> implements IMetamodelClassService {
	/**
	 * 根据父包的id查找该包下的类模型
	 * @param parentId
	 * @param isshow
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<MetamodelClass> queryModelClassByParentId(String parentId, String isshow)throws Exception{
		return dao.queryModelClassByParentId(parentId,isshow);
	}
	
	/**
	 * 根据父包的id查找该包下的类模型
	 * @param parentId
	 * @param isshow
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<MetamodelClass> queryModelClassByParentIdAndType(String parentId, String isshow, String type) throws Exception{
		return dao.queryModelClassByParentIdAndType(parentId,isshow,type);
	}

	/**
	 * 根据父包的id查找该包下的类模型
	 * @param parentId
	 * @param isshow
	 * @return
	 * @throws Exception
	 */
	@Override
	public String queryModelClassIdListById(String packageId, String classId) throws Exception{
		return dao.queryModelClassIdListById(packageId,classId);
	}
	
	/**
	 * 根据模型类ID查询模型类的基本信息
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	@Override
	public MetamodelClass queryModelClassByClassId(String classId) throws Exception{
		return dao.queryModelClassByClassId(classId);
	}
	
	/**
	 * 根据模型类ID查询模型类的属性信息
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	@Override
	public Page<MetamodelClassproperty> queryModelClassPropertyByClassId(String classId,Page<MetamodelClassproperty> page) throws Exception{
		return dao.queryModelClassPropertyByClassId(classId, page);
	}
	
	/**
	 * 查询组合关系
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Override
	public Page<MetamodelClassgroup> queryModelClassGroup(Map params, Page<MetamodelClassgroup> page) throws Exception{
		return dao.queryModelClassGroup(params,page);
	}
	
	/**
	 * 查询被组合关系
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Override
	public Page<MetamodelClassgroup> queryModelClassGrouped(Map params, Page<MetamodelClassgroup> page) throws Exception{
		return dao.queryModelClassGrouped(params, page);
	}
	
	/**
	 * 查询模型类关联关系
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Override
	public Page<MetamodelRelation> queryModelClassRelation(Map params,Page<MetamodelRelation> page) throws Exception{
		return dao.queryModelClassRelation(params, page);
	}
	
	/**
	 * 查询模型类（分页）
	 * @param params
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@Override
	public Page<MetamodelClass>  queryModelClass(Map params, Page<MetamodelClass> page)throws Exception{
		return dao.queryModelClass(params, page);
	}
	
	/**
	 * 查询菜单
	 * @param entity 菜单实体
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<MetamodelClass> selectMetamodelClassList(MetamodelClass entity,int pageNo,int pageSize){
		return dao.selectMetamodelClassList(entity, pageNo, pageSize);
	}
	
	
	/**
	 * 根据code查询元模型
	 * @param code
	 * @return
	 */
	@Override
	public MetamodelClass selectMetamodelClass(String classCode){
		return dao.selectMetamodelClass(classCode);
	}
	
	/**
	 * 根据ID查询元模型
	 * @param classId
	 * @return
	 */
	@Override
	public MetamodelClass selectMetamodelClassByClassId(String classId){
		return dao.selectMetamodelClassByClassId(classId);
	}
	
	/**
	 * 根据元数据ID查询元模型
	 * @param classId
	 * @return
	 */
	@Override
	public List<MetamodelClass> queryMetamodelByMetadataId(String metadataId){
		return dao.queryMetamodelByMetadataId(metadataId);
	}
	
	/**
	 * 根据code查询元模型，不包含当前元模型ID
	 */
	@Override
	public List<MetamodelClass> queryByCodeAndNotId(String code,String id , String packageId){
		return dao.queryByCodeAndNotId(code, id, packageId);
	}
	
	/**
	 * 根据被组合ID查询模型类
	 * @param classGroupedId
	 * @return
	 */
	@Override
	public  MetamodelClass queryByClassGroupedId(String classGroupedId){
		return dao.queryByClassGroupedId(classGroupedId);
	}
	
	@Override
	public List<MetamodelClass> selectList(Map<String, Object> map){
		return dao.selectByMap(map);
	}
}
