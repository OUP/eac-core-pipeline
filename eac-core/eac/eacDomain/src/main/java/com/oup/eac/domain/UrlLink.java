package com.oup.eac.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author harlandd Class representing an href anchor link
 */
@Entity
@DiscriminatorValue("URLLINK")
public class UrlLink extends Tag {

    private String url;

    private boolean newWindow;

    @Override
	public TagType getTagType() {
        return TagType.URLLINK;
    }

    /**
     * @return the url
     */
	public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            the url
     */
	public void setUrl(final String url) {
        this.url = url;
    }

    /**
     * @return open the link in a new window
     */
	public boolean isNewWindow() {
        return newWindow;
    }

    /**
     * @param newWindow
     *            opne the link in a new window
     */
	public void setNewWindow(final boolean newWindow) {
        this.newWindow = newWindow;
    }

}
