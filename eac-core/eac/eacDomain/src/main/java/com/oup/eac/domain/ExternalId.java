package com.oup.eac.domain;



public abstract class ExternalId<T extends OUPBaseDomainObject> extends BaseDomainObject {

    public static enum DomainObjectType {
        PRODUCT,
        CUSTOMER
    }
   
    
    private String externalId;

    
    private ExternalSystemIdType externalSystemIdType;
    
    /**
     * Gets the external id.
     *
     * @return the external id
     */
	public String getExternalId() {
        return externalId;
    }

    /**
     * Sets the external id.
     *
     * @param externalId1 the new external id
     */
	public void setExternalId(final String externalId1) {
        this.externalId = externalId1;
    }

    /**
     * All of the external id entities point to a domain object
     * @return the domain object being pointed at
     */
    public abstract T getDomainObject();
    
    public abstract DomainObjectType getDomainObjectType();

    public ExternalSystemIdType getExternalSystemIdType() {
        return externalSystemIdType;
    }

    public void setExternalSystemIdType(ExternalSystemIdType externalSystemIdType) {
        this.externalSystemIdType = externalSystemIdType;
    }
    
    public void setDomainObjectType(DomainObjectType value){
        throw new UnsupportedOperationException();
    }
    
}
