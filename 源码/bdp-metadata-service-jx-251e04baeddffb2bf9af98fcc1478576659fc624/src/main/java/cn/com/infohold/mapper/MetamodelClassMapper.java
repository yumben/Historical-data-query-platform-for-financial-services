package cn.com.infohold.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import cn.com.infohold.entity.MetamodelClass;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface MetamodelClassMapper extends BaseMapper<MetamodelClass> {

	/**
	 * 根据模型ID查询元模型
	 * @param classId
	 * @return
	 */
	MetamodelClass queryModelClassByClassId(@Param("classId")String classId);
	
	/**
	 * 根据模型代码查询元模型
	 * @param classCode
	 * @return
	 */
	MetamodelClass queryModelClassByCode(@Param("classCode")String classCode);
	
	/**
	 * 根据Map（packageId，classname）查询元模型
	 * @param param
	 * @param page
	 * @return
	 */
	List<MetamodelClass> queryModelClass(@Param("map")Map param,Pagination page);
	
	/**
	 * 根据（packageId，classname）查询元模型
	 * @param packageId
	 * @param classname
	 * @param page
	 * @return
	 */
	List<MetamodelClass> queryModelClass(@Param("packageId")String packageId,@Param("classname")String classname,Pagination page);

	/**
	 * 根据元模型的组合类
	 * @param classId
	 * @return
	 */
	List<MetamodelClass> selectMetamodelClassByGroupClassId(@Param("classId")String classId);

}