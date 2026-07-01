package hello;

import hello.config.*;
import hello.datasource.MyDatasourcePropertiesV1;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;

//@Import(MyDatasourceEnvConfig.class)
//@Import(MyDatasourceValueConfig.class)
//@Import(MyDatasourceConfigV1.class)
//@Import(MyDatasourceConfigV2.class)
@Import(MyDatasourceConfigV3.class)
@SpringBootApplication(scanBasePackages = {"hello.datasource", "hello.pay"})
//@ConfigurationPropertiesScan // @EnableConfigurationProperties 이거 안해도 됨
public class ExternalReadApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExternalReadApplication.class, args);
    }

}

// @Value
/*
> 내부적으로 Environment를 사용함
*/
