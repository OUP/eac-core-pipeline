package com.oup.eac.domain;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Index;

/**
 * @author harlandd Abstract Class representing a tag
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TAG_TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class Tag extends BaseDomainObject {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "element_id", nullable = false)
    @Index (name = "tag_element_idx")
    private Element element;

    public static enum TagType {
        TEXTFIELD, PASSWORDFIELD, CHECKBOX, SELECT, RADIO, LABEL, URLLINK, MULTISELECT, HIDDENFIELD;

        /**
         * @return the name
         */
        public String getName() {
            return name();
        }
    }

    /**
     * @return the tag type
     */
    public abstract TagType getTagType();

    /**
     * @return the element
     */
	public Element getElement() {
        return element;
    }

    /**
     * @param element
     *            the element
     */
	public void setElement(final Element element) {
        this.element = element;
    }

}
