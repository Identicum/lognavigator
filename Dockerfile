FROM ghcr.io/identicum/alpine-jdk21-maven AS build

WORKDIR /build
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q package && mv target/lognavigator-*.war target/lognavigator.war


FROM ghcr.io/identicum/alpine-jre21-tomcat11:latest

LABEL org.opencontainers.image.source=https://github.com/Identicum/lognavigator

COPY --from=build /build/target/lognavigator.war /tmp/lognavigator.war

RUN rm -rf $CATALINA_HOME/webapps/ROOT/* && \
    unzip -qq /tmp/lognavigator.war -d $CATALINA_HOME/webapps/ROOT && \
    rm -f /tmp/lognavigator.war

HEALTHCHECK --start-period=5s --timeout=1s --retries=5 --interval=60s CMD curl --fail http://localhost:8080/login --output /dev/null || exit 1
