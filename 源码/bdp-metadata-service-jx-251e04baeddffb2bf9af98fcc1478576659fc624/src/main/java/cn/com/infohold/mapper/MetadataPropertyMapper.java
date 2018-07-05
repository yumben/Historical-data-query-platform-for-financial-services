package cn.com.infohold.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.com.infohold.entity.MetadataProperty;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface MetadataPropertyMapper extends BaseMapper<MetadataProperty> {


	/**
	 * 根据元数据ID ,属性编码，模型ID查询元数据属性信息
	 * @param metadataId
	 * @param propertyCode
	 * @param classId
	 * @return
	 */
	List<MetadataProperty> queryMetaDataPropertyByMetadataIdAndPropertyCode(@Param("metadataId") String metadataId,@Param("propertyCode") String propertyCode,@Param("classId") String classId);
	
	/**
	 * 根据元数据ID ,属性编码，模型ID查询元数据属性信息，方法1
	 * @param metadataId
	 * @param propertyCode
	 * @param classId
	 * @return
	 */
	List<MetadataProperty> queryMetaDataPropertyByMetadataIdAndPropertyCode1(@Param("metadataId") String metadataId,@Param("propertyCode") String propertyCode,@Param("classId") String classId);
	
	/**
	 * 根据元数据ID删除元数据属性信息
	 * @param mids
	 */
	void deleteByMetadataIdList(@Param("mids") List<String> mids);
	
	/**
	 * 根据元数据ID查询元数据属性信息
	 * @param metadataId
	 * @return
	 */
	List<MetadataProperty> queryMetaDataPropertyByMetadataId(@Param("metadataId") String metadataId);

}