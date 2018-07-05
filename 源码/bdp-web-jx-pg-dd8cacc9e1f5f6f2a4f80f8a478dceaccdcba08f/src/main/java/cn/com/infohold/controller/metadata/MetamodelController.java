package cn.com.infohold.controller.metadata;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.infohold.service.IMetamodelService;
import cn.com.infohold.tools.util.StringUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "MetamodelController")
@Controller
@RequestMapping("/metamodel")
public class MetamodelController  {
	@Autowired
	IMetamodelService metamodelServiceImpl;
	
	@RequestMapping("/add")
	@ResponseBody
	public JSONObject add(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {

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

			jsonObject.put("code", "suc");
			jsonObject.put("msg", "修改成功！");
		} catch (Exception e) {
			jsonObject.put("code", "err");
			jsonObject.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jsonObject;
	}

	@RequestMapping("/queryModelMenu")
	@ResponseBody
	public JSONObject queryModelMenu(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			String NodeId="";
			String NodeType="package";
			String params = request.getParameter("params");
			if (StringUtil.isEmpty(params)) {
				JSONObject json2=JSONObject.parseObject(params);
				
			}
			jsonObject.put("code", "suc");
			jsonObject.put("msg", "成功！");
		} catch (Exception e) {
			jsonObject.put("code", "err");
			jsonObject.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jsonObject;
	}
	
}
