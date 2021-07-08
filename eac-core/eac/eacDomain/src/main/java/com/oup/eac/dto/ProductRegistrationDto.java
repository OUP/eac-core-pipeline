package com.oup.eac.dto;

/**
 * Product specific Extension of RegistrationDto.
 * Used to carry product description onto Product Registration jsp page.
 * @author David Hay
 *
 */
public class ProductRegistrationDto extends RegistrationDto {

    private String productDescription;
    
    public ProductRegistrationDto() {
    }
    
    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

}
