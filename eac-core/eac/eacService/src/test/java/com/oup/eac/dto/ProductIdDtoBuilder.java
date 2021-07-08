package com.oup.eac.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.Product;
import com.oup.eac.dto.ExternalProductIdDto;

/**
 * Convenience class for creating ExternalProductIdDto instances for classes.
 *  
 * @author David Hay
 *
 */
public class ProductIdDtoBuilder {
    
    private List<Product> products = new ArrayList<Product>();
    private Map<String, List<ExternalProductId>> externalProductIds = new HashMap<String, List<ExternalProductId>>();
    
    public ProductIdDtoBuilder add(Product product, List<ExternalProductId> externalIds){
        this.products.add(product);
        this.externalProductIds.put(product.getId(),externalIds);
        return this;
    };
    
    public ExternalProductIdDto build(){
        ExternalProductIdDto result = new ExternalProductIdDto(products, externalProductIds);
        return result;
    }
    
}