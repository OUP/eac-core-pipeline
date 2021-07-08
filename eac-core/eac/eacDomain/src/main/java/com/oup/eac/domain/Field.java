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
 * @author harlandd A component of elements.
 */
@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint( columnNames = 
		{"component_id", "element_id"})
	}
)
@org.hibernate.annotations.Table(
        indexes = { 
                @Index(name = "field_component_element_idx", columnNames = { "component_id", "element_id" }),
                @Index(name = "field_sequence_idx", columnNames = {"sequence" })
        },
        appliesTo = "field")
public class Field extends BaseDomainObject implements Comparable<Field> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id", nullable = false)
    private Component component;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "element_id", nullable = false)
    private Element element;
    
    private String defaultValue;
    
    @Column(nullable = false)
    private boolean required;

    @Column(nullable = false)
    private int sequence;

    /**
     * 
     * @return is required
     */
	public boolean isRequired() {
        return required;
    }

    /**
     * 
     * @param required
     *            is required
     */
	public void setRequired(final boolean required) {
        this.required = required;
    }
    
    /**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
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
	public void setComponent(final Component component) {
		this.component = component;
	}

    /**
     * @return the sequence
     */
	public int getSequence() {
        return sequence;
    }

    /**
     * @param sequence
     *            the sequence to set
     */
	public void setSequence(final int sequence) {
        this.sequence = sequence;
    }

    /**
     * @return the element
     */
	public Element getElement() {
        return element;
    }

    /**
     * @param element
     *            the element to set
     */
	public void setElement(final Element element) {
        this.element = element;
    }

    /**
     * @param component
     *            the component to compare
     * @return result of comparison
     */
    @Override
	public int compareTo(final Field field) {
        if (this.getSequence() == field.getSequence()) {
            return 0;
        }
        if (this.getSequence() > field.getSequence()) {
            return 1;
        }
        return -1;
    }

	@Override
	public String toString() {
		return "Field [component=" + component + ", element=" + element + ", defaultValue=" + defaultValue + ", required=" + required + ", sequence="
				+ sequence + "]";
	}

}
