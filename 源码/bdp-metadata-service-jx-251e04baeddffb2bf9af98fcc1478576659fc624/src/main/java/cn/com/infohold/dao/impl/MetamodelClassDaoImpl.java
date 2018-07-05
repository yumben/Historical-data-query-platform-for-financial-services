package cn.com.infohold.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;

import cn.com.infohold.core.dao.impl.MyBatisDaoImpl;
import cn.com.infohold.dao.IMetamodelClassDao;
import cn.com.infohold.entity.Metadata;
import cn.com.infohold.entity.MetamodelClass;
import cn.com.infohold.entity.MetamodelClassgroup;
import cn.com.infohold.entity.MetamodelClassproperty;
import cn.com.infohold.entity.MetamodelPackage;
import cn.com.infohold.entity.MetamodelRelation;
import cn.com.infohold.mapper.MetadataMapper;
import cn.com.infohold.mapper.MetamodelClassMapper;
import cn.com.infohold.mapper.MetamodelClassgroupMapper;
import cn.com.infohold.mapper.MetamodelClasspropertyMapper;
import cn.com.infohold.mapper.MetamodelPackageMapper;
import cn.com.infohold.mapper.MetamodelRelationMapper;
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
@Log4j2(topic = "MetamodelClassDaoImpl")
@Service
public class MetamodelClassDaoImpl extends MyBatisDaoImpl<MetamodelClassMapper, MetamodelClass> implements IMetamodelClassDao {

	@Autowired
	MetamodelClassMapper metamodelClassMapper;
	@Autowired
	MetamodelPackageMapper metamodelPackageMapper;
	@Autowired
	MetamodelClasspropertyMapper metamodelClasspropertyMapper;
	@Autowired
	MetamodelClassgroupMapper metamodelClassgroupMapper;
	@Autowired
	MetamodelRelationMapper metamodelRelationMapper;
	@Autowired
	MetadataMapper metadataMapper;

	@Override
	public List<MetamodelClass> queryModelClassByParentId(String parentId, String isshow) throws Exception {
		List<MetamodelClass> list = new ArrayList<MetamodelClass>();
		EntityWrapper<MetamodelClass> entity = new EntityWrapper<MetamodelClass>();
		if (isshow != null && !isshow.equals("")) {
			entity.eq("isshow", isshow);
		}
		if (parentId != null && !parentId.equals("")) {
			entity.eq("package_id", parentId);
		} else {
			throw new Exception("模型包ID为空");
		}
		list = metamodelClassMapper.selectList(entity);
		return list;
	}

	@Override
	public List<MetamodelClass> queryModelClassByParentIdAndType(String parentId, String isshow, String type)
			throws Exception {
		List<MetamodelClass> list = new ArrayList<MetamodelClass>();
		EntityWrapper<MetamodelClass> entity = new EntityWrapper<MetamodelClass>();
		if (isshow != null && !isshow.equals("")) {
			entity.eq("isshow", isshow);
		}
		if (parentId != null && !parentId.equals("")) {
			entity.eq("package_id", parentId);
		} else {
			throw new Exception("模型包ID为空");
		}
		if (type != null && !type.equals("")) {
			entity.eq("type", type);
		}
		list = metamodelClassMapper.selectList(entity);
		return list;
	}

	@Override
	public String queryModelClassIdListById(String packageId, String classId) throws Exception {
		String contentPackageId = null;

		// 拼接元模型包id
		try {
			List<MetamodelPackage> list = metamodelPackageMapper.selectList(null);
			MetamodelPackage vo = queryVO(list, packageId);
			contentPackageId = vo.getPackageId().toString();
			while (Long.parseLong(vo.getParentId()) != 0) {
				vo = queryVO(list, vo.getParentId());
				contentPackageId = vo.getPackageId() + "/" + contentPackageId;
			}

		} catch (Exception e) {
			log.error("查询元模型包所属上下文目录出错:" + e.getMessage());
		}

		return contentPackageId;
	}

	private MetamodelPackage queryVO(List list, String packageId) {
		MetamodelPackage VO = new MetamodelPackage();
		for (int i = 0; i < list.size(); i++) {
			VO = (MetamodelPackage) list.get(i);
			if (VO.getPackageId().equals(packageId)) {
				break;
			}
		}
		return VO;
	}

