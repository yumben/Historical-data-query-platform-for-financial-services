package cn.com.infohold.service;

import java.util.List;
import java.util.Map;

import bdp.commons.dataservice.ret.RetBean;
import bdp.commons.sys.SysMenu;


/**
 * <p>
 * 服务类
 * </p>
 *
 * @author huangdi
 * @since 2017-08-26
 */
public interface ISysMenuService {

	/**
	 * 查询菜单
	 * 
	 * @param entity
	 *            菜单实体
	 * @param pageNo
	 *            当前页码
	 * @param pageSize
	 *            每页大小
	 * @return
	 * @throws Exception
	 */
	RetBean selectMenuList(SysMenu entity, int pageNo, int pageSize, String flag) throws Exception;

	List<Map<String, Object>> selectMenuAll() throws Exception;

	boolean deleteById(SysMenu entity);

	boolean updateById(SysMenu entity);

	SysMenu selectById(SysMenu entity) throws Exception;

	List<Map<String, Object>> getMenuTree(String token);

	boolean insert(SysMenu entity);
}
