package com.eatza.customer.service;

import static com.eatza.customer.util.ErrorCodesEnum.SQL_UPDATE_FAILED;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatza.customer.dto.CustomerDetailsUpdateDto;
import com.eatza.customer.dto.CustomerRegistrationDto;
import com.eatza.customer.dto.CustomerRegistrationResponseDto;
import com.eatza.customer.exception.CustomerException;
import com.eatza.customer.model.Customer;
import com.eatza.customer.repository.CustomerRepository;
import com.eatza.customer.util.CommonUtil;
import com.eatza.customer.util.JwtTokenUtil;
import com.eatza.customer.util.ModelToDtoParser;

import lombok.Data;

@Data
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	JwtTokenUtil tokenUtil;
	
	@Value("${password.expiry.days}")
	Integer passwordExpiryDays;

	@Override
	public CustomerRegistrationResponseDto addUser(CustomerRegistrationDto customerDto) throws CustomerException {
		try {
			
			String password = CommonUtil.passwordEncoder().encode(customerDto.getPassword());
			Customer customer = new Customer();
			customer.setFirstName(customerDto.getFirstName());
			customer.setLastName(customerDto.getLastName());
			customer.setUserName(customerDto.getUserName());
			customer.setPassword(password);
			customer.setMailId(customerDto.getMailId());
			customer.setPasswordExpiry(LocalDateTime.of(LocalDate.now(), LocalTime.now()).plusDays(passwordExpiryDays));
									
			Customer savedCustomer = customerRepository.save(customer);
			if(savedCustomer != null)
				return ModelToDtoParser.parser(savedCustomer);
			
			throw new CustomerException("Failed to add user");
			
		} catch (Exception ex){
			if(!(ex instanceof CustomerException))
				throw new CustomerException(ex.getMessage());
			throw (CustomerException) ex;
		}
	}

	@Override
	public CustomerDetailsUpdateDto updateUserMailId(Long id, String mailId) throws CustomerException {
		try {
		
			if(customerRepository.updateMailId(id, mailId) == 1) 
				return CustomerDetailsUpdateDto.builder()
						.field("MailId")
						.status("Field mailId changed successfully")
						.build();
			
			throw new CustomerException("Mail id update failed", SQL_UPDATE_FAILED);
			
		} catch (Exception ex){
			if(!(ex instanceof CustomerException))
				throw new CustomerException(ex.getMessage());
			throw (CustomerException) ex;
		}
	}

	@Override
	public CustomerDetailsUpdateDto updateUserPassword(Long id, String oldPassword, String newPassword) throws CustomerException {
		try {
			
			Optional<Customer> cust = customerRepository.findById(id);
			if(!cust.isPresent())
				throw new CustomerException("User not present");
			Customer customer = cust.get();
			if(customer.getActive() != 'Y') 
				throw new CustomerException("User inactive ");
			if(!CommonUtil.passwordEncoder().matches(oldPassword, customer.getPassword())) {
				throw new CustomerException("User old password incorrect");
			}
			String password = CommonUtil.passwordEncoder().encode(newPassword);
			if(customerRepository.updatePassword(id, password) == 1) 
				return CustomerDetailsUpdateDto.builder()
						.field("Password")
						.status("Field password changed successfully")
						.build();
			
			throw new CustomerException("Password update failed", SQL_UPDATE_FAILED);
			
		} catch (Exception ex){
			if(!(ex instanceof CustomerException))
				throw new CustomerException(ex.getMessage());
			throw (CustomerException) ex;
		}
	}

	@Override
	public CustomerDetailsUpdateDto updateUserActive(Long id, char active) throws CustomerException {
		try {
			
			if(customerRepository.updateUserActive(id, active) == 1) 
				return CustomerDetailsUpdateDto.builder()
						.field("Active")
						.status("Field active changed successfully")
						.build();
			
			throw new CustomerException("Active flag update failed", SQL_UPDATE_FAILED);
			
		} catch (Exception ex){
			if(!(ex instanceof CustomerException))
				throw new CustomerException(ex.getMessage());
			throw (CustomerException) ex;
		}
	}

}
