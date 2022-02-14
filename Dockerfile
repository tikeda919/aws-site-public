FROM maven:3.6 AS aws-site-build

WORKDIR /usr/local/src
RUN git clone https://github.com/tikeda919/aws-site-public.git \
    && cd aws-site-public \
    && mvn package


FROM tomcat:8.5

COPY --from=aws-site-build /usr/local/src/aws-site-public/target/scraping.war /usr/local/tomcat/webapps/
