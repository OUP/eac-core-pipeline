package com.oup.eac.admin.validators;

import java.util.Arrays;
import java.util.Collections;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.validation.Errors;

import com.oup.eac.admin.beans.SkinBean;
import com.oup.eac.domain.UrlSkin;

public class SkinBeanValidatorTest {

	private SkinBeanValidator validator = new SkinBeanValidator();
	
	@Test
	public void shouldFailWhenEmptySiteName() {
		String emptySiteName = "";
		UrlSkin urlSkin = new UrlSkin();
		urlSkin.setSiteName(emptySiteName);
		urlSkin.setSkinPath("/some/path");
		urlSkin.setUrl("http://localhost");
		urlSkin.setContactPath("http://localhost/contact/path");
		
		SkinBean skinBean = new SkinBean(Collections.<UrlSkin>emptyList());
		skinBean.setSelectedSkin(urlSkin);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedSkin.siteName", "error.skin.siteName.empty");

		EasyMock.replay(mockErrors);

		validator.validate(skinBean, mockErrors);

		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenEmptySiteUrl() {
		String emptySiteUrl = "";
		UrlSkin urlSkin = new UrlSkin();
		urlSkin.setSiteName("a site");
		urlSkin.setSkinPath("/some/path");
		urlSkin.setUrl(emptySiteUrl);
		urlSkin.setContactPath("http://localhost/contact/path");
		
		SkinBean skinBean = new SkinBean(Collections.<UrlSkin>emptyList());
		skinBean.setSelectedSkin(urlSkin);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedSkin.url", "error.skin.siteUrl.empty");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(skinBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenSiteUrlAlreadyExistsWhenAddingNew() {
		String siteUrl = "http://host/path/to/page.htm";
		UrlSkin urlSkin = new UrlSkin();
		urlSkin.setSiteName("a site");
		urlSkin.setSkinPath("/some/path");
		urlSkin.setUrl(siteUrl);
		urlSkin.setContactPath("http://localhost/contact/path");
		
		UrlSkin existingSkin = new UrlSkin();
		existingSkin.setId("12345");
		existingSkin.setSiteName("different site");
		existingSkin.setSkinPath("/different/path");
		existingSkin.setUrl(siteUrl);
		existingSkin.setContactPath("http://localhost/different/contact/path");
		
		SkinBean skinBean = new SkinBean(Arrays.asList(existingSkin));
		skinBean.setSelectedSkin(urlSkin);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedSkin.url", "error.skin.siteUrl.duplicate");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(skinBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenSiteUrlAlreadyExistsWhenUpdatingExisting() {
		String siteUrl = "http://host/path/to/page.htm";
		UrlSkin existingSkin1 = new UrlSkin();
		existingSkin1.setId("12345");
		existingSkin1.setSiteName("a site");
		existingSkin1.setSkinPath("/some/path");
		existingSkin1.setUrl(siteUrl);
		existingSkin1.setContactPath("http://localhost/contact/path");
		
		UrlSkin existingSkin2 = new UrlSkin();
		existingSkin2.setId("54321");
		existingSkin2.setSiteName("different site");
		existingSkin2.setSkinPath("/different/path");
		existingSkin2.setUrl(siteUrl);
		existingSkin2.setContactPath("http://localhost/different/contact/path");
		
		SkinBean skinBean = new SkinBean(Arrays.asList(existingSkin1, existingSkin2));
		skinBean.setSelectedSkin(existingSkin1);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedSkin.url", "error.skin.siteUrl.duplicate");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(skinBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenEmptyContactUrl() {
		String emptyContactUrl = "";
		UrlSkin urlSkin = new UrlSkin();
		urlSkin.setSiteName("a site");
		urlSkin.setSkinPath("/some/path");
		urlSkin.setUrl("http://localhost");
		urlSkin.setContactPath(emptyContactUrl);
		
		SkinBean skinBean = new SkinBean(Collections.<UrlSkin>emptyList());
		skinBean.setSelectedSkin(urlSkin);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedSkin.contactPath", "error.skin.contactUrl.empty");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(skinBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenEmptySkinUrl() {
		String emptySkinUrl = "";
		UrlSkin urlSkin = new UrlSkin();
		urlSkin.setSiteName("a site");
		urlSkin.setSkinPath(emptySkinUrl);
		urlSkin.setUrl("http://localhost");
		urlSkin.setContactPath("http://contacturl");
		
		SkinBean skinBean = new SkinBean(Collections.<UrlSkin>emptyList());
		skinBean.setSelectedSkin(urlSkin);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedSkin.skinPath", "error.skin.skinUrl.empty");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(skinBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenSiteURLMalformed() {
		String malformedSiteUrl = "foobar";
		UrlSkin urlSkin = new UrlSkin();
		urlSkin.setSiteName("a site");
		urlSkin.setSkinPath("/some/path");
		urlSkin.setUrl(malformedSiteUrl);
		urlSkin.setContactPath("http://localhost/contact/path");
		
		SkinBean skinBean = new SkinBean(Collections.<UrlSkin>emptyList());
		skinBean.setSelectedSkin(urlSkin);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedSkin.url", "error.skin.siteUrl.malformed");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(skinBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenContactUrlMalformed() {
		String malformedContactUrl = "foobar";
		UrlSkin urlSkin = new UrlSkin();
		urlSkin.setSiteName("a site");
		urlSkin.setSkinPath("/some/path");
		urlSkin.setUrl("http://localhost");
		urlSkin.setContactPath(malformedContactUrl);
		
		SkinBean skinBean = new SkinBean(Collections.<UrlSkin>emptyList());
		skinBean.setSelectedSkin(urlSkin);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedSkin.contactPath", "error.skin.contactUrl.malformed");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(skinBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldNotFailWhenContactUrlMailto() {
		String mailToContactUrl = "mailto:edtechsupport@oup.com";
		UrlSkin urlSkin = new UrlSkin();
		urlSkin.setSiteName("a site");
		urlSkin.setSkinPath("/some/path");
		urlSkin.setUrl("http://localhost");
		urlSkin.setContactPath(mailToContactUrl);
		
		SkinBean skinBean = new SkinBean(Collections.<UrlSkin>emptyList());
		skinBean.setSelectedSkin(urlSkin);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		
		EasyMock.replay(mockErrors);
		
		validator.validate(skinBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
}
