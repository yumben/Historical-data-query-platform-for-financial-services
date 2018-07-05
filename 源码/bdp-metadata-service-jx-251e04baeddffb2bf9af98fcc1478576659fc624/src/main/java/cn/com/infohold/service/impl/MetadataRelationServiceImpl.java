package cn.com.infohold.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.com.infohold.core.service.impl.ServiceImpl;
import cn.com.infohold.dao.impl.MetadataRelationDaoImpl;
import cn.com.infohold.entity.MetadataRelation;
import cn.com.infohold.service.IMetadataRelationService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@Service
public class MetadataRelationServiceImpl extends ServiceImpl<MetadataRelationDaoImpl, MetadataRelation> implements IMetadataRelationService {
	@Override
	public List<MetadataRelation> selectList(Map<String, Object> map){
		return dao.selectByMap(map);
	}

	@Override
	public List<MetadataRelation> selectMetadataRelationList(MetadataRelation entity, int pageNo, int pageSize) {
		
		return dao.selectMetadataRelationList(entity, pageNo, pageSize);
	}

	@Override
	public boolean delMetadataRelation(String id) {
		return dao.delMetadataRelation(id);
	}

	@Override
	public boolean addMetaDataRelation(String metadataId, String[] metadataedId, String conditionId,
			String relationId) {
		return dao.addMetaDataRelation(metadataId, metadataedId, conditionId, relationId);
	}

	@Override
	public List<MetadataRelation> queryMetadataRelationshipBymetadataId(String metadataId) {
		return dao.queryMetadataRelationshipBymetadataId(metadataId);
	}

	@Override
	public boolean delMetadataRalation(MetadataRelation metadataRelation) {
		// TODO Auto-generated method stub
		dao.delMetadataRalation(metadataRelation);
		return false;
	}

	@Override
	public List<MetadataRelation> queryRelationByIdAndRelationClassId(String metadataId, String relationId) {
		// TODO Auto-generated method stub
		
		return dao.queryRelationByIdAndRelationClassId(metadataId,relationId);
	}
}
