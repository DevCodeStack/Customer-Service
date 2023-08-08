package com.eatza.customer;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

import com.eatza.customer.config.JwtFilter;
import com.eatza.customer.util.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class CustomerRegistrationServiceApplicationTests {

	@Mock
	CustomerRegistrationServiceApplication customerRegistrationServiceApplication;
	
	JwtTokenUtil tokenUtil;
	
	@Test
	public void contextLoads() {
		
		customerRegistrationServiceApplication = new CustomerRegistrationServiceApplication();
		
		FilterRegistrationBean<JwtFilter> registrationBean = 
				customerRegistrationServiceApplication.filterRegistration(tokenUtil);
		assertThat(registrationBean.getUrlPatterns().equals(Arrays.array("/customer/*")));
		
	}

}
