package cn.com.infohold.service;

import java.util.List;
import java.util.Map;

import cn.com.infohold.core.service.IService;
import cn.com.infohold.entity.MetadataRelation;
import cn.com.infohold.entity.MetamodelRelation;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface IMetadataRelationService extends IService<MetadataRelation> {
	/**
	 * 根据Map查询模型包列表
	 * @param map<String,Object>(metadata_relation_id-元数据关联id,metadata_id-元数据id,
	 * metadata_relationed-被关联元数据id,
	 * condition_id-条件id,relation_id-关联关系id,create_date-创建时间)，map可为空
	 * @return
	 */
	List<MetadataRelation> selectList(Map<String, Object> map);
	
	/**
	 * 
	 * @param entity
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<MetadataRelation> selectMetadataRelationList(MetadataRelation entity,int pageNo,int pageSize);
	/**
	 * 删除元数据关联关系
	 */
	boolean delMetadataRelation(String id);
	/**
	 * 添加元数据关联关系
	 * @param metadataId
	 * @param metadataedId
	 * @param conditionId
	 * @param relationId
	 * @return
	 */
	boolean addMetaDataRelation(String metadataId ,String[] metadataedId,String conditionId,String relationId);
	
	/**
	 * 根据元数据ID查询元数据关联关系
	 * @param metadataId
	 * @return
	 */
	List<MetadataRelation> queryMetadataRelationshipBymetadataId(String metadataId);
	
	/**
	 * 根据元数据关联关系ID删除元数据关联关系
	 * @param metadataRelation
	 * @return
	 */
	boolean delMetadataRalation(MetadataRelation metadataRelation);

	/**
	 * 根据元数据ID和关联关系ID查询关联关系列表
	 * @param metadataId
	 * @param relationId
	 * @return
	 */
	List<MetadataRelation> queryRelationByIdAndRelationClassId(String metadataId, String relationId);
}
