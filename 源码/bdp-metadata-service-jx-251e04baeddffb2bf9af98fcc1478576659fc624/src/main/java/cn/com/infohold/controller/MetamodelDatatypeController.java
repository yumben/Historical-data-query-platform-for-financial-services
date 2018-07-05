package cn.com.infohold.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
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

import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.commons.AppConstants;
import cn.com.infohold.core.controller.BaseController;
import cn.com.infohold.entity.MetamodelDatatype;
import cn.com.infohold.entity.MetamodelPackage;
import cn.com.infohold.service.IMetamodelDatatypeEnumerateService;
import cn.com.infohold.service.IMetamodelDatatypeService;
import cn.com.infohold.service.IMetamodelPackageService;
import cn.com.infohold.tools.util.StringUtil;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@RestController
@RequestMapping("/metamodelDatatype")
public class MetamodelDatatypeController extends BaseController {
	@Autowired
	IMetamodelDatatypeService metamodelDatatypeServiceImpl;
	@Autowired
	IMetamodelPackageService metamodelPackageServiceImpl;
	@Autowired
	IMetamodelDatatypeEnumerateService metamodelDatatypeEnumerateServiceImpl;
	
	@RequestMapping("/add")
	@ResponseBody
	public JSONObject add(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			String json = request.getParameter("json");
			MetamodelDatatype entity = BasicJsonUtil.getInstance().toJavaBean(json, MetamodelDatatype.class);
			metamodelDatatypeServiceImpl.insert(entity);
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
			metamodelDatatypeServiceImpl.deleteById(entity);
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
			MetamodelDatatype entity = BasicJsonUtil.getInstance().toJavaBean(json, MetamodelDatatype.class);
			metamodelDatatypeServiceImpl.insert(entity);
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
	public List<MetamodelDatatype> selectAll(HttpServletRequest request) {
		Map<String, Object> columnMap = new HashMap<String, Object>();
		return metamodelDatatypeServiceImpl.selectByMap(columnMap);
	}

	@RequestMapping("/selectObj")
	@ResponseBody
	public MetamodelDatatype selectObj(HttpServletRequest request) {
		String id = request.getParameter("id");
		return metamodelDatatypeServiceImpl.selectById(id);
	}
	
	
	@RequestMapping(value = "/selectMetamodelDatatype", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String selectMetamodelDatatype(HttpServletRequest request){
		String json = StringUtil.getString(request.getParameter("json"));//json格式的查询条件
//		String callback = StringUtil.getString(request.getParameter("callback"));//跨域请求
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
		JSONObject jsonObject = new JSONObject();
		try {
			MetamodelDatatype entity = JSON.parseObject(json,MetamodelDatatype.class);
			List<MetamodelDatatype> list = metamodelDatatypeServiceImpl.selectMetamodelDatatypeList(entity,pageNo,pageSize);
			jsonObject.put("list", list);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", -2);
		}
		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/saveMetamodelDatatype", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String saveMetamodelDatatype(HttpServletRequest request){
//		String callback = request.getParameter("callback");
		String json = request.getParameter("json");
		MetamodelDatatype entity = JSON.parseObject(json,MetamodelDatatype.class);
		JSONObject jsonObject = new JSONObject();
		try {
			boolean flag = metamodelDatatypeServiceImpl.insertOrUpdate(entity);
			jsonObject.put("flag", flag);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			jsonObject.put("flag", false);
			jsonObject.put("code", -2);
		}
		
		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/getMetamodelDatatypeById", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getMetamodelDatatypeById(HttpServletRequest request){
//		String callback = request.getParameter("callback");
		String id = request.getParameter("id");
		JSONObject jsonObject = new JSONObject();
		try {
			MetamodelDatatype entity = metamodelDatatypeServiceImpl.selectById(id);
			jsonObject.put("entity", entity);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", -2);
		}
		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/delMetamodelDatatypeById", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String delMetamodelDatatypeById(HttpServletRequest request){
//		String callback = request.getParameter("callback");
		String id = request.getParameter("id");
		JSONObject jsonObject = new JSONObject();
		try {
			boolean flag = metamodelDatatypeServiceImpl.deleteById(id);
			jsonObject.put("flag", flag);
		} catch (Exception e) {
			// TODO: handle exception
			jsonObject.put("flag", false);
			jsonObject.put("code", -2);
			e.printStackTrace();
		}
		
		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/ajaxQueryAddInfoDataAction", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String ajaxQueryAddInfoDataAction(HttpServletRequest request){
//		String callback = request.getParameter("callback");
		JSONObject jsonObject = new JSONObject();
		try {
			List<MetamodelDatatype> list = metamodelDatatypeServiceImpl.selectList(null);
			jsonObject.put("list", list);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", -2);
		}
		return jsonObject.toJSONString();
	}
	@RequestMapping(value = "/ajaxQueryModelDataTypeAction", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String ajaxQueryModelDataTypeAction(HttpServletRequest request){
//		String callback = request.getParameter("callback");
		JSONObject jsonObject = new JSONObject();
		String id = request.getParameter("id");
		String queryType = request.getParameter("queryType");
		String datatypeName = request.getParameter("datatypeName");
		String packageId = request.getParameter("packageId");
		String pageNoStr = StringUtil.getString(request.getParameter("pageNo"));
		String pageSizeStr = StringUtil.getString(request.getParameter("pageSize"));
//		String totalCount = "";
//		String totalPage = "";
		String entity = request.getParameter("metamodelDataType");
//		List<MetamodelDatatypeEnumerate> enumerateVOs = new ArrayList<MetamodelDatatypeEnumerate>();
		MetamodelDatatype metamodelDataTypeVO = JSON.parseObject(entity, MetamodelDatatype.class);	
		int pageNo = AppConstants.pageNo;
		if(!"".equals(pageNoStr)){
			pageNo = Integer.parseInt(pageNoStr);
		}
		int pageSize = AppConstants.pageSize;
		if(!"".equals(pageSizeStr)){
			pageSize = Integer.parseInt(pageSizeStr);
		}
		try {
			if (queryType != null && !queryType.equals("")) {
				if (queryType.equals("datatypelist")) {
                    //显示界面列表
					Map<String,String> params = new HashMap<String,String>();
					if(datatypeName!=null&&!datatypeName.equals("")){
						params.put("datatypeName", datatypeName);
					}
					if(packageId!=null&&!packageId.equals("")){
						params.put("packageId", packageId);
					}
					
					jsonObject = metamodelDatatypeServiceImpl.selectMetamodelDatatypeByIdAndCode(params, pageNo+1,pageSize);
					
				}else if(queryType.equals("goaddDataType")){
					//去增加页面  和去修改页面下拉框
					List<MetamodelPackage> list=metamodelPackageServiceImpl.selectList(null);
					jsonObject.put("list", list);
				}else if(queryType.equals("saveAddDataType")){
					//保存增加页面
					metamodelDataTypeVO.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					metamodelDatatypeServiceImpl.insert(metamodelDataTypeVO);
					jsonObject.put("code", 0);
				}else if(queryType.equals("goUpDataType")){
					//去修改页面
					metamodelDataTypeVO=metamodelDatatypeServiceImpl.selectById(id);
					jsonObject.put("metamodelDataTypeVO", metamodelDataTypeVO);
					jsonObject.put("code", 0);
				}else if(queryType.equals("saveUpDataType")){
					//保存修改页面
					metamodelDataTypeVO.setEditDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					metamodelDatatypeServiceImpl.updateById(metamodelDataTypeVO);
					jsonObject.put("code", 0);
				}else if(queryType.equals("delDataType")){
					
					/*	enumerateVOs = metamodelDatatypeEnumerateServiceImpl.selectMetamodelDatatypeEnumerateListBydatatypeId(id);
					if(enumerateVOs.size()>0){
						jsonObject.put("enumerateVOs", enumerateVOs);
					}else{
						metamodelDatatypeServiceImpl.deleteById(id);
						jsonObject.put("enumerateVOs", enumerateVOs);
					}		
					jsonObject.put("code", 0);*/
					
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			jsonObject.put("code", -2);
		}
		return jsonObject.toJSONString();
	}
}
