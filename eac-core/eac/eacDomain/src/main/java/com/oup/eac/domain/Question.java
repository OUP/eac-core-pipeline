package com.oup.eac.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.apache.commons.lang.StringUtils;

@Entity
public class Question extends BaseDomainObject {
	
	private String description;
	
	private boolean productSpecific;
	
	private String elementText;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
	private Set<Answer> answers = new HashSet<Answer>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
	private Set<Element> elements = new HashSet<Element>();
	
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval=true)
    private Set<ExportName> exportNames = new HashSet<ExportName>();
	
    /**
     * 
     * @return the text
     */
	public String getElementText() {
        return elementText;
    }

    /**
     * 
     * @param elementText
     *            the text
     */
	public void setElementText(final String elementText) {
        this.elementText = elementText;
    }

	/**
	 * @return the productSpecific
	 */
	public boolean isProductSpecific() {
		return productSpecific;
	}

	/**
	 * @param productSpecific the productSpecific to set
	 */
	public void setProductSpecific(final boolean productSpecific) {
		this.productSpecific = productSpecific;
	}

	/**
	 * @return the answers
	 */
	public Set<Answer> getAnswers() {
		return answers;
	}

	/**
	 * @param answers the answers to set
	 */
	public void setAnswers(final Set<Answer> answers) {
		this.answers = answers;
	}

	/**
	 * @return the exportName
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param exportName the exportName to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
    /**
     * @return the exportName
     */
    public String getExportName(ExportType exportType) {
        for(ExportName exportName : exportNames) {
            if(exportName.getExportType() == exportType) {
                return exportName.getName();
            }
        }
        return getDescription();
    }
    
    public List<ExportName> getExportNamesAsList() {
    	List<ExportName> sortedExportNames = new ArrayList<ExportName>(exportNames);
    	Collections.sort(sortedExportNames, new Comparator<ExportName>() {
			@Override
			public int compare(final ExportName o1, final ExportName o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
    	return sortedExportNames;
    }

    public void addExportName(final ExportName exportName) {
    	exportName.setQuestion(this);
    	exportNames.add(exportName);
    }
    
    public void removeExportName(final String id) {
    	for (Iterator<ExportName> iter = exportNames.iterator(); iter.hasNext();) {
    		ExportName exportName = iter.next();
    		if (StringUtils.equals(id, exportName.getId())) {
    			iter.remove();
    		}
    	}
    }

	/**
	 * @return the elements
	 */
	public Set<Element> getElements() {
		return elements;
	}

	/**
	 * @param elements the elements to set
	 */
	public void setElements(Set<Element> elements) {
		this.elements = elements;
	}
	
	public String getDisplayName() {
		StringBuilder builder = new StringBuilder();
		builder.append(elementText);
		builder.append(" ");
		builder.append(description);
		if (productSpecific) {
			builder.append(" ");
			builder.append("(ps)");
		}
		return builder.toString();
	}
	
}
