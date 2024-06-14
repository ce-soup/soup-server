package com.github.soup.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    private val AUTH_TOKEN_HEADER = "Authorization"

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(apiInfo())
            .addSecurityItem(SecurityRequirement().addList(AUTH_TOKEN_HEADER))
            .components(Components().addSecuritySchemes("Authorization", bearerAuth))
    }

    private fun apiInfo(): Info {
        return Info()
            .title("soup service server")
            .version("1.0")
            .description("soup service server api documents")
    }

    var bearerAuth = SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("Authorization")
        .`in`(SecurityScheme.In.HEADER)
        .name(AUTH_TOKEN_HEADER)
}