package com.eatza.customer.service;

import com.eatza.customer.dto.UserDto;
import com.eatza.customer.exception.CustomerException;
import com.eatza.customer.exception.InvalidTokenException;
import com.eatza.customer.exception.UnauthorizedException;

public interface JwtAuthenticationService {
	
	String authenticateUser(UserDto userDto) throws UnauthorizedException, CustomerException;
	
	Boolean validateCustomer(String token) throws InvalidTokenException;
}
