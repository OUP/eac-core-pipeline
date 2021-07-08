package com.oup.eac.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author harlandd Class a checkbox tag
 */
@Entity
@DiscriminatorValue("CHECKBOX")
public class Checkbox extends Tag {

    private Boolean value = Boolean.TRUE;

    @Override
	public TagType getTagType() {
        return TagType.CHECKBOX;
    }

    /**
     * 
     * @return the boolean value
     */
	public Boolean getValue() {
        return value;
    }

    /**
     * 
     * @param value
     *            the value
     */
	public void setValue(final Boolean value) {
        this.value = value;
    }

}
