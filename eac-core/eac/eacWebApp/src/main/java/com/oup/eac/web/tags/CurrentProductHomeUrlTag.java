package com.oup.eac.web.tags;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.UrlSkin;
import com.oup.eac.web.controllers.helpers.SessionHelper;

public class CurrentProductHomeUrlTag extends BaseSkinTag {

    @Override
    public String getValue(final HttpServletRequest request) {
        String result = null;
        RegisterableProduct regProd = SessionHelper.getRegisterableProduct(request);
        if (regProd != null) {
            result = regProd.getHomePage();
        }
        if (StringUtils.isBlank(result)) {
            ProductRegistrationDefinition regDef = SessionHelper.getProductRegistrationDefinition(request);
            if (regDef != null && regDef.getProduct() != null) {
                result = regDef.getProduct().getHomePage();
            }
        }
        return result;
    }

    @Override
    public String getDefaultValue(final UrlSkin defaultUrlSkin) {
        return defaultUrlSkin.getUrl();
    }

}
