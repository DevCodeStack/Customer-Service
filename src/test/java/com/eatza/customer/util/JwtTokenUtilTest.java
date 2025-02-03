package com.eatza.customer.util;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eatza.customer.exception.InvalidTokenException;
import com.eatza.customer.model.Customer;
import com.eatza.customer.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class JwtTokenUtilTest {

	@Mock
	CustomerRepository customerRepository;
	
	@InjectMocks
	JwtTokenUtil tokenUtil;
	
	public Customer customer;
	
	@BeforeEach
	void setUp() throws Exception {
		
		tokenUtil.setSecret("0194cc426c227da58721b57cdde3fc8b6se4cr67et1k56ey9");
		
		customer = new Customer();
		customer.setFirstName("Fname");
		customer.setLastName("Lname");
		customer.setUserName("fl234");
		customer.setMailId("fn243@xyz.com");
		customer.setPassword("password");
		customer.setActive('Y');
		customer.setId(1l);
		customer.setType("customer");
		customer.setPasswordExpired('N');
		customer.setPasswordLastUpdate(LocalDateTime.now());
		customer.setCreateTimestamp(LocalDateTime.now());
		customer.setUpdateTimestamp(LocalDateTime.now());
	}

	@Test
	void generateToken_Success() {
		
		var token = tokenUtil.generateToken(customer);
		assertEquals(customer.getUserName(), tokenUtil.getUsernameFromToken(token.getToken()));
		
	}
	
	@Test
	void validateToken_ValidToken() {
		var token = tokenUtil.generateToken(customer);
		
		when(customerRepository.findById(any())).thenReturn(Optional.of(customer));
		assertThat(tokenUtil.validateToken(token.getToken()));
		
	}
	
	@Test
	void validateToken_InvalidToken() {
		var token = tokenUtil.generateToken(customer);
		customer.setUserName("FirstName");
		
		when(customerRepository.findById(any())).thenReturn(Optional.of(customer));
		assertEquals(false, tokenUtil.validateToken(token.getToken()));
		
	}
	
	@Test
	void validateToken_CustomException() {
		var token = tokenUtil.generateToken(customer);
		
		when(customerRepository.findById(any())).thenReturn(Optional.empty());
		assertThrows(InvalidTokenException.class, () -> {tokenUtil.validateToken(token.getToken());});
		
		customer.setActive('N');
		when(customerRepository.findById(any())).thenReturn(Optional.of(customer));
		assertThrows(InvalidTokenException.class, () -> {tokenUtil.validateToken(token.getToken());});
		
	}
	
	@Test
	void validateToken_SQLException() {
		var token = tokenUtil.generateToken(customer);
		
		when(customerRepository.findById(any())).thenThrow(new RuntimeException());
		assertThrows(InvalidTokenException.class, () -> {tokenUtil.validateToken(token.getToken());});
		
	}

}
