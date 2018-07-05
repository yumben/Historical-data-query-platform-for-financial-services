package cn.com.infohold.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import cn.com.infohold.entity.MetamodelPackage;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface MetamodelPackageMapper extends BaseMapper<MetamodelPackage> {
	/**
	 * 根据模型包名称和模型包ID列表查询模型包列表
	 * @param page
	 * @param idlist
	 * @param packageName
	 * @return
	 */
	List<MetamodelPackage> queryModelePackageByIdAndPackeName(Pagination page,@Param("idlist")List<String> idlist,@Param("packageName")String packageName);

}