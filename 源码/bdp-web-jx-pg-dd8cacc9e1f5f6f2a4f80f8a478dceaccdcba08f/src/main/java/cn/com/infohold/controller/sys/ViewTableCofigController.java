package cn.com.infohold.controller.sys;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.core.BasePage;
import cn.com.infohold.entity.ViewCofig;
import cn.com.infohold.entity.ViewColumnCofig;
import cn.com.infohold.entity.ViewTableCofig;
import cn.com.infohold.service.IViewColumnCofigService;
import cn.com.infohold.service.IViewTableCofigService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-17
 */
@Controller
@RequestMapping("/viewTableCofig")
public class ViewTableCofigController  {
	@Autowired
	IViewTableCofigService viewTableCofigServiceImpl;
	@Autowired
	IViewColumnCofigService viewColumnCofigServiceImpl;

	@RequestMapping("/add")
	@ResponseBody
	public JSONObject add(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			String json = request.getParameter("json");

			viewTableCofigServiceImpl.insertObj(json);
			jsonObject.put("code", "suc");
			jsonObject.put("msg", "增加成功！");
		} catch (Exception e) {
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
			String id = request.getParameter("id");
			JSONObject params = new JSONObject();
			params.put("table_id", id);
			viewTableCofigServiceImpl.delData("2", params.toJSONString());
			viewTableCofigServiceImpl.delData("1", params.toJSONString());
			
			jsonObject.put("code", "suc");
			jsonObject.put("msg", "删除成功！");
		} catch (Exception e) {
			jsonObject.put("code", "err");
			jsonObject.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jsonObject;
	}

	@RequestMapping("/update")
	@ResponseBody
	public JSONObject update(HttpServletRequest request) throws SQLException, Exception {
		JSONObject jsonObject = new JSONObject();
		try {
			String json = request.getParameter("json");
			viewTableCofigServiceImpl.insertObj(json);
			jsonObject.put("code", "suc");
			jsonObject.put("msg", "修改成功！");
		} catch (IOException e) {
			jsonObject.put("code", "err");
			jsonObject.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jsonObject;
	}

	@RequestMapping("/selectAll")
	@ResponseBody
	public List<ViewTableCofig> selectAll(HttpServletRequest request) {
		Map<String, Object> columnMap = new HashMap<String, Object>();
		return viewTableCofigServiceImpl.selectByMap(columnMap);
	}

	@RequestMapping("/selectObjAndPropertie")
	@ResponseBody
	public JSONObject selectObjAndPropertie(HttpServletRequest request) throws Exception {
		JSONObject jsonObject = new JSONObject();
		String tableName = request.getParameter("table");
		jsonObject = viewTableCofigServiceImpl.selectTable(tableName);
		return jsonObject;
	}

	@RequestMapping("/selectObj")
	@ResponseBody
	public ViewCofig selectObj(HttpServletRequest request) throws IOException {
		String id = request.getParameter("id");
		ViewTableCofig tableCofig = viewTableCofigServiceImpl.selectById(id);
		String json = BasicJsonUtil.getInstance().toJsonString(tableCofig);
		ViewCofig cofig = BasicJsonUtil.getInstance().toJavaBean(json, ViewCofig.class);
		Map<String, Object> columnMap = new HashMap<String, Object>();
		columnMap.put("table_id", id);
		List<ViewColumnCofig> list = viewColumnCofigServiceImpl.selectByMap(columnMap);
		Collections.sort(list, new Comparator<ViewColumnCofig>() {
			@Override
			public int compare(ViewColumnCofig o1, ViewColumnCofig o2) {
				 return o1.getOrderIndex().compareTo(o2.getOrderIndex());//升序
			}
		});
		cofig.setColumns(list);
		return cofig;
	}

	@RequestMapping("/selectDataObj")
	@ResponseBody
	public JSONObject selectDataObj(HttpServletRequest request) throws Exception {
		JSONObject jsonObject = new JSONObject();
		JSONObject json = new JSONObject();
		String id = request.getParameter("id");
		json.put("table_id", id);
		List<Map<String, Object>> tableCofig = viewTableCofigServiceImpl.selectData("1", json.toJSONString());
		if (tableCofig.size() > 0) {
			jsonObject.putAll(tableCofig.get(0));
			List<Map<String, Object>> columns = viewTableCofigServiceImpl.selectData("2", json.toJSONString(),
					"order_index");
			jsonObject.put("columns", columns);
		}
		return jsonObject;
	}

	@RequestMapping("/selectData/{table}")
	@ResponseBody
	public JSONObject selectData(@PathVariable String table, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			String json = request.getParameter("json");
			String curPage = request.getParameter("curPage");
			String pageSize = request.getParameter("pageSize");
			BasePage<Map<String, Object>> page = viewTableCofigServiceImpl.selectData(table, json,
					Integer.parseInt(curPage), Integer.parseInt(pageSize));
			jsonObject.put("curPage", curPage);
			jsonObject.put("pageSize", pageSize);
			jsonObject.put("totalCount", page.getTotal());
			jsonObject.put("totalPage", page.getPages());
			jsonObject.put("list", page.getRecords());
		} catch (Exception e) {
			jsonObject.put("code", "err");
			jsonObject.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jsonObject;
	}

	@RequestMapping("/selectDataObj/{table}")
	@ResponseBody
	public JSONObject selectDataObj(@PathVariable String table, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			String json = request.getParameter("json");
			List<Map<String, Object>> list = viewTableCofigServiceImpl.selectDataObj(table, json);
			jsonObject.put("code", "suc");
			jsonObject.put("msg", "增加成功！");
			jsonObject.put("list", list);
		} catch (Exception e) {
			jsonObject.put("code", "err");
			jsonObject.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jsonObject;
	}

	@RequestMapping("/addData/{table}")
	@ResponseBody
	public JSONObject addData(@PathVariable String table, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			String json = request.getParameter("json");
			viewTableCofigServiceImpl.addData(table, json);
			jsonObject.put("code", "suc");
			jsonObject.put("msg", "增加成功！");
		} catch (Exception e) {
			jsonObject.put("code", "err");
			jsonObject.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jsonObject;
	}

	@RequestMapping("/updateData/{table}")
	@ResponseBody
	public JSONObject updateData(@PathVariable String table, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			String json = request.getParameter("json");
			viewTableCofigServiceImpl.updateData(table, json);
			jsonObject.put("code", "suc");
			jsonObject.put("msg", "增加成功！");
		} catch (Exception e) {
			jsonObject.put("code", "err");
			jsonObject.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jsonObject;
	}

	@RequestMapping("/delData/{table}")
	@ResponseBody
	public JSONObject delData(@PathVariable String table, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			String json = request.getParameter("json");
			viewTableCofigServiceImpl.delData(table, json);
			jsonObject.put("code", "suc");
			jsonObject.put("msg", "增加成功！");
		} catch (Exception e) {
			jsonObject.put("code", "err");
			jsonObject.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jsonObject;
	}
}
