package com.Get_Your_DL_public_portal.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


// this cors class is basically used to sync the BE and FE calls (like domains, ports etc)
// e.g FE is running at local host 5723 and BE is running at 8080 so without this class you will get CORS error (cross origin resource sharing)

@Configuration
public class CorsConfig {

    private static final Logger LOG= LoggerFactory.getLogger(CorsConfig.class);


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        LOG.info("Cors are now enabled Man!!!");
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                LOG.info("Registry ===> {}", registry);

                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173") // FE is running at this port
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // allowed http method
                        .allowedHeaders("Authorization", "Content-Type") // allowed headers
                        .allowCredentials(true); // if this is set as false then authoriation headers and session will not be shared by browser even if it is set in FE code
            }
        };
    }

}
