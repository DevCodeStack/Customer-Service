package com.eatza.customer.service;

import com.eatza.customer.dto.TokenDto;
import com.eatza.customer.dto.UserDto;
import com.eatza.customer.exception.CustomerException;
import com.eatza.customer.exception.InvalidTokenException;
import com.eatza.customer.exception.UnauthorizedException;

public interface JwtAuthenticationService {
	
	TokenDto authenticateUser(UserDto userDto) throws UnauthorizedException, CustomerException;
	
	Boolean validateCustomer(String token) throws InvalidTokenException;
}
