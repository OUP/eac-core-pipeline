package com.oup.eac.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author harlandd Class a select tag
 */
@Entity
@DiscriminatorValue("TEXTFIELD")
public class TextField extends Tag {

    @Override
	public TagType getTagType() {
        return TagType.TEXTFIELD;
    }

}
