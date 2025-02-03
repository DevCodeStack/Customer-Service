package com.eatza.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TokenDto {
	
	private String token;
	private long expiry;
}
