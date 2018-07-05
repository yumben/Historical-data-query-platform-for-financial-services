package cn.com.infohold.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.com.infohold.core.service.impl.ServiceImpl;
import cn.com.infohold.dao.IMetamodelPackageDao;
import cn.com.infohold.dao.impl.MetamodelPackageDaoImpl;
import cn.com.infohold.entity.MetamodelPackage;
import cn.com.infohold.service.IMetamodelPackageService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@Service
public class MetamodelPackageServiceImpl extends ServiceImpl<MetamodelPackageDaoImpl, MetamodelPackage> implements IMetamodelPackageService {
	@Autowired
	IMetamodelPackageDao metamodelPackageDaoImpl;
	/**
	 * 根据父包的id和类型查找该包下的子包
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<MetamodelPackage> queryModelPackageByParentIdAndType(String parentId, String type) throws Exception{
		return metamodelPackageDaoImpl.queryModelPackageByParentIdAndType(parentId, type);
	}
	
	/**
	 * 根据父包的id查找该包下的子包
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<MetamodelPackage> queryModelPackageByParentId(String parentId) throws Exception{
		return metamodelPackageDaoImpl.queryModelPackageByParentId(parentId);
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
	public List<MetamodelPackage> selectMetamodelPackageList(MetamodelPackage entity,int pageNo,int pageSize){
		return metamodelPackageDaoImpl.selectMetamodelPackageList(entity, pageNo, pageSize);
	}
	
	/**
	 * 根据map（packageId、packageName）查询模型包列表
	 * @param param
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Override
	public JSONObject queryMetamodelPackageBysql(Map param,int pageNo,int  pageSize){
		return metamodelPackageDaoImpl.queryMetamodelPackageBysql(param, pageNo, pageSize);
	}
	
	@Override
	public List<MetamodelPackage> selectList(Map<String, Object> map){
		return dao.selectByMap(map);
	}
}
