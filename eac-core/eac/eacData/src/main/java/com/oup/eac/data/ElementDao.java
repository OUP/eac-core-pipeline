package com.oup.eac.data;

import java.util.List;
import java.util.Locale;

import com.oup.eac.domain.Element;

/**
 * Element DAO interface
 */
public interface ElementDao extends BaseDao<Element, String> {

	Element getElementByIdWithFullyFetchedQuestion(final String id);
	
	List<Locale> getOrderedElementCountryRestrictionLocales();
}
