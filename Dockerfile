# Build stage
FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests
# Runtime stage
FROM amazoncorretto:18
ARG PROFILE=dev
ARG APP_VERSION=1.0.5
ARG MAILING_FRONTEND_URL=http://bsn-ui:8080/activate-account
WORKDIR /app
COPY --from=build /build/target/book-*.jar /app/
EXPOSE 8090
ENV DB_URL=jdbc:postgresql://postgres-sql-bsn:5432/booK_social_network
ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}
ENV MAILING_URL=${MAILING_FRONTEND_URL}
ENV EMAIL_HOSTNAME=smtp.gmail.com
ENV EMAIL_USERNAME=georgeswakeu21@gmail.com
ENV EMAIL_PASSWORD=qbcoghiyxdhvgpog
CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} book-network-${JAR_VERSION}.jar