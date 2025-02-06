package com.eatza.customer.exception;

import com.eatza.customer.util.ErrorCodesEnum;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomerException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8554789485569010954L;
	
	private ErrorCodesEnum error;

	public CustomerException() {
		super(ErrorCodesEnum.INTERNAL_SERVER_ERROR.getMsg());
		this.error = ErrorCodesEnum.INTERNAL_SERVER_ERROR;
	}

	public CustomerException(String message) {
		super(message);
		this.error = ErrorCodesEnum.INTERNAL_SERVER_ERROR;
	}
	
	public CustomerException(String message, Throwable exception) {
		super(message, exception);
		this.error = ErrorCodesEnum.INTERNAL_SERVER_ERROR;
	}
	
	public CustomerException(String message, ErrorCodesEnum error) {
		super(message);
		this.error = error;
	}

}
