package com.pointofsale.dataSupplier.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Development");

        Info info = new Info()
            .title("Product Management System about Point of Sale")
            .version("1.0")
            .description("This API exposes end point to manage products and sales");
            
        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }
}
