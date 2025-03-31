package com.fatihbozik.aviationroutefinder.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import jakarta.annotation.PostConstruct;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

/**
 * Java config for springdoc-openapi API documentation library
 */
@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("REST Aviation Route Finder backend API documentation")
                        .version("1.0")
                        .description(
                                "This is the REST API documentation of the Aviation Route Finder backend. " +
                                "If authentication is enabled, use admin/admin when calling the APIs")
                        .license(swaggerLicense())
                        .contact(swaggerContact()));
    }

    private Contact swaggerContact() {
        Contact aviationRouteFinderContact = new Contact();
        aviationRouteFinderContact.setName("Fatih Bozik");
        aviationRouteFinderContact.setUrl("https://github.com/FatihBozik/aviation-route-finder");
        return aviationRouteFinderContact;
    }

    private License swaggerLicense() {
        License aviationRouteFinderLicense = new License();
        aviationRouteFinderLicense.setName("MIT");
        aviationRouteFinderLicense.setUrl("https://mit-license.org/");
        aviationRouteFinderLicense.setExtensions(Collections.emptyMap());
        return aviationRouteFinderLicense;
    }

}

