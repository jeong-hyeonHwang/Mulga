# Gitlab 소스 클론 이후 빌드 및 배포할 수 있도록 정리한 문서 마감

상태: 완료
영역: 포팅 관련

# 목차

# 서버 환경

- ubuntu
    
    ```
    PRETTY_NAME="Ubuntu 22.04.4 LTS"
    NAME="Ubuntu"
    VERSION_ID="22.04"
    VERSION="22.04.4 LTS (Jammy Jellyfish)"
    VERSION_CODENAME=jammy
    ID=ubuntu
    ID_LIKE=debian
    HOME_URL="https://www.ubuntu.com/"
    SUPPORT_URL="https://help.ubuntu.com/"
    BUG_REPORT_URL="https://bugs.launchpad.net/ubuntu/"
    PRIVACY_POLICY_URL="https://www.ubuntu.com/legal/terms-and-policies/privacy-policy"
    UBUNTU_CODENAME=jammy
    ```
    
- docker
    
    ```
    Client: Docker Engine - Community
     Version:    28.0.2
     Context:    default
     Debug Mode: false
     Plugins:
      buildx: Docker Buildx (Docker Inc.)
        Version:  v0.21.1
        Path:     /usr/libexec/docker/cli-plugins/docker-buildx
      compose: Docker Compose (Docker Inc.)
        Version:  v2.33.1
        Path:     /usr/libexec/docker/cli-plugins/docker-compose
    ```
    
- jenkins/2.502
    
    ```
    Jenkins 관리 - Plugins
    - 없을 시 좌측 Available plugins에서 Gitlab, Docker Pipeline, Git 플러그인 설치
    
    Jenkins 관리 - Tools
    - JDK installations - Add JDK
    -- Name: jdk21
    -- Install automatically 체크
    -- Download URL for binary archive: https://corretto.aws/downloads/latest/amazon-corretto-21-x64-linux-jdk.tar.gz
    -- Subdirectory of extracted archive: amazon-corretto-21.0.6.7.1-linux-x64
    - Gradle instllations - Add Gradle
    -- name: gradle
    -- Install automatically 체크
    -- Version: 8.12.1
    
    Jenkins 관리 - Credentials - System - Global credentials (unrestricted)
    - Kind: Username with password
    -- Scope: Global (Jenkins, nodes, items, all child items, etc)
    -- Username: GitLab 아이디
    -- Password: GitLab 비밀번호
    -- ID: GitLab
    -- Create
    - (이하 Config 서버 배포 후 진행)
    - Kind: Secret file
    -- Scope: Global (Jenkins, nodes, items, all child items, etc)
    -- File: config-server.crt 업로드 (Config 서버에 브라우저로 접속 후 주소창 좌측의 자물쇠 클릭하여 인증서 다운로드)
    -- ID: ConfigSSL
    -- Create
    ```
    
- java
    
    ```
    openjdk version "21.0.6" 2025-01-21
    OpenJDK Runtime Environment (build 21.0.6+7-Ubuntu-122.04.1)
    OpenJDK 64-Bit Server VM (build 21.0.6+7-Ubuntu-122.04.1, mixed mode, sharing)
    ```
    

# 빌드 시 사용되는 환경변수

- 백엔드
    
    
    | ID | Name |
    | --- | --- |
    | spring.application.name | 이름 |
    | spring.config.import | config server의 주소 |
    | spring.profiles.active | 현재 활성화할 프로파일 |
    
    ```json
    spring:
      application:
        name: mug-back
      config:
        import: configserver:CONFIG_SERVER_URL_PLACEHOLDER
      profiles:
        active: dev
    ```
    
    - CONFIG_SERVER_URL_PLACEHOLDER 환경변수 값은 Jenkins에서 주입
