package com.oup.eac.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author harlandd Dto for transfering data from the erights fasade to the service layer.
 */
public class AuthenticationResponseDto {

    private String sessionKey;
    private List<String> userIds = new ArrayList<String>();
    private CustomerDto customerDto;
    /**
     * @return the session key
     */
    public final String getSessionKey() {
        return sessionKey;
    }

    /**
     * @param sessionKey
     *            the session key
     */
    public final void setSessionKey(final String sessionKey) {
        this.sessionKey = sessionKey;
    }

    /**
     * @return the userIds
     */
    public final List<String> getUserIds() {
        return userIds;
    }

    /**
     * @param userIds
     *            the userIds to set
     */
    public final void setUserIds(final List<String> userIds) {
        this.userIds = userIds;
    }

    /**
     * @param userIds
     *            the userIds
     */
    public final void addUserIds(final List<String> userIds) {
        this.userIds.addAll(userIds);
    }

	/**
	 * @return the customerDto
	 */
	public CustomerDto getCustomerDto() {
		return customerDto;
	}

	/**
	 * @param customerDto the customerDto to set
	 */
	public void setCustomerDto(CustomerDto customerDto) {
		this.customerDto = customerDto;
	}
}
