package cn.com.infohold.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.com.infohold.core.service.impl.ServiceImpl;
import cn.com.infohold.dao.impl.AppparDaoImpl;
import cn.com.infohold.entity.Apppar;
import cn.com.infohold.service.IAppparService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@Service
public class AppparServiceImpl extends ServiceImpl<AppparDaoImpl, Apppar> implements IAppparService {
	
	@Override
	public Map<String,String> selectAppparList(String code) {
		return dao.selectAppparList(code);
	}
	
	@Override
	public String selectAppparList(String code,String value) {
		return dao.selectAppparList(code,value);
	}

	@Override
	public List<Apppar> selectList(Map<String, Object> map) {
		return dao.selectByMap(map);
	}
}
