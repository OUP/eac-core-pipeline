package com.oup.eac.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author harlandd Class a select tag
 */
@Entity
@DiscriminatorValue("MULTISELECT")
public class MultiSelect extends OptionsTag {

    @Override
	public TagType getTagType() {
        return TagType.MULTISELECT;
    }

}
