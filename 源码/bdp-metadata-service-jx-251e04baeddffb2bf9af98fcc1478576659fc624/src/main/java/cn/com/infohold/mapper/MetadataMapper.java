package cn.com.infohold.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import cn.com.infohold.entity.Metadata;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface MetadataMapper extends BaseMapper<Metadata> {

	/**
	 * 查询指标来源表
	 * 
	 * @param ids
	 * @return
	 */
	List<Metadata> getTableList(@Param("ids") List<String> ids, @Param("metadataId") String metadataId);

	/**
	 * 查询指标的信息回显
	 * 
	 * @param mids
	 * @param cids
	 * @return
	 */
	// List<IndexInfo> getIndexInfo(@Param("mids") List<String>
	// mids,@Param("cids") List<String> cids);

	/**
	 * 查询当前id关联的东西儿
	 */
	List<Metadata> getFromMetaData(@Param("metadataId") String metadataId);

	/**
	 * 根据id查询上下文信息
	 * 
	 * @param metadataId
	 * @return
	 */
	Metadata selectContextById(@Param("metadataId") String metadataId);

	/**
	 * 删除元数据
	 * 
	 * @param mids
	 */
	// void deleteMetadataIdList(@Param("mids") List<String> mids);

	/**
	 * 根据元数据ID查询元数据对象
	 * 
	 * @param metadataId
	 * @return
	 */
	// Metadata selectMetadataById(@Param("metadataId") String metadataId);

	/**
	 * 根据目录IDList查询元数据
	 * 
	 * @param cids
	 * @return
	 */
	// List<Metadata> selectMetadataByCatalogIdList(@Param("cids") List<String>
	// cids);

	/**
	 * 根据目录ID查询元数据
	 * 
	 * @param cids
	 * @return
	 */
	// List<Metadata> selectMetadataByCatalogId(@Param("cid") String cid);

	/**
	 * 根据目录ID查询仪表盘元数据
	 * 
	 * @param cids
	 * @return
	 */
	List<Metadata> selectPannelMetadataByCatalogIdList(@Param("cid") String cid);

	// void updMetadataByMetadataId(@Param("metadataName") String
	// metadataName,@Param("metadataId") String metadataId);

	/**
	 * 根据父ID和类型查询元数据
	 * 
	 * @param parentId
	 * @param classType
	 * @return
	 */
	List<Metadata> selectMetadataByParentIdAndClassType(@Param("parentId") String parentId,
			@Param("classType") String classType);

	/**
	 * 根据父ID查询元数据
	 * 
	 * @param parentId
	 * @return
	 */
	List<Metadata> selectMetadataByParentId(@Param("parentId") String parentId);

	/**
	 * 根据父ID和用户类型查询元数据
	 * 
	 * @param parentId
	 * @param classType
	 * @return
	 */
	List<Metadata> queryMetaDataByParentIdAndClassIdAndClassType(@Param("parentId") String parentId,
			@Param("classId") String classId, @Param("classType") String classType);

	/**
	 * 根据父ID和用户类型查询元数据
	 * 
	 * @param parentId
	 * @param classType
	 * @return
	 */
	// List<Metadata> queryMetaDataByParentIdAndClassId(@Param("parentId")
	// String parentId,@Param("classId")String classId);

	/**
	 * 根据父ID和用户类型查询元数据
	 * 
	 * @param parentId
	 * @param classType
	 * @return
	 */
	// List<Metadata> queryMetaDataByParentIdAndNotId(@Param("parentId") String
	// parentId,@Param("metadataId")String metadataId);

	Metadata queryMetadataById(@Param("metadataId") String metadataId);

	/**
	 * 根据元数据code或者name 查询元数据
	 * 
	 * @param metadataId
	 * @return
	 */
	List<Metadata> queryMetaDataListByCodeOrName(Pagination page, @Param("codeOrName") String codeOrName);

	/**
	 * 根据模型ID查询元数据
	 * 
	 * @param classId
	 * @return
	 */
	List<Metadata> queryMataByclassId(@Param("classId") String classId);

	/**
	 * 根据模型ID、关联ID、元数据ID查询元数据关联关系
	 * 
	 * @param page
	 * @param classId
	 * @param relationId
	 * @param metadataId
	 * @return
	 */
	List<Metadata> queryRelationAndMetaDataByClassId(Pagination page, @Param("classId") String classId,
			@Param("relationId") String relationId, @Param("metadataId") String metadataId);

	/**
	 * 根据模型ID和被组合模型ID查询元数据
	 * 
	 * @param classId
	 * @param classgroupedId
	 * @return
	 */
	// List<Metadata>
	// selectMetadataByClassIDAndClassgroupedId(@Param("classId")String classId
	// ,@Param("classgroupedId")String classgroupedId);
	/**
	 * 根据元数据ID或者元数据Code查询元数据，包含属性信息code和value
	 * 
	 * @param map
	 * @return
	 */
	List<Metadata> queryMetadataByMap(@Param("map") Map<String, Object> map);

	/**
	 * 批量查询子集
	 * 
	 * @param pids
	 * @return
	 */
	List<Metadata> querySubMetadataByListId(@Param("pids") List<String> pids);

	/**
	 * 根据元数据ID列表查询关联元数据
	 * 
	 * @param pids
	 * @return
	 */
	List<Metadata> queryRelationMetadataByListId(@Param("class_id") String class_id, @Param("pids") List<String> pids);

	/**
	 * 根据元数据ID列表查询元数据
	 * 
	 * @param pids
	 * @return
	 */
	List<Metadata> queryMetadataByListId(@Param("pids") List<String> pids);

	/**
	 * 查询所有关联元数据
	 * 
	 * @return
	 */
	List<Metadata> queryAllRelationMetadata(@Param("class_id") String class_id);

	Metadata selectMetadataCodeById(@Param("metadataId") String metadataId);

}