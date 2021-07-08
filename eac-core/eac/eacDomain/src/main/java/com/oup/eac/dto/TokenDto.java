/**
 * 
 */
package com.oup.eac.dto;

/**
 * @author harlandd
 * 
 */
public class TokenDto {

    private String licenseId;

    private String userId;

    private String registrationId;

    private String orignalUrl;
    
    private String userName;

	private String productName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}


    /**
     * Default constructor.
     */
    public TokenDto() {
        super();
    }
    
	/**
	 * Create a TokenDto.
	 * 
	 * @param licenseId
	 *            the licence id
	 * @param userId
	 *            the user id
	 * @param registrationId
	 *            the registration id
	 * @param orignalUrl
	 *            the original url
	 */
	public TokenDto(final String licenseId, final String userId, final String orignalUrl, String userName, String productName) {
		super();
		this.licenseId = licenseId;
		this.userId = userId;
		this.orignalUrl = orignalUrl;
		this.userName = userName;
		this.productName = productName;
	}

    /**
     * Create a TokenDto.
     * 
     * @param licenseId
     *            the licence id
     * @param userId
     *            the user id
     * @param registrationId
     *            the registration id
     * @param orignalUrl
     *            the original url
     */
    public TokenDto(final String licenseId, final String userId, /*final String registrationId,*/ final String orignalUrl) {
        super();
        this.licenseId = licenseId;
        this.userId = userId;
       /* this.registrationId = registrationId;*/
        this.orignalUrl = orignalUrl;
    }

    /**
     * @return the registrationId
     */
    public final String getRegistrationId() {
        return registrationId;
    }

    /**
     * @param registrationId
     *            the registrationId to set
     */
    public final void setRegistrationId(final String registrationId) {
        this.registrationId = registrationId;
    }

    /**
     * @return the licenseId
     */
    public final String getLicenseId() {
        return licenseId;
    }

    /**
     * @param licenseId
     *            the licenseId to set
     */
    public final void setLicenseId(final String licenseId) {
        this.licenseId = licenseId;
    }

    /**
     * @return the userId
     */
    public final String getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public final void setUserId(final String userId) {
        this.userId = userId;
    }

    /**
     * @return the orignalUrl
     */
    public final String getOrignalUrl() {
        return orignalUrl;
    }

    /**
     * @param orignalUrl
     *            the orignalUrl to set
     */
    public final void setOrignalUrl(final String orignalUrl) {
        this.orignalUrl = orignalUrl;
    }

}
