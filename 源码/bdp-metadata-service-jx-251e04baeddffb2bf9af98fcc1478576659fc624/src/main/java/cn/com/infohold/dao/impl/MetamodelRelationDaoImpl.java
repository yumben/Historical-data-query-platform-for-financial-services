package cn.com.infohold.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DaoSupport;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;

import cn.com.infohold.core.dao.impl.MyBatisDaoImpl;
import cn.com.infohold.dao.IMetamodelRelationDao;
import cn.com.infohold.entity.MetadataRelation;
import cn.com.infohold.entity.MetamodelClass;
import cn.com.infohold.entity.MetamodelRelation;
import cn.com.infohold.mapper.MetamodelClassMapper;
import cn.com.infohold.mapper.MetamodelRelationMapper;
import cn.com.infohold.service.IMetadataRelationService;
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
public class MetamodelRelationDaoImpl extends MyBatisDaoImpl<MetamodelRelationMapper, MetamodelRelation> implements IMetamodelRelationDao {
	@Autowired
	MetamodelRelationMapper metamodelRelationMapper;
	@Autowired
	MetamodelClassMapper metamodelClassMapper;
	@Autowired
	IMetadataRelationService metadataRelationServiceImpl;
	@Override
	public List<MetamodelRelation> selectMetamodelRelationList(MetamodelRelation entity, int pageNo, int pageSize){
		// TODO Auto-generated method stub
		EntityWrapper<MetamodelRelation> wrapper = new EntityWrapper<MetamodelRelation>();
		if(!"".equals(StringUtil.getString(entity.getRelationId()))){
			wrapper.like("relation_id", entity.getRelationId());
		}

		if(!"".equals(StringUtil.getString(entity.getRelationCode()))){
			wrapper.eq("relation_code", entity.getRelationCode());
		}

		if(!"".equals(StringUtil.getString(entity.getRelationName()))){
			wrapper.eq("relation_name", entity.getRelationName());
		}

		if(!"".equals(StringUtil.getString(entity.getClassId()))){
			wrapper.eq("class_id", entity.getClassId());
		}

		if(!"".equals(StringUtil.getString(entity.getClassCoded()))){
			wrapper.eq("classed_id", entity.getClassCoded());
		}

		if(!"".equals(StringUtil.getString(entity.getRelationTypeId()))){
			wrapper.eq("relation_type_id", entity.getRelationTypeId());
		}

		if(!"".equals(StringUtil.getString(entity.getDescribes()))){
			wrapper.eq("describes", entity.getDescribes());
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
		return metamodelRelationMapper.selectPage(rowBounds, wrapper);
	}
	
	@Override
	public MetamodelRelation selectMetamodelRelationById(String id){
		MetamodelRelation metamodelRelation = new MetamodelRelation();
		metamodelRelation = selectById(id);
		String classId = metamodelRelation.getClassId();
		MetamodelClass metamodelClass =metamodelClassMapper.selectById(classId);
		if (metamodelClass!=null) {
			metamodelRelation.setClassCode(metamodelClass.getClassCode());
			metamodelRelation.setClassName(metamodelClass.getClassName());
		}
		return metamodelRelation;
		
	}
	
	@Override
	public List<MetamodelRelation> queryByClassIdRelationInfo(String classId){
		try {
			EntityWrapper<MetamodelRelation> wrapper = new EntityWrapper<MetamodelRelation>();
			if (classId !=null || !"".equals(classId)) {
				wrapper.eq("class_id", classId);
			}
			return metamodelRelationMapper.selectList(wrapper);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	
	@Override
	public JSONObject delMetamodelRelationById(String id){
		JSONObject jsonObject = new JSONObject();
		try {
			if(id!=null){
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("relation_id", id);
				List<MetadataRelation> list = metadataRelationServiceImpl.selectList(map);
				if (list!=null && list.size()>0) {
					jsonObject.put("code", -1);
					jsonObject.put("msg","关联关系已被元数据使用，不允许删除");
					return jsonObject;
				}
				deleteById(id);
				jsonObject.put("code", 0);
				jsonObject.put("msg","删除成功");
			}else {
				jsonObject.put("code", -1);
				jsonObject.put("msg","请输入关联关系ID");
				return jsonObject;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", -2);
			jsonObject.put("msg","操作异常");
		}
		return jsonObject;
	}
}
