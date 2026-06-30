package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AutoConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoConfigApplication.class, args);
    }

}

// 자동 구성 확인
/*
> 상황
- DbConfig를 적용하지 않고 아래의 테스트를 진행
- 결과: 빈 등록 문구는 뜨지 않지만(당연) 성공하는 것을 확인할 수 있음
- 이유: 스프링 부트의 자동 구성(auto configuration)
```java
@Test
void checkBean() {
    log.info("dataSource = {}", dataSource);
    log.info("transactionManager = {}", transactionManager);
    log.info("jdbcTemplate = {}", jdbcTemplate);

    assertThat(dataSource).isNotNull();
    assertThat(transactionManager).isNotNull();
    assertThat(jdbcTemplate).isNotNull();
}
```
*/

// 스프링 부트의 자동 구성
/*
> Auto Configuration
- 자주 사용하는 수 많은 빈들을 자동으로 등록해주는 기능

> 자동 구성 살짝 알아보기
- spring-boot-autoconfigure라는 프로젝트 안에서 수 많은 자동 구성을 제공함(spring-boot-starter 안에 들어있음)
- 해당 라이브러리에 들어가면 boot라는 폴더 안에 수많은 빈 등록 코드들이 있음
- @AutoConfiguration: 자동 구성을 사용하려면 이 어노테이션을 등록
- @ConditionalOnClass({DataSource.class, JdbcTemplate.class})
    - 해당 클래스가 있으면 설정이 동작 / 없으면 모두 무효화, 빈 등록 X
- @Import: 스프링에서 자바 설정을 추가할 때 사용함
    - 다른 설정 클래스나 컴포넌트 클래스를 현재 설정 클래스로 불러올 때 사용

> @ConditionalOnMisssingBean(JdbcOperations.class)
- JdbcOperations 빈이 없을 때 동작
- JdbcTemplate의 부모 인터페이스
- 따라서 수동 빈 등록이 되어있는 경우 동작하지 않음

> Auto Configuration - 자동 설정? 자동 구성?
- 자동 설정
    - 빈들을 자동으로 등록해서 스프링이 동작하는 환경을 자동으로 설정해주기 때문에 자동 설정이라는 용어도 많음

- 자동 구성
    - 스프링 실행에 필요한 빈들을 적절하게 배치해줌

- 결론: 둘 다 맞는 말

> 핵심 개념
- @Conditional: 특정 조건에 맞을 때 설정이 동작하도록 함
- @AutoConfiguration: 자동 구성이 어떻게 동작한느지 내부 원리 이해
*/

// 자동 구성 직접 만들기 - 기반 예제
/*
> memory 패키지
- Memory
- MemoryFinder
- MemoryController

> 만든 이유
- 완전히 독립적인 라이브러리라고 생각하고
- 사용하는 건 hello 패키지 아래에서 사용
- MemoryConfig.java로 빈을 등록 후 사용 (MemoryController, MemoryFinder)
*/

// @Conditional
/*
> 개요
- 메모리 조회 기능을 특정 조건일 때만 해당 기능이 활성화 되도록 설정
- Condition 인터페이스

> Condition 인터페이스
- matches(): true 동작 / false 미동작
- ConditonContext: 스프링 컨테이너, 환경 정보
- AnnotaionTypeMetadata: 어노테이션 메타 정보

> 목표
- Condition 인터페이스를 구현해서 자바 시스템 속성이 memory=on이라고 되어 있을 때만 메모리 기능이 동작
    - java -Dmemory=on -jar project.jar
    - edit configuration - vm options에 -Dmemory=on 입력
*/

// @Conditional - 다양한 기능
/*
> 이전
- Condition 인터페이스를 직접 구현
- 하지만 Spring은 이미 필요한 대부분의 구현체를 다 정의해둠

> @ConditionalOnProperty(name = "memory", havingValue = "on") // -Dmemory=on
- 환경 정보가 -Dmemory=on이라는 조건에 맞으면 동작, 그렇지 않으면 동작하지 않음

> 그 외
- @ConditionalOnClass, @ConditionalOnMissingClass
    - 클래스가 있는 경우 동작, 나머지는 그 반대
    
- @ConditionalOnBean, @ConditionalOnMissingBean
    - 빈이 등록되어 있는 경우 동작, 나머지는 그 반대
    
- @ConditionalOnProperty
    - 환경 정보가 있는 경우 동작
    
- @ConditionalOnResource
    - 리소스가 있는 경우 동작

- @ConditionalOnWebApplication, @ConditionalOnNotWebApplication
    - 웹 애플리케이션인 경우 동작

- @ConditionalOnExpression
    - SpEL 표현식에 만족하는 경우 동작

> 참고
- 스프링 부트의 기능 X / 스프링의 기능 O
*/

// 순수 라이브러리 만들기 ~ 순수 라이브러리 사용하기 2
/*
> memory-v1 / project-v1 프로젝트 생성
- memory-v1 jar 파일 생성 후 project-v1에 넣기

> 자동 구성의 진가
- 나는 현재 memory-v1 라이브러리 구성을 알고 있음
    - 어떤 걸 스프링 빈으로 등록할지 알고 있음
    - 근데 사용자는 이걸 모름
    - 이걸 해결해주는 것이 스프링 부트 자동 구성

- 라이브러리 제공자 쪽에서 자동 구성이란 걸 잘 구성해서 주면 빈이 자동으로 등록할 수 있음
*/

// 자동 구성 라이브러리 만들기 ~ 자동 구성 라이브러리 사용하기 2
/*
> memory-v2 / project-v2
- memory-v2에 MemoryAutoConfig 추가 및 추가 설정 파일 생성(xxx.imports)
- 이후 똑같이 빌드 후 project-v2에 라이브러리 임포트
*/

// 자동 구성 이해1 - 스프링 부트의 동작
/*
> xxx.imports를 보면 AutoConfiguration의 대상이 되는 애들을 다 지정해둠
- memory-v2에도 있고
- spring-boot-autoconfigure에도 있음

> 동작 방식
- @SpringBootApplication
    - @EnableAutoConfiguration: 자동 구성을 활성화하는 기능
        - @Import(AutoConfigurationImportSelector.class)

*/

// 자동 구성 이해2 - 스프링 부트의 동작
/*
> @Import(AutoConfigurationImportSelector.class)
- @Import는 주로 스프링 설정 정보를 포함할 때 사용함
- 근데 AutoConfigurationImportSelector는 @Configuration이 없음

> @Import에 설정 정보를 추가하는 방법
- 정적인 방법: @Import(클래스)
- 동적인 방법: @Import('ImportSelector')코드로 프로그래밍해서 설정으로 사용할 대상으로 동적으로 선택 가능
    - 인터페이스임

> @EnableAutoConfiguration 동작 방식
- 다음 경로의 파일을 확인함
    - META-INF/spring/org.xxx.xxx.xxx.imports
        - 여기에 있는 설정 정보 파일을 다 찾음
        - 해당되는 파일은 설정 정보를 읽고 빈으로 등록됨
*/