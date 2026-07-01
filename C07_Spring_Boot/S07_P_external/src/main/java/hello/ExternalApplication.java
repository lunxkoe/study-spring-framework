package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExternalApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExternalApplication.class, args);
    }

}

// 외부 설정이란?
/*
> 환경
- 개발 환경: 개발 서버, 개발 DB 사용
- 운영 환경: 운영 서버, 운영 DB 사용

> 환경에 따라서 변하는 설정값을 실행 시점에 주입
- 배포 환경과 무관하게 하나의 빌드 결과물을 만듦, 설정값을 두지 않음
- 설정값은 실행 시점에 각 환경에 따라 외부에서 주입
    - 개발 서버: app.jar를 실행할 때, dev.db.com 값을 외부 설정으로 주입
    - 운영 서버: app.jar를 실행할 때, prod.db.com 값을 외부 설정으로 주입

> 외부 설정 방법
- OS 환경 변수
- 자바 시스템 속성
- 자바 커멘드 라인 인수
- 외부 파일(설정 데이터): 프로그램에서 외부 파일을 직접 읽어서 사용
    - 애플리케이션에서 특정 위치의 파일을 읽도록 함
        - 각 서버마다 해당 파일안에 다른 설정 정보를 남겨둠
*/


// 외부 설정 - OS 환경 변수
/*
> OS 환경 변수
- 해당 OS를 사용하는 모든 프로그램에서 읽을 수 있는 설정 값
- 조회 방법
    - 윈도우: set
    - 리눅스: printenv
*/

// 외부 설정 - 자바 시스템 속성
/*
> 사용법
- java -Durl=dev -jar app.jar
*/

// 외부 설정 - 커맨드 라인 인수
/*
> 커맨드 라인 인수
- main(args)
- java -jar app.jar dataA dataB
- key=value 형식이 아님
- =을 기준으로 파싱하고 Map으로 만들어야함
*/

// 외부 설정 - 커맨드 라인 옵션 인수
/*
> 커맨드 라인 옵션 인수
- 파싱이 안되는 커맨드 라인 인수의 단점을 커버해줌
- **스프링 만의 표준 방식**

> 사용
- --key=value
- --username=userA --username=userB
*/

// 외부 설정 - 커맨드 라인 옵션 인수와 스프링 부트
/*
> 스프링 부트
- ApplicationArguments를 스프링 빈으로 등록해줌
- 그 안에 입력한 커맨드 라인을 저장해둠
- 해당 빈을 주입 받으면 커맨드 라인으로 입력한 값을 어디서든 사용할 수 있음
*/

// 외부 설정 - 스프링 통합
/*
> Environment
- 특정 외부 설정에 종속되지 않고 key=value 형식의 외부 설정에 접근할 수 있음
    - environment.getProperty(key)를 통해서 값을 조회할 수 있음
    - Environment는 내부에서 여러 과정을 거쳐서 PropertySource에 접근함
    - 우선순위가 정해져 있음
- **모든 외부 설정은 이제 Environment를 통해서 조회함

- application.yaml / application.properties도 PropertySource에 추가됨

> 우선순위
- 더 유연한 것이 우선순위를 가짐(변경하기 어려운 파일보다 실행 시 원하는 값을 줄 수 있는 자바 시스템 속성이 더 우선권을 가짐)
- 범위가 더 좋은 것이 우선권을 가짐(자바 시스템 속성은 해당 JVM 안에서 모두 접근할 수 있음, 반면에 커맨드 라인 옵션 인수는 main의 arg를 통해서 들어오기 때문에 접근 범위가 더 좁음)
*/

// 설정 데이터 1 - 외부 파일
/*
- build/libs에 application.properties를 생성 후 jar를 실행하면 읽음
*/

// 설정 데이터 2 - 내부 파일 분리
/*
> application-[].yaml
- application-dev
- application-prod
- 빌드 시점에 둘 다 가지고 배포함
    - 실행 시 최소한의 구분은 필요
    - 개발 서버는 dev라는 값을 제공하고, 운영 서버는 prod라는 값을 제공(프로필)
    - 프로필이 dev / prod => dev.yaml / prod.yaml을 읽음
    - 스프링이 이미 다 구현 해둠

> 프로필
- spring.profiles.active 외부 설정에 값을 넣으면 해당 프로필을 사용한다고 판단
- applictaion-{profile}.properties
*/

// 설정 데이터 3 - 내부 파일 합체
/*
> 분리하는 방법
```yaml
spring.config.activate.on-profile=dev

--- <-- 이걸로 구분함
spring.config.activate.on-profile=prod

```

- properties는 #--- | !---
- yaml은 ---
- 이후 spring.config.activate.on-profile={profile}로 구분

> 주의사항
- 분할 기호 위 아래에 주석을 적으면 안됨

```properties
db.url=local.db.com
db.username=local_user
db.password=local_pw
== 여기는 무조건 사용됨 프로필과 상관없이

!---

spring.config.activate.on-profile=dev
db.url=dev.db.com
db.username=dev_user
db.password=dev_pw

!---

spring.config.activate.on-profile=prod
db.url=prod.db.com
db.username=prod_user
db.password=prod_pw
```
- 특정 프로필을 지정하면 같은 값이 있다면 덮어버림

> 설정 데이터 순서
- 일단 기본 값을 읽음
- 프로필을 읽어는데 값이 있으면 해당 프로필의 같은 값(key)를 기준으로 값을 덮어버림
- 참고로 프로필을 두 가지 동시 활성화 가능(,로 구분)

```properties
db.url=local.db.com
db.username=local_user
db.password=local_pw

!---

spring.config.activate.on-profile=dev
db.url=dev.db.com
db.username=dev_user
db.password=dev_pw

!---

spring.config.activate.on-profile=prod
db.url=prod.db.com
db.username=prod_user
db.password=prod_pw

!---

url=hello.db.com
```
- 이렇게 하면 순서가 위에서 아래로 읽어서 무조건 url은 hello.db.com이 되어버림
*/

// 우선순위 - 전체
/*
> 자주 사용하는 우선순위 (아래로 갈수록 높음)
- 설정 데이터(application.properties)
- OS 환경변수
- 자바 시스템 속성
- 커맨드 라인 옵션 인수
- @TestPropertySource(테스트에서 사용)

> 설정 데이터 우선순위 (아래가 우선순위가 높음)
- jar 내부 application.properties
- jar 내부 프로필 적용 파일 application-{profile}.properties
- jar 외부 application.properties
- jar 외부 프로필 적용 파일 application-{profile}.properties
*/