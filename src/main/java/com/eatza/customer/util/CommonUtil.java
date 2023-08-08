package com.eatza.customer.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CommonUtil {
	
	public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
}
