Spring Boot 시큐리티 / JWT 토큰 연습

## bootPractice

## Refresh Token 확장판 [바로가기](https://github.com/Kascald/JWT_Practice-w.RefreshToken)
### 프로젝트 설명

bootPractice 
Spring Boot를 사용하여 JSON Web Token (JWT) 기반 인증 및 권한 부여를 구현한 연습용 프로젝트입니다. 이 프로젝트는 MySQL 데이터베이스를 사용하며, 개발 환경에서의 편리한 개발을 위해 Spring DevTools를 사용하고 있습니다. 이 프로젝트의 주요 기능 및 설정에 대한 자세한 설명은 다음과 같습니다.

### 주요 연습기능

1. **JWT 기반 인증 및 권한 부여**
   - JWT를 사용하여 사용자 인증 및 권한 부여를 처리합니다.
   - 랜덤 키와 해시된 키를 사용하여 JWT를 서명하고 검증합니다.
   - 액세스 토큰과 리프레시 토큰의 만료 시간을 각각 10분과 1시간으로 설정하였습니다.

2. **Spring Boot와 MySQL 연동**
   - MySQL 데이터베이스와의 연결을 설정하고 JPA를 통해 데이터베이스 작업을 수행합니다.
   - Hibernate를 사용하여 엔티티를 매핑하고, 데이터베이스 스키마를 자동으로 생성합니다.
   - 개발 환경에서 SQL 쿼리를 보기 위해 SQL 로그를 출력합니다.


### 실행 방법

1. **프로젝트 클론**
   - GitHub 또는 소스 관리 시스템에서 프로젝트를 클론합니다.
     ```bash
     git clone https://github.com/Kascald/bootPractice.git
     cd bootPractice
     ```

2. **MySQL 데이터베이스 설정**
   - MySQL 서버를 설치하고 실행합니다.
   - MySQL에 `preon` 데이터베이스를 생성합니다.
     ```sql
     CREATE DATABASE preon;
     ```

3. **프로젝트 설정 수정**
   - `src/main/resources/application.properties` 파일을 열어 데이터베이스 접속 정보를 본인의 환경에 맞게 수정합니다.
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/preon?useUnicode=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true&useSSL=false
     spring.datasource.username=your_mysql_username
     spring.datasource.password=your_mysql_password
     ```

4. **프로젝트 빌드 및 실행**
   - Gradle을 사용하여 프로젝트를 빌드하고 실행합니다.
     ```bash
     ./gradlew clean build
     ./gradlew bootRun
     ```

5. **웹 애플리케이션 접속**
   - 웹 브라우저에서 `http://localhost:8080`으로 접속하여 애플리케이션을 확인합니다.

## Comment

위의 단계를 따라 bootPractice프로젝트를 로컬 환경에서 실행할 수 있습니다. MySQL 데이터베이스 설정과 프로젝트 설정 파일의 수정에 유의하여 본인의 환경에 맞게 설정해 주세요. 성공적으로 실행되면, JWT 기반 인증 시스템이 구현된 Spring Boot 애플리케이션을 확인할 수 있습니다.

View는 별도로 세부적으로 구현하지 않았으며, Postman을 통해 동작을 확인, 테스트 하였습니다.
