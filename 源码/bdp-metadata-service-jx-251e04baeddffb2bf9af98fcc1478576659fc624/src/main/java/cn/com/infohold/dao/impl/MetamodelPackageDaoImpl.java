package cn.com.infohold.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;

import cn.com.infohold.core.dao.impl.MyBatisDaoImpl;
import cn.com.infohold.dao.IMetamodelPackageDao;
import cn.com.infohold.entity.MetamodelPackage;
import cn.com.infohold.mapper.MetamodelPackageMapper;
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
public class MetamodelPackageDaoImpl extends MyBatisDaoImpl<MetamodelPackageMapper, MetamodelPackage> implements IMetamodelPackageDao {
	
	@Autowired
	MetamodelPackageMapper metamodelPackageMapper;
	
	@Override
	public List<MetamodelPackage> selectMetamodelPackageList(MetamodelPackage entity, int pageNo, int pageSize){
		// TODO Auto-generated method stub
		EntityWrapper<MetamodelPackage> wrapper = new EntityWrapper<MetamodelPackage>();
		if(!"".equals(StringUtil.getString(entity.getPackageId()))){
			wrapper.like("package_id", entity.getPackageId());
		}

		if(!"".equals(StringUtil.getString(entity.getPackageCode()))){
			wrapper.eq("package_code", entity.getPackageCode());
		}

		if(!"".equals(StringUtil.getString(entity.getPackageName()))){
			wrapper.eq("package_name", entity.getPackageName());
		}

		if(!"".equals(StringUtil.getString(entity.getDescribes()))){
			wrapper.eq("describes", entity.getDescribes());
		}

		if(!"".equals(StringUtil.getString(entity.getParentId()))){
			wrapper.eq("parent_id", entity.getParentId());
		}

		if(!"".equals(StringUtil.getString(entity.getPackageType()))){
			wrapper.eq("type", entity.getPackageType());
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
		return metamodelPackageMapper.selectPage(rowBounds, wrapper);
	}
	
	@Override
	public List<MetamodelPackage> queryModelPackageByParentIdAndType(String parentId, String type) throws Exception {
		List<MetamodelPackage> list = new ArrayList<MetamodelPackage>();
		EntityWrapper<MetamodelPackage> entity = new EntityWrapper<MetamodelPackage>();
		if(parentId != null && !parentId.equals("")){
			entity.eq("parent_id", parentId);
			if (type != null && !type.equals("")) {
				entity.eq("type", type);
			}
		}else{
			entity.isNull("parent_id");
			if (type != null && !type.equals("")) {
				entity.eq("type", type);
			}
		}
		list = metamodelPackageMapper.selectList(entity);
		return list;
	}

	@Override
	public List<MetamodelPackage> queryModelPackageByParentId(String parentId) throws Exception {
		List<MetamodelPackage> list = new ArrayList<MetamodelPackage>();
		EntityWrapper<MetamodelPackage> entity = new EntityWrapper<MetamodelPackage>();
		if(parentId != null && !parentId.equals("")){
			entity.eq("parent_id", parentId);
		}else{
			entity.isNull("parent_id");
		}
		list = metamodelPackageMapper.selectList(entity);
		return list;
	}
	
	@Override
	public JSONObject queryMetamodelPackageBysql(Map param,int pageNo,int  pageSize){
		List<MetamodelPackage> list4=new ArrayList<MetamodelPackage>();
		JSONObject jsonObject = new JSONObject();
		String packageId=(String)param.get("packageId");
		String packageName=(String)param.get("packageName");
		
		list4=metamodelPackageMapper.selectList(null);
		List<String> idlist = new ArrayList<String>();
		idlist.add(packageId);
		if (!"null".equals(packageId) && !"".equals(packageId)) {
			idlist = queryBySubPackageIdList(packageId,idlist);
		}
		//List list2=dao.findBySql(sql, params);
		if (packageName!=null && !"".equals(packageName)) {
			packageName="%"+packageName+"%";
		}
		Page<MetamodelPackage> page = new Page<MetamodelPackage>(pageNo,pageSize);
		List<MetamodelPackage> list = metamodelPackageMapper.queryModelePackageByIdAndPackeName(page,idlist,packageName);
		if (list!= null && list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < list4.size(); j++) {
					if (list.get(i).getParentId() == list4.get(j).getPackageId()) {
						list.get(i).setParentName(list4.get(j).getPackageName());
					}
				}
			}
			
		}
		jsonObject.put("totalCount", page.getTotal());
		jsonObject.put("totalPage", page.getPages());
		jsonObject.put("list", list);
		return jsonObject;	
}
	
	
	List<String>  queryBySubPackageIdList(String packageId ,List<String> list){
		EntityWrapper<MetamodelPackage> wrapper = new EntityWrapper<MetamodelPackage>();
		wrapper.eq("parent_id", packageId);
		List<MetamodelPackage> list2 = metamodelPackageMapper.selectList(wrapper);
		if (list2!= null && list2.size()>0) {
			for (int  i = 0;  i < list2.size();  i++) {
				String parent_id = list2.get(i).getPackageId();
				list.add(parent_id);
				list = queryBySubPackageIdList(parent_id, list);
			}
		}
		return list;
	}
	
}
