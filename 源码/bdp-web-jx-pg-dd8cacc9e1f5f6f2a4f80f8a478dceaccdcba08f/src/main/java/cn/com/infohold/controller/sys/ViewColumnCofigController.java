package cn.com.infohold.controller.sys;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.entity.ViewColumnCofig;
import cn.com.infohold.service.IViewColumnCofigService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-17
 */
@Controller
@RequestMapping("/viewColumnCofig")
public class ViewColumnCofigController  {
	@Autowired
	IViewColumnCofigService viewColumnCofigServiceImpl;

	@RequestMapping("/add")
	@ResponseBody
	public JSONObject add(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			String json = request.getParameter("json");
			ViewColumnCofig entity = BasicJsonUtil.getInstance().toJavaBean(json, ViewColumnCofig.class);
			viewColumnCofigServiceImpl.insert(entity);
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
			String id = request.getParameter("id");
			viewColumnCofigServiceImpl.deleteById(id);
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
			ViewColumnCofig entity = BasicJsonUtil.getInstance().toJavaBean(json, ViewColumnCofig.class);
			viewColumnCofigServiceImpl.insert(entity);
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
	public List<ViewColumnCofig> selectAll(HttpServletRequest request) {
		Map<String, Object> columnMap = new HashMap<String, Object>();
		return viewColumnCofigServiceImpl.selectByMap(columnMap);
	}

	@RequestMapping("/selectObj")
	@ResponseBody
	public ViewColumnCofig selectObj(HttpServletRequest request) {
		String id = request.getParameter("id");
		return viewColumnCofigServiceImpl.selectById(id);
	}
}
