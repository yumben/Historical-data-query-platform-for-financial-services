package cn.com.infohold.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import cn.com.infohold.entity.MetamodelClassgroup;
import cn.com.infohold.entity.MetamodelClassproperty;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface MetamodelClasspropertyMapper extends BaseMapper<MetamodelClassproperty> {

	/**
	 * 根据模型ID查询模型属性列表
	 * @param classId
	 * @param page
	 * @return
	 */
	List<MetamodelClassproperty> queryModelClassPropertyByClassId(@Param("classId")String classId,Pagination page);
	
	/**
	 * 根据属性ID查询属性信息
	 * @param propertyId
	 * @param page
	 * @return
	 */
	List<MetamodelClassproperty> queryByPropertyIDResultInfoAttr(@Param("propertyId")String propertyId,Pagination page);


}