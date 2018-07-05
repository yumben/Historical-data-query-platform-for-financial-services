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

	void exportFile(HttpServletResponse response, String[] retPath)
			throws Exception;

//	void exportFile(String filePath,String fileName ,HttpServletResponse response)
//			throws Exception;
	// SXSSFWorkbook geneSXSSFWorkbook(List<QueryFields> fields,
	// List<Map<String, Object>> exportList) throws Exception;
}
