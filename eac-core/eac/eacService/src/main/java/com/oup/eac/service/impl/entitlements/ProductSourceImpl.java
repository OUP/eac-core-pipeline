package com.oup.eac.service.impl.entitlements;

import java.util.Map;

import com.oup.eac.domain.Product;
import com.oup.eac.service.entitlements.ProductSource;

public class ProductSourceImpl implements ProductSource {

    private final Map<String, Product> productMap;

    public ProductSourceImpl(Map<String,Product> productMap){
        this.productMap = productMap;
    }

	@Override
	public Product getProductById(String productId) {
		return this.productMap.get(productId);
    }
}
