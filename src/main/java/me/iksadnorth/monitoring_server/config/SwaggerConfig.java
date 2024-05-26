package me.iksadnorth.monitoring_server.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("서버 CPU 사용률 모니터링 시스템")
                .description("서버의 CPU 사용률을 분, 시, 주, 월 단위로 모니터링하고 이를 데이터베이스에 저장하여 API를 통해 조회할 수 있는 시스템입니다.")
                .version("1.0.0");
    }
}
