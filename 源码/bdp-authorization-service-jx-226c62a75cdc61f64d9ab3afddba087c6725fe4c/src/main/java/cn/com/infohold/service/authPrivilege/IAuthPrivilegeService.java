package cn.com.infohold.service.authPrivilege;

import java.io.IOException;

import bdp.commons.authorization.auth.AuthPrivilegesBean;
import bdp.commons.dataservice.ret.RetBean;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-08-03
 */
public interface IAuthPrivilegeService {

	
	RetBean authAdd(AuthPrivilegesBean authPrivilegesBean) throws IOException, IllegalArgumentException, IllegalAccessException;
	RetBean authQuery(String privilege_master_id) throws IOException;
	
}
