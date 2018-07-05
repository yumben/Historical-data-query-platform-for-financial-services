package cn.com.infohold.controller;

import java.io.IOException;
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
import cn.com.infohold.entity.MetamodelClassgroup;
import cn.com.infohold.service.IMetamodelClassService;
import cn.com.infohold.service.IMetamodelClassgroupService;
import cn.com.infohold.tools.util.StringUtil;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@Controller
@RequestMapping("/metamodelClassgroup")
public class MetamodelClassgroupController extends BaseController {
	@Autowired
	IMetamodelClassgroupService metamodelClassgroupServiceImpl;
	@Autowired
	IMetamodelClassService metamodelClassServiceImpl;
	@RequestMapping("/add")
	@ResponseBody
	public JSONObject add(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			String json = request.getParameter("json");
			MetamodelClassgroup entity = BasicJsonUtil.getInstance().toJavaBean(json, MetamodelClassgroup.class);
			metamodelClassgroupServiceImpl.insert(entity);
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
			metamodelClassgroupServiceImpl.deleteById(entity);
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
			MetamodelClassgroup entity = BasicJsonUtil.getInstance().toJavaBean(json, MetamodelClassgroup.class);
			metamodelClassgroupServiceImpl.insert(entity);
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
	public List<MetamodelClassgroup> selectAll(HttpServletRequest request) {
		Map<String, Object> columnMap = new HashMap<String, Object>();
		return metamodelClassgroupServiceImpl.selectByMap(columnMap);
	}

	@RequestMapping("/selectObj")
	@ResponseBody
	public MetamodelClassgroup selectObj(HttpServletRequest request) {
		String id = request.getParameter("id");
		return metamodelClassgroupServiceImpl.selectById(id);
	}
	
	@RequestMapping(value = "/selectMetamodelClassgroup", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String selectMetamodelClassgroup(HttpServletRequest request){
		String json = StringUtil.getString(request.getParameter("json"));//json格式的查询条件
		String callback = StringUtil.getString(request.getParameter("callback"));//跨域请求
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
		MetamodelClassgroup entity = JSON.parseObject(json,MetamodelClassgroup.class);
		List<MetamodelClassgroup> list = metamodelClassgroupServiceImpl.selectMetamodelClassgroupList(entity,pageNo,pageSize);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", list);
		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/saveMetamodelClassgroup", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String saveMetamodelClassgroup(HttpServletRequest request){
		String callback = request.getParameter("callback");
		String json = request.getParameter("json");
		MetamodelClassgroup entity = JSON.parseObject(json,MetamodelClassgroup.class);
		boolean flag = metamodelClassgroupServiceImpl.insertOrUpdate(entity);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("flag", flag);
		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/getMetamodelClassgroupById", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getMetamodelClassgroupById(HttpServletRequest request){
		String callback = request.getParameter("callback");
		String id = request.getParameter("id");
		MetamodelClassgroup entity = metamodelClassgroupServiceImpl.selectById(id);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("entity", entity);
		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/delMetamodelClassgroupById", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String delMetamodelClassgroupById(HttpServletRequest request){
		String callback = request.getParameter("callback");
		String id = request.getParameter("id");
		boolean flag = metamodelClassgroupServiceImpl.deleteById(id);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("flag", flag);
		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/ajaxGroupInfoAction", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String ajaxGroupInfoAction(HttpServletRequest request){
		String callback = request.getParameter("callback");
		 String operate =request.getParameter("operate");
		 String id = request.getParameter("id"); //组合类id
	
		
		 String code = request.getParameter("code");
		 String classId = request.getParameter("classId");
		 String classedId = request.getParameter("classedId");
		 String metamodelGroupStr = request.getParameter("metamodelGroupVO");
		 int codeSize = 0;
		 int classedIdSize = 0;
		 MetamodelClassgroup metamodelGroupVO = JSON.parseObject(metamodelGroupStr, MetamodelClassgroup.class);
		JSONObject jsonObject = new JSONObject();
		try {
            if(operate.equals("add")){
            	metamodelClassgroupServiceImpl.insert(metamodelGroupVO);
            	jsonObject.put("code", 0);
            }else if(operate.equals("edit")){
            	metamodelClassgroupServiceImpl.updateById(metamodelGroupVO);
            	jsonObject.put("code", 0);
            }else if(operate.equals("del")){
            	jsonObject = metamodelClassgroupServiceImpl.delMetamodelClassgroupById(id);
            	jsonObject.put("code", 0);
            }else if(operate.equals("queryById")){
            	metamodelGroupVO = new MetamodelClassgroup();
            	
            	metamodelGroupVO = metamodelClassgroupServiceImpl.selectById(id);
            	String classgroupedId = metamodelGroupVO.getClassgroupedId();
				//通过被组合类id查找被组合类的名称和代码
				MetamodelClass metamodelClassVO = metamodelClassServiceImpl.selectById(classgroupedId);
				metamodelGroupVO.setClassCode(metamodelClassVO.getClassCode());
				metamodelGroupVO.setClassName(metamodelClassVO.getClassName());
				jsonObject.put("code", 0);
				jsonObject.put("metamodelGroupVO", metamodelGroupVO);
            }else if(operate.equals("check")){
            	Map<String, Object> codeMap = new HashMap<String, Object>();
            	codeMap.put("relationship_code", code);
            	codeMap.put("class_id", classId);
            	codeSize = metamodelClassgroupServiceImpl.selectByMap(codeMap).size();
            	Map<String, Object> classedMap = new HashMap<String, Object>();
            	classedMap.put("class_id", classId);
            	classedMap.put("classgrouped_id", classedId);
            	classedIdSize = metamodelClassgroupServiceImpl.selectByMap(classedMap).size();
            	jsonObject.put("code", 0);
            	jsonObject.put("codeSize", codeSize);
            	jsonObject.put("classedIdSize",classedIdSize);
            }

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return jsonObject.toJSONString();
	}
	
}
