package cn.com.infohold.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.com.infohold.core.service.impl.ServiceImpl;
import cn.com.infohold.dao.impl.MetamodelDatatypeDaoImpl;
import cn.com.infohold.entity.MetamodelDatatype;
import cn.com.infohold.service.IMetamodelDatatypeService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@Service
public class MetamodelDatatypeServiceImpl extends ServiceImpl<MetamodelDatatypeDaoImpl, MetamodelDatatype> implements IMetamodelDatatypeService {
	/**
	 * 查询模型数据类型
	 * @param entity 菜单实体
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<MetamodelDatatype> selectMetamodelDatatypeList(MetamodelDatatype entity,int pageNo,int pageSize){
		return dao.selectMetamodelDatatypeList(entity, pageNo, pageSize);
	}
	
	/**
	 * 根据Code和ID查询模型数据类型
	 * @param param
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Override
	public JSONObject selectMetamodelDatatypeByIdAndCode(Map<String,String> param ,int pageNo,int pageSize){
		return dao.selectMetamodelDatatypeByIdAndCode(param, pageNo, pageSize);
	}
	
	/**
	 * 根据模型包ID查询模型数据类型列表
	 * @param packageId
	 * @return
	 */
	@Override
	public List<MetamodelDatatype> selectMetamodelDatatypeListByPackageId(String packageId){
		return dao.selectMetamodelDatatypeListByPackageId(packageId);
	}
	
	@Override
	public List<MetamodelDatatype> selectList(Map<String, Object> map){
		return dao.selectByMap(map);
	}
}
