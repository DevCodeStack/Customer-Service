package com.eatza.customer.util;

import static com.eatza.customer.util.ErrorCodesEnum.TOKEN_ERROR;

import java.io.Serializable;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eatza.customer.dto.TokenDto;
import com.eatza.customer.exception.InvalidTokenException;
import com.eatza.customer.exception.UnauthorizedException;
import com.eatza.customer.model.Customer;
import com.eatza.customer.repository.CustomerRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;

@Data
@Component
public class JwtTokenUtil implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1961936149219108137L;
	
	@Autowired
	CustomerRepository customerRepository;

	public static final long JWT_TOKEN_VALIDITY = 1000*60*10;
	
	@Value("${jwt.secret}")
	private String secret;
	
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	public String getIdFromToken(String token) {
		return getClaimFromToken(token, Claims::getId);
	}
	
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}
	
	public Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	public TokenDto generateToken(Customer customer) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", customer.getId());
		return TokenDto.builder()
				.token(finalToken(claims, customer))
				.expiry(JWT_TOKEN_VALIDITY).build();
	}
	
	private String finalToken(Map<String, Object> claims, Customer customer) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(customer.getUserName())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setId(Long.toString(customer.getId()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
				.signWith(getSignKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
	
	public Boolean validateToken(String token) throws InvalidTokenException {
		try {
			final String userName = getUsernameFromToken(token);
			Long id = Long.parseLong(getIdFromToken(token));
			Optional<Customer> cust = customerRepository.findById(id);
			if(cust.isPresent()) {
				Customer customer = cust.get();
				if(customer.getActive() == 'Y')
					return (userName.equals(customer.getUserName()) 
							&& !isTokenExpired(token));
			}
			throw new UnauthorizedException("Customer not present or active");
			
		} catch(Exception ex) {
			throw new InvalidTokenException(ex.getMessage(), 
					TOKEN_ERROR);
		} 
	}
	
}
