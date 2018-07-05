FROM 192.168.31.72:5000/java:8
RUN mkdir /jar
RUN mkdir /logs
COPY /target/bdp-data-service*.jar /jar/bdp-data-service.jar
RUN bash -c 'touch /jar/bdp-data-service.jar'
# 开放9876端口
EXPOSE 9876

# 配置容器启动后执行的命令
ENTRYPOINT ["java","-Xmx512m","-Djava.security.egd=file:/dev/./urandom","-jar","/jar/bdp-data-service.jar"]