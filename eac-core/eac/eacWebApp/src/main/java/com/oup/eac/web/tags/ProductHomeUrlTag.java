package com.oup.eac.web.tags;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import com.oup.eac.domain.Product;
import com.oup.eac.domain.UrlSkin;
import com.oup.eac.service.DomainSkinResolverService;
import com.oup.eac.web.utils.UrlCustomiser;

public class ProductHomeUrlTag extends RequestContextAwareTag {

    private static final Logger LOG = Logger.getLogger(ProductHomeUrlTag.class);

    private Product product;
    private String var;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    @Override
    protected int doStartTagInternal() throws Exception {
        if (product != null && StringUtils.isNotBlank(var)) {
            ServletRequest request = this.pageContext.getRequest();
            String homePageUrl = product.getHomePage();
            String customizedHomePageUrl = customizeHomePageUrl(homePageUrl);
            request.setAttribute(var, customizedHomePageUrl);
        }
        return SKIP_BODY;
    }

    private String customizeHomePageUrl(final String homePageUrl) {
        if (StringUtils.isBlank(homePageUrl)) {
            return homePageUrl;
        }
        //if there was no context - we could not have got into doStartTagInternal
        WebApplicationContext context = getRequestContext().getWebApplicationContext();
        
        DomainSkinResolverService domainResolver = getBean(context, null, DomainSkinResolverService.class);
        if (domainResolver == null) {
            return homePageUrl;
        }
        UrlSkin skin = domainResolver.getSkinFromDomain(homePageUrl);
        if (skin == null) {
            return homePageUrl;
        }
        String customiserBeanName = skin.getUrlCustomiserBean();
        if (StringUtils.isBlank(customiserBeanName)) {
            return homePageUrl;
        }
        UrlCustomiser iser = getBean(context, customiserBeanName, UrlCustomiser.class);
        if (iser == null) {
            return homePageUrl;
        }
        String result = iser.customiseUrl(homePageUrl, (HttpServletRequest) pageContext.getRequest());
        return result;
    }

    private <T> T getBean(ApplicationContext context, String name, Class<T> requiredType) {
        T result = null;
        try {
            if (StringUtils.isBlank(name)) {
                result = context.getBean(requiredType);
            } else {
                result = context.getBean(name, requiredType);
            }
        } catch (NoSuchBeanDefinitionException nsbde) {
            LOG.debug(nsbde.getMessage());
        }
        return result;
    }

}
