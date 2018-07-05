package cn.com.infohold.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.com.infohold.basic.util.file.PropUtil;
import cn.com.infohold.core.service.impl.ServiceImpl;
import cn.com.infohold.dao.IServiceUrlDao;
import cn.com.infohold.entity.ServiceUrl;
import cn.com.infohold.service.IServiceUrlService;
import cn.com.infohold.tools.util.StringUtil;
import cn.easybdp.basic.util.http.BasicHttpUtil;
import lombok.extern.log4j.Log4j2;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author huangdi
 * @since 2017-11-16
 */
@Log4j2(topic = "ServiceUrlServiceImpl")
@Service
public class ServiceUrlServiceImpl extends ServiceImpl<IServiceUrlDao, ServiceUrl> implements IServiceUrlService {

	private static final BasicHttpUtil BHU = BasicHttpUtil.getInstance();

	@Override
	public String selectServuceUrlByCode(String urlCode) {
		String url = "";
		try {
			if (urlCode != null && !"".equals(urlCode)) {
				if (StringUtil.isEmpty(PropUtil.getProperty(urlCode))) {
					Map<String, Object> columnMap = new HashMap<String, Object>();
					columnMap.put("url_code", urlCode);
					List<ServiceUrl> list = selectByMap(columnMap);
					if (list != null && list.size() > 0) {
						url = list.get(0).getUrlValue();
						PropUtil.setProperty(urlCode, url);
					}
				} else {
					url = PropUtil.getProperty(urlCode);
				}
			}
		} catch (Exception e) {
		}
		return url;
	}

	@Override
	public String post(String serviceName, String method, Map<String, String> multiValueMap) throws IOException {
		String url = selectServuceUrlByCode(serviceName);
		url += method;
		log.debug("请求url={}", url);
//		log.debug("请求json={}", multiValueMap.toString());
		String str = null;
		try {
			str = BHU.postRequst(url, multiValueMap);
		} catch (Exception ex) {
			log.error("ex={}", ex);
			log.error("str={}", str);
			throw ex;
		}
		return str;
	}

}
