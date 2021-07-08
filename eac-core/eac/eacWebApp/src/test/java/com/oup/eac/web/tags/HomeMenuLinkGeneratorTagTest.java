package com.oup.eac.web.tags;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.RequestDataValueProcessor;

import com.oup.eac.common.utils.UrlMap;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ProductRegistration;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.UrlSkin;
import com.oup.eac.dto.profile.ProfileRegistrationDto;
import com.oup.eac.service.UrlMapService;
import com.oup.eac.web.controllers.helpers.SessionHelper;
import com.oup.eac.web.profile.CachingProfileRegistrationDtoSource;
import com.oup.eac.web.utils.CustomerTimeoutConfig;

public class HomeMenuLinkGeneratorTagTest {

	private static final String VAR = "varname";

	private final ServletContext mockServletContext = new MockServletContext();;
	private final HttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
	private final HttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
	private final PageContext mockPageContext = new MockPageContext(mockServletContext, mockHttpServletRequest, mockHttpServletResponse);;
	private final WebApplicationContext mockWebApplicationContext = createMock(WebApplicationContext.class);;
	private final UrlMapService mockUrlMapService = createMock(UrlMapService.class);
	private final CachingProfileRegistrationDtoSource mockCachingProfileRegistrationDtoSource = createMock(CachingProfileRegistrationDtoSource.class);
	private final UrlMap<UrlSkin> urlMap = new UrlMap<UrlSkin>();

	private HomeMenuLinkGeneratorTag tag = new HomeMenuLinkGeneratorTag();

