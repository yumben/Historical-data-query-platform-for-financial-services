package cn.com.infohold.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import bdp.commons.dataservice.bean.SqlBean;
import bdp.commons.dataservice.ret.RetBean;
import cn.com.infohold.basic.util.file.PropUtil;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.commons.AppConstants;
import cn.com.infohold.core.controller.BaseController;
import cn.com.infohold.entity.Metadata;
import cn.com.infohold.entity.MetadataCatalog;
import cn.com.infohold.entity.MetadataProperty;
import cn.com.infohold.entity.MetamodelClass;
import cn.com.infohold.entity.MetamodelClassproperty;
import cn.com.infohold.service.IAppparService;
import cn.com.infohold.service.IMetadataCatalogService;
import cn.com.infohold.service.IMetadataPropertyKeyService;
import cn.com.infohold.service.IMetadataPropertyService;
import cn.com.infohold.service.IMetadataService;
import cn.com.infohold.service.IMetamodelClassService;
import cn.com.infohold.service.IMetamodelClasspropertyService;
import cn.com.infohold.service.IVMetadataPropertyService;
import cn.com.infohold.tools.util.StringUtil;
import cn.com.infohold.util.CommonUtil;
import cn.com.infohold.util.DatabaseUtil;
import com.jcabi.aspects.Loggable;
import lombok.extern.log4j.Log4j2;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@Log4j2
@RestController
@RequestMapping("/metadata")
@Loggable(Loggable.DEBUG)
public class MetadataController extends BaseController {

	@Autowired
	IMetadataService metadataServiceImpl;
	@Autowired
	IMetadataCatalogService metadataCatalogServiceImpl;
	@Autowired
	IMetamodelClassService metamodelClassServiceImpl;
	@Autowired
	IMetadataPropertyService metadataPropertyServiceImpl;
	@Autowired
	IAppparService appparServiceImpl;
	@Autowired
	IMetadataPropertyKeyService metadataPropertyKeyServiceImpl;
	@Autowired
	IMetamodelClasspropertyService metamodelClasspropertyServiceImpl;
	@Autowired
	DatabaseUtil databaseUtil;
	@Autowired
	IVMetadataPropertyService vMetadataPropertyServiceImpl;

	@Autowired
	DataSource mysqls;

	/**
	 * 添加元数据
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/saveMetadata", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject add(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			String json = request.getParameter("metadataJson");
			jsonObject = metadataServiceImpl.addMetadataList(json);

		} catch (Exception e) {
			jsonObject.put("code", "err");
			jsonObject.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jsonObject;
	}

	@RequestMapping(value = "/delMetadata", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject del(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			String metadataCode = request.getParameter("metadataCode");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("metadata_code", metadataCode);
			jsonObject = metadataServiceImpl.deleteMetadata(map);
			/*
			 * jsonObject.put("code", "suc"); jsonObject.put("msg", "增加成功！");
			 */
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
			String entity = request.getParameter("json");
			Metadata metadata = BasicJsonUtil.getInstance().toJavaBean(entity, Metadata.class);
			metadataServiceImpl.insert(metadata);
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
	public List<Metadata> selectAll(HttpServletRequest request) {
		Map<String, Object> columnMap = new HashMap<String, Object>();
		return metadataServiceImpl.selectByMap(columnMap);
	}

	@RequestMapping("/selectObj")
	@ResponseBody
	public JSONObject selectObj(HttpServletRequest request) {
		String id = request.getParameter("id");
		Map<String, Object> columMap = new HashMap<String, Object>();
		columMap.put("metadata_id", id);
		Map<String, Object> map = metadataPropertyServiceImpl.selectForMap(columMap);
		map.put("metadata_id", id);
		JSONObject jsonObject = new JSONObject();
		jsonObject.putAll(map);
		return jsonObject;
	}

