FROM amazoncorretto:21

WORKDIR /app

# 로컬 빌드 결과물(JAR)을 이미지로 복사
# 로컬의 원본 파일을 컨테이너 내부에 복사
COPY build/libs/*.jar app.jar
COPY prodEncryptionKey.jks /home/ubuntu/configServer/prodEncryptionKey.jks

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
