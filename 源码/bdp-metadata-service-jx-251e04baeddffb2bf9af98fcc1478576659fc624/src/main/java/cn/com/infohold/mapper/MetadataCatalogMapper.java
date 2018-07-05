package cn.com.infohold.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.com.infohold.entity.MetadataCatalog;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface MetadataCatalogMapper extends BaseMapper<MetadataCatalog> {


	/**
	 * 根据目录类型查询目录IDlist
	 * @param catalogType
	 * @return
	 */
	//List<MetadataCatalog> selectCatalogByCatalogType(@Param("catalogType") String catalogType);

	/**
	 * 根據模型ID查詢目錄
	 */
	List<MetadataCatalog> selectCatalogByClassId(@Param("classId")String classId);

}