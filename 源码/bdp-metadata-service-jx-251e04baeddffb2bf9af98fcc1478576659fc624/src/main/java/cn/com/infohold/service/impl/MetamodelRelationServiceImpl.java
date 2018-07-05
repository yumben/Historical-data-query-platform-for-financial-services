package cn.com.infohold.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.com.infohold.core.service.impl.ServiceImpl;
import cn.com.infohold.dao.impl.MetamodelRelationDaoImpl;
import cn.com.infohold.entity.MetamodelRelation;
import cn.com.infohold.service.IMetamodelRelationService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@Service
public class MetamodelRelationServiceImpl extends ServiceImpl<MetamodelRelationDaoImpl, MetamodelRelation> implements IMetamodelRelationService {
	@Override
	public List<MetamodelRelation> selectList(Map<String, Object> map){
		return dao.selectByMap(map);
	}

	@Override
	public List<MetamodelRelation> selectMetamodelRelationList(MetamodelRelation entity, int pageNo, int pageSize) {
		
		return dao.selectMetamodelRelationList(entity, pageNo, pageSize);
	}

	@Override
	public MetamodelRelation selectMetamodelRelationById(String id) {
		return dao.selectMetamodelRelationById(id);
	}

	@Override
	public List<MetamodelRelation> queryByClassIdRelationInfo(String classId) {
		return dao.queryByClassIdRelationInfo(classId);
	}

	@Override
	public JSONObject delMetamodelRelationById(String id) {
		return dao.delMetamodelRelationById(id);
	}
}
