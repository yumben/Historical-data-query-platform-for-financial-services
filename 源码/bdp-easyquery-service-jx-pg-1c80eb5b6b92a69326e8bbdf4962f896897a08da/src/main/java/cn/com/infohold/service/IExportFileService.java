package cn.com.infohold.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import bdp.commons.easyquery.param.QueryParams;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author huangdi
 * @since 2017-11-16
 */
public interface IExportFileService {

	String geneExportFile(QueryParams queryParams, List<Map<String, Object>> exportList)
			throws Exception;
}