	@RequestMapping("/selectData/{tableId}")
	@ResponseBody
	public JSONObject selectData(@PathVariable String tableId, HttpServletRequest request) throws Exception {
		String json = request.getParameter("json");
		JSONObject jsonObject = new JSONObject();
		List<Map<String, Object>> list = databaseUtil.getDataByMetadataId(tableId, json);
		jsonObject.put("total", 100);
		jsonObject.put("list", list);
		return jsonObject;
	}

	/**
	 * 查询元数据及父级和属性
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/selectObj4ParentAndPropertie")
	@ResponseBody
	public JSONObject selectObj4ParentAndPropertie(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		Metadata entity = metadataServiceImpl.selectById(id);
		String text = BasicJsonUtil.getInstance().toJsonString(entity);
		JSONObject jsonObject = JSONObject.parseObject(text);
		Map<String, Object> columnMap = new HashMap<String, Object>();
		columnMap.put("metadata_id", id);
		List<MetadataProperty> properties = metadataPropertyServiceImpl.selectByMap(columnMap);
		jsonObject.put("properties", properties);
		if (!StringUtil.isEmpty(entity.getParentMetadata())) {
			Metadata parentEntity = metadataServiceImpl.selectById(entity.getParentMetadata());
			jsonObject.put("parent", parentEntity);
		}
		return jsonObject;
	}

	/**
	 * 同步表到元数据
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("/synchroTables")
	@ResponseBody
	public JSONObject synchroTables(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			String id = request.getParameter("id");
			String tableName = request.getParameter("tableName");
			String dbType = request.getParameter("dbType");
			List<JSONObject> list = databaseUtil.getTableByMetadataId(id, tableName, dbType);
			for (JSONObject json : list) {
				metadataServiceImpl.addMetadataJSon(json.toJSONString());
			}
			jsonObject.put("code", "suc");
			jsonObject.put("msg", "同步成功");
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", "err");
			jsonObject.put("msg", e.getMessage());
		}
		return jsonObject;
	}

	/**
	 * 查询元数据菜单
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/ajaxQueryMetadataMenuAction", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String queryMetadataMenuAction(HttpServletRequest request) {
		/**
		 * 目录ID
		 */
		String parentId = request.getParameter("parentId");
		/**
		 * 元数据ID
		 */
		String metadataId = request.getParameter("metadataId");
		/**
		 * 元模型ID
		 */
		String classId = request.getParameter("classId");
		/**
		 * 元模型类型
		 */
		String classType = request.getParameter("classType");

		String metadataIds = request.getParameter("metadataIds");
		String flags = request.getParameter("flags");
		List<MetadataCatalog> catalogList = new ArrayList<MetadataCatalog>();
		List<Metadata> metadataList = new ArrayList<Metadata>();
		List<MetamodelClass> modelClassList = new ArrayList<MetamodelClass>();
		JSONObject jsonObject = new JSONObject();
		try {
			if ((parentId == null || parentId.equals("")) && (metadataId == null || metadataId.equals(""))
					&& (classId == null || classId.equals(""))) {
				catalogList = metadataCatalogServiceImpl.queryMetaDataCatalogByParentIdAndType(parentId, classType);
				// System.out.println();
			}
			// 查询元数据目录下的元数据
			if (parentId != null && !parentId.equals("")) {
				catalogList = metadataCatalogServiceImpl.queryMetaDataCatalogByParentIdAndType(parentId, classType);
				metadataList = metadataServiceImpl.queryMetaDataByCatalogParentId(parentId, classType);
			}

			if (metadataId != null && !metadataId.equals("")) {
				if (classId != null && !classId.equals("")) {
					// 查询下级元数据
					metadataList = metadataServiceImpl.queryMetaDataByParentIdAndClassId(metadataId, classId,
							classType);
				} else {
					// 查询元数据对应元模型的组合关系的元模型
					modelClassList = metamodelClassServiceImpl.queryMetamodelByMetadataId(metadataId);
					if (flags != null && !flags.equals("")) {
						metadataList = metadataServiceImpl.queryMetaDataByParentIdAndNotId(metadataId, metadataIds);
					}
					// }
				}

			}
			jsonObject.put("catalogList", catalogList);
			jsonObject.put("metadataList", metadataList);
			jsonObject.put("modelClassList", modelClassList);
			jsonObject.put("classId", classId);
			jsonObject.put("parentId", parentId);
			jsonObject.put("metadataId", metadataId);
			jsonObject.put("code", 0);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", -2);
		}

