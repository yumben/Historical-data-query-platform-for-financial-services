package cn.com.infohold.service.auth;

import java.util.List;
import java.util.Map;

import bdp.commons.authorization.auth.LoginBean;
import bdp.commons.dataservice.ret.RetBean;
import cn.com.infohold.bean.AuthDataBean;

public interface ILoginService {

	LoginBean login(LoginBean loginBean) throws Exception;
	
	List<Map<String, Object>> selectUsers(String userName)throws Exception;

	LoginBean getUser(LoginBean loginBean);

	RetBean getUnitList(LoginBean loginBean);

	RetBean getToken(String token);
	
	public LoginBean logByRsa(LoginBean loginBean,AuthDataBean authDataBean) throws Exception;
	
	public String getKeyMap(String systemCode) throws Exception;
}
