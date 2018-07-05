package cn.com.infohold.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;

import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.commons.AppConstants;
import cn.com.infohold.core.controller.BaseController;
import cn.com.infohold.entity.MetamodelClass;
import cn.com.infohold.entity.MetamodelClassgroup;
import cn.com.infohold.entity.MetamodelClassproperty;
import cn.com.infohold.entity.MetamodelPackage;
import cn.com.infohold.entity.MetamodelRelation;
import cn.com.infohold.entity.MetamodelRelationType;
import cn.com.infohold.service.IMetamodelClassService;
import cn.com.infohold.service.IMetamodelClassgroupService;
import cn.com.infohold.service.IMetamodelClasspropertyService;
import cn.com.infohold.service.IMetamodelPackageService;
import cn.com.infohold.service.IMetamodelRelationService;
import cn.com.infohold.service.IMetamodelRelationTypeService;
import cn.com.infohold.tools.util.DateUtil;
import cn.com.infohold.tools.util.StringUtil;
import lombok.extern.log4j.Log4j2;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@Log4j2(topic = "MetamodelClassController")
@RestController
@RequestMapping("/metamodelClass")
public class MetamodelClassController extends BaseController {
	@Autowired
	IMetamodelClassService metamodelClassServiceImpl;
	@Autowired
	IMetamodelPackageService metamodelPackageServiceImpl;
	@Autowired
	IMetamodelRelationTypeService metamodelRelationTypeServiceImpl;
	@Autowired
	IMetamodelRelationService metamodelRelationServiceImpl;
	@Autowired
	IMetamodelClassgroupService metamodelClassgroupServiceImpl;
	@Autowired
	IMetamodelClasspropertyService metamodelClasspropertyServiceImpl;

	@RequestMapping("/add")
	@ResponseBody
	public JSONObject add(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			String json = request.getParameter("json");
			MetamodelClass entity = BasicJsonUtil.getInstance().toJavaBean(json, MetamodelClass.class);
			metamodelClassServiceImpl.insert(entity);
			jsonObject.put("code", "suc");
			jsonObject.put("msg", "增加成功！");
		} catch (IOException e) {
			jsonObject.put("code", "err");
			jsonObject.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jsonObject;
	}

	@RequestMapping("/del")
	@ResponseBody
	public JSONObject del(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			String entity = request.getParameter("json");
			metamodelClassServiceImpl.deleteById(entity);
			jsonObject.put("code", "suc");
			jsonObject.put("msg", "增加成功！");
		} catch (Exception e) {
			jsonObject.put("code", "err");
			jsonObject.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jsonObject;
	}

	@RequestMapping("/update")
	@ResponseBody
	public JSONObject update(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			String json = request.getParameter("json");
			MetamodelClass entity = BasicJsonUtil.getInstance().toJavaBean(json, MetamodelClass.class);
			metamodelClassServiceImpl.insert(entity);
			jsonObject.put("code", "suc");
			jsonObject.put("msg", "增加成功！");
		} catch (IOException e) {
			jsonObject.put("code", "err");
			jsonObject.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jsonObject;
	}

	@RequestMapping("/selectAll")
	@ResponseBody
	public List<MetamodelClass> selectAll(HttpServletRequest request) {
		Map<String, Object> columnMap = new HashMap<String, Object>();
		return metamodelClassServiceImpl.selectByMap(columnMap);
	}

	@RequestMapping("/selectObj")
	@ResponseBody
	public MetamodelClass selectObj(HttpServletRequest request) {
		String id = request.getParameter("id");
		return metamodelClassServiceImpl.selectById(id);
	}
	
