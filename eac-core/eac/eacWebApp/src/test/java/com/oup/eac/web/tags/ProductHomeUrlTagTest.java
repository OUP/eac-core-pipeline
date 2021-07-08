package com.oup.eac.web.tags;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.RequestDataValueProcessor;

import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.UrlSkin;
import com.oup.eac.service.DomainSkinResolverService;
import com.oup.eac.web.utils.UrlCustomiser;

public class ProductHomeUrlTagTest {

    private static final String VAR = "varname";

    private static final String PRODUCT_HOME_PAGE_1 = "PRODUCT_HOME_PAGE_1";
    private static final String PRODUCT_HOME_PAGE_2 = "PRODUCT_HOME_PAGE_2";

    private ProductHomeUrlTag tag;

    private RegisterableProduct product1;
    private RegisterableProduct product2;
    private RegisterableProduct product3;
    private RegisterableProduct product4;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockPageContext context;
    private MockServletContext servletContext;

    @Before
    public void setup() {
        product1 = new RegisterableProduct();
        product1.setHomePage(PRODUCT_HOME_PAGE_1);

        product2 = new RegisterableProduct();
        product2.setHomePage(PRODUCT_HOME_PAGE_2);

        product3 = new RegisterableProduct();
        product3.setHomePage(null);

        product4 = new RegisterableProduct();
        product4.setHomePage("");

        tag = new ProductHomeUrlTag();
        tag.setVar(VAR);
        request = new MockHttpServletRequest();

        DefaultListableBeanFactory dlbf = new DefaultListableBeanFactory();
        GenericWebApplicationContext gwac = new GenericWebApplicationContext(dlbf);

        setWebApplicationContext(gwac);

        response = new MockHttpServletResponse();
        tag.setProduct(product1);

        context = new MockPageContext(new MockServletContext(), request, response);
        tag.setPageContext(context);
        Assert.assertEquals(VAR, tag.getVar());

        this.servletContext = new MockServletContext();
    }

