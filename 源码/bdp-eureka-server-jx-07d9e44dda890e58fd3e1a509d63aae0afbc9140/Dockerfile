FROM 192.168.31.72:5000/java:8
RUN mkdir /jar
RUN mkdir /logs
COPY /target/bdp-eureka-server*.jar /jar/eureka.jar
RUN bash -c 'touch /jar/eureka.jar'
# 开放8767端口
EXPOSE 8767

# 配置容器启动后执行的命令
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/jar/eureka.jar"]