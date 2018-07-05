package cn.com.infohold.dao.impl.mybatis;

import org.springframework.stereotype.Service;

import cn.com.infohold.core.dao.impl.MyBatisDaoImpl;
import cn.com.infohold.dao.IViewTableCofigDao;
import cn.com.infohold.entity.ViewTableCofig;
import cn.com.infohold.mapper.ViewTableCofigMapper;

@Service
public class ViewTableCofigDaoImpl extends MyBatisDaoImpl<ViewTableCofigMapper, ViewTableCofig>
		implements IViewTableCofigDao {

}
