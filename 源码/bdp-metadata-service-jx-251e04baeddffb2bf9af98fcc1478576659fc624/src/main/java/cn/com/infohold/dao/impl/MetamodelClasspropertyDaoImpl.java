package cn.com.infohold.dao.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;

import cn.com.infohold.core.dao.impl.MyBatisDaoImpl;
import cn.com.infohold.dao.IMetamodelClasspropertyDao;
import cn.com.infohold.entity.MetamodelClassproperty;
import cn.com.infohold.mapper.MetamodelClasspropertyMapper;
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
public class MetamodelClasspropertyDaoImpl extends MyBatisDaoImpl<MetamodelClasspropertyMapper, MetamodelClassproperty> implements IMetamodelClasspropertyDao {
	@Autowired
	MetamodelClasspropertyMapper metamodelClasspropertyMapper;
	@Override
	public List<MetamodelClassproperty> selectMetamodelClasspropertyList(MetamodelClassproperty entity, int pageNo, int pageSize){
		// TODO Auto-generated method stub
		EntityWrapper<MetamodelClassproperty> wrapper = new EntityWrapper<MetamodelClassproperty>();
		if(!"".equals(StringUtil.getString(entity.getPropertyId()))){
			wrapper.like("property_id", entity.getPropertyId());
		}

		if(!"".equals(StringUtil.getString(entity.getDatatypeId()))){
			wrapper.eq("datatypeId", entity.getDatatypeId());
		}

		if(!"".equals(StringUtil.getString(entity.getClassId()))){
			wrapper.eq("class_id", entity.getClassId());
		}

		if(!"".equals(StringUtil.getString(entity.getPropertyCode()))){
			wrapper.eq("property_code", entity.getPropertyCode());
		}

		if(!"".equals(StringUtil.getString(entity.getPropertyName()))){
			wrapper.eq("property_name", entity.getPropertyName());
		}

		if(!"".equals(StringUtil.getString(entity.getEditDate()))){
			wrapper.eq("edit_date", entity.getEditDate());
		}

		if(!"".equals(StringUtil.getString(entity.getEditName()))){
			wrapper.eq("edit_name", entity.getEditName());
		}

		if(!"".equals(StringUtil.getString(entity.getInheritance()))){
			wrapper.eq("inheritance", entity.getInheritance());
		}

		if(!"".equals(StringUtil.getString(entity.getIscannull()))){
			wrapper.eq("iscannull", entity.getIscannull());
		}

		if(!"".equals(StringUtil.getString(entity.getIsshow()))){
			wrapper.eq("isshow", entity.getIsshow());
		}

		if(!"".equals(StringUtil.getString(entity.getIsedit()))){
			wrapper.eq("isedit", entity.getIsedit());
		}

		if(!"".equals(StringUtil.getString(entity.getIspassword()))){
			wrapper.eq("ispassword", entity.getIspassword());
		}

		if(!"".equals(StringUtil.getString(entity.getLengths()))){
			wrapper.eq("lengths", entity.getLengths());
		}

		if(!"".equals(StringUtil.getString(entity.getDefaultValue()))){
			wrapper.eq("default_value", entity.getDefaultValue());
		}

		if(!"".equals(StringUtil.getString(entity.getEditcontrolName()))){
			wrapper.eq("Editcontrol_name", entity.getEditcontrolName());
		}
		pageNo = CommonUtil.offsetCurrent(pageNo, pageSize);
		RowBounds rowBounds = new RowBounds(pageNo,pageSize);
		return metamodelClasspropertyMapper.selectPage(rowBounds, wrapper);
	}
	
