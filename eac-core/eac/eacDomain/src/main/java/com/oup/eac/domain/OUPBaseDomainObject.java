package com.oup.eac.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

/**
 * Abstract class for all domain classes to extend.
 * 
 * @author harlandd
 */
@MappedSuperclass
public abstract class OUPBaseDomainObject implements Serializable {

    @Id
    /*@GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")*/
    @Column(nullable = false)
    private String id;

    @Version
    @Column(name = "OBJ_VERSION", nullable = false)
    private long version;

    /**
	 * 
	 */
	public OUPBaseDomainObject() {
		super();
	}

	/**
	 * @param id
	 * @param version
	 */
	public OUPBaseDomainObject(final String id, final long version) {
		super();
		this.id = id;
		this.version = version;
	}

	/**
     * 
     * @return the id
     */
	public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *            the id
     */
	public void setId(final String id) {
        this.id = id;
    }

    /**
     * 
     * @return the version
     */
	public long getVersion() {
        return version;
    }

    /**
     * 
     * @param version
     *            the version
     */
	public void setVersion(final long version) {
        this.version = version;
    }

}
