package cn.com.infohold.service;

import java.io.IOException;
import java.util.Map;

import cn.com.infohold.core.service.IService;
import cn.com.infohold.entity.ServiceUrl;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author huangdi
 * @since 2017-11-16
 */
public interface IServiceUrlService extends IService<ServiceUrl> {

	String selectServuceUrlByCode(String string);

	String post(String serviceName, String method, Map<String, String> multiValueMap) throws IOException;
	
}
