package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ActuatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActuatorApplication.class, args);
    }

    @Bean
    public InMemoryHttpExchangeRepository httpExchangeRepository() {
        return new InMemoryHttpExchangeRepository();
    }

}


// 프로메테우스 - 게이지와 카운트
/*
> 게이지
- 임의로 오르내일 수 있는 값
- 예) CPU 사용량, 메모리 사용량, 사용중인 커넥션

> 카운터
- 단순하게 증가하는 값
- dP) HTTP 요청 수, Log 출력 수
*/