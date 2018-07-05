package cn.com.infohold.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import cn.com.infohold.entity.MetamodelDatatypeEnumerate;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface MetamodelDatatypeEnumerateMapper extends BaseMapper<MetamodelDatatypeEnumerate> {

	/**
	 * 根据模型包ID和枚举代码查询枚举
	 * @param page
	 * @param packageId
	 * @param enumerateNameOrCode
	 * @return
	 */
	List<MetamodelDatatypeEnumerate> queryModelEnumerateByNameAndPackageId(Pagination page ,@Param("packageId")String packageId,@Param("enumerateNameOrCode")String enumerateNameOrCode);


}