    private void setWebApplicationContext(WebApplicationContext ctx) {
        request.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE, ctx);
    }

    @Test
    public void testProduct1() throws Exception {
        tag.setProduct(product1);
        tag.doStartTag();
        Assert.assertEquals(product1, tag.getProduct());
        Assert.assertEquals(PRODUCT_HOME_PAGE_1, request.getAttribute(VAR));
    }

    @Test
    public void testProduct2() throws Exception {
        tag.setProduct(product2);
        tag.doStartTag();
        Assert.assertEquals(PRODUCT_HOME_PAGE_2, request.getAttribute(VAR));
    }

    @Test
    public void testProduct3() throws Exception {
        tag.setProduct(product3);
        tag.doStartTag();
        Assert.assertEquals(null, request.getAttribute(VAR));
    }

    @Test
    public void testProduct4() throws Exception {
        tag.setProduct(product4);
        tag.doStartTag();
        Assert.assertEquals("", request.getAttribute(VAR));
    }

    @Test
    public void testNullProduct() throws Exception {
        tag.setProduct(null);
        tag.doStartTag();
        Assert.assertEquals(null, request.getAttribute(VAR));
    }

    @Test
    public void testProduct1AndNullVar() throws Exception {
        tag.setVar(null);
        tag.setProduct(product1);
        tag.doStartTag();
        Assert.assertEquals(null, request.getAttribute(VAR));
    }

    @Test
    public void testAlterUrlOkay() throws Exception {
        WebApplicationContext mWAC = EasyMock.createMock(WebApplicationContext.class);
        DomainSkinResolverService mDomainSkinResolver = EasyMock.createMock(DomainSkinResolverService.class);
        setWebApplicationContext(mWAC);

        UrlSkin skin1 = new UrlSkin();
        skin1.setUrl("URL1");
        skin1.setUrlCustomiserBean("customiserOne");

        EasyMock.expect(mDomainSkinResolver.getSkinFromDomain(PRODUCT_HOME_PAGE_1)).andReturn(skin1);
        EasyMock.expect(mWAC.getServletContext()).andReturn(this.servletContext);
        EasyMock.expect(mWAC.getBean("requestDataValueProcessor", RequestDataValueProcessor.class)).andReturn(null);
        EasyMock.expect(mWAC.getBean(DomainSkinResolverService.class)).andReturn(mDomainSkinResolver);
        UrlCustomiser customiserOne = new UrlCustomiser() {
            @Override
            public String customiseUrl(String url, HttpServletRequest request) {
                return url + "?customizedBy=One";
            }
        };
        EasyMock.expect(mWAC.getBean("customiserOne", UrlCustomiser.class)).andReturn(customiserOne);
        EasyMock.replay(mWAC, mDomainSkinResolver);
        tag.setVar(VAR);
        tag.setProduct(product1);
        tag.doStartTag();
        Assert.assertEquals("PRODUCT_HOME_PAGE_1?customizedBy=One", request.getAttribute(VAR));
        EasyMock.verify(mWAC, mDomainSkinResolver);
    }

    @Test
    public void testAlterUrlFailNoDomainSkinResolverService() throws Exception {
        WebApplicationContext mWAC = EasyMock.createMock(WebApplicationContext.class);
        DomainSkinResolverService mDomainSkinResolver = EasyMock.createMock(DomainSkinResolverService.class);
        setWebApplicationContext(mWAC);
        EasyMock.expect(mWAC.getServletContext()).andReturn(this.servletContext);
        EasyMock.expect(mWAC.getBean("requestDataValueProcessor", RequestDataValueProcessor.class)).andReturn(null);
        EasyMock.expect(mWAC.getBean(DomainSkinResolverService.class)).andReturn(null);
        UrlSkin skin1 = new UrlSkin();
        skin1.setUrl("URL1");
        skin1.setUrlCustomiserBean("customiserOne");
        tag.setVar(VAR);
        tag.setProduct(product1);
        EasyMock.replay(mWAC, mDomainSkinResolver);
        tag.doStartTag();
        EasyMock.verify(mWAC, mDomainSkinResolver);
    }

    @Test
    public void testAlterUrlFailNoUrlSkin() throws Exception {
        WebApplicationContext mWAC = EasyMock.createMock(WebApplicationContext.class);
        DomainSkinResolverService mDomainSkinResolver = EasyMock.createMock(DomainSkinResolverService.class);
        setWebApplicationContext(mWAC);
        EasyMock.expect(mWAC.getServletContext()).andReturn(this.servletContext);
        EasyMock.expect(mWAC.getBean("requestDataValueProcessor", RequestDataValueProcessor.class)).andReturn(null);
        EasyMock.expect(mWAC.getBean(DomainSkinResolverService.class)).andReturn(mDomainSkinResolver);
        EasyMock.expect(mDomainSkinResolver.getSkinFromDomain(PRODUCT_HOME_PAGE_1)).andReturn(null);
        UrlSkin skin1 = new UrlSkin();
        skin1.setUrl("URL1");
        skin1.setUrlCustomiserBean("customiserOne");
        tag.setVar(VAR);
        tag.setProduct(product1);
        EasyMock.replay(mWAC, mDomainSkinResolver);
        tag.doStartTag();
        EasyMock.verify(mWAC, mDomainSkinResolver);
    }

    @Test
    public void testAlterUrlFailNoCustomizerBeanName() throws Exception {
        WebApplicationContext mWAC = EasyMock.createMock(WebApplicationContext.class);
        DomainSkinResolverService mDomainSkinResolver = EasyMock.createMock(DomainSkinResolverService.class);
        setWebApplicationContext(mWAC);
        EasyMock.expect(mWAC.getServletContext()).andReturn(this.servletContext);
        EasyMock.expect(mWAC.getBean("requestDataValueProcessor", RequestDataValueProcessor.class)).andReturn(null);
        EasyMock.expect(mWAC.getBean(DomainSkinResolverService.class)).andReturn(mDomainSkinResolver);

        UrlSkin skin1 = new UrlSkin();
        skin1.setUrl("URL1");
        skin1.setUrlCustomiserBean(null);
        tag.setVar(VAR);
        tag.setProduct(product1);
        EasyMock.expect(mDomainSkinResolver.getSkinFromDomain(PRODUCT_HOME_PAGE_1)).andReturn(skin1);
        EasyMock.replay(mWAC, mDomainSkinResolver);
        tag.doStartTag();
        EasyMock.verify(mWAC, mDomainSkinResolver);
    }

    @Test
    public void testAlterUrlFailNoCustomizerBean() throws Exception {
        WebApplicationContext mWAC = EasyMock.createMock(WebApplicationContext.class);
        DomainSkinResolverService mDomainSkinResolver = EasyMock.createMock(DomainSkinResolverService.class);
        setWebApplicationContext(mWAC);
        EasyMock.expect(mWAC.getServletContext()).andReturn(this.servletContext);
        EasyMock.expect(mWAC.getBean("requestDataValueProcessor", RequestDataValueProcessor.class)).andReturn(null);
        EasyMock.expect(mWAC.getBean(DomainSkinResolverService.class)).andReturn(mDomainSkinResolver);
        EasyMock.expect(mWAC.getBean("customiserOne", UrlCustomiser.class)).andReturn(null);
        UrlSkin skin1 = new UrlSkin();
        skin1.setUrl("URL1");
        skin1.setUrlCustomiserBean("customiserOne");
        tag.setVar(VAR);
        tag.setProduct(product1);
        EasyMock.expect(mDomainSkinResolver.getSkinFromDomain(PRODUCT_HOME_PAGE_1)).andReturn(skin1);
        EasyMock.replay(mWAC, mDomainSkinResolver);
        tag.doStartTag();
        EasyMock.verify(mWAC, mDomainSkinResolver);
    }

}
