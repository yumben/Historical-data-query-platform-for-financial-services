package cn.com.infohold.dao;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.com.infohold.core.dao.IDao;
import cn.com.infohold.entity.MetamodelPackage;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface IMetamodelPackageDao extends IDao<MetamodelPackage> {
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
	 * 根据map（packageId、packageName）查询模型包列表
	 * @param param
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public JSONObject queryMetamodelPackageBysql(Map param,int pageNo,int  pageSize);
}
