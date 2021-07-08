package com.oup.eac.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

/**
 * @author harlandd OptionsTag abstract class for extending by tags that have options.
 */
@Entity
@DiscriminatorValue("OPTIONS_TAG")
public abstract class OptionsTag extends Tag {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tag", fetch = FetchType.EAGER, orphanRemoval = true)
    @Sort(type = SortType.NATURAL)
    private Set<TagOption> options = new TreeSet<TagOption>();

    /**
     * @return the tag options
     */
	public Set<TagOption> getOptions() {
        return options;
    }
	
	public void setOptions(final Set<TagOption> options) {
		this.options = options;
	}

	public List<TagOption> getOptionsAsList() {
		return new ArrayList<TagOption>(options);
	}

    /**
     * @param option
     *            the option tag to add.
     */
	public void addOption(final TagOption option) {
        options.add(option);
        option.setTag(this);
    }
	
	public void removeOption(final String id) {
		for (Iterator<TagOption> iter = options.iterator(); iter.hasNext();) {
			TagOption tagOption = iter.next();
    		if (StringUtils.equals(id, tagOption.getId())) {
    			iter.remove();
    		}
		}
	}
}
