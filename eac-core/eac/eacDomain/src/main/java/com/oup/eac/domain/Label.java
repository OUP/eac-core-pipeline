package com.oup.eac.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author harlandd Class representing a label tag
 */
@Entity
@DiscriminatorValue("LABEL")
public class Label extends Tag {

    @Override
	public TagType getTagType() {
        return TagType.LABEL;
    }

}
