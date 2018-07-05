package cn.com.infohold.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.com.infohold.core.service.impl.ServiceImpl;
import cn.com.infohold.dao.impl.MetamodelRelationTypeDaoImpl;
import cn.com.infohold.entity.MetamodelRelationType;
import cn.com.infohold.service.IMetamodelRelationTypeService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@Service
public class MetamodelRelationTypeServiceImpl extends ServiceImpl<MetamodelRelationTypeDaoImpl, MetamodelRelationType> implements IMetamodelRelationTypeService {
	@Override
	public List<MetamodelRelationType> selectMetamodelRelationTypeList(MetamodelRelationType entity,int pageNo,int pageSize){
		return dao.selectMetamodelRelationTypeList(entity, pageNo, pageSize);
	}
	@Override
	public List<MetamodelRelationType> selectList(Map<String, Object> map){
		return dao.selectByMap(map);
	}
}