	@RequestMapping(value = "/ajaxQueryModelMenuAction", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String ajaxQueryModelMenuAction(HttpServletRequest request) {
		String parentId = request.getParameter("parentId");
		String classId = request.getParameter("classId");
		String type = request.getParameter("type");
		String isshow = request.getParameter("isshow");
		List<MetamodelPackage> packageList = new ArrayList<MetamodelPackage>();
		List<MetamodelClass> classList = new ArrayList<MetamodelClass>();
		JSONObject jsonObject = new JSONObject();
		log.debug("进入查询模型包和模型类功能");
		try {
			// 查询模型包
			if ((parentId == null || parentId.equals("")) && (classId == null || classId.equals(""))) {
				packageList = metamodelPackageServiceImpl.queryModelPackageByParentIdAndType(parentId, type);
				
				jsonObject.put("packageList", packageList);
			}
			// 查询模型类
			if (parentId != null && !parentId.equals("")) {
				packageList = metamodelPackageServiceImpl.queryModelPackageByParentIdAndType(parentId, type);
				classList = metamodelClassServiceImpl.queryModelClassByParentIdAndType(parentId, isshow, type);
				
				jsonObject.put("packageList", packageList);
				jsonObject.put("classList", classList);
			}
			jsonObject.put("parentId", parentId);
			jsonObject.put("classId", classId);
			jsonObject.put("code", 0);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e);
			jsonObject.put("code", -2);
		}
		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/ajaxInitModelMenuAction", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String ajaxInitModelMenuAction(HttpServletRequest request) {
		String packageId = request.getParameter("packageId");
		String classId = request.getParameter("classId");
		JSONObject jsonObject = new JSONObject();
		log.debug("进入查询元模型包菜单");
		try {
			String packageIdStr = metamodelClassServiceImpl.queryModelClassIdListById(packageId, classId);
			jsonObject.put("packageIdStr", packageIdStr);
			jsonObject.put("code", 0);
		} catch (Exception e) {
			log.debug(e);
			e.printStackTrace();
			jsonObject.put("code", -2);
		}
		return jsonObject.toJSONString();
	}

