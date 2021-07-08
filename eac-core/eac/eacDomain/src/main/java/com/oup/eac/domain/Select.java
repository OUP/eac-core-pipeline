package com.oup.eac.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author harlandd Class a select tag
 */
@Entity
@DiscriminatorValue("SELECT")
public class Select extends OptionsTag {

    @Column(nullable = false)
    private boolean emptyOption;

    @Override
    public TagType getTagType() {
        return TagType.SELECT;
    }

    /**
     * @return is the empty option required
     */
	public boolean isEmptyOption() {
        return emptyOption;
    }

    /**
     * @param emptyOption
     *            is the empty option required
     */
	public void setEmptyOption(final boolean emptyOption) {
        this.emptyOption = emptyOption;
    }

}
