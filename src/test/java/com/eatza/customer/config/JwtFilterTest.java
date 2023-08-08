package com.eatza.customer.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.eatza.customer.dto.ErrorResponseDto;
import com.eatza.customer.util.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

	@Mock
	JwtTokenUtil tokenUtil;
	
	@Mock
	ObjectMapper objectMapper;
	
	@InjectMocks
	JwtFilter filter;
	
	MockHttpServletRequest request;
	MockHttpServletResponse response;
	MockFilterChain filterChain;
	
	@BeforeEach
	void setUp() throws Exception {
		
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		filterChain = new MockFilterChain();
		objectMapper = new ObjectMapper();
		
	}

	@Test
	void doFilter_UnauthorizedException() throws Exception {
		request.addHeader("authorization", "Bearer token");
		
		when(tokenUtil.validateToken(any())).thenReturn(false);
		assertDoesNotThrow(() -> {filter.doFilter(request, response, filterChain);});
		ErrorResponseDto  errorResponseDto = 
				objectMapper.readValue(response.getContentAsString(), ErrorResponseDto.class);
		assertEquals("Invalid token" , errorResponseDto.getDescription());
		
	}
	
	@Test
	void doFilter_InvalidTokenException_NullHeader() throws Exception {
		
		assertDoesNotThrow(() -> {filter.doFilter(request, response, filterChain);});
		ErrorResponseDto  errorResponseDto = 
				objectMapper.readValue(response.getContentAsString(), ErrorResponseDto.class);
		assertEquals("Missing or invalid Authorization header" , errorResponseDto.getDescription());
	}
	
	@Test
	void doFilter_InvalidTokenException_MissingBearer() throws Exception {
		request.addHeader("authorization", "token");
		assertDoesNotThrow(() -> {filter.doFilter(request, response, filterChain);});
		ErrorResponseDto  errorResponseDto = 
				objectMapper.readValue(response.getContentAsString(), ErrorResponseDto.class);
		assertEquals("Missing or invalid Authorization header" , errorResponseDto.getDescription());
	}

}
