package cn.com.infohold.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.com.infohold.entity.Apppar;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface MetadataDictTypeMapper extends BaseMapper<Apppar> {
	public List<Map<String, Object>> selectMetadataDictTypeByIds(@Param("ids") List<String> ids);

	public List<Map<String, Object>> selectParameterListByIds(@Param("ids") List<String> ids);

	public List<Map<String, Object>> selectMetadataDictType(String dicti_type);

	public List<Map<String, Object>> selectParameterList();
}