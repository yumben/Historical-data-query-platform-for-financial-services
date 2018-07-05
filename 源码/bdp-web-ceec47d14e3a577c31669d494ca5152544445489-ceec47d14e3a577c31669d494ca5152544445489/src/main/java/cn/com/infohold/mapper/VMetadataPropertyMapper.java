package cn.com.infohold.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.com.infohold.entity.VMetadataProperty;

/**
 * <p>
  * VIEW Mapper 接口
 * </p>
 *
 * @author huangdi
 * @since 2017-11-28
 */
public interface VMetadataPropertyMapper extends BaseMapper<VMetadataProperty> {

	List<String> getAuthData(@Param("token")String token);
}