package com.oup.eac.service.entitlements;

import com.oup.eac.domain.Product;

public interface ProductSource {
    public Product getProductById(String productId);
}