	@Override
	public List<MetamodelClassproperty> selectMetamodelClasspropertyList(MetamodelClassproperty entity){
		// TODO Auto-generated method stub
		EntityWrapper<MetamodelClassproperty> wrapper = new EntityWrapper<MetamodelClassproperty>();
		if(!"".equals(StringUtil.getString(entity.getPropertyId()))){
			wrapper.like("property_id", entity.getPropertyId());
		}

		if(!"".equals(StringUtil.getString(entity.getDatatypeId()))){
			wrapper.eq("datatypeId", entity.getDatatypeId());
		}

		if(!"".equals(StringUtil.getString(entity.getClassId()))){
			wrapper.eq("class_id", entity.getClassId());
		}

		if(!"".equals(StringUtil.getString(entity.getPropertyCode()))){
			wrapper.eq("property_code", entity.getPropertyCode());
		}

		if(!"".equals(StringUtil.getString(entity.getPropertyName()))){
			wrapper.eq("property_name", entity.getPropertyName());
		}

		if(!"".equals(StringUtil.getString(entity.getEditDate()))){
			wrapper.eq("edit_date", entity.getEditDate());
		}

		if(!"".equals(StringUtil.getString(entity.getEditName()))){
			wrapper.eq("edit_name", entity.getEditName());
		}

		if(!"".equals(StringUtil.getString(entity.getInheritance()))){
			wrapper.eq("inheritance", entity.getInheritance());
		}

		if(!"".equals(StringUtil.getString(entity.getIscannull()))){
			wrapper.eq("iscannull", entity.getIscannull());
		}

		if(!"".equals(StringUtil.getString(entity.getIsshow()))){
			wrapper.eq("isshow", entity.getIsshow());
		}

		if(!"".equals(StringUtil.getString(entity.getIsedit()))){
			wrapper.eq("isedit", entity.getIsedit());
		}

		if(!"".equals(StringUtil.getString(entity.getIspassword()))){
			wrapper.eq("ispassword", entity.getIspassword());
		}

		if(!"".equals(StringUtil.getString(entity.getLengths()))){
			wrapper.eq("lengths", entity.getLengths());
		}

		if(!"".equals(StringUtil.getString(entity.getDefaultValue()))){
			wrapper.eq("default_value", entity.getDefaultValue());
		}

		if(!"".equals(StringUtil.getString(entity.getEditcontrolName()))){
			wrapper.eq("Editcontrol_name", entity.getEditcontrolName());
		}
		return metamodelClasspropertyMapper.selectList(wrapper);
	}
	
	@Override
	public Page<MetamodelClassproperty> queryByPropertyIDResultInfoAttr(String propertyId,
			Page<MetamodelClassproperty> page) {
		page.setRecords(metamodelClasspropertyMapper.queryByPropertyIDResultInfoAttr(propertyId, page));
		return page;
	}
	
	public List<MetamodelClassproperty> selectMetamodelClasspropertyList(String classId){
		// TODO Auto-generated method stub
		EntityWrapper<MetamodelClassproperty> wrapper = new EntityWrapper<MetamodelClassproperty>();
		wrapper.eq("class_id", classId);
		wrapper.orderBy("display_order");
		return metamodelClasspropertyMapper.selectList(wrapper);
	}
	
	public boolean deleteMetamodelClasspropertyById(String id) {
		EntityWrapper<MetamodelClassproperty> wrapper = new EntityWrapper<MetamodelClassproperty>();
    	wrapper.eq("parent_property_id", id);
    	metamodelClasspropertyMapper.delete(wrapper);
    	boolean flag = deleteById(id);
    	return flag;
	}
	
	public String selectMetamodelClasspropertyCount(MetamodelClassproperty metamodelClassproperty){
		if (metamodelClassproperty !=null) {
			EntityWrapper<MetamodelClassproperty> wrapper = new EntityWrapper<MetamodelClassproperty>();
        	
			if (metamodelClassproperty.getPropertyCode() != null) {
				wrapper.eq("property_code", metamodelClassproperty.getPropertyCode());
	        }
			if (metamodelClassproperty.getClassId() != null) {
				wrapper.eq("class_id", metamodelClassproperty.getClassId());
			}
			if (metamodelClassproperty.getPropertyName() != null) {
				wrapper.eq("property_name", metamodelClassproperty.getPropertyName());
			}
			if (metamodelClassproperty.getPropertyId()!=null) {
				wrapper.notIn("property_id", metamodelClassproperty.getPropertyId());
			}
			return String.valueOf(metamodelClasspropertyMapper.selectCount(wrapper));
		}else {
			return null;
		}
	}
}
