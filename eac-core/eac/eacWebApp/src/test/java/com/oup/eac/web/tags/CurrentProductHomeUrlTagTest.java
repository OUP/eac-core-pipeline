package com.oup.eac.web.tags;

import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.UrlSkin;
import com.oup.eac.service.DefaultUrlSkinService;
import com.oup.eac.web.controllers.helpers.SessionHelper;

public class CurrentProductHomeUrlTagTest {

    private static final String VAR = "varname";

    private static final String PRODUCT_HOME_PAGE_1 = "PRODUCT_HOME_PAGE_1";
    private static final String PRODUCT_HOME_PAGE_2 = "PRODUCT_HOME_PAGE_2";
    
    private static final String DEF_URL = "http://www.google.com";
    private static final String DEF_SITE_NAME = "EAC_TEST";
    private static final String DEF_CONTACT_PATH = "http://www.domain.com/contact_us";
    private static final String DEF_SKIN_PATH = "http://www.domain.com/css/styles.css";

    private BaseSkinTag tag;
    private MockServletContext sCtx;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockPageContext ctx;
    private WebApplicationContext mockWebAppContext;
    private DefaultUrlSkinService defaultUrlSkinService;
    private UrlSkin defaultUrlSkin;
    
    private RegisterableProduct product1;
    private RegisterableProduct product2;
    private ProductRegistrationDefinition prod1RegDef;

    @Before
    public void setup() {
        tag = new CurrentProductHomeUrlTag();
        tag.setVar(VAR);
        sCtx = new MockServletContext();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        ctx = new MockPageContext(sCtx, request, response);
        tag.setPageContext(ctx);

        Assert.assertEquals(VAR, tag.getVar());

        this.defaultUrlSkin = new UrlSkin();
        this.defaultUrlSkin.setUrl(DEF_URL);
        this.defaultUrlSkin.setContactPath(DEF_CONTACT_PATH);
        this.defaultUrlSkin.setSiteName(DEF_SITE_NAME);
        this.defaultUrlSkin.setSkinPath(DEF_SKIN_PATH);
        this.mockWebAppContext = EasyMock.createMock(WebApplicationContext.class);
        this.defaultUrlSkinService = EasyMock.createMock(DefaultUrlSkinService.class);
        request.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE, mockWebAppContext);

        this.product1 = new RegisterableProduct();
        this.product1.setHomePage(PRODUCT_HOME_PAGE_1);
        
        this.product2 = new RegisterableProduct();
        this.product2.setHomePage(PRODUCT_HOME_PAGE_2);
        
        this.prod1RegDef = new ProductRegistrationDefinition();
        this.prod1RegDef.setProduct(product1);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetDefaultValue() throws Exception {
        SessionHelper.setRegisterableProduct(request, null);
        SessionHelper.setProductRegistrationDefinition(request,  null);
        
        EasyMock.expect(this.mockWebAppContext.getBean(EasyMock.eq("requestDataValueProcessor"), eqAnything())).andReturn(null);
        EasyMock.expect(this.mockWebAppContext.getServletContext()).andReturn(null);
        EasyMock.expect(this.mockWebAppContext.getBean(EasyMock.eq(BaseSkinTag.BEAN_NAME_DEFAULT_URL_SKIN_SERVICE), EasyMock.anyObject(Class.class)))
                .andReturn(defaultUrlSkinService);
        EasyMock.expect(this.defaultUrlSkinService.getDefaultUrlSkin()).andReturn(defaultUrlSkin);
        EasyMock.replay(this.mockWebAppContext, this.defaultUrlSkinService);
        tag.setVar(VAR);
        tag.doStartTag();
        tag.doAfterBody();
        Assert.assertEquals(DEF_URL, request.getAttribute(VAR));
        EasyMock.verify(this.mockWebAppContext, this.defaultUrlSkinService);
    }

    @Test
    public void testGetSessionValue1() throws Exception {
        
        SessionHelper.setProductRegistrationDefinition(request, prod1RegDef);
        SessionHelper.setRegisterableProduct(request, null);

        EasyMock.expect(this.mockWebAppContext.getServletContext()).andReturn(null);
        EasyMock.expect(this.mockWebAppContext.getBean(EasyMock.eq("requestDataValueProcessor"), eqAnything())).andReturn(null);
        EasyMock.replay(this.mockWebAppContext, this.defaultUrlSkinService);
        tag.setVar(VAR);
        tag.doStartTag();
        tag.doAfterBody();
        Assert.assertEquals(PRODUCT_HOME_PAGE_1, request.getAttribute(VAR));
        EasyMock.verify(this.mockWebAppContext, this.defaultUrlSkinService);
    }
    
