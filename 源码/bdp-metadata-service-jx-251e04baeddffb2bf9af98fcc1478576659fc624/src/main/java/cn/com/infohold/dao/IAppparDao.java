package cn.com.infohold.dao;

import java.util.Map;

import cn.com.infohold.core.dao.IDao;
import cn.com.infohold.entity.Apppar;

public interface IAppparDao extends IDao<Apppar> {
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
}
