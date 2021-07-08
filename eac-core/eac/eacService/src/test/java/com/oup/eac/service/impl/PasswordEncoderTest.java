package com.oup.eac.service.impl;

import javax.naming.NamingException;

import org.junit.Test;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import com.oup.eac.common.mock.AbstractMockTest;

public class PasswordEncoderTest extends AbstractMockTest {

    private StandardPasswordEncoder passwordEncoder = new StandardPasswordEncoder();
    
    public PasswordEncoderTest() throws NamingException {
        super();
    }

    @Test
    public void encodePassword() {
        String password = "Passw0rd";
        String encodedPassword = passwordEncoder.encode(password);
        
        System.out.println("Encoded password: " + password + " as " + encodedPassword);
    }
}
