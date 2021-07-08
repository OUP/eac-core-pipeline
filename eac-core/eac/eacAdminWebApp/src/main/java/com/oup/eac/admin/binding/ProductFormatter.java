package com.oup.eac.admin.binding;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import com.oup.eac.domain.Product;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.ServiceLayerException;

@Component (value="productFormatter")
public class ProductFormatter implements Formatter<Product> {

    @Autowired
    private ProductService productService;
    
    @Override
    public String print(Product object, Locale locale) {
        return object.getProductName();
    }

    @Override
    public Product parse(String text, Locale locale) throws ParseException {
        /*try {*/
        	Product product = new RegisterableProduct();
        	product.setId(text);
            return product;
        /*} catch (ServiceLayerException e) {
            throw new ParseException("Product could not be parsed from id " + text, 0);
        }*/
    }

    
}
