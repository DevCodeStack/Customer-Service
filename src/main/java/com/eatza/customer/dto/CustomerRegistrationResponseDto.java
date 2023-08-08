package com.eatza.customer.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class CustomerRegistrationResponseDto {
	
	private Long id;
	private String firstName;
	private String lastName;
	private String userName;
	private String mailId;
	private String type;
	private LocalDateTime passwordExpiry;
}
