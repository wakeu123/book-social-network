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
ARG APP_VERSION=1.0.0
ARG MAILING_FRONTEND_URL=http://bsn-ui:8080/activate-account
ARG ARG_DB_URL=jdbc:postgresql://postgres-sql-bsn:5432/book_social_network
WORKDIR /app
COPY --from=build /build/target/book-*.jar /app/
EXPOSE 8088
ENV DB_URL=${ARG_DB_URL}
ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}
ENV MAILING_URL=${MAILING_FRONTEND_URL}
ENV EMAIL_HOSTNAME=smtp.gmail.com
ENV EMAIL_USERNAME=georges
ENV EMAIL_PASSWORD=qbco ghiy xdhv gpog
CMD java -jar -Dapplication.mailing.frontend.activation-url=${MAILING_URL} -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} book-network-${JAR_VERSION}.jar