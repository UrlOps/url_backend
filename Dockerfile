# --- 1. Build Stage ---
# Gradle과 JDK 17이 설치된 이미지를 빌드 환경으로 사용
FROM gradle:7.6.2-jdk17 AS builder

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 관련 파일 먼저 복사하여 종속성 캐싱 활용
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle
# 종속성 다운로드 (소스코드 변경 시 이 부분은 재실행되지 않음)
RUN ./gradlew dependencies

# 소스코드 전체 복사
COPY src ./src

# 테스트를 제외하고 애플리케이션 빌드
RUN ./gradlew build -x test

# --- 2. Run Stage ---
# 실제 실행에는 JRE만 포함된 가벼운 이미지 사용
FROM openjdk:17-jre-slim

WORKDIR /app

# Build Stage에서 생성된 JAR 파일만 복사
COPY --from=builder /app/build/libs/*.jar /app/application.jar

# 애플리케이션 포트 노출
EXPOSE 8080

# 컨테이너 실행 시 prod 프로파일로 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/application.jar", "--spring.profiles.active=prod"]