	@Override
	public MetamodelClass queryModelClassByClassId(String classId) throws Exception {
		MetamodelClass classVo = new MetamodelClass();
		if (classId != null && !classId.equals("")) {
			classVo = metamodelClassMapper.queryModelClassByClassId(classId);
		}
		return classVo;
	}

	@Override
	public Page<MetamodelClassproperty> queryModelClassPropertyByClassId(String classId,
			Page<MetamodelClassproperty> page) throws Exception {
		if (classId != null && !classId.equals("")) {
			page.setRecords(metamodelClasspropertyMapper.queryModelClassPropertyByClassId(classId, page));
		} else {
			throw new Exception("模型类ID为空");
		}
		return page;
	}

	@Override
	public Page<MetamodelClassgroup> queryModelClassGroup(Map params, Page<MetamodelClassgroup> page) throws Exception {
		// 当查询条件不为空时
		if (params.get("groupedClassName") != null
				&& !params.get("groupedClassName").equals("")
				|| params.get("groupName") != null
				&& !params.get("groupName").equals("")) {
			
			if (params.get("classId") != null && !params.get("classId").equals("")) {
				page.setRecords(metamodelClassgroupMapper.queryModelClassGroup(params, page));
			} else {
				throw new Exception("模型类ID为空");
			}
		}else {
			if (params.get("classId") != null && !params.get("classId").equals("")) {
				page.setRecords(metamodelClassgroupMapper.queryModelClassGroupByClassId(params, page));
			} else {
				throw new Exception("模型类ID为空");
			}
		}
			


		/*else {
			String hql = "from MetamodelGroupVO m ,MetamodelClassVO c where m.classId=c.id";
			if (params.get("classId") != null && !params.get("classId").equals("")) {

				hql += " and m.classId = ?";
				Object[] params1 = new Object[1];
				params1[0] = Long.valueOf((String) params.get("classId"));
				if (params.get("groupedClassName") != null && !params.get("groupedClassName").equals("")) {
					hql += " and (c.code like " + "'" + params.get("groupedClassName") + "' or c.name like " + "'"
							+ params.get("groupedClassName") + "')";
				} else if (params.get("groupName") != null && !params.get("groupName").equals("")) {
					hql += " and (m.code like " + "'" + params.get("groupName") + "' or m.name like " + "'"
							+ params.get("groupName") + "')";
				}
				hql += " order by m.id desc";
				int count = (int) dao.findTotalNumOfRecord(hql, params1, false);
				List list2 = dao.findByHql(hql, params1, pageIndex, pageSize);
				for (int i = 0; i < list2.size(); i++) {
					Object[] obj = (Object[]) list2.get(i);
					MetamodelGroupVO meGroupVO = (MetamodelGroupVO) obj[0];
					MetamodelClassVO meClassVo = (MetamodelClassVO) obj[1];
					meGroupVO.setClassCode(meClassVo.getCode());
					meGroupVO.setClassName(meClassVo.getName());

					GroupList.add(meGroupVO);
				}
				result.setResultList(GroupList);
				result.setTotalCount(count);
				result.setTotalPage(NumberUtil.getTotalPage(count, pageSize));

			} else {
				throw new Exception("模型类ID为空");
			}
		}
		 */
		return page;
	}

	@Override
	public Page<MetamodelClassgroup> queryModelClassGrouped(Map params, Page<MetamodelClassgroup> page)
			throws Exception {
		page.setRecords(metamodelClassgroupMapper.queryModelClassGrouped(params, page));
		return page;
	}

	@Override
	public Page<MetamodelRelation> queryModelClassRelation(Map params, Page<MetamodelRelation> page) throws Exception {
		if (params.get("classId") != null && !params.get("classId").equals("")) {
			page.setRecords(metamodelRelationMapper.queryModelClassRelation(params, page));
		} else {
			throw new Exception("模型类ID为空");
		}
		return page;
	}

	@Override
	public Page<MetamodelClass> queryModelClass(Map params, Page<MetamodelClass> page) throws Exception {
		String packageId = (String)params.get("packageId");
		String classname = (String)params.get("classname");
		page.setRecords(metamodelClassMapper.queryModelClass(packageId,classname, page));
		return page;
	}

