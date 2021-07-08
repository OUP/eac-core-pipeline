package com.oup.eac.service;

import java.util.List;
import java.util.Locale;

import com.oup.eac.domain.Element;

public interface ElementService {

	Element getElementById(final String id);
	
	List<Element> getElementsOrderedByTitleText();
	
	List<Element> getElementsOrderedByName();
	
	List<Locale> getOrderedElementCountryRestrictionLocales();
	
	void saveElement(final Element element);
	
	void deleteElement(final Element element);
}
