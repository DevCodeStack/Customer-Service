package com.eatza.customer.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.eatza.customer.dto.ErrorResponseDto;
import com.eatza.customer.dto.TokenDto;
import com.eatza.customer.dto.UserDto;
import com.eatza.customer.exception.CustomGlobalExceptionHandler;
import com.eatza.customer.exception.InvalidTokenException;
import com.eatza.customer.exception.UnauthorizedException;
import com.eatza.customer.service.JwtAuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationControllerTest {
	
	private MockMvc mockMvc;
	
	@Mock
	private JwtAuthenticationService authenticationService;
	
	@InjectMocks
	private JwtAuthenticationController authenticationController;
	
	private UserDto userDto;
	
	private JacksonTester<UserDto> jsonUserDto;
	
	private JacksonTester<ErrorResponseDto> jsonErrorResponseDto;
	
	@BeforeEach
	void setup() {
		
		mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
				.setControllerAdvice(new CustomGlobalExceptionHandler())
				.build();
		
		JacksonTester.initFields(this, new ObjectMapper());
		
		userDto = new UserDto();
		userDto.setPassword("password");
		userDto.setUsername("username");
	}
	
	//Positive test case : login
	@Test
	public void login_Success() throws Exception {
		
		var token = TokenDto.builder().token("myToken").build();
		when(authenticationService.authenticateUser(any())).thenReturn(token);

		MockHttpServletResponse response = mockMvc.perform(
				post("/login").contentType(MediaType.APPLICATION_JSON)
				.content(jsonUserDto.write(userDto).getJson())).andReturn().getResponse();
	
		assertEquals(token.getToken(), (new ObjectMapper().readValue(response.getContentAsString(), TokenDto.class)).getToken());
		
	}
	
	//Negative test case : login
	@Test
	public void login_Failed() throws Exception {
		
		when(authenticationService.authenticateUser(any()))
			.thenThrow(new UnauthorizedException("User not present"));
		
		MockHttpServletResponse response = mockMvc.perform(
					post("/login").contentType(MediaType.APPLICATION_JSON)
					.content(jsonUserDto.write(userDto).getJson())).andReturn().getResponse();
		
		ErrorResponseDto errorResponseDto = jsonErrorResponseDto.parse(response.getContentAsString()).getObject();
		assertEquals("User not present", errorResponseDto.getDescription());
		
	}
	
	//Positive test case : validateCustomerByToken
	@Test
	public void validateCustomerByToken_Success() throws Exception {
		
		String token = "myToken";
		when(authenticationService.validateCustomer(any())).thenReturn(true);

		MockHttpServletResponse response = mockMvc.perform(
				get("/validate").accept(MediaType.APPLICATION_JSON)
				.param("token", token))
				.andReturn().getResponse();
		
		assertEquals(true, Boolean.parseBoolean(response.getContentAsString()));
		
	}
	
	//Negative test case : validateCustomerByToken
	@Test
	public void validateCustomerByToken_Failed() throws Exception {
		
		String token = "myToken";
		when(authenticationService.validateCustomer(any()))
		.thenThrow(new InvalidTokenException("Customer not present or active"));
		
		MockHttpServletResponse response = mockMvc.perform(
				get("/validate").accept(MediaType.APPLICATION_JSON)
				.param("token", token))
				.andReturn().getResponse();
	
	ErrorResponseDto errorResponseDto = jsonErrorResponseDto.parse(response.getContentAsString()).getObject();
	assertEquals("Customer not present or active", errorResponseDto.getDescription());
		
	}

}
