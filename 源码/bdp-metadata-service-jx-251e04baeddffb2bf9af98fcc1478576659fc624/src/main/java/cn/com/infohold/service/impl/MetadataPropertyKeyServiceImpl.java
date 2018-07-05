package cn.com.infohold.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.infohold.core.service.impl.ServiceImpl;
import cn.com.infohold.dao.IMetadataPropertyKeyDao;
import cn.com.infohold.dao.impl.MetadataPropertyKeyDaoImpl;
import cn.com.infohold.entity.MetadataPropertyKey;
import cn.com.infohold.service.IMetadataPropertyKeyService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@Service
public class MetadataPropertyKeyServiceImpl extends ServiceImpl<MetadataPropertyKeyDaoImpl, MetadataPropertyKey> implements IMetadataPropertyKeyService {
	@Autowired
	IMetadataPropertyKeyDao metadataPropertyKeyDaoImlp;
	@Override
	public List<MetadataPropertyKey> queryMetadataPropertyKeyVOByCode(String code){
		return metadataPropertyKeyDaoImlp.queryMetadataPropertyKeyVOByCode(code);
	}
}
