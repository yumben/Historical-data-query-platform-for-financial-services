package cn.com.infohold.dao.impl.mybatis;

import org.springframework.stereotype.Service;

import cn.com.infohold.core.dao.impl.MyBatisDaoImpl;
import cn.com.infohold.dao.IViewColumnCofigDao;
import cn.com.infohold.entity.ViewColumnCofig;
import cn.com.infohold.mapper.ViewColumnCofigMapper;

@Service
public class ViewColumnCofigDaoImpl extends MyBatisDaoImpl<ViewColumnCofigMapper, ViewColumnCofig>
		implements IViewColumnCofigDao {

}
