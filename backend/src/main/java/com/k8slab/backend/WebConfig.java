package com.k8slab.backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração reposnsavel por permitir requisições cross-origin
 */
@Configuration
public class WebConfig implements WebMvcConfigurer{

	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("http://localhost:4200", "http://localhost:3000", "http://localhost:80")
				.allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
				.allowedHeaders("*");
		WebMvcConfigurer.super.addCorsMappings(registry);
	}

}
