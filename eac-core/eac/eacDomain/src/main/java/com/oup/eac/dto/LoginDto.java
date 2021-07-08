package com.oup.eac.dto;

import java.io.Serializable;

/**
 * @author harlandd Login registration dto.
 */
public class LoginDto implements Serializable {

    private String username;

    private String password;

    /**
     * @return the username
     */
    public final String getUsername() {
        return username;
    }

    /**
     * 
     * @param username
     *            the username
     */
    public final void setUsername(final String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public final String getPassword() {
        return password;
    }

    /**
     * 
     * @param password
     *            the password
     */
    public final void setPassword(final String password) {
        this.password = password;
    }
}
