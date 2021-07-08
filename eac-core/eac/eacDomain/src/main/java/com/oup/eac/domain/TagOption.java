package com.oup.eac.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Index;

/**
 * @author harlandd Class representing an option for an Options tag
 */
@Entity
@org.hibernate.annotations.Table(
        indexes = { 
                @Index(name = "tag_option_sequence_idx", columnNames = { "sequence" })
                },
        appliesTo = "tag_option")
public class TagOption extends BaseDomainObject implements Comparable<TagOption> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    @Index (name = "tag_option_options_tag_idx")
    private OptionsTag tag;

    private String label;

    private String value;
    
    @Column(nullable = false)
    private int sequence;

    /**
     * Default constructor.
     */
    public TagOption() {
        super();
    }

    /**
     * Constructor accepting a label and a value.
     * 
     * @param label
     *            the label
     * @param value
     *            the value
     */
    public TagOption(final String label, final String value) {
        super();
        this.label = label;
        this.value = value;
    }

    /**
     * @return the label
     */
	public String getLabel() {
        return label;
    }

    /**
     * @param label
     *            the label
     */
	public void setLabel(final String label) {
        this.label = label;
    }

    /**
     * @return the value
     */
	public String getValue() {
        return value;
    }

    /**
     * @param value
     *            the value
     */
	public void setValue(final String value) {
        this.value = value;
    }

    /**
     * @return the tag
     */
	public OptionsTag getTag() {
        return tag;
    }

    /**
     * @param tag
     *            the tag
     */
	public void setTag(final OptionsTag tag) {
        this.tag = tag;
    }

    /**
     * @return the sequence
     */
	public int getSequence() {
        return sequence;
    }

    /**
     * @param sequence the sequence to set
     */
	public void setSequence(final int sequence) {
        this.sequence = sequence;
    }

    @Override
    public int compareTo(TagOption o) {
        if(this.getSequence() == o.getSequence()) {
            return 0;
        }
        if(this.getSequence() > o.getSequence()) {
            return 1;
        }
        return -1;
    }

}
