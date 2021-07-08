package com.oup.eac.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;


public class ExternalSystem extends BaseDomainObject {

    private String name;

    private String description;
    
    private Set<ExternalSystemIdType> externalSystemIdTypes =  new HashSet<ExternalSystemIdType>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name != null){
            name = name.toLowerCase();
        }
        this.name =name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ExternalSystemIdType> getExternalSystemIdTypes() {
        return externalSystemIdTypes;
    }

    public void setExternalSystemIdTypes(Set<ExternalSystemIdType> externalSystemIdTypes) {
        this.externalSystemIdTypes = externalSystemIdTypes;
    }
    
}
