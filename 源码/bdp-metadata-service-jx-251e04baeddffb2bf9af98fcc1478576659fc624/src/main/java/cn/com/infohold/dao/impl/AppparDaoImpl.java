package cn.com.infohold.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import cn.com.infohold.core.dao.impl.MyBatisDaoImpl;
import cn.com.infohold.dao.IAppparDao;
import cn.com.infohold.entity.Apppar;
import cn.com.infohold.mapper.AppparMapper;
import cn.com.infohold.tools.util.StringUtil;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@Service
public class AppparDaoImpl extends MyBatisDaoImpl<AppparMapper, Apppar> implements IAppparDao {
	@Autowired
	AppparMapper appparMapper;
	
	@Override
	public Map<String,String> selectAppparList(String code) {
		Map<String, String> resultMap = new HashMap<String, String>();
		EntityWrapper<Apppar> wrapper = new EntityWrapper<Apppar>();
		if(!"".equals(StringUtil.getString(code))){
			wrapper.like("code", code);
		}
		List<Apppar> list = appparMapper.selectList(wrapper);
		if (list !=null ) {
			for (int i = 0; i < list.size(); i++) {
				Apppar apppar = list.get(i);
				resultMap.put(apppar.getValue(), apppar.getShowmsg());
			}
		}
		return resultMap;
	}
	
	@Override
	public String selectAppparList(String code,String value) {
		Map<String, String> resultMap = new HashMap<String, String>();
		EntityWrapper<Apppar> wrapper = new EntityWrapper<Apppar>();
		if(!"".equals(StringUtil.getString(code))){
			wrapper.like("code", code);
		}
		if(!"".equals(StringUtil.getString(value))){
			wrapper.like("value", value);
		}
		List<Apppar> list = appparMapper.selectList(wrapper);
		if (list !=null ) {
			for (int i = 0; i < list.size(); i++) {
				Apppar apppar = list.get(i);
				return apppar.getShowmsg();
			}
		}
		return null;
	}
}