	@Before
	public void setup() throws MalformedURLException {
		tag.setVar(VAR);
		tag.setPageContext(mockPageContext);

		urlMap.put("http://oxfordfajar.com.my", fajarSkin());
		urlMap.put("http://www.elt.oup.com", eltSkin());

		mockHttpServletRequest.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE, mockWebApplicationContext);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldGenerateAllLinksWhenCustomerNotLoggedInAndNotOnSkinnedPage() throws Exception {
		expect(mockWebApplicationContext.getBean("requestDataValueProcessor", RequestDataValueProcessor.class)).andReturn(null);
		expect(mockWebApplicationContext.getServletContext()).andReturn(null);
		expect(mockWebApplicationContext.getBean("urlMapService", UrlMapService.class)).andReturn(mockUrlMapService);
		expect(mockUrlMapService.getUrlMap()).andReturn(urlMap);

		replay(mockWebApplicationContext, mockUrlMapService);

		tag.setVar(VAR);
		tag.doStartTag();

		List<UrlSkin> urlSkins = (List<UrlSkin>) mockHttpServletRequest.getAttribute(VAR);
		assertNotNull(urlSkins);
		assertThat(urlSkins.size(), equalTo(2));
		assertThat(urlSkins.get(0).getUrl(), equalTo("http://www.elt.oup.com"));
		assertThat(urlSkins.get(1).getUrl(), equalTo("http://oxfordfajar.com.my"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldGenerateLinkWhenCustomerNotLoggedInButOnSkinnedPage() throws Exception {
		expect(mockWebApplicationContext.getBean("requestDataValueProcessor", RequestDataValueProcessor.class)).andReturn(null);
		expect(mockWebApplicationContext.getServletContext()).andReturn(null);
		expect(mockWebApplicationContext.getBean("urlMapService", UrlMapService.class)).andReturn(mockUrlMapService);
		expect(mockUrlMapService.getUrlMap()).andReturn(urlMap);

		SessionHelper.setUrlSkin(mockHttpServletRequest, fajarSkin());

		replay(mockWebApplicationContext, mockUrlMapService);

		tag.setVar(VAR);
		tag.doStartTag();

		List<UrlSkin> urlSkins = (List<UrlSkin>) mockHttpServletRequest.getAttribute(VAR);
		assertNotNull(urlSkins);
		assertThat(urlSkins.size(), equalTo(1));
		assertThat(urlSkins.get(0).getUrl(), equalTo("http://oxfordfajar.com.my"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldGenerateLinkWhenCustomerLoggedInWithRegisteredProductButNotOnSkinnedPage() throws Exception {
		expect(mockWebApplicationContext.getBean("requestDataValueProcessor", RequestDataValueProcessor.class)).andReturn(null);
		expect(mockWebApplicationContext.getServletContext()).andReturn(null);
		expect(mockWebApplicationContext.getBean("urlMapService", UrlMapService.class)).andReturn(mockUrlMapService);
		expect(mockWebApplicationContext.getBean("webCustomerTimeoutConfig", CustomerTimeoutConfig.class)).andReturn(null);
		expect(mockWebApplicationContext.getBean(CachingProfileRegistrationDtoSource.class)).andReturn(mockCachingProfileRegistrationDtoSource);
		expect(mockUrlMapService.getUrlMap()).andReturn(urlMap);

		Customer customer = new Customer();
		mockHttpServletRequest.getSession().setAttribute("CUSTOMER", customer);

		RegisterableProduct product = new RegisterableProduct();
		product.setHomePage("http://www.elt.oup.com");
		ProductRegistrationDefinition registrationDefinition = new ProductRegistrationDefinition();
		registrationDefinition.setProduct(product);
		ProductRegistration productRegistration = new ProductRegistration();
		productRegistration.setRegistrationDefinition(registrationDefinition);
		ProfileRegistrationDto dto = new ProfileRegistrationDto(productRegistration, null);

		expect(mockCachingProfileRegistrationDtoSource.getProfileRegistrationDtos(customer, mockHttpServletRequest.getSession())).andReturn(Arrays.asList(dto));

		replay(mockWebApplicationContext, mockUrlMapService, mockCachingProfileRegistrationDtoSource);

		tag.setVar(VAR);
		tag.doStartTag();

		List<UrlSkin> urlSkins = (List<UrlSkin>) mockHttpServletRequest.getAttribute(VAR);
		assertNotNull(urlSkins);
		assertThat(urlSkins.size(), equalTo(1));
		assertThat(urlSkins.get(0).getUrl(), equalTo("http://www.elt.oup.com"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldGenerateAllLinksWhenCustomerLoggedInWithNoRegisteredProductsAndNotOnSkinnedPage() throws Exception {
		expect(mockWebApplicationContext.getBean("requestDataValueProcessor", RequestDataValueProcessor.class)).andReturn(null);
		expect(mockWebApplicationContext.getServletContext()).andReturn(null);
		expect(mockWebApplicationContext.getBean("urlMapService", UrlMapService.class)).andReturn(mockUrlMapService);
		expect(mockWebApplicationContext.getBean("webCustomerTimeoutConfig", CustomerTimeoutConfig.class)).andReturn(null);
		expect(mockWebApplicationContext.getBean(CachingProfileRegistrationDtoSource.class)).andReturn(mockCachingProfileRegistrationDtoSource);
		expect(mockUrlMapService.getUrlMap()).andReturn(urlMap);

		Customer customer = new Customer();
		mockHttpServletRequest.getSession().setAttribute("CUSTOMER", customer);

		expect(mockCachingProfileRegistrationDtoSource.getProfileRegistrationDtos(customer, mockHttpServletRequest.getSession())).andReturn(
				new ArrayList<ProfileRegistrationDto>());

		replay(mockWebApplicationContext, mockUrlMapService, mockCachingProfileRegistrationDtoSource);

		tag.setVar(VAR);
		tag.doStartTag();

		List<UrlSkin> urlSkins = (List<UrlSkin>) mockHttpServletRequest.getAttribute(VAR);
		assertNotNull(urlSkins);
		assertThat(urlSkins.size(), equalTo(2));
		assertThat(urlSkins.get(0).getUrl(), equalTo("http://www.elt.oup.com"));
		assertThat(urlSkins.get(1).getUrl(), equalTo("http://oxfordfajar.com.my"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldGenerateLinksWhenCustomerLoggedInWithRegisteredProductAndOnSkinnedPageOfDifferentProduct() throws Exception {
		expect(mockWebApplicationContext.getBean("requestDataValueProcessor", RequestDataValueProcessor.class)).andReturn(null);
		expect(mockWebApplicationContext.getServletContext()).andReturn(null);
		expect(mockWebApplicationContext.getBean("urlMapService", UrlMapService.class)).andReturn(mockUrlMapService);
		expect(mockWebApplicationContext.getBean("webCustomerTimeoutConfig", CustomerTimeoutConfig.class)).andReturn(null);
		expect(mockWebApplicationContext.getBean(CachingProfileRegistrationDtoSource.class)).andReturn(mockCachingProfileRegistrationDtoSource);
		expect(mockUrlMapService.getUrlMap()).andReturn(urlMap);

		Customer customer = new Customer();
		mockHttpServletRequest.getSession().setAttribute("CUSTOMER", customer);

		RegisterableProduct product = new RegisterableProduct();
		product.setHomePage("http://www.elt.oup.com");
		ProductRegistrationDefinition registrationDefinition = new ProductRegistrationDefinition();
		registrationDefinition.setProduct(product);
		ProductRegistration productRegistration = new ProductRegistration();
		productRegistration.setRegistrationDefinition(registrationDefinition);
		ProfileRegistrationDto dto = new ProfileRegistrationDto(productRegistration, null);

		expect(mockCachingProfileRegistrationDtoSource.getProfileRegistrationDtos(customer, mockHttpServletRequest.getSession())).andReturn(Arrays.asList(dto));

		SessionHelper.setUrlSkin(mockHttpServletRequest, fajarSkin());

		replay(mockWebApplicationContext, mockUrlMapService, mockCachingProfileRegistrationDtoSource);

		tag.setVar(VAR);
		tag.doStartTag();

		List<UrlSkin> urlSkins = (List<UrlSkin>) mockHttpServletRequest.getAttribute(VAR);
		assertNotNull(urlSkins);
		assertThat(urlSkins.size(), equalTo(2));
		assertThat(urlSkins.get(0).getUrl(), equalTo("http://www.elt.oup.com"));
		assertThat(urlSkins.get(1).getUrl(), equalTo("http://oxfordfajar.com.my"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldNotGenerateDuplicateLinksWhenCustomerLoggedInWithRegisteredProductAndOnSkinnedPageOfSameProduct() throws Exception {
		expect(mockWebApplicationContext.getBean("requestDataValueProcessor", RequestDataValueProcessor.class)).andReturn(null);
		expect(mockWebApplicationContext.getServletContext()).andReturn(null);
		expect(mockWebApplicationContext.getBean("urlMapService", UrlMapService.class)).andReturn(mockUrlMapService);
		expect(mockWebApplicationContext.getBean(CachingProfileRegistrationDtoSource.class)).andReturn(mockCachingProfileRegistrationDtoSource);
		expect(mockWebApplicationContext.getBean("webCustomerTimeoutConfig", CustomerTimeoutConfig.class)).andReturn(null);
		expect(mockUrlMapService.getUrlMap()).andReturn(urlMap);

		Customer customer = new Customer();
		mockHttpServletRequest.getSession().setAttribute("CUSTOMER", customer);

		RegisterableProduct product = new RegisterableProduct();
		product.setHomePage("http://www.elt.oup.com");
		ProductRegistrationDefinition registrationDefinition = new ProductRegistrationDefinition();
		registrationDefinition.setProduct(product);
		ProductRegistration productRegistration = new ProductRegistration();
		productRegistration.setRegistrationDefinition(registrationDefinition);
		ProfileRegistrationDto dto = new ProfileRegistrationDto(productRegistration, null);

		expect(mockCachingProfileRegistrationDtoSource.getProfileRegistrationDtos(customer, mockHttpServletRequest.getSession())).andReturn(Arrays.asList(dto));

		SessionHelper.setUrlSkin(mockHttpServletRequest, eltSkin());

		replay(mockWebApplicationContext, mockUrlMapService, mockCachingProfileRegistrationDtoSource);

		tag.setVar(VAR);
		tag.doStartTag();

		List<UrlSkin> urlSkins = (List<UrlSkin>) mockHttpServletRequest.getAttribute(VAR);
		assertNotNull(urlSkins);
		assertThat(urlSkins.size(), equalTo(1));
		assertThat(urlSkins.get(0).getUrl(), equalTo("http://www.elt.oup.com"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldNotGenerateLinksForNonPrimarySites() throws Exception {
		UrlSkin anotherFajarSkin = new UrlSkin();
		anotherFajarSkin.setSiteName("Oxford Fajar Secondary");
		anotherFajarSkin.setUrl("http://somewhere.else/");
		anotherFajarSkin.setPrimarySite(false);
		urlMap.put("http://somewhere.else/", anotherFajarSkin);
		
		expect(mockWebApplicationContext.getBean("requestDataValueProcessor", RequestDataValueProcessor.class)).andReturn(null);
		expect(mockWebApplicationContext.getServletContext()).andReturn(null);
		expect(mockWebApplicationContext.getBean("urlMapService", UrlMapService.class)).andReturn(mockUrlMapService);
		expect(mockUrlMapService.getUrlMap()).andReturn(urlMap);

		replay(mockWebApplicationContext, mockUrlMapService);

		tag.setVar(VAR);
		tag.doStartTag();

		List<UrlSkin> urlSkins = (List<UrlSkin>) mockHttpServletRequest.getAttribute(VAR);
		assertNotNull(urlSkins);
		assertThat(urlSkins.size(), equalTo(2));
		assertThat(urlSkins.get(0).getUrl(), equalTo("http://www.elt.oup.com"));
		assertThat(urlSkins.get(1).getUrl(), equalTo("http://oxfordfajar.com.my"));
	}

	private UrlSkin fajarSkin() {
		UrlSkin fajarSkin = new UrlSkin();
		fajarSkin.setSiteName("Oxford Fajar");
		fajarSkin.setUrl("http://oxfordfajar.com.my");
		fajarSkin.setPrimarySite(true);
		return fajarSkin;
	}

	private UrlSkin eltSkin() {
		UrlSkin eltSkin = new UrlSkin();
		eltSkin.setSiteName("Oxford ELT");
		eltSkin.setUrl("http://www.elt.oup.com");
		eltSkin.setPrimarySite(true);
		return eltSkin;
	}
}
