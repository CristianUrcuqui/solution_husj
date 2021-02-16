package com.chapumix.solution.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.chapumix.solution.app.utils.PacienteDinamica;

@Configuration
public class MvcConfig implements WebMvcConfigurer{
	
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/error_403").setViewName("error_403");		
	}
	
	//me sirve para agregar recursos externos al proyecto
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {		
		WebMvcConfigurer.super.addResourceHandlers(registry);
		registry.addResourceHandler("/pdf/**")
		/*.addResourceLocations("file:/C:/tmp/pdf/"); Windows*/		
		.addResourceLocations("file:/home/jar/pdf/"); //Linux
		
	}



	//Estos bean se usan para que spring los registre internamente y luego pueda ser usadas en cualquier parte del codigo.
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
	
	@Bean
	public PacienteDinamica pacienteDinamica() {
	    return new PacienteDinamica();
	}
	

}
