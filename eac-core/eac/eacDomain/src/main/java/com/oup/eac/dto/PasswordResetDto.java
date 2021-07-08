package com.oup.eac.dto;

import java.io.Serializable;

/**
 * @author harlandd Password reset dto.
 */
public class PasswordResetDto implements Serializable {

    private String username;

    /**
     * @return the username
     */
    public final String getUsername() {
        return username;
    }

    /**
     * @param username
     *            the username
     */
    public final void setUsername(final String username) {
        this.username = username;
    }

}
