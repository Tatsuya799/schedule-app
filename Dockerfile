# 1. 道具が全部入っている大きな箱（Maven）を借りる
FROM maven:3.9.5-eclipse-temurin-17 AS build
COPY . .
# 隠しフォルダを使わず、直接 mvn コマンドを実行する
RUN mvn clean package -DskipTests

# 2. 実行用の軽い箱に移し替える
FROM eclipse-temurin:17-jdk
COPY --from=build /target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","/app.jar"]