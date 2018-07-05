package cn.com.infohold.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import cn.com.infohold.core.dao.impl.MyBatisDaoImpl;
import cn.com.infohold.dao.IAppparDao;
import cn.com.infohold.dao.IMetadataRelationDao;
import cn.com.infohold.entity.MetadataRelation;
import cn.com.infohold.entity.MetamodelRelation;
import cn.com.infohold.mapper.MetadataRelationMapper;
import cn.com.infohold.mapper.MetamodelRelationMapper;
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
public class MetadataRelationDaoImpl extends MyBatisDaoImpl<MetadataRelationMapper, MetadataRelation> implements IMetadataRelationDao {
	@Autowired
	MetadataRelationMapper metadataRelationMapper;
	@Autowired
	MetamodelRelationMapper metamodelRelationMapper;
	@Autowired
	IAppparDao appparServiceImpl;
	@Override
	public List<MetadataRelation> selectMetadataRelationList(MetadataRelation entity, int pageNo, int pageSize){
		// TODO Auto-generated method stub
		EntityWrapper<MetadataRelation> wrapper = new EntityWrapper<MetadataRelation>();
		if(!"".equals(StringUtil.getString(entity.getMetadataRelationId()))){
			wrapper.like("metadata_relation_id", entity.getMetadataRelationId());
		}

		if(!"".equals(StringUtil.getString(entity.getMetadataId()))){
			wrapper.eq("metadata_id", entity.getMetadataId());
		}

		if(!"".equals(StringUtil.getString(entity.getMetadataRelationed()))){
			wrapper.eq("metadata_relationed", entity.getMetadataRelationed());
		}

		if(!"".equals(StringUtil.getString(entity.getConditionId()))){
			wrapper.eq("condition_id", entity.getConditionId());
		}

		if(!"".equals(StringUtil.getString(entity.getRelationId()))){
			wrapper.eq("relation_id", entity.getRelationId());
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
		return metadataRelationMapper.selectPage(rowBounds, wrapper);
	}
	
	@Override
	public boolean addMetaDataRelation(String metadataId ,String[] metadataedId,String conditionId,String relationId){
		if (metadataedId != null && metadataedId.length > 0) {
			for(int i=0; i<metadataedId.length; i++){
				
				MetamodelRelation relationVO = metamodelRelationMapper.selectById(relationId);
				
				Map<String, String> appparMap= appparServiceImpl.selectAppparList("metadata_relation");
				
				String createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				
				boolean flag = false;
				if (appparMap.get(relationVO.getRelationCode())!=null) {
					flag = true;
				}
				
				if(flag){
					MetadataRelation vo = new MetadataRelation();
					MetadataRelation voed = new MetadataRelation();
					
					vo.setMetadataId(metadataId);
					voed.setMetadataId(metadataedId[i]);
					
					vo.setMetadataRelationed(metadataedId[i]);
					voed.setMetadataRelationed(metadataId);
					
					if(conditionId!=null&&!conditionId.equals("")){
						vo.setConditionId(conditionId);
						voed.setConditionId(conditionId);
					}
					vo.setRelationId(relationId);
					voed.setRelationId(relationId);
					vo.setCreateDate(createDate);
					voed.setCreateDate(createDate);
					insert(vo);
					insert(voed);
				}else {
					MetadataRelation vo = new MetadataRelation();
					vo.setMetadataId(metadataId);
					if(conditionId!=null&&!conditionId.equals("")){
						vo.setConditionId(conditionId);
					}
					vo.setRelationId(relationId);
					vo.setCreateDate(createDate);
					vo.setMetadataRelationed(metadataedId[i]);
					insert(vo);
				}
				
			}
		}
		
		return true;
		
	}
	@Override
	public boolean delMetadataRelation(String id){
		MetadataRelation vo = new MetadataRelation();
		if(id!=null){
			vo = selectById(id);
			MetamodelRelation relationVO = metamodelRelationMapper.selectById(vo.getRelationId()+"");
			
			Map<String, String> appparMap= appparServiceImpl.selectAppparList("metadata_relation");
			
			
			boolean flag = false;
			if (appparMap.get(relationVO.getRelationCode())!=null) {
				flag = true;
			}
			
			if (flag) {
				EntityWrapper<MetadataRelation> wrapper = new EntityWrapper<MetadataRelation>();
				wrapper.eq("metadata_relationed", vo.getMetadataRelationed()+"");
				wrapper.eq("metadata_id", vo.getMetadataId()+"");
				wrapper.eq("relation_id", vo.getRelationId()+"");
				metadataRelationMapper.delete(wrapper);
				deleteById(vo.getMetadataRelationId());
				
			}else {
				deleteById(vo.getMetadataRelationId());
			}
		}
		deleteById(id);
		return true;
		
	}
	
	@Override
	public List<MetadataRelation> queryMetadataRelationshipBymetadataId(String metadataId){
		EntityWrapper<MetadataRelation> wrapper = new EntityWrapper<MetadataRelation>();
		if(metadataId != null && !"".equals(metadataId)){
			wrapper.eq("metadata_id", metadataId);
			wrapper.or();
			wrapper.eq("metadata_relationed", metadataId);
		}
		return metadataRelationMapper.selectList(wrapper);
	}
	
	@Override
	public boolean delMetadataRalation(MetadataRelation metadataRelation){
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			
			if(!"".equals(StringUtil.getString(metadataRelation.getMetadataId()))){
				map.put("metadata_id",metadataRelation.getMetadataRelationed() );
			}

			if(!"".equals(StringUtil.getString(metadataRelation.getMetadataRelationed()))){
				map.put("metadata_relationed", metadataRelation.getMetadataId());
			}

			if(!"".equals(StringUtil.getString(metadataRelation.getRelationId()))){
				map.put("relation_id", metadataRelation.getRelationId());
			}

			metadataRelationMapper.deleteByMap(map);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Override
	public List<MetadataRelation> queryRelationByIdAndRelationClassId(String metadataId, String relationId){
		try {
			EntityWrapper<MetadataRelation> wrapper = new EntityWrapper<MetadataRelation>();
			if (!"".equals(StringUtil.getString(metadataId))) {
				wrapper.eq("metadata_id", metadataId);
			}
			if (!"".equals(StringUtil.getString(relationId))) {
				wrapper.eq("relation_id", relationId);			
			}
			return metadataRelationMapper.selectList(wrapper);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