		return jsonObject.toJSONString();
	}

	/**
	 * 查询元模型属性信息
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxQueryMetamodelPropertyAction", produces = "text/html;charset=UTF-8")
	public String ajaxQueryMetamodelPropertyAction(HttpServletRequest request) throws Exception {
		/**
		 * 元模型ID
		 */
		String classId = request.getParameter("classId");
		String parentId = request.getParameter("parentId");
		String flag = request.getParameter("flag");
		String metadataId = request.getParameter("metadataId");
		List<MetadataProperty> metadataPropertyVOList = new ArrayList<MetadataProperty>();
		List<MetamodelClassproperty> mList = new ArrayList<MetamodelClassproperty>();
		JSONObject jsonObject = new JSONObject();
		try {
			Map<Object, Object> returnMap = metadataServiceImpl.checkMetadataTypeByClassId(classId, metadataId,
					parentId);
			if ("metamodelClassPropertyVO".equals(flag)) {
				if (classId != null && !classId.equals("")) {
					mList = metamodelClasspropertyServiceImpl.selectMetamodelClasspropertyList(classId);
				}
			} else {
				if (classId != null && !classId.equals("")) {
					mList = metamodelClasspropertyServiceImpl.selectMetamodelClasspropertyList(classId);
				}
				if (metadataId != null && !metadataId.equals("")) {
					metadataPropertyVOList = metadataPropertyServiceImpl.selectMetaDataPropertyByMetadataId(metadataId);
				}
			}
			jsonObject.put("dictionaryVOList", returnMap.get("dictionaryVOList"));// 字段类型的下拉列表
			jsonObject.put("mList", mList);
			jsonObject.put("metadataPropertyVOList", metadataPropertyVOList);
			jsonObject.put("classId", classId);
			jsonObject.put("metadataId", metadataId);
			jsonObject.put("dbFlag", returnMap.get("dbFlag"));// 用于判断是数据库、还是表、还是字段等，用于前段判断显示用
			jsonObject.put("code", 0);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", -2);
		}

		return jsonObject.toJSONString();
	}

	/**
	 * 初始化菜单
	 *
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxInitMenuAction", produces = "text/html;charset=UTF-8")
	public String ajaxInitMenuAction(HttpServletRequest request) {
		/**
		 * 元数据ID
		 */
		String metadataId = request.getParameter("metadataId");
		String catalogId = request.getParameter("catalogId");
		String catalogIdStr = "";
		String metadataIdStr = "";
		String classIdStr = "";
		JSONObject jsonObject = new JSONObject();
		try {
			List<String> list = metadataCatalogServiceImpl.queryContextCatalog(catalogId, metadataId);
			catalogIdStr = list.get(3);
			metadataIdStr = list.get(4);
			classIdStr = list.get(5);
			jsonObject.put("classIdStr", classIdStr);
			jsonObject.put("metadataIdStr", metadataIdStr);
			jsonObject.put("catalogIdStr", catalogIdStr);
			jsonObject.put("code", 0);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", -2);
		}
		return jsonObject.toJSONString();

	}

	/**
	 * 查询元数据对象基本信息和属性信息
	 *
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxQueryMetadataObjectAction", produces = "text/html;charset=UTF-8")
	public String ajaxQueryMetadataObjectAction(HttpServletRequest request) {
		/**
		 * 元数据ID
		 */
		String metadataId = request.getParameter("metadataId");
		/**
		 * 元模型ID
		 */
		String classId = request.getParameter("classId");
		String flag = request.getParameter("flag");
		Metadata metadata = new Metadata();
		List<MetadataProperty> propertyList = new ArrayList<MetadataProperty>();
		List<MetamodelClass> modelClassList = new ArrayList<MetamodelClass>();
		JSONObject jsonObject = new JSONObject();
		try {
			if (flag != null && flag.equals("getMetada")) {

				List<Metadata> meList = metadataServiceImpl.queryMetaDataByparentId(metadataId);
				if (meList.size() == 0) {
					metadata = metadataServiceImpl.selectById(metadataId);
				}
			} else {

				if (metadataId != null && !metadataId.equals("")) {
					// 查询元数据基本信息
					metadata = metadataServiceImpl.queryMetadataById(metadataId);
					// 查询元数据属性信息
					propertyList = metadataPropertyServiceImpl.selectMetaDataPropertyByMetadataId(metadataId);
					// 查询元数据对应元模型的组合关系的元模型
					modelClassList = metamodelClassServiceImpl.queryMetamodelByMetadataId(metadataId);

				}
			}
			jsonObject.put("propertyList", propertyList);
			jsonObject.put("metadataVO", metadata);
			jsonObject.put("modelClassList", modelClassList);
			jsonObject.put("classId", classId);
			jsonObject.put("metadataId", metadataId);

			jsonObject.put("code", 0);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", -2);
		}

		return jsonObject.toJSONString();
	}

	/**
	 * 根据元数据ID查询元数据
	 *
	 * @param request
	 * @return
	 */
	/*
	 * @RequestMapping(value = "/getMetaData", produces =
	 * "text/html;charset=UTF-8")
	 * 
	 * @ResponseBody public String getMetaData(HttpServletRequest request){
	 * String metadataId = request.getParameter("metadataId"); String callback =
	 * request.getParameter("callback"); //return
	 * CommonUtil.stringMosaic(callback,
	 * metadataServiceImpl.getMetaData(metadataId).toJSONString()); return
	 * metadataServiceImpl.getMetaData(metadataId).toJSONString(); }
	 */
	@RequestMapping(value = "/getMetaDataOrIndex", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getMetaDataOrIndex(HttpServletRequest request) {
		String classId = request.getParameter("classId");
		MetamodelClass metamodelClass = metamodelClassServiceImpl.selectById(classId);
		JSONObject jsonObject = new JSONObject();
		if (metamodelClass.getClassCode().equals("index")) {
			jsonObject.put("flag", false);
		} else {
			jsonObject.put("flag", true);
		}
		return jsonObject.toJSONString();
	}

	/**
	 * 根据元数据ID或者目录类型查询元数据，返回json串
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getMetadataJsonById", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getMetadataJsonById(HttpServletRequest request) {
		String metadataId = request.getParameter("metadataId");
		String catalogType = request.getParameter("catalogType");
		JSONObject returnjsonObject = new JSONObject();
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		try {
			if (metadataId != null && metadataId.length() > 0) {// 根据元数据ID查询
				jsonObject = metadataServiceImpl.selectMetadataJson(metadataId);
				returnjsonObject.put("list", jsonObject);
			} else if (catalogType != null && catalogType.length() > 0) {// 根据目录类型查询
				jsonArray = metadataServiceImpl.selectMetadataJsonByCatalogType(catalogType);
				returnjsonObject.put("list", jsonArray);
			} else {// 查询全部

			}

			returnjsonObject.put("code", 0);
			returnjsonObject.put("msg", "交易成功");
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", -2);
		}
		return returnjsonObject.toJSONString();
	}

	/**
	 * 根据元数据code和level查询标识（level：p-查上级，i-查本身，c-查子集，a-上级、本身、子集），返回json串
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/selectMetadataJson", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject selectMetadataJson(HttpServletRequest request) {
		long s = System.currentTimeMillis();
		String metadataCode = request.getParameter("metadataCode");
		String level = request.getParameter("level");
		JSONObject jsonObject = new JSONObject();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("metadata_code", metadataCode);
			jsonObject = metadataServiceImpl.selectMetadata(map, level);
			long s1 = System.currentTimeMillis();
			log.debug("查询服务调的元数据 selectMetadataJson  {}ms", (s1 - s));
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			jsonObject.put("code", -2);
		}
		return jsonObject;
	}

	/**
	 * 根据元数据ID删除元数据（如果元数据有子集或者被关联关系，则不允许删除）
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/delMetadataListById", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String delMetadataListById(HttpServletRequest request) {
		String id = request.getParameter("metadataId");
		JSONObject jsonObject = new JSONObject();
		try {
			if (id == null || "".equals(id)) {
				jsonObject.put("code", -1);
				jsonObject.put("msg", "请输入元数据ID");
				return jsonObject.toJSONString();
			}
			jsonObject = metadataServiceImpl.deleteMetadata(id);

		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", -2);
		}
		return jsonObject.toJSONString();
	}

	/**
	 * 根据元数据json串修改元数据
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updateMetadata", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject updateMetadata(HttpServletRequest request) {
		String json = request.getParameter("metadataJson");
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = metadataServiceImpl.updateMetadataJson(json);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", -2);
		}

		return jsonObject;
	}

	/**
	 * 查询元模型
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/ajaxQueryClassListAction", produces = "text/html;charset=UTF-8")
	public String ajaxQueryClassListAction(HttpServletRequest request) {
		/**
		 * 元数据ID
		 */
		String metadataId = request.getParameter("metadataId");
		/**
		 * 元模型ID
		 */
		String classId = request.getParameter("classId");
		Metadata metadata = new Metadata();
		JSONObject jsonObject = new JSONObject();

		try {
			metadata.setParentMetadata(metadataId);
			metadata.setClassId(classId);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("class_id", classId);
			map.put("parent_metadata", metadataId);
			// List<Metadata> metadataClassVOList =
			// metadataServiceImpl.selectMetadataList(metadata);
			List<Metadata> metadataClassVOList = metadataServiceImpl.selectByMap(map);
			jsonObject.put("metadataClassVOList", metadataClassVOList);
			jsonObject.put("classId", classId);
			jsonObject.put("metadataId", metadataId);
			jsonObject.put("code", 0);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", -2);
		}
		return jsonObject.toJSONString();

	}

	/**
	 * 查询元数据信息，包含页码
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/ajaxQueryMetadataListAction", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String ajaxQueryMetadataListAction(HttpServletRequest request) {
		// String json =
		// StringUtil.getString(request.getParameter("json"));//json格式的查询条件
		String pageNoStr = StringUtil.getString(request.getParameter("pageNo"));
		String pageSizeStr = StringUtil.getString(request.getParameter("pageSize"));
		String codeOrName = StringUtil.getString(request.getParameter("codeOrName"));
		JSONObject jsonObject = new JSONObject();
		try {
			int pageNo = AppConstants.pageNo;
			if (pageNoStr != null && !"".equals(pageNoStr)) {
				pageNo = Integer.parseInt(pageNoStr);
			}
			int pageSize = AppConstants.pageSize;
			if (pageSizeStr != null && !"".equals(pageSizeStr)) {
				pageSize = Integer.parseInt(pageSizeStr);
			}
			jsonObject = metadataServiceImpl.queryMetaDataListByCodeOrName(codeOrName, pageNo + 1, pageSize);

		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", -2);
		}

		return jsonObject.toJSONString();
	}

	/**
	 * 查询元数据关联关系
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/ajaxQueryMetadataRelationshipListAction", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String queryMetadataRelationshipListAction(HttpServletRequest request) {
		// String json =
		// StringUtil.getString(request.getParameter("json"));//json格式的查询条件
		String pageNoStr = StringUtil.getString(request.getParameter("pageNo"));
		String pageSizeStr = StringUtil.getString(request.getParameter("pageSize"));
		String metadataId = StringUtil.getString(request.getParameter("metadataId"));
		JSONObject jsonObject = new JSONObject();
		try {
			int pageNo = AppConstants.pageNo;
			if (pageNoStr != null && !"".equals(pageNoStr)) {
				pageNo = Integer.parseInt(pageNoStr);
			}
			int pageSize = AppConstants.pageSize;
			if (pageSizeStr != null && !"".equals(pageSizeStr)) {
				pageSize = Integer.parseInt(pageSizeStr);
			}
			jsonObject = metadataServiceImpl.queryMetadataRelationshipBymetadataId(metadataId, pageNo, pageSize);

		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", -2);
		}

		return jsonObject.toJSONString();
	}

	/**
	 * 根据元数据ID删除元数据（包括元数据所有子集及关联关系，如果有被关联关系，不允许删除）
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/delMetadataByIdList", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String delMetadataByIdList(HttpServletRequest request) {
		String idList = request.getParameter("idList");
		String flag = request.getParameter("flag");
		// id = "19275";
		JSONObject jsonObject = new JSONObject();
		try {
			if (idList == null || "".equals(idList)) {
				jsonObject.put("code", -1);
				jsonObject.put("msg", "请输入元数据ID");
				return jsonObject.toJSONString();
			}
			if (idList != null) {
				jsonObject = metadataServiceImpl.deleteMetadataByIdList(JSON.parseArray(idList), flag);
			}

		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", -2);
		}
		return jsonObject.toJSONString();
	}

	/**
	 * 根据元数据对象json串保存元数据，仅是元数据对象，不需要保存属性之类的，并且返回元数据ID
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addMetadata", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String addMetadataObject(HttpServletRequest request) {
		String json = request.getParameter("metadata");
		JSONObject jsonObject = new JSONObject();

		try {
			Metadata metadata = new Metadata();
			metadata = JSON.parseObject(json, Metadata.class);
			metadataServiceImpl.insertOrUpdate(metadata);
			jsonObject.put("code", 0);
			jsonObject.put("metadata", metadata.getMetadataId());
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", -2);
		}
		return jsonObject.toString();
	}

	/**
	 * 根据元数据代码（metadata_code）查询元数据属性信息及其所有子集属性信息
	 *
	 * @param request
	 * @return json串
	 */
	@RequestMapping("/selectPropertyByMetadataCode")
	@ResponseBody
	public String selectPropertyByMetadataCode(HttpServletRequest request) {
		String id = request.getParameter("metadata_code");
		return vMetadataPropertyServiceImpl.selectPropertyByMetadataCode(id);
	}

	@RequestMapping("/selectPropertyByClassId")
	@ResponseBody
	public JSONObject selectPropertyByClassId(HttpServletRequest request) {
		String class_id = request.getParameter("class_id");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("class_id", class_id);
		List<Map<String, Object>> list = vMetadataPropertyServiceImpl.selectPropertyByClassId(map);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", list);
		return jsonObject;
	}

	@RequestMapping("/selectProperty/{resultType}")
	@ResponseBody
	public JSONObject selectProperty(@PathVariable String resultType, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			boolean isSelectChildren = false;
			boolean isSelectParent = false;
			switch (resultType) {
			case "c":
				isSelectChildren = true;
				isSelectParent = false;
				break;
			case "p":
				isSelectChildren = false;
				isSelectParent = true;
				break;
			case "a":
				isSelectChildren = true;
				isSelectParent = true;
				break;
			case "i":
				isSelectChildren = false;
				isSelectParent = false;
				break;
			}
			String json = request.getParameter("json");
			Map<String, Object> map = BasicJsonUtil.getInstance().toJsonStringMap(json);
			List<Map<String, Object>> list = vMetadataPropertyServiceImpl.selectProperty(map, isSelectChildren,
					isSelectParent);
			jsonObject.put("code", "suc");
			jsonObject.put("total", list.size());
			jsonObject.put("list", list);
		} catch (IOException e) {
			jsonObject.put("code", "err");
			jsonObject.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jsonObject;
	}

	@RequestMapping("/selectPropertyByResource")
	@ResponseBody
	public JSONObject selectPropertyByResource(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			boolean isSelectChildren = false;
			boolean isSelectParent = false;
			isSelectChildren = true;
			isSelectParent = false;

			String json = request.getParameter("json");
			String token = request.getParameter("token");
			Map<String, Object> map = BasicJsonUtil.getInstance().toJsonStringMap(json);
			List<Map<String, Object>> list = vMetadataPropertyServiceImpl.selectPropertyByResource(map,
					isSelectChildren, isSelectParent, token);
			jsonObject.put("code", "suc");
			jsonObject.put("total", list.size());
			jsonObject.put("list", list);
		} catch (IOException e) {
			jsonObject.put("code", "err");
			jsonObject.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jsonObject;
	}

	/**
	 * 根据元数据代码（metadata_code）查询父级元数据属性信息
	 *
	 * @param request
	 * @return json串
	 */
	@RequestMapping("/queryParentPropertyByMetadataCode")
	@ResponseBody
	public JSONObject queryParentPropertyByMetadataCode(@RequestParam String metadataCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("metadata_code", metadataCode);
		List<Map<String, Object>> list = vMetadataPropertyServiceImpl.selectPropertyByClassId(map);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", list);
		return jsonObject;
	}

	/**
	 * 根据元数据ID或者目录类型查询元数据，返回json串
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/selectMetadataById")
	@ResponseBody
	public Metadata selectMetadataById(HttpServletRequest request) {
		String metadataId = request.getParameter("metadataId");
		try {
			Metadata metadata = metadataServiceImpl.queryMetadataById(metadataId);
			return metadata;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据元数据ID获取code
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/selectMetadataCodeById")
	@ResponseBody
	public Metadata selectMetadataCodeById(HttpServletRequest request) {
		String metadataId = request.getParameter("metadataId");
		try {
			Metadata metadata = metadataServiceImpl.selectMetadataCodeById(metadataId);
			return metadata;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据元数据code和level查询标识（level：p-查上级，i-查本身，c-查子集，a-上级、本身、子集），返回json串
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/selectMetadataJson/{level}", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject selectMetadataJsonById(@PathVariable String level, HttpServletRequest request) {
		String param = request.getParameter("param");
		JSONObject jsonObject = new JSONObject();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map = BasicJsonUtil.getInstance().toJsonStringMap(param);
			List<Map<Object, Object>> list = metadataServiceImpl.selectMetadataMap(map, level);
			jsonObject.put("code", 0);
			jsonObject.put("list", list);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", -2);
		}
		return jsonObject;
	}

	/**
	 * 根据元数据ID列表（String），返回json串
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/selectRelationMetadataJson", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject selectRelationMetadataJson(HttpServletRequest request) {
		String param = request.getParameter("param");
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonArray = metadataServiceImpl.selectRelationMetadataJson(param);
			jsonObject.put("list", jsonArray);
			jsonObject.put("code", "0");
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", "-2");
		}
		return jsonObject;
	}

	@RequestMapping(value = "/selectRelationInfoJson")
	@ResponseBody
	public RetBean selectRelationInfoJson(HttpServletRequest request) {
		String param = request.getParameter("param");
		RetBean retBean = new RetBean();
		try {
			JSONObject reqObject = JSON.parseObject(param);
			retBean = metadataServiceImpl.selectRelationInfoJson(reqObject.getString("ids"),
					PropUtil.getProperty("metadata-db"));
			retBean.setRet_code("0");
		} catch (Exception e) {
			e.printStackTrace();
			retBean.setRet_code("-2");
			retBean.setRet_message(e.getMessage());
		}
		return retBean;
	}

	@RequestMapping(value = "/selectMetadataBySqlBean")
	@ResponseBody
	public RetBean selectMetadataBySqlBean(HttpServletRequest request) {
		String param = request.getParameter("param");
		RetBean retBean = new RetBean();
		try {
			SqlBean sqlBean = CommonUtil.toQueryBean(param, SqlBean.class);
			retBean = metadataServiceImpl.selectMetadataBySqlBean(sqlBean);
			retBean.setRet_code("0");
		} catch (Exception e) {
			e.printStackTrace();
			retBean.setRet_code("-2");
			retBean.setRet_message(e.getMessage());
		}
		return retBean;
	}

}
