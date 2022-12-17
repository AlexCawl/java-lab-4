#
# Package stage
#
FROM openjdk:17-alpine
COPY target/java-lab-4-crawler-1.0.jar /usr/local/lib/web-crawler.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/web-crawler.jar"]