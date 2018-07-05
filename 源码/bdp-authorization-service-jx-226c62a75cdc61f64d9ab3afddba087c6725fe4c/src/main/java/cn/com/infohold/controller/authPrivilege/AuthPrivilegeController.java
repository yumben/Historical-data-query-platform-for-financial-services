package cn.com.infohold.controller.authPrivilege;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bdp.commons.authorization.auth.AuthPrivilegesBean;
import bdp.commons.dataservice.ret.RetBean;
import cn.com.infohold.service.authPrivilege.IAuthPrivilegeService;
import cn.com.infohold.util.AnalysisUtil;
import lombok.extern.log4j.Log4j2;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author mojiaxing
 * @since 2017-08-03
 */
@RestController
@Log4j2
public class AuthPrivilegeController {
	@Autowired
	IAuthPrivilegeService authPrivilegeServiceImpl;

	@RequestMapping(value = "/authAdd")
	public RetBean authAdd(@RequestParam String param) throws Exception {
		AuthPrivilegesBean authPrivilegesBean = AnalysisUtil.toJavaBean(param, AuthPrivilegesBean.class);
		RetBean ret = authPrivilegeServiceImpl.authAdd(authPrivilegesBean);
		try {
		} catch (Exception ex) {
			ret.setRet_code("-1");
			ret.setRet_message(ex.getLocalizedMessage());
			log.error(ex);
			throw ex;
		}
		return ret;
	}

	@RequestMapping(value = "/authQuery")
	public RetBean authQuery(@RequestParam String privilege_master_id) throws Exception {
		log.debug("authQuery开始调用==" + privilege_master_id);
		RetBean ret = new RetBean();
		try {
			ret = authPrivilegeServiceImpl.authQuery(privilege_master_id);
		} catch (Exception ex) {
			ret.setRet_code("-1");
			ret.setRet_message(ex.getLocalizedMessage());
			log.error(ex);
			throw ex;
		}
		return ret;
	}

	@RequestMapping(value = "/authQueryAll")
	public RetBean authQueryAll(@RequestParam String param) throws Exception {
		RetBean ret = new RetBean();
		try {
		} catch (Exception ex) {
			ret.setRet_code("-1");
			ret.setRet_message(ex.getLocalizedMessage());
			log.error(ex);
			throw ex;
		}
		return ret;
	}

}
