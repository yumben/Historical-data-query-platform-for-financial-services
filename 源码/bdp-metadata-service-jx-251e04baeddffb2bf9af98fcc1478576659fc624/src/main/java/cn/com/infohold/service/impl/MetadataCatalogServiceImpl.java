package cn.com.infohold.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.com.infohold.core.service.impl.ServiceImpl;
import cn.com.infohold.dao.IMetadataCatalogDao;
import cn.com.infohold.dao.impl.MetadataCatalogDaoImpl;
import cn.com.infohold.entity.MetadataCatalog;
import cn.com.infohold.service.IMetadataCatalogService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@Service
public class MetadataCatalogServiceImpl extends ServiceImpl<MetadataCatalogDaoImpl, MetadataCatalog> implements IMetadataCatalogService {
	@Autowired
	IMetadataCatalogDao metadataCatalogDaoImpl;
	/**
	 * 查询菜单
	 * @param entity 菜单实体
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<MetadataCatalog> selectMetadataCatalogList(MetadataCatalog entity,int pageNo,int pageSize){
		return metadataCatalogDaoImpl.selectMetadataCatalogList(entity, pageNo, pageSize);
	}
	
	@Override
	public List<MetadataCatalog> queryMetaDataCatalogByParentIdAndType(String parentId, String classType){
		return metadataCatalogDaoImpl.queryMetaDataCatalogByParentIdAndType(parentId, classType);
	}
	
	@Override
	public String queryContextCatalog(String catalogId){
		return metadataCatalogDaoImpl.queryContextCatalog(catalogId);
	}
	
	@Override
	public List<String> queryContextCatalog(String catalogId, String metadataId){
		return metadataCatalogDaoImpl.queryContextCatalog(catalogId, metadataId);
	}
	
	@Override
	public MetadataCatalog selectMetadataCatalogByCatalogId(String catalogId){
		return metadataCatalogDaoImpl.selectMetadataCatalogByCatalogId(catalogId);
	}
	
	@Override
	public Map<String,String> queryContextCatalogMap(String metadataIdStr){
		return metadataCatalogDaoImpl.queryContextCatalogMap(metadataIdStr);
	}
	
	/**
	 * 根据目录ID删除目录
	 * @param catalogId
	 * @return
	 */
	@Override
	public JSONObject delMetadataCatalogByCatalogId(String catalogId){
		return metadataCatalogDaoImpl.delMetadataCatalogByCatalogId(catalogId);
	}
	
	@Override
	public List<MetadataCatalog> queryCatalogBy(String classId){
		return metadataCatalogDaoImpl.queryCatalogBy(classId);
	}
}
