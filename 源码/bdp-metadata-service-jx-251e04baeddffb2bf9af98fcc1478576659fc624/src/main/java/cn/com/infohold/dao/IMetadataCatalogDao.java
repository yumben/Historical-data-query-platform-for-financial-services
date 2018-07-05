package cn.com.infohold.dao;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.com.infohold.core.dao.IDao;
import cn.com.infohold.entity.MetadataCatalog;

public interface IMetadataCatalogDao extends IDao<MetadataCatalog> {
	/**
	 * 查询菜单
	 * @param entity 菜单实体
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @return
	 * @throws Exception
	 */
	List<MetadataCatalog> selectMetadataCatalogList(MetadataCatalog entity,int pageNo,int pageSize);
	
	List<MetadataCatalog> queryMetaDataCatalogByParentIdAndType(String parentId, String classType);
	
	String queryContextCatalog(String catalogId);
	
	List<String> queryContextCatalog(String catalogId, String metadataId);
	
	MetadataCatalog selectMetadataCatalogByCatalogId(String catalogId);
	
	Map<String,String> queryContextCatalogMap(String metadataIdStr);
	
	/**
	 * 根据目录ID删除目录
	 * @param catalogId
	 * @return
	 */
	JSONObject delMetadataCatalogByCatalogId(String catalogId);
	List<MetadataCatalog> queryCatalogBy(String classId);
}
