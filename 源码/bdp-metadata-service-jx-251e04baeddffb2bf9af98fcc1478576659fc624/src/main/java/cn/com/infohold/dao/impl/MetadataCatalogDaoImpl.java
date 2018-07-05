package cn.com.infohold.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;

import cn.com.infohold.core.dao.impl.MyBatisDaoImpl;
import cn.com.infohold.dao.IMetadataCatalogDao;
import cn.com.infohold.dao.IMetadataDao;
import cn.com.infohold.entity.Metadata;
import cn.com.infohold.entity.MetadataCatalog;
import cn.com.infohold.mapper.MetadataCatalogMapper;
import cn.com.infohold.mapper.MetadataMapper;
import cn.com.infohold.tools.util.CommonUtil;
import cn.com.infohold.tools.util.StringUtil;
import lombok.extern.log4j.Log4j2;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@Log4j2
@Service
public class MetadataCatalogDaoImpl extends MyBatisDaoImpl<MetadataCatalogMapper, MetadataCatalog> implements IMetadataCatalogDao {
	@Autowired
	MetadataCatalogMapper metadataCatalogMapper;
	@Autowired
	IMetadataDao metadataServiceImpl; 
	@Autowired
	MetadataMapper metadataMapper;
	@Override
	public List<MetadataCatalog> selectMetadataCatalogList(MetadataCatalog entity, int pageNo, int pageSize){
		// TODO Auto-generated method stub
		EntityWrapper<MetadataCatalog> wrapper = new EntityWrapper<MetadataCatalog>();
		if(!"".equals(StringUtil.getString(entity.getCatalogCode()))){
			wrapper.like("catalog_code", entity.getCatalogCode());
		}

		if(!"".equals(StringUtil.getString(entity.getCatalogId()))){
			wrapper.eq("catalog_id", entity.getCatalogId());
		}

		if(!"".equals(StringUtil.getString(entity.getCatalogName()))){
			wrapper.eq("catalog_name", entity.getCatalogName());
		}

		if(!"".equals(StringUtil.getString(entity.getCatalogType()))){
			wrapper.eq("catalog_type", entity.getCatalogType());
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

		if(!"".equals(StringUtil.getString(entity.getParentCatalog()))){
			wrapper.eq("parent_catalog", entity.getParentCatalog());
		}
		pageNo = CommonUtil.offsetCurrent(pageNo, pageSize);
		RowBounds rowBounds = new RowBounds(pageNo,pageSize);
		return metadataCatalogMapper.selectPage(rowBounds, wrapper);
	}
	
	@Override
	public List<MetadataCatalog> queryMetaDataCatalogByParentIdAndType(String parentId, String classType){
		EntityWrapper<MetadataCatalog> wrapper = new EntityWrapper<MetadataCatalog>();
		if(parentId != null && !parentId.equals("")){
			wrapper.eq("parent_catalog", parentId);
		
			if (classType != null && !classType.equals("")) {
				
				wrapper.eq("catalog_type", classType);
			}
			
		}else{
			wrapper.isNull("parent_catalog");
			wrapper.or();
			wrapper.eq("parent_catalog", "0");
			if (classType != null && !classType.equals("")) {
				wrapper.eq("catalog_type", classType);
			}
		}
		
		return metadataCatalogMapper.selectList(wrapper);
	}
	
	@Override
	public String queryContextCatalog(String catalogId){
		List<MetadataCatalog> list = metadataCatalogMapper.selectList(null);
		String contextCatalog = "";
		MetadataCatalog metadataCatalog = queryVO(list,catalogId);
		contextCatalog = metadataCatalog.getCatalogName();
		while(metadataCatalog.getParentCatalog()!=null){
			metadataCatalog = queryVO(list, metadataCatalog.getParentCatalog());
			contextCatalog = metadataCatalog.getCatalogName() + "/"+contextCatalog;
		}
		return contextCatalog;
	}
	
	private MetadataCatalog queryVO(List<MetadataCatalog> list, String catalogId){
		MetadataCatalog VO = new MetadataCatalog();
		for(int i=0; i<list.size(); i++){
			VO = (MetadataCatalog) list.get(i);
			if(VO.getCatalogId().equals(catalogId)){
				break;
			}
		}
		return VO;	
	}
	
	@Override
	public List<String> queryContextCatalog(String catalogId, String metadataId){
		String contextCatalog = "";
		String contextCatalog1 = "";
		String contextCode = "";
		String contextCode1 = "";
		String contextType = "";
		String contextType1 = "";
		String contextCatalogId = "";
		String contextmetadataId = "";
		String contextClassId = "";
		List<String> lists = new ArrayList<String>();
		
		List list = metadataCatalogMapper.selectList(null);
		MetadataCatalog VO = queryVO(list, catalogId);
		contextCatalog = VO.getCatalogName();
		contextCode = VO.getCatalogCode();
		contextType = "目录";
		contextCatalogId = VO.getCatalogId().toString();
		while (VO.getParentCatalog() != null && !"0".equals(VO.getParentCatalog())) {
			VO = queryVO(list, VO.getParentCatalog());

			contextCatalog = VO.getCatalogName() + "/" + contextCatalog;
			contextCode = VO.getCatalogCode() + "/" + contextCode;
			contextType = "目录" + "/" + contextType;
			contextCatalogId = VO.getCatalogId() + "/" + contextCatalogId;
		}

		if (metadataId != null) {
			EntityWrapper<Metadata> wrapper = new EntityWrapper<Metadata>();
			wrapper.eq("catalog_id", catalogId);
			List list1 = metadataMapper.selectList(wrapper);
			Metadata metadataVO = metadataServiceImpl.queryMetadataVO(list1, metadataId);
			if (list1 !=null && list1.size()>0 && metadataVO!=null) {
				contextCatalog1 = metadataVO.getMetadataName();
				contextCode1 = metadataVO.getMetadataCode();
				Metadata conMetadata = metadataMapper.queryMetadataById(metadataVO.getMetadataId());
				if (conMetadata!=null) {
					contextType1 = conMetadata.getClassName();
				}
				contextmetadataId = metadataVO.getMetadataId()+"";
				contextClassId = metadataVO.getClassId()+"";
				while (metadataVO.getParentMetadata() != null) {
					metadataVO = metadataServiceImpl.queryMetadataVO(list1, metadataVO.getParentMetadata());
					contextCatalog1 = metadataVO.getMetadataName() + "/" + contextCatalog1;
					contextCode1 = metadataVO.getMetadataCode() + "/" + contextCode1;
					conMetadata = metadataMapper.queryMetadataById(metadataVO.getMetadataId());
					if (conMetadata!=null) {
						contextType1 = conMetadata.getClassName() + "/"
								+ contextType1;
					}
					
					contextmetadataId = metadataVO.getMetadataId() + "/" + contextmetadataId;
					contextClassId = metadataVO.getClassId() + "/" + contextClassId;
				}
			}
			
		}

		contextCatalog = "/" + contextCatalog;
		contextCatalog = contextCatalog + "/" + contextCatalog1;
		contextCode = "/" + contextCode;
		contextCode = contextCode + "/" + contextCode1;
		contextType = "/" + contextType;
		contextType = contextType + "/" + contextType1;
		lists.add(contextCatalog);
		lists.add(contextCode);
		lists.add(contextType);
		lists.add(contextCatalogId);
		lists.add(contextmetadataId);
		lists.add(contextClassId);
		return lists;
	}
	
	@Override
	public MetadataCatalog selectMetadataCatalogByCatalogId(String catalogId){
		if (catalogId!=null) {
			MetadataCatalog metadataCatalog = selectById(catalogId);
			if (metadataCatalog!=null) {
				metadataCatalog.setContextCatalog(queryContextCatalog(catalogId));
			}
			return metadataCatalog;
		}
		return null;
		
	}
	
	@Override
	public Map<String,String> queryContextCatalogMap(String metadataIdStr){
	     String contextCatalog="";
	     String contextCatalog1 = "";
	     Map<String,String> retMap=new HashMap<String, String>();
	     Map<String,String> catalogMap=new HashMap<String, String>();
	     try {
	    	 
			List<MetadataCatalog> list = metadataCatalogMapper.selectList(null);
			for (int i = 0; i < list.size(); i++) {
				MetadataCatalog VO = (MetadataCatalog)list.get(i);
				String catalogId = VO.getCatalogId();
				contextCatalog = "/"+VO.getCatalogName();
				while (VO.getParentCatalog() !=null && VO.getParentCatalog().length()>0) {
					VO = queryVO(list, VO.getParentCatalog());
					contextCatalog = "/"+VO.getCatalogName() + contextCatalog;
				}
				catalogMap.put(catalogId, contextCatalog);
				
			}
			if (metadataIdStr != null) {
				List<Metadata> list1 = metadataMapper.selectList(null);
				String[] metadataIdArr = metadataIdStr.split(",");
				Map<String, String> existMap = new HashMap<String, String>();
				for (int j = 0; j < metadataIdArr.length; j++) {
					contextCatalog1 = "";
					Metadata metadataVO = metadataServiceImpl.queryMetadataVO(list1, metadataIdArr[j]);
					String metadataId = metadataVO.getMetadataId();
					if (existMap != null && existMap.get(metadataId) != null) {
						continue;
					}
					existMap.put(metadataId, metadataVO.getMetadataName());
					contextCatalog1 = "/" + metadataVO.getMetadataName();
					while (metadataVO.getParentMetadata() != null && metadataVO.getParentMetadata().length() > 0) {
						metadataVO = metadataServiceImpl.queryMetadataVO(list1, metadataVO.getParentMetadata());
						contextCatalog1 = "/" + metadataVO.getMetadataName() + contextCatalog1;
					}
					retMap.put(metadataId, catalogMap.get(metadataVO.getCatalogId()) + contextCatalog1);
					// System.out.println("####"+metadataId+"="+catalogMap.get(metadataVO.getCatalogId())+contextCatalog1);
				}

			}
			
		} catch (Exception e) {
                    //(ex);
			log.error("查询元数据所属上下文目录出错:" + e.getMessage());
			e.printStackTrace();
			
		}
		return retMap;
	}
	
	@Override
	public JSONObject delMetadataCatalogByCatalogId(String catalogId){
		JSONObject jsonObject = new JSONObject();
		try {
			if (catalogId != null ) {
				MetadataCatalog metadataCatalog = selectById(catalogId);
				if (metadataCatalog!=null) {
					EntityWrapper<Metadata> wrapper = new EntityWrapper<Metadata>();
					wrapper.eq("catalog_id", catalogId);
					List<Metadata> metadatas = metadataMapper.selectList(wrapper);
					if (metadatas == null || metadatas.size()==0) {
						EntityWrapper<MetadataCatalog> wr = new EntityWrapper<MetadataCatalog>();
						wr.eq("parent_catalog", catalogId);
						List<MetadataCatalog> MetadataCatalogs = metadataCatalogMapper.selectList(wr);
						if (MetadataCatalogs.size()==0) {
							deleteById(catalogId);
							jsonObject.put("code", 0);
							jsonObject.put("msg", "删除成功");
						}else {
							jsonObject.put("code", -1);
							jsonObject.put("msg", "该目录有下级目录，不允许删除");
						}
					}else {
						jsonObject.put("code", -1);
						jsonObject.put("msg", "该目录有下级元数据，不允许删除");
					}
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return jsonObject;
	}
	
	@Override
	public List<MetadataCatalog> queryCatalogBy(String classId){
		try {
			if (classId !=null && !"".equals(classId)) {
				return metadataCatalogMapper.selectCatalogByClassId(classId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return null;
		
	}
}
