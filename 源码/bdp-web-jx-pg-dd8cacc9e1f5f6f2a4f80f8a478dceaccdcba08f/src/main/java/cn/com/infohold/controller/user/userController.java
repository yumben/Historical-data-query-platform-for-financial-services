package cn.com.infohold.controller.user;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import bdp.commons.dataservice.ret.RetBean;
import cn.com.infohold.basic.util.file.PropUtil;
import cn.com.infohold.service.IViewTableCofigService;
import cn.com.infohold.util.CommonUtil;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequestMapping("/userManager")
public class userController {
	@Autowired
	IViewTableCofigService viewTableCofigServiceImpl;
	
	@RequestMapping(value = "/userQuery",produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String userQuery(HttpServletRequest request) throws IOException {
		JSONObject jsonObject=new JSONObject();
		int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		String json = request.getParameter("param");
		RetBean retBean = null;
		try {
			retBean = viewTableCofigServiceImpl.selectDataByMetadataCode(PropUtil.getProperty("userTable"), json, pageIndex, pageSize);
			jsonObject=CommonUtil.retBeanToJsonObject(retBean);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/roleQuery",produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String roleQuery(HttpServletRequest request) throws IOException {
		JSONObject jsonObject=new JSONObject();
		int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		String json = request.getParameter("param");
		RetBean retBean = null;
		try {
			retBean = viewTableCofigServiceImpl.selectDataByMetadataCode(PropUtil.getProperty("roleTable"), json, pageIndex, pageSize);
			jsonObject=CommonUtil.retBeanToJsonObject(retBean);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonObject.toJSONString();
	}
}
