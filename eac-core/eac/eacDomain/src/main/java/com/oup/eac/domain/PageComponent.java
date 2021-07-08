package com.oup.eac.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;

/**
 * The page component.
 * 
 * @author harlandd
 */
@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint( columnNames = 
		{"page_definition_id", "component_id"})
	}
)
@org.hibernate.annotations.Table(
        indexes = { 
                @Index(name = "page_component_definition_idx", columnNames = { "component_id", "page_definition_id" }),
                @Index(name = "page_component_sequence_idx", columnNames = { "sequence" })
                },
        appliesTo = "page_component")
public class PageComponent extends BaseDomainObject implements Comparable<PageComponent> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_definition_id", nullable = false)
    private PageDefinition pageDefinition;

    @Column(nullable = false)
    private int sequence;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id", nullable = false)
    private Component component;

    /**
     * @return the pageDefinition
     */
	public PageDefinition getPageDefinition() {
        return pageDefinition;
    }

    /**
     * @param pageDefinition
     *            the pageDefinition to set
     */
	public void setPageDefinition(final PageDefinition pageDefinition) {
        this.pageDefinition = pageDefinition;
    }

    /**
	 * @return the component
	 */
	public Component getComponent() {
		return component;
	}

	/**
	 * @param component the component to set
	 */
	public void setComponent(Component component) {
		this.component = component;
	}

	/**
     * @return the position
     */
	public int getSequence() {
        return sequence;
    }

    /**
     * @param sequence
     *            the position to set
     */
	public void setSequence(final int sequence) {
        this.sequence = sequence;
    }

    /**
     * @param pageComponent
     *            the pageComponent to compare
     * @return the result of the comparison
     */
    @Override
	public int compareTo(final PageComponent pageComponent) {
        if (this.getSequence() == pageComponent.getSequence()) {
            return 0;
        }
        if (this.getSequence() > pageComponent.getSequence()) {
            return 1;
        }
        return -1;
    }
}
