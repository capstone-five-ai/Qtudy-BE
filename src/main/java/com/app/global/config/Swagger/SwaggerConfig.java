package com.app.global.config.Swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private static final String API_NAME = "Qtudy Swagger";
    private static final String API_VERSION = "1.0.0";
    private static final String API_DESCRIPTION = "Test 중입니다....";

    @Bean
    public Docket api() {   // http://[ip주소]:[포트번호]/swagger-ui.html
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false) // 기본 응답코드 표시
                .apiInfo(apiInfo()) // Api 정보
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.app.domain")) // 적용할 패키지명
                .paths(PathSelectors.any()) // 패키지 하위에서 적용할 URL path 지정
                .build();
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(API_NAME)
                .version(API_VERSION)
                .description(API_DESCRIPTION)
                .build();
    }
}
