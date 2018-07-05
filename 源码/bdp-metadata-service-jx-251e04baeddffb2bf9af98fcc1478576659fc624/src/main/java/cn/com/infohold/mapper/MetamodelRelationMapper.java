package cn.com.infohold.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import cn.com.infohold.entity.MetamodelRelation;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface MetamodelRelationMapper extends BaseMapper<MetamodelRelation> {

	/**
	 * 根据map（relationType，relationName）查询模型关系
	 * @param param
	 * @param page
	 * @return
	 */
	List<MetamodelRelation> queryModelClassRelation(@Param("map")Map param,Pagination page);
	
	/**
	 * 根据关联关系ID查询模型关联关系列表
	 * @param relationId
	 * @return
	 */
	List<MetamodelRelation> queryByIdRelationInfo(@Param("relationId")String relationId);

}