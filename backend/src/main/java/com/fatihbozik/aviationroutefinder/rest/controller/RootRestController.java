package com.fatihbozik.aviationroutefinder.rest.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Hidden
@RestController
@RequestMapping("/")
public class RootRestController {
    @Value("#{servletContext.contextPath}")
    private String servletContextPath;

    @GetMapping
    public void redirectToSwagger(HttpServletResponse response) throws IOException {
        response.sendRedirect(this.servletContextPath + "/swagger-ui/index.html");
    }
}
