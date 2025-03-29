package com.fatihbozik.aviationroutefinder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

@Configuration
public class PasswordEncoderConfiguration {

    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        var encoders = Map.of("noop", NoOpPasswordEncoder.getInstance());
        var passwordEncoder = new DelegatingPasswordEncoder("noop", encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(NoOpPasswordEncoder.getInstance());
        return passwordEncoder;
    }
}
