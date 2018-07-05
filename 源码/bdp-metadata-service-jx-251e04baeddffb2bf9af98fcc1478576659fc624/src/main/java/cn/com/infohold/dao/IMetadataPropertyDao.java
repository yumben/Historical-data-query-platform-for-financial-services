package cn.com.infohold.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.com.infohold.core.dao.IDao;
import cn.com.infohold.entity.MetadataProperty;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface IMetadataPropertyDao extends IDao<MetadataProperty> {
	/**
	 * 查询菜单
	 * @param entity 菜单实体
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @return
	 * @throws Exception
	 */
	List<MetadataProperty> selectMetadataPropertyList(MetadataProperty entity,int pageNo,int pageSize);
	
	/**
	 * 根据元数据ID查询元数据属性列表
	 * @param metadataId
	 * @return 返回列表
	 */
	List<MetadataProperty> queryMetadataPropertyVO(String metadataId);
	
	/**
	 * 根据元模型ID查询元数据属性列表
	 * @param metadataId
	 * @param propertyCode
	 * @param classId
	 * @return
	 */
	List<MetadataProperty> queryMetaDataPropertyByMetadataIdAndPropertyCode(String metadataId,String propertyCode,String classId);
	
	/**
	 * 根据元数据ID查询元数据属性
	 * @param metadataId
	 * @return json数组
	 */
	JSONObject queryMetaDataPropertyByMetadataId(String metadataId);
	
	/**
	 * 根据元数据ID查询元数据属性列表（根据class_property_id排序升序）
	 * @param metadataId
	 * @return
	 */
	List<MetadataProperty> selectMetaDataPropertyByMetadataId(String metadataId);
	
	/**
	 * 根据元数据ID、属性代码、元模型ID查询元数据属性列表
	 * @param metadataId
	 * @param propertyCode
	 * @param classId
	 * @return
	 */
	List<MetadataProperty> queryMetaDataPropertyByMetadataIdAndPropertyCode1(String metadataId,String propertyCode,String classId);

}