- 설정 서버
    
    
    | ID | Name |
    | --- | --- |
    | server.port | 서버 포트 |
    | server.ssl.key-store | SSL 키스토어 위치 |
    | server.ssl.key-store-password | SSL 키스토어 비밀번호 |
    | server.ssl.key-store-type | SSL 키스토어 타입 |
    | server.ssl.key-alias | SSL 키 별칭 |
    | spring.cloud.config.server.git.uri | Git 저장소 URI |
    | spring.cloud.config.server.git.username | Git 저장소 사용자 이름 |
    | spring.cloud.config.server.git.password | Git 저장소 비밀번호 |
    | spring.cloud.config.server.git.clone-on-start | 서버 시작 시 Git 클론 여부 |
    | spring.cloud.config.prefix | 설정 파일 프리픽스 |
    | encrypt.key-store.location | 암호화 키스토어 위치 |
    | encrypt.key-store.password | 암호화 키스토어 비밀번호 |
    | encrypt.key-store.alias | 암호화 키 별칭 |
    
    ```jsx
    server:
      port: ${PORT}
      ssl:
        key-store: ${KEY_LOCATION}
        key-store-password: ${KEY_STORE_PASSWORD}
        key-store-type: ${KEY_STORE_TYPE}
        key-alias: ${KEY_ALIAS}
    spring:
      cloud:
        config:
          server:
            git:
              uri: ${GITHUB_URI}
              username: ${GITHUB_USERNAME}
              password: ${GITHUB_PASSWORD}
              clone-on-start: true
            prefix: /configuration
    encrypt:
      key-store:
        location: ${KEY_LOCATION}
        password: ${KEY_STORE_PASSWORD}
        alias: ${KEY_ALIAS}
    ```
    
- 프론트엔드
    
    
    | ID | Name |
    | --- | --- |
    | sdk.dir | 인증서버 클라이언트 아이디 |
    | rabbitmq.host | RabbitMQ 주소 |
    | rabbitmq.port | RabbitMQ 포트 번호 |
    | rabbitmq.username | RabbitMQ 사용자 이름 |
    | rabbitmq.password | RabbitMQ 비밀번호 |
    | backend.host | API 서버 주소 |

```json
sdk.dir=C\:\\Users\\SSAFY\\AppData\\Local\\Android\\Sdk
rabbitmq.host=j12a207.p.ssafy.io
rabbitmq.port=5672
rabbitmq.username=mulga207
rabbitmq.password=ssafy207!
backend.host=http://j12a207.p.ssafy.io:8000
```

# 배포 시 특이사항

- **Android 빌드**: 
    1. Firebase 바탕으로 앱 서명의 무결성과 보안 확보를 위해 gitlab 아래의 두 파일을 `/app` 하위에 삽입해야 합니다.
    - `key.jks`
    - `key.properties`<br>
    2. RabbitMQ, backend 주소 설정을 위해 gitlab 아래의 파일을 `/S12P21A207` 하위에 삽입해야 합니다.
    - `local.properties`

- 설정 서버
    - gitlab의 config 브랜치의 스프링 부트 프로젝트 배포
    - config server와 연결된 Github 레파지토리는 private으로 관리
    - 한 번 배포하면 이후에 다시 배포하지 않아도 되므로 Jenkins 설정 없이 도커 컨테이너로 개별 배포
    1. S12P21A207\config 경로에 Dockerfile,  `prodEncryption.jks`  파일 추가 후 Spring boot 프로젝트 빌드 
        
        [Dockerfile](Dockerfile.txt)
        
        [prodEncryptionKey.jks](prodEncryptionKey.jks)
        
        ```jsx
        ./gradlew clean bootJar
        ```
        
    2. 보안 상의 이유로 해당 경로에서  `prodEncryption.jks`  삭제 권장
    3. 도커 이미지 빌드 후 푸시
        
        ```jsx
        docker build -t kyulilee/config-server .
        docker push kyulilee/config-server
        ```
        
    4. pem 키가 있는 경로에서 EC2 접속 후 도커 컨테이너 실행
        
        ```jsx
        ssh -i J12A207T.pem [ubuntu@j12a207.p.ssafy.io](mailto:ubuntu@j12a207.p.ssafy.io)
        docker run -d --name config-server -p 8888:8888 kyulilee/config-server:latest
        ```
        
    

# Jenkins 파이프라인

