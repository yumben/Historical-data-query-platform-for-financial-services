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

import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.commons.AppConstants;
import cn.com.infohold.core.controller.BaseController;
import cn.com.infohold.entity.MetamodelClass;
import cn.com.infohold.entity.MetamodelPackage;
import cn.com.infohold.service.IMetamodelClassService;
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
@Controller
@RequestMapping("/metamodelPackage")
public class MetamodelPackageController extends BaseController {
	@Autowired
	IMetamodelPackageService metamodelPackageServiceImpl;
	@Autowired
	IMetamodelClassService metamodelClassServiceImpl;
	@RequestMapping("/add")
	@ResponseBody
	public JSONObject add(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			String json = request.getParameter("json");
			MetamodelPackage entity = BasicJsonUtil.getInstance().toJavaBean(json, MetamodelPackage.class);
			metamodelPackageServiceImpl.insert(entity);
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
			metamodelPackageServiceImpl.deleteById(entity);
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
			MetamodelPackage entity = BasicJsonUtil.getInstance().toJavaBean(json, MetamodelPackage.class);
			metamodelPackageServiceImpl.insert(entity);
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
	public List<MetamodelPackage> selectAll(HttpServletRequest request) {
		Map<String, Object> columnMap = new HashMap<String, Object>();
		return metamodelPackageServiceImpl.selectByMap(columnMap);
	}

	@RequestMapping("/selectObj")
	@ResponseBody
	public MetamodelPackage selectObj(HttpServletRequest request) {
		String id = request.getParameter("id");
		return metamodelPackageServiceImpl.selectById(id);
	}
	
	
	@RequestMapping(value = "/selectMetamodelPackage", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String selectMetamodelPackage(HttpServletRequest request){
		String json = StringUtil.getString(request.getParameter("json"));//json格式的查询条件
		String callback = StringUtil.getString(request.getParameter("callback"));//跨域请求
		String pageNoStr = StringUtil.getString(request.getParameter("pageNo"));
		String pageSizeStr = StringUtil.getString(request.getParameter("pageSize"));
		JSONObject jsonObject = new JSONObject();
		try {
			int pageNo = AppConstants.pageNo;
			if(!"".equals(pageNoStr)){
				pageNo = Integer.parseInt(pageNoStr);
			}
			int pageSize = AppConstants.pageSize;
			if(!"".equals(pageSizeStr)){
				pageSize = Integer.parseInt(pageSizeStr);
			}
			MetamodelPackage entity = JSON.parseObject(json,MetamodelPackage.class);
			List<MetamodelPackage> list = metamodelPackageServiceImpl.selectMetamodelPackageList(entity,pageNo,pageSize);
			jsonObject.put("list", list);
			jsonObject.put("code", 0);
		} catch (Exception e) {
			// TODO: handle exception
			jsonObject.put("code", -2);
			e.printStackTrace();
		}
		
		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/saveMetamodelPackage", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String saveMetamodelPackage(HttpServletRequest request){
		String callback = request.getParameter("callback");
		String json = request.getParameter("metamodelPackage");
		JSONObject jsonObject = new JSONObject();
		try {
			MetamodelPackage entity = JSON.parseObject(json,MetamodelPackage.class);
			boolean flag = metamodelPackageServiceImpl.insertOrUpdate(entity);
			jsonObject.put("flag", flag);
			jsonObject.put("code", 0);
		} catch (Exception e) {
			// TODO: handle exception
			jsonObject.put("code", -2);
			e.printStackTrace();
		}
		
		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/getMetamodelPackageById", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getMetamodelPackageById(HttpServletRequest request){
		JSONObject jsonObject = new JSONObject();
		String callback = request.getParameter("callback");
		try {
			String id = request.getParameter("id");
			MetamodelPackage entity = metamodelPackageServiceImpl.selectById(id);
			
			jsonObject.put("metamodelPackageVO", entity);
			jsonObject.put("code", 0);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", -2);
		}
		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/delMetamodelPackageById", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String delMetamodelPackageById(HttpServletRequest request){
		String callback = request.getParameter("callback");
		String id = request.getParameter("id");
		JSONObject jsonObject = new JSONObject();
		try {
			boolean flag = metamodelPackageServiceImpl.deleteById(id);
			jsonObject.put("flag", flag);
			jsonObject.put("code", 0);
		} catch (Exception e) {
			// TODO: handle exception
			jsonObject.put("code", -2);
			e.printStackTrace();
		}
		
		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/queryModelPackage", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String queryModelPackage(HttpServletRequest request){
		String callback = request.getParameter("callback");
		JSONObject jsonObject = new JSONObject();
		try {
			List<MetamodelPackage> list= metamodelPackageServiceImpl.selectList(null);
			jsonObject.put("list", list);
			jsonObject.put("code", 0);
		} catch (Exception e) {
			// TODO: handle exception
			jsonObject.put("code", -2);
		}
		
		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/savePackage", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String savePackage(HttpServletRequest request){
		String callback = request.getParameter("callback");
		JSONObject jsonObject = new JSONObject();
		String operate = request.getParameter("operate");
		String id = request.getParameter("id");
		List<MetamodelPackage>  listcode = new ArrayList<MetamodelPackage>();
		List<MetamodelClass>  listcode2 = new ArrayList<MetamodelClass>();
		String code="";
		String json = request.getParameter("metamodelPackage");
		MetamodelPackage metamodelPackageVO = JSON.parseObject(json,MetamodelPackage.class);
		try {
            if(operate.equals("add") || operate.equals("edit")){
    			metamodelPackageVO.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    			if ("".equals(metamodelPackageVO.getParentId())) {
    				metamodelPackageVO.setParentId(null);
				}
    			metamodelPackageServiceImpl.insertOrUpdate(metamodelPackageVO);
    		}else if(operate.equals("del")){
    			Map<String,Object> map = new HashMap<String,Object>();
    			map.put("package_id", id);
    			listcode2=metamodelClassServiceImpl.selectList(map);			//查询子类
            	listcode=metamodelPackageServiceImpl.queryModelPackageByParentId(id);		//查询子包
            	if(listcode2.size()==0&&listcode.size()==0){
            		metamodelPackageServiceImpl.deleteById(id);
            	}else{
            		listcode = listcode;
            		listcode2 = listcode2;
            	}
            	
            }else if(operate.equals("check")){
            	Map<String,Object> map = new HashMap<String,Object>();
            	map.put("package_code", code);
            	listcode=metamodelPackageServiceImpl.selectList(map);
            	
            }
            jsonObject.put("listcode2", listcode2);
            jsonObject.put("listcode", listcode);
            jsonObject.put("code", 0);
		} catch (Exception ex) {
			ex.printStackTrace();
			jsonObject.put("code", -2);
		}
		
		return jsonObject.toJSONString();
	}
	
}
