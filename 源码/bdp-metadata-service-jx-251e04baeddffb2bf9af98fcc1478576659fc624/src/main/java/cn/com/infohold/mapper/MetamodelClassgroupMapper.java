package cn.com.infohold.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import cn.com.infohold.entity.MetamodelClassgroup;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface MetamodelClassgroupMapper extends BaseMapper<MetamodelClassgroup> {

	/**
	 * 根据Map（classId，groupedClassName，groupName）查询模型组合关系
	 * @param param
	 * @param page
	 * @return
	 */
	List<MetamodelClassgroup> queryModelClassGroup(@Param("map")Map param,Pagination page);
	
	/**
	 * 根据Map（classId，groupedClassName，groupName）查询模型组合关系
	 * @param param
	 * @param page
	 * @return
	 */
	List<MetamodelClassgroup> queryModelClassGroupByClassId(@Param("map")Map param,Pagination page);

	/**
	 * 根据Map（classId，groupClassName，groupName）查询模型组合关系
	 * @param param
	 * @param page
	 * @return
	 */
	List<MetamodelClassgroup> queryModelClassGrouped(@Param("map")Map param,Pagination page);

}