package com.eatza.customer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.eatza.customer.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
	@Modifying
	@Query(nativeQuery = true, value = "Update eatza.customers set mail_id = ?2, update_timestamp = CURRENT_TIMESTAMP where id = ?1")
	public Integer updateMailId(Long id, String mailId);
	
	@Modifying
	@Query(nativeQuery = true, value = "Update eatza.customers set password = ?2, update_timestamp = CURRENT_TIMESTAMP, password_last_update = CURRENT_TIMESTAMP where id = ?1")
	public Integer updatePassword(Long id, String password);
	
	@Modifying
	@Query(nativeQuery = true, value = "Update eatza.customers set active = ?2, update_timestamp = CURRENT_TIMESTAMP where id = ?1")
	public Integer updateUserActive(Long id, char active);
	
	@Query(nativeQuery = true, value = "select * from eatza.customers where username = ?1")
	public Optional<Customer> findByUsername(String username);
	
	@Query(nativeQuery = true, value = "select * from eatza.customers where id = ?1")
	public Optional<Customer> findById(Long id);
}
