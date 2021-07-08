package com.oup.eac.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.Product;

public class ExternalProductIdDto {

    private final List<Product> products;

    private final Map<String, List<ExternalProductId>> externalProductIds;

    public ExternalProductIdDto(List<Product> products) {
        super();
        this.products = products;
        this.externalProductIds = Collections.<String, List<ExternalProductId>>emptyMap();
    }

    public ExternalProductIdDto(List<Product> products, Map<String, List<ExternalProductId>> externalProductIds) {
        super();
        this.products = products;
        this.externalProductIds = externalProductIds;
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<ExternalProductId> getExternalProductIds(Product product) {
        if(products.contains(product)){
            List<ExternalProductId> temp = this.externalProductIds.get(product.getId());
            if(temp == null){
                return new ArrayList<ExternalProductId>();
            }else{
                return temp;
            }
        }else{
            return null;
        }
    }

}
