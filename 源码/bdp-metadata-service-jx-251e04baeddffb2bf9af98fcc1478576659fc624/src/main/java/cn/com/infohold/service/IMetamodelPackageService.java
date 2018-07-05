package cn.com.infohold.service;

import cn.com.infohold.entity.MetamodelDatatype;
import cn.com.infohold.entity.MetamodelPackage;

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
public interface IMetamodelPackageService extends IService<MetamodelPackage> {
	/**
	 * 根据父包的id和类型查找该包下的子包
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<MetamodelPackage> queryModelPackageByParentIdAndType(String parentId, String type) throws Exception;
	
	/**
	 * 根据父包的id查找该包下的子包
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<MetamodelPackage> queryModelPackageByParentId(String parentId) throws Exception;
	/**
	 * 查询菜单
	 * @param entity 菜单实体
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @return
	 * @throws Exception
	 */
	List<MetamodelPackage> selectMetamodelPackageList(MetamodelPackage entity,int pageNo,int pageSize);
	
	/**
	 * 根据map（packageId-模型包ID、packageName-模型包代码或者名称）查询模型包列表
	 * @param param
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	JSONObject queryMetamodelPackageBysql(Map param,int pageNo,int  pageSize);
	
	
	/**
	 * 根据Map查询模型包列表
	 * @param map<String,Object>(package_id-模型包ID,package_code-模型包代码,package_name-模型包名称,
	 * describes-模型包描述,parent_id-上级ID,package_type-模型包类型,create_date-创建时间)，map可为空
	 * @return
	 */
	List<MetamodelPackage> selectList(Map<String, Object> map);
}
