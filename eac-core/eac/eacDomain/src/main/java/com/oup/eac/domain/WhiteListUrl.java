package com.oup.eac.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="white_list_urls")
public class WhiteListUrl extends BaseDomainObject {
	
	@Column(nullable=false, unique=false)
    private String url;
	
	@Column(nullable=false, unique=false)
    private Date date_created;
	
	@Column(nullable=false, unique=false)
    private Date date_updated;
	
	@Column(nullable=true, unique=false)
    private Date date_deleted;
	
	
	public final String getUrl() {
		return url;
	}

	public final void setUrl(String url) {
		this.url = url;
	}

	public final Date getDate_created() {
		return date_created;
	}

	public final void setDate_created(Date date_created) {
		this.date_created = date_created;
	}

	public final Date getDate_updated() {
		return date_updated;
	}

	public final void setDate_updated(Date date_updated) {
		this.date_updated = date_updated;
	}

	public final Date getDate_deleted() {
		return date_deleted;
	}

	public final void setDate_deleted(Date date_deleted) {
		this.date_deleted = date_deleted;
	}

	

}
