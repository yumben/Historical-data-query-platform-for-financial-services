package cn.com.infohold.config;

import com.alibaba.druid.pool.DruidDataSource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
 
 @Configuration
 @EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
 public class MysqlDataSourceConfig {
 
     @Value("${spring.datasource.driver-class-name}")
     private String driver;
 
     @Value("${spring.datasource.url}")
     private String url;
 
     @Value("${spring.datasource.username}")
     private String username;
 
     @Value("${spring.datasource.password}")
     private String password;
     
     @Value("${spring.datasource.max-active}")
     private int maxActive; 
 
     @Bean(name="mysqlds")
     public DataSource mysql()
     {
         DruidDataSource ds = new DruidDataSource();
         ds.setUrl(url);
         ds.setDriverClassName(driver);
          ds.setUsername(username);
          ds.setPassword(password); 
          ds.setMaxActive(maxActive);
//         DriverManagerDataSource ds = new DriverManagerDataSource();
//         ds.setDriverClassName(driver);
//         ds.setUrl(url);
//         ds.setUsername(username);
//         ds.setPassword(password); 
         return ds;
     }
 }