	@RequestMapping(value = "/ajaxQueryModelClassAction", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String ajaxQueryModelClassAction(HttpServletRequest request) {
		String packageId = request.getParameter("packageId");
		String classId = request.getParameter("classId");
		String queryType = request.getParameter("queryType");
		String groupedClassName = request.getParameter("groupedClassName");
		String groupName = request.getParameter("groupName");
		String groupClassName = request.getParameter("groupClassName");
		String relationType = request.getParameter("relationType");
		String relationName = request.getParameter("relationName");
		String classname = request.getParameter("classname");
		int pageNo = StringUtil.isNotEmpty(request.getParameter("pageNo"))//前端翻页框架使当前页码要比实际当前页码少1
				? Integer.parseInt(request.getParameter("pageNo"))+1 : 1;
		int pageSize = StringUtil.isNotEmpty(request.getParameter("pageSize"))
				? Integer.parseInt(request.getParameter("pageSize")) : 10;
		List<MetamodelClassproperty> propertyList = new ArrayList<MetamodelClassproperty>();
		List<MetamodelClassgroup> groupList = new ArrayList<MetamodelClassgroup>();
		List<MetamodelRelation> relationList = new ArrayList<MetamodelRelation>();
		List<MetamodelRelationType> relaTypeList = new ArrayList<MetamodelRelationType>();
		
		MetamodelClass classVo = new MetamodelClass();
		JSONObject jsonObject = new JSONObject();
		log.debug("进入查询模型类信息功能");
		try {
			if (queryType != null && !queryType.equals("")) {
				if (queryType.equals("classInfo")) {
					// 查询模型类基本信息
					if (classId != null && !classId.equals("")) {
						classVo = metamodelClassServiceImpl.queryModelClassByClassId(classId);
						jsonObject.put("classVo", classVo);
					}
				} else if (queryType.equals("propertyList")) {
					// 查询模型类属性信息
					if (classId != null && !classId.equals("")) {
						Page<MetamodelClassproperty> page = new Page<MetamodelClassproperty>(pageNo, pageSize);
						page = metamodelClassServiceImpl.queryModelClassPropertyByClassId(classId, page);

						propertyList = page.getRecords();
						jsonObject.put("list", propertyList);
						jsonObject.put("totalCount", page.getTotal());
						jsonObject.put("totalPage", page.getPages());
						jsonObject.put("code", 0);
					}
				} else if (queryType.equals("groupList")) {
					// 查询组合关系
					Map<String,Object> params = new HashMap<String,Object>();
					params.put("classId", classId);
					if (groupedClassName != null && !groupedClassName.equals("")) {
						params.put("groupedClassName", groupedClassName);
					}
					if (groupName != null && !groupName.equals("")) {
						params.put("groupName", groupName);
					}
					Page<MetamodelClassgroup> page = new Page<MetamodelClassgroup>(pageNo, pageSize);
					page = metamodelClassServiceImpl.queryModelClassGroup(params, page);
					groupList = page.getRecords();
					// 遍历将被组合类的名称和代码赋值给groupList
					MetamodelClass metamodelClass = metamodelClassServiceImpl
							.selectById(classId);
					for (MetamodelClassgroup metamodelGroupVO : groupList) {
						
						// 当查询条件不为空时
						if (groupName != null && !groupName.equals("")
								|| groupedClassName != null && !groupedClassName.equals("")) {
							
							metamodelGroupVO.setClassCode(metamodelClass.getClassCode());
							metamodelGroupVO.setClassName(metamodelClass.getClassName());
						} else {
							String classgroupedId = metamodelGroupVO.getClassgroupedId();
							// 通过被组合类id查找被组合类的名称和代码
							MetamodelClass metamodelClassVO = metamodelClassServiceImpl.selectById(classgroupedId);
							metamodelGroupVO.setClassCoded(metamodelClassVO.getClassCode());
							metamodelGroupVO.setClassNamed(metamodelClassVO.getClassName());
						}
						
					}
					jsonObject.put("list", groupList);
					jsonObject.put("totalCount", page.getTotal());
					jsonObject.put("totalPage", page.getPages());
					jsonObject.put("code", 0);
				} else if (queryType.equals("groupedList")) {
					// 查询组合关系
					Map<String,Object> params = new HashMap<String,Object>();
					params.put("classId", classId);
					if (groupClassName != null && !groupClassName.equals("")) {
						params.put("groupClassName", groupClassName);
					}
					if (groupName != null && !groupName.equals("")) {
						params.put("groupName", groupName);
					}
					Page<MetamodelClassgroup> page = new Page<MetamodelClassgroup>(pageNo, pageSize);
					page = metamodelClassServiceImpl.queryModelClassGrouped(params, page);
					groupList = page.getRecords();
					// 遍历将被组合类的名称和代码赋值给groupList
					
					jsonObject.put("list", groupList);
					jsonObject.put("totalCount", page.getTotal());
					jsonObject.put("totalPage", page.getPages());
					jsonObject.put("code", 0);
				} else if (queryType.equals("relationList")) {
					// 查询关联关系
					Map<String,Object> params = new HashMap<String,Object>();
					params.put("classId", classId);
					if (relationType != null && !relationType.equals("")) {
						params.put("relationType", relationType);
					}
					if (relationName != null && !relationName.equals("")) {
						params.put("relationName", relationName);
					}
					Page<MetamodelRelation> page = new Page<MetamodelRelation>(pageNo, pageSize);
					page = metamodelClassServiceImpl.queryModelClassRelation(params, page);
					relationList = page.getRecords();
					for (MetamodelRelation metamodelRelationVO : relationList) {
						MetamodelClass metamodelClassVO = metamodelClassServiceImpl
								.selectById(metamodelRelationVO.getClassedId());
						metamodelRelationVO.setClassCoded(metamodelClassVO.getClassCode());
						metamodelRelationVO.setClassNamed(metamodelClassVO.getClassName());
					}
					jsonObject.put("list", relationList);
					jsonObject.put("totalCount", page.getTotal());
					jsonObject.put("totalPage", page.getPages());
					jsonObject.put("code", 0);
				} else if (queryType.equals("classlist")) {
					// 获取显示类列表的数据
					// pageIndex=pageIndex+1;
					Map<String,Object> params = new HashMap<String,Object>();
					if (classname != null && !classname.equals("")) {
						params.put("classname", classname);
					}
					if (packageId != null && !packageId.equals("")) {
						params.put("packageId", packageId);
					}
					Page<MetamodelClass> page = new Page<MetamodelClass>(pageNo, pageSize);
					page = metamodelClassServiceImpl.queryModelClass(params, page);
					jsonObject.put("list", page.getRecords());
					jsonObject.put("totalCount", page.getTotal());
					jsonObject.put("totalPage", page.getPages());
					jsonObject.put("code", 0);
				} else if (queryType.equals("relationType")) {
					//查询关联关系类型列表
					relaTypeList = metamodelRelationTypeServiceImpl.selectList(null);
					jsonObject.put("relaTypeList", relaTypeList);
					jsonObject.put("code", 0);
				}

			}
			jsonObject.put("classVo", classVo);
			jsonObject.put("propertyList", propertyList);
		} catch (Exception e) {
			log.debug(e);
			e.printStackTrace();
			jsonObject.put("code", -2);
		}
		return jsonObject.toJSONString();
	}
	
	
	@RequestMapping(value = "/ajaxQueryModelClassSaveAction", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String ajaxQueryModelClassSaveAction(HttpServletRequest request) {
		String operate = request.getParameter("operate");
		String id = request.getParameter("id");
		String entity = request.getParameter("metamodelClass");
		String packageId = request.getParameter("packageId");
		JSONObject jsonObject = new JSONObject();
		List<MetamodelClass>  listcode = new ArrayList<MetamodelClass>();
		String code = request.getParameter("code");
		MetamodelClass metamodelClassVO = JSON.parseObject(entity, MetamodelClass.class);
		try {
			if(operate.equals("add")){
				metamodelClassVO.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				metamodelClassServiceImpl.insert(metamodelClassVO);
            }else if(operate.equals("edit")){
            	metamodelClassVO.setEditDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            	metamodelClassServiceImpl.updateById(metamodelClassVO);
            }else if(operate.equals("del")){
            	Map<String,Object> mcg = new HashMap<String, Object>();
            	Map<String,Object> mr = new HashMap<String, Object>();
            	//EntityWrapper<MetamodelInheritanceRelationship> mi = new EntityWrapper<MetamodelInheritanceRelationship>();
            	//关联
            	mr.put("class_id", id);
            	List<MetamodelRelation> list=metamodelRelationServiceImpl.selectList(mr);
                //被关联
            	mr = new HashMap<String, Object>();
            	mr.put("classed_id", id);
            	List<MetamodelRelation> list2=metamodelRelationServiceImpl.selectList(mr);
            	//组合
            	mcg.put("class_id", id);
            	List<MetamodelClassgroup> list3=metamodelClassgroupServiceImpl.selectList(mcg);
            	//被组合
            	mcg = new HashMap<String, Object>();
            	mcg.put("classgrouped_id", id);
            	List<MetamodelClassgroup> list4=metamodelClassgroupServiceImpl.selectList(mcg);
            	
            	//继承关系
            	//mi.eq("classId", id);
            	//metamodelInheritanceRelationshipServiceImpl.selectList(mi);
            	 if(list.size()!=0||list2.size()!=0||list3.size()!=0||list4.size()!=0){
            		 jsonObject.put("isdelete", "yes");
            	 }else{
            		 Map<String,Object> map = new HashMap<String,Object>();
            		 map.put("class_id", id);
            		 metamodelClasspropertyServiceImpl.delete(map);
            		 metamodelClassServiceImpl.deleteById(id);
            	}
            }else if(operate.equals("check")){
            	
            		listcode=metamodelClassServiceImpl.queryByCodeAndNotId(code, id,packageId);
            }
			jsonObject.put("listcode", listcode);
			jsonObject.put("metamodelClassVO", metamodelClassVO);
			jsonObject.put("code", "0");
			
		} catch (Exception e) {
			log.debug(e);
			jsonObject.put("code", "-2");
			e.printStackTrace();
		}
		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/ajaxInfoAttrAction", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String ajaxInfoAttrAction(HttpServletRequest request) {
		String operate = request.getParameter("operate");
		String code = request.getParameter("code");
		String propertyName = request.getParameter("propertyName");
		String classId = request.getParameter("classId");
		String id = request.getParameter("id");
		String entity = request.getParameter("entity");
		
		String propertyId = request.getParameter("propertyId");
		String pageNoStr = StringUtil.getString(request.getParameter("pageIndex"));
		String pageSizeStr = StringUtil.getString(request.getParameter("pageSize"));
		int pageNo = AppConstants.pageNo;
		if(!"".equals(pageNoStr)){
			pageNo = Integer.parseInt(pageNoStr);
		}
		int pageSize = AppConstants.pageSize;
		if(!"".equals(pageSizeStr)){
			pageSize = Integer.parseInt(pageSizeStr);
		}
		MetamodelClassproperty metamodelClassPropertyVO = new MetamodelClassproperty();
		JSONObject jsonObject = new JSONObject();
		try {
            if(operate.equals("add")){
            	
            	metamodelClassPropertyVO = JSON.parseObject(entity,MetamodelClassproperty.class);
            	metamodelClassPropertyVO.setCreateDate(DateUtil.getServerTime("yyyy-MM-dd HH:mm:ss").toString());
            	boolean flag = metamodelClasspropertyServiceImpl.insert(metamodelClassPropertyVO);
            	jsonObject.put("flag", flag);
            	//metamodelInheritanceRelationshipServiceImpl.classAddProperty(metamodelClassPropertyVO);
            }else if(operate.equals("edit")){
            	
            	metamodelClassPropertyVO.setEditDate(DateUtil.getServerTime("yyyy-MM-dd HH:mm:ss").toString());
            	metamodelClassPropertyVO = JSON.parseObject(entity,MetamodelClassproperty.class);
            //	metamodelInheritanceRelationshipServiceImpl.classUpdProperty(metamodelClassPropertyVO);
            	boolean flag = metamodelClasspropertyServiceImpl.updateById(metamodelClassPropertyVO);
            	jsonObject.put("flag", flag);
            	
            }else if(operate.equals("del")){
            	
            	boolean flag = metamodelClasspropertyServiceImpl.deleteMetamodelClasspropertyById(id);
            	jsonObject.put("flag", flag);
            	
            }else if(operate.equals("queryByPropertyId")){
            	
            	Page<MetamodelClassproperty> page = new Page<MetamodelClassproperty>(pageNo,pageSize);
            	page = metamodelClasspropertyServiceImpl.queryByPropertyIDResultInfoAttr(propertyId,page);
            	jsonObject.put("propertyList", page.getRecords());
            	jsonObject.put("total", page.getTotal());
            	
            }else if(operate.equals("check")){//属性代码和属性名称校验重复
            	MetamodelClassproperty metamodelClassproperty = new MetamodelClassproperty();
            	metamodelClassproperty.setPropertyCode(code);
            	metamodelClassproperty.setClassId(classId);
            	metamodelClassproperty.setPropertyId(id);
            	String codeSize = metamodelClasspropertyServiceImpl.selectMetamodelClasspropertyCount(metamodelClassproperty);
            	metamodelClassproperty = new MetamodelClassproperty();
            	metamodelClassproperty.setPropertyName(propertyName);
            	metamodelClassproperty.setClassId(classId);
            	metamodelClassproperty.setPropertyId(id);
            	String propertyNameSize = metamodelClasspropertyServiceImpl.selectMetamodelClasspropertyCount(metamodelClassproperty);
            	jsonObject.put("codeSize", codeSize);
            	jsonObject.put("propertyNameSize", propertyNameSize);
            }
            jsonObject.put("code", "0");
		} catch (Exception e) {
			log.debug(e);
			e.printStackTrace();
			jsonObject.put("code", -2);
		}
		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/ajaxQueryModelPackageAction", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String ajaxQueryModelPackageAction(HttpServletRequest request) {
		String packageId = request.getParameter("packageId");
		String packageName = request.getParameter("packageName");
		
		String pageNoStr = StringUtil.getString(request.getParameter("pageNo"));
		String pageSizeStr = StringUtil.getString(request.getParameter("pageSize"));
		int pageNo = AppConstants.pageNo;
		if(!"".equals(pageNoStr)){
			pageNo = Integer.parseInt(pageNoStr)+1;
		}
		int pageSize = AppConstants.pageSize;
		if(!"".equals(pageSizeStr)){
			pageSize = Integer.parseInt(pageSizeStr);
		}
		JSONObject jsonObject = new JSONObject();
		log.debug("进入查询元模型包功能");
		try {
			if (packageId != null && !packageId.equals("")) {

				Map<String,Object> param = new HashMap<String,Object>();
				param.put("packageId", packageId);
				if (packageName != null && !packageName.equals("")) {
					param.put("packageName", packageName);
				}
				jsonObject = metamodelPackageServiceImpl.queryMetamodelPackageBysql(param, pageNo, pageSize);
			}
			jsonObject.put("code", "0");
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", -2);
		}
		return jsonObject.toJSONString();
	}
	
	
	@RequestMapping(value = "/ajaxQueryAddGroupInfoAction", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String ajaxQueryAddGroupInfoAction(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		log.debug("进入查询元模型功能");
		try {
			List<MetamodelClass> list = metamodelClassServiceImpl.selectList(null);
			jsonObject.put("list", list);
			jsonObject.put("code", "0");
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", -2);
		}
		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/getMetamodelClassById", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getMetamodelClassById(HttpServletRequest request){
		String id = request.getParameter("id");
		JSONObject jsonObject = new JSONObject();
		try {
			MetamodelClass entity = metamodelClassServiceImpl.selectById(id);
			jsonObject.put("metamodelClassVO", entity);
			jsonObject.put("code", 0);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", -2);
		}
		//MetamodelClass entity = metamodelClassServiceImpl.selectById(id);
		jsonObject.put("code", 0);
		return jsonObject.toJSONString();
	}
}
