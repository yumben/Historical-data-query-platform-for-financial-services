package cn.com.infohold.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.com.infohold.core.service.IService;
import cn.com.infohold.entity.MetamodelDatatype;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface IMetamodelDatatypeService extends IService<MetamodelDatatype> {
	/**
	 * 查询模型数据类型
	 * @param entity 菜单实体
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @return
	 * @throws Exception
	 */
	List<MetamodelDatatype> selectMetamodelDatatypeList(MetamodelDatatype entity,int pageNo,int pageSize);
	
	/**
	 * 根据Code和ID查询模型数据类型
	 * @param param
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	JSONObject selectMetamodelDatatypeByIdAndCode(Map<String,String> param ,int pageNo,int pageSize);
	
	/**
	 * 根据模型包ID查询模型数据类型列表
	 * @param packageId
	 * @return
	 */
	List<MetamodelDatatype> selectMetamodelDatatypeListByPackageId(String packageId);
	
	/**
	 * 根据Map查询模型数据类型列表
	 * @param map<String,Object>(datatype_id-数据类型id,package_id-元模型包ID,datatype_code-数据类型代码,datatype_name-数据类型名称,
	 * datatype_describe-数据类型描述,visibility-可见性,create_date-创建时间)，map可为空
	 * @return
	 */
	List<MetamodelDatatype> selectList(Map<String, Object> map);
}
