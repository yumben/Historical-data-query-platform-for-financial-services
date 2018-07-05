package cn.com.infohold.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {
	@Value("${dialectType}")
	String dialectType;

	public String getDialectType() {
		return dialectType;
	}

	public void setDialectType(String dialectType) {
		this.dialectType = dialectType;
	}
	
}
