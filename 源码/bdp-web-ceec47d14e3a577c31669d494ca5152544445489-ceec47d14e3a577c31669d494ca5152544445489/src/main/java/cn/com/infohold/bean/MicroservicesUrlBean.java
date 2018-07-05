package cn.com.infohold.bean;

import cn.com.infohold.basic.util.file.PropUtil;
import lombok.Data;

@Data
public class MicroservicesUrlBean {
	static {
		PropUtil.readProperties("properties/commons.properties");
	}

	private String bdpMetadataService;
	private String bdpDataService;
	private String bdpEasyqueryService;
	
	
	
}
