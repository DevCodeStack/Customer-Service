package com.eatza.customer.service;

import com.eatza.customer.dto.CustomerDetailsUpdateDto;
import com.eatza.customer.dto.CustomerRegistrationDto;
import com.eatza.customer.dto.CustomerRegistrationResponseDto;
import com.eatza.customer.exception.CustomerException;

public interface CustomerService {

	CustomerRegistrationResponseDto addUser(CustomerRegistrationDto customer) throws CustomerException;

	CustomerDetailsUpdateDto updateUserMailId(Long id, String mailId) throws CustomerException;

	CustomerDetailsUpdateDto updateUserPassword(Long id, String oldPassword, String newPassword) throws CustomerException;

	CustomerDetailsUpdateDto updateUserActive(Long id, char active) throws CustomerException;

}
