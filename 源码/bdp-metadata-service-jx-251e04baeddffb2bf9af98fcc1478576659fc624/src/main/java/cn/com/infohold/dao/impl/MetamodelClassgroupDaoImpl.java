package cn.com.infohold.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;

import cn.com.infohold.core.dao.impl.MyBatisDaoImpl;
import cn.com.infohold.dao.IMetamodelClassgroupDao;
import cn.com.infohold.entity.Metadata;
import cn.com.infohold.entity.MetamodelClassgroup;
import cn.com.infohold.mapper.MetadataMapper;
import cn.com.infohold.mapper.MetamodelClassgroupMapper;
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
public class MetamodelClassgroupDaoImpl extends MyBatisDaoImpl<MetamodelClassgroupMapper, MetamodelClassgroup> implements IMetamodelClassgroupDao {
	@Autowired
	MetamodelClassgroupMapper metamodelClassgroupMapper;
	@Autowired
	MetadataMapper metadataMapper;
	@Override
	public List<MetamodelClassgroup> selectMetamodelClassgroupList(MetamodelClassgroup entity, int pageNo, int pageSize){
		// TODO Auto-generated method stub
		EntityWrapper<MetamodelClassgroup> wrapper = new EntityWrapper<MetamodelClassgroup>();
		if(!"".equals(StringUtil.getString(entity.getClassgroupId()))){
			wrapper.like("classgroup_id", entity.getClassgroupId());
		}

		if(!"".equals(StringUtil.getString(entity.getClassId()))){
			wrapper.eq("class_id", entity.getClassId());
		}

		if(!"".equals(StringUtil.getString(entity.getRelationshipCode()))){
			wrapper.eq("relationship_code", entity.getRelationshipCode());
		}

		if(!"".equals(StringUtil.getString(entity.getRelationshipName()))){
			wrapper.eq("relationship_name", entity.getRelationshipName());
		}
		if(!"".equals(StringUtil.getString(entity.getClassgroupedId()))){
			wrapper.eq("classgrouped_id", entity.getClassgroupedId());
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
		return metamodelClassgroupMapper.selectPage(rowBounds, wrapper);
	}
	@Override
	public List<MetamodelClassgroup> selectMetamodelClassgroupListByMap(Map<String, Object> map){
		try {
			if (map !=null) {
				return metamodelClassgroupMapper.selectByMap(map);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public JSONObject delMetamodelClassgroupById(String id){
		JSONObject jsonObject = new JSONObject();
		try {
			MetamodelClassgroup metamodelClassgroup = selectById(id);
			if (metamodelClassgroup !=null) {
				String classId = metamodelClassgroup.getClassId();
				String classgroupId = metamodelClassgroup.getClassgroupedId();
				EntityWrapper<Metadata> entityWrapper = new EntityWrapper<Metadata>();
				entityWrapper.eq("class_id", classgroupId);
				List<Metadata> plist = metadataMapper.selectList(entityWrapper);
				List<String> idList = new ArrayList<String>();
				if (plist!=null && plist.size()>0) {
					for (int i = 0; i < plist.size(); i++) {
						idList.add(plist.get(i).getMetadataId());
					}
				}
				List<Metadata> list = new ArrayList<Metadata>();
				if (idList != null && idList.size()>0) {
					entityWrapper = new EntityWrapper<Metadata>();
					entityWrapper.in("metadata_id", idList);
					entityWrapper.andNew("class_id='"+classId+"'").or("class_id='"+classgroupId+"'");
					list = metadataMapper.selectList(entityWrapper);
					//list = metadataMapper.selectMetadataByClassIDAndClassgroupedId(classId,classgroupId);
					
				}
				if (list!=null && list.size()>0) {
					jsonObject.put("code", -1);
					jsonObject.put("msg", "被组合类已被元数据使用，不允许删除");
					return jsonObject;
				}
				deleteById(id);
				jsonObject.put("code", 0);
				jsonObject.put("msg", "删除成功");
			}
		} catch (Exception e) {
			jsonObject.put("code", -2);
			jsonObject.put("msg", "操作异常");
		}
		
		return jsonObject;
	}
}