	@Override
	public List<MetamodelClass> selectMetamodelClassList(MetamodelClass entity, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		EntityWrapper<MetamodelClass> wrapper = new EntityWrapper<MetamodelClass>();
		if(!"".equals(StringUtil.getString(entity.getClassId()))){
			wrapper.like("class_id", entity.getClassId());
		}

		if(!"".equals(StringUtil.getString(entity.getClassCode()))){
			wrapper.eq("class_code", entity.getClassCode());
		}

		if(!"".equals(StringUtil.getString(entity.getClassName()))){
			wrapper.eq("class_name", entity.getClassName());
		}

		if(!"".equals(StringUtil.getString(entity.getDescribes()))){
			wrapper.eq("describes", entity.getDescribes());
		}

		if(!"".equals(StringUtil.getString(entity.getType()))){
			wrapper.eq("type", entity.getType());
		}

		if(!"".equals(StringUtil.getString(entity.getEditDate()))){
			wrapper.eq("edit_date", entity.getEditDate());
		}

		if(!"".equals(StringUtil.getString(entity.getEditName()))){
			wrapper.eq("edit_name", entity.getEditName());
		}

		if(!"".equals(StringUtil.getString(entity.getIsadd()))){
			wrapper.eq("isadd", entity.getIsadd());
		}
		if(!"".equals(StringUtil.getString(entity.getIsshow()))){
			wrapper.eq("isshow", entity.getIsshow());
		}
		if(!"".equals(StringUtil.getString(entity.getDisplayIcon()))){
			wrapper.eq("display_icon", entity.getDisplayIcon());
		}
		if(!"".equals(StringUtil.getString(entity.getPackageId()))){
			wrapper.eq("package_id", entity.getPackageId());
		}
		pageNo = CommonUtil.offsetCurrent(pageNo, pageSize);
		RowBounds rowBounds = new RowBounds(pageNo,pageSize);
		return metamodelClassMapper.selectPage(rowBounds, wrapper);
	}
	
	
	
	public MetamodelClass selectMetamodelClass(String classCode){
		
		EntityWrapper<MetamodelClass> wrapper = new EntityWrapper<MetamodelClass>();
		if(!"".equals(StringUtil.getString(classCode))){
			wrapper.like("class_code", classCode);
		}
		List<MetamodelClass> modelList = metamodelClassMapper.selectList(wrapper);
		if (modelList !=null && modelList.size()>0) {
			return modelList.get(0);
		}else {
			return null;
		}
	}
	public MetamodelClass selectMetamodelClassByClassId(String classId){
			
			EntityWrapper<MetamodelClass> wrapper = new EntityWrapper<MetamodelClass>();
			if(!"".equals(StringUtil.getString(classId))){
				wrapper.like("class_id", classId);
			}
			List<MetamodelClass> modelList = metamodelClassMapper.selectList(wrapper);
			if (modelList !=null && modelList.size()>0) {
				return modelList.get(0);
			}else {
				return null;
			}
		}
	
	@Override
	public List<MetamodelClass> queryMetamodelByMetadataId(String metadataId){
		Metadata metadata = metadataMapper.selectById(metadataId);
		if (metadata !=null) {
			return metamodelClassMapper.selectMetamodelClassByGroupClassId(metadata.getClassId());
		}
		return null;
		
	}
	@Override
	public List<MetamodelClass> queryByCodeAndNotId(String code,String id , String packageId){
		EntityWrapper<MetamodelClass> wrapper = new EntityWrapper<MetamodelClass>();
		if (!"".equals(StringUtil.getString(code))) {
			wrapper.eq("class_code", code);
		}
		if (!"".equals(StringUtil.getString(code))) {
			wrapper.notIn("class_id", id);
		}
		if (!"".equals(StringUtil.getString(code))) {
			wrapper.eq("package_id", packageId);
		}
		return metamodelClassMapper.selectList(wrapper);
		
	}
	
	@Override
	public  MetamodelClass queryByClassGroupedId(String classGroupedId){
		EntityWrapper<MetamodelClass> wrapper = new EntityWrapper<MetamodelClass>();
		
		if (!"".equals(StringUtil.getString(classGroupedId))) {
			wrapper.notIn("class_id", classGroupedId);
			List<MetamodelClass> list =  metamodelClassMapper.selectList(wrapper);
			return list.get(0);
		}
		return null;
		
	}
	
	
}
