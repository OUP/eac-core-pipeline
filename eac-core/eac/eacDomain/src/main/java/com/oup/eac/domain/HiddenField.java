package com.oup.eac.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("HIDDENFIELD")
public class HiddenField extends Tag{

	@Override
	public TagType getTagType() {
		return TagType.HIDDENFIELD;
	}

}
