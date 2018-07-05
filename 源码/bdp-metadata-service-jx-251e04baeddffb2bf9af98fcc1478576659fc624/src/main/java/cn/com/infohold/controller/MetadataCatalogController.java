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
import cn.com.infohold.entity.MetadataCatalog;
import cn.com.infohold.service.IMetadataCatalogService;
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
@RequestMapping("/metadataCatalog")
public class MetadataCatalogController extends BaseController {
	@Autowired
	IMetadataCatalogService metadataCatalogServiceImpl;

	@RequestMapping("/saveMetadataCatalog")
	@ResponseBody
	public JSONObject add(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			String json = request.getParameter("vo");
			BasicJsonUtil basicJsonUtil = BasicJsonUtil.getInstance();
			MetadataCatalog entity = basicJsonUtil.toJavaBean(json, MetadataCatalog.class);
			if ("0".equals(entity.getParentCatalog())) {
				entity.setParentCatalog(null);
			}
			metadataCatalogServiceImpl.insert(entity);
			jsonObject.put("code", "0");
			jsonObject.put("msg", "增加成功！");
		} catch (IOException e) {
			jsonObject.put("code", "-2");
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
			metadataCatalogServiceImpl.deleteById(entity);
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
			MetadataCatalog entity = BasicJsonUtil.getInstance().toJavaBean(json, MetadataCatalog.class);
			metadataCatalogServiceImpl.insert(entity);
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
	public List<MetadataCatalog> selectAll(HttpServletRequest request) {
		Map<String, Object> columnMap = new HashMap<String, Object>();
		return metadataCatalogServiceImpl.selectByMap(columnMap);
	}

	@RequestMapping("/selectObj")
	@ResponseBody
	public MetadataCatalog selectObj(HttpServletRequest request) {
		String id = request.getParameter("id");
		return metadataCatalogServiceImpl.selectById(id);
	}
	
	@RequestMapping(value = "/getMetadataCatalogById", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getMetadataCatalogById(HttpServletRequest request){
		String callback = request.getParameter("callback");
		String id = request.getParameter("id");
		JSONObject jsonObject = new JSONObject();
		try {
			MetadataCatalog entity = metadataCatalogServiceImpl.selectMetadataCatalogByCatalogId(id);
			jsonObject.put("vo", entity);
			jsonObject.put("code", 0);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", -2);
		}
		
		return jsonObject.toJSONString();
	}
	@RequestMapping(value = "/selectMetadataCatalog", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String selectMetadataCatalog(HttpServletRequest request){
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
			MetadataCatalog entity = JSON.parseObject(json,MetadataCatalog.class);
			List<MetadataCatalog> list = metadataCatalogServiceImpl.selectMetadataCatalogList(entity,pageNo,pageSize);
			jsonObject.put("list", list);
			jsonObject.put("code", 0);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", -2);
		}
		
		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/saveMetadataCatalog", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String saveMetadataCatalog(HttpServletRequest request){
		String callback = request.getParameter("callback");
		String json = request.getParameter("vo");
		JSONObject jsonObject = new JSONObject();
		try {
			MetadataCatalog entity = JSON.parseObject(json,MetadataCatalog.class);
			if ("".equals(entity.getParentCatalog()) || entity.getParentCatalog().length()==0 || "0".equals(entity.getParentCatalog())) {
				entity.setParentCatalog(null);
			}
			boolean flag = metadataCatalogServiceImpl.insertOrUpdate(entity);
			if (flag) {
				jsonObject.put("code", 0);
				jsonObject.put("msg", "操作成功");
			}else {
				jsonObject.put("code", -1);
				jsonObject.put("msg", "操作失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", -2);
		}
		
		
		return jsonObject.toJSONString();
	}
	
	
	@RequestMapping(value = "/delMetadataCatalogById", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String delMetadataCatalogById(HttpServletRequest request){
		String callback = request.getParameter("callback");
		String id = request.getParameter("id");
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = metadataCatalogServiceImpl.delMetadataCatalogByCatalogId(id);
			
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", -2);
		}
		
		return jsonObject.toJSONString();
	}
}
