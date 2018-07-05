package cn.com.infohold;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import cn.com.infohold.basic.util.file.PropUtil;

@EnableDiscoveryClient
@SpringBootApplication
@EnableCaching
public class ServiceApplication {
	static {
		PropUtil.readProperties("properties/commons.properties");
	}
	public static void main(String[] args) {
		new SpringApplicationBuilder(ServiceApplication.class).web(true).run(args);
	}

}
