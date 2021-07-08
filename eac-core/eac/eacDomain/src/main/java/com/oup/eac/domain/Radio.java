package com.oup.eac.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author harlandd Class a radio tag
 */
@Entity
@DiscriminatorValue("RADIO")
public class Radio extends OptionsTag {

    @Override
	public TagType getTagType() {
        return TagType.RADIO;
    }
}
