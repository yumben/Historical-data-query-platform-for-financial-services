package cn.com.infohold.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;

import cn.com.infohold.core.dao.impl.MyBatisDaoImpl;
import cn.com.infohold.dao.IMetamodelDatatypeDao;
import cn.com.infohold.entity.MetamodelDatatype;
import cn.com.infohold.mapper.MetamodelDatatypeMapper;
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
public class MetamodelDatatypeDaoImpl extends MyBatisDaoImpl<MetamodelDatatypeMapper, MetamodelDatatype> implements IMetamodelDatatypeDao {
	@Autowired
	MetamodelDatatypeMapper metamodelDatatypeMapper;
	@Override
	public List<MetamodelDatatype> selectMetamodelDatatypeList(MetamodelDatatype entity, int pageNo, int pageSize){
		// TODO Auto-generated method stub
		EntityWrapper<MetamodelDatatype> wrapper = new EntityWrapper<MetamodelDatatype>();
		if(!"".equals(StringUtil.getString(entity.getDatatypeId()))){
			wrapper.like("datatype_id", entity.getDatatypeId());
		}

		if(!"".equals(StringUtil.getString(entity.getPackageId()))){
			wrapper.eq("package_id", entity.getPackageId());
		}

		if(!"".equals(StringUtil.getString(entity.getDatatypeCode()))){
			wrapper.eq("datatype_code", entity.getDatatypeCode());
		}

		if(!"".equals(StringUtil.getString(entity.getDatatypeName()))){
			wrapper.eq("datatype_name", entity.getDatatypeName());
		}

		if(!"".equals(StringUtil.getString(entity.getDatatypeDescribe()))){
			wrapper.eq("datatype_describe", entity.getDatatypeDescribe());
		}
		if(!"".equals(StringUtil.getString(entity.getVisibility()))){
			wrapper.eq("visibility", entity.getVisibility());
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
		return metamodelDatatypeMapper.selectPage(rowBounds, wrapper);
	}
	
	@Override
	public JSONObject selectMetamodelDatatypeByIdAndCode(Map<String,String> param ,int pageNo,int pageSize){
		JSONObject jsonObject = new JSONObject();
		try {
			Page<MetamodelDatatype> page = new Page<MetamodelDatatype>(pageNo,pageSize);
			String packageId = param.get("packageId");
			String datatypeName = param.get("datatypeName");
			if (datatypeName!=null) {
				datatypeName = "%"+datatypeName+"%";
			}
			List<MetamodelDatatype> list = metamodelDatatypeMapper.selectMetamodelDatatypeByIdAndCode(page, packageId, datatypeName);
			page.setRecords(list);
			jsonObject.put("list", list);
			jsonObject.put("totalCount", page.getTotal());
			jsonObject.put("totalPage", page.getPages());
			jsonObject.put("code", 0);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			jsonObject.put("code", -2);
		}
		return jsonObject;
		
	}
	
	@Override
	public List<MetamodelDatatype> selectMetamodelDatatypeListByPackageId(String packageId){
		EntityWrapper<MetamodelDatatype> wrapper = new EntityWrapper<MetamodelDatatype>();
		if (packageId !=null && !"".equals(packageId)) {
			wrapper.eq("package_id", packageId);
		}
		return metamodelDatatypeMapper.selectList(wrapper);
	}
}
