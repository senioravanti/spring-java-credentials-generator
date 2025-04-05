package ru.manannikov.credentialsgenerator.config;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig {
    @Bean
    public Faker faker() {
        return new Faker();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(
               CorsRegistry registry
            ) {
                registry
                    .addMapping("/**")
                    .allowedOrigins("*")
                    .allowedMethods("OPTIONS", "HEAD", "GET", "POST", "PUT", "DELETE")
                ;
            }
        };
    }
}
