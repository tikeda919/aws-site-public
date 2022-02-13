FROM maven:3.6 AS aws-site-build

WORKDIR /usr/local/src
RUN git clone https://github.com/tikeda919/aws-site.git \
    && cd scraping \
    && mvn package


FROM tomcat:8.5

COPY --from=aws-site-build /usr/local/src/scraping/target/scraping.war /usr/local/tomcat/webapps/
