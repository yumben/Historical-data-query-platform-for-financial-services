FROM 192.168.31.72:5000/tomcat:7-jre8-utf8-conf-jconsole-dc72
RUN rm -rf /usr/local/tomcat/webapps/bdp-web
COPY target/*.war /usr/local/tomcat/webapps/bdp-web.war
CMD ["catalina.sh", "run"]