    @Test
    public void testGetSessionValue2() throws Exception {
        
        SessionHelper.setProductRegistrationDefinition(request, null);
        SessionHelper.setRegisterableProduct(request, product2);
        
        EasyMock.expect(this.mockWebAppContext.getServletContext()).andReturn(null);
        EasyMock.expect(this.mockWebAppContext.getBean(EasyMock.eq("requestDataValueProcessor"), eqAnything())).andReturn(null);
        EasyMock.replay(this.mockWebAppContext, this.defaultUrlSkinService);
        tag.setVar(VAR);
        tag.doStartTag();
        tag.doAfterBody();
        Assert.assertEquals(PRODUCT_HOME_PAGE_2, request.getAttribute(VAR));
        EasyMock.verify(this.mockWebAppContext, this.defaultUrlSkinService);
    }
    
    @Test
    public void testGetSessionValue3() throws Exception {
        
        SessionHelper.setProductRegistrationDefinition(request, prod1RegDef);
        SessionHelper.setRegisterableProduct(request, product2);
        
        EasyMock.expect(this.mockWebAppContext.getServletContext()).andReturn(null);
        EasyMock.expect(this.mockWebAppContext.getBean(EasyMock.eq("requestDataValueProcessor"), eqAnything())).andReturn(null);
        EasyMock.replay(this.mockWebAppContext, this.defaultUrlSkinService);
        tag.setVar(VAR);
        tag.doStartTag();
        tag.doAfterBody();
        Assert.assertEquals(PRODUCT_HOME_PAGE_2, request.getAttribute(VAR));
        EasyMock.verify(this.mockWebAppContext, this.defaultUrlSkinService);
    }


    @SuppressWarnings("unchecked")
    @Test
    public void testNoValue1() throws Exception {
        EasyMock.expect(this.mockWebAppContext.getServletContext()).andReturn(null);
        EasyMock.expect(this.mockWebAppContext.getBean(EasyMock.eq("requestDataValueProcessor"), eqAnything())).andReturn(null);
        EasyMock.expect(this.mockWebAppContext.getBean(EasyMock.eq(BaseSkinTag.BEAN_NAME_DEFAULT_URL_SKIN_SERVICE), EasyMock.anyObject(Class.class))).andReturn(null);
        EasyMock.replay(this.mockWebAppContext, this.defaultUrlSkinService);
        tag.setVar(VAR);
        tag.doStartTag();
        tag.doAfterBody();
        EasyMock.verify(this.mockWebAppContext, this.defaultUrlSkinService);
        Assert.assertEquals(null, request.getAttribute(VAR));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNoValue2() throws Exception {
        this.defaultUrlSkin.setUrl(null);
        EasyMock.expect(this.mockWebAppContext.getServletContext()).andReturn(null);
        EasyMock.expect(this.mockWebAppContext.getBean(EasyMock.eq("requestDataValueProcessor"), eqAnything())).andReturn(null);
        EasyMock.expect(this.mockWebAppContext.getBean(EasyMock.eq(BaseSkinTag.BEAN_NAME_DEFAULT_URL_SKIN_SERVICE), EasyMock.anyObject(Class.class)))
                .andReturn(defaultUrlSkinService);
        EasyMock.expect(this.defaultUrlSkinService.getDefaultUrlSkin()).andReturn(defaultUrlSkin);
        EasyMock.replay(this.mockWebAppContext, this.defaultUrlSkinService);
        tag.setVar(VAR);
        tag.doStartTag();
        tag.doAfterBody();
        EasyMock.verify(this.mockWebAppContext, this.defaultUrlSkinService);
        Assert.assertEquals(null, request.getAttribute(VAR));
    }
    
    protected Class<?> eqAnything() {
        IArgumentMatcher matcher = new IArgumentMatcher() {

            @Override
            public boolean matches(Object arg) {
                return true;
            }

            @Override
            public void appendTo(StringBuffer out) {
                out.append("eqAnything(");                
                out.append(")");
            }
        };
        EasyMock.reportMatcher(matcher);
        return null;
    }

}

