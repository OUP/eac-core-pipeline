package com.oup.eac.web.tags;

import com.oup.eac.service.ProductService;

public class IsProductUsedTag extends BaseGetValueTag {

    private String productId;
    
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public Object getValue() {
        ProductService productService = getService("productService", ProductService.class);
        Boolean value = productService.isProductUsed(productId);
        return value;
    }

}
