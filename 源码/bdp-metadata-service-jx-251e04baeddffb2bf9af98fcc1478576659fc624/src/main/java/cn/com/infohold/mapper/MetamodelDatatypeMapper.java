package cn.com.infohold.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import cn.com.infohold.entity.MetamodelDatatype;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface MetamodelDatatypeMapper extends BaseMapper<MetamodelDatatype> {

	/**
	 * 根据模型包名和数据类型代码或者名称查询数据类型
	 * @param page
	 * @param packageId
	 * @param datatypeName
	 * @return
	 */
	List<MetamodelDatatype> selectMetamodelDatatypeByIdAndCode(Pagination page,@Param("packageId")String packageId,@Param("datatypeName")String datatypeName);
}