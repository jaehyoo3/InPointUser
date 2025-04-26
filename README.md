# InPointUser 프로젝트 README

## 프로젝트 개요

InPointUser는 사용자 관리 및 포인트 충전 기능을 제공하는 Spring Boot 기반 백엔드 애플리케이션입니다. Toss Payments API를 연동하여 결제 기능을 구현하고, Docker를 통해 간편하게
배포 및 실행할 수 있습니다.

## 주요 기능

* **사용자 관리:**
    * 사용자 목록 조회 (페이징 및 정렬 지원)
    * 사용자 프로필 조회수 증가 기능
* **포인트 충전:**
    * Toss Payments API 연동을 통한 포인트 충전 요청
    * 쿠폰 적용 및 할인 기능
    * 결제 상태 관리 (요청됨, 성공, 실패)
* **API 문서:**
    * Swagger를 이용한 API 명세서 자동 생성

## 기술 스택

* **언어:** Java
* **프레임워크:** Spring Boot, Spring Data JPA
* **데이터베이스:** MySQL
* **빌드 도구:** Gradle
* **API 문서화:** Swagger
* **컨테이너화:** Docker, Docker Compose
* **기타:** QueryDSL, Lombok

## 사전 준비 사항

* Docker 및 Docker Compose 설치
* Java (프로젝트 버전에 맞게 설치 필요, 예: JDK 17)
* Gradle
* Toss Payments Secret Key (테스트 키 포함)

## 설정 및 실행 방법

1. **데이터베이스 및 환경 변수 설정:**
    * `docker-compose.yml` 파일 또는 환경 변수를 통해 MySQL 데이터베이스 관련 정보(비밀번호, 데이터베이스 이름)를 설정합니다. 기본값으로 `MYSQL_ROOT_PASSWORD=1234`,
      `MYSQL_DATABASE=inPointUser`가 사용됩니다.
    * `application.yaml` 파일 또는 환경 변수를 통해 Toss Payments Secret Key (`toss.secret-key`)를 설정합니다. 기본 테스트 키가 포함되어 있습니다.
2. **Docker Compose 실행:**
    * 프로젝트 루트 디렉토리에서 다음 명령어를 실행하여 Docker 컨테이너를 빌드하고 실행합니다.
        ```bash
        docker-compose up --build -d
        ```
    * 이 명령어는 MySQL 데이터베이스 컨테이너 (`mysql_db`)와 백엔드 애플리케이션 컨테이너 (`inpoint_backend`)를 생성하고 실행합니다.
3. **애플리케이션 확인:**
    * 애플리케이션은 기본적으로 8080 포트에서 실행됩니다. 웹 브라우저나 API 테스트 도구를 사용하여 `http://localhost:8080`으로 접속하여 애플리케이션 상태를 확인할 수 있습니다.
    * Swagger UI는 `http://localhost:8080/swagger-ui.html` (또는 설정된 경로) 에서 확인할 수 있습니다 (SwaggerConfig 확인 필요).

## API 엔드포인트

* **User API (`/api/users`)**:
    * `GET /`: 사용자 목록 조회 (페이징, 정렬 지원: `name`, `view`, `created`)
    * `POST /{userId}/increment-view-count`: 사용자 프로필 조회수 증가
* **TossPayment API (`/api/payments`)**:
    * `POST /request`: 결제 요청 생성 (사용자 ID, 금액, 쿠폰 코드 등 필요)
    * `POST /confirm`: 결제 승인 처리 (주문 ID, 결제 키, 금액 등 필요)

## 참고 자료

* 프로젝트 내 `HELP.md` 파일에 Spring 및 Gradle 관련 공식 문서 링크가 포함되어 있습니다.
* `data.sql` 파일에 초기 사용자 데이터가 정의되어 있습니다.