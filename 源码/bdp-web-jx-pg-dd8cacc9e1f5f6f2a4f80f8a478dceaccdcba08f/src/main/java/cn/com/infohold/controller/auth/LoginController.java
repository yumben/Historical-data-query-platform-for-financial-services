package cn.com.infohold.controller.auth;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import bdp.commons.authorization.auth.LoginBean;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.bean.AuthDataBean;
import cn.com.infohold.service.IServiceUrlService;
import cn.com.infohold.util.Base64Util;
import cn.com.infohold.util.RSAUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/auth")
public class LoginController {

	@Autowired
	IServiceUrlService serviceUrlServiceImpl;

	@RequestMapping(value = "/login", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String login(HttpServletRequest request) throws IOException {
		JSONObject jsonObject = new JSONObject();
		try {
			String userName = request.getParameter("userName");
			String token = request.getParameter("token");
			String password = request.getParameter("passWord");
			Map<String, String> multiValueMap = new HashMap<String, String>();
			LoginBean loginBean = new LoginBean();
			loginBean.setUserName(userName);
			loginBean.setPassword(password);
			multiValueMap.put("param", BasicJsonUtil.getInstance().toJsonString(loginBean));
			String text = serviceUrlServiceImpl.post("bdp-authorization-service", "login", multiValueMap);
			jsonObject = JSONObject.parseObject(text);
			if (jsonObject.getString("loginStatus").equals("0")) {
				jsonObject.put("code", "suc");
				jsonObject.put("msg", "登录成功");
			} else {
				jsonObject.put("code", "err");
			}

		} catch (Exception e) {
			jsonObject.put("code", "err");
			jsonObject.put("msg", e.getMessage());
			e.printStackTrace();
		}

		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/turnChange", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public void turnChange(HttpServletRequest request,HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("UTF-8");
		JSONObject jsonObject = new JSONObject();
		try {
			String dataJson = request.getParameter("dataEncode");
			String systemCode = request.getParameter("systemCode");
			Map<String, String> multiValueMap = new HashMap<String, String>();
			LoginBean loginBean = new LoginBean();
			multiValueMap.put("param", BasicJsonUtil.getInstance().toJsonString(loginBean));
			AuthDataBean authDataBean = new AuthDataBean();
			authDataBean.setDataEncode(dataJson);
			authDataBean.setSystemCode(systemCode);
			multiValueMap.put("dataJson", BasicJsonUtil.getInstance().toJsonString(authDataBean));
			String text = serviceUrlServiceImpl.post("bdp-authorization-service", "getReFresh", multiValueMap);
			jsonObject = JSONObject.parseObject(text);
			if (jsonObject.getString("loginStatus").equals("0")) {
				jsonObject.put("code", "suc");
				jsonObject.put("msg", "登录成功");		
				Cookie tokenC = new Cookie("token", jsonObject.get("token").toString());
				tokenC.setPath("/");
				response.addCookie(tokenC);
				Cookie userNameC = new Cookie("userName", jsonObject.get("userName").toString());
				userNameC.setPath("/");
				response.addCookie(userNameC);
				Cookie userCNNameC = new Cookie("userCNName", URLEncoder.encode(jsonObject.get("userCNName").toString()));
				userCNNameC.setPath("/");
				response.addCookie(userCNNameC);		
				response.addHeader("P3P","CP='IDCDSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
				response.addHeader("P3P","CP=\"CURaADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSPCOR\"");				
				response.sendRedirect("/bdp-web/easyquery/indexnohead.html");
			} else {
				jsonObject.put("code", "err");
				jsonObject.put("msg", "跳转失败");
				response.sendRedirect("/bdp-web/error/noteller.html");	
			}
		} catch (Exception e) {
			jsonObject.put("code", "err");
			jsonObject.put("msg", e.getMessage());
			e.printStackTrace();
		}	
	//	return jsonObject.toJSONString();
	}

	@RequestMapping(value = "/turn", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public void turn(HttpServletRequest request,HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("UTF-8");
		String userName = request.getParameter("userName");
		String systemCode = request.getParameter("systemCode");	
		Map<String, String> multiValueMap1 = new HashMap<String, String>();
		multiValueMap1.put("systemCode", systemCode);
		String publicKey = serviceUrlServiceImpl.post("bdp-authorization-service", "getPublicKey", multiValueMap1);
		JSONObject jsonObject = new JSONObject();
		try {
			byte[] encodedData = RSAUtil.encrypt(userName.getBytes(), publicKey, true);
			String strData = Base64Util.encryptBASE64(encodedData);
			Map<String, String> multiValueMap = new HashMap<String, String>();
			LoginBean loginBean = new LoginBean();
			loginBean.setUserName(userName);
			multiValueMap.put("param", BasicJsonUtil.getInstance().toJsonString(loginBean));
			AuthDataBean authDataBean = new AuthDataBean();
			authDataBean.setUserId(userName);
			authDataBean.setDataEncode(strData);
			authDataBean.setSystemCode(systemCode);
			multiValueMap.put("dataJson", BasicJsonUtil.getInstance().toJsonString(authDataBean));
			String text = serviceUrlServiceImpl.post("bdp-authorization-service", "getReFresh", multiValueMap);	
			jsonObject = JSONObject.parseObject(text);
			if (jsonObject.getString("loginStatus").equals("0")) {
				jsonObject.put("code", "suc");
				jsonObject.put("msg", "登录成功");	
				Cookie tokenC = new Cookie("token", jsonObject.get("token").toString());
				tokenC.setPath("/");
				response.addCookie(tokenC);
				Cookie userNameC = new Cookie("userName", jsonObject.get("userName").toString());
				userNameC.setPath("/");
				response.addCookie(userNameC);
				Cookie userCNNameC = new Cookie("userCNName", URLEncoder.encode(jsonObject.get("userCNName").toString()));
				userCNNameC.setPath("/");
				response.addCookie(userCNNameC);
				response.addHeader("P3P","CP='IDCDSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
				response.addHeader("P3P","CP=\"CURaADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSPCOR\"");
				response.sendRedirect("/bdp-web/easyquery/indexnohead.html");
			} else {
				jsonObject.put("code", "err");
				jsonObject.put("msg", "跳转失败");
				response.sendRedirect("/bdp-web/error/noteller.html");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 首先是登录页面
	@RequestMapping(value = "/loginPage", method = { RequestMethod.GET })
	public String loginPage(HttpServletRequest request, String account, String password) {
		return "/views/html/login/login";
	}

	@RequestMapping(value = "/loginSuccess", method = { RequestMethod.GET })
	public String accusationPage(HttpServletRequest request) {
		return "/workbench/index";
	}

	// 主动登出的时候使用
	@RequestMapping(value = "/logOut", method = { RequestMethod.GET })
	public String loginOut(HttpServletRequest request, HttpServletResponse response) {
		return "redirect:loginPage";
	}

}
