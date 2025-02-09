package com.eatza.customer.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.eatza.customer.dto.CustomerDetailsUpdateDto;
import com.eatza.customer.dto.CustomerRegistrationDto;
import com.eatza.customer.dto.CustomerRegistrationResponseDto;
import com.eatza.customer.dto.ErrorResponseDto;
import com.eatza.customer.exception.CustomGlobalExceptionHandler;
import com.eatza.customer.exception.CustomerException;
import com.eatza.customer.model.Customer;
import com.eatza.customer.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {
	
	private MockMvc mockMvc;
	
	@Mock
	private CustomerService customerService;
	
	@InjectMocks
	private CustomerController customerController;
	
	private CustomerRegistrationDto requestedCustomer;
	
	private JacksonTester<CustomerDetailsUpdateDto> jsonUpdateResponseDto;
	
	private JacksonTester<ErrorResponseDto> jsonErrorResponseDto;
	
	private JacksonTester<CustomerRegistrationDto> jsonRequestedCustomer;
	
	private Customer customer;
	
	@BeforeEach
	void setUp() {
		
		mockMvc = MockMvcBuilders.standaloneSetup(customerController)
				.setControllerAdvice(new CustomGlobalExceptionHandler())
				.build();
		
		JacksonTester.initFields(this, new ObjectMapper());
		
		requestedCustomer = new CustomerRegistrationDto();
		requestedCustomer.setFirstName("Fname");
		requestedCustomer.setLastName("Lname");
		requestedCustomer.setUserName("fl234");
		requestedCustomer.setMailId("fn243@xyz.com");
		requestedCustomer.setPassword("password");
		
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
		customer.setPasswordExpiry(LocalDateTime.of(LocalDate.now(), LocalTime.now()).plusDays(90));
	}
	
	@Test
    void testCustomerDetailsByIdSuccess() throws Exception {
        when(customerService.fetchById(any())).thenReturn(customer);

        mockMvc.perform(get("/customer/id/1"))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.mailid()").exists());

        verify(customerService, Mockito.times(1)).fetchById(1l);
    }
	
	@Test
    void testCustomerDetailsByNameSuccess() throws Exception {
        when(customerService.fetchByUsername(anyString())).thenReturn(customer);

        mockMvc.perform(get("/customer/name/fl234"))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.mailid()").exists());

        verify(customerService, Mockito.times(1)).fetchByUsername(anyString());
    }

	//Positive test case : userRegistration
	@Test
	public void userRegistrationSuccess() throws Exception {
		
		CustomerRegistrationResponseDto responseDto = new CustomerRegistrationResponseDto();
		responseDto.setId(1l);
		responseDto.setFirstName("Fname");
		responseDto.setLastName("Lname");
		responseDto.setUserName("fl234");
		responseDto.setMailId("fn243@xyz.com");
		responseDto.setType("customer");
		responseDto.setPasswordExpiry(LocalDateTime.now());
			
		
		when(customerService.addUser(any())).thenReturn(responseDto);
		
		MockHttpServletResponse response = mockMvc.perform(
				post("/registration/customer").contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequestedCustomer.write(requestedCustomer).getJson()))
				.andReturn().getResponse();
		
		assertEquals(200, response.getStatus());
	}
	
	//Negative test case : userRegistration
	@Test
	public void userRegistrationFailed() throws Exception {
		
		when(customerService.addUser(any()))
		.thenThrow(new CustomerException("Failed to add user"));
		
		MockHttpServletResponse response = mockMvc.perform(
				post("/registration/customer").contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequestedCustomer.write(requestedCustomer).getJson()))
				.andReturn().getResponse();
		
		ErrorResponseDto responseDto = jsonErrorResponseDto.parseObject(response.getContentAsString());
		
		assertEquals("Failed to add user", responseDto.getDescription());
	}
	
	//Positive test case : updateMailId
	@Test
	public void updateMailIdSuccess() throws Exception {		
		Long id = 1l;
		String mailId = "fn243@xyz.com";
		CustomerDetailsUpdateDto customerDetailsUpdateDto = 
				CustomerDetailsUpdateDto.builder()
					.field("MailId")
					.status("Field mailId changed successfully")
					.build();
		when(customerService.updateUserMailId(any(), any())).thenReturn(customerDetailsUpdateDto);
		
		MockHttpServletResponse response = mockMvc.perform(
				put("/customer/"+id+"/mail").accept(MediaType.APPLICATION_JSON)
				.param("mailId", mailId))
				.andReturn().getResponse();
		
		CustomerDetailsUpdateDto detailsUpdateDto = jsonUpdateResponseDto.parse(response.getContentAsString()).getObject();
				
		assertEquals("Field mailId changed successfully", detailsUpdateDto.getStatus());
	}
	
	//Negative test case : updateMailId
	@Test
	public void updateMailIdFailed() throws Exception {		
		Long id = 1l;
		String mailId = "fn243@xyz.com";
		when(customerService.updateUserMailId(id, mailId))
		.thenThrow(new CustomerException("Mail id update failed"));
		
		MockHttpServletResponse response = mockMvc.perform(
				put("/customer/"+id+"/mail").accept(MediaType.APPLICATION_JSON)
				.param("mailId", mailId))
				.andReturn().getResponse();
		
		ErrorResponseDto responseDto = jsonErrorResponseDto.parseObject(response.getContentAsString());
		
		assertEquals("Mail id update failed", responseDto.getDescription());
	}
	
	//Positive test case : updatePassword
	@Test
	public void updatePasswordSuccess() throws Exception {		
		Long id = 1l;
		String oldPassword = "oldPassword";
		String newPassword = "newPassword";
		CustomerDetailsUpdateDto customerDetailsUpdateDto = 
				CustomerDetailsUpdateDto.builder()
					.field("Password")
					.status("Field password changed successfully")
					.build();
		when(customerService.updateUserPassword(any(), any(), any())).thenReturn(customerDetailsUpdateDto);	
		
		MockHttpServletResponse response = mockMvc.perform(
				put("/customer/"+id+"/password").accept(MediaType.APPLICATION_JSON)
				.param("oldPassword", oldPassword)
				.param("newPassword", newPassword))
				.andReturn().getResponse();
		
		CustomerDetailsUpdateDto detailsUpdateDto = jsonUpdateResponseDto.parse(response.getContentAsString()).getObject();
		
		assertEquals("Field password changed successfully", detailsUpdateDto.getStatus());
	}
	
	//Negative test case : updatePassword
	@Test
	public void updatePasswordFailed() throws Exception {
		Long id = 1l;
		String oldPassword = "oldPassword";
		String newPassword = "newPassword";
		when(customerService.updateUserPassword(id, oldPassword, newPassword))
		.thenThrow(new CustomerException("Password update failed"));
		
		MockHttpServletResponse response = mockMvc.perform(
				put("/customer/"+id+"/password").accept(MediaType.APPLICATION_JSON)
				.param("oldPassword", oldPassword)
				.param("newPassword", newPassword))
				.andReturn().getResponse();
		
		ErrorResponseDto responseDto = jsonErrorResponseDto.parseObject(response.getContentAsString());
		
		assertEquals("Password update failed", responseDto.getDescription());
	}
	
	//Positive test case : updateActiveFlag
	@Test
	public void updateActiveFlagSuccess() throws Exception {
		Long id = 1l;
		String active = "Y";
		CustomerDetailsUpdateDto updateRequestDto = new CustomerDetailsUpdateDto();
		updateRequestDto.setField("Active");
		updateRequestDto.setStatus("Field active changed successfully");
		
		when(customerService.updateUserActive(id, active.charAt(0))).thenReturn(updateRequestDto);
		
		MockHttpServletResponse response = mockMvc.perform(
				put("/customer/"+id+"/active").accept(MediaType.APPLICATION_JSON)
				.param("active", active))
				.andReturn().getResponse();
		
		CustomerDetailsUpdateDto updateResponseDto = 
				jsonUpdateResponseDto.parseObject(response.getContentAsString());
		
		assertEquals(updateRequestDto.getField(), updateResponseDto.getField());
	}
	
	//Negative test case : updateActiveFlag
	@Test
	public void updateActiveFlagFailed() throws Exception {
		Long id = 1l;
		String active = "Y";
		when(customerService.updateUserActive(id, active.charAt(0)))
			.thenThrow(new CustomerException("Active flag update failed"));
		
		MockHttpServletResponse response = mockMvc.perform(
				put("/customer/"+id+"/active").accept(MediaType.APPLICATION_JSON)
				.param("active", active))
				.andReturn().getResponse();
		
		ErrorResponseDto responseDto = jsonErrorResponseDto.parseObject(response.getContentAsString());
		
		assertEquals("Active flag update failed", responseDto.getDescription());
	}
	
	//Negative test case : updateActiveFlag
	@Test
	public void updateActiveFlag_Exception() throws Exception {
		Long id = 1l;
		String active = "Y";
		when(customerService.updateUserActive(id, active.charAt(0)))
			.thenThrow(new RuntimeException("Active flag update failed"));
		
		MockHttpServletResponse response = mockMvc.perform(
				put("/customer/"+id+"/active").accept(MediaType.APPLICATION_JSON)
				.param("active", active))
				.andReturn().getResponse();
		
		ErrorResponseDto responseDto = jsonErrorResponseDto.parseObject(response.getContentAsString());
		
		assertEquals("EX500", responseDto.getCode());
	}
	
}
