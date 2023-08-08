package com.eatza.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.eatza.customer.config.JwtFilter;
import com.eatza.customer.util.JwtTokenUtil;

@SpringBootApplication
@EnableEurekaClient
@EnableWebMvc
public class CustomerRegistrationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerRegistrationServiceApplication.class, args);
	}
	
	@Bean
	public FilterRegistrationBean<JwtFilter> filterRegistration(JwtTokenUtil tokenUtil){
		
		FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new JwtFilter(tokenUtil));
		registrationBean.addUrlPatterns("/customer/*");
		return registrationBean;
	}

}
