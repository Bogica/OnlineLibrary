package com.example.library.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger configuration
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket productApi() {
        Contact contact =new Contact(
                "Tea Milardovic",
                "https://github.com/Bogica",
                "whatwillyoudo@withmyemail.com"
        );
        ApiInfo apiInfo= new ApiInfoBuilder().title("Online Library")
                .description("Online library application REST Api")
                .termsOfServiceUrl("jUST CHILL!!!")
                .contact(contact)
                .licenseUrl("something@something.com").version("1.0").build();

        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.any())
                .build();
    }
}
