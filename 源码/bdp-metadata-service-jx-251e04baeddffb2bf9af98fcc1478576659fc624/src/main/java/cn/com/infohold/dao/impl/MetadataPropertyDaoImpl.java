package cn.com.infohold.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;

import cn.com.infohold.core.dao.impl.MyBatisDaoImpl;
import cn.com.infohold.dao.IMetadataPropertyDao;
import cn.com.infohold.entity.MetadataProperty;
import cn.com.infohold.mapper.MetadataPropertyMapper;
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
public class MetadataPropertyDaoImpl extends MyBatisDaoImpl<MetadataPropertyMapper, MetadataProperty> implements IMetadataPropertyDao {
	@Autowired
	MetadataPropertyMapper metadataPropertyMapper;
	@Override
	public List<MetadataProperty> selectMetadataPropertyList(MetadataProperty entity, int pageNo, int pageSize){
		// TODO Auto-generated method stub
		EntityWrapper<MetadataProperty> wrapper = new EntityWrapper<MetadataProperty>();
		if(!"".equals(StringUtil.getString(entity.getMetadataPropertyId()))){
			wrapper.like("metadata_property_id", entity.getMetadataPropertyId());
		}

		if(!"".equals(StringUtil.getString(entity.getMetadataId()))){
			wrapper.eq("metadata_id", entity.getMetadataId());
		}

		if(!"".equals(StringUtil.getString(entity.getClassPropertyId()))){
			wrapper.eq("class_property_id", entity.getClassPropertyId());
		}

		if(!"".equals(StringUtil.getString(entity.getPropertyValue()))){
			wrapper.eq("property_value", entity.getPropertyValue());
		}

		if(!"".equals(StringUtil.getString(entity.getCreateDate()))){
			wrapper.eq("create_date", entity.getCreateDate());
		}

		if(!"".equals(StringUtil.getString(entity.getEditDate()))){
			wrapper.eq("edit_date", entity.getEditDate());
		}

		if(!"".equals(StringUtil.getString(entity.getEditName()))){
			wrapper.eq("edit_name", entity.getEditName());
		}
		pageNo = CommonUtil.offsetCurrent(pageNo, pageSize);
		RowBounds rowBounds = new RowBounds(pageNo,pageSize);
		return metadataPropertyMapper.selectPage(rowBounds, wrapper);
	}
	
	@Override
	public List<MetadataProperty> queryMetadataPropertyVO(String metadataId){
		EntityWrapper<MetadataProperty> wrapper = new EntityWrapper<MetadataProperty>();
		wrapper.eq("metadata_id", metadataId);
		return metadataPropertyMapper.selectList(wrapper);
		
	}
	
	@Override
	public List<MetadataProperty> queryMetaDataPropertyByMetadataIdAndPropertyCode(String metadataId,String propertyCode,String classId){
		return  metadataPropertyMapper.queryMetaDataPropertyByMetadataIdAndPropertyCode(metadataId, propertyCode, classId);
	}
	
	@Override
	public List<MetadataProperty> queryMetaDataPropertyByMetadataIdAndPropertyCode1(String metadataId,String propertyCode,String classId){
		return  metadataPropertyMapper.queryMetaDataPropertyByMetadataIdAndPropertyCode(metadataId, propertyCode, classId);
	}
	
	@Override
	public JSONObject queryMetaDataPropertyByMetadataId(String metadataId) {
		List<MetadataProperty> prolist = metadataPropertyMapper.queryMetaDataPropertyByMetadataId(metadataId);
		JSONObject proJsonObject = new JSONObject();
		if (prolist!=null) {
			for (int i = 0; i < prolist.size(); i++) {
				MetadataProperty metadataProperty = prolist.get(i);
				proJsonObject.put(metadataProperty.getPropertyCode(), metadataProperty.getPropertyValue());
			}
		}
		
		return proJsonObject;
	}
	
	@Override
	public List<MetadataProperty> selectMetaDataPropertyByMetadataId(String metadataId){
		List<MetadataProperty> list = new ArrayList<MetadataProperty>();
		EntityWrapper<MetadataProperty> wrapper = new EntityWrapper<MetadataProperty>();
		
		
		if(metadataId != null && !metadataId.equals("")){
			wrapper.eq("metadata_id", metadataId);
			wrapper.orderBy("class_property_id", true);
			return metadataPropertyMapper.selectList(wrapper);
		}
		return list;
	}
}
