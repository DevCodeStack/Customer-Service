package com.eatza.customer.service;

import static com.eatza.customer.util.ErrorCodesEnum.AUTHENTICATION_ERROR;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatza.customer.dto.TokenDto;
import com.eatza.customer.dto.UserDto;
import com.eatza.customer.exception.CustomerException;
import com.eatza.customer.exception.InvalidTokenException;
import com.eatza.customer.exception.UnauthorizedException;
import com.eatza.customer.model.Customer;
import com.eatza.customer.repository.CustomerRepository;
import com.eatza.customer.util.CommonUtil;
import com.eatza.customer.util.JwtTokenUtil;

@Service
public class JwtAuthenticationServiceImpl implements JwtAuthenticationService {
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	JwtTokenUtil tokenUtil;
	
	@Override
	public TokenDto authenticateUser(UserDto userDto) throws UnauthorizedException, CustomerException {
		try {
			Optional<Customer> cust = customerRepository.findByUsername(userDto.getUsername());
			if(!cust.isPresent())
				throw new UnauthorizedException("User not present");
			Customer customer = cust.get();
			if(customer.getActive() != 'Y')
				throw new UnauthorizedException("User inactive");
			if(customer.getPasswordExpired() != 'N')
				throw new UnauthorizedException("User password expired");
			if(CommonUtil.passwordEncoder().matches(userDto.getPassword(), customer.getPassword()))
				return tokenUtil.generateToken(customer);
			
			throw new UnauthorizedException("User password incorrect");
			
		} catch (UnauthorizedException uex){
			uex.setError(AUTHENTICATION_ERROR);
			throw uex;
		} catch (Exception ex){
			throw new CustomerException(ex.getMessage());
		}
	}
	
	@Override
	public Boolean validateCustomer(String token) throws InvalidTokenException {
		return tokenUtil.validateToken(token);
	}

}
