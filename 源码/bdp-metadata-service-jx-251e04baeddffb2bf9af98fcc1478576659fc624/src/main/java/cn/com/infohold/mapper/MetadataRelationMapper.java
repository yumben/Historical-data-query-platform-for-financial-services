package cn.com.infohold.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.com.infohold.entity.MetadataRelation;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface MetadataRelationMapper extends BaseMapper<MetadataRelation> {

	/**
	 * 删除元数据关联关系，包括关联与被关联
	 * @param mids
	 */
	void deleteByMetadataIdList(@Param("mids") List<String> mids);
	
	/**
	 * 删除关联关系，仅当前ID主关联的关系
	 * @param mids
	 */
	void deleteByMetadataIdListInMetadataId(@Param("mids") List<String> mids);
	
	/**
	 * 根据元数据ID查询关联关系
	 * @param mids
	 * @return
	 */
	List<MetadataRelation> selectMetadataRelation(@Param("mids") List<String> mids);

}