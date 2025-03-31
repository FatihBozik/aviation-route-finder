package com.fatihbozik.aviationroutefinder.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        Contact petclinicContact = new Contact();
        petclinicContact.setName("Fatih Bozik");
        petclinicContact.setUrl("https://github.com/FatihBozik/aviation-route-finder");
        return petclinicContact;
    }

    private License swaggerLicense() {
        License petClinicLicense = new License();
        petClinicLicense.setName("MIT");
        petClinicLicense.setUrl("https://mit-license.org/");
        petClinicLicense.setExtensions(Collections.emptyMap());
        return petClinicLicense;
    }

}

