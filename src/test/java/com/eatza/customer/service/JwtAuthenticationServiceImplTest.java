package com.eatza.customer.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eatza.customer.dto.TokenDto;
import com.eatza.customer.dto.UserDto;
import com.eatza.customer.exception.CustomerException;
import com.eatza.customer.exception.UnauthorizedException;
import com.eatza.customer.model.Customer;
import com.eatza.customer.repository.CustomerRepository;
import com.eatza.customer.util.CommonUtil;
import com.eatza.customer.util.JwtTokenUtil;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationServiceImplTest {

	@Mock
	CustomerRepository customerRepository;
	
	@Mock
	JwtTokenUtil tokenUtil;
	
	@InjectMocks
	JwtAuthenticationServiceImpl authenticationServiceImpl;
	
	public UserDto userDto;
	
	public Customer customer;
	
	@BeforeEach
	void setUp() throws Exception {
		
		userDto = new UserDto();
		userDto.setPassword("password");
		userDto.setUsername("username");
		
		customer = new Customer();
		customer.setFirstName("Fname");
		customer.setLastName("Lname");
		customer.setUserName("fl234");
		customer.setMailId("fn243@xyz.com");
		customer.setPassword("password");
		customer.setActive('Y');
	}

	//Positive test case : authenticateUser
	@Test
	void authenticateUserSuccess() {
		var token = TokenDto.builder().token("myToken").build();
		customer.setPassword(CommonUtil.passwordEncoder().encode(customer.getPassword()));
		
		when(customerRepository.findByUsername(any())).thenReturn(Optional.of(customer));
		when(tokenUtil.generateToken(any())).thenReturn(token);
		
		var generatedToken = authenticationServiceImpl.authenticateUser(userDto);
		assertEquals(token.getToken(), generatedToken.getToken());
		
	}
	
	//Negative test case : authenticateUser
	@Test
	void authenticateUser_EmptyObject() {
		
		when(customerRepository.findByUsername(any())).thenReturn(Optional.empty());
		
		assertThrows(UnauthorizedException.class, () -> {authenticationServiceImpl.authenticateUser(userDto);});
		
	}
	
	//Negative test case : authenticateUser
	@Test
	void authenticateUser_PasswordMismatch() {
		String password = "newpassword";
		customer.setPassword(CommonUtil.passwordEncoder().encode(password));
		
		when(customerRepository.findByUsername(any())).thenReturn(Optional.of(customer));
		
		assertThrows(UnauthorizedException.class, () -> {authenticationServiceImpl.authenticateUser(userDto);});
		
	}
	
	//Negative test case : authenticateUser
	@Test
	void authenticateUser_Inactive() {
		customer.setPassword(CommonUtil.passwordEncoder().encode(customer.getPassword()));
		customer.setActive('N');
		
		when(customerRepository.findByUsername(any())).thenReturn(Optional.of(customer));
		
		assertThrows(UnauthorizedException.class, () -> {authenticationServiceImpl.authenticateUser(userDto);});
		
	}
	
	//Negative test case : authenticateUser
	@Test
	void authenticateUser_PasswordExpired() {
		customer.setPassword(CommonUtil.passwordEncoder().encode(customer.getPassword()));
		customer.setPasswordExpired('Y');
		
		when(customerRepository.findByUsername(any())).thenReturn(Optional.of(customer));
		
		assertThrows(UnauthorizedException.class, () -> {authenticationServiceImpl.authenticateUser(userDto);});
		
	}
	
	//Negative test case : authenticateUser
	@Test
	void authenticateUser_SQLException() {
		
		when(customerRepository.findByUsername(any())).thenThrow(new RuntimeException());
		
		assertThrows(CustomerException.class, () -> {authenticationServiceImpl.authenticateUser(userDto);});
		
	}
	
	//Positive test case : validateCustomer
	@Test
	void validateCustomerSuccess() {
		String token = "token";
		
		when(tokenUtil.validateToken(any())).thenReturn(true);
		
		Boolean status = authenticationServiceImpl.validateCustomer(token);
		assertEquals(true, status);
		
	}
	
	//Negative test case : validateCustomer
	@Test
	void validateCustomerFailed() {
		String token = "token";
		
		when(tokenUtil.validateToken(any())).thenReturn(false);
		
		Boolean status = authenticationServiceImpl.validateCustomer(token);
		assertEquals(false, status);
		
	}

}
