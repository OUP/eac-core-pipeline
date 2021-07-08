package com.oup.eac.web.tags;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.RequestDataValueProcessor;

import com.oup.eac.domain.UrlSkin;
import com.oup.eac.service.DefaultUrlSkinService;
import com.oup.eac.web.controllers.helpers.SessionHelper;

public abstract class BaseSkinTagTest {

    protected static final String VAR = "varname";

    protected static final String DEF_URL = "http://www.google.com";
    protected static final String DEF_SITE_NAME = "EAC_TEST";
    protected static final String DEF_CONTACT_PATH = "http://www.domain.com/contact_us";
    protected static final String DEF_SKIN_PATH = "http://www.domain.com/css/styles.css";
    protected static final String URL = "http://www.oracle.com";
    protected static final String SITE_NAME = "EAC_TEST_2";
    protected static final String CONTACT_PATH = "http://www.domain2.com/contact_us";
    protected static final String SKIN_PATH = "http://www.domain2.com/css/styles.css";

    protected BaseSkinTag tag;
    protected MockServletContext sCtx;
    protected MockHttpServletRequest request;
    protected MockHttpServletResponse response;
    protected MockPageContext ctx;
    protected WebApplicationContext mockWebAppContext;
    protected DefaultUrlSkinService defaultUrlSkinService;
    protected UrlSkin sessionUrlSkin;
    protected UrlSkin defaultUrlSkin;
    
    private RequestDataValueProcessor processor;

    public abstract String getDefaultValue();

    public abstract String getSessionValue();

    public abstract BaseSkinTag getTag();

    public abstract void setNullSkinValue(UrlSkin sessionUrlSkin);

    @Before
    public void setup() {
        tag = getTag();
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

        this.sessionUrlSkin = new UrlSkin();
        this.sessionUrlSkin.setUrl(URL);
        this.sessionUrlSkin.setContactPath(CONTACT_PATH);
        this.sessionUrlSkin.setSiteName(SITE_NAME);
        this.sessionUrlSkin.setSkinPath(SKIN_PATH);

    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetDefaultValue() throws Exception {
        EasyMock.expect(this.mockWebAppContext.getServletContext()).andReturn(null);
        EasyMock.expect(this.mockWebAppContext.getBean(EasyMock.eq(BaseSkinTag.BEAN_NAME_DEFAULT_URL_SKIN_SERVICE), EasyMock.anyObject(Class.class)))
                .andReturn(defaultUrlSkinService);
        EasyMock.expect(this.mockWebAppContext.getBean(EasyMock.eq("requestDataValueProcessor"), EasyMock.anyObject(Class.class))).andReturn(processor);
        EasyMock.expect(this.defaultUrlSkinService.getDefaultUrlSkin()).andReturn(defaultUrlSkin);
        EasyMock.replay(this.mockWebAppContext, this.defaultUrlSkinService);
        tag.setVar(VAR);
        tag.doStartTag();
        tag.doAfterBody();
        Assert.assertEquals(getDefaultValue(), request.getAttribute(VAR));
        EasyMock.verify(this.mockWebAppContext, this.defaultUrlSkinService);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetSessionValue() throws Exception {
        SessionHelper.setUrlSkin(request, sessionUrlSkin);
        EasyMock.expect(this.mockWebAppContext.getServletContext()).andReturn(null);
        EasyMock.expect(this.mockWebAppContext.getBean(EasyMock.eq("requestDataValueProcessor"), EasyMock.anyObject(Class.class))).andReturn(processor);
        EasyMock.replay(this.mockWebAppContext, this.defaultUrlSkinService);
        tag.setVar(VAR);
        tag.doStartTag();
        tag.doAfterBody();
        Assert.assertEquals(getSessionValue(), request.getAttribute(VAR));
        EasyMock.verify(this.mockWebAppContext, this.defaultUrlSkinService);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNoValue1() throws Exception {
        EasyMock.expect(this.mockWebAppContext.getServletContext()).andReturn(null);
        EasyMock.expect(this.mockWebAppContext.getBean(EasyMock.eq(BaseSkinTag.BEAN_NAME_DEFAULT_URL_SKIN_SERVICE), EasyMock.anyObject(Class.class)))
                .andReturn(null);
        EasyMock.expect(this.mockWebAppContext.getBean(EasyMock.eq("requestDataValueProcessor"), EasyMock.anyObject(Class.class))).andReturn(processor);        
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
        setNullSkinValue(defaultUrlSkin);
        EasyMock.expect(this.mockWebAppContext.getServletContext()).andReturn(null);
        EasyMock.expect(this.mockWebAppContext.getBean(EasyMock.eq(BaseSkinTag.BEAN_NAME_DEFAULT_URL_SKIN_SERVICE), EasyMock.anyObject(Class.class)))
                .andReturn(defaultUrlSkinService);
        EasyMock.expect(this.mockWebAppContext.getBean(EasyMock.eq("requestDataValueProcessor"), EasyMock.anyObject(Class.class))).andReturn(processor);
        EasyMock.expect(this.defaultUrlSkinService.getDefaultUrlSkin()).andReturn(defaultUrlSkin);
        EasyMock.replay(this.mockWebAppContext, this.defaultUrlSkinService);
        tag.setVar(VAR);
        tag.doStartTag();
        tag.doAfterBody();
        EasyMock.verify(this.mockWebAppContext, this.defaultUrlSkinService);
        Assert.assertEquals(null, request.getAttribute(VAR));
    }

}
