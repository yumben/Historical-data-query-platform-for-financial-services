package cn.com.infohold.dao;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.com.infohold.core.dao.IDao;
import cn.com.infohold.entity.MetamodelDatatype;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface IMetamodelDatatypeDao extends IDao<MetamodelDatatype> {
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
}
