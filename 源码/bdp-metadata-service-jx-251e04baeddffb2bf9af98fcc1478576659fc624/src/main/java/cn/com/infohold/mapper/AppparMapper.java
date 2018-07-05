package cn.com.infohold.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.com.infohold.entity.Apppar;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface AppparMapper extends BaseMapper<Apppar> {
	/**
	 * 根据code和value查询
	 * @param code
	 * @param value
	 * @return
	 */
	//Apppar selectApppar(@Param("code") String code,@Param("value") String value);
}