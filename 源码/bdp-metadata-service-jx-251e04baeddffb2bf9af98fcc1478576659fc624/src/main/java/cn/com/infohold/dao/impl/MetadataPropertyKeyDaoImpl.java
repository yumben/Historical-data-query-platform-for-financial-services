package cn.com.infohold.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import cn.com.infohold.core.dao.impl.MyBatisDaoImpl;
import cn.com.infohold.dao.IMetadataPropertyKeyDao;
import cn.com.infohold.entity.MetadataPropertyKey;
import cn.com.infohold.mapper.MetadataPropertyKeyMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@Service
public class MetadataPropertyKeyDaoImpl extends MyBatisDaoImpl<MetadataPropertyKeyMapper, MetadataPropertyKey> implements IMetadataPropertyKeyDao {
	@Autowired
	MetadataPropertyKeyMapper metadataPropertyKeyMapper;
	@Override
	public List<MetadataPropertyKey> queryMetadataPropertyKeyVOByCode(String code){
		EntityWrapper<MetadataPropertyKey> wrapper = new EntityWrapper<MetadataPropertyKey>();
		if (code != null) {
			wrapper.eq("code", code);
		}
		return metadataPropertyKeyMapper.selectList(wrapper);
	}
}