- **backend**
    - Jenkins 대시보드 - 새로운 Item
    
    ```
    이름: be-develop
    Pipeline 선택
    Triggers
    - Build when a change is pushed to GitLab. 체크
    -- Push Events 체크
    -- Opened Merge Request Events 체크
    -- Rebuild open Merge Requests: Never
    -- Approved Merge Request (EE-only) 체크
    -- Comments 체크
    - 고급
    -- Enable [ci-skip] 체크
    -- Ignore WIP Merge Requests 체크
    -- Set build description to build cause (eg. Merge request or Git Push) 체크
    -- Allowed branches: Allow all branches to trigger this job
    -- Secret token: Generate 눌러 발급
    
    Pipeline
    - Definition: Pipeline script
    - Script: 하단 스크립트 복사
    - Use Groovy Sandbox 체크
    
    ```
    
    ```jsx
    pipeline {
        agent any
    
        environment {
            DOCKER_IMAGE = 'mug-backend'  // Docker image name
            CONFIG_SERVER_URL = 'https://j12a207.p.ssafy.io:8888/configuration'
        }
        
        tools {
            jdk 'jdk21'
        }
    
        stages {
            stage('Checkout') {
                steps {
                    git(
                        url: 'https://lab.ssafy.com/s12-fintech-finance-sub1/S12P21A207.git',
                        branch: 'be-develop',  // Branch to checkout
                        credentialsId: 'GitLab'
                    )
                }
            }
            
            stage('Modify application.yml') {
                steps {
                    script {
                        // Use sed to replace the placeholder with the actual value from CONFIG_SERVER_URL
                        sh """
                            sed -i 's|CONFIG_SERVER_URL_PLACEHOLDER|${CONFIG_SERVER_URL}|g' mug-backend/src/main/resources/application.yml
                        """
                        sh 'cat mug-backend/src/main/resources/application.yml'
                    }
                }
            }
            
            stage('Copy SSL Certificate from Jenkins Secrets') {
                steps {
                    script {
                        // Use withCredentials to fetch the SSL certificate from Jenkins credentials
                        withCredentials([file(credentialsId: 'ConfigSSL', variable: 'SSL_CERT_PATH')]) {
                            // Save the certificate in a directory to be used later in the Docker build
                            sh """
                                # Copy the SSL certificate to a location that can be used in the Docker build
                                cp ${SSL_CERT_PATH} ./my-cert.crt
                            """
                        }
                    }
                }
            }
    
            stage('Build Spring Boot App with Gradle') {
                steps {
                    script {
                        // Ensure Gradle is used to build the project
                        sh 'chmod +x ./mug-backend/gradlew'
                        dir('mug-backend') {
                            sh './gradlew clean build -x test'
                        }
                    }
                }
            }
    
            stage('Create Dockerfile') {
                steps {
                    script {
                        // Create a Dockerfile dynamically within the pipeline
                        writeFile file: 'Dockerfile', text: '''
                        FROM amazoncorretto:21
                        COPY my-cert.crt /usr/local/share/ca-certificates/my-cert.crt
                        RUN keytool -import -noprompt -trustcacerts -alias my-cert -file /usr/local/share/ca-certificates/my-cert.crt -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit
                        COPY mug-backend/build/libs/*.jar app.jar
                        EXPOSE 8080
                        ENTRYPOINT ["java", "-jar", "/app.jar"]
                        '''
                    }
                }
            }
    
            stage('Build Docker Image') {
                steps {
                    script {
                        // Build the Docker image with the Spring Boot application JAR
                        docker.build("${DOCKER_IMAGE}")
                    }
                }
            }
    
            stage('Deploy Docker Image') {
                steps {
                    script {
                        // Stop and remove any existing Docker container with the same name
                        sh '''
                        docker stop ${DOCKER_IMAGE} || true
                        docker rm ${DOCKER_IMAGE} || true
                        docker run -d --name ${DOCKER_IMAGE} -p 8000:8080 ${DOCKER_IMAGE}
                        docker image prune -f
                        '''
                    }
                }
            }
        }
    
        post {
            always {
                cleanWs()  // Clean workspace after build
            }
        }
    }
    
    ```
    
    - GitLab 프로젝트 - Settings - Webhooks - Add new webhook
    
    ```
    URL: http://j12a207.p.ssafy.io:8080/project/be-develop
    Secret token: 위 Jenkins 파이프라인에서 발급받은 Secret token 복사
    Trigger
    - Push events 체크
    -- Wildcard pattern: *be-develop
    SSL verification
    - Enable SSL verification 체크
    Test - Push events - "Hook executed successfully: HTTP 200" 뜨는지 확웨인
    ```
    

# 웹사이트 배포

```
1. GitLab 서 website 브랜치 다운로드

2. scp -r -i /{J12A207T.pem 디렉토리}/J12A207T.pem /{website 브랜치 다운로드 받은 디렉토리}/website/ [ubuntu@j12a207.p.ssafy.io](mailto:ubuntu@j12a207.p.ssafy.io):/home/ubuntu/

3. cmd 서 J12A207T.pem 저장된 디렉토리로 이동

4. ssh -i J12A207T.pem [ubuntu@j12a207.p.ssafy.io](mailto:ubuntu@j12a207.p.ssafy.io)

5. docker build -t website .

6. docker run -d -p 80:80 website
```
