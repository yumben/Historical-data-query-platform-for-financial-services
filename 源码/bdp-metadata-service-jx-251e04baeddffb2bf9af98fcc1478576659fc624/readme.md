# 使用readme.md文件介绍项目和安装部署

#1、修改配置文件root/resources/bootstrap.properties，指向配置对应的配置文件：
spring.cloud.config.profile=new-pgsql

#2、配置中心配置对应的配置文件bdp-metadata-service-new-pgsql.properties：
#配置dao层的log输出(控制台打印出sql语句及参数、结果信息，推荐配置,红色为路径)
logging.level.cn.com.infohold.dao= trace
#debug出项目信息
logging.level.com.xuanke=debug
# jdbc_config
spring.datasource.url=jdbc:postgresql://192.168.31.251:5432/bdp_basic
spring.datasource.username=postgres
spring.datasource.password=lifanhong
spring.datasource.driver-class-name=org.postgresql.Driver
dialectType=postgresql
#druid_config
spring.datasource.max-active: 20
spring.datasource.initial-size: 1
spring.datasource.min-idle: 3
spring.datasource.max-wait: 60000
spring.datasource.time-between-eviction-runs-millis: 60000
spring.datasource.min-evictable-idle-time-millis: 300000
spring.datasource.test-while-idle: true
spring.datasource.test-on-borrow: false
spring.datasource.test-on-return: false
spring.datasource.poolPreparedStatements: true



# mybatis_config
mybatis.mapper-locations=classpath:mybatis/*Mapper.xml 
mybatis.typeAliasesPackage=cn.com.infohold.entity
3、重启
