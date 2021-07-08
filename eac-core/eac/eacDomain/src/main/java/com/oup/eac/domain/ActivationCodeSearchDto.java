package com.oup.eac.domain;


public class ActivationCodeSearchDto extends BaseDomainObject {

    private ActivationCodeBatch activationCodeBatch;
    
    private String code;
    
    private Integer allowedUsage;
    
    private Integer actualUsage = Integer.valueOf(0);
    
    private Product product;

    public ActivationCodeSearchDto() {
		super();
	}
	
	/**
     * @return the actualUsage
     */
	public Integer getActualUsage() {
        return actualUsage;
    }

    /**
     * @param actualUsage the actualUsage to set
     */
	public void setActualUsage(final Integer actualUsage) {
        this.actualUsage = actualUsage;
    }
    
    /**
     * @return the allowedUsage
     */
	public Integer getAllowedUsage() {
        return allowedUsage;
    }

    /**
     * @param allowedUsage the allowedUsage to set
     */
	public void setAllowedUsage(final Integer allowedUsage) {
        this.allowedUsage = allowedUsage;
    }

	/**
     * @return the code
     */
	public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
	public void setCode(final String code) {
        this.code = code;
    }

    /**
     * @return the activationCodeBatch
     */
	public ActivationCodeBatch getActivationCodeBatch() {
        return activationCodeBatch;
    }

    /**
     * @param activationCodeBatch the activationCodeBatch to set
     */
	public void setActivationCodeBatch(final ActivationCodeBatch activationCodeBatch) {
        this.activationCodeBatch = activationCodeBatch;
    }
   
	public void incrementActualUsage() {
        setActualUsage(Integer.valueOf(getActualUsage().intValue() + 1));
    }
	
	public void decrementActualUsage(){
		setActualUsage(Integer.valueOf(getActualUsage().intValue() - 1));
	}
    
	public boolean isUsageLimitReached() {
    	return getActualUsage().intValue() >= getAllowedUsage().intValue();
    }

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
}
