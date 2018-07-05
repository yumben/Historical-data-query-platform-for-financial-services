package cn.com.infohold.service;

import cn.com.infohold.entity.MetamodelClass;
import cn.com.infohold.entity.MetamodelClassgroup;
import cn.com.infohold.entity.MetamodelClassproperty;
import cn.com.infohold.entity.MetamodelRelation;

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
public interface IMetamodelClassService extends IService<MetamodelClass> {
	/**
	 * 根据父包的id查找该包下的类模型
	 * @param parentId
	 * @param isshow
	 * @return
	 * @throws Exception
	 */
	public List<MetamodelClass> queryModelClassByParentId(String parentId, String isshow) throws Exception;
	
	/**
	 * 根据父包的id查找该包下的类模型
	 * @param parentId
	 * @param isshow
	 * @return
	 * @throws Exception
	 */
	public List<MetamodelClass> queryModelClassByParentIdAndType(String parentId, String isshow, String type) throws Exception;

	/**
	 * 根据父包的id查找该包下的类模型
	 * @param parentId
	 * @param isshow
	 * @return
	 * @throws Exception
	 */
	public String queryModelClassIdListById(String packageId, String classId) throws Exception;
	
	/**
	 * 根据模型类ID查询模型类的基本信息
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	public MetamodelClass queryModelClassByClassId(String classId) throws Exception;
	
	/**
	 * 根据模型类ID查询模型类的属性信息
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	public Page<MetamodelClassproperty> queryModelClassPropertyByClassId(String classId,Page<MetamodelClassproperty> page) throws Exception;
	
	/**
	 * 查询组合关系
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Page<MetamodelClassgroup> queryModelClassGroup(Map params, Page<MetamodelClassgroup> page) throws Exception;
	
	/**
	 * 查询被组合关系
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Page<MetamodelClassgroup> queryModelClassGrouped(Map params, Page<MetamodelClassgroup> page) throws Exception;
	
	/**
	 * 查询模型类关联关系
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Page<MetamodelRelation> queryModelClassRelation(Map params,Page<MetamodelRelation> page) throws Exception;
	
	/**
	 * 查询模型类（分页）
	 * @param params
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public Page<MetamodelClass>  queryModelClass(Map params, Page<MetamodelClass> page)throws Exception;
	
	/**
	 * 查询菜单
	 * @param entity 菜单实体
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @return
	 * @throws Exception
	 */
	public List<MetamodelClass> selectMetamodelClassList(MetamodelClass entity,int pageNo,int pageSize);
	
	
	/**
	 * 根据code查询元模型
	 * @param code
	 * @return
	 */
	public MetamodelClass selectMetamodelClass(String classCode);
	
	/**
	 * 根据ID查询元模型
	 * @param classId
	 * @return
	 */
	public MetamodelClass selectMetamodelClassByClassId(String classId);
	
	/**
	 * 根据元数据ID查询元模型
	 * @param classId
	 * @return
	 */
	public List<MetamodelClass> queryMetamodelByMetadataId(String metadataId);
	
	/**
	 * 根据code查询元模型，不包含当前元模型ID
	 */
	public List<MetamodelClass> queryByCodeAndNotId(String code,String id , String packageId);
	
	/**
	 * 根据被组合ID查询模型类
	 * @param classGroupedId
	 * @return
	 */
	public  MetamodelClass queryByClassGroupedId(String classGroupedId);
	
	/**
	 * 根据map查询元模型列表
	 * @param map<String,Object>(key值可为：class_id，class_code，class_name，describes，type，
	 * isshow，isadd，display_icon，package_id，create_date)，可为空
	 * @return
	 */
	List<MetamodelClass> selectList(Map<String, Object> map);
}
