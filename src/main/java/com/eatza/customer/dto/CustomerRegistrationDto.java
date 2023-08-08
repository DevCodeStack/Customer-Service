package com.eatza.customer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter @NoArgsConstructor
public class CustomerRegistrationDto {
	
	private String firstName;
	private String lastName;
	private String userName;
	private String mailId;
	private String password;
	
}
