package com.oup.eac.web.tags;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import com.oup.eac.common.utils.UrlMap;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.UrlSkin;
import com.oup.eac.dto.profile.ProfileRegistrationDto;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.UrlMapService;
import com.oup.eac.web.controllers.helpers.SessionHelper;
import com.oup.eac.web.profile.CachingProfileRegistrationDtoSource;

public class HomeMenuLinkGeneratorTag extends RequestContextAwareTag {

	private static final Logger LOG = Logger.getLogger(HomeMenuLinkGeneratorTag.class);

	private static final String BEAN_NAME_URL_MAP_SERVICE = "urlMapService";
	private static final String BY_TYPE = null;

	private String var;

	@Override
	protected int doStartTagInternal() throws Exception {

		List<UrlSkin> urlSkins = new ArrayList<UrlSkin>();

		try {
			urlSkins.addAll(getUniqueUrlSkinsForHomeMenu());
			removeNonPrimarySites(urlSkins);
			sortBySiteName(urlSkins);
		} catch (Exception e) {
			// An error getting the links for the home menu should
			// not cause the page to fall over. Log it and carry on.
			LOG.error("Error getting list of links for home menu: " + e, e);
		}

		setInRequest(urlSkins);

		return SKIP_BODY;
	}

	private Set<UrlSkin> getUniqueUrlSkinsForHomeMenu() throws MalformedURLException, ServiceLayerException {
		Set<UrlSkin> urlSkins = new HashSet<UrlSkin>();
		UrlMap<UrlSkin> urlMap = getService(BEAN_NAME_URL_MAP_SERVICE, UrlMapService.class).getUrlMap();
		Customer customer = SessionHelper.getCustomer((HttpServletRequest) pageContext.getRequest());
		UrlSkin currentSkin = SessionHelper.getUrlSkin((HttpServletRequest) pageContext.getRequest());

		if (customer != null) {
			Set<String> productHomePages = getHomePagesForProductRegistrations(customer);
			if (currentSkin != null) {
				urlSkins.add(currentSkin);
				urlSkins.addAll(getUrlSkinsForProductHomePages(productHomePages, urlMap));
			} else {
				if (productHomePages.size() > 0) {
					urlSkins.addAll(getUrlSkinsForProductHomePages(productHomePages, urlMap));
				} else {
					urlSkins.addAll(urlMap.values());
				}
			}
		} else {
			if (currentSkin != null) {
				urlSkins.add(currentSkin);
			} else {
				urlSkins.addAll(urlMap.values());
			}
		}

		return urlSkins;
	}

	private <T> T getService(String serviceName, Class<T> serviceType) {
		ApplicationContext appContext = getRequestContext().getWebApplicationContext();
		T service;
		if (serviceName != null) {
			service = appContext.getBean(serviceName, serviceType);
		} else {
			service = appContext.getBean(serviceType);
		}
		return service;
	}

	private Set<String> getHomePagesForProductRegistrations(Customer customer) throws ServiceLayerException {
		Set<String> productHomePages = new HashSet<String>();
		CachingProfileRegistrationDtoSource profileRegistrationDtoSource = getService(BY_TYPE, CachingProfileRegistrationDtoSource.class);
		List<ProfileRegistrationDto> profileRegistrations = profileRegistrationDtoSource.getProfileRegistrationDtos(customer, pageContext.getSession());
		for (ProfileRegistrationDto profileRegistration : profileRegistrations) {
			productHomePages.add(profileRegistration.getRegistration().getRegistrationDefinition().getProduct().getHomePage());
		}
		return productHomePages;
	}

	private Set<UrlSkin> getUrlSkinsForProductHomePages(Set<String> productHomePages, UrlMap<UrlSkin> urlMap) throws MalformedURLException {
		Set<UrlSkin> urlSkins = new HashSet<UrlSkin>();
		for (String productHomePage : productHomePages) {
			UrlSkin urlSkin = urlMap.get(productHomePage);
			if (urlSkin != null) {
				urlSkins.add(urlSkin);
			}
		}
		return urlSkins;
	}
	
	private void removeNonPrimarySites(final List<UrlSkin> urlSkins) {
		for (Iterator<UrlSkin> iter = urlSkins.iterator(); iter.hasNext();) {
			UrlSkin urlSkin = iter.next();
			if (!urlSkin.isPrimarySite()) {
				iter.remove();
			}
		}
		
	}

	private void sortBySiteName(List<UrlSkin> urlSkins) throws MalformedURLException {
		Collections.sort(urlSkins, new Comparator<UrlSkin>() {
			@Override
			public int compare(UrlSkin urlSkin1, UrlSkin urlSkin2) {
				return urlSkin1.getSiteName().compareTo(urlSkin2.getSiteName());
			}
		});
	}

	private void setInRequest(List<UrlSkin> urlSkins) {
		((HttpServletRequest) pageContext.getRequest()).setAttribute(var, urlSkins);
	}

	public void setVar(String var) {
		this.var = var;
	}

}
