package com.oup.eac.domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

/**
 * @author harlandd A component of elements.
 */
@Entity
public class Component extends BaseDomainObject {

	private String name;
    private String labelKey;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "component", orphanRemoval = true)
    @Sort(type = SortType.NATURAL)
    private Set<Field> fields = new TreeSet<Field>();
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "component")
    private Set<PageComponent> pageComponents = new HashSet<PageComponent>();
    
	public Component() {
		super();
	}

	public Component(final String id, final long version, final String name, final String labelKey, final Set<Field> fields) {
		super(id, version);
		this.name = name;
		this.labelKey = labelKey;
		this.fields = fields;
	}
	
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the labelKey
	 */
	public String getLabelKey() {
		return labelKey;
	}

	/**
	 * @param labelKey the labelKey to set
	 */
	public void setLabelKey(final String labelKey) {
		this.labelKey = labelKey;
	}

	/**
	 * @return the fields
	 */
	public Set<Field> getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(final Set<Field> fields) {
		this.fields = fields;
	}
	
	/**
	 * Compoent add field helper method
	 * 
	 * @param field
	 */
	public void addField(final Field field) {
		this.fields.add(field);
		field.setComponent(this);
	}
	
	public void removeField(final Field field) {
		this.fields.remove(field);
	}
	
	public void removeField(final String id) {
		for (Iterator<Field> iter = fields.iterator(); iter.hasNext();) {
			Field field = iter.next();
			if (field.getId().equals(id)) {
				iter.remove();
			}
		}
	}
	
	/**
	 * @return the pageComponents
	 */
	public Set<PageComponent> getPageComponents() {
		return pageComponents;
	}


	/**
	 * @param pageComponents the pageComponents to set
	 */
	public void setPageComponents(final Set<PageComponent> pageComponents) {
		this.pageComponents = pageComponents;
	}


	public static Component valueOf(final String id, final long version, final String name, final String labelKey, final Set<Field> fields) {
		Component component = new Component(id, version, name, labelKey, fields);
		return component;
	}
}
