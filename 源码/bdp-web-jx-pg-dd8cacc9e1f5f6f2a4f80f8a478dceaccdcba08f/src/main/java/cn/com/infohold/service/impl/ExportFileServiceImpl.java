package cn.com.infohold.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import cn.com.infohold.basic.util.file.FileUtil;
import cn.com.infohold.service.IExportFileService;
import lombok.extern.log4j.Log4j2;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author huangdi
 * @since 2017-11-16
 */
@Log4j2(topic = "ExportFileServiceImpl")
@Service
public class ExportFileServiceImpl implements IExportFileService {

	public void exportFile(HttpServletResponse response,
			String[] retPath) throws Exception {
		log.debug(" exportFile  begin");
		OutputStream os = null;
		InputStream inputStream = null;
		try {
			os = response.getOutputStream();
			File exportFile = new File(retPath[1] + retPath[0]);
			inputStream = new FileInputStream(exportFile);
			//循环写入输出流
	        byte[] b = new byte[2048];
	        int length;
	        while ((length = inputStream.read(b)) > 0) {
	            os.write(b, 0, length);
	        }
			log.debug(" exportFile  end");
		} catch (Exception e) {
			log.debug(e);
			throw e;
		} finally {
			if (null != os) {
				os.close();
			}
			if (null != inputStream) {
				inputStream.close();
			}
//			FileUtil.deleteFile(retPath[1] + retPath[0]);
		}

	}

}
