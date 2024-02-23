# Ubuntu 20.04를 베이스 이미지로 사용
FROM ubuntu:20.04

# 환경 변수 설정으로 대화형 프롬프트 비활성화
ENV DEBIAN_FRONTEND=noninteractive

# 시스템 패키지 업데이트 및 필요한 도구 설치
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    curl \
    software-properties-common \
    && add-apt-repository -y ppa:openjdk-r/ppa \
    && apt-get update \
    && apt-get install -y openjdk-17-jdk \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Google Chrome 설치
RUN wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb \
    && apt-get update \
    && apt-get install -y ./google-chrome-stable_current_amd64.deb || true \
    && apt-get install -f -y \
    && rm google-chrome-stable_current_amd64.deb
    
# Chrome WebDriver 다운로드 및 설정
RUN wget -q "https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/121.0.6167.85/linux64/chromedriver-linux64.zip" -O chromedriver.zip \
    && unzip chromedriver.zip -d /usr/local/bin/ \
    && chmod +x /usr/local/bin/chromedriver-linux64/chromedriver \
    && mv /usr/local/bin/chromedriver-linux64/chromedriver /usr/local/bin/ 

# 애플리케이션 파일 복사
ARG JAR_FILE=target/seleniumTest-1-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app/seleniumtest.jar

# 작업 디렉터리 설정
WORKDIR /app

# 애플리케이션 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "seleniumtest.jar"]
