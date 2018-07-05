package cn.com.infohold.dao.impl;

import org.springframework.stereotype.Service;

import cn.com.infohold.core.dao.impl.MyBatisDaoImpl;
import cn.com.infohold.dao.IMetadataGroupDao;
import cn.com.infohold.entity.MetadataGroup;
import cn.com.infohold.mapper.MetadataGroupMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@Service
public class MetadataGroupDaoImpl extends MyBatisDaoImpl<MetadataGroupMapper, MetadataGroup> implements IMetadataGroupDao {
	
}
