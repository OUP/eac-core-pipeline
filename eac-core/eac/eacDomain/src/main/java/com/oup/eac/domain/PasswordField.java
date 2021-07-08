package com.oup.eac.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author harlandd Class a password field tag
 */
@Entity
@DiscriminatorValue("PASSWORDFIELD")
public class PasswordField extends Tag {

    @Override
	public TagType getTagType() {
        return TagType.PASSWORDFIELD;
    }

}
