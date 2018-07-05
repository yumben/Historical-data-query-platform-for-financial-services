package cn.com.infohold.service;

import java.util.List;
import java.util.Map;

import cn.com.infohold.core.service.IService;
import cn.com.infohold.entity.Apppar;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface IAppparService extends IService<Apppar> {
	/**
	 *	根据code取apppar
	 * @param code
	 * @return
	 */
	Map<String,String> selectAppparList(String code);
	
	/**
	 * 根据code，value取showmsg
	 * @param code
	 * @param value
	 * @return
	 */
	String selectAppparList(String code,String value);
	
	/**
	 * 根据Map查询配置表类表
	 * @param map
	 * @return
	 */
	List<Apppar> selectList(Map<String, Object> map);
}
