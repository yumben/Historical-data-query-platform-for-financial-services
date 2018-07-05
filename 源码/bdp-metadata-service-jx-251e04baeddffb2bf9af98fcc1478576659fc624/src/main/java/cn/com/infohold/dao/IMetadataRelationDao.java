package cn.com.infohold.dao;

import java.util.List;

import cn.com.infohold.core.dao.IDao;
import cn.com.infohold.entity.MetadataRelation;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface IMetadataRelationDao extends IDao<MetadataRelation> {
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
