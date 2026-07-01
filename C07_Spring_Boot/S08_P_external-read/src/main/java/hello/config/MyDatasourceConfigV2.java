package hello.config;

import hello.datasource.MyDataSource;
import hello.datasource.MyDatasourcePropertiesV1;
import hello.datasource.MyDatasourcePropertiesV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Slf4j
@EnableConfigurationProperties(MyDatasourcePropertiesV2.class)
public class MyDatasourceConfigV2 {

    private final MyDatasourcePropertiesV2 properties;

    public MyDatasourceConfigV2(MyDatasourcePropertiesV2 properties) {
        this.properties = properties;
    }

    @Bean
    public MyDataSource dataSource() {
        return new MyDataSource(
                properties.getUrl(),
                properties.getUsername(),
                properties.getPassword(),
                properties.getEtc().getMaxConnection(),
                properties.getEtc().getTimeout(),
                properties.getEtc().getOptions()
        );
    }
}
