package com.oup.eac.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


public class ExternalProductId extends ExternalId<Product> {
    
    
    private Product product;

    /**
     * Gets the product.
     *
     * @return the product
     */
	public Product getProduct() {
        return product;
    }

    /**
     * Sets the product.
     *
     * @param product1 the new product
     */
	public void setProduct(final Product product1) {
        this.product = product1;
    }

    @Override
    public Product getDomainObject() {
        return product;
    }

    @Override
    public com.oup.eac.domain.ExternalId.DomainObjectType getDomainObjectType() {
        return DomainObjectType.PRODUCT;
    }
    
    
    @Override
    public int hashCode(){
        
        if(this.getExternalId() == null){
            this.setExternalId("");
        }
        if(this.getExternalSystemIdType() == null){
            this.setExternalSystemIdType(new ExternalSystemIdType());
        }
        if(this.getExternalSystemIdType().getId()==null){
            this.getExternalSystemIdType().setId("");
        }
        
        return this.getExternalId().hashCode() + this.getExternalSystemIdType().getId().hashCode();
                    
    }
    
    
    @Override
    public boolean equals(Object o){
        boolean compare=false;
        ExternalProductId externalProductId = (ExternalProductId)o;        
        if(this.getExternalId() == null){
            this.setExternalId("");
        }
        
        if(this.getExternalSystemIdType() == null){
            this.setExternalSystemIdType(new ExternalSystemIdType());
        }
        
        if(this.getExternalSystemIdType().getId()==null){
            this.getExternalSystemIdType().setId("");
        }
        
        if(externalProductId != null){
            if(this.getExternalId().equals(externalProductId.getExternalId()) && this.getExternalSystemIdType().getId().equals(externalProductId.getExternalSystemIdType().getId())){
                compare = true;
            }   
        }
                
        return compare;
        
    }

}
