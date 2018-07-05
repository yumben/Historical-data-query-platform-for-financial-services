FROM 192.168.31.72:5000/java:8
RUN mkdir /jar
RUN mkdir /logs
COPY /target/bdp-easyquery-service*.jar /jar/bdp-easyquery-service.jar
RUN bash -c 'touch /jar/bdp-easyquery-service.jar'
# 开放9898端口
EXPOSE 9898

# 配置容器启动后执行的命令
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/jar/bdp-easyquery-service.jar"]