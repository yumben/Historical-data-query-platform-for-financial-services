package cn.com.infohold.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import cn.com.infohold.core.dao.impl.MyBatisDaoImpl;
import cn.com.infohold.dao.IMetamodelRelationTypeDao;
import cn.com.infohold.entity.MetamodelRelationType;
import cn.com.infohold.mapper.MetamodelRelationTypeMapper;
import cn.com.infohold.tools.util.CommonUtil;
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
public class MetamodelRelationTypeDaoImpl extends MyBatisDaoImpl<MetamodelRelationTypeMapper, MetamodelRelationType> implements IMetamodelRelationTypeDao {
	@Autowired
	MetamodelRelationTypeMapper metamodelRelationTypeMapper;
	@Override
	public List<MetamodelRelationType> selectMetamodelRelationTypeList(MetamodelRelationType entity, int pageNo, int pageSize){
		// TODO Auto-generated method stub
		EntityWrapper<MetamodelRelationType> wrapper = new EntityWrapper<MetamodelRelationType>();
		if(!"".equals(StringUtil.getString(entity.getRelationTypeId()))){
			wrapper.like("relation_type_id", entity.getRelationTypeId());
		}

		if(!"".equals(StringUtil.getString(entity.getRelationTypeName()))){
			wrapper.eq("relation_type_name", entity.getRelationTypeName());
		}

		if(!"".equals(StringUtil.getString(entity.getDescribes()))){
			wrapper.eq("describes", entity.getDescribes());
		}

		if(!"".equals(StringUtil.getString(entity.getParentrelaId()))){
			wrapper.eq("parentrela_id", entity.getParentrelaId());
		}

		pageNo = CommonUtil.offsetCurrent(pageNo, pageSize);
		RowBounds rowBounds = new RowBounds(pageNo,pageSize);
		return metamodelRelationTypeMapper.selectPage(rowBounds, wrapper);
	}
	@Override
	public List<MetamodelRelationType> selectList(Map<String, String> map){
		EntityWrapper<MetamodelRelationType> wrapper = new EntityWrapper<MetamodelRelationType>();
		if(!"".equals(StringUtil.getString(map.get("relationTypeId")))){
			wrapper.like("relation_type_id", map.get("relationTypeId"));
		}

		if(!"".equals(StringUtil.getString(map.get("relationTypeName")))){
			wrapper.eq("relation_type_name", map.get("relationTypeName"));
		}

		if(!"".equals(StringUtil.getString(map.get("describes")))){
			wrapper.eq("describes", map.get("describes"));
		}

		if(!"".equals(StringUtil.getString(map.get("parentrelaId")))){
			wrapper.eq("parentrela_id", map.get("parentrelaId"));
		}

		return metamodelRelationTypeMapper.selectList(wrapper);
	}
}
