package com.example.careeix.src;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2 // Swagger2 버전을 활성화 하겠다는 어노테이션
@EnableAutoConfiguration
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        List<Parameter> global = new ArrayList<>();
        global.add(new ParameterBuilder().
                name("X-ACCESS-TOKEN").
                description("X-ACCESS-TOKEN")
                .parameterType("header").
                required(false)
                .modelRef(new ModelRef("string")).build());

        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(global)
                .apiInfo(this.apiInfo())
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.any()) // 모든 RequestMapping URI 추출
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Careeix")
                .version("1.0.0")
                .description("화이팅")
                .build();
    }
}


    /*
     < Docket >
     - Swagger 설정을 할 수 있게 도와주는 클래스이다.
     - API 자체에 대한 정보는 컨트롤러에서 작성한다.

     1. useDefaultResponseMessages()
     - false로 설정하면 Swagger에서 제공해주는 응답코드(200, 401, 403, 404)에 대한 기본 메시지를 제거해준다.
     2. groupName()
     - Docket Bean이 한 개일 경우 생략해도 상관없으나, 둘 이상일 경우 충돌을 방지해야 하므로 설정해줘야 한다
     3. select()
     - ApiSelectorBuilder를 생성하여 apis()와 paths()를 사용할 수 있게 해준다.
     4. apis()
     - api 스펙이 작성되어 있는 패키지를 지정한다. 즉, 컨트롤러가 존재하는 패키지를 basepackage로 지정하여 해당 패키지에 존재하는 API를 문서화 한다.
     5. paths()
     - apis()로 선택되어진 API중 특정 path 조건에 맞는 API들을 다시 필터링하여 문서화한다.
     - PathSelectors.any()로 설정하면 패키지 안에 모든 API를 한 번에 볼 수 있다.
     6. apiInfo()
     - 제목, 설명 등 문서에 대한 정보들을 설정하기 위해 호출한다.

     */


