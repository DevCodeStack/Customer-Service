package com.eatza.customer.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eatza.customer.dto.CustomerDetailsUpdateDto;
import com.eatza.customer.dto.CustomerRegistrationDto;
import com.eatza.customer.dto.CustomerRegistrationResponseDto;
import com.eatza.customer.exception.CustomerException;
import com.eatza.customer.model.Customer;
import com.eatza.customer.repository.CustomerRepository;
import com.eatza.customer.util.CommonUtil;
import com.eatza.customer.util.ErrorCodesEnum;
import com.eatza.customer.util.JwtTokenUtil;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
	
	@Mock
	CustomerRepository customerRepository;
	
	@Mock
	JwtTokenUtil tokenUtil;
	
	@InjectMocks
	CustomerServiceImpl customerServiceImpl;

	public CustomerRegistrationDto customerRegistration;
	
	public Customer customer;
	
	Integer passwordExpiryDays = 90;
	
	@BeforeEach
	void setUp() throws Exception {
		
		customerServiceImpl.setPasswordExpiryDays(passwordExpiryDays);
		
		customerRegistration = new CustomerRegistrationDto();
		customerRegistration.setFirstName("Fname");
		customerRegistration.setLastName("Lname");
		customerRegistration.setUserName("fl234");
		customerRegistration.setMailId("fn243@xyz.com");
		customerRegistration.setPassword("password");
		
		customer = new Customer();
		customer.setFirstName("Fname");
		customer.setLastName("Lname");
		customer.setUserName("fl234");
		customer.setMailId("fn243@xyz.com");
		customer.setPassword("password");
		customer.setActive('Y');
		customer.setId(1l);
		customer.setPasswordExpired('N');
		customer.setCreateTimestamp(LocalDateTime.now());
		customer.setUpdateTimestamp(LocalDateTime.now());
		customer.setPasswordLastUpdate(LocalDateTime.now());
		customer.setPasswordExpiry(LocalDateTime.of(LocalDate.now(), LocalTime.now()).plusDays(passwordExpiryDays));
	}
	
	//Positive case : addUser
	@Test
	void addUserSuccess() {
		
		when(customerRepository.save(any())).thenReturn(customer);
		CustomerRegistrationResponseDto responseDto = 
				customerServiceImpl.addUser(customerRegistration);
		
		assertEquals(customer.getMailId(), responseDto.getMailId());
	}
	
	//Negative case : addUser
	@Test
	void addUserFailed_SQLException() {
		
		when(customerRepository.save(any())).thenThrow(new RuntimeException());
		assertThrows(CustomerException.class, () -> {customerServiceImpl.addUser(customerRegistration);});
	}
	
	//Positive test case : updateUserMailId
	@Test
	void updateUserMailIdSuccess() {
		Long id = 1l;
		String mailId = "fn243@xyz.com";
		
		when(customerRepository.updateMailId(any(), any())).thenReturn(1);
		CustomerDetailsUpdateDto customerDetailsUpdateDto = 
				customerServiceImpl.updateUserMailId(id, mailId);
		
		assertEquals("Field mailId changed successfully", customerDetailsUpdateDto.getStatus());
		
	}
	
	//Negative case : updateUserMailId
	@Test
	void updateUserMailId_CustomException() {
		Long id = 1l;
		String mailId = "fn243@xyz.com";
		
		when(customerRepository.updateMailId(any(), any())).thenReturn(0);
		assertThrows(CustomerException.class, () -> {customerServiceImpl.updateUserMailId(id, mailId);});
	}
	
	//Negative case : updateUserMailId
	@Test
	void updateUserMailId_SQLException() {
		Long id = 1l;
		String mailId = "fn243@xyz.com";
		
		when(customerRepository.updateMailId(any(), any())).thenThrow(new RuntimeException());
		assertThrows(CustomerException.class, () -> {customerServiceImpl.updateUserMailId(id, mailId);});
	}
	
	//Positive test case : updateUserPassword
	@Test
	void updateUserPasswordSuccess() {
		Long id = 1l;
		String oldPassword = "oldpassword";
		String newPassword = "newpassword";
		customer.setPassword(CommonUtil.passwordEncoder().encode(oldPassword));
		
		when(customerRepository.findById(any())).thenReturn(Optional.of(customer));
		when(customerRepository.updatePassword(any(), any())).thenReturn(1);
		CustomerDetailsUpdateDto customerDetailsUpdateDto = 
				customerServiceImpl.updateUserPassword(id, oldPassword, newPassword);
		
		assertEquals("Field password changed successfully", customerDetailsUpdateDto.getStatus());
		
	}
	
	//Negative case : updateUserPassword
	@Test
	void updateUserPassword_EmptyObject() {
		Long id = 1l;
		String oldPassword = "oldpassword";
		String newPassword = "newpassword";
		customer.setPassword(CommonUtil.passwordEncoder().encode(oldPassword));
		
		when(customerRepository.findById(any())).thenReturn(Optional.empty());
		assertThrows(CustomerException.class, () -> {customerServiceImpl.updateUserPassword(id, oldPassword, newPassword);});
	}
	
	//Negative case : updateUserPassword
	@Test
	void updateUserPassword_PasswordMismatch() {
		Long id = 1l;
		String oldPassword = "oldpassword";
		String newPassword = "newpassword";
		customer.setPassword(CommonUtil.passwordEncoder().encode(customer.getPassword()));
		
		when(customerRepository.findById(any())).thenReturn(Optional.of(customer));
		CustomerException thrown = assertThrows(CustomerException.class, 
				() -> {customerServiceImpl.updateUserPassword(id, oldPassword, newPassword);});
		assertEquals(ErrorCodesEnum.INTERNAL_SERVER_ERROR.getCode(), thrown.getError().getCode());
		assertEquals(ErrorCodesEnum.INTERNAL_SERVER_ERROR.getMsg(), thrown.getError().getMsg());
		assertEquals("User old password incorrect", thrown.getMessage());
	}
	
	//Negative case : updateUserPassword
	@Test
	void updateUserPassword_InactiveUser() {
		Long id = 1l;
		String oldPassword = "oldpassword";
		String newPassword = "newpassword";
		customer.setPassword(CommonUtil.passwordEncoder().encode(oldPassword));
		customer.setActive('N');
		
		when(customerRepository.findById(any())).thenReturn(Optional.of(customer));
		assertThrows(CustomerException.class, () -> {customerServiceImpl.updateUserPassword(id, oldPassword, newPassword);});
	}
	
	//Negative case : updateUserPassword
	@Test
	void updateUserPassword_CustomException() {
		Long id = 1l;
		String oldPassword = "oldpassword";
		String newPassword = "newpassword";
		customer.setPassword(CommonUtil.passwordEncoder().encode(oldPassword));
		
		when(customerRepository.findById(any())).thenReturn(Optional.of(customer));
		when(customerRepository.updatePassword(any(), any())).thenReturn(0);
		assertThrows(CustomerException.class, () -> {customerServiceImpl.updateUserPassword(id, oldPassword, newPassword);});
	}
	
	//Negative case : updateUserPassword
	@Test
	void updateUserPassword_SQLException() {
		Long id = 1l;
		String oldPassword = "oldpassword";
		String newPassword = "newpassword";
		customer.setPassword(CommonUtil.passwordEncoder().encode(oldPassword));
		
		when(customerRepository.findById(any())).thenReturn(Optional.of(customer));
		when(customerRepository.updatePassword(any(), any())).thenThrow(new RuntimeException());
		assertThrows(CustomerException.class, () -> {customerServiceImpl.updateUserPassword(id, oldPassword, newPassword);});
	}
	
	//Positive test case : updateUserActive
	@Test
	void updateUserActiveSuccess() {
		Long id = 1l;
		char active = 'Y';
		
		when(customerRepository.updateUserActive(id, active)).thenReturn(1);
		CustomerDetailsUpdateDto customerDetailsUpdateDto = 
				customerServiceImpl.updateUserActive(id, active);
		
		assertEquals("Field active changed successfully", customerDetailsUpdateDto.getStatus());
		
	}
	
	//Negative case : updateUserActive
	@Test
	void updateUserActive_CustomException() {
		Long id = 1l;
		char active = 'Y';
		
		when(customerRepository.updateUserActive(id, active)).thenReturn(0);
		assertThrows(CustomerException.class, () -> {customerServiceImpl.updateUserActive(id, active);});
	}
	
	//Negative case : updateUserActive
	@Test
	void updateUserActive_SQLException() {
		Long id = 1l;
		char active = 'Y';
		
		when(customerRepository.updateUserActive(id, active)).thenThrow(new RuntimeException());
		assertThrows(CustomerException.class, () -> {customerServiceImpl.updateUserActive(id, active);});
	}

}
