package com.eatza.customer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfiguration {
	
//	@Bean
//    public Docket api() { 
//		  return new Docket(DocumentationType.SWAGGER_2)
//			        .select()
//			        .apis(RequestHandlerSelectors.basePackage("com.eatza.customer"))
//			        .paths(PathSelectors.any())
//			        .build()
//			        .apiInfo(apiInfo())
//			        .securitySchemes(Collections.singletonList(getApiKey()));                                       
//    }
//
//	private SecurityScheme getApiKey() {
//		return new ApiKey("Bearer", "Authorization", "header");
//	}
//
//	private ApiInfo apiInfo() {
//		ApiInfoBuilder builder = new ApiInfoBuilder();
//		return builder.title("Customer Service APIs")
//				.description("Customer registration service for eatza restaurants")
//				.version("0.0.1")
//				.build();
//	}
	
	@Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot API with Security")
                        .version("1.0.0")
                        .description("API documentation with authentication"))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                .name("BearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }

}
