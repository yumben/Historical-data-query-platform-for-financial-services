package cn.com.infohold.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bdp.commons.authorization.auth.LoginBean;
import bdp.commons.dataservice.ret.RetBean;
import cn.com.infohold.bean.AuthDataBean;
import cn.com.infohold.service.auth.ILoginService;
import cn.com.infohold.util.AnalysisUtil;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class LoginController {

	@Autowired
	ILoginService loginService;

//	@Value("${isFilter}")
//	String isFilter;

	@RequestMapping(value = "/login")
	public LoginBean login(@RequestParam String param) throws Exception {
		LoginBean loginBean = AnalysisUtil.toJavaBean(param, LoginBean.class);
		try {
			loginBean = loginService.login(loginBean);
		} catch (Exception ex) {
			loginBean.setLoginStatus("-1");
			log.error(ex);
			throw ex;
		}
		return loginBean;
	}
	
	//接收页面传输参数部分
	@RequestMapping(value = "/getReFresh")
	public LoginBean getReFresh(@RequestParam String param,String dataJson) throws Exception {
		AuthDataBean authDataBean = AnalysisUtil.toJavaBean(dataJson, AuthDataBean.class);
		LoginBean loginBean = AnalysisUtil.toJavaBean(param, LoginBean.class);
		try {
			loginBean = loginService.logByRsa(loginBean, authDataBean);
		} catch (Exception ex) {
			loginBean.setLoginStatus("-1");
			log.error(ex);
			throw ex;
		}
		return loginBean;
	}

	@RequestMapping(value = "/getPublicKey")
	public String getPublicKey(@RequestParam String systemCode) throws Exception {
		String publicKey;
		try {
			publicKey = loginService.getKeyMap(systemCode);
		} catch (Exception ex) {
			log.error(ex);
			throw ex;
		}
		return publicKey;
	}
	
	@RequestMapping(value = "/getUser")
	public LoginBean getUser(@RequestParam String param) throws Exception {
		LoginBean loginBean = AnalysisUtil.toJavaBean(param, LoginBean.class);
		try {
			loginBean = loginService.getUser(loginBean);
		} catch (Exception ex) {
			loginBean.setLoginStatus("-1");
			log.error(ex);
			throw ex;
		}
		return loginBean;
	}

	@RequestMapping(value = "/getUnitList")
	public RetBean getUnitList(@RequestParam String param) throws Exception {
		RetBean ret = new RetBean();
		LoginBean loginBean = AnalysisUtil.toJavaBean(param, LoginBean.class);
		try {
			ret = loginService.getUnitList(loginBean);
		} catch (Exception ex) {
			ret.setRet_code("-1");
			ret.setRet_message(ex.getLocalizedMessage());
			log.error(ex);
			throw ex;
		}
		return ret;
	}

	@RequestMapping(value = "/getToken")
	public RetBean getToken(@RequestParam String param) throws Exception {
		RetBean ret = new RetBean();
		try {
			ret = loginService.getToken(param);
		} catch (Exception ex) {
			ret.setRet_code("-1");
			ret.setRet_message(ex.getLocalizedMessage());
			log.error(ex);
			throw ex;
		}
		return ret;
	}

}
