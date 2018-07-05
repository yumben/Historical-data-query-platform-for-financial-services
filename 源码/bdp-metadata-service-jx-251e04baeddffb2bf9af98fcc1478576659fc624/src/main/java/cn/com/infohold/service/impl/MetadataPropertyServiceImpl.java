package cn.com.infohold.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.com.infohold.core.service.impl.ServiceImpl;
import cn.com.infohold.dao.IMetadataPropertyDao;
import cn.com.infohold.dao.impl.MetadataPropertyDaoImpl;
import cn.com.infohold.entity.MetadataProperty;
import cn.com.infohold.service.IMetadataPropertyService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@Service
public class MetadataPropertyServiceImpl extends ServiceImpl<MetadataPropertyDaoImpl, MetadataProperty> implements IMetadataPropertyService {
	@Autowired
	IMetadataPropertyDao metadataPropertyDaoImpl;
	/**
	 * 查询菜单
	 * @param entity 菜单实体
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<MetadataProperty> selectMetadataPropertyList(MetadataProperty entity,int pageNo,int pageSize){
		return metadataPropertyDaoImpl.selectMetadataPropertyList(entity,pageNo ,pageSize);
	}
	
	/**
	 * 根据元数据ID查询元数据属性列表
	 * @param metadataId
	 * @return 返回列表
	 */
	@Override
	public List<MetadataProperty> queryMetadataPropertyVO(String metadataId){
		return metadataPropertyDaoImpl.queryMetadataPropertyVO(metadataId);
	}
	
	/**
	 * 根据元模型ID查询元数据属性列表
	 * @param metadataId
	 * @param propertyCode
	 * @param classId
	 * @return
	 */
	@Override
	public List<MetadataProperty> queryMetaDataPropertyByMetadataIdAndPropertyCode(String metadataId,String propertyCode,String classId){
		return metadataPropertyDaoImpl.queryMetaDataPropertyByMetadataIdAndPropertyCode( metadataId,propertyCode, classId);
	}
	
	/**
	 * 根据元数据ID查询元数据属性
	 * @param metadataId
	 * @return json数组
	 */
	@Override
	public JSONObject queryMetaDataPropertyByMetadataId(String metadataId){
		return metadataPropertyDaoImpl.queryMetaDataPropertyByMetadataId(metadataId);
	}
	
	/**
	 * 根据元数据ID查询元数据属性列表（根据class_property_id排序升序）
	 * @param metadataId
	 * @return
	 */
	@Override
	public List<MetadataProperty> selectMetaDataPropertyByMetadataId(String metadataId){
		return metadataPropertyDaoImpl.selectMetaDataPropertyByMetadataId(metadataId);
	}
	
	/**
	 * 根据元数据ID、属性代码、元模型ID查询元数据属性列表
	 * @param metadataId
	 * @param propertyCode
	 * @param classId
	 * @return
	 */
	@Override
	public List<MetadataProperty> queryMetaDataPropertyByMetadataIdAndPropertyCode1(String metadataId,String propertyCode,String classId){
		return metadataPropertyDaoImpl.queryMetaDataPropertyByMetadataIdAndPropertyCode1(metadataId,propertyCode,classId);
	}

	@Override
	public Map<String, Object> selectForMap(Map<String, Object> columMap) {
		Map<String, Object> map=new HashMap<String, Object>();
		List<MetadataProperty> list=dao.selectByMap(columMap);
		for (MetadataProperty metadataProperty : list) {
			map.put(metadataProperty.getClassPropertyId(), metadataProperty.getPropertyValue());
		}
		return map;
	}
}
