FROM centos
RUN yum install -y java-11
//RUN yum install -y java-1.8.0-openjdk
ADD target/miracleClient-0.0.1-SNAPSHOT.jar /opt
ENTRYPOINT ["java","-jar","/opt/miracleClient-0.0.1-SNAPSHOT.jar"]
CMD [""]

