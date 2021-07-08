package com.oup.eac.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ProgressBarElement extends BaseDomainObject implements Comparable<ProgressBarElement> {

	public static enum ElementType {
		INCOMPLETE_STEP,
		CURRENT_COMPLETED_STEP,
		INCOMPLETE_NO_STEP,
		COMPLETE_NO_STEP;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "progress_bar_id")
	private ProgressBar progressBar;
	
	private String label;
	
	private String defaultMessage;
	
	private int sequence;
	
	@Enumerated(EnumType.STRING)
	private ElementType elementType;

	/**
	 * 
	 */
	public ProgressBarElement() {
		super();
	}

	/**
	 * @param icon
	 * @param label
	 * @param elementType
	 */
	public ProgressBarElement(final String label, final String defaultMessage, final ElementType elementType, final int sequence) {
		super();
		this.label = label;
		this.defaultMessage = defaultMessage;
		this.elementType = elementType;
		this.sequence = sequence;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(final String label) {
		this.label = label;
	}
	
	public ElementType getElementType() {
		return elementType;
	}

	public void setElementType(final ElementType elementType) {
		this.elementType = elementType;
	}
	
	/**
	 * @return the defaultMessage
	 */
	public String getDefaultMessage() {
		return defaultMessage;
	}

	/**
	 * @param defaultMessage the defaultMessage to set
	 */
	public void setDefaultMessage(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	/**
	 * @return the progressBar
	 */
	public ProgressBar getProgressBar() {
		return progressBar;
	}

	/**
	 * @param progressBar the progressBar to set
	 */
	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
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
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
	public static ProgressBarElement valueOf(String label, String defaultMessage, ElementType elementType, int sequence) {
		return new ProgressBarElement(label, defaultMessage, elementType, sequence);
	}

	@Override
	public int compareTo(ProgressBarElement o) {
		if(this.getSequence() > o.getSequence()) {
			return 1;
		}
		if(this.getSequence() < o.getSequence()) {
			return -1;
		}
		return 0;
	}
	
}
