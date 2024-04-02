package com.app.global.config.Swagger;

import static io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;




// [서버]/swagger-ui/index.html -> 주소
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI publicApi() {
        Info info = new Info()
                .title("Qtudy API 문서")
                .description("Qtudy Server API 문서입니다")
                .version("v1");

        SecurityScheme securityScheme = new SecurityScheme() // 보안기능 (이후는 동규가...^^)
                .name("Authorization")
                .type(Type.HTTP)
                .in(HEADER)
                .bearerFormat("Authorization")
                .scheme("Bearer");


        return new OpenAPI()
                .info(info);
    }
}