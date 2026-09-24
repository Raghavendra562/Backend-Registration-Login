package com.registration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerPortConfig implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

    @Value("${PORT:8080}")
    private String port;

    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {
        factory.setPort(Integer.parseInt(port));
    }
}