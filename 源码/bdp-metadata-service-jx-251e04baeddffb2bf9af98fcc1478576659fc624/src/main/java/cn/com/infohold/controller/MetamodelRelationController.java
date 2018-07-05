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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;

import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.commons.AppConstants;
import cn.com.infohold.core.controller.BaseController;
import cn.com.infohold.entity.Apppar;
import cn.com.infohold.entity.Metadata;
import cn.com.infohold.entity.MetadataCatalog;
import cn.com.infohold.entity.MetadataRelation;
import cn.com.infohold.entity.MetamodelClass;
import cn.com.infohold.entity.MetamodelRelation;
import cn.com.infohold.service.IAppparService;
import cn.com.infohold.service.IMetadataCatalogService;
import cn.com.infohold.service.IMetadataRelationService;
import cn.com.infohold.service.IMetadataService;
import cn.com.infohold.service.IMetamodelClassService;
import cn.com.infohold.service.IMetamodelRelationService;
import cn.com.infohold.tools.util.StringUtil;
import cn.com.infohold.util.TypeConversionUtil;
import lombok.extern.log4j.Log4j2;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@Log4j2(topic = "MetamodelRelationController")
@Controller
@RequestMapping("/metamodelRelation")
public class MetamodelRelationController extends BaseController {
	@Autowired
	IMetamodelRelationService metamodelRelationServiceImpl;
	@Autowired
	IMetamodelClassService metamodelClassServiceImpl;
	@Autowired
	IMetadataCatalogService metadataCatalogServiceImpl;
	@Autowired
	IMetadataService metadataServiceImpl;
	@Autowired
	IAppparService appparServiceImpl;
	@Autowired
	IMetadataRelationService metadataRelationServiceImpl;
	@RequestMapping("/add")
	@ResponseBody
	public JSONObject add(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			String json = request.getParameter("json");
			MetamodelRelation entity = BasicJsonUtil.getInstance().toJavaBean(json, MetamodelRelation.class);
			metamodelRelationServiceImpl.insert(entity);
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
			metamodelRelationServiceImpl.deleteById(entity);
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
			MetamodelRelation entity = BasicJsonUtil.getInstance().toJavaBean(json, MetamodelRelation.class);
			metamodelRelationServiceImpl.insert(entity);
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
	public List<MetamodelRelation> selectAll(HttpServletRequest request) {
		Map<String, Object> columnMap = new HashMap<String, Object>();
		return metamodelRelationServiceImpl.selectByMap(columnMap);
	}

	@RequestMapping("/selectObj")
	@ResponseBody
	public MetamodelRelation selectObj(HttpServletRequest request) {
		String id = request.getParameter("id");
		return metamodelRelationServiceImpl.selectById(id);
	}
	
	@RequestMapping(value = "/selectMetamodelRelation", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String selectMetamodelRelation(HttpServletRequest request){
		String json = StringUtil.getString(request.getParameter("json"));//json格式的查询条件
		String pageNoStr = StringUtil.getString(request.getParameter("pageNo"));
		String pageSizeStr = StringUtil.getString(request.getParameter("pageSize"));
		int pageNo = AppConstants.pageNo;
		if(!"".equals(pageNoStr)){
			pageNo = Integer.parseInt(pageNoStr);
		}
		int pageSize = AppConstants.pageSize;
		if(!"".equals(pageSizeStr)){
			pageSize = Integer.parseInt(pageSizeStr);
		}
		MetamodelRelation entity = JSON.parseObject(json,MetamodelRelation.class);
		List<MetamodelRelation> list = metamodelRelationServiceImpl.selectMetamodelRelationList(entity,pageNo,pageSize);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", list);
		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/saveMetamodelRelation", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String saveMetamodelRelation(HttpServletRequest request){
		String json = request.getParameter("json");
		MetamodelRelation entity = JSON.parseObject(json,MetamodelRelation.class);
		boolean flag = metamodelRelationServiceImpl.insertOrUpdate(entity);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("flag", flag);
		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/getMetamodelRelationById", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getMetamodelRelationById(HttpServletRequest request){
		String id = request.getParameter("id");
		MetamodelRelation entity = metamodelRelationServiceImpl.selectMetamodelRelationById(id);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("entity", entity);
		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/delMetamodelRelationById", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String delMetamodelRelationById(HttpServletRequest request){
		String id = request.getParameter("id");
		boolean flag = metamodelRelationServiceImpl.deleteById(id);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("flag", flag);
		return jsonObject.toJSONString();
	}
	
	
	@RequestMapping(value = "/ajaxQueryRelationAction", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String ajaxQueryRelationAction(HttpServletRequest request){

		/** 元模型类对象*/
		MetamodelClass metamodelClassVO = new MetamodelClass();
		/** 元模型关联关系数据集合*/
		List<MetamodelRelation> mMetamodelRelationVOList = new ArrayList<MetamodelRelation>();
		/** 判断查询类型的标识*/
		String flag = request.getParameter("flag");
		/** 元模型ID*/
		String classId = request.getParameter("classId");
		String relationId = request.getParameter("relationId");
		String metadataId = request.getParameter("metadataId");
		String pageNoStr = StringUtil.getString(request.getParameter("pageNo"));
		String pageSizeStr = StringUtil.getString(request.getParameter("pageSize"));
		List<MetadataRelation> metadataRelationVOs = new ArrayList<MetadataRelation>();
		log.debug("进入检索关联关系功能");
		JSONObject jsonObject = new JSONObject();
		try {
			if (flag != null && flag.equals("metamodelRelation")) {
				if (classId != null && !classId.equals("")) {

					mMetamodelRelationVOList = metamodelRelationServiceImpl.queryByClassIdRelationInfo(classId + "");
					metamodelClassVO = metamodelClassServiceImpl.queryModelClassByClassId(classId);
					jsonObject.put("mMetamodelRelationVOList", mMetamodelRelationVOList);
					jsonObject.put("metamodelClassVO", metamodelClassVO);
					jsonObject.put("code", 0);
				}
			}else if(flag!=null&&flag.equals("getmetadarela")){
				metadataRelationVOs=metadataRelationServiceImpl.queryRelationByIdAndRelationClassId(metadataId,relationId);
				jsonObject.put("metadataRelationVOs", metadataRelationVOs);
				jsonObject.put("code", 0);
			}else {
				int pageNo = AppConstants.pageNo;
				if(pageNoStr!=null && !"".equals(pageNoStr)){
					pageNo = Integer.parseInt(pageNoStr);
				}
				int pageSize = AppConstants.pageSize;
				if(pageSizeStr!= null && !"".equals(pageSizeStr)){
					pageSize = Integer.parseInt(pageSizeStr);
				}
				if(classId!=null&&relationId!=null&&metadataId!=null){
					jsonObject = metadataServiceImpl.queryRelationAndMetaDataByClassId(classId, relationId, metadataId, pageNo, pageSize);
									
				}

			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			jsonObject.put("code", -2);
		}
		return jsonObject.toJSONString();
		
	}
	
	@RequestMapping(value = "/ajaxQueryRelationLeftAction", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String ajaxQueryRelationLeftAction(HttpServletRequest request){

		/** 元数据*/
		List<Metadata> metadataList = new ArrayList<Metadata>();
		/** 元模型关联关系数据集合*/
		List<MetadataCatalog> catalogList = new ArrayList<MetadataCatalog>();
		/** 判断查询类型的标识*/
		String flags = request.getParameter("flags");
		/** 元模型ID*/
		String classId = request.getParameter("classId");
		String metadataId = request.getParameter("metadataId");
		String metadataIds = request.getParameter("metadataIds");
		log.debug("进入检索关联关系功能");
		JSONObject jsonObject = new JSONObject();
		try {

			if(flags!=null&&flags.equals("initMenu")){
				catalogList=metadataCatalogServiceImpl.queryCatalogBy(classId);
			}else if(flags!=null&&flags.equals("inittwo")){
				metadataList=metadataServiceImpl.queryMataByclassId(classId);
				if (metadataList.size()==0) {
				    catalogList=metadataCatalogServiceImpl.queryCatalogBy(classId);
				}
			}else if(flags!=null&&flags.equals("querymetadatass")){
				 metadataList=metadataServiceImpl.queryMetadataByCatalogBy(metadataId, classId, metadataIds);
			}
			jsonObject.put("catalogList", catalogList);
			jsonObject.put("metadataList", metadataList);
			jsonObject.put("metadataId", metadataId);
		} catch (Exception ex) {
			ex.printStackTrace();
			jsonObject.put("code", -2);
		}
		return jsonObject.toJSONString();
		
	}
	
	@RequestMapping(value = "/ajaxAddOrDeleteRelationAction", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String ajaxAddOrDeleteRelationAction(HttpServletRequest request){

		
		/** 判断查询类型的标识*/
		String flag = request.getParameter("flag");
		/** 关联ID*/
		String id = request.getParameter("id");
		/** 元模型ID*/
		String relationId = request.getParameter("relationId");
		String metadataId = request.getParameter("metadataId");
		String metadataedIdStr = request.getParameter("metadataedId");
		String[] metadataedId= TypeConversionUtil.JSONArrayToString(metadataedIdStr);
		String conditionId = request.getParameter("conditionId");
		String createDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

		JSONObject jsonObject = new JSONObject();
		try {
			
			log.debug("进入增加和删除元数据关联关系功能");
			
			if (flag.equals("add")) {
				
				if (metadataedId != null && metadataedId.length > 0) {
					for(int i=0; i<metadataedId.length; i++){
						
						MetamodelRelation relationVO = metamodelRelationServiceImpl.selectMetamodelRelationById(relationId);
						Map<String, Object> wrapper = new HashMap<String,Object>();
						wrapper.put("code", "metadata_relation");
						List<Apppar> list = appparServiceImpl.selectList(wrapper);
						
						boolean flags = false;
						if(list != null && list.size()>0){
							for(int j=0;j<list.size();j++){
								if(relationVO.getRelationCode().equals(list.get(j).getShowmsg())){
									flags = true;
									break;
								}
							}
						}
						
						if(flags){
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
							metadataRelationServiceImpl.insert(vo);
							metadataRelationServiceImpl.insert(voed);
						}else {
							MetadataRelation vo = new MetadataRelation();
							vo.setMetadataId(metadataId);
							if(conditionId!=null&&!conditionId.equals("")){
								vo.setConditionId(conditionId);
							}
							vo.setRelationId(relationId);
							vo.setCreateDate(createDate);
							vo.setMetadataRelationed(metadataedId[i]);
							metadataRelationServiceImpl.insert(vo);
							
						}
						
					}
				}
				
			}
			
			else {
				if(id!=null){
					
					
					MetadataRelation vo = metadataRelationServiceImpl.selectById(id);
					MetamodelRelation relationVO = metamodelRelationServiceImpl.selectMetamodelRelationById(vo.getRelationId());
					
					Map<String, Object> wrapper = new HashMap<String,Object>();
					wrapper.put("code", "metadata_relation");
					List<Apppar> list = appparServiceImpl.selectList(wrapper);
					boolean flags = false;
					if(list != null && list.size()>0){
						for(int j=0;j<list.size();j++){
							if(relationVO.getRelationCode().equals(list.get(j).getShowmsg())){
								flags = true;
								break;
							}
						}
					}
					
					if (flags) {
						//删除被关联关系
						metadataRelationServiceImpl.delMetadataRalation(vo);
						//metadataObjectService.deleteMetaDataRelationVO(vo);
						//删除关联关系
						metadataRelationServiceImpl.delMetadataRelation(vo.getMetadataRelationId());
					}else {
						metadataRelationServiceImpl.delMetadataRelation(vo.getMetadataRelationId());
						//metadataObjectService.deleteMetaDataRelationVO(vo);
					}
				}
				
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			jsonObject.put("code", -2);
		}
		return jsonObject.toJSONString();
		
	}
	
	//ajaxRelationInfoAction
	@RequestMapping(value = "/ajaxRelationInfoAction", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String ajaxRelationInfoAction(HttpServletRequest request){
		String operate = request.getParameter("operate");
		String code = request.getParameter("code");
		String classedId = request.getParameter("classedId");
		String classId = request.getParameter("classId");
		String id = request.getParameter("id");
		String relationTypeId = request.getParameter("relationTypeId");
		String entity = request.getParameter("metamodelRelationVO");
		MetamodelRelation metamodelRelationVO = JSON.parseObject(entity, MetamodelRelation.class);
		
		JSONObject jsonObject = new JSONObject();
		try {
            if(operate.equals("add")){
            	metamodelRelationServiceImpl.insert(metamodelRelationVO);
            	jsonObject.put("code", 0);
            }else if(operate.equals("edit")){
            	metamodelRelationServiceImpl.updateById(metamodelRelationVO);
            	jsonObject.put("code", 0);
            }else if(operate.equals("del")){
            	jsonObject = metamodelRelationServiceImpl.delMetamodelRelationById(id);
            	
            }else if(operate.equals("queryById")){
            	metamodelRelationVO = metamodelRelationServiceImpl.selectMetamodelRelationById(id);
            	classedId = metamodelRelationVO.getClassedId();
				//通过被组合类id查找被组合类的名称和代码
				MetamodelClass metamodelClassVO = metamodelClassServiceImpl.queryByClassGroupedId(classedId);
				metamodelRelationVO.setClassCoded(metamodelClassVO.getClassCode());
				metamodelRelationVO.setClassNamed(metamodelClassVO.getClassName());
				jsonObject.put("metamodelRelationVO", metamodelRelationVO);
				jsonObject.put("code", 0);
            }else if (operate.equals("check")) {
            	Map<String, Object> map = new HashMap<String, Object>();
            	//MetamodelRelation
            	map.put("relation_code", code);
            	map.put("class_id", classId);
            	int codeSize = metamodelRelationServiceImpl.selectByMap(map).size();
            	map = new HashMap<String, Object>();
            	map.put("classed_id", classedId);
            	map.put("class_id", classId);
            	map.put("relation_type_id", relationTypeId);
            	int relationSize = metamodelRelationServiceImpl.selectByMap(map).size();
            	jsonObject.put("code", 0);
            	jsonObject.put("codeSize", codeSize);
            	jsonObject.put("relationSize", relationSize);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			jsonObject.put("code", -2);
		}
		return jsonObject.toJSONString();
	}
}
