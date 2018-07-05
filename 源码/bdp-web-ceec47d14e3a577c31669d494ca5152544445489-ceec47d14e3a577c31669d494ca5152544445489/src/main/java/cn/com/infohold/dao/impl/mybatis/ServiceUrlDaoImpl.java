package cn.com.infohold.dao.impl.mybatis;

import org.springframework.stereotype.Service;

import cn.com.infohold.core.dao.impl.MyBatisDaoImpl;
import cn.com.infohold.dao.IServiceUrlDao;
import cn.com.infohold.entity.ServiceUrl;
import cn.com.infohold.mapper.ServiceUrlMapper;

@Service
public class ServiceUrlDaoImpl extends MyBatisDaoImpl<ServiceUrlMapper, ServiceUrl> implements IServiceUrlDao {


}
