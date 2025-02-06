package com.eatza.customer.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="customers", schema = "eatza")
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
	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(name = "password", nullable = false)
	private String password;
	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(name = "create_timestamp")
    private LocalDateTime createTimestamp = LocalDateTime.now();
	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(name = "update_timestamp")
    private LocalDateTime updateTimestamp = LocalDateTime.now();
	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(name = "password_last_update")
    private LocalDateTime passwordLastUpdate = LocalDateTime.now();
	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(name = "password_expiry")
    private LocalDateTime passwordExpiry;
    @Column(name = "password_expired")
    private char passwordExpired = 'N';
	
	
}
