package com.eatza.customer.util;

import com.eatza.customer.dto.CustomerRegistrationResponseDto;
import com.eatza.customer.model.Customer;

public class ModelToDtoParser {
	
	private ModelToDtoParser() {
		
	}
	
	public static CustomerRegistrationResponseDto parser(Customer customer) {
		return CustomerRegistrationResponseDto.builder()
				.id(customer.getId())
				.firstName(customer.getFirstName())
				.lastName(customer.getLastName())
				.userName(customer.getUserName())
				.mailId(customer.getMailId())
				.type(customer.getType())
				.passwordExpiry(customer.getPasswordExpiry())
				.build();
	}
}
