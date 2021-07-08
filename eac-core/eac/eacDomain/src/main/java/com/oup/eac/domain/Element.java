package com.oup.eac.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.commons.lang.StringUtils;

/**
 * @author harlandd
 * Element representing each element in a page.
 */
@Entity
public class Element extends BaseDomainObject {
	
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id")
	private Question question;
	
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "element")
    private Set<Field> fields = new HashSet<Field>();

    private String titleText;

    private String regularExpression;
    
    private String helpText;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "element", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Tag> tags = new HashSet<Tag>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "element", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<ElementCountryRestriction> elementCountryRestrictions = new HashSet<ElementCountryRestriction>();
    
    public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the elementCountryRestrictions
	 */
	public Set<ElementCountryRestriction> getElementCountryRestrictions() {
		return elementCountryRestrictions;
	}
	
	public ElementCountryRestriction getElementCountryRestriction(final String id) {
		ElementCountryRestriction restriction = null;
		for (ElementCountryRestriction elementCountryRestriction : elementCountryRestrictions) {
			if (elementCountryRestriction.getId().equals(id)) {
				restriction = elementCountryRestriction;
				break;
			}
		}
		return restriction;
	}
	
	public List<ElementCountryRestriction> getElementCountryRestrictionsAsList() {
		List<ElementCountryRestriction> sortedElementCountryRestrictions = new ArrayList<ElementCountryRestriction>(elementCountryRestrictions);
		Collections.sort(sortedElementCountryRestrictions, new Comparator<ElementCountryRestriction>() {
			@Override
			public int compare(final ElementCountryRestriction o1, final ElementCountryRestriction o2) {
				return o1.getLocale().getDisplayName().compareTo(o2.getLocale().getDisplayName());
			}
		});
		return sortedElementCountryRestrictions;
	}

	/**
	 * @param elementCountryRestrictions the elementCountryRestrictions to set
	 */
	public void setElementCountryRestrictions(final Set<ElementCountryRestriction> elementCountryRestrictions) {
		this.elementCountryRestrictions = elementCountryRestrictions;
	}
	
	public void addElementCountryRestriction(final ElementCountryRestriction restriction) {
		restriction.setElement(this);
		elementCountryRestrictions.add(restriction);
	}
	
    public void removeElementCountryRestriction(final String id) {
    	for (Iterator<ElementCountryRestriction> iter = elementCountryRestrictions.iterator(); iter.hasNext();) {
    		ElementCountryRestriction restriction = iter.next();
    		if (StringUtils.equals(id, restriction.getId())) {
    			iter.remove();
    		}
    	}
    }

	/**
     * 
     * @return the first tag
     */
	public Tag getTag() {
        for (Tag tag : tags) {
            return tag;
        }
        return null;
    }

    /**
	 * @return the question
	 */
	public Question getQuestion() {
		return question;
	}

	/**
	 * @param question the question to set
	 */
	public void setQuestion(final Question question) {
		this.question = question;
	}

	/**
     * 
     * @return the tags
     */
	public Set<Tag> getTags() {
        return tags;
    }

    /**
     * 
     * @param tags
     *            the tags
     */
	public void setTags(final Set<Tag> tags) {
        this.tags = tags;
    }

    /**
     * 
     * @return the regular expression
     */
	public String getRegularExpression() {
        return regularExpression;
    }

    /**
     * 
     * @param regularExpression
     *            the regular expression
     */
	public void setRegularExpression(final String regularExpression) {
        this.regularExpression = regularExpression;
    }

    /**
	 * @return the fields
	 */
	public Set<Field> getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(final Set<Field> fields) {
		this.fields = fields;
	}
	
	public void addField(final Field field) {
		fields.add(field);
		field.setElement(this);
	}
	
	public void addTag(final Tag tag) {
		if(this.tags.size() > 0) {
			throw new AssertionError("One element cannot have more than one tag.");
		}
		this.tags.add(tag);
		tag.setElement(this);
	}

	/**
     * @return the titleText
     */
	public String getTitleText() {
        return titleText;
    }

    /**
     * @param titleText
     *            the titleText to set
     */
	public void setTitleText(final String titleText) {
        this.titleText = titleText;
    }

    /**
     * @return the helpText
     */
	public String getHelpText() {
        return helpText;
    }

    /**
     * @param helpText the helpText to set
     */
	public void setHelpText(final String helpText) {
        this.helpText = helpText;
    }
    
    /**
	 * Checks that the supplied locale matches the locale in one of this Element's country restrictions.<br>
	 * The list of rules is as follows.<br>
	 * 
	 * <pre>
	 * 
	 * 	User Locale  | Element Country Restriction
	 * 	--------------------------------------
	 * 	             | ja_JP  |  ko_KR
	 * 	            
	 * 	ja           |   Y    |   N
	 * 	ja_JP        |   Y    |   N
	 * 	ja_JP_JP     |   Y    |   N
	 * 	en_JP        |   Y    |   N
	 * 	ja_GB        |   N    |   N
	 * 	en           |   N    |   N
	 * 	en_GB        |   N    |   N
	 * 	ko           |   N    |   Y
	 * 	ko_KR        |   N    |   Y
	 * 	en_KR        |   N    |   Y
	 * 
	 * </pre>
	 * 
	 * @param locale
	 *            the supplied locale
	 * @return true if there's at least one match, false otherwise.
	 */
	public boolean isValidForLocale(final Locale locale) {
		if (elementCountryRestrictions == null) {
			return false;
		}
		String country = locale.getCountry();
		if (StringUtils.isNotBlank(country)) {
			return isValidForCountry(country);
		}
		return isValidForLanguage(locale.getLanguage());
	}
	
	private boolean isValidForCountry(String country) {
		boolean isValid = false;
		for (ElementCountryRestriction restriction : elementCountryRestrictions) {
			Locale restrictionLocale = restriction.getLocale();
			if (restrictionLocale.getCountry().equals(country)) {
				isValid = true;
				break;
			}
		}
		return isValid;
	}
	
	private boolean isValidForLanguage(String language) {
		boolean isValid = false;
		for (ElementCountryRestriction restriction : elementCountryRestrictions) {
			Locale restrictionLocale = restriction.getLocale();
			if (restrictionLocale.getLanguage().equals(language)) {
				isValid = true;
				break;
			}
		}
		return isValid;
	}
    
	public boolean isLocaleRestricted() {
    	return elementCountryRestrictions.size() > 0;
    }
    
	public boolean isNotLocaleRestricted() {
    	return !isLocaleRestricted();
    }

}
