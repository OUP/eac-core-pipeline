package com.oup.eac.domain;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

/**
 * The page definition.
 * 
 * @author harlandd
 */
@Entity
@DiscriminatorColumn(name = "page_definition_type", discriminatorType = DiscriminatorType.STRING)
/*@org.hibernate.annotations.Table(
        indexes = { 
                @Index(name = "page_definition_idx", columnNames = { "division_id", "page_definition_type" }) },
        appliesTo = "page_definition")*/
public abstract class PageDefinition extends BaseDomainObject {

    public static enum PageDefinitionType {
        ACCOUNT_PAGE_DEFINITION,
        PRODUCT_PAGE_DEFINITION;
    }

    private String name;
    
    private String title;
    
    private String preamble;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pageDefinition", cascade=CascadeType.ALL, orphanRemoval = true)
    @Sort(type = SortType.NATURAL)
    private Set<PageComponent> pageComponents = new TreeSet<PageComponent>();
    
    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "division_id", nullable = false)*/
    @Transient
    private Division division;
    
    @Index (name = "division_erights_id_idx")
	private Integer divisionErightsId;	
    
	public abstract PageDefinitionType getPageDefinitionType();
		
    /**
     * @return the name
     */
	public String getName() {
        return name;
    }
	
    public Division getDivision() {
		return division;
	}

	public void setDivision(Division division) {
		this.division = division;
	}

	/**
     * @param title
     *            the title to set
     */
	public void setTitle(final String title) {
        this.title = title;
    }
    
    /**
     * @return the title
     */
	public String getTitle() {
        return title;
    }

    /**
     * @param name
     *            the name to set
     */
	public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the components.
     */
	public Set<PageComponent> getPageComponents() {
        return pageComponents;
    }

    /**
	 * @return the preamble
	 */
	public String getPreamble() {
		return preamble;
	}

	/**
	 * @param preamble the preamble to set
	 */
	public void setPreamble(final String preamble) {
		this.preamble = preamble;
	}

	/**
     * @param pageComponents
     *            the components
     */
	public void setPageComponents(final Set<PageComponent> pageComponents) {
        this.pageComponents = pageComponents;
    }
    
	public void addPageComponent(final PageComponent pageComponent) {
    	this.pageComponents.add(pageComponent);
    	pageComponent.setPageDefinition(this);
    }
    
	
	
	public Integer getDivisionErightsId() {
		return divisionErightsId;
	}

	public void setDivisionErightsId(Integer divisionErightsId) {
		this.divisionErightsId = divisionErightsId;
	}

	public void removePageComponent(final String id) {
		for (Iterator<PageComponent> iter = pageComponents.iterator(); iter.hasNext();) {
			PageComponent pageComponent = iter.next();
			if (pageComponent.getId().equals(id)) {
				iter.remove();
			}
		}
	}
}
