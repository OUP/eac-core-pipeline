/**
 * 
 */
package com.oup.eac.dto;

/**
 * @author harlandd
 * 
 */
public class EmailTokenDto {

    private String userId;

    private String orignalUrl;

    /**
     * Default constructor.
     */
    public EmailTokenDto() {
        super();
    }

    public EmailTokenDto(String userId, String orignalUrl) {
        super();
        this.userId = userId;
        this.orignalUrl = orignalUrl;
    }

    /**
     * @return the userId
     */
    public final String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public final void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the orignalUrl
     */
    public final String getOrignalUrl() {
        return orignalUrl;
    }

    /**
     * @param orignalUrl the orignalUrl to set
     */
    public final void setOrignalUrl(String orignalUrl) {
        this.orignalUrl = orignalUrl;
    }

}
