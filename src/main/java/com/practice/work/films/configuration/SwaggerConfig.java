package com.practice.work.films.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.practice.work.films.controllers"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .tags(new Tag("Add Film Tag", "REST endpoint to add a new film to the mongoDB"),
                        new Tag("Add Multiple Films", "REST endpoint to add multiple new films to the mongoDB"),
                        new Tag("Fetch All Films", "Fetch a list of all films in the mongoDB")
                );
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Film MongoDB REST Service",
                "Provides endpoints to query my mongo film database",
                "",
                "",
                new Contact("Ernie", "", "ern3st@hotmail.co.uk"),
                "", "", Collections.emptyList()
        );
    }
}
