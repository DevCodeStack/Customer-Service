package com.eatza.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eatza.customer.dto.UserDto;
import com.eatza.customer.exception.CustomerException;
import com.eatza.customer.exception.InvalidTokenException;
import com.eatza.customer.exception.UnauthorizedException;
import com.eatza.customer.service.JwtAuthenticationService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
public class JwtAuthenticationController {

	@Autowired
	JwtAuthenticationService authenticationService;

	@PostMapping("/login")
	@ApiOperation(tags = "JwtAuthenticationController", value = "Token generation on customer login")
	public ResponseEntity<String> login(@RequestBody UserDto user) throws UnauthorizedException, CustomerException{

		log.debug("Calling authentication service to verify user");
		String token = authenticationService.authenticateUser(user);
		log.debug("User verified, returning back token");
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(token);
	}
	
	@GetMapping("/validate")
	@ApiOperation(tags = "JwtAuthenticationController", value = "Token validation")
	public ResponseEntity<Boolean> validateCustomerByToken(@RequestParam String token) throws InvalidTokenException {
		log.debug("In validateCustomerByToken method");
		return ResponseEntity.status(HttpStatus.OK).body(authenticationService.validateCustomer(token));
	}
}
