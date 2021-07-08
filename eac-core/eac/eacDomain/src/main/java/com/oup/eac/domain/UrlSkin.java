package com.oup.eac.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

@Entity
@Table(name="url_skin")
public class UrlSkin extends BaseDomainObject implements UrlSkinInfo {

    @Column(nullable=false, unique=true)
    @Index(name = "url_skin_idx")
    private String url;

    @Column(nullable=false)
    private String skinPath;
    
    @Column(nullable=true)
    private String siteName;
  
    @Column(nullable=true)
    private String contactPath;
    
    @Column(nullable=true, name="customiser_bean_name")
    private String urlCustomiserBean;
    
    private boolean primarySite;
    
    @Override
	public String getSkinPath() {
        return skinPath;
    }

    public void setSkinPath(String skinPath) {
        this.skinPath = skinPath;
    }

	@Override
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

    @Override
	public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    @Override
	public String getContactPath() {
        return contactPath;
    }

    public void setContactPath(String contactPath) {
        this.contactPath = contactPath;
    }
    
	public boolean isPrimarySite() {
		return primarySite;
	}

	public void setPrimarySite(boolean primarySite) {
		this.primarySite = primarySite;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((skinPath == null) ? 0 : skinPath.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UrlSkin other = (UrlSkin) obj;
		if (skinPath == null) {
			if (other.skinPath != null)
				return false;
		} else if (!skinPath.equals(other.skinPath))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
        if (urlCustomiserBean == null) {
            if (other.urlCustomiserBean != null)
                return false;
        } else if (!urlCustomiserBean.equals(other.urlCustomiserBean))
            return false;
		return true;
	}

	@Override
	public String toString() {
	    String msg = String.format("UrlSkin [url=%s, skinPath=%s, siteName=%s, contactPath=%s, urlCustomeriserBean=%s]",this.url, this.skinPath, this.siteName, this.contactPath, this.urlCustomiserBean); 
		return msg; 
	}

    public String getUrlCustomiserBean() {
        return urlCustomiserBean;
    }

    public void setUrlCustomiserBean(String urlCustomiserBean) {
        this.urlCustomiserBean = urlCustomiserBean;
    }
	
}
