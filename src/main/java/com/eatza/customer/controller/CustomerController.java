package com.eatza.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eatza.customer.dto.CustomerDetailsUpdateDto;
import com.eatza.customer.dto.CustomerRegistrationDto;
import com.eatza.customer.dto.CustomerRegistrationResponseDto;
import com.eatza.customer.exception.CustomerException;
import com.eatza.customer.service.CustomerService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CustomerController {
	
	@Autowired
	CustomerService customerService;
	
	@PostMapping("/registration/customer")
	@ApiOperation(tags = "CustomerController", value = "Customer registration")
	public ResponseEntity<CustomerRegistrationResponseDto> userRegistration(@RequestBody CustomerRegistrationDto customer) throws CustomerException {
		log.debug("In userRegistration method");
		return ResponseEntity.status(HttpStatus.OK).body(customerService.addUser(customer));
	}
	
	@PutMapping("/customer/{id}/mail")
	@ApiOperation(tags = "CustomerController", value = "Updating customer mail id", authorizations = {@Authorization(value = "Bearer")})
	public ResponseEntity<CustomerDetailsUpdateDto> updateMailId(@PathVariable Long id, 
			@RequestParam String mailId) throws CustomerException {
		log.debug("In updateMailId method");
		return ResponseEntity.status(HttpStatus.OK).body(customerService.updateUserMailId(id, mailId));
	}
	
	@PutMapping("/customer/{id}/password")
	@ApiOperation(tags = "CustomerController", value = "Updating customer password", authorizations = {@Authorization(value = "Bearer")})
	public ResponseEntity<CustomerDetailsUpdateDto> updatePassword(@PathVariable Long id, 
			@RequestParam String oldPassword, @RequestParam String newPassword) throws CustomerException {
		log.debug("In updatePassword method");
		return ResponseEntity.status(HttpStatus.OK).body(customerService.updateUserPassword(id, oldPassword, newPassword));
	}
	
	@PutMapping("/customer/{id}/active")
	@ApiOperation(tags = "CustomerController", value = "Updating customer active flag", authorizations = {@Authorization(value = "Bearer")})
	public ResponseEntity<CustomerDetailsUpdateDto> updateActiveFlag(@PathVariable Long id, 
			@RequestParam char active) throws CustomerException {
		log.debug("In updateActiveFlag method");
		return ResponseEntity.status(HttpStatus.OK).body(customerService.updateUserActive(id, active));
	}

}
