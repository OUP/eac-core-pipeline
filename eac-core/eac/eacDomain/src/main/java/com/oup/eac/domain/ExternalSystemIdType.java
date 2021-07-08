package com.oup.eac.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents the 'id type' of an external system.
 * New External System Id Types can be added without a code change.
 * @author David Hay
 *
 */

public class ExternalSystemIdType extends BaseDomainObject {

    private ExternalSystem externalSystem;
   
    private Set<ExternalId<?>> externalIds = new HashSet<ExternalId<?>>();
       
    private String name;
    
    private String description;

    private int prevHashCode=0;
    
    private ExternalSystemIdType oldExternalSystemIdtype;
    
    private boolean isDeletable ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name != null){
            name = name.toLowerCase();
        }
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExternalSystem getExternalSystem() {
        return externalSystem;
    }

    public void setExternalSystem(ExternalSystem externalSystem) {
        this.externalSystem = externalSystem;
    }

    public Set<ExternalId<?>> getExternalIds() {
        return externalIds;
    }

    public void setExternalIds(Set<ExternalId<?>> externalIds) {
        this.externalIds = externalIds;
    }

    public int getPrevHashCode() {
        return prevHashCode;
    }

    public void setPrevHashCode(int prevHashCode) {
        this.prevHashCode = prevHashCode;
    }
    
    @Override
    public int hashCode(){
        int newHashCode;
        if(this.name == null){
            this.name="";
        }
        if(this.description==null){
            this.description="";
        }
        newHashCode=this.name.hashCode()+this.description.hashCode();
        if (this.prevHashCode==0){            
            this.setPrevHashCode(newHashCode);
        }         
        return newHashCode;
    }
    
    @Override
    public boolean equals(Object o){
        boolean compare=false;
        ExternalSystemIdType externalSystemIdType = (ExternalSystemIdType)o;
        if(this.name.equals(externalSystemIdType.getName()) && this.externalSystem.getName().equals(externalSystemIdType.getExternalSystem().getName())){
            compare = true;
        }
        
        return compare;
        
    }

    public ExternalSystemIdType getOldExternalSystemIdtype() {
        return oldExternalSystemIdtype;
    }

    public void setOldExternalSystemIdtype(ExternalSystemIdType oldExternalSystemIdtype) {
        this.oldExternalSystemIdtype = oldExternalSystemIdtype;
    }

	public boolean isDeletable() {
		return isDeletable;
	}

	public void setDeletable(boolean isDeletable) {
		this.isDeletable = isDeletable;
	}
    
    
    
}
