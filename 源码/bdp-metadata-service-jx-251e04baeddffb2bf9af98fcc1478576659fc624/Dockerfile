FROM 192.168.31.72:5000/java:8
RUN mkdir /jar
RUN mkdir /logs
COPY /target/bdp-metadata-service*.jar /jar/bdp-metadata-service.jar
RUN bash -c 'touch /jar/bdp-metadata-service.jar'
# 开放8793端口
EXPOSE 8793

# 配置容器启动后执行的命令
ENTRYPOINT ["java","-Xmx512m","-Djava.security.egd=file:/dev/./urandom","-jar","/jar/bdp-metadata-service.jar"]