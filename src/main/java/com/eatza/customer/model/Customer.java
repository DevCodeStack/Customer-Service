package com.eatza.customer.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="customers")
@Getter @Setter @NoArgsConstructor
public class Customer {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	@Column(name = "firstname", nullable = false)
	private String firstName;
	@Column(name = "lastname", nullable = false)
	private String lastName;
	@Column(name = "username", nullable = false)
	private String userName;
	@Column(name = "mail_id", nullable = false)
	private String mailId;
	@Column(name = "active")
	private char active = 'Y';
	@Column(name = "customer")
	private String type = "customer";
	@Column(name = "password", nullable = false)
	private String password;
	@Column(name = "create_timestamp")
    private LocalDateTime createTimestamp = LocalDateTime.now();
	@Column(name = "update_timestamp")
    private LocalDateTime updateTimestamp = LocalDateTime.now();
	@Column(name = "password_last_update")
    private LocalDateTime passwordLastUpdate = LocalDateTime.now();
	@Column(name = "password_expiry")
    private LocalDateTime passwordExpiry;
    @Column(name = "password_expired")
    private char passwordExpired = 'N';
	
